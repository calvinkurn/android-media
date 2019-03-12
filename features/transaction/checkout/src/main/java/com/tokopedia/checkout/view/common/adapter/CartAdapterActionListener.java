package com.tokopedia.checkout.view.common.adapter;

import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.view.common.holderitemdata.CartItemTickerErrorHolderData;
import com.tokopedia.promocheckout.common.view.model.PromoStackingData;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentCartItemModel;
import com.tokopedia.promocheckout.common.view.model.PromoData;

/**
 * @author anggaprasetiyo on 13/03/18.
 */

public interface CartAdapterActionListener {

    void onCartPromoSuggestionActionClicked(CartPromoSuggestion cartPromoSuggestion, int position);

    void onCartPromoSuggestionButtonCloseClicked(CartPromoSuggestion cartPromoSuggestion, int position);

    // void onCartPromoUseVoucherPromoClicked(PromoData cartPromo, int position);

    void onCartPromoUseVoucherGlobalPromoClicked(PromoStackingData cartPromoGlobal, int position);

    // void onCartPromoUseVoucherMerchantPromoClicked(PromoDataMerchant cartPromoMerchant, int position);

    void onCartPromoUseVoucherMerchantPromoClickedTest();

    // void onCartPromoCancelVoucherPromoClicked(PromoData cartPromo, int position);

    void onCartPromoCancelVoucherPromoGlobalClicked(PromoStackingData cartPromoGlobal, int position);

    // void onCartPromoCancelVoucherPromoMerchantClicked(PromoDataMerchant cartPromoMerchant, int position);

    // void onCartPromoTrackingImpression(PromoData cartPromo, int position);

    void onCartPromoGlobalTrackingImpression(PromoStackingData cartPromoGlobal, int position);

    // void onCartPromoMerchantTrackingImpression(PromoDataMerchant cartPromoMerchant, int position);

    // void onCartPromoTrackingCancelled(PromoData cartPromo, int position);

    void onCartPromoGlobalTrackingCancelled(PromoStackingData cartPromoGlobal, int position);

    // void onCartPromoMerchantTrackingCancelled(PromoDataMerchant cartPromoMerchant, int position);

    void onCartDataEnableToCheckout();

    void onNeedToSaveState(ShipmentCartItemModel shipmentCartItemModel);

    void onCartDataDisableToCheckout(String message);

    void onCartItemTickerErrorActionClicked(CartItemTickerErrorHolderData data, int position);

    void onDropshipperValidationResult(boolean result, Object shipmentData, int position, int requestCode);

    // void onClickDetailPromo(PromoData data, int position);

    void onClickDetailPromoGlobal(PromoStackingData dataGlobal, int position);

    // void onClickDetailPromoMerchant(PromoDataMerchant dataMerchant, int position);
}
