package com.tokopedia.digital.categorylist.view.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.digital.R;
import com.tokopedia.digital.categorylist.view.fragment.DigitalCategoryListFragment;

import static com.tokopedia.digital.applink.DigitalApplinkConstant.DIGITAL;
import static com.tokopedia.digital.applink.DigitalApplinkConstant.DIGITAL_CATEGORY;
import static com.tokopedia.digital.categorylist.view.fragment.DigitalCategoryListFragment.PARAM_IS_COUPON_ACTIVE;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public class DigitalCategoryListActivity extends BasePresenterActivity {

    @Override
    public String getScreenName() {
        return DigitalCategoryListActivity.class.getSimpleName();
    }

    @SuppressWarnings("unused")
    @DeepLink({DIGITAL_CATEGORY, DIGITAL})
    public static Intent getCallingApplinksTaskStask(Context context, Bundle extras) {
        int isCouponApplied = 0;
        if (extras.containsKey("is_coupon_applied")) {
            isCouponApplied = extras.getInt("is_coupon_applied");
        }
        return DigitalCategoryListActivity.newInstance(context, isCouponApplied);
    }


    @Override
    protected void onResume() {
        super.onResume();
        unregisterShake();
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, DigitalCategoryListActivity.class);
    }

    public static Intent newInstance(Context context, int isCouponApplied) {
        Intent intent = new Intent(context, DigitalCategoryListActivity.class);
        intent.putExtra(PARAM_IS_COUPON_ACTIVE, isCouponApplied);
        return intent;
    }

    public static Intent newInstance(Context context, Bundle bundle) {
        Intent intent = new Intent(context, DigitalCategoryListActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newInstance(Context context, boolean isFromSeller) {
        Intent intent = new Intent(context, DigitalCategoryListActivity.class);
        return intent;
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
        return R.layout.activity_digital_category_list_digital_module;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setViewListener() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !(fragment instanceof DigitalCategoryListFragment)) {
            DigitalCategoryListFragment digitalCategoryListFragment;


            if (getIntent() != null) {
                boolean isFromAppShortCut = getIntent().getBooleanExtra(Constants.FROM_APP_SHORTCUTS, false);
                digitalCategoryListFragment = DigitalCategoryListFragment.newInstance(isFromAppShortCut);
            } else {
                digitalCategoryListFragment = DigitalCategoryListFragment.newInstance(
                        getIntent().getIntExtra(PARAM_IS_COUPON_ACTIVE, 0)
                );
            }

            getFragmentManager().beginTransaction().replace(R.id.container,
                    digitalCategoryListFragment).commit();
        }
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
