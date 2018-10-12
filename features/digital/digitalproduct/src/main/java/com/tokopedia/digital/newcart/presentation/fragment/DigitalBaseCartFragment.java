package com.tokopedia.digital.newcart.presentation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.cart.view.model.cart.CartAdditionalInfo;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.common_digital.cart.view.model.cart.CartItemDigital;
import com.tokopedia.common_digital.cart.view.model.cart.UserInputPriceDigital;
import com.tokopedia.common_digital.cart.view.model.checkout.CheckoutDataParameter;
import com.tokopedia.design.voucher.VoucherCartHachikoView;
import com.tokopedia.digital.cart.presentation.compoundview.InputPriceHolderView;
import com.tokopedia.digital.cart.presentation.model.VoucherAttributeDigital;
import com.tokopedia.digital.cart.presentation.model.VoucherDigital;
import com.tokopedia.digital.newcart.presentation.compoundview.DigitalCartCheckoutHolderView;
import com.tokopedia.digital.newcart.presentation.compoundview.DigitalCartDetailHolderView;
import com.tokopedia.digital.newcart.presentation.contract.DigitalBaseContract;
import com.tokopedia.digital.utils.DeviceUtil;
import com.tokopedia.loyalty.view.activity.LoyaltyActivity;
import com.tokopedia.network.utils.AuthUtil;

import java.util.List;
import java.util.Map;

public abstract class DigitalBaseCartFragment<P extends DigitalBaseContract.Presenter> extends BaseDaggerFragment implements DigitalBaseContract.View, InputPriceHolderView.ActionListener, VoucherCartHachikoView.ActionListener, DigitalCartCheckoutHolderView.ActionListener {
    protected static final String ARG_PASS_DATA = "ARG_PASS_DATA";
    protected static final String ARG_CART_INFO = "ARG_CART_INFO";
    protected static final String ARG_CHECKOUT_INFO = "ARG_CHECKOUT_INFO";

    protected CartDigitalInfoData cartDigitalInfoData;
    protected CheckoutDataParameter.Builder checkoutDataParameterBuilder;

    protected DigitalCheckoutPassData checkoutPassData;
    protected DigitalCartDetailHolderView detailHolderView;
    protected DigitalCartCheckoutHolderView checkoutHolderView;

    protected P presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkoutPassData = getArguments().getParcelable(ARG_PASS_DATA);
        cartDigitalInfoData = getArguments().getParcelable(ARG_CART_INFO);
        CheckoutDataParameter checkoutDataParameter = getArguments().getParcelable(ARG_CHECKOUT_INFO);
        checkoutDataParameterBuilder = checkoutDataParameter.newBuilder();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView(view);
        presenter.onViewCreated();
    }

    protected abstract void setupView(View view);

    @Override
    public void setToolbarTitle(int resId) {

    }

    @Override
    public CartDigitalInfoData getCartInfoData() {
        return cartDigitalInfoData;
    }

    @Override
    public void showHachikoCart() {
        checkoutHolderView.setVoucherActionListener(this);
        checkoutHolderView.showHachikoCart();
    }

    @Override
    public void setHachikoPromoAndCouponLabel() {
        checkoutHolderView.setHachikoPromoAndCouponLabel();
    }

    @Override
    public void setHachikoPromoLabelOnly() {
        checkoutHolderView.setHachikoPromoLabelOnly();
    }

    @Override
    public void hideHachikoCart() {
        checkoutHolderView.hideHachikoCart();
    }

    @Override
    public void setHachikoCoupon(String title, String message, String voucherCode) {
        checkoutHolderView.setHachikoCoupon(title, message, voucherCode);
    }

    @Override
    public void enableVoucherDiscount(long discountAmountPlain) {
        checkoutHolderView.enableVoucherDiscount(discountAmountPlain);
    }

    @Override
    public void setHachikoVoucher(String voucherCode, String message) {
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
        return checkoutDataParameterBuilder;
    }

    @Override
    public void renderInputPrice(String total, UserInputPriceDigital userInputPriceDigital) {
        checkoutHolderView.setInputPriceListener(this);
        checkoutHolderView.renderInputPrice(total, userInputPriceDigital);
    }

    @Override
    public void renderCheckoutView(String price, String totalPrice, long pricePlain) {
        checkoutHolderView.setActionListener(this);
        checkoutHolderView.renderCheckout(cartDigitalInfoData.getAttributes().getPrice(),
                cartDigitalInfoData.getAttributes().getPrice(),
                cartDigitalInfoData.getAttributes().getPricePlain());
    }

    @Override
    public DigitalCheckoutPassData getCheckoutPassData() {
        return checkoutPassData;
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
        // TODO : UnifyTracking.eventVoucherSuccess(voucherCode, "");
    }

    @Override
    public void trackingCancelledVoucher() {
        // TODO : UnifyTracking.eventClickCancelVoucher("", "");
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
        }
    }

    @Override
    public void onCheckoutButtonClicked() {
        presenter.processToCheckout();
    }
}
