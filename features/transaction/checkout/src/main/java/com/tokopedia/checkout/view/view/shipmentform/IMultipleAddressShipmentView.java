package com.tokopedia.checkout.view.view.shipmentform;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.checkout.view.holderitemdata.CartItemPromoHolderData;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartListResult;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;

/**
 * Created by nakama on 09/03/18.
 */

public interface IMultipleAddressShipmentView {

    void showLoading();

    void hideLoading();

    void showPromoMessage(CheckPromoCodeCartShipmentResult checkPromoCodeCartShipmentResult,
                          CartItemPromoHolderData cartItemPromoHolderData);

    void showPromoError(String message);

    void renderCheckShipmentPrepareCheckoutSuccess();

    void renderErrorDataHasChangedCheckShipmentPrepareCheckout(CartShipmentAddressFormData cartShipmentAddressFormData);

    void renderErrorCheckShipmentPrepareCheckout(String message);

    void renderErrorHttpCheckShipmentPrepareCheckout(String message);

    void renderErrorNoConnectionCheckShipmentPrepareCheckout(String message);

    void renderErrorTimeoutConnectionCheckShipmentPrepareCheckout(String message);

    TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams
    );

    void renderCheckPromoCodeFromSuggestedPromoSuccess(CheckPromoCodeCartListResult checkPromoCodeCartListResult);

    void renderErrorCheckPromoCodeFromSuggestedPromo(String message);
}
