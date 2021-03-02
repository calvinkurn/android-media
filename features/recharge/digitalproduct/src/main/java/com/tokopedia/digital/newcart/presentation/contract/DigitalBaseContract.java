package com.tokopedia.digital.newcart.presentation.contract;

import android.app.Activity;

import androidx.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.digital.newcart.domain.model.CheckoutDigitalData;
import com.tokopedia.digital.newcart.presentation.model.DigitalSubscriptionParams;
import com.tokopedia.digital.newcart.presentation.model.cart.CartAdditionalInfo;
import com.tokopedia.digital.newcart.presentation.model.cart.CartDigitalInfoData;
import com.tokopedia.digital.newcart.presentation.model.cart.CartItemDigital;
import com.tokopedia.digital.newcart.presentation.model.cart.FintechProduct;
import com.tokopedia.digital.newcart.presentation.model.cart.UserInputPriceDigital;
import com.tokopedia.digital.newcart.presentation.model.checkout.CheckoutDataParameter;
import com.tokopedia.promocheckout.common.view.model.PromoData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DigitalBaseContract {
    interface View extends CustomerView {

        CartDigitalInfoData getCartInfoData();

        void renderPromoTicker();

        void hidePromoTicker();

        void resetPromoTicker();

        void renderPromo();

        void onAutoApplyPromo(String couponTitle,
                              String couponMessage,
                              String couponCode,
                              int isCoupon);

        void enableVoucherDiscount(long discountAmountPlain);

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

        Map<String, String> getGeneratedAuthParamNetwork(String userId,
                                                         String deviceId,
                                                         Map<String, String> param);

        void showCartView();

        void hideFullPageLoading();

        RequestBodyIdentifier getDigitalIdentifierParam();

        void disableVoucherCheckoutDiscount();

        void renderToTopPay(CheckoutDigitalData checkoutDigitalData);

        void showToastMessage(String message);

        String getIdemPotencyKey();

        String getClientNumber();

        long getOrderId();

        String getZoneId();

        HashMap<String, String> getFields();

        boolean isInstantCheckout();

        int getProductId();

        void closeViewWithMessageAlert(String message);

        void setCartDigitalInfo(CartDigitalInfoData cartDigitalInfoData);

        void interruptRequestTokenVerification(String phoneNumber);

        CheckoutDataParameter getCheckoutData();

        void renderErrorInstantCheckout(String message);

        void expandAdditionalInfo();

        String getString(@StringRes int resId);

        void setCheckoutParameter(CheckoutDataParameter.Builder builder);

        void showPostPaidDialog(String title,
                                String content,
                                String confirmButtonTitle);

        void startPerfomanceMonitoringTrace();

        void stopPerfomanceMonitoringTrace();

        DigitalSubscriptionParams getDigitalSubscriptionParams();

        void successCancelVoucherCart();

        void failedCancelVoucherCart(Throwable message);

        void showError(String message);

        void renderMyBillsEgoldView(FintechProduct fintechProduct);

        void updateTotalPriceWithFintechAmount();

        void renderEgoldMoreInfo(String title, String tooltip, String linkUrl);

        Boolean isEgoldChecked();
    }

    interface Presenter<T extends View> extends CustomerPresenter<T> {

        void onViewCreated();

        void onClickPromoButton();

        void onClickPromoDetail();

        void onReceivePromoCode(PromoData promoData);

        void processToCheckout();

        void onPaymentSuccess(String categoryId);

        void processPatchOtpCart(String categoryId);

        void processGetCartDataAfterCheckout(String categoryId);

        void onEgoldMoreInfoClicked();

        void onEgoldCheckedListener(Boolean checked, Long inputPrice);

        void updateTotalPriceWithFintechAmount(Boolean checked, Long inputPrice);

        void cancelVoucherCart();
    }
}
