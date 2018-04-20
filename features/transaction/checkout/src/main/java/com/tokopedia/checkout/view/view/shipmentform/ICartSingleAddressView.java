package com.tokopedia.checkout.view.view.shipmentform;

import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.checkout.view.base.IBaseView;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;

/**
 * @author Aghny A. Putra on 26/01/18
 */
public interface ICartSingleAddressView extends IBaseView {

    void showLoading();

    void hideLoading();

    void renderCheckPromoShipmentDataSuccess(CheckPromoCodeCartShipmentResult checkPromoCodeCartShipmentResult);

    void renderErrorCheckPromoShipmentData(String message);

    void renderErrorHttpCheckPromoShipmentData(String message);

    void renderErrorNoConnectionCheckPromoShipmentData(String message);

    void renderErrorTimeoutConnectionCheckPromoShipmentData(String message);


    void renderCheckShipmentPrepareCheckoutSuccess();

    void renderErrorDataHasChangedCheckShipmentPrepareCheckout(CartShipmentAddressFormData cartShipmentAddressFormData);

    void renderErrorCheckShipmentPrepareCheckout(String message);

    void renderErrorHttpCheckShipmentPrepareCheckout(String message);

    void renderErrorNoConnectionCheckShipmentPrepareCheckout(String message);

    void renderErrorTimeoutConnectionCheckShipmentPrepareCheckout(String message);

    void renderCheckPromoCodeFromSuggestedPromoSuccess(PromoCodeCartListData promoCodeCartListData);

    void renderErrorCheckPromoCodeFromSuggestedPromo(String message);
}
