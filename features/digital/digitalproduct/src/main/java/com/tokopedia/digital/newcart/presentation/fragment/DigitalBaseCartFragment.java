package com.tokopedia.digital.newcart.presentation.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.abstraction.constant.IRouterConstant;
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
import com.tokopedia.design.component.Dialog;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.voucher.VoucherCartHachikoView;
import com.tokopedia.digital.R;
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

import java.util.List;
import java.util.Map;

public abstract class DigitalBaseCartFragment<P extends DigitalBaseContract.Presenter> extends BaseDaggerFragment
        implements DigitalBaseContract.View,
        InputPriceHolderView.ActionListener,
        VoucherCartHachikoView.ActionListener,
        DigitalCartCheckoutHolderView.ActionListener {
    protected static final String ARG_PASS_DATA = "ARG_PASS_DATA";
    protected static final String ARG_CART_INFO = "ARG_CART_INFO";
    protected static final String ARG_CHECKOUT_INFO = "ARG_CHECKOUT_INFO";
    private static final int REQUEST_CODE_OTP = 1001;

    protected CartDigitalInfoData cartDigitalInfoData;
    protected CheckoutDataParameter.Builder checkoutDataParameterBuilder;

    protected DigitalCheckoutPassData cartPassData;
    protected DigitalCartDetailHolderView detailHolderView;
    protected DigitalCartCheckoutHolderView checkoutHolderView;
    protected InputPriceHolderView inputPriceHolderView;
    protected LinearLayout inputPriceContainer;
    private boolean isAlreadyShowPostPaidPopUp;

    protected P presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cartPassData = getArguments().getParcelable(ARG_PASS_DATA);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView(view);
        presenter.attachView(this);
    }

    protected abstract void setupView(View view);

    @Override
    public CartDigitalInfoData getCartInfoData() {
        return cartDigitalInfoData;
    }

    @Override
    public void renderHachikoCart() {
        checkoutHolderView.setVoucherActionListener(this);
        checkoutHolderView.showHachikoCart();
    }

    @Override
    public void renderHachikoPromoAndCouponLabel() {
        checkoutHolderView.setHachikoPromoAndCouponLabel();
    }

    @Override
    public void renderHachikoPromoLabelOnly() {
        checkoutHolderView.setHachikoPromoLabelOnly();
    }

    @Override
    public void hideHachikoCart() {
        checkoutHolderView.hideHachikoCart();
    }

    @Override
    public void renderHachikoCoupon(String title, String message, String voucherCode) {
        checkoutHolderView.setVoucherActionListener(this);
        checkoutHolderView.setHachikoCoupon(title, message, voucherCode);
    }

    @Override
    public void enableVoucherDiscount(long discountAmountPlain) {
        checkoutHolderView.enableVoucherDiscount(discountAmountPlain);
    }

    @Override
    public void renderHachikoVoucher(String voucherCode, String message) {
        checkoutHolderView.setVoucherActionListener(this);
        checkoutHolderView.setVoucher(voucherCode, message);
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
        checkoutDataParameterBuilder.voucherCode(checkoutHolderView.getVoucherCode());
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
    public void onClickUseVoucher() {
        presenter.onUseVoucherButtonClicked();
    }

    @Override
    public void disableVoucherDiscount() {
        presenter.onClearVoucher();
    }

    @Override
    public void trackingSuccessVoucher(String voucherName) {

    }

    @Override
    public void trackingCancelledVoucher() {

    }


    @Override
    public void navigateToCouponActiveAndSelected(String categoryId) {
        Intent intent = LoyaltyActivity.newInstanceCouponActiveAndSelected(
                getActivity(), IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.DIGITAL_STRING, categoryId
        );
        navigateToActivityRequest(intent, IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE);
    }

    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }


    @Override
    public void navigateToCouponActive(String categoryId) {
        Intent intent = LoyaltyActivity.newInstanceCouponActive(
                getActivity(), IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.DIGITAL_STRING, categoryId
        );
        navigateToActivityRequest(intent, IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void navigateToCouponNotActive(String categoryId) {
        Intent intent = LoyaltyActivity.newInstanceCouponNotActive(
                getActivity(), IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.DIGITAL_STRING,
                categoryId
        );
        navigateToActivityRequest(intent, IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE);
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
        if (requestCode == IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE) {
            if (resultCode == IRouterConstant.LoyaltyModule.ResultLoyaltyActivity.VOUCHER_RESULT_CODE) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    String voucherCode = bundle.getString(
                            IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_CODE, "");
                    String voucherMessage = bundle.getString(
                            IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_MESSAGE, "");
                    long voucherDiscountAmount = bundle.getLong(
                            IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_DISCOUNT_AMOUNT);

                    presenter.onReceiveVoucherCode(voucherCode, voucherMessage, voucherDiscountAmount, 0);
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

                    presenter.onReceiveCoupon(couponTitle, couponMessage, couponCode, couponDiscountAmount, 1);
                }
            }
        } else if (requestCode == TopPayActivity.REQUEST_CODE) {
            switch (resultCode) {
                case TopPayActivity.PAYMENT_SUCCESS:
                    if (getActivity().getApplicationContext() instanceof DigitalModuleRouter) {
                        ((DigitalModuleRouter)getActivity().getApplicationContext()).
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
        return Integer.parseInt(cartPassData.getProductId());
    }

    @Override
    public void closeViewWithMessageAlert(String message) {
        Intent intent = new Intent();
        intent.putExtra(DigitalRouter.Companion.getEXTRA_MESSAGE(), message);
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
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }
}
