package com.tokopedia.sellerapp.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.appupdate.AppUpdateDialogBuilder;
import com.tokopedia.core.appupdate.ApplicationUpdate;
import com.tokopedia.core.appupdate.model.DetailUpdate;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.GCMHandlerListener;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.dashboard.view.fragment.DashboardFragment;
import com.tokopedia.sellerapp.fcm.appupdate.FirebaseRemoteAppUpdate;

//import com.tokopedia.sellerapp.deeplink.DeepLinkDelegate;
//import com.tokopedia.sellerapp.deeplink.DeepLinkHandlerActivity;

/**
 * Created by nathan on 9/5/17.
 */

public class DashboardActivity extends DrawerPresenterActivity
        implements GCMHandlerListener {

    public static final String TAG = DashboardActivity.class.getSimpleName();

    public static Intent createInstance(Context context) {
        return new Intent(context, DashboardActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, DashboardFragment.newInstance(), TAG)
                    .commit();
        }
        checkAppUpdate();
    }

    private void checkAppUpdate() {
        ApplicationUpdate appUpdate = new FirebaseRemoteAppUpdate(this);
        appUpdate.checkApplicationUpdate(new ApplicationUpdate.OnUpdateListener() {
            @Override
            public void onNeedUpdate(final DetailUpdate detail) {
                if (!isFinishing()) {
                    new AppUpdateDialogBuilder(DashboardActivity.this, detail).getAlertDialog().show();
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onNotNeedUpdate() {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        FCMCacheManager.checkAndSyncFcmId(getApplicationContext());
        NotificationModHandler.showDialogNotificationIfNotShowing(this);
    }

    @Override
    public void onGCMSuccess(String gcmId) {
        
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.SELLER_INDEX_HOME;
    }

}
