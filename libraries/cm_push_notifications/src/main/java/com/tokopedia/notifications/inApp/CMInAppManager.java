package com.tokopedia.notifications.inApp;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.RemoteMessage;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.fragmentLifecycle.FragmentLifecycleObserver;
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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.WeakHashMap;

import timber.log.Timber;

import static com.tokopedia.notifications.inApp.ruleEngine.RulesUtil.Constants.RemoteConfig.KEY_CM_INAPP_END_TIME_INTERVAL;
import static com.tokopedia.notifications.inApp.viewEngine.CmInAppBundleConvertor.HOURS_24_IN_MILLIS;
import static com.tokopedia.notifications.inApp.viewEngine.CmInAppConstant.TYPE_INTERSTITIAL;
import static com.tokopedia.notifications.inApp.viewEngine.CmInAppConstant.TYPE_INTERSTITIAL_IMAGE_ONLY;

/**
 * @author lalit.singh
 */
public class CMInAppManager implements CmInAppListener,
        DataProvider,
        CmActivityLifecycleHandler.CmActivityApplicationCallback,
        ShowInAppCallback,
        SendPushContract {

    private static CMInAppManager inAppManager;
    private Application application;
    private CmInAppListener cmInAppListener;
    private final Object lock = new Object();
    private List<String> excludeScreenList;

    //map  - which will tell whether this activity has pop-up or not
    final WeakHashMap<Activity, Boolean> dialogIsShownMap = new WeakHashMap<>();
    public CmActivityLifecycleHandler activityLifecycleHandler;

    //DialogHandlers
    private CmDialogHandler cmDialogHandler;
    public CmDataConsumer cmDataConsumer;
    String TEMP_TAG = "GratifTag";
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
        cmDataConsumer = new CmDataConsumer(this);

        cmDialogHandler = new CmDialogHandler();
        PushIntentHandler pushIntentHandler = new PushIntentHandler();
        activityLifecycleHandler = new CmActivityLifecycleHandler(this,
                pushIntentHandler,
                this,
                dialogIsShownMap);
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
    public void notificationsDataResult(List<CMInApp> inAppDataList, int entityHashCode, String screenName) {
        synchronized (lock) {
            if (canShowInApp(inAppDataList)) {
                CMInApp cmInApp = inAppDataList.get(0);
                Timber.d(TEMP_TAG + " in-app found for screen name=" + screenName + ",type=" + cmInApp.type);
                sendEventInAppPrepared(cmInApp);
                if (checkForOtherSources(cmInApp, entityHashCode, screenName)) return;
                showDialog(cmInApp);
                dataConsumed(cmInApp);
            }else{
                Timber.d(TEMP_TAG + " NO in-app found for screen name=" + screenName);
            }
        }
    }

    private boolean checkForOtherSources(CMInApp cmInApp, int entityHashCode, String screenName) {
        if (CmEventListener.INSTANCE.getInAppPopupContractMap().containsKey(cmInApp.type)) {
            CmEventListener.INSTANCE.getInAppPopupContractMap().get(cmInApp.type).handleInAppPopup(cmInApp, entityHashCode, screenName);
            return true;
        }
        return false;
    }


    private void sendEventInAppPrepared(CMInApp cmInApp) {
        sendPushEvent(cmInApp, IrisAnalyticsEvents.INAPP_PREREAD, null);
    }

    @Override
    public void sendEventInAppDelivered(CMInApp cmInApp) {
        sendPushEvent(cmInApp, IrisAnalyticsEvents.INAPP_DELIVERED, null);
    }

    private void showDialog(CMInApp data) {
        WeakReference<Activity> currentActivity = activityLifecycleHandler.getCurrentWeakActivity();
        String type = data.type;
        if (!TextUtils.isEmpty(type)) {
            switch (data.getType()) {
                case TYPE_INTERSTITIAL_IMAGE_ONLY:
                case TYPE_INTERSTITIAL:
                    showInterstitialDialog(currentActivity, data);
                    break;
                default:
                    showLegacyDialog(currentActivity, data);
                    break;
            }
        }
    }

    private void showInterstitialDialog(WeakReference<Activity> currentActivity, CMInApp data) {
        cmDialogHandler.interstitialDialog(currentActivity, data, new CmDialogHandler.CmDialogHandlerCallback() {
            @Override
            public void onShow(@NotNull Activity activity) {
                dialogIsShownMap.put(activity, true);
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
                dialogIsShownMap.put(activity, true);
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
            return !dialogIsShownMap.containsKey(activity);
        }
        return false;
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
            dialogIsShownMap.remove(activity);
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

    @Override
    public void sendPushEvent(CMInApp cmInApp, String eventName, String elementId) {
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

    public WeakHashMap<Activity, Boolean> getDialogIsShownMap(){
        return dialogIsShownMap;
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