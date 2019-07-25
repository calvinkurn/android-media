package com.tokopedia.digital.newcart.presentation.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalPromo;
import com.tokopedia.cachemanager.SaveInstanceCacheManager;
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier;
import com.tokopedia.common_digital.cart.view.activity.InstantCheckoutActivity;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.cart.view.model.cart.CartAdditionalInfo;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.common_digital.cart.view.model.cart.CartItemDigital;
import com.tokopedia.common_digital.cart.view.model.cart.UserInputPriceDigital;
import com.tokopedia.common_digital.cart.view.model.checkout.CheckoutDataParameter;
import com.tokopedia.common_digital.cart.view.model.checkout.InstantCheckoutData;
import com.tokopedia.common_digital.common.DigitalRouter;
import com.tokopedia.common_digital.common.constant.DigitalExtraParam;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.digital.R;
import com.tokopedia.digital.common.analytic.DigitalAnalytics;
import com.tokopedia.digital.common.router.DigitalModuleRouter;
import com.tokopedia.digital.newcart.data.cache.DigitalPostPaidLocalCache;
import com.tokopedia.digital.newcart.domain.model.CheckoutDigitalData;
import com.tokopedia.digital.newcart.presentation.compoundview.DigitalCartCheckoutHolderView;
import com.tokopedia.digital.newcart.presentation.compoundview.DigitalCartDetailHolderView;
import com.tokopedia.digital.newcart.presentation.compoundview.InputPriceHolderView;
import com.tokopedia.digital.newcart.presentation.contract.DigitalBaseContract;
import com.tokopedia.digital.utils.DeviceUtil;
import com.tokopedia.loyalty.view.activity.LoyaltyActivity;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.payment.activity.TopPayActivity;
import com.tokopedia.payment.model.PaymentPassData;
import com.tokopedia.promocheckout.common.data.ConstantKt;
import com.tokopedia.promocheckout.common.util.TickerCheckoutUtilKt;
import com.tokopedia.promocheckout.common.view.model.PromoData;
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel;
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView;

import java.util.List;
import java.util.Map;

public abstract class DigitalBaseCartFragment<P extends DigitalBaseContract.Presenter> extends BaseDaggerFragment
        implements DigitalBaseContract.View,
        InputPriceHolderView.ActionListener,
        TickerCheckoutView.ActionListener,
        DigitalCartCheckoutHolderView.ActionListener {
    protected static final String ARG_PASS_DATA = "ARG_PASS_DATA";
    protected static final String ARG_CART_INFO = "ARG_CART_INFO";
    private static final int REQUEST_CODE_OTP = 1001;

    protected CartDigitalInfoData cartDigitalInfoData;
    protected CheckoutDataParameter.Builder checkoutDataParameterBuilder;

    protected DigitalCheckoutPassData cartPassData;
    protected DigitalCartDetailHolderView detailHolderView;
    protected DigitalCartCheckoutHolderView checkoutHolderView;
    protected InputPriceHolderView inputPriceHolderView;
    protected LinearLayout inputPriceContainer;
    private boolean isAlreadyShowPostPaidPopUp;
    private boolean traceStop;
    private PerformanceMonitoring performanceMonitoring;
    private static final String DIGITAL_CHECKOUT_TRACE = "dg_checkout";
    private SaveInstanceCacheManager saveInstanceCacheManager;
    private DigitalAnalytics digitalAnalytics;
    protected PromoData promoData = new PromoData();

    private static final String EXTRA_STATE_CHECKOUT_DATA_PARAMETER_BUILDER = "EXTRA_STATE_CHECKOUT_DATA_PARAMETER_BUILDER";
    private static final String EXTRA_STATE_PROMO_DATA = "EXTRA_STATE_PROMO_DATA";

    protected P presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cartPassData = getArguments().getParcelable(ARG_PASS_DATA);
        saveInstanceCacheManager = new SaveInstanceCacheManager(getActivity(), savedInstanceState);
        digitalAnalytics = new DigitalAnalytics();
        if (savedInstanceState != null) {
            promoData = savedInstanceState.getParcelable(EXTRA_STATE_PROMO_DATA);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            checkoutDataParameterBuilder = saveInstanceCacheManager.get(EXTRA_STATE_CHECKOUT_DATA_PARAMETER_BUILDER,
                    CheckoutDataParameter.Builder.class, null);
        }
        setupView(view);
        presenter.attachView(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(EXTRA_STATE_PROMO_DATA, promoData);
        super.onSaveInstanceState(outState);
        saveInstanceCacheManager.onSave(outState);
        saveInstanceCacheManager.put(EXTRA_STATE_CHECKOUT_DATA_PARAMETER_BUILDER, checkoutDataParameterBuilder);
    }

    protected abstract void setupView(View view);

    @Override
    public CartDigitalInfoData getCartInfoData() {
        return cartDigitalInfoData;
    }

    @Override
    public void renderPromoTicker() {
        checkoutHolderView.setPromoTickerActionListener(this);
        checkoutHolderView.showPromoTicker();
    }

    @Override
    public void hidePromoTicker() {
        checkoutHolderView.hidePromoTicker();
    }

    @Override
    public void resetPromoTicker() {
        checkoutHolderView.resetPromoTicker();
    }

    @Override
    public void renderPromo() {
        checkoutHolderView.setPromoTickerActionListener(this);
        checkoutHolderView.setPromoInfo(promoData.getTitle(), promoData.getDescription(), promoData.getState());
    }

    @Override
    public void onAutoApplyPromo(String couponTitle, String couponMessage, String couponCode, int isCoupon) {
        promoData.setTitle(couponTitle);
        promoData.setDescription(couponMessage);
        promoData.setPromoCode(couponCode);
        promoData.setTypePromo(isCoupon);
        promoData.setState(TickerCheckoutView.State.ACTIVE);
    }

    @Override
    public void enableVoucherDiscount(long discountAmountPlain) {
        checkoutHolderView.enableVoucherDiscount(discountAmountPlain);
    }

    @Override
    public void renderDetailMainInfo(List<CartItemDigital> mainInfo) {
        detailHolderView.setMainInfo(mainInfo);
    }

    @Override
    public void renderAdditionalInfo(List<CartAdditionalInfo> additionalInfos) {
        detailHolderView.setAdditionalInfos(additionalInfos);
    }

    @Override
    public CheckoutDataParameter.Builder getCheckoutDataParameter() {
        checkoutDataParameterBuilder.voucherCode(promoData.getPromoCode());
        return checkoutDataParameterBuilder;
    }

    @Override
    public void renderInputPriceView(String total, UserInputPriceDigital userInputPriceDigital) {
        inputPriceHolderView.setActionListener(this);
        inputPriceContainer.setVisibility(View.VISIBLE);
        inputPriceHolderView.setInputPriceInfo(total, userInputPriceDigital.getMinPaymentPlain(),
                userInputPriceDigital.getMaxPaymentPlain());
        inputPriceHolderView.bindView(userInputPriceDigital.getMinPayment(),
                userInputPriceDigital.getMaxPayment());
    }

    @Override
    public void renderCheckoutView(long pricePlain) {
        checkoutHolderView.setActionListener(this);
        checkoutHolderView.renderCheckout(pricePlain);
    }

    @Override
    public DigitalCheckoutPassData getCartPassData() {
        return cartPassData;
    }

    @Override
    public Map<String, String> getGeneratedAuthParamNetwork(String userId, String deviceId, Map<String, String> param) {
        return AuthUtil.generateParamsNetwork(userId, deviceId, param);
    }

    @Override
    public void onInputPriceByUserFilled(long paymentAmount) {
        checkoutDataParameterBuilder.transactionAmount(paymentAmount);
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
    public void onClickUsePromo() {
        digitalAnalytics.eventclickUseVoucher(cartDigitalInfoData.getAttributes().getCategoryName());
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalPromo.PROMO_LIST_DIGITAL);
        intent.putExtra("EXTRA_COUPON_ACTIVE", cartDigitalInfoData.getAttributes().isCouponActive());
        intent.putExtra("EXTRA_PROMO_DIGITAL_MODEL", getPromoDigitalModel());
        startActivityForResult(intent, ConstantKt.getREQUST_CODE_LIST_PROMO());
    }

    private PromoDigitalModel getPromoDigitalModel() {
        return new PromoDigitalModel(
                Integer.parseInt(cartPassData.getCategoryId()),
                getProductId(),
                cartPassData.getClientNumber(),
                cartDigitalInfoData.getAttributes().getPricePlain()
        );
    }

    @Override
    public void onDisablePromoDiscount() {
        digitalAnalytics.eventclickCancelApplyCoupon(cartDigitalInfoData.getAttributes().getCategoryName(), promoData.getPromoCode());
        promoData.setPromoCode("");
    }

    @Override
    public void onClickDetailPromo() {
        Intent intent;
        String promoCode = promoData.getPromoCode();
        if (!promoCode.isEmpty()) {
            int requestCode;
            if (promoData.getTypePromo() == 0) {
                intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalPromo.PROMO_LIST_DIGITAL);
                intent.putExtra("EXTRA_PROMO_CODE", promoCode);
                intent.putExtra("EXTRA_COUPON_ACTIVE", cartDigitalInfoData.getAttributes().isCouponActive());
                requestCode = ConstantKt.getREQUST_CODE_LIST_PROMO();
            } else {
                intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalPromo.PROMO_DETAIL_DIGITAL);
                intent.putExtra("EXTRA_IS_USE", true);
                intent.putExtra("EXTRA_KUPON_CODE", promoCode);
                requestCode = ConstantKt.getREQUEST_CODE_PROMO_DETAIL();
            }
            intent.putExtra("EXTRA_PROMO_DIGITAL_MODEL", getPromoDigitalModel());
            startActivityForResult(intent, requestCode);
        } else {
            showToastMessage("Tidak ada promo yang sedang dipakai");
        }
    }

    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public RequestBodyIdentifier getDigitalIdentifierParam() {
        return DeviceUtil.getDigitalIdentifierParam(getActivity());
    }

    @Override
    public void disableVoucherCheckoutDiscount() {
        checkoutHolderView.disableVoucherDiscount();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == ConstantKt.getREQUST_CODE_LIST_PROMO() || requestCode == ConstantKt.getREQUEST_CODE_PROMO_DETAIL()) && resultCode == Activity.RESULT_OK) {
            if (data.hasExtra(TickerCheckoutUtilKt.getEXTRA_PROMO_DATA())) {
                promoData = data.getParcelableExtra(TickerCheckoutUtilKt.getEXTRA_PROMO_DATA());
                // Check between apply promo code or cancel promo from promo detail
                switch (promoData.getState()) {
                    case EMPTY: {
                        promoData.setPromoCode("");
                        resetPromoTicker();
                        break;
                    }
                    case FAILED: {
                        promoData.setPromoCode("");
                        presenter.onReceivePromoCode(promoData.getTitle(), promoData.getDescription(), promoData.getPromoCode(), promoData.getTypePromo());
                        break;
                    }
                    case ACTIVE: {
                        presenter.onReceivePromoCode(promoData.getTitle(), promoData.getDescription(), promoData.getPromoCode(), promoData.getTypePromo());
                        break;
                    }
                    default: { }
                }
            }
        } else if (requestCode == TopPayActivity.REQUEST_CODE) {
            switch (resultCode) {
                case TopPayActivity.PAYMENT_SUCCESS:
                    if (getActivity().getApplicationContext() instanceof DigitalModuleRouter) {
                        ((DigitalModuleRouter) getActivity().getApplicationContext()).
                                showAdvancedAppRatingDialog(getActivity(), dialog -> {
                                    getActivity().setResult(DigitalRouter.Companion.getPAYMENT_SUCCESS());
                                    closeView();
                                });
                    }

                    presenter.onPaymentSuccess(cartPassData.getCategoryId());

                    break;
                case TopPayActivity.PAYMENT_FAILED:
                    showToastMessage(
                            getString(R.string.alert_payment_canceled_or_failed_digital_module)
                    );
                    presenter.processGetCartDataAfterCheckout(cartPassData.getCategoryId());
                    break;
                case TopPayActivity.PAYMENT_CANCELLED:
                    showToastMessage(getString(R.string.alert_payment_canceled_digital_module));
                    presenter.processGetCartDataAfterCheckout(cartPassData.getCategoryId());
                    break;
                default:
                    presenter.processGetCartDataAfterCheckout(cartPassData.getCategoryId());
                    break;
            }
        } else if (requestCode == REQUEST_CODE_OTP) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.processPatchOtpCart(cartPassData.getCategoryId());
            } else {
                closeView();
            }
        } else if (requestCode == InstantCheckoutActivity.Companion.getREQUEST_CODE()) {
            closeView();
        }
    }

    private void closeView() {
        getActivity().finish();
    }

    @Override
    public void showToastMessage(String message) {
        View view = getView();
        if (view != null) ToasterError.showClose(getActivity(), message);
        else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCheckoutButtonClicked() {
        presenter.processToCheckout();
    }

    @Override
    public void renderToTopPay(CheckoutDigitalData checkoutDigitalData) {
        PaymentPassData paymentPassData = new PaymentPassData();
        paymentPassData.convertToPaymenPassData(checkoutDigitalData);
        navigateToActivityRequest(TopPayActivity.createInstance(getActivity(), paymentPassData),
                TopPayActivity.REQUEST_CODE);
    }


    @Override
    public String getIdemPotencyKey() {
        return cartPassData.getIdemPotencyKey();
    }

    @Override
    public String getClientNumber() {
        return cartPassData.getClientNumber();
    }

    @Override
    public boolean isInstantCheckout() {
        return cartPassData.getInstantCheckout().equals("1");
    }

    @Override
    public int getProductId() {
        String productIdString = cartPassData.getProductId();
        return TextUtils.isEmpty(productIdString) ? 0 : Integer.parseInt(productIdString);
    }

    @Override
    public void closeViewWithMessageAlert(String message) {
        Intent intent = new Intent();
        intent.putExtra(DigitalExtraParam.EXTRA_MESSAGE, message);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void setCartDigitalInfo(CartDigitalInfoData cartDigitalInfoData) {
        this.cartDigitalInfoData = cartDigitalInfoData;
    }

    @Override
    public void interruptRequestTokenVerification(String phoneNumber) {
        Intent intent = VerificationActivity.getCallingIntent(getActivity(),
                phoneNumber,
                RequestOtpUseCase.OTP_TYPE_CHECKOUT_DIGITAL,
                true,
                RequestOtpUseCase.MODE_SMS);
        startActivityForResult(intent, REQUEST_CODE_OTP);
    }

    @Override
    public CheckoutDataParameter getCheckoutData() {
        return checkoutDataParameterBuilder.build();
    }

    @Override
    public void renderErrorInstantCheckout(String message) {
        showToastMessage(message);
        presenter.processGetCartDataAfterCheckout(cartPassData.getCategoryId());
    }

    @Override
    public void renderToInstantCheckoutPage(InstantCheckoutData instantCheckoutData) {
        navigateToActivityRequest(
                InstantCheckoutActivity.Companion.newInstance(getActivity(), instantCheckoutData),
                InstantCheckoutActivity.Companion.getREQUEST_CODE()
        );
        closeView();
    }

    @Override
    public void expandAdditionalInfo() {
        detailHolderView.expandAdditional();
    }

    @Override
    public void setCheckoutParameter(CheckoutDataParameter.Builder builder) {
        checkoutDataParameterBuilder = builder;
    }

    @Override
    public boolean isAlreadyShowPostPaid() {
        return isAlreadyShowPostPaidPopUp;
    }

    @Override
    public void showPostPaidDialog(String title,
                                   String content,
                                   String confirmButtonTitle,
                                   String userId) {
        isAlreadyShowPostPaidPopUp = true;
        DigitalPostPaidDialog dialog = new DigitalPostPaidDialog(
                getActivity(),
                Dialog.Type.RETORIC,
                DigitalPostPaidLocalCache.newInstance(getActivity()),
                userId
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
    public void startPerfomanceMonitoringTrace() {
        performanceMonitoring = PerformanceMonitoring.start(DIGITAL_CHECKOUT_TRACE);
        performanceMonitoring.startTrace(DIGITAL_CHECKOUT_TRACE);
    }

    @Override
    public void stopPerfomanceMonitoringTrace() {
        if (!traceStop) {
            performanceMonitoring.stopTrace();
            traceStop = true;
        }
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }
}
