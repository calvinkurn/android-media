package com.tokopedia.checkout.view.view.cartlist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.applink.CheckoutAppLink;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.router.ICheckoutModuleRouter;
import com.tokopedia.checkout.view.base.BaseCheckoutActivity;
import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.di.component.CartComponentInjector;
import com.tokopedia.checkout.view.view.cartlist.removecartitem.RemoveCartItemFragment;

import java.util.List;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartActivity extends BaseCheckoutActivity implements CartFragment.ActionListener,
        HasComponent<CartComponent> {

    @DeepLink(CheckoutAppLink.CART)
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
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            startActivity(
                    ((ICheckoutModuleRouter) getApplication())
                            .getHomeIntent(this)
            );
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
    public void onRemoveAllCartMenuClicked(List<CartItemData> cartItemData) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !(fragment instanceof CartRemoveProductFragment)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.parent_view, RemoveCartItemFragment.newInstance(cartItemData))
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    protected Fragment getNewFragment() {
        return CartFragment.newInstance();
    }

    @Override
    public CartComponent getComponent() {
        return CartComponentInjector.newInstance(getApplication()).getCartApiServiceComponent();
    }
}
