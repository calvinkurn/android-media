package com.tokopedia.digital.newcart.presentation.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.applink.internal.ApplinkConstInternalPayment;
import com.tokopedia.applink.internal.ApplinkConstInternalPromo;
import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.cachemanager.SaveInstanceCacheManager;
import com.tokopedia.common.payment.PaymentConstant;
import com.tokopedia.common.payment.model.PaymentPassData;
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.common.constant.DigitalExtraParam;
import com.tokopedia.device.info.DeviceInfo;
import com.tokopedia.dialog.DialogUnify;
import com.tokopedia.digital.R;
import com.tokopedia.digital.common.analytic.DigitalAnalytics;
import com.tokopedia.digital.newcart.domain.model.CheckoutDigitalData;
import com.tokopedia.digital.newcart.presentation.compoundview.DigitalCartCheckoutHolderView;
import com.tokopedia.digital.newcart.presentation.compoundview.DigitalCartDetailHolderView;
import com.tokopedia.digital.newcart.presentation.compoundview.DigitalCartMyBillsView;
import com.tokopedia.digital.newcart.presentation.compoundview.InputPriceHolderView;
import com.tokopedia.digital.newcart.presentation.contract.DigitalBaseContract;
import com.tokopedia.digital.newcart.presentation.model.DigitalSubscriptionParams;
import com.tokopedia.digital.newcart.presentation.model.cart.CartAdditionalInfo;
import com.tokopedia.digital.newcart.presentation.model.cart.CartDigitalInfoData;
import com.tokopedia.digital.newcart.presentation.model.cart.CartItemDigital;
import com.tokopedia.digital.newcart.presentation.model.cart.FintechProduct;
import com.tokopedia.digital.newcart.presentation.model.cart.UserInputPriceDigital;
import com.tokopedia.digital.newcart.presentation.model.checkout.CheckoutDataParameter;
import com.tokopedia.digital.utils.DeviceUtil;
import com.tokopedia.empty_state.EmptyStateUnify;
import com.tokopedia.network.constant.ErrorNetMessage;
import com.tokopedia.network.utils.ErrorHandler;
import com.tokopedia.promocheckout.common.data.ConstantKt;
import com.tokopedia.promocheckout.common.util.TickerCheckoutUtilKt;
import com.tokopedia.promocheckout.common.view.model.PromoData;
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel;
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView;
import com.tokopedia.unifycomponents.BottomSheetUnify;
import com.tokopedia.unifycomponents.Toaster;
import com.tokopedia.unifyprinciples.Typography;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import kotlin.Unit;


public abstract class DigitalBaseCartFragment<P extends DigitalBaseContract.Presenter> extends BaseDaggerFragment
        implements DigitalBaseContract.View,
        InputPriceHolderView.ActionListener,
        TickerCheckoutView.ActionListener,
        DigitalCartCheckoutHolderView.ActionListener,
        DigitalCartMyBillsView.OnMoreInfoClickListener {
    protected static final String ARG_PASS_DATA = "ARG_PASS_DATA";
    protected static final String ARG_CART_INFO = "ARG_CART_INFO";
    protected static final String ARG_SUBSCRIPTION_PARAMS = "ARG_SUBSCRIPTION_PARAMS";
    private static final int REQUEST_CODE_OTP = 1001;

    public static final int OTP_TYPE_CHECKOUT_DIGITAL = 16;

    protected CartDigitalInfoData cartDigitalInfoData;
    protected CheckoutDataParameter.Builder checkoutDataParameterBuilder;

    protected DigitalCheckoutPassData cartPassData;
    protected DigitalCartDetailHolderView detailHolderView;
    protected DigitalCartCheckoutHolderView checkoutHolderView;
    protected DigitalCartMyBillsView mybillEgold;
    protected InputPriceHolderView inputPriceHolderView;
    protected LinearLayout inputPriceContainer;
    private boolean traceStop;
    private PerformanceMonitoring performanceMonitoring;
    private static final String DIGITAL_CHECKOUT_TRACE = "dg_checkout";
    private SaveInstanceCacheManager saveInstanceCacheManager;
    private DigitalAnalytics digitalAnalytics;
    protected PromoData promoData = new PromoData();

    private DigitalSubscriptionParams digitalSubscriptionParams = new DigitalSubscriptionParams();

    private static final String EXTRA_STATE_CHECKOUT_DATA_PARAMETER_BUILDER = "EXTRA_STATE_CHECKOUT_DATA_PARAMETER_BUILDER";
    private static final String EXTRA_STATE_PROMO_DATA = "EXTRA_STATE_PROMO_DATA";

    protected P presenter;

    protected EmptyStateUnify emptyState;

    private static final String DIGITAL_CART_FAILED_TRANSACTION_IMAGE_URL = "https://images.tokopedia.net/img/android/res/singleDpi/ic_digital_checkout_failed_transaction.png";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        DeviceInfo.getAdsIdSuspend(requireContext(), null);
        super.onCreate(savedInstanceState);
        cartPassData = getArguments().getParcelable(ARG_PASS_DATA);
        DigitalSubscriptionParams subParams = getArguments().getParcelable(ARG_SUBSCRIPTION_PARAMS);
        if (subParams != null) {
            digitalSubscriptionParams = subParams;
        }
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
        return AuthHelper.generateParamsNetwork(userId, deviceId, param);
    }

    @Override
    public void onInputPriceByUserFilled(long paymentAmount) {
        checkoutHolderView.renderCheckout(paymentAmount);
        checkoutDataParameterBuilder.transactionAmount(paymentAmount);
        presenter.updateTotalPriceWithFintechAmount(mybillEgold.isChecked(), paymentAmount);
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
        presenter.onClickPromoButton();
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalPromo.PROMO_LIST_DIGITAL);
        intent.putExtra("EXTRA_COUPON_ACTIVE",
                Objects.requireNonNull(cartDigitalInfoData.getAttributes()).isCouponActive()
        );
        intent.putExtra("EXTRA_PROMO_DIGITAL_MODEL", getPromoDigitalModel());
        startActivityForResult(intent, ConstantKt.getREQUEST_CODE_PROMO_LIST());
    }

    private PromoDigitalModel getPromoDigitalModel() {
        Long price = cartDigitalInfoData.getAttributes() != null ? cartDigitalInfoData.getAttributes().getPricePlain() : 0;
        if (inputPriceContainer.getVisibility() == View.VISIBLE) {
            price = inputPriceHolderView.getPriceInput();
        }
        return new PromoDigitalModel(
                Integer.parseInt(Objects.requireNonNull(cartPassData.getCategoryId())),
                getCategoryName(),
                getOperatorName(),
                getProductId(),
                cartPassData.getClientNumber() != null ? cartPassData.getClientNumber() : "",
                price
        );
    }

    @Override
    public void onDisablePromoDiscount() {
        digitalAnalytics.eventclickCancelApplyCoupon(getCategoryName(), promoData.getPromoCode());
        presenter.cancelVoucherCart();
    }

    @Override
    public void successCancelVoucherCart() {
        checkoutHolderView.resetPromoTicker();
        promoData.setPromoCode("");
        disableVoucherCheckoutDiscount();
    }

    @Override
    public void failedCancelVoucherCart(Throwable throwable) {
        String message = ErrorNetMessage.MESSAGE_ERROR_DEFAULT;
        if (throwable != null && !throwable.getMessage().equals("")) {
            message = ErrorHandler.getErrorMessage(getActivity(), throwable);
        }
        showToastMessage(message);
    }

    private String getCategoryName() {
        return Objects.requireNonNull(Objects.requireNonNull(cartDigitalInfoData.getAttributes()).getCategoryName());
    }

    private String getOperatorName() {
        return Objects.requireNonNull(Objects.requireNonNull(cartDigitalInfoData.getAttributes()).getOperatorName());
    }

    @Override
    public void onClickDetailPromo() {
        presenter.onClickPromoDetail();
        Intent intent;
        String promoCode = promoData.getPromoCode();
        if (!promoCode.isEmpty()) {
            int requestCode;
            if (promoData.getTypePromo() == PromoData.TYPE_VOUCHER) {
                intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalPromo.PROMO_LIST_DIGITAL);
                intent.putExtra("EXTRA_PROMO_CODE", promoCode);
                intent.putExtra("EXTRA_COUPON_ACTIVE",
                        Objects.requireNonNull(cartDigitalInfoData.getAttributes()).isCouponActive()
                );
                requestCode = ConstantKt.getREQUEST_CODE_PROMO_LIST();
            } else {
                intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalPromo.PROMO_DETAIL_DIGITAL);
                intent.putExtra("EXTRA_IS_USE", true);
                intent.putExtra("EXTRA_KUPON_CODE", promoCode);
                requestCode = ConstantKt.getREQUEST_CODE_PROMO_DETAIL();
            }
            intent.putExtra("EXTRA_PROMO_DIGITAL_MODEL", getPromoDigitalModel());
            startActivityForResult(intent, requestCode);
        } else {
            showToastMessage(getString(com.tokopedia.promocheckout.common.R.string.promo_none_applied));
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
        detailHolderView.removeAdditionalInfo();
        checkoutHolderView.disableVoucherDiscount();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == ConstantKt.getREQUEST_CODE_PROMO_LIST() || requestCode == ConstantKt.getREQUEST_CODE_PROMO_DETAIL()) && resultCode == Activity.RESULT_OK) {
            if (data.hasExtra(TickerCheckoutUtilKt.getEXTRA_PROMO_DATA())) {
                promoData = data.getParcelableExtra(TickerCheckoutUtilKt.getEXTRA_PROMO_DATA());
                disableVoucherCheckoutDiscount();
                // Check between apply promo code or cancel promo from promo detail
                switch (promoData.getState()) {
                    case EMPTY: {
                        promoData.setPromoCode("");
                        resetPromoTicker();
                        break;
                    }
                    case FAILED: {
                        promoData.setPromoCode("");
                        presenter.onReceivePromoCode(promoData);
                        break;
                    }
                    case ACTIVE: {
                        presenter.onReceivePromoCode(promoData);
                        break;
                    }
                    default: {
                    }
                }
            }
        } else if (requestCode == PaymentConstant.REQUEST_CODE) {
            switch (resultCode) {
                case PaymentConstant.PAYMENT_SUCCESS:
                    presenter.onPaymentSuccess(cartPassData.getCategoryId());
                    break;
                case PaymentConstant.PAYMENT_FAILED:
                    showToastMessage(
                            getString(R.string.alert_payment_canceled_or_failed_digital_module)
                    );
                    presenter.processGetCartDataAfterCheckout(cartPassData.getCategoryId());
                    break;
                case PaymentConstant.PAYMENT_CANCELLED:
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
        }
    }

    private void closeView() {
        getActivity().finish();
    }

    @Override
    public void showToastMessage(String message) {
        View view = getView();
        if (view != null) Toaster.build(getView(), message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                getString(com.tokopedia.abstraction.R.string.close), v -> {
                }).show();
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

        Intent intent = RouteManager.getIntent(getContext(), ApplinkConstInternalPayment.PAYMENT_CHECKOUT);
        intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData);
        startActivityForResult(intent, PaymentConstant.REQUEST_CODE);
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
    public long getOrderId() {
        String orderIdString = cartPassData.getOrderId();
        try {
            return TextUtils.isEmpty(orderIdString) ? 0L : Long.parseLong(orderIdString);
        } catch (Exception e) {
            return 0L;
        }
    }

    @Override
    public String getZoneId() {
        return cartPassData.getZoneId();
    }

    @Override
    public HashMap<String, String> getFields() {
        return cartPassData.getFields();
    }

    @Override
    public boolean isInstantCheckout() {
        return cartPassData.getInstantCheckout().equals("1");
    }

    @Override
    public int getProductId() {
        String productIdString = cartPassData.getProductId();
        try {
            return TextUtils.isEmpty(productIdString) ? 0 : Integer.parseInt(productIdString);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void closeViewWithMessageAlert(String message) {
        if (cartPassData.isFromPDP()) {
            Intent intent = new Intent();
            intent.putExtra(DigitalExtraParam.EXTRA_MESSAGE, message);
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        } else {
            showError(message);
        }
    }

    @Override
    public void showError(String message) {
        if(message == null || message.isEmpty()){
            emptyState.setDescription(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
            emptyState.setImageUrl(DIGITAL_CART_FAILED_TRANSACTION_IMAGE_URL);
            emptyState.setTitle(getString(R.string.digital_transaction_failed_title));
        } else {
            emptyState.setDescription(message);
            if (message.equals(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL) || message.equals(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION) || message.equals(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT)) {
                emptyState.setImageDrawable(getResources().getDrawable(com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection));
                emptyState.setTitle(getString(com.tokopedia.globalerror.R.string.noConnectionAction));
            }
            else if(message.equals(ErrorNetMessage.MESSAGE_ERROR_SERVER) || message.equals(ErrorNetMessage.MESSAGE_ERROR_DEFAULT)){
                emptyState.setImageDrawable(getResources().getDrawable(com.tokopedia.globalerror.R.drawable.unify_globalerrors_500));
                emptyState.setTitle(getString(com.tokopedia.globalerror.R.string.error500Title));
            }
            else {
                emptyState.setImageUrl(DIGITAL_CART_FAILED_TRANSACTION_IMAGE_URL);
                emptyState.setTitle(getString(R.string.digital_transaction_failed_title));
            }
        }
        emptyState.setPrimaryCTAText(getString(R.string.digital_empty_state_checkout_btn));
        emptyState.setPrimaryCTAClickListener(() -> {
            emptyState.setVisibility(View.GONE);
            presenter.onViewCreated();
            return Unit.INSTANCE;
        });
        emptyState.setVisibility(View.VISIBLE);
    }

    @Override
    public void setCartDigitalInfo(CartDigitalInfoData cartDigitalInfoData) {
        this.cartDigitalInfoData = cartDigitalInfoData;
    }

    @Override
    public void interruptRequestTokenVerification(String phoneNumber) {
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalGlobal.COTP);

        Bundle bundle = new Bundle();
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true);
        bundle.putString(ApplinkConstInternalGlobal.PARAM_MSISDN, phoneNumber);
        bundle.putString(ApplinkConstInternalGlobal.PARAM_EMAIL, "");
        bundle.putInt(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_TYPE_CHECKOUT_DIGITAL);
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true);

        intent.putExtras(bundle);
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
    public void expandAdditionalInfo() {
        detailHolderView.expandAdditional();
    }

    @Override
    public void setCheckoutParameter(CheckoutDataParameter.Builder builder) {
        checkoutDataParameterBuilder = builder;
    }

    @Override
    public void showPostPaidDialog(String title,
                                   String content,
                                   String confirmButtonTitle) {
        try {
            DialogUnify dialogUnify = new DialogUnify(getActivity(),
                    DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE);
            dialogUnify.setTitle(title);
            dialogUnify.setDescription(MethodChecker.fromHtml(content));
            dialogUnify.setPrimaryCTAText(confirmButtonTitle);
            dialogUnify.setPrimaryCTAClickListener(() -> {
                dialogUnify.dismiss();
                return Unit.INSTANCE;
            });
            dialogUnify.show();
        } catch (Throwable e) {

        }
    }

    @Override
    public Boolean isEgoldChecked() {
        return mybillEgold.isChecked();
    }

    @Override
    public void renderMyBillsEgoldView(FintechProduct fintechProduct) {
        if (fintechProduct != null) {
            mybillEgold.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                presenter.onEgoldCheckedListener(isChecked, inputPriceHolderView.getPriceInput());
            });

            if (fintechProduct.getCheckBoxDisabled()) {
                mybillEgold.getSubscriptionCheckbox().setVisibility(View.GONE);
            } else {
                mybillEgold.getSubscriptionCheckbox().setVisibility(View.VISIBLE);
                if (!mybillEgold.isChecked()) mybillEgold.setChecked(fintechProduct.getOptIn());
                presenter.onEgoldCheckedListener(mybillEgold.isChecked(), inputPriceHolderView.getPriceInput());
            }

            mybillEgold.hasMoreInfo(true);
            if (fintechProduct.getInfo() != null) {
                if (fintechProduct.getInfo().getTitle() != null)
                    mybillEgold.setHeaderTitle(fintechProduct.getInfo().getTitle());
                if (fintechProduct.getInfo().getSubtitle() != null)
                    mybillEgold.setDescription(fintechProduct.getInfo().getSubtitle());
            }
            mybillEgold.setVisibility(View.VISIBLE);
        } else {
            mybillEgold.setVisibility(View.GONE);
        }
    }

    @Override
    public void renderEgoldMoreInfo(String title, String tooltip, String linkUrl) {
        if (linkUrl != null && !linkUrl.isEmpty()) {
            if (getContext() != null) RouteManager.route(getContext(), linkUrl);
        } else if (tooltip != null && !tooltip.isEmpty()) {
            if (getContext() != null) {
                View moreInfoView = View.inflate(getContext(), R.layout.view_digital_egold_info_bottom_sheet, null);
                Typography moreInfoText = moreInfoView.findViewById(R.id.egold_tooltip);
                moreInfoText.setText(tooltip);

                BottomSheetUnify moreInfoBottomSheet = new BottomSheetUnify();
                if (title != null) moreInfoBottomSheet.setTitle(title);
                moreInfoBottomSheet.setFullPage(false);
                moreInfoBottomSheet.setChild(moreInfoView);
                moreInfoBottomSheet.clearAction();
                moreInfoBottomSheet.setCloseClickListener(view -> {
                    moreInfoBottomSheet.dismiss();
                    return Unit.INSTANCE;
                });
                if (getFragmentManager() != null) {
                    moreInfoBottomSheet.show(getFragmentManager(), "E-gold more info bottom sheet");
                }
            }
        }
    }


    public void updateTotalPriceWithFintechAmount() {
        if (mybillEgold.getVisibility() == View.VISIBLE) {
            presenter.updateTotalPriceWithFintechAmount(mybillEgold.isChecked(), inputPriceHolderView.getPriceInput());
        }
    }

    @Override
    public void onMoreInfoClicked() {
        presenter.onEgoldMoreInfoClicked();
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
    public DigitalSubscriptionParams getDigitalSubscriptionParams() {
        if (digitalSubscriptionParams != null) {
            return digitalSubscriptionParams;
        } else {
            return new DigitalSubscriptionParams();
        }
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }
}
