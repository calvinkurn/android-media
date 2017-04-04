package com.tokopedia.digital.cart.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.StringRes;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.digital.DigitalEndpointService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.cart.activity.InstantCheckoutActivity;
import com.tokopedia.digital.cart.activity.OtpVerificationActivity;
import com.tokopedia.digital.cart.compoundview.CheckoutHolderView;
import com.tokopedia.digital.cart.compoundview.InputPriceHolderView;
import com.tokopedia.digital.cart.compoundview.ItemCartHolderView;
import com.tokopedia.digital.cart.compoundview.VoucherCartHolderView;
import com.tokopedia.digital.cart.data.mapper.CartMapperData;
import com.tokopedia.digital.cart.data.mapper.ICartMapperData;
import com.tokopedia.digital.cart.domain.CartDigitalRepository;
import com.tokopedia.digital.cart.domain.CheckoutRepository;
import com.tokopedia.digital.cart.domain.VoucherDigitalRepository;
import com.tokopedia.digital.cart.interactor.CartDigitalInteractor;
import com.tokopedia.digital.cart.listener.IDigitalCartView;
import com.tokopedia.digital.cart.model.CartDigitalInfoData;
import com.tokopedia.digital.cart.model.CheckoutDataParameter;
import com.tokopedia.digital.cart.model.CheckoutDigitalData;
import com.tokopedia.digital.cart.model.InstantCheckoutData;
import com.tokopedia.digital.cart.model.UserInputPriceDigital;
import com.tokopedia.digital.cart.model.VoucherDigital;
import com.tokopedia.digital.cart.presenter.CartDigitalPresenter;
import com.tokopedia.digital.cart.presenter.ICartDigitalPresenter;
import com.tokopedia.digital.utils.DeviceUtil;
import com.tokopedia.payment.activity.TopPayActivity;
import com.tokopedia.payment.model.PaymentPassData;

import butterknife.BindView;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 2/21/17.
 */

public class CartDigitalFragment extends BasePresenterFragment<ICartDigitalPresenter> implements
        IDigitalCartView, CheckoutHolderView.IAction,
        InputPriceHolderView.ActionListener, VoucherCartHolderView.ActionListener {

    private static final String TAG = CartDigitalFragment.class.getSimpleName();
    private static final String ARG_CART_DIGITAL_DATA_PASS = "ARG_CART_DIGITAL_DATA_PASS";

    private static final String EXTRA_STATE_CART_DIGITAL_INFO_DATA =
            "EXTRA_STATE_CART_DIGITAL_INFO_DATA";
    private static final String EXTRA_STATE_VOUCHER_DIGITAL =
            "EXTRA_STATE_VOUCHER_DIGITAL";
    private static final String EXTRA_STATE_CHECKOUT_DATA_PARAMETER =
            "EXTRA_STATE_CHECKOUT_DATA_PARAMETER";
    private static final String EXTRA_STATE_CHECKOUT_PASS_DATA =
            "EXTRA_STATE_CHECKOUT_PASS_DATA";

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

    private SessionHandler sessionHandler;
    private ActionListener actionListener;
    private TkpdProgressDialog progressDialogNormal;
    private CheckoutDataParameter.Builder checkoutDataBuilder;

    private DigitalCheckoutPassData passData;
    private CartDigitalInfoData cartDigitalInfoDataState;
    private VoucherDigital voucherDigitalState;
    private CompositeSubscription compositeSubscription;

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
        presenter.processAddToCart();
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putParcelable(EXTRA_STATE_CART_DIGITAL_INFO_DATA, cartDigitalInfoDataState);
        state.putParcelable(EXTRA_STATE_CHECKOUT_DATA_PARAMETER, checkoutDataBuilder.build());
        state.putParcelable(EXTRA_STATE_VOUCHER_DIGITAL, voucherDigitalState);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        DigitalCheckoutPassData digitalCheckoutPassData = (DigitalCheckoutPassData) savedState.get(
                EXTRA_STATE_CHECKOUT_PASS_DATA
        );
        if (digitalCheckoutPassData != null) passData = digitalCheckoutPassData;
        CheckoutDataParameter checkoutDataParameter = (CheckoutDataParameter) savedState.get(
                EXTRA_STATE_CHECKOUT_DATA_PARAMETER
        );
        if (checkoutDataParameter != null) checkoutDataBuilder = checkoutDataParameter.newBuilder();
        CartDigitalInfoData cartDigitalInfoData = (CartDigitalInfoData) savedState.get(
                EXTRA_STATE_CART_DIGITAL_INFO_DATA
        );
        if (cartDigitalInfoData != null) renderCartDigitalInfoData(cartDigitalInfoData);
        VoucherDigital voucherDigital = (VoucherDigital) savedState.get(EXTRA_STATE_VOUCHER_DIGITAL);
        if (voucherDigital != null) renderVoucherInfoData(voucherDigital);
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        DigitalEndpointService digitalEndpointService = new DigitalEndpointService();
        ICartMapperData cartMapperData = new CartMapperData();
        if (compositeSubscription == null) compositeSubscription = new CompositeSubscription();
        presenter = new CartDigitalPresenter(this, new CartDigitalInteractor(
                compositeSubscription, new CartDigitalRepository(digitalEndpointService, cartMapperData),
                new VoucherDigitalRepository(digitalEndpointService, cartMapperData),
                new CheckoutRepository(digitalEndpointService, cartMapperData)
        ));
    }

    @Override
    protected void initialListener(Activity activity) {
        actionListener = (ActionListener) activity;
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
        checkoutDataBuilder = new CheckoutDataParameter.Builder();
        inputPriceHolderView.setActionListener(this);
        voucherCartHolderView.setActionListener(this);
        sessionHandler = new SessionHandler(getActivity());
        progressDialogNormal = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
        progressDialogNormal.setCancelable(false);
    }

    @Override
    protected void setActionVar() {

    }


    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void showInitialProgressLoading() {
        pbMainLoading.setVisibility(View.VISIBLE);
        mainContainer.setVisibility(View.GONE);
    }

    @Override
    public void hideInitialProgressLoading() {
        pbMainLoading.setVisibility(View.GONE);
        mainContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgressLoading() {
        progressDialogNormal.showDialog();
    }

    @Override
    public void hideProgressLoading() {
        progressDialogNormal.dismiss();
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
        presenter.processGetCartData();
    }

    @Override
    public void renderCartDigitalInfoData(CartDigitalInfoData cartDigitalInfoData) {
        this.cartDigitalInfoDataState = cartDigitalInfoData;
        renderCartInfo(cartDigitalInfoData);
    }

    private void renderCartInfo(CartDigitalInfoData cartDigitalInfoData) {
        buildCheckoutData(cartDigitalInfoData);
        actionListener.setTitleCart(cartDigitalInfoData.getTitle());
        itemCartHolderView.renderAdditionalInfo(cartDigitalInfoData.getAdditionalInfos());
        itemCartHolderView.renderDataMainInfo(cartDigitalInfoData.getMainInfo());
        itemCartHolderView.setCategoryName(cartDigitalInfoData.getAttributes().getCategoryName());
        itemCartHolderView.setOperatorName(cartDigitalInfoData.getAttributes().getOperatorName());
        itemCartHolderView.setImageItemCart(cartDigitalInfoData.getAttributes().getIcon());
        renderDataInputPrice(String.valueOf(cartDigitalInfoData.getAttributes().getPricePlain()),
                cartDigitalInfoData.getAttributes().getUserInputPrice());
        checkoutHolderView.renderData(
                this,
                cartDigitalInfoData.getAttributes().getPrice(),
                cartDigitalInfoData.getAttributes().getPrice()
        );
        if (passData.getInstantCheckout().equals("1")) {
            pbMainLoading.setVisibility(View.VISIBLE);
            mainContainer.setVisibility(View.GONE);
            presenter.processToInstantCheckout();
        } else {
            pbMainLoading.setVisibility(View.GONE);
            mainContainer.setVisibility(View.VISIBLE);
        }
    }

    private void buildCheckoutData(CartDigitalInfoData cartDigitalInfoData) {
        checkoutDataBuilder.accessToken(getAccountToken());
        checkoutDataBuilder.walletRefreshToken(getWalletRefreshToken());
        checkoutDataBuilder.ipAddress(DeviceUtil.getLocalIpAddress());
        checkoutDataBuilder.relationId(cartDigitalInfoData.getId());
        checkoutDataBuilder.relationType(cartDigitalInfoData.getType());
        checkoutDataBuilder.transactionAmount(cartDigitalInfoData.getAttributes().getPricePlain());
        checkoutDataBuilder.userAgent(DeviceUtil.getUserAgentForApiCall());
        checkoutDataBuilder.needOtp(cartDigitalInfoData.isNeedOtp());
    }

    @Override
    public void renderErrorGetCartData(String message) {
        closeViewWithMessageAlert(message);
    }

    @Override
    public void renderErrorHttpGetCartData(String message) {
        closeViewWithMessageAlert(message);
    }

    @Override
    public void renderErrorNoConnectionGetCartData(String message) {
        closeViewWithMessageAlert(message);
    }

    @Override
    public void renderErrorTimeoutConnectionGetCartData(String message) {
        closeViewWithMessageAlert(message);
    }

    private void renderDataInputPrice(String total, UserInputPriceDigital userInputPriceDigital) {
        if (userInputPriceDigital != null) {
            checkoutDataBuilder.transactionAmount(0);
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
    public void renderLoadingAddToCart() {
        pbMainLoading.setVisibility(View.VISIBLE);
        mainContainer.setVisibility(View.GONE);
    }

    @Override
    public void renderAddToCartData(CartDigitalInfoData cartDigitalInfoData) {
        this.cartDigitalInfoDataState = cartDigitalInfoData;
        renderCartInfo(cartDigitalInfoData);
    }

    @Override
    public void renderErrorAddToCart(String message) {
        closeViewWithMessageAlert(message);
    }

    @Override
    public void renderErrorHttpAddToCart(String message) {
        closeViewWithMessageAlert(message);
    }

    @Override
    public void renderErrorNoConnectionAddToCart(String message) {
        closeViewWithMessageAlert(message);
    }

    @Override
    public void renderErrorTimeoutConnectionAddToCart(String message) {
        closeViewWithMessageAlert(message);
    }

    @Override
    public void renderLoadingGetCartInfo() {
        pbMainLoading.setVisibility(View.VISIBLE);
        mainContainer.setVisibility(View.GONE);
    }

    @Override
    public void renderVoucherInfoData(VoucherDigital voucherDigital) {
        this.voucherDigitalState = voucherDigital;
        voucherCartHolderView.setUsedVoucher(
                voucherDigital.getAttributeVoucher().getVoucherCode(),
                voucherDigital.getAttributeVoucher().getMessage());
    }

    @Override
    public void closeViewWithMessageAlert(String message) {
        pbMainLoading.setVisibility(View.GONE);
        mainContainer.setVisibility(View.GONE);
        Intent intent = new Intent();
        intent.putExtra(IDigitalModuleRouter.EXTRA_MESSAGE, message);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
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
    public void renderErrorCheckVoucher(String message) {
        voucherCartHolderView.setErrorVoucher(message);
    }

    @Override
    public void renderErrorHttpCheckVoucher(String message) {
        showToastMessage(message);
    }

    @Override
    public void renderErrorNoConnectionCheckVoucher(String message) {
        NetworkErrorHelper.showDialogCustomMSG(getActivity(),
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.processCheckVoucher();
                    }
                }, message
        );
    }

    @Override
    public void renderErrorTimeoutConnectionCheckVoucher(String message) {
        showToastMessage(message);
    }

    @Override
    public void renderToTopPay(CheckoutDigitalData checkoutDigitalData) {
        PaymentPassData paymentPassData = new PaymentPassData();
        paymentPassData.convertToPaymenPassData(checkoutDigitalData);
        navigateToActivityRequest(TopPayActivity.createInstance(getActivity(), paymentPassData),
                TopPayActivity.REQUEST_CODE);
    }

    @Override
    public void renderErrorCheckout(String message) {
        showToastMessage(message);
    }

    @Override
    public void renderErrorHttpCheckout(String message) {
        showToastMessage(message);
    }

    @Override
    public void renderErrorNoConnectionCheckout(String message) {
        NetworkErrorHelper.showDialog(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.processToCheckout();
            }
        });
    }

    @Override
    public void renderErrorTimeoutConnectionCheckout(String message) {
        showToastMessage(message);
    }

    @Override
    public void renderToInstantCheckoutPage(InstantCheckoutData instantCheckoutData) {
        navigateToActivityRequest(
                InstantCheckoutActivity.newInstance(getActivity(), instantCheckoutData),
                InstantCheckoutActivity.REQUEST_CODE
        );
        closeView();
    }

    @Override
    public void renderErrorInstantCheckout(String message) {
        closeViewWithMessageAlert(message);
    }

    @Override
    public void renderErrorHttpInstantCheckout(String message) {
        closeViewWithMessageAlert(message);
    }

    @Override
    public void renderErrorNoConnectionInstantCheckout(String message) {
        NetworkErrorHelper.showDialog(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.processToInstantCheckout();
            }
        });
    }

    @Override
    public void renderErrorTimeoutConnectionInstantCheckout(String message) {
        closeViewWithMessageAlert(message);
    }

    @Override
    public String getDigitalCategoryId() {
        return passData.getCategoryId();
    }

    @Override
    public String getVoucherCode() {
        return voucherCartHolderView.getVoucherCode();
    }

    @Override
    public CheckoutDataParameter getCheckoutData() {
        checkoutDataBuilder.voucherCode(voucherCartHolderView.getVoucherCode());
        return checkoutDataBuilder.build();
    }

    @Override
    public String getClientNumber() {
        return passData.getClientNumber();
    }

    @Override
    public boolean isInstantCheckout() {
        return passData.getInstantCheckout().equals("1");
    }

    @Override
    public int getProductId() {
        return Integer.parseInt(passData.getProductId());
    }

    @Override
    public String getIdemPotencyKey() {
        return passData.getIdemPotencyKey();
    }

    @Override
    public void clearContentRendered() {
        pbMainLoading.setVisibility(View.GONE);
        mainContainer.setVisibility(View.GONE);
    }

    @Override
    public void interruptRequestTokenVerification() {
        navigateToActivityRequest(
                OtpVerificationActivity.newInstance(getActivity()),
                OtpVerificationActivity.REQUEST_CODE
        );
    }

    @Override
    public void onClickButtonNext() {
        presenter.processToCheckout();
    }

    @Override
    public void onInputPriceByUserFilled(long paymentAmount) {
        Log.d(TAG, "userInputPayment: " + paymentAmount);
        checkoutDataBuilder.transactionAmount(paymentAmount);
    }

    @Override
    public void onVoucherCheckButtonClicked() {
        presenter.processCheckVoucher();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OtpVerificationActivity.REQUEST_CODE) {
            String message = data.getStringExtra(OtpVerificationActivity.EXTRA_MESSAGE);
            switch (resultCode) {
                case OtpVerificationActivity.RESULT_OTP_VERIFIED:
                    Log.d(TAG, "OTP VERIFIED");
                    presenter.processPatchOtpCart();
                    if (message != null) showToastMessage(message);
                    break;
                default:
                    Log.d(TAG, "OTP NOT VERIFIED");
                    if (message != null) closeViewWithMessageAlert(message);
                    else closeView();
                    break;
            }
        } else if (requestCode == TopPayActivity.REQUEST_CODE) {
            switch (resultCode) {
                case TopPayActivity.PAYMENT_SUCCESS:
                    closeView();
                    break;
                case TopPayActivity.PAYMENT_FAILED:
                    showToastMessage(
                            getString(R.string.alert_payment_canceled_or_failed_digital_module)
                    );
                    presenter.processGetCartDataAfterCheckout();
                    break;
                case TopPayActivity.PAYMENT_CANCELLED:
                    showToastMessage(getString(R.string.alert_payment_canceled_digital_module));
                    presenter.processGetCartDataAfterCheckout();
                    break;
                default:
                    presenter.processGetCartData();
                    break;
            }
        } else if (requestCode == InstantCheckoutActivity.REQUEST_CODE) {
            closeView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeSubscription != null && compositeSubscription.hasSubscriptions())
            compositeSubscription.unsubscribe();
    }

    public interface ActionListener {
        void setTitleCart(String title);
    }
}