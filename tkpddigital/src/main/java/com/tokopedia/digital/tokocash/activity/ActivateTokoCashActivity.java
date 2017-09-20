package com.tokopedia.digital.tokocash.activity;

import android.app.Fragment;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.digital.R;
import com.tokopedia.digital.tokocash.fragment.ActivateTokoCashFragment;
import com.tokopedia.digital.tokocash.fragment.RequestOTPWalletFragment;

/**
 * Created by nabillasabbaha on 7/24/17.
 */

public class ActivateTokoCashActivity extends BasePresenterActivity
        implements ActivateTokoCashFragment.ActionListener, RequestOTPWalletFragment.ActionListener {

    @SuppressWarnings("unused")
    @DeepLink(Constants.Applinks.WALLET_ACTIVATION)
    public static Intent getcallingIntent(Context context, Bundle extras) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        if (extras.getBoolean(Constants.EXTRA_APPLINK_FROM_PUSH, false)) {
            Intent homeIntent;
            if (GlobalConfig.isSellerApp()) {
                homeIntent = SellerAppRouter.getSellerHomeActivity(context);
            } else {
                homeIntent = HomeRouter.getHomeActivity(context);
            }
            homeIntent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT,
                    HomeRouter.INIT_STATE_FRAGMENT_HOME);
            taskStackBuilder.addNextIntent(homeIntent);
        }
        Intent destination = ActivateTokoCashActivity.newInstance(context);
        destination.putExtra(Constants.EXTRA_FROM_PUSH, true);
        taskStackBuilder.addNextIntent(destination);
        return destination;
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, ActivateTokoCashActivity.class);
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
        return R.layout.activity_activate_tokocash;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setViewListener() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !(fragment instanceof ActivateTokoCashFragment)) {
            getFragmentManager().beginTransaction().replace(R.id.container,
                    ActivateTokoCashFragment.newInstance()).commit();
        }
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void setTitlePage(String titlePage) {
        toolbar.setTitle(titlePage);
    }

    @Override
    public void directToSuccessActivateTokoCashPage() {
        startActivity(SuccessActivateTokoCashActivity.newInstance(getApplicationContext()));
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void directPageToOTPWallet() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !(fragment instanceof RequestOTPWalletFragment))
            getFragmentManager().beginTransaction().replace(R.id.container,
                    RequestOTPWalletFragment.newInstance()).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStackImmediate();
        else
            super.onBackPressed();
    }
}