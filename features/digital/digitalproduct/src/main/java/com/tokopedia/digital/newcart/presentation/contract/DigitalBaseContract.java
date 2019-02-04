package com.tokopedia.digital.newcart.presentation.contract;

import android.app.Activity;
import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.cart.view.model.cart.CartAdditionalInfo;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.common_digital.cart.view.model.cart.CartItemDigital;
import com.tokopedia.common_digital.cart.view.model.cart.UserInputPriceDigital;
import com.tokopedia.common_digital.cart.view.model.checkout.CheckoutDataParameter;
import com.tokopedia.common_digital.cart.view.model.checkout.InstantCheckoutData;
import com.tokopedia.digital.cart.presentation.model.CheckoutDigitalData;

import java.util.List;
import java.util.Map;

public interface DigitalBaseContract {
    interface View extends CustomerView{

        CartDigitalInfoData getCartInfoData();

        void renderHachikoCart();

        void renderHachikoPromoAndCouponLabel();

        void renderHachikoPromoLabelOnly();

        void hideHachikoCart();

        void renderHachikoCoupon(String title, String message, String voucherCode);

        void enableVoucherDiscount(long discountAmountPlain);

        void renderHachikoVoucher(String voucherCode, String message);

        void renderDetailMainInfo(List<CartItemDigital> mainInfo);

        void renderAdditionalInfo(List<CartAdditionalInfo> additionalInfos);

        void renderCategoryInfo(String categoryName);

        CheckoutDataParameter.Builder getCheckoutDataParameter();

        void renderInputPriceView(String total, UserInputPriceDigital userInputPriceDigital);

        void renderCheckoutView(long pricePlain);

        Activity getActivity();

        DigitalCheckoutPassData getCartPassData();

        void hideCartView();

        void showFullPageLoading();

        Map<String,String> getGeneratedAuthParamNetwork(String userId,
                                                        String deviceId,
                                                        Map<String, String> param);

        void showCartView();

        void hideFullPageLoading();

        void navigateToCouponActiveAndSelected(String categoryId);

        void navigateToCouponActive(String categoryId);

        void navigateToCouponNotActive(String categoryId);

        RequestBodyIdentifier getDigitalIdentifierParam();

        void disableVoucherCheckoutDiscount();

        void renderToTopPay(CheckoutDigitalData checkoutDigitalData);

        void showToastMessage(String message);

        String getIdemPotencyKey();

        String getClientNumber();

        boolean isInstantCheckout();

        int getProductId();

        void closeViewWithMessageAlert(String message);

        void setCartDigitalInfo(CartDigitalInfoData cartDigitalInfoData);

        void interruptRequestTokenVerification(String phoneNumber);

        CheckoutDataParameter getCheckoutData();

        void renderErrorInstantCheckout(String message);

        void renderToInstantCheckoutPage(InstantCheckoutData instantCheckoutData);

        void expandAdditionalInfo();

        String getString(@StringRes int resId);

        void setCheckoutParameter(CheckoutDataParameter.Builder builder);

        boolean isAlreadyShowPostPaid();

        void showPostPaidDialog(String title,
                                String content,
                                String confirmButtonTitle,
                                String userId);

    }

    interface Presenter<T extends View> extends CustomerPresenter<T>{

        void onViewCreated();

        void onUseVoucherButtonClicked();

        void onReceiveVoucherCode(String code, String message, long discount, int isCoupon);

        void onReceiveCoupon(String couponTitle,
                             String couponMessage,
                             String couponCode,
                             long couponDiscountAmount,
                             int isCoupon);

        void onClearVoucher();

        void processToCheckout();

        void onPaymentSuccess(String categoryId);

        void processPatchOtpCart(String categoryId);

        void processGetCartDataAfterCheckout(String categoryId);
    }
}
