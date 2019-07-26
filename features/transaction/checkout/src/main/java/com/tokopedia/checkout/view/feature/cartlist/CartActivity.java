package com.tokopedia.checkout.view.feature.cartlist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.common.base.BaseCheckoutActivity;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartActivity extends BaseCheckoutActivity {

    public static final String EXTRA_CART_ID = "cart_id";

    private Fragment cartFragment;
    private Fragment emptyCartFragment;
    private String cartId;

    @DeepLink(ApplinkConst.CART)
    public static Intent getCallingIntent(Context context, Bundle extras) {
        Intent intent = new Intent(context, CartActivity.class).putExtras(extras);
        intent.putExtras(extras);
        if (extras.getString(DeepLink.URI) != null) {
            Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
            intent.setData(uri.build());
        }
        return intent;
    }

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
        cartFragment = CartFragment.newInstance(bundle,"");
        return cartFragment;
    }

}
