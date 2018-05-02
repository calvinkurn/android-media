package com.tokopedia.checkout.view.view.cartlist;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.view.base.BaseCheckoutActivity;
import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.di.component.CartComponentInjector;
import com.tokopedia.checkout.view.di.component.DaggerCartComponent;
import com.tokopedia.checkout.view.di.module.DataModule;

import java.util.List;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartActivity extends BaseCheckoutActivity implements CartFragment.ActionListener,
        HasComponent<CartComponent> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
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
                    .replace(R.id.parent_view, CartRemoveProductFragment.newInstance(cartItemData))
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
        return CartComponentInjector.newInstance(
                DaggerCartComponent.builder()
                        .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                        .dataModule(new DataModule())
                        .build())
                .getCartApiServiceComponent();
    }
}
