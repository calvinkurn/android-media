package com.tokopedia.checkout.view.view.cartlist;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.checkout.R;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;

import java.util.List;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartActivity extends BasePresenterActivity implements CartFragment.ActionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        setupToolbar();
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
    protected int getLayoutId() {
        return R.layout.activity_cart_tx_module;
    }

    @Override
    protected void initView() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !(fragment instanceof CartFragment)) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, CartFragment.newInstance())
                    .commit();
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
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    public void onRemoveAllCartMenuClicked(List<CartItemData> cartItemData) {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !(fragment instanceof CartRemoveProductFragment)) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, CartRemoveProductFragment.newInstance(cartItemData))
                    .addToBackStack(null)
                    .commit();
        }
    }
}
