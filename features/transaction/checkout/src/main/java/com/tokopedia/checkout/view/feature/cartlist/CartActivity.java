package com.tokopedia.checkout.view.feature.cartlist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.common.base.BaseCheckoutActivity;
import com.tokopedia.checkout.view.feature.emptycart.EmptyCartFragment;
import com.tokopedia.navigation_common.listener.EmptyCartListener;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartActivity extends BaseCheckoutActivity implements EmptyCartListener {

    private Fragment cartFragment;
    private Fragment emptyCartFragment;

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
        if (getIntent() != null && TextUtils.isEmpty(getIntent().getStringExtra("cart_id"))) {
            bundle.putString("cart_id", getIntent().getStringExtra("cart_id"));
        }
        cartFragment = CartFragment.newInstance(bundle, "");
        ((CartFragment) cartFragment).setEmptyCartListener(this);
        return cartFragment;
    }

    @Override
    public void onCartEmpty(String autoApplyMessage, String state, String titleDesc, String promoCode) {
        if (emptyCartFragment == null) {
            emptyCartFragment = EmptyCartFragment.newInstance(autoApplyMessage, "", state, titleDesc, promoCode);
        }
        if (emptyCartFragment.isAdded()) return;
        cartFragment = null;
        getSupportFragmentManager().beginTransaction()
                .replace(com.tokopedia.abstraction.R.id.parent_view, emptyCartFragment, getTagFragment())
                .commit();
    }

    @Override
    public void onCartNotEmpty(Bundle bundle) {
        if (cartFragment == null) {
            if (getIntent() != null && TextUtils.isEmpty(getIntent().getStringExtra("cart_id"))) {
                bundle.putString("cart_id", getIntent().getStringExtra("cart_id"));
            }
            cartFragment = CartFragment.newInstance(bundle, "");
        }
        if (cartFragment.isAdded()) return;
        emptyCartFragment = null;
        getSupportFragmentManager().beginTransaction()
                .replace(com.tokopedia.abstraction.R.id.parent_view, cartFragment, getTagFragment())
                .commit();
    }
}
