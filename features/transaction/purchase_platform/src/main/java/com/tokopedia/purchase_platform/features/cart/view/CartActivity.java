package com.tokopedia.purchase_platform.features.cart.view;

import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.purchase_platform.R;
import com.tokopedia.purchase_platform.common.base.BaseCheckoutActivity;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartActivity extends BaseCheckoutActivity {

    public static final String EXTRA_CART_ID = "cart_id";

    private Fragment cartFragment;
    private Fragment emptyCartFragment;
    private String cartId;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_cart;
    }

    @Override
    protected void initInjector() {
    }

    @Override
    protected void setupURIPass(Uri data) {
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        cartId = extras.getString(EXTRA_CART_ID);
    }

    @Override
    protected void initView() {
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        updateTitle(title.toString());
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof ICartListAnalyticsListener) {
            ((ICartListAnalyticsListener) currentFragment).sendAnalyticsOnClickBackArrow();
        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
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
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_CART_ID, cartId);
        cartFragment = CartFragment.Companion.newInstance(bundle,"");
        return cartFragment;
    }

}
