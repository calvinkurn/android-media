package com.tokopedia.checkout.view.common.adapter;

import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.view.common.holderitemdata.CartItemTickerErrorHolderData;
import com.tokopedia.promocheckout.common.view.model.PromoStackingData;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentCartItemModel;

/**
 * @author anggaprasetiyo on 13/03/18.
 */

public interface CartAdapterActionListener {

    void onCartPromoSuggestionButtonCloseClicked(CartPromoSuggestion cartPromoSuggestion, int position);

    void onCartPromoUseVoucherGlobalPromoClicked(PromoStackingData cartPromoGlobal, int position);

    void onVoucherMerchantPromoClicked(Object object);

    void onCartPromoCancelVoucherPromoGlobalClicked(PromoStackingData cartPromoGlobal, int position);

    void onCancelVoucherMerchantClicked(String promoMerchantCode, int position, boolean ignoreAPIResponse);

    void onCartPromoGlobalTrackingImpression(PromoStackingData cartPromoGlobal, int position);

    void onCartPromoGlobalTrackingCancelled(PromoStackingData cartPromoGlobal, int position);

    void onCartDataEnableToCheckout();

    void onNeedToSaveState(ShipmentCartItemModel shipmentCartItemModel);

    void onCartDataDisableToCheckout(String message);

    void onCartItemTickerErrorActionClicked(CartItemTickerErrorHolderData data, int position);

    void onDropshipperValidationResult(boolean result, Object shipmentData, int position, int requestCode);

    void onClickDetailPromoGlobal(PromoStackingData dataGlobal, int position);

}
