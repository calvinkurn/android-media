package com.tokopedia.notifications.inApp;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.RemoteMessage;
import com.tokopedia.abstraction.base.view.listener.FragmentLifecycleObserver;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.notifications.CMRouter;
import com.tokopedia.notifications.FragmentObserver;
import com.tokopedia.notifications.R;
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
import com.tokopedia.promotionstarget.data.notification.NotificationEntryType;
import com.tokopedia.promotionstarget.domain.presenter.GratifCancellationExceptionType;
import com.tokopedia.promotionstarget.domain.presenter.GratifPopupIngoreType;
import com.tokopedia.promotionstarget.domain.presenter.GratificationPresenter;
import com.tokopedia.promotionstarget.presentation.ui.dialog.CmGratificationDialog;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;

import kotlinx.coroutines.Job;
import timber.log.Timber;

import static com.tokopedia.notifications.common.CMConstant.EXTRA_BASE_MODEL;
import static com.tokopedia.notifications.inApp.ruleEngine.RulesUtil.Constants.RemoteConfig.KEY_CM_INAPP_END_TIME_INTERVAL;
import static com.tokopedia.notifications.inApp.viewEngine.CmInAppBundleConvertor.HOURS_24_IN_MILLIS;
import static com.tokopedia.notifications.inApp.viewEngine.CmInAppConstant.TYPE_GRATIF;
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
    private FragmentObserver fragmentObserver;
    private GratificationPresenter gratificationPresenter;
    private static final String DISCO_PAGE_ACTIVITY_NAME = "com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity";
    private Map<WeakReference<Activity>, WeakReference<BroadcastReceiver>> broadcastReceiverMap = new HashMap<>();
    private Map<Integer, Job> mapOfGratifJobs = new ConcurrentHashMap<Integer, Job>();

    /*
     * This flag is used for validation of the dialog to be displayed.
     * This is useful for avoiding InApp dialog appearing more than once.
     * */
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
        gratificationPresenter = new GratificationPresenter(application);
        gratificationPresenter.setExceptionCallback(th -> {
            if (th != null)
                th.printStackTrace();
        });
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
        fragmentObserver = new FragmentObserver(this);
        FragmentLifecycleObserver.INSTANCE.registerCallback(fragmentObserver);
    }

    private void updateCurrentActivity(Activity activity) {
        currentActivity = new WeakReference<>(activity);
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

    private void showPopupFromPush(Activity activity, String name, String gratificationId) {
        WeakReference<Activity> tempWeakActivity = new WeakReference<>(activity);
        gratificationPresenter.showGratificationInApp(tempWeakActivity, gratificationId, NotificationEntryType.PUSH, new GratificationPresenter.GratifPopupCallback() {
            @Override
            public void onShow(@NonNull DialogInterface dialogInterface) {
                Log.d("NOOB", "Gratif Dialog - push - show");
            }

            @Override
            public void onDismiss(@NonNull DialogInterface dialogInterface) {
                Log.d("NOOB", "Gratif Dialog - push - dismiss");
            }

            @Override
            public void onIgnored(@GratifPopupIngoreType int reason) {
                Log.d("NOOB", "Gratif Dialog - push - ignore - reason" + String.valueOf(reason));
            }
        }, name);
    }

    private void showIgnoreToast(String type, @GratifPopupIngoreType int reason) {

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            Context context = getCurrentActivity();

            if (context != null) {
                SharedPreferences sp1 = context.getSharedPreferences("promo_gratif", Context.MODE_PRIVATE);
                boolean isDebugGratifChecked = sp1.getBoolean("gratif_debug_toast", false);
                if (isDebugGratifChecked) {
                    String message = "DEBUG - " + type + " - " + reason;
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void notificationsDataResult(List<CMInApp> inAppDataList, int entityHashCode, String screenName) {
        synchronized (lock) {
            if (canShowInApp(inAppDataList)) {
                CMInApp cmInApp = inAppDataList.get(0);
                sendEventInAppPrepared(cmInApp);
                Timber.d(TEMP_TAG + " in-app found for screen name=" + screenName + ",type=" + cmInApp.type);
                showDialog(cmInApp, entityHashCode, screenName);
                if (!cmInApp.getType().equals(TYPE_GRATIF)) {
                    dataConsumed(cmInApp);
                }
            } else {
                Timber.d(TEMP_TAG + " NO in-app found for screen name=" + screenName);
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

    /**
     * legacy dialog
     *
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

        CmGratificationDialog.Companion.getWeakHashMap().put(activity, true);
    }

    /**
     * dialog for interstitial and interstitialImg
     *
     * @param data
     */
    private void interstitialDialog(CMInApp data) {
        if (getCurrentActivity() == null) return;
        Activity activity = getCurrentActivity();
        try {
            // show interstitial banner
            BannerView.create(activity, data);

            // set flag if has dialog showing
            CmGratificationDialog.Companion.getWeakHashMap().put(activity, true);
        } catch (Exception e) {
            onCMInAppInflateException(data);
        }
    }

    private void showGratifDialog(CMInApp data, int entityHashCode, String screenName) {
        if (getCurrentActivity() == null) return;

        try {
            String customValues = data.getCustomValues();
            JSONObject json = new JSONObject(customValues);
            String gratificationId = json.getString("gratificationId");
            Job job = gratificationPresenter.showGratificationInApp(currentActivity, gratificationId, NotificationEntryType.ORGANIC, new GratificationPresenter.GratifPopupCallback() {
                @Override
                public void onShow(@NonNull DialogInterface dialogInterface) {
                    dataConsumed(data);
                    Timber.d(TEMP_TAG + " Dialog - organic - show");
                }

                @Override
                public void onDismiss(@NonNull DialogInterface dialogInterface) {
                    Timber.d(TEMP_TAG + " Dialog - organic - dismiss");
                }

                @Override
                public void onIgnored(@GratifPopupIngoreType int reason) {
                    if (reason != GratifPopupIngoreType.DIALOG_ALREADY_ACTIVE) {
                        dataConsumed(data);
                    }
                    showIgnoreToast("organic", reason);
                    Timber.d(TEMP_TAG + " Dialog - organic - ignored- reason - " + String.valueOf(reason));
                }
            }, screenName);

            if (job != null) {
                mapOfGratifJobs.put(entityHashCode, job);
            }

        } catch (Exception e) {
            dataConsumed(data);
            onCMInAppInflateException(data);
        }
    }

    private void showDialog(CMInApp data, int entityHashCode, String screenName) {
        switch (data.getType()) {
            case TYPE_INTERSTITIAL_IMAGE_ONLY:
            case TYPE_INTERSTITIAL:
                interstitialDialog(data);
                break;
            case TYPE_GRATIF:
                showGratifDialog(data, entityHashCode, screenName);
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

    //todo Rahul onNewIntent of DISCO activity is pending
    public void onNewIntent(Activity activity, Intent intent) {
        if (application == null) application = activity.getApplication();
        updateCurrentActivity(activity);
        if (intent != null && intent.getExtras() != null) {
            decideDialog(activity, intent.getExtras());
        }
    }

    public void onActivityCreatedInternalForPush(final Activity activity) {
        if (application == null) application = activity.getApplication();
        updateCurrentActivity(activity);
        Bundle finalBundle = null;
        Intent intent = activity.getIntent();
        if (intent != null) {
            finalBundle = intent.getExtras();
        }

        //registerBroadcast
        String className = activity.getClass().getName();
        if (className.equals(DISCO_PAGE_ACTIVITY_NAME)) {
            registerBroadcastReceiver(activity);
        }
        decideDialog(activity, finalBundle);
    }

    public void onActivityStartedInternal(final Activity activity) {
        if (application == null) application = activity.getApplication();
        updateCurrentActivity(activity);
        if (canShowDialog()) {
            showInAppNotification(currentActivity.get().getClass().getName(), activity.hashCode());
        }
    }

    private void decideDialog(Activity activity, Bundle bundle) {
        boolean canShowPopupFromPush = false;
        String gratificationId = null;
        if (bundle != null) {
            boolean isComingFromPush = bundle.keySet().contains(EXTRA_BASE_MODEL);
            if (isComingFromPush) {
                gratificationId = bundle.getString("gratificationId"); //todo Remove hardcoding
                canShowPopupFromPush = (!(TextUtils.isEmpty(gratificationId)));
                try {
                    Intent intent = activity.getIntent();
                    Bundle activityBundle = intent.getExtras();
                    if (activityBundle != null) {

                        if (activityBundle.keySet().contains(EXTRA_BASE_MODEL)) {
                            activity.getIntent().removeExtra("gratificationId");
                            activity.getIntent().putExtra("gratificationId_processed", gratificationId);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (canShowPopupFromPush && !TextUtils.isEmpty(gratificationId)) {
            showPopupFromPush(activity, currentActivity.get().getClass().getName(), gratificationId);
        }
    }

    private void registerBroadcastReceiver(Activity activity) {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    final String DISCO_IS_NATIVE = "DISCO_IS_NATIVE";
                    boolean isNative = bundle.getBoolean(DISCO_IS_NATIVE);
                    if (isNative) {
                        Intent activityIntent = activity.getIntent();
                        if (activityIntent != null) {
                            Bundle activityBundle = activityIntent.getExtras();
                            decideDialog(activity, activityBundle);
                        }
                    }
                }
            }
        };
        final String DISCO_INTENT_FILTER = "DISCO_ACTIVITY_SELECTION";
        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, new IntentFilter(DISCO_INTENT_FILTER));
        broadcastReceiverMap.put(new WeakReference<>(activity), new WeakReference<>(receiver));
    }

    private void unRegisterBroadcastReceiver(Activity activity) {
        Set<WeakReference<Activity>> set = broadcastReceiverMap.keySet();
        WeakReference<Activity> finalWeakActivity = null;
        for (WeakReference<Activity> weakReference : set) {
            if (weakReference.get() == activity) {
                finalWeakActivity = weakReference;
                break;
            }
        }
        WeakReference<BroadcastReceiver> weakReceiver = broadcastReceiverMap.get(finalWeakActivity);
        if (weakReceiver != null) {
            BroadcastReceiver receiver = weakReceiver.get();
            if (receiver != null) {
                LocalBroadcastManager.getInstance(activity).unregisterReceiver(receiver);
            }
            broadcastReceiverMap.remove(finalWeakActivity);
        }
    }

    public void onFragmentStart(Fragment fragment) {
        cancelGratifJob(fragment.hashCode(), GratifCancellationExceptionType.FRAGMENT_STARTED);

        if (canShowDialog()) {
            showInAppNotification(fragment.getClass().getName(), fragment.hashCode());
        }
    }

    public void onFragmentResume(Fragment fragment) {
        cancelGratifJob(fragment.hashCode(), GratifCancellationExceptionType.FRAGMENT_RESUME);

        if (canShowDialog()) {
            showInAppNotification(fragment.getClass().getName(), fragment.hashCode());
        }
    }

    public boolean canShowDialog() {
        Activity activity = getCurrentActivity();
        if (activity != null) {
            return !CmGratificationDialog.Companion.getWeakHashMap().containsKey(activity);
        }
        return false;
    }

    public void onFragmentSelected(Fragment fragment) {
        cancelGratifJob(fragment.hashCode(), GratifCancellationExceptionType.FRAGMENT_SELECTED);
        if (canShowDialog()) {
            showInAppNotification(fragment.getClass().getName(), fragment.hashCode());
        }
    }

    public void onFragmentStop(Fragment fragment) {
        cancelGratifJob(fragment.hashCode(), GratifCancellationExceptionType.FRAGMENT_STOP);
    }

    public void onFragmentUnSelected(Fragment fragment) {
        cancelGratifJob(fragment.hashCode(), GratifCancellationExceptionType.FRAGMENT_UNSELECTED);
    }

    public void onFragmentDestroyed(Fragment fragment) {
        cancelGratifJob(fragment.hashCode(), GratifCancellationExceptionType.FRAGMENT_DESTROYED);
    }

    private void cancelGratifJob(int entityHashCode, @GratifCancellationExceptionType String reason) {
        Job job = mapOfGratifJobs.get(entityHashCode);
        if (job != null && job.isActive()) {
            job.cancel(new CancellationException(reason));
            mapOfGratifJobs.remove(entityHashCode);
        }
    }

    public void onActivityStopInternal(Activity activity) {
        if (currentActivity != null && currentActivity.get().getClass().
                getSimpleName().equalsIgnoreCase(activity.getClass().getSimpleName())) {
            currentActivity.clear();
        }
        unRegisterBroadcastReceiver(activity);

        cancelGratifJob(activity.hashCode(), GratifCancellationExceptionType.ACTIVITY_STOP);
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
        }
    }

    @Override
    public void onCMinAppDismiss(CMInApp inApp) {
        RulesManager.getInstance().viewDismissed(inApp.id);
        Activity activity = getCurrentActivity();
        if (activity != null)
            CmGratificationDialog.Companion.getWeakHashMap().remove(activity);
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
}
