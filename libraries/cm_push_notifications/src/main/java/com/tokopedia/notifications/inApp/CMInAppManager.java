package com.tokopedia.notifications.inApp;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.RemoteMessage;
import com.tokopedia.abstraction.base.view.listener.FragmentLifecycleObserver;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.notifications.CMRouter;
import com.tokopedia.notifications.FragmentObserver;
import com.tokopedia.notifications.common.IrisAnalyticsEvents;
import com.tokopedia.notifications.inApp.ruleEngine.RulesManager;
import com.tokopedia.notifications.inApp.ruleEngine.interfaces.DataProvider;
import com.tokopedia.notifications.inApp.ruleEngine.rulesinterpreter.RuleInterpreterImpl;
import com.tokopedia.notifications.inApp.ruleEngine.storage.DataConsumerImpl;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp;
import com.tokopedia.notifications.inApp.viewEngine.CMActivityLifeCycle;
import com.tokopedia.notifications.inApp.viewEngine.CMInAppController;
import com.tokopedia.notifications.inApp.viewEngine.CmInAppBundleConvertor;
import com.tokopedia.notifications.inApp.viewEngine.CmInAppListener;
import com.tokopedia.notifications.inApp.viewEngine.ElementType;
import com.tokopedia.promotionstarget.domain.presenter.GratifCancellationExceptionType;
import com.tokopedia.promotionstarget.domain.presenter.GratifPopupIngoreType;
import com.tokopedia.promotionstarget.domain.presenter.GratificationPresenter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;

import kotlinx.coroutines.Job;
import timber.log.Timber;

import static com.tokopedia.notifications.inApp.ruleEngine.RulesUtil.Constants.RemoteConfig.KEY_CM_INAPP_END_TIME_INTERVAL;
import static com.tokopedia.notifications.inApp.viewEngine.CmInAppBundleConvertor.HOURS_24_IN_MILLIS;
import static com.tokopedia.notifications.inApp.viewEngine.CmInAppConstant.TYPE_GRATIF;
import static com.tokopedia.notifications.inApp.viewEngine.CmInAppConstant.TYPE_INTERSTITIAL;
import static com.tokopedia.notifications.inApp.viewEngine.CmInAppConstant.TYPE_INTERSTITIAL_IMAGE_ONLY;

/**
 * @author lalit.singh
 */
public class CMInAppManager implements CmInAppListener,
        DataProvider,
        CmActivityLifecycleHandler.CmActivityApplicationCallback,
        ShowInAppCallback {

    private static CMInAppManager inAppManager;
    private Application application;
    private CmInAppListener cmInAppListener;
    private final Object lock = new Object();
    private List<String> excludeScreenList;
    private Map<Integer, Job> mapOfGratifJobs = new ConcurrentHashMap<>();

    //map  - which will tell whether this activity has pop-up or not
    private WeakHashMap<Activity, Boolean> weakHashMap = new WeakHashMap<>();
    public CmActivityLifecycleHandler activityLifecycleHandler;

    //DialogHandlers
    private CmDialogHandler cmDialogHandler;
    private GratificationDialogHandler gratificationDialogHandler;

    static {
        inAppManager = new CMInAppManager();
    }

    public long getCmInAppEndTimeInterval() {
        return ((CMRouter) application.getApplicationContext()).getLongRemoteConfig(
                KEY_CM_INAPP_END_TIME_INTERVAL, HOURS_24_IN_MILLIS * 7);
    }

    public static CMInAppManager getInstance() {
        return inAppManager;
    }

    public void init(@NonNull Application application) {
        this.application = application;
        this.cmInAppListener = this;
        GratificationPresenter gratificationPresenter = new GratificationPresenter(application, weakHashMap);
        gratificationPresenter.setExceptionCallback(th -> {
            if (th != null) {
                th.printStackTrace();
                Timber.e(th);
            }
        });
        cmDialogHandler = new CmDialogHandler();
        gratificationDialogHandler = new GratificationDialogHandler(gratificationPresenter);
        PushIntentHandler pushIntentHandler = new PushIntentHandler(gratificationDialogHandler);
        BroadcastHandler broadcastHandler = new BroadcastHandler(pushIntentHandler);
        activityLifecycleHandler = new CmActivityLifecycleHandler(this,
                pushIntentHandler,
                broadcastHandler,
                this,
                weakHashMap);
        RulesManager.initRuleEngine(application, new RuleInterpreterImpl(), new DataConsumerImpl());
        initInAppManager();
    }

    public static CmInAppListener getCmInAppListener() {
        if (inAppManager == null) return null;
        if (inAppManager.cmInAppListener == null) return null;
        return inAppManager.cmInAppListener;
    }

    private void initInAppManager() {

        application.registerActivityLifecycleCallbacks(new CMActivityLifeCycle(activityLifecycleHandler));
        CmFragmentLifecycleHandler cmFragmentLifecycleHandler = new CmFragmentLifecycleHandler(this);
        FragmentObserver fragmentObserver = new FragmentObserver(cmFragmentLifecycleHandler);
        FragmentLifecycleObserver.INSTANCE.registerCallback(fragmentObserver);
    }

    private void showInAppNotification(String name, int entityHashCode) {
        if (excludeScreenList != null && excludeScreenList.contains(name))
            return;
        RulesManager.getInstance().checkValidity(
                name,
                0L,
                this,
                entityHashCode
        );
    }

    @Override
    public void notificationsDataResult(List<CMInApp> inAppDataList, int entityHashCode) {
        synchronized (lock) {
            if (canShowInApp(inAppDataList)) {
                CMInApp cmInApp = inAppDataList.get(0);
                sendEventInAppPrepared(cmInApp);
                showDialog(cmInApp, entityHashCode);
                if (!cmInApp.getType().equals(TYPE_GRATIF)) {
                    dataConsumed(cmInApp);
                }
            }
        }
    }

    private void sendEventInAppPrepared(CMInApp cmInApp) {
        sendPushEvent(cmInApp, IrisAnalyticsEvents.INAPP_PREREAD, null);
    }

    @Override
    public void sendEventInAppDelivered(CMInApp cmInApp) {
        sendPushEvent(cmInApp, IrisAnalyticsEvents.INAPP_DELIVERED, null);
    }

    private void showGratifDialog(CMInApp data, int entityHashCode) {

        Job job = gratificationDialogHandler.showOrganicDialog(activityLifecycleHandler.getCurrentWeakActivity(), data, new GratificationPresenter.AbstractGratifPopupCallback() {

            @Override
            public void onIgnored(int reason) {
                if (reason != GratifPopupIngoreType.DIALOG_ALREADY_ACTIVE) {
                    dataConsumed(data);
                }
                gratificationDialogHandler.showIgnoreToast(activityLifecycleHandler.getCurrentWeakActivity(), "organic", reason);
            }

            @Override
            public void onShow(@NotNull DialogInterface dialog) {
                dataConsumed(data);
            }

            @Override
            public void onExeption(@NotNull Exception ex) {
                dataConsumed(data);
                onCMInAppInflateException(data);
            }
        });
        mapOfGratifJobs.put(entityHashCode, job);
    }

    private void showDialog(CMInApp data, int entityHashCode) {
        WeakReference<Activity> currentActivity = activityLifecycleHandler.getCurrentWeakActivity();
        switch (data.getType()) {
            case TYPE_INTERSTITIAL_IMAGE_ONLY:
            case TYPE_INTERSTITIAL:
                showInterstitialDialog(currentActivity, data);
                break;
            case TYPE_GRATIF:
                showGratifDialog(data, entityHashCode);
                break;
            default:
                showLegacyDialog(currentActivity, data);
                break;
        }
    }

    private void showInterstitialDialog(WeakReference<Activity> currentActivity, CMInApp data) {
        cmDialogHandler.interstitialDialog(currentActivity, data, new CmDialogHandler.CmDialogHandlerCallback() {
            @Override
            public void onShow(@NotNull Activity activity) {
                weakHashMap.put(activity, true);
            }

            @Override
            public void onException(@NotNull Exception e, @NotNull CMInApp data) {
                onCMInAppInflateException(data);
            }
        });
    }

    private void showLegacyDialog(WeakReference<Activity> currentActivity, CMInApp data) {
        cmDialogHandler.showLegacyDialog(currentActivity, data, new CmDialogHandler.AbstractCmDialogHandlerCallback() {
            @Override
            public void onShow(@NotNull Activity activity) {
                weakHashMap.put(activity, true);
            }
        });
    }

    private boolean canShowInApp(List<CMInApp> inAppDataList) {
        return inAppDataList != null && inAppDataList.size() > 0;
    }

    @Override
    public boolean canShowDialog() {
        Activity activity = activityLifecycleHandler.getCurrentActivity();
        if (activity != null) {
            return !weakHashMap.containsKey(activity);
        }
        return false;
    }

    @Override
    public void cancelGratifJob(int entityHashCode, @GratifCancellationExceptionType String reason) {
        Job job = mapOfGratifJobs.get(entityHashCode);
        if (job != null && job.isActive()) {
            job.cancel(new CancellationException(reason));
            mapOfGratifJobs.remove(entityHashCode);
        }
    }

    private void dataConsumed(CMInApp inAppData) {
        RulesManager.getInstance().dataConsumed(inAppData.id);
        sendPushEvent(inAppData, IrisAnalyticsEvents.INAPP_RECEIVED, null);
    }

    public void dataConsumed(long id) {
        RulesManager.getInstance().dataConsumed(id);
    }

    public void viewDismissed(long id) {
        RulesManager.getInstance().viewDismissed(id);
    }

    public void handlePushPayload(RemoteMessage remoteMessage) {
        try {
            CMInApp cmInApp = CmInAppBundleConvertor.getCmInApp(remoteMessage);
            if (null != cmInApp) {
                if (application != null) {
                    sendEventInAppDelivered(cmInApp);
                    new CMInAppController().downloadImagesAndUpdateDB(application, cmInApp);
                }
            }
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    public void onCMinAppDismiss(CMInApp inApp) {
        RulesManager.getInstance().viewDismissed(inApp.id);
        Activity activity = activityLifecycleHandler.getCurrentActivity();
        if (activity != null)
            weakHashMap.remove(activity);
    }

    @Override
    public void onCMinAppInteraction(CMInApp cmInApp) {
        RulesManager.getInstance().interactedWithView(cmInApp.id);
    }

    @Override
    public void onCMInAppLinkClick(String appLink, CMInApp cmInApp, ElementType elementType) {
        Activity activity = activityLifecycleHandler.getCurrentActivity();
        if (activity != null) {
            activity.startActivity(RouteManager.getIntent(activity, appLink));
        }

        switch (elementType.getViewType()) {
            case ElementType.BUTTON:
                sendPushEvent(cmInApp, IrisAnalyticsEvents.INAPP_CLICKED, elementType.getElementId());
                break;
            case ElementType.MAIN:
            default:
                sendPushEvent(cmInApp, IrisAnalyticsEvents.INAPP_CLICKED, null);
        }
    }

    private void sendPushEvent(CMInApp cmInApp, String eventName, String elementId) {
        if (cmInApp == null) return;

        if (elementId != null) {
            IrisAnalyticsEvents.INSTANCE.sendPushEvent(application.getApplicationContext(), eventName, cmInApp, elementId);
        } else {
            IrisAnalyticsEvents.INSTANCE.sendPushEvent(application.getApplicationContext(), eventName, cmInApp);
        }
    }

    @Override
    public void onCMInAppClosed(CMInApp cmInApp) {
        sendPushEvent(cmInApp, IrisAnalyticsEvents.INAPP_DISMISSED, null);
    }

    @Override
    public void onCMInAppInflateException(CMInApp inApp) {
        RulesManager.getInstance().dataInflateError(inApp.id);
    }

    public void setExcludeScreenList(List<String> excludeScreenList) {
        this.excludeScreenList = excludeScreenList;
    }

    @Nullable
    @Override
    public Application getApplication() {
        return application;
    }

    @Override
    public void setApplication(@NotNull Application application) {
        this.application = application;
    }

    @Override
    public void showInAppForScreen(@NotNull String name, int entityHashCode) {
        showInAppNotification(name, entityHashCode);
    }

    public CmActivityLifecycleHandler getActivityLifecycleHandler() {
        return activityLifecycleHandler;
    }
}