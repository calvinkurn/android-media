package com.tokopedia.digital.cart.presentation.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier;
import com.tokopedia.common_digital.cart.view.activity.InstantCheckoutActivity;
import com.tokopedia.common_digital.cart.view.model.cart.CartAutoApplyVoucher;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.common_digital.cart.view.model.cart.UserInputPriceDigital;
import com.tokopedia.common_digital.cart.view.model.checkout.CheckoutDataParameter;
import com.tokopedia.common_digital.cart.view.model.checkout.InstantCheckoutData;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.design.voucher.VoucherCartHachikoView;
import com.tokopedia.digital.R;
import com.tokopedia.digital.cart.data.cache.DigitalPostPaidLocalCache;
import com.tokopedia.digital.cart.data.mapper.CartMapperData;
import com.tokopedia.digital.cart.data.mapper.ICartMapperData;
import com.tokopedia.digital.cart.fragment.DigitalPostPaidDialog;
import com.tokopedia.digital.common.data.apiservice.DigitalEndpointService;
import com.tokopedia.digital.common.router.DigitalModuleRouter;
import com.tokopedia.digital.common.util.DigitalAnalytics;
import com.tokopedia.digital.cart.di.DigitalCartComponentInstance;
import com.tokopedia.digital.cart.presentation.compoundview.CheckoutHolderView;
import com.tokopedia.digital.cart.presentation.compoundview.InputPriceHolderView;
import com.tokopedia.digital.cart.presentation.compoundview.ItemCartHolderView;
import com.tokopedia.digital.cart.presentation.model.CheckoutDigitalData;
import com.tokopedia.digital.cart.presentation.model.VoucherAttributeDigital;
import com.tokopedia.digital.cart.presentation.model.VoucherDigital;
import com.tokopedia.digital.cart.presentation.presenter.CartDigitalContract;
import com.tokopedia.digital.cart.presentation.presenter.CartDigitalPresenter;
import com.tokopedia.digital.cart.presentation.presenter.ICartDigitalPresenter;
import com.tokopedia.digital.utils.DeviceUtil;
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.payment.activity.TopPayActivity;
import com.tokopedia.payment.model.PaymentPassData;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;


/**
 * @author anggaprasetiyo on 2/21/17.
 */

public class CartDigitalFragment extends BasePresenterFragment<ICartDigitalPresenter> implements
        CheckoutHolderView.IAction,
        InputPriceHolderView.ActionListener, VoucherCartHachikoView.ActionListener,
        CartDigitalContract.View {
    private static final int REQUEST_CODE_LOGIN = 1000;
    private static final int REQUEST_CODE_OTP = 1001;

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

    private final int COUPON_ACTIVE = 1;

    private CheckoutHolderView checkoutHolderView;
    private ItemCartHolderView itemCartHolderView;
    private VoucherCartHachikoView voucherCartHachikoView;
    private ProgressBar pbMainLoading;
    private NestedScrollView mainContainer;
    private InputPriceHolderView inputPriceHolderView;

    private SessionHandler sessionHandler;
    private ActionListener actionListener;
    private TkpdProgressDialog progressDialogNormal;
    private CheckoutDataParameter.Builder checkoutDataBuilder;

    private DigitalCheckoutPassData passData;
    private CartDigitalInfoData cartDigitalInfoDataState;
    private VoucherDigital voucherDigitalState;
    private boolean isAlreadyShowPostPaidPopUp;

    @Inject
    CartDigitalPresenter presenter;

    public static Fragment newInstance(DigitalCheckoutPassData passData) {
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
        presenter.onFirstTimeLaunched();
    }


    @Override
    public void navigateToLoggedInPage() {
        if (getActivity() != null && getActivity().getApplication() instanceof DigitalModuleRouter) {
            Intent intent = ((DigitalModuleRouter) getActivity().getApplication())
                    .getLoginIntent(getActivity());
            if (intent != null) {
                navigateToActivityRequest(intent, REQUEST_CODE_LOGIN);
            }
        }
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
    protected void initInjector() {
        super.initInjector();

        DigitalCartComponentInstance.getDigitalCartComponent(getActivity().getApplication())
                .inject(this);
    }

    @Override
    protected void initialPresenter() {
        presenter.attachView(this);
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
        checkoutHolderView = view.findViewById(R.id.checkout_cart_holder_view);
        itemCartHolderView = view.findViewById(R.id.item_cart_holder_view);
        voucherCartHachikoView = view.findViewById(R.id.voucher_cart_holder_view);
        pbMainLoading = view.findViewById(R.id.pb_main_loading);
        mainContainer = view.findViewById(R.id.nsv_container);
        inputPriceHolderView = view.findViewById(R.id.input_price_holder_view);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {
        checkoutDataBuilder = new CheckoutDataParameter.Builder();
        inputPriceHolderView.setActionListener(this);
        voucherCartHachikoView.setActionListener(this);
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
    public void showProgressLoading(String title, String message) {
        progressDialogNormal.showDialog(title, message);
    }

    @Override
    public CartDigitalInfoData getCartDataInfo() {
        return cartDigitalInfoDataState;
    }

    @Override
    public void showPostPaidDialog(String title,
                                   String content,
                                   String confirmButtonTitle) {
        isAlreadyShowPostPaidPopUp = true;
        DigitalPostPaidDialog dialog = new DigitalPostPaidDialog(
                getActivity(),
                Dialog.Type.RETORIC,
                DigitalPostPaidLocalCache.newInstance(getActivity()),
                getUserId()
        );
        dialog.setTitle(title);
        dialog.setDesc(MethodChecker.fromHtml(content));
        dialog.setBtnOk(confirmButtonTitle);
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public boolean isAlreadyShowPostPaid() {
        return isAlreadyShowPostPaidPopUp;
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
    public String getStringFromResource(@StringRes int resId) {
        return getString(resId);
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams) {
        return AuthUtil.generateParamsNetwork(getActivity(), originParams);
    }

    @Override
    public RequestBodyIdentifier getDigitalIdentifierParam() {
        return DeviceUtil.getDigitalIdentifierParam(getActivity());
    }

    @Override
    public void closeView() {
        getActivity().finish();
    }

    @Override
    public void renderCartDigitalInfoData(CartDigitalInfoData cartDigitalInfoData) {
        this.cartDigitalInfoDataState = cartDigitalInfoData;
        renderCartInfo(cartDigitalInfoData);
    }

    private void renderCartInfo(CartDigitalInfoData cartDigitalInfoData) {
        buildCheckoutData(cartDigitalInfoData);
        actionListener.setTitleCart(cartDigitalInfoData.getTitle());
        if (GlobalConfig.isSellerApp()) {
            voucherCartHachikoView.setVisibility(View.GONE);
        } else {
            if (cartDigitalInfoData.getAttributes().isEnableVoucher()) {
                voucherCartHachikoView.setVisibility(View.VISIBLE);
                if (cartDigitalInfoData.getAttributes().isCouponActive() == COUPON_ACTIVE) {
                    voucherCartHachikoView.setPromoAndCouponLabel();
                } else {
                    voucherCartHachikoView.setPromoLabelOnly();
                }
            } else {
                voucherCartHachikoView.setVisibility(View.GONE);
            }

            UnifyTracking.eventClickVoucher(getActivity(),cartDigitalInfoDataState.getAttributes().getCategoryName(),
                    cartDigitalInfoData.getAttributes().getVoucherAutoCode(),
                    cartDigitalInfoDataState.getAttributes().getOperatorName());

            if (cartDigitalInfoData.getAttributes().isEnableVoucher() &&
                    cartDigitalInfoData.getAttributes().getAutoApplyVoucher() != null &&
                    cartDigitalInfoData.getAttributes().getAutoApplyVoucher().isSuccess()) {
                if (!(cartDigitalInfoData.getAttributes().isCouponActive() == 0 && cartDigitalInfoData.getAttributes().getAutoApplyVoucher().isCoupon() == 1)) {
                    CartAutoApplyVoucher cartAutoApplyVoucher = cartDigitalInfoData.getAttributes().getAutoApplyVoucher();
                    VoucherDigital voucherDigital = new VoucherDigital();
                    VoucherAttributeDigital voucherAttributeDigital = new VoucherAttributeDigital();
                    voucherAttributeDigital.setVoucherCode(cartAutoApplyVoucher.getCode());
                    voucherAttributeDigital.setDiscountAmountPlain(cartAutoApplyVoucher.getDiscountAmount());
                    voucherAttributeDigital.setMessage(cartAutoApplyVoucher.getMessageSuccess());
                    voucherAttributeDigital.setIsCoupon(cartAutoApplyVoucher.isCoupon());
                    voucherAttributeDigital.setTitle(cartAutoApplyVoucher.getTitle());
                    voucherDigital.setAttributeVoucher(voucherAttributeDigital);

                    renderCouponAndVoucher(voucherDigital);
                }
            }
        }
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
                cartDigitalInfoData.getAttributes().getPrice(),
                cartDigitalInfoData.getAttributes().getPricePlain()
        );
        if (voucherDigitalState != null) {
            renderCouponAndVoucher(voucherDigitalState);
        }
        if (passData.getInstantCheckout().equals("1") && !cartDigitalInfoData.isForceRenderCart()) {
            pbMainLoading.setVisibility(View.VISIBLE);
            mainContainer.setVisibility(View.GONE);
            presenter.processToInstantCheckout();
        } else {
            pbMainLoading.setVisibility(View.GONE);
            mainContainer.setVisibility(View.VISIBLE);
        }

        presenter.sendAnalyticsATCSuccess(cartDigitalInfoData, passData.getSource());

        sendGTMAnalytics(
                cartDigitalInfoData.getAttributes().getCategoryName(),
                cartDigitalInfoData.getAttributes().getOperatorName()
                        + " - " + cartDigitalInfoData.getAttributes().getPricePlain(),
                cartDigitalInfoData.isInstantCheckout()
        );

        presenter.autoApplyCouponIfAvailable(passData.getCategoryId());
    }

    private void renderCouponAndVoucher(VoucherDigital voucherDigital) {
        if (voucherDigital.getAttributeVoucher().getIsCoupon() == 1) {
            renderCouponInfoData(voucherDigital);
        } else {
            renderVoucherInfoData(voucherDigital);
        }
    }

    private void sendGTMAnalytics(String ec, String el, boolean analyticsKind) {
        UnifyTracking.eventViewCheckoutPage(getActivity(),ec, el);

        if (analyticsKind) {
            UnifyTracking.eventClickBeliInstantSaldoWidget(getActivity(),ec, el);
        }
        {
            UnifyTracking.eventClickBeliWidget(getActivity(),ec, el);
        }
    }

    private void buildCheckoutData(CartDigitalInfoData cartDigitalInfoData) {
        checkoutDataBuilder.cartId(cartDigitalInfoData.getId());
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
        voucherCartHachikoView.setVoucher(
                voucherDigital.getAttributeVoucher().getVoucherCode(),
                voucherDigital.getAttributeVoucher().getMessage());
        if (voucherDigital.getAttributeVoucher().getDiscountAmountPlain() > 0) {
            checkoutHolderView.enableVoucherDiscount(
                    voucherDigital.getAttributeVoucher().getDiscountAmountPlain()
            );
        }
    }

    @Override
    public void renderCouponInfoData(VoucherDigital voucherDigital) {
        this.voucherDigitalState = voucherDigital;
        voucherCartHachikoView.setCoupon(voucherDigital.getAttributeVoucher().getTitle(),
                voucherDigital.getAttributeVoucher().getMessage(),
                voucherDigital.getAttributeVoucher().getVoucherCode()
        );
        if (voucherDigital.getAttributeVoucher().getDiscountAmountPlain() > 0) {
            checkoutHolderView.enableVoucherDiscount(voucherDigital.getAttributeVoucher().getDiscountAmountPlain());
        }
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
        return SessionHandler.getAccessToken(getActivity());
    }

    @Override
    public String getWalletRefreshToken() {
        return sessionHandler.getWalletRefreshToken(getActivity());
    }

    private void showSnackBarAlert(String message) {
        View view = getView();
        if (view != null) Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
        showSnackBarAlert(message);
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
        showToastMessage(message);
        presenter.processGetCartDataAfterCheckout(passData.getCategoryId());
    }

    @Override
    public void renderErrorHttpInstantCheckout(String message) {
        closeViewWithMessageAlert(message);
    }

    @Override
    public void renderErrorNoConnectionInstantCheckout(String message) {
        closeViewWithMessageAlert(message);
    }

    @Override
    public void renderErrorTimeoutConnectionInstantCheckout(String message) {
        closeViewWithMessageAlert(message);
    }

    @Override
    public CheckoutDataParameter getCheckoutData() {
        checkoutDataBuilder.voucherCode(voucherCartHachikoView.getVoucherCode());
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

    public void setCartDigitalInfo(CartDigitalInfoData cartDigitalInfoData) {
        if (cartDigitalInfoData != null) {
            this.cartDigitalInfoDataState = cartDigitalInfoData;
            buildCheckoutData(cartDigitalInfoData);
        }
    }

    @Override
    public Context getApplicationContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public DigitalCheckoutPassData getPassData() {
        return passData;
    }

    @Override
    public void interruptRequestTokenVerification() {
        Intent intent = VerificationActivity.getCallingIntent(getActivity(),
                SessionHandler.getPhoneNumber(), RequestOtpUseCase.OTP_TYPE_CHECKOUT_DIGITAL,
                true, RequestOtpUseCase.MODE_SMS);
        startActivityForResult(intent, REQUEST_CODE_OTP);
    }

    @Override
    public void onClickButtonNext() {
        UnifyTracking.eventClickLanjutCheckoutPage(
                getActivity(),
                cartDigitalInfoDataState.getAttributes().getCategoryName(),
                cartDigitalInfoDataState.getAttributes().getOperatorName()
                        + " - " + cartDigitalInfoDataState.getAttributes().getPricePlain()
        );
        presenter.processToCheckout();
    }

    @Override
    public void onInputPriceByUserFilled(long paymentAmount) {
        Log.d(TAG, "userInputPayment: " + paymentAmount);
        checkoutDataBuilder.transactionAmount(paymentAmount);
    }

    @Override
    public void enableCheckoutButton() {
        checkoutHolderView.enableCheckoutButton();
    }

    @Override
    public void disableCheckoutButton() {
        checkoutHolderView.disableCheckoutButton();
    }

    @Override
    public void onClickUseVoucher() {
        if (cartDigitalInfoDataState.getAttributes().isEnableVoucher()) {
            if(getApplicationContext() instanceof DigitalModuleRouter) {
                DigitalModuleRouter digitalModuleRouter = ((DigitalModuleRouter)getApplicationContext());
                Intent intent;
                if (cartDigitalInfoDataState.getAttributes().isCouponActive() == COUPON_ACTIVE) {
                    if (cartDigitalInfoDataState.getAttributes().getDefaultPromoTab() != null &&
                            cartDigitalInfoDataState.getAttributes().getDefaultPromoTab().equalsIgnoreCase(
                                    IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_STATE)) {
                        intent = digitalModuleRouter.getLoyaltyActivitySelectedCoupon(
                                context, IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.DIGITAL_STRING, passData.getCategoryId()
                        );
                    } else {
                        intent = digitalModuleRouter.getLoyaltyActivity(
                                context, IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.DIGITAL_STRING, passData.getCategoryId()
                        );
                    }
                } else {
                    intent = digitalModuleRouter.getLoyaltyActivityNoCouponActive(
                            context, IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.DIGITAL_STRING,
                            passData.getCategoryId()
                    );
                }
                navigateToActivityRequest(intent, IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE);
            }
        } else {
            voucherCartHachikoView.setVisibility(View.GONE);
        }
    }

    @Override
    public void disableVoucherDiscount() {
        presenter.onClearVoucher();
        this.voucherDigitalState = null;
        checkoutHolderView.disableVoucherDiscount();
    }

    @Override
    public void trackingSuccessVoucher(String voucherCode) {
        UnifyTracking.eventVoucherSuccess(getActivity(),voucherCode, "");
    }

    @Override
    public void trackingCancelledVoucher() {
        UnifyTracking.eventClickCancelVoucher(getActivity(),"", "");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OTP) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.processPatchOtpCart(passData.getCategoryId());
            } else {
                closeView();
            }
        } else if (requestCode == TopPayActivity.REQUEST_CODE) {
            switch (resultCode) {
                case TopPayActivity.PAYMENT_SUCCESS:
                    if (getApplicationContext() instanceof DigitalModuleRouter) {
                        ((DigitalModuleRouter) getApplicationContext()).
                                showAdvancedAppRatingDialog(getActivity(), dialog -> {
                                    getActivity().setResult(IDigitalModuleRouter.PAYMENT_SUCCESS);
                                    closeView();
                                });
                    }
                    presenter.onPaymentSuccess(passData.getCategoryId());

                    break;
                case TopPayActivity.PAYMENT_FAILED:
                    showToastMessage(
                            getString(R.string.alert_payment_canceled_or_failed_digital_module)
                    );
                    presenter.processGetCartDataAfterCheckout(passData.getCategoryId());
                    break;
                case TopPayActivity.PAYMENT_CANCELLED:
                    showToastMessage(getString(R.string.alert_payment_canceled_digital_module));
                    presenter.processGetCartDataAfterCheckout(passData.getCategoryId());
                    break;
                default:
                    presenter.processGetCartData(passData.getCategoryId());
                    break;
            }
        } else if (requestCode == InstantCheckoutActivity.REQUEST_CODE) {
            closeView();
        } else if (requestCode == IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE) {
            if (resultCode == IRouterConstant.LoyaltyModule.ResultLoyaltyActivity.VOUCHER_RESULT_CODE) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    String voucherCode = bundle.getString(
                            IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_CODE, "");
                    String voucherMessage = bundle.getString(
                            IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_MESSAGE, "");
                    long voucherDiscountAmount = bundle.getLong(
                            IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_DISCOUNT_AMOUNT);

                    VoucherDigital voucherDigital = new VoucherDigital();
                    VoucherAttributeDigital voucherAttributeDigital = new VoucherAttributeDigital();
                    voucherAttributeDigital.setVoucherCode(voucherCode);
                    voucherAttributeDigital.setDiscountAmountPlain(voucherDiscountAmount);
                    voucherAttributeDigital.setMessage(voucherMessage);
                    voucherAttributeDigital.setIsCoupon(0);
                    voucherDigital.setAttributeVoucher(voucherAttributeDigital);

                    voucherDigitalState = voucherDigital;

                    voucherCartHachikoView.setVoucher(voucherCode, voucherMessage);

                    if (voucherDiscountAmount > 0) {
                        checkoutHolderView.enableVoucherDiscount(voucherDiscountAmount);
                    }
                }
            } else if (resultCode == IRouterConstant.LoyaltyModule.ResultLoyaltyActivity.COUPON_RESULT_CODE) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    String couponTitle = bundle.getString(
                            IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_TITLE, "");
                    String couponMessage = bundle.getString(
                            IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_MESSAGE, "");
                    String couponCode = bundle.getString(
                            IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_CODE, "");
                    long couponDiscountAmount = bundle.getLong(
                            IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_DISCOUNT_AMOUNT);

                    VoucherDigital voucherDigital = new VoucherDigital();
                    VoucherAttributeDigital voucherAttributeDigital = new VoucherAttributeDigital();
                    voucherAttributeDigital.setIsCoupon(1);
                    voucherAttributeDigital.setTitle(couponTitle);
                    voucherAttributeDigital.setVoucherCode(couponCode);
                    voucherAttributeDigital.setDiscountAmountPlain(couponDiscountAmount);
                    voucherAttributeDigital.setMessage(couponMessage);
                    voucherDigital.setAttributeVoucher(voucherAttributeDigital);

                    voucherDigitalState = voucherDigital;

                    voucherCartHachikoView.setCoupon(couponTitle, couponMessage, couponCode);

                    if (couponDiscountAmount > 0) {
                        checkoutHolderView.enableVoucherDiscount(couponDiscountAmount);
                    }
                }
            }
        } else if (requestCode == REQUEST_CODE_LOGIN) {
            presenter.onLoginResultReceived();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        progressDialogNormal.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    public interface ActionListener {
        void setTitleCart(String title);
    }

}
