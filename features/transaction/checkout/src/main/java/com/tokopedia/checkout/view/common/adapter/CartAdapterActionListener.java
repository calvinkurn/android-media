package com.tokopedia.checkout.view.common.adapter;

import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.view.common.holderitemdata.CartItemPromoHolderData;
import com.tokopedia.checkout.view.common.holderitemdata.CartItemTickerErrorHolderData;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentCartItemModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentData;

/**
 * @author anggaprasetiyo on 13/03/18.
 */

public interface CartAdapterActionListener {

    void onCartPromoSuggestionActionClicked(CartPromoSuggestion cartPromoSuggestion, int position);

    void onCartPromoSuggestionButtonCloseClicked(CartPromoSuggestion cartPromoSuggestion, int position);

    void onCartPromoUseVoucherPromoClicked(CartItemPromoHolderData cartPromo, int position);

    void onCartPromoCancelVoucherPromoClicked(CartItemPromoHolderData cartPromo, int position);

    void onCartPromoTrackingSuccess(CartItemPromoHolderData cartPromo, int position);

    void onCartPromoTrackingCancelled(CartItemPromoHolderData cartPromo, int position);

    void onCartDataEnableToCheckout();

    void onNeedToSaveState(ShipmentCartItemModel shipmentCartItemModel);

    void onCartDataDisableToCheckout(String message);

    void onCartItemTickerErrorActionClicked(CartItemTickerErrorHolderData data, int position);

    void onDropshipperValidationResult(boolean result, ShipmentData shipmentData, int position);
}
