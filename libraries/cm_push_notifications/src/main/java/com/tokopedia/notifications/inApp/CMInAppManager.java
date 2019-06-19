package com.tokopedia.notifications.inApp;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.google.firebase.messaging.RemoteMessage;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.notifications.R;
import com.tokopedia.notifications.inApp.ruleEngine.RulesManager;
import com.tokopedia.notifications.inApp.ruleEngine.interfaces.DataProvider;
import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp;
import com.tokopedia.notifications.inApp.viewEngine.CMActivityLifeCycle;
import com.tokopedia.notifications.inApp.viewEngine.CmInAppBundleConvertor;
import com.tokopedia.notifications.inApp.viewEngine.CmInAppListener;
import com.tokopedia.notifications.inApp.viewEngine.ViewEngine;

import java.lang.ref.WeakReference;
import java.util.List;


/**
 * @author lalit.singh
 */
public class CMInAppManager implements CmInAppListener {

    private static CMInAppManager inAppManager;

    Application application;

    WeakReference<Activity> currentActivity;

    CmInAppListener cmInAppListener;


    public static CMInAppManager getInstance() {
        return inAppManager;
    }

    static {
        inAppManager = new CMInAppManager();
    }

    public void init(@NonNull Application application) {
        this.application = application;
        this.cmInAppListener = this;
        RulesManager.initRuleEngine(application);
        initInAppManager();
    }

    public static CmInAppListener getCmInAppListener() {
        if (inAppManager == null)
            return null;
        if (inAppManager.cmInAppListener == null)
            return null;
        return inAppManager.cmInAppListener;
    }

    private void initInAppManager() {
        application.registerActivityLifecycleCallbacks(new CMActivityLifeCycle(this));
    }

    private void updateCurrentActivity(Activity activity) {
        currentActivity = new WeakReference<>(activity);
    }

    private void showInAppNotification() {
        DataProvider dataProvider = new DataProvider() {
            @Override
            public void notificationsDataResult(List<CMInApp> inAppDataList) {
                if (canShowInApp(inAppDataList)) {
                    final CMInApp cmInApp = ViewEngine.getInstance(application)
                            .createView(getCurrentActivity(), inAppDataList.get(0));
                    if (cmInApp == null)
                        return;
                    Activity activity = getCurrentActivity();
                    if (activity == null)
                        return;
                    View inAppViewPrev = activity.findViewById(R.id.mainContainer);
                    if (null != inAppViewPrev)//In-App view already present on Activity
                        return;
                    FrameLayout root = (FrameLayout) activity.getWindow()
                            .getDecorView()
                            .findViewById(android.R.id.content)
                            .getRootView();
                    root.addView(cmInApp.getCmInAppView());
                    dataConsumed(cmInApp);
                }
            }
        };
        RulesManager.getInstance().checkValidity(currentActivity.get().getClass().getName(), 0L, dataProvider);
    }

    private boolean canShowInApp(List<CMInApp> inAppDataList) {
        if (inAppDataList != null && inAppDataList.size() > 0) {
            return true;
        } else {
            return false;
        }
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
        if (application == null)
            application = activity.getApplication();
        updateCurrentActivity(activity);
        showInAppNotification();
    }

    public void onActivityStopInternal(Activity activity) {
        if (currentActivity != null && currentActivity.get().getClass().
                getSimpleName().equalsIgnoreCase(activity.getClass().getSimpleName()))
            currentActivity.clear();
    }

    private void dataConsumed(CMInApp inAppData) {
        RulesManager.getInstance().dataConsumed(inAppData.id);
    }

    public void dataConsumed(long id) {
        RulesManager.getInstance().dataConsumed(id);
    }

    private void putDataToStore(CMInApp inAppData) {
        RepositoryManager.getInstance().getStorageProvider().putDataToStore(inAppData);
    }

    public void viewDismissed(long id) {
        RulesManager.getInstance().viewDismissed(id);
    }

    public void handlePushPayload(RemoteMessage remoteMessage) {
        try {
            CMInApp cmInApp = CmInAppBundleConvertor.getCmInApp(remoteMessage);
            if (null != cmInApp)
                putDataToStore(cmInApp);
        } catch (Exception e) {
        }
    }

    @Override
    public boolean showCmInAppMessage() {
        return false;
    }

    @Override
    public void onCMInAppShown(CMInApp cmInApp) {

    }

    @Override
    public void onCMinAppDismiss() {
        Activity activity = getCurrentActivity();
        if (activity != null) {
            View mainView = activity.findViewById(R.id.mainContainer);
            if (mainView != null && mainView.getTag() != null && mainView.getTag() instanceof CMInApp) {
                CMInApp cmInApp = (CMInApp) mainView.getTag();
                RulesManager.getInstance().viewDismissed(cmInApp.id);
            }
        }
    }

    @Override
    public void onCMInAppLinkClick(Uri deepLinkUri, String screenName) {
        Log.d("InApp", deepLinkUri.toString());
        Intent appLinkIntent = RouteManager.getIntent(application.getApplicationContext(), deepLinkUri.toString());
        if (getCurrentActivity() != null)
            getCurrentActivity().startActivity(appLinkIntent);
    }

    @Override
    public void onCMInAppClosed() {

    }
}

