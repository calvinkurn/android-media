package com.tokopedia.digital.categorylist.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.digital.categorylist.view.fragment.DigitalCategoryListFragment;

import static com.tokopedia.digital.applink.DigitalApplinkConstant.DIGITAL;
import static com.tokopedia.digital.applink.DigitalApplinkConstant.DIGITAL_CATEGORY;
import static com.tokopedia.digital.categorylist.view.fragment.DigitalCategoryListFragment.PARAM_IS_COUPON_ACTIVE;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public class DigitalCategoryListActivity extends BaseSimpleActivity {

    public static final String KEY_IS_COUPON_APPLIED_APPLINK = "is_coupon_applied";

    @Override
    public String getScreenName() {
        return DigitalCategoryListActivity.class.getSimpleName();
    }

    @SuppressWarnings("unused")
    @DeepLink({DIGITAL_CATEGORY, DIGITAL})
    public static Intent getCallingApplinksTaskStask(Context context, Bundle extras) {
        int isCouponApplied = 0;
        if (extras.containsKey(KEY_IS_COUPON_APPLIED_APPLINK)) {
            isCouponApplied = Integer.parseInt(extras.getString(KEY_IS_COUPON_APPLIED_APPLINK));
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
    protected android.support.v4.app.Fragment getNewFragment() {
        DigitalCategoryListFragment digitalCategoryListFragment = DigitalCategoryListFragment.newInstance();

        if (getIntent() != null) {
            if (getIntent().hasExtra(DigitalCategoryListFragment.FROM_APP_SHORTCUTS)) {
                boolean isFromAppShortCut = getIntent().getBooleanExtra(DigitalCategoryListFragment.FROM_APP_SHORTCUTS, false);
                digitalCategoryListFragment = DigitalCategoryListFragment.newInstance(isFromAppShortCut);
            }

            if (getIntent().hasExtra(PARAM_IS_COUPON_ACTIVE)) {
                digitalCategoryListFragment = DigitalCategoryListFragment.newInstance(
                        getIntent().getIntExtra(PARAM_IS_COUPON_ACTIVE, 0)
                );
            }
        }
        return digitalCategoryListFragment;
    }
}
