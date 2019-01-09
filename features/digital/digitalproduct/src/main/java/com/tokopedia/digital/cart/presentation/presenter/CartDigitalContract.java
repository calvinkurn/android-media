package com.tokopedia.digital.cart.presentation.presenter;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.common_digital.cart.view.model.checkout.CheckoutDataParameter;
import com.tokopedia.common_digital.cart.view.model.checkout.InstantCheckoutData;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.digital.cart.presentation.listener.IBaseView;
import com.tokopedia.digital.cart.presentation.model.CheckoutDigitalData;
import com.tokopedia.digital.cart.presentation.model.VoucherDigital;

/**
 * Created by Rizky on 29/08/18.
 */
public class CartDigitalContract {

    public interface View extends IBaseView {

        void renderLoadingAddToCart();

        void renderAddToCartData(CartDigitalInfoData cartDigitalInfoData);

        void renderErrorAddToCart(String message);

        void renderErrorHttpAddToCart(String message);

        void renderErrorNoConnectionAddToCart(String message);

        void renderErrorTimeoutConnectionAddToCart(String message);

        void renderLoadingGetCartInfo();

        void renderCartDigitalInfoData(CartDigitalInfoData cartDigitalInfoData);

        void renderErrorGetCartData(String message);

        void renderErrorHttpGetCartData(String message);

        void renderErrorNoConnectionGetCartData(String message);

        void renderErrorTimeoutConnectionGetCartData(String message);

        void renderVoucherInfoData(VoucherDigital voucherDigital);

        void renderCouponInfoData(VoucherDigital voucherDigital);

        void renderToTopPay(CheckoutDigitalData checkoutDigitalData);

        void renderErrorCheckout(String message);

        void renderErrorHttpCheckout(String message);

        void renderErrorNoConnectionCheckout(String message);

        void renderErrorTimeoutConnectionCheckout(String message);

        void renderToInstantCheckoutPage(InstantCheckoutData instantCheckoutData);

        void renderErrorInstantCheckout(String message);

        void renderErrorHttpInstantCheckout(String message);

        void renderErrorNoConnectionInstantCheckout(String message);

        void renderErrorTimeoutConnectionInstantCheckout(String message);

        void closeViewWithMessageAlert(String message);

        void setCartDigitalInfo(CartDigitalInfoData cartDigitalInfo);

        void interruptRequestTokenVerification();

        String getUserId();

        String getAccountToken();

        String getWalletRefreshToken();

        CheckoutDataParameter getCheckoutData();

        String getClientNumber();

        boolean isInstantCheckout();

        int getProductId();

        String getIdemPotencyKey();

        Context getApplicationContext();

        Activity getActivity();

        DigitalCheckoutPassData getPassData();

        void showProgressLoading(String title,String message);

        CartDigitalInfoData getCartDataInfo();

        void navigateToLoggedInPage();

        void showPostPaidDialog(String title,
                                String content,
                                String confirmButtonTitle);

        boolean isAlreadyShowPostPaid();
    }

    interface Presenter extends CustomerPresenter<View> {

        void processGetCartData(String digitalCategoryId);

        void processGetCartDataAfterCheckout(String digitalCategoryId);

        void processAddToCart();

        void processCheckVoucher(String voucherCode, String digitalCategoryId);

        void processToCheckout();

        void processToInstantCheckout();

        void processPatchOtpCart(String digitalCategoryId);

        void autoApplyCouponIfAvailable(String digitalCategoryId);

        void sendAnalyticsATCSuccess(CartDigitalInfoData cartDigitalInfoData,  int extraComeFrom);

        void onClearVoucher();

        void onPaymentSuccess(String categoryId);

        void onLoginResultReceived();

        void onFirstTimeLaunched();
    }


}
