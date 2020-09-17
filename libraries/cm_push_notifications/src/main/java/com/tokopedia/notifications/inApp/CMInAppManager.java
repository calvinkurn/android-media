package com.tokopedia.notifications.inApp;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.google.firebase.messaging.RemoteMessage;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.notifications.CMRouter;
import com.tokopedia.notifications.R;
import com.tokopedia.notifications.common.CMConstant;
import com.tokopedia.notifications.common.IrisAnalyticsEvents;
import com.tokopedia.notifications.inApp.ruleEngine.RulesManager;
import com.tokopedia.notifications.inApp.ruleEngine.interfaces.DataProvider;
import com.tokopedia.notifications.inApp.ruleEngine.rulesinterpreter.RuleInterpreterImpl;
import com.tokopedia.notifications.inApp.ruleEngine.storage.DataConsumerImpl;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp;
import com.tokopedia.notifications.inApp.viewEngine.BannerView;
import com.tokopedia.notifications.inApp.viewEngine.CMActivityLifeCycle;
import com.tokopedia.notifications.inApp.viewEngine.CMInAppController;
import com.tokopedia.notifications.inApp.viewEngine.CmInAppBundleConvertor;
import com.tokopedia.notifications.inApp.viewEngine.CmInAppListener;
import com.tokopedia.notifications.inApp.viewEngine.ElementType;
import com.tokopedia.notifications.inApp.viewEngine.ViewEngine;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.NonNull;

import timber.log.Timber;

import static com.tokopedia.notifications.inApp.ruleEngine.RulesUtil.Constants.RemoteConfig.KEY_CM_INAPP_END_TIME_INTERVAL;
import static com.tokopedia.notifications.inApp.viewEngine.CmInAppBundleConvertor.HOURS_24_IN_MILLIS;
import static com.tokopedia.notifications.inApp.viewEngine.CmInAppConstant.TYPE_INTERSTITIAL;
import static com.tokopedia.notifications.inApp.viewEngine.CmInAppConstant.TYPE_INTERSTITIAL_IMAGE_ONLY;

/**
 * @author lalit.singh
 */
public class CMInAppManager implements CmInAppListener, DataProvider {

    private static CMInAppManager inAppManager;
    private Application application;
    private WeakReference<Activity> currentActivity;
    private CmInAppListener cmInAppListener;
    private final Object lock = new Object();
    private List<String> excludeScreenList;

    /*
    * This flag is used for validation of the dialog to be displayed.
    * This is useful for avoiding InApp dialog appearing more than once.
    * */
    private Boolean isDialogShowing = false;

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
        RulesManager.initRuleEngine(application, new RuleInterpreterImpl(), new DataConsumerImpl());
        initInAppManager();
    }

    public static CmInAppListener getCmInAppListener() {
        if (inAppManager == null) return null;
        if (inAppManager.cmInAppListener == null) return null;
        return inAppManager.cmInAppListener;
    }

    private void initInAppManager() {
        application.registerActivityLifecycleCallbacks(new CMActivityLifeCycle(this));
    }

    private void updateCurrentActivity(Activity activity) {
        currentActivity = new WeakReference<>(activity);
    }

    private void showInAppNotification() {
        if (excludeScreenList != null && excludeScreenList.contains(currentActivity.get().getClass().getName()))
            return;
        RulesManager.getInstance().checkValidity(
                currentActivity.get().getClass().getName(),
                0L,
                this
        );
    }

    @Override
    public void notificationsDataResult(List<CMInApp> inAppDataList) {
        synchronized (lock) {
            if (canShowInApp(inAppDataList)) {
                CMInApp cmInApp = inAppDataList.get(0);
                sendEventInAppPrepared(cmInApp);
                showDialog(cmInApp);
                dataConsumed(cmInApp);
                sendAmplificationEventInAppRead(cmInApp);
            }
        }
    }

    private void sendEventInAppPrepared(CMInApp cmInApp) {
        sendPushEvent(cmInApp, IrisAnalyticsEvents.INAPP_PREREAD, null);
    }

    private void sendAmplificationEventInAppRead(CMInApp cmInApp) {
        if (cmInApp.isAmplification()) {
            IrisAnalyticsEvents.INSTANCE.sendAmplificationInAppEvent(
                    application.getApplicationContext(),
                    IrisAnalyticsEvents.INAPP_READ,
                    cmInApp
            );
        }
    }

    @Override
    public void sendEventInAppDelivered(CMInApp cmInApp) {
        sendPushEvent(cmInApp, IrisAnalyticsEvents.INAPP_DELIVERED, null);
    }

    /**
     * legacy dialog
     * @param cmInApp
     */
    private void showLegacyDialog(CMInApp cmInApp) {
        Activity activity = getCurrentActivity();
        ViewEngine viewEngine = new ViewEngine(currentActivity.get());

        final View view = viewEngine.createView(cmInApp);
        if (view == null) return;

        View inAppViewPrev = activity.findViewById(R.id.mainContainer);
        //In-App view already present on Activity
        if (null != inAppViewPrev) return;

        FrameLayout root = (FrameLayout) activity.getWindow()
                .getDecorView()
                .findViewById(android.R.id.content)
                .getRootView();
        root.addView(view);

        // set flag if has dialog showing
        isDialogShowing = true;
    }

    /**
     * dialog for interstitial and interstitialImg
     * @param data
     */
    private void interstitialDialog(CMInApp data) {
        if (getCurrentActivity() == null) return;
        Activity activity = getCurrentActivity();
        try {
            // show interstitial banner
            BannerView.create(activity, data);

            // set flag if has dialog showing
            isDialogShowing = true;
        } catch (Exception e) {
            Timber.w( CMConstant.TimberTags.TAG + "exception;err=" + Log.getStackTraceString(e) + ";data=" + data);
            onCMInAppInflateException(data);
        }
    }

    private void showDialog(CMInApp data) {
        switch (data.getType()) {
            case TYPE_INTERSTITIAL_IMAGE_ONLY:
            case TYPE_INTERSTITIAL:
                interstitialDialog(data);
                break;
            default:
                showLegacyDialog(data);
                break;
        }
    }

    private boolean canShowInApp(List<CMInApp> inAppDataList) {
        return inAppDataList != null && inAppDataList.size() > 0;
    }

    private Activity getCurrentActivity() {
        if (currentActivity == null)
            return null;
        Activity activity = currentActivity.get();
        if (activity != null) {
            if (activity.isFinishing())
                return null;
            return activity;
        }
        return null;
    }

    public void onActivityStartedInternal(Activity activity) {
        if (application == null) application = activity.getApplication();
        updateCurrentActivity(activity);

        if (!isDialogShowing) {
            showInAppNotification();
        }
    }

    public void onActivityStopInternal(Activity activity) {
        if (currentActivity != null && currentActivity.get().getClass().
                getSimpleName().equalsIgnoreCase(activity.getClass().getSimpleName())) {
            currentActivity.clear();
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
                } else {
                    Timber.w("%svalidation;reason=application_null", CMConstant.TimberTags.TAG);
                }
            } else {
                Timber.w("%ssvalidation;reason=cmInApp_null", CMConstant.TimberTags.TAG);
            }
        } catch (Exception e) {
            Timber.w(CMConstant.TimberTags.TAG + "exception;err=" + Log.getStackTraceString(e) + ";data=" + remoteMessage.getData());
        }
    }

    @Override
    public void onCMinAppDismiss(CMInApp inApp) {
        RulesManager.getInstance().viewDismissed(inApp.id);
        isDialogShowing = false;
    }

    @Override
    public void onCMinAppInteraction(CMInApp cmInApp) {
        RulesManager.getInstance().interactedWithView(cmInApp.id);
    }

    @Override
    public void onCMInAppLinkClick(String appLink, CMInApp cmInApp, ElementType elementType) {
        if (getCurrentActivity() != null) {
            Activity activity = currentActivity.get();
            activity.startActivity(RouteManager.getIntent(activity, appLink));
        } else {
            Timber.w("%svalidation;reason=application_null_no_activity", CMConstant.TimberTags.TAG);
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
            IrisAnalyticsEvents.INSTANCE.sendInAppEvent(application.getApplicationContext(), eventName, cmInApp, elementId);
        } else {
            IrisAnalyticsEvents.INSTANCE.sendInAppEvent(application.getApplicationContext(), eventName, cmInApp);
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
}
