package com.tokopedia.checkout.view.view.cartlist;

import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.view.holderitemdata.CartItemHolderData;

import java.util.List;
import java.util.Map;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public interface ICartListPresenter {

    void processInitialGetCartData();

    void processDeleteCart(CartItemData cartItemData, boolean addWishList);

    void processDeleteAndRefreshCart(List<CartItemData> removedCartItems, boolean addWishList);

    void processToShipmentSingleAddress();

    void processToShipmentMultipleAddress(RecipientAddressModel selectedAddress);

    void reCalculateSubTotal(List<CartItemHolderData> dataList);

    void processCheckPromoCodeFromSuggestedPromo(String promoCode);

    void processToShipmentForm();

    void processResetAndRefreshCartData();

    void processResetThenToShipmentForm();

    void processCancelAutoApply();

    Map<String, Object> generateCartDataAnalytics(CartItemData removedCartItem);

    Map<String, Object> generateCartDataAnalytics(List<CartItemData> cartItemDataList);
}
