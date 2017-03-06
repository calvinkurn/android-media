package com.tokopedia.digital.cart.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.cart.compoundview.CheckoutHolderView;
import com.tokopedia.digital.cart.compoundview.InputPriceHolderView;
import com.tokopedia.digital.cart.compoundview.ItemCartHolderView;
import com.tokopedia.digital.cart.compoundview.VoucherCartHolderView;
import com.tokopedia.digital.cart.interactor.CartDigitalInteractor;
import com.tokopedia.digital.cart.listener.IDigitalCartView;
import com.tokopedia.digital.cart.model.CartDigitalInfoData;
import com.tokopedia.digital.cart.model.UserInputPriceDigital;
import com.tokopedia.digital.cart.presenter.CartDigitalPresenter;
import com.tokopedia.digital.cart.presenter.ICartDigitalPresenter;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author anggaprasetiyo on 2/21/17.
 */

public class CartDigitalFragment extends BasePresenterFragment<ICartDigitalPresenter> implements
        IDigitalCartView, CheckoutHolderView.IAction,
        InputPriceHolderView.EditTextUserInputListener {

    private static final String TAG = CartDigitalFragment.class.getSimpleName();
    private static final String ARG_CART_DIGITAL_DATA_PASS = "ARG_CART_DIGITAL_DATA_PASS";
    private DigitalCheckoutPassData passData;

    @BindView(R2.id.checkout_cart_holder_view)
    CheckoutHolderView checkoutHolderView;
    @BindView(R2.id.item_cart_holder_view)
    ItemCartHolderView itemCartHolderView;
    @BindView(R2.id.voucher_cart_holder_view)
    VoucherCartHolderView voucherCartHolderView;
    @BindView(R2.id.pb_main_loading)
    ProgressBar pbMainLoading;
    @BindView(R2.id.nsv_container)
    NestedScrollView mainContainer;
    @BindView(R2.id.input_price_holder_view)
    InputPriceHolderView inputPriceHolderView;

    private InputPriceHolderView.EditTextUserInputListener listener;
    private SessionHandler sessionHandler;

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
        //   presenter.processAddToCart(passData);
        inputPriceHolderView.setEditTextUserInputListener(this);
        presenter.processGetCartData(passData.getCategoryId());
        sessionHandler = new SessionHandler(getActivity());
        //   presenter.processGetCartData(passData.getCategoryId());
        presenter.processAddToCart(passData);
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
        presenter = new CartDigitalPresenter(this, new CartDigitalInteractor());
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
        View view = getView();
        if (view != null) NetworkErrorHelper.showSnackbar(getActivity(), message);
        else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDialog(Dialog dialog) {

    }

    @Override
    public void dismissDialog(Dialog dialog) {
        if (dialog.isShowing()) dialog.dismiss();
    }

    @Override
    public void executeIntentService(Bundle bundle, Class<? extends IntentService> clazz) {

    }

    @Override
    public String getStringFromResource(@StringRes int resId) {
        return getString(resId);
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams) {
        return AuthUtil.generateParamsNetwork(getActivity(), originParams);
    }

    @Override
    public void closeView() {
        getActivity().finish();
    }

    @OnClick(R2.id.btn_next)
    void actionNext() {
        presenter.processGetCartData(passData.getCategoryId());
    }

    @Override
    public void renderCartDigitalInfoData(CartDigitalInfoData cartDigitalInfoData) {
        pbMainLoading.setVisibility(View.GONE);
        mainContainer.setVisibility(View.VISIBLE);
        itemCartHolderView.renderAdditionalInfo(cartDigitalInfoData.getAdditionalInfos());
        itemCartHolderView.renderDataMainInfo(cartDigitalInfoData.getMainInfo());
        itemCartHolderView.setCategoryName("");
        itemCartHolderView.setOperatorName("");
        itemCartHolderView.setImageItemCart("");
        renderDataInputPrice(String.valueOf(cartDigitalInfoData.getAttributes().getPricePlain()),
                cartDigitalInfoData.getAttributes().getUserInputPrice());
        checkoutHolderView.renderData(
                this,
                cartDigitalInfoData.getAttributes().getPrice(),
                cartDigitalInfoData.getAttributes().getPrice()
        );
    }

    private void renderDataInputPrice(String total, UserInputPriceDigital userInputPriceDigital) {
        if (userInputPriceDigital != null) {
            inputPriceHolderView.setVisibility(View.VISIBLE);
            inputPriceHolderView.setInputPriceInfo(total, userInputPriceDigital.getMinPaymentPlain(),
                    userInputPriceDigital.getMaxPaymentPlain());
            inputPriceHolderView.bindView(userInputPriceDigital.getMinPayment(),
                    userInputPriceDigital.getMaxPayment());
        } else {
            inputPriceHolderView.setVisibility(View.GONE);
        }
    }

    @Override
    public void renderLoadingGetCartInfo() {
        pbMainLoading.setVisibility(View.VISIBLE);
        mainContainer.setVisibility(View.GONE);
    }

    @Override
    public void closeViewWithMessageAlert(String message) {
        pbMainLoading.setVisibility(View.GONE);
        mainContainer.setVisibility(View.GONE);
        View view = getView();
        if (view != null) {
            Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
            snackbar.setCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    getActivity().finish();
                }
            });
            snackbar.show();
        } else {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }

    @Override
    public String getDeviceId() {
        return GCMHandler.getRegistrationId(getActivity());
    }

    @Override
    public String getUserId() {
        return sessionHandler.getLoginID();
    }

    @Override
    public String getAccountToken() {
        return sessionHandler.getAccessToken(getActivity());
    }

    @Override
    public String getWalletRefreshToken() {
        return sessionHandler.getWalletRefreshToken(getActivity());
    }

    @Override
    public String getIpAddress() {
        return getLocalIpAddress();
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return Formatter.formatIpAddress(inetAddress.hashCode());
                    }
                }
            }
        } catch (SocketException ex) {
            return "";
        }
        return null;
    }

    @Override
    public void onClickButtonNext() {

    }

    @Override
    public void userInputPayment(long paymentAmount) {
        Log.d(TAG, "userInputPayment: " + paymentAmount);
    }
}
