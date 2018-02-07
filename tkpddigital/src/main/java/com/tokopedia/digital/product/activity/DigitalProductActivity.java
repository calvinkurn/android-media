package com.tokopedia.digital.product.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.fragment.DigitalProductFragment;

/**
 * @author anggaprasetiyo on 4/25/17.
 */

public class DigitalProductActivity extends BasePresenterActivity
        implements DigitalProductFragment.ActionListener {
    public static final String EXTRA_CATEGORY_PASS_DATA = "EXTRA_CATEGORY_PASS_DATA";
    private static final String EXTRA_STATE_TITLE_TOOLBAR = "EXTRA_STATE_TITLE_TOOLBAR";

    private String titleToolbar;
    private DigitalCategoryDetailPassData passData;

    public static Intent newInstance(Context context, DigitalCategoryDetailPassData passData) {
        return new Intent(context, DigitalProductActivity.class)
                .putExtra(EXTRA_CATEGORY_PASS_DATA, passData);
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @SuppressWarnings("unused")
    @DeepLink({Constants.Applinks.DIGITAL_PRODUCT})
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
        DigitalCategoryDetailPassData passData = new DigitalCategoryDetailPassData.Builder()
                .appLinks(uri.toString())
                .categoryId(extras.getString(DigitalCategoryDetailPassData.PARAM_CATEGORY_ID))
                .operatorId(extras.getString(DigitalCategoryDetailPassData.PARAM_OPERATOR_ID))
                .productId(extras.getString(DigitalCategoryDetailPassData.PARAM_PRODUCT_ID))
                .clientNumber(extras.getString(DigitalCategoryDetailPassData.PARAM_CLIENT_NUMBER))
                .build();
        Intent destination = DigitalProductActivity.newInstance(context, passData);
        destination.putExtra(Constants.EXTRA_FROM_PUSH, true);
        taskStackBuilder.addNextIntent(destination);
        return destination;
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.passData = extras.getParcelable(EXTRA_CATEGORY_PASS_DATA);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cart_digital_module;
    }

    @Override
    protected void initView() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !(fragment instanceof DigitalProductFragment))
            getFragmentManager().beginTransaction().replace(R.id.container,
                    DigitalProductFragment.newInstance(
                            passData.getCategoryId(),
                            passData.getOperatorId(),
                            passData.getProductId(),
                            passData.getClientNumber()))
                    .commit();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_STATE_TITLE_TOOLBAR, titleToolbar);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.titleToolbar = savedInstanceState.getString(EXTRA_STATE_TITLE_TOOLBAR);
        invalidateTitleToolBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateTitleToolBar();
    }

    @Override
    public void updateTitleToolbar(String title) {
        this.titleToolbar = title;
        invalidateTitleToolBar();
        TrackingUtils.sendMoEngageOpenDigitalCatScreen(title, passData.getCategoryId());
    }

    private void invalidateTitleToolBar() {
        if (!TextUtils.isEmpty(titleToolbar)) toolbar.setTitle(titleToolbar);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
