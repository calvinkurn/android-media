package com.tokopedia.notifications.inApp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.RemoteMessage;
import com.tokopedia.abstraction.base.view.fragment.lifecycle.FragmentLifecycleObserver;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.notifications.FragmentObserver;
import com.tokopedia.notifications.common.CMConstant;
import com.tokopedia.notifications.common.CMNotificationUtils;
import com.tokopedia.notifications.common.CMRemoteConfigUtils;
import com.tokopedia.notifications.common.IrisAnalyticsEvents;
import com.tokopedia.notifications.inApp.ruleEngine.RulesManager;
import com.tokopedia.notifications.inApp.ruleEngine.interfaces.DataConsumer;
import com.tokopedia.notifications.inApp.ruleEngine.interfaces.DataProvider;
import com.tokopedia.notifications.inApp.ruleEngine.rulesinterpreter.RuleInterpreterImpl;
import com.tokopedia.notifications.inApp.ruleEngine.storage.DataConsumerImpl;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp;
import com.tokopedia.notifications.inApp.viewEngine.CMActivityLifeCycle;
import com.tokopedia.notifications.inApp.viewEngine.CMInAppController;
import com.tokopedia.notifications.inApp.viewEngine.CmInAppBundleConvertor;
import com.tokopedia.notifications.inApp.viewEngine.CmInAppListener;
import com.tokopedia.notifications.inApp.viewEngine.ElementType;
import com.tokopedia.notifications.utils.NotificationCancelManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import timber.log.Timber;

import static com.tokopedia.notifications.inApp.ruleEngine.RulesUtil.Constants.RemoteConfig.KEY_CM_INAPP_END_TIME_INTERVAL;
import static com.tokopedia.notifications.inApp.viewEngine.CmInAppBundleConvertor.HOURS_24_IN_MILLIS;
import static com.tokopedia.notifications.inApp.viewEngine.CmInAppConstant.TYPE_INTERSTITIAL;
import static com.tokopedia.notifications.inApp.viewEngine.CmInAppConstant.TYPE_INTERSTITIAL_IMAGE_ONLY;
import static com.tokopedia.notifications.inApp.viewEngine.CmInAppConstant.TYPE_SILENT;

/**
 * @author lalit.singh
 */
public class CMInAppManager implements CmInAppListener,
        DataProvider,
        CmActivityLifecycleHandler.CmActivityApplicationCallback,
        ShowInAppCallback,
        SendPushContract, CmDialogVisibilityContract {

    private static final CMInAppManager inAppManager;
    private Application application;
    private CmInAppListener cmInAppListener;
    private final Object lock = new Object();
    private List<String> excludeScreenList;
    private CMRemoteConfigUtils cmRemoteConfigUtils;
    private PushIntentHandler pushIntentHandler;

    //map  - which will tell whether this activity has pop-up or not
    private final WeakHashMap<Activity, Boolean> dialogIsShownMap = new WeakHashMap<>();
    public CmActivityLifecycleHandler activityLifecycleHandler;

    //DialogHandlers
    private CmDialogHandler cmDialogHandler;
    public CmDataConsumer cmDataConsumer;
    public DataConsumer dataConsumer;

    static {
        inAppManager = new CMInAppManager();
    }

    public long getCmInAppEndTimeInterval() {
        return cmRemoteConfigUtils.getLongRemoteConfig(
                KEY_CM_INAPP_END_TIME_INTERVAL, HOURS_24_IN_MILLIS * 7);
    }

    public static CMInAppManager getInstance() {
        return inAppManager;
    }

    public void init(@NonNull Application application) {
        this.application = application;
        this.cmInAppListener = this;
        cmRemoteConfigUtils = new CMRemoteConfigUtils(application);
        cmDataConsumer = new CmDataConsumer(this);

        cmDialogHandler = new CmDialogHandler();
        pushIntentHandler = new PushIntentHandler();
        activityLifecycleHandler = new CmActivityLifecycleHandler(this,
                pushIntentHandler,
                this,
                this);
        dataConsumer = new DataConsumerImpl();
        RulesManager.initRuleEngine(application, new RuleInterpreterImpl(), dataConsumer);
        initInAppManager();
    }

    public static CmInAppListener getCmInAppListener() {
        if (inAppManager == null) return null;
        if (inAppManager.cmInAppListener == null) return null;
        return inAppManager.cmInAppListener;
    }

    private void initInAppManager() {
        CMActivityLifeCycle lifeCycle = new CMActivityLifeCycle(activityLifecycleHandler);
        application.registerActivityLifecycleCallbacks(lifeCycle);
        CmFragmentLifecycleHandler cmFragmentLifecycleHandler = new CmFragmentLifecycleHandler(this, pushIntentHandler);
        FragmentObserver fragmentObserver = new FragmentObserver(cmFragmentLifecycleHandler);
        FragmentLifecycleObserver.INSTANCE.registerCallback(fragmentObserver);
    }

    private void showInAppNotification(String name, int entityHashCode, boolean isActivity) {
        if (excludeScreenList != null && excludeScreenList.contains(name))
            return;
        RulesManager.getInstance().checkValidity(
                name,
                0L,
                this,
                entityHashCode,
                isActivity
        );
    }

    @Override
    public void notificationsDataResult(List<CMInApp> inAppDataList, int entityHashCode, String screenName) {
        synchronized (lock) {
            if (canShowInApp(inAppDataList)) {
                CMInApp cmInApp = inAppDataList.get(0);
                sendEventInAppPrepared(cmInApp);
                if (checkForOtherSources(cmInApp, entityHashCode, screenName)) return;
                if(canShowDialog()) {
                    showDialog(cmInApp);
                }
            }
        }
    }

    private boolean checkForOtherSources(CMInApp cmInApp, int entityHashCode, String screenName) {
        if (CmEventListener.INSTANCE.getInAppPopupContractMap().containsKey(cmInApp.type)) {
            CmEventListener.INSTANCE.getInAppPopupContractMap().get(cmInApp.type).handleInAppPopup(cmInApp, entityHashCode, screenName);
            sendAmplificationEventInAppRead(cmInApp);
            return true;
        }
        return false;
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

    private void showDialog(CMInApp data) {
        WeakReference<Activity> currentActivity = activityLifecycleHandler.getCurrentWeakActivity();
        String type = data.type;
        if (!TextUtils.isEmpty(type)) {
            switch (data.getType()) {
                case TYPE_INTERSTITIAL_IMAGE_ONLY:
                case TYPE_INTERSTITIAL:
                    showInterstitialDialog(currentActivity, data);
                    break;
                case TYPE_SILENT:
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
                onDialogShown(activity);
                dataConsumed(data);
                sendAmplificationEventInAppRead(data);
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
                onDialogShown(activity);
                dataConsumed(data);
                sendAmplificationEventInAppRead(data);
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
                } else {
                    Map<String, String> messageMap = new HashMap<>();
                    messageMap.put("type", "validation");
                    messageMap.put("reason", "application_null");
                    messageMap.put("data",  "");
                    ServerLogger.log(Priority.P2, "CM_VALIDATION", messageMap);
                }
            }
        } catch (Exception e) {
            Map<String, String> data = remoteMessage.getData();
            if (data != null) {
                Map<String, String> messageMap = new HashMap<>();
                messageMap.put("type", "exception");
                messageMap.put("err", Log.getStackTraceString
                        (e).substring(0, (Math.min(Log.getStackTraceString(e).length(), CMConstant.TimberTags.MAX_LIMIT))));
                messageMap.put("data", data.toString()
                        .substring(0, (Math.min(data.toString().length(), CMConstant.TimberTags.MAX_LIMIT))));
                ServerLogger.log(Priority.P2, "CM_VALIDATION", messageMap);
            }
            else {
                Map<String, String> messageMap = new HashMap<>();
                messageMap.put("type", "exception");
                messageMap.put("err", Log.getStackTraceString
                        (e).substring(0, (Math.min(Log.getStackTraceString(e).length(), CMConstant.TimberTags.MAX_LIMIT))));
                messageMap.put("data", "");
                ServerLogger.log(Priority.P2, "CM_VALIDATION", messageMap);
            }
        }
    }

    @Override
    public void onCMinAppDismiss(CMInApp inApp) {
        RulesManager.getInstance().viewDismissed(inApp.id);
        Activity activity = activityLifecycleHandler.getCurrentActivity();
        if (activity != null)
            onDialogDismiss(activity);
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
            CMNotificationUtils.INSTANCE.sendUTMParamsInGTM(appLink);
        } else {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "validation");
            messageMap.put("reason", "application_null_no_activity");
            messageMap.put("data", "");
            ServerLogger.log(Priority.P2, "CM_VALIDATION", messageMap);
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
    public void showInAppForScreen(@NotNull String name, int entityHashCode, boolean isActivity) {
        showInAppNotification(name, entityHashCode, isActivity);
    }

    public CmActivityLifecycleHandler getActivityLifecycleHandler() {
        return activityLifecycleHandler;
    }

    @Override
    public void onDialogShown(@NotNull Activity activity) {
        dialogIsShownMap.put(activity, true);
    }

    @Override
    public void onDialogDismiss(@NotNull Activity activity) {
        dialogIsShownMap.remove(activity);
    }

    @Override
    public boolean isDialogVisible(@NotNull Activity activity) {
        return dialogIsShownMap.containsKey(activity) && dialogIsShownMap.get(activity);
    }
}