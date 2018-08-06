package com.tokopedia.checkout.view.view.cartlist;

import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.view.holderitemdata.CartItemHolderData;

import java.util.List;
import java.util.Map;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public interface ICartListPresenter {

    void detachView();

    void processInitialGetCartData();

    void processDeleteCart(CartItemData cartItemData, boolean addWishList);

    void processDeleteAndRefreshCart(List<CartItemData> removedCartItems, boolean addWishList);

    void processToUpdateCartData();

    void reCalculateSubTotal(List<CartItemHolderData> dataList);

    void processCheckPromoCodeFromSuggestedPromo(String promoCode, boolean isAutoApply);

    void processResetAndRefreshCartData();

    void processCancelAutoApply();

    Map<String, Object> generateCartDataAnalytics(CartItemData removedCartItem, String enhancedECommerceAction);

    Map<String, Object> generateCartDataAnalytics(List<CartItemData> cartItemDataList, String enhancedECommerceAction);
}
