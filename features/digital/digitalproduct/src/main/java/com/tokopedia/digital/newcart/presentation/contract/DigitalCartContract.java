package com.tokopedia.digital.newcart.presentation.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.common_digital.cart.view.model.checkout.CheckoutDataParameter;

import java.util.Map;

public interface DigitalCartContract {
    interface View extends CustomerView {

        void hideContent();

        void showLoading();

        String getIdemPotencyKey();

        String getClientNumber();

        boolean isInstantCheckout();

        int getProductId();

        RequestBodyIdentifier getDigitalIdentifierParam();

        void closeViewWithMessageAlert(String message);

        void setCartDigitalInfo(CartDigitalInfoData cartDigitalInfoData);

        void interruptRequestTokenVerification();

        CheckoutDataParameter getCheckoutData();

        Map<String,String> getGeneratedAuthParamNetwork(String userId, String deviceId, Map<String, String> paramGetCart);

        void inflateDefaultCartPage(CartDigitalInfoData cartDigitalInfoData);
    }

    interface Presenter extends CustomerPresenter<View> {

        void onViewCreated();

        void processPatchOtpCart(String categoryId);
    }
}
