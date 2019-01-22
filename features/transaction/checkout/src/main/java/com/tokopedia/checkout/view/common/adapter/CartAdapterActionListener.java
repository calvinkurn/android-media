package com.tokopedia.checkout.view.common.adapter;

import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.view.common.holderitemdata.CartItemTickerErrorHolderData;
import com.tokopedia.promocheckout.common.view.model.PromoData;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentCartItemModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentData;

/**
 * @author anggaprasetiyo on 13/03/18.
 */

public interface CartAdapterActionListener {

    void onCartPromoSuggestionActionClicked(CartPromoSuggestion cartPromoSuggestion, int position);

    void onCartPromoSuggestionButtonCloseClicked(CartPromoSuggestion cartPromoSuggestion, int position);

    void onCartPromoUseVoucherPromoClicked(PromoData cartPromo, int position);

    void onCartPromoCancelVoucherPromoClicked(PromoData cartPromo, int position);

    void onCartPromoTrackingImpression(PromoData cartPromo, int position);

    void onCartPromoTrackingCancelled(PromoData cartPromo, int position);

    void onCartDataEnableToCheckout();

    void onNeedToSaveState(ShipmentCartItemModel shipmentCartItemModel);

    void onCartDataDisableToCheckout(String message);

    void onCartItemTickerErrorActionClicked(CartItemTickerErrorHolderData data, int position);

    void onDropshipperValidationResult(boolean result, Object shipmentData, int position);

    void onClickDetailPromo(PromoData data, int position);
}
