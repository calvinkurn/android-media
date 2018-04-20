package com.tokopedia.checkout.view.view.shipmentform;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.tokopedia.checkout.data.entity.request.CheckoutRequest;
import com.tokopedia.checkout.domain.datamodel.cartcheckout.CheckoutData;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.transaction.common.base.IBaseView;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 29/01/18.
 */

public interface ICartShipmentActivity extends IBaseView {

    void checkoutCart(CheckoutRequest checkoutRequest);

    void checkPromoCodeShipment(Subscriber<CheckPromoCodeCartShipmentResult> subscriber,
                                CheckPromoCodeCartShipmentRequest checkPromoCodeCartShipmentRequest);

    void renderCheckoutCartSuccess(CheckoutData checkoutData);

    void renderErrorCheckoutCart(String message);

    void renderErrorHttpCheckoutCart(String message);

    void renderErrorNoConnectionCheckoutCart(String message);

    void renderErrorTimeoutConnectionCheckoutCart(String message);


    void renderThanksTopPaySuccess(String message);

    void renderErrorThanksTopPay(String message);

    void renderErrorHttpThanksTopPay(String message);

    void renderErrorNoConnectionThanksTopPay(String message);

    void renderErrorTimeoutConnectionThanksTopPay(String message);


    void closeWithResult(int resultCode, @Nullable Intent intent);

}
