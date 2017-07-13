package com.tokopedia.digital.cart.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.digital.R;
import com.tokopedia.digital.cart.fragment.CartDigitalFragment;

/**
 * @author anggaprasetiyo on 2/21/17.
 */

public class CartDigitalActivity extends BasePresenterActivity implements
        CartDigitalFragment.ActionListener {
    private static final String EXTRA_PASS_DIGITAL_CART_DATA = "EXTRA_PASS_DIGITAL_CART_DATA";
    private DigitalCheckoutPassData passData;


    public static Intent newInstance(Context context, DigitalCheckoutPassData passData) {
        return new Intent(context, CartDigitalActivity.class)
                .putExtra(EXTRA_PASS_DIGITAL_CART_DATA, passData);
    }

    public static Intent newInstance(Context context, Bundle bundle) {
        DigitalCheckoutPassData passData = new DigitalCheckoutPassData();
        passData.setAction(bundle.getString(DigitalCheckoutPassData.PARAM_ACTION));
        passData.setCategoryId(bundle.getString(DigitalCheckoutPassData.PARAM_CATEGORY_ID));
        passData.setClientNumber(bundle.getString(DigitalCheckoutPassData.PARAM_CLIENT_NUMBER));
        passData.setOperatorId(bundle.getString(DigitalCheckoutPassData.PARAM_OPERATOR_ID));
        passData.setProductId(bundle.getString(DigitalCheckoutPassData.PARAM_PRODUCT_ID));
        passData.setIsPromo(bundle.getString(DigitalCheckoutPassData.PARAM_IS_PROMO));
        passData.setInstantCheckout(bundle.getString(DigitalCheckoutPassData.PARAM_INSTANT_CHECKOUT));
        passData.setUtmCampaign(bundle.getString(DigitalCheckoutPassData.PARAM_UTM_CAMPAIGN));
        passData.setUtmMedium(bundle.getString(DigitalCheckoutPassData.PARAM_UTM_MEDIUM));
        passData.setUtmSource(bundle.getString(DigitalCheckoutPassData.PARAM_UTM_SOURCE));
        passData.setUtmContent(bundle.getString(DigitalCheckoutPassData.PARAM_UTM_CONTENT));
        passData.setIdemPotencyKey(bundle.getString(DigitalCheckoutPassData.PARAM_IDEM_POTENCY_KEY));
        return new Intent(context, CartDigitalActivity.class)
                .putExtra(EXTRA_PASS_DIGITAL_CART_DATA, passData);
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        passData = extras.getParcelable(EXTRA_PASS_DIGITAL_CART_DATA);
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

    }

    @Override
    protected void setViewListener() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !(fragment instanceof CartDigitalFragment))
            getFragmentManager().beginTransaction().replace(R.id.container,
                    CartDigitalFragment.newInstance(passData)).commit();

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }


    @Override
    public void setTitleCart(String title) {
        toolbar.setTitle(title);
    }
}
