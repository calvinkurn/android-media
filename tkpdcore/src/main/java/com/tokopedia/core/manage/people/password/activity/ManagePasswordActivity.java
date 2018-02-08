package com.tokopedia.core.manage.people.password.activity;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.manage.people.password.fragment.ManagePasswordFragment;
import com.tokopedia.core.manage.people.password.intentservice.ManagePasswordIntentService;
import com.tokopedia.core.manage.people.password.intentservice.ManagePasswordResultReceiver;
import com.tokopedia.core.manage.people.password.presenter.ManagePasswordActivityPresenter;
import com.tokopedia.core.manage.people.password.presenter.ManagePasswordActivityPresenterImpl;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.welcome.WelcomeActivity;

public class ManagePasswordActivity extends BasePresenterActivity<ManagePasswordActivityPresenter>
                                    implements ManagePasswordActivityView, ManagePasswordResultReceiver.Receiver{

    ManagePasswordResultReceiver receiver;

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_CONFIG_P_PASSWORD;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initResultReceiver();
        super.onCreate(savedInstanceState);
    }

    private void initResultReceiver() {
        receiver = new ManagePasswordResultReceiver(new Handler());
        receiver.setReceiver(this);
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
    }

    @Override
    protected void initialPresenter() {
        presenter = new ManagePasswordActivityPresenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_manage_password;
    }

    @Override
    protected void initView() {
        presenter.initFragment();
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
    public void inflateFragment(Fragment fragment, String tag) {
        if (getFragmentManager().findFragmentByTag(tag) == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, tag)
                    .commit();
        }
    }

    public void changePassword(Bundle param) {
        ManagePasswordIntentService.changePassword(this,param,receiver);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        ManagePasswordFragment fragment = (ManagePasswordFragment) getFragmentManager()
                .findFragmentByTag(ManagePasswordFragment.class.getSimpleName());
        if (fragment != null) {
            switch (resultCode) {
                case ManagePasswordIntentService.SUCCESS_CHANGE_PASSWORD:
                    onReceiveResultSuccess(fragment,resultData);
                    break;
                case ManagePasswordIntentService.ERROR_CHANGE_PASSWORD:
                    onReceiveResultError(fragment, resultData);
                    break;
            }
        }
    }

    private void onReceiveResultError(ManagePasswordFragment fragment, Bundle resultData) {
        fragment.onErrorAction(resultData);
    }

    private void onReceiveResultSuccess(ManagePasswordFragment fragment, Bundle resultData) {
        fragment.onSuccessAction(resultData);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                exit();
            }
        }, 2000);

    }

    private void exit() {
        new GlobalCacheManager().deleteAll();
        SessionHandler.clearUserData(this);
        TrackingUtils.eventMoEngageLogoutUser();

        Intent intent;
        if (GlobalConfig.isSellerApp()) {
            intent = new Intent(this, WelcomeActivity.class);
        } else {
            intent = HomeRouter.getHomeActivity(this);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
