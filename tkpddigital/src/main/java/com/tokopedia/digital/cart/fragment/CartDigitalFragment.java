package com.tokopedia.digital.cart.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.StringRes;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.digitalmodule.passdata.ExampPassData;
import com.tokopedia.digital.R;
import com.tokopedia.digital.cart.listener.IDigitalCartView;
import com.tokopedia.digital.cart.presenter.CartDigitalPresenter;
import com.tokopedia.digital.cart.presenter.ICartDigitalPresenter;

/**
 * @author anggaprasetiyo on 2/21/17.
 */

public class CartDigitalFragment extends BasePresenterFragment<ICartDigitalPresenter> implements IDigitalCartView {

    private static final String ARG_CART_DIGITAL_DATA_PASS = "ARG_CART_DIGITAL_DATA_PASS";
    private ExampPassData passData;

    public static Fragment newInstance(Parcelable passData) {
        CartDigitalFragment cartDigitalFragment = new CartDigitalFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_CART_DIGITAL_DATA_PASS, passData);
        cartDigitalFragment.setArguments(bundle);
        return cartDigitalFragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new CartDigitalPresenter(this);
        presenter.processGetCartData(passData.getCategoryId());
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        passData = arguments.getParcelable(ARG_CART_DIGITAL_DATA_PASS);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_cart_digital_module;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }


    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {

    }

    @Override
    public void navigateToActivity(Intent intent) {

    }

    @Override
    public void showProgressLoading() {

    }

    @Override
    public void hideProgressLoading() {

    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void showDialog(Dialog dialog) {

    }

    @Override
    public void dismissDialog(Dialog dialog) {

    }

    @Override
    public void executeIntentService(Bundle bundle, Class<? extends IntentService> clazz) {

    }

    @Override
    public String getStringFromResource(@StringRes int resId) {
        return null;
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(TKPDMapParam<String, String> originParams) {
        return null;
    }

    @Override
    public void closeView() {

    }
}
