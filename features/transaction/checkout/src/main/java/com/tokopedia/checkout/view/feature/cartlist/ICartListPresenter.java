package com.tokopedia.checkout.view.feature.cartlist;

import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartShopHolderData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

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

    void reCalculateSubTotal(List<CartShopHolderData> dataList);

    void processCheckPromoCodeFromSuggestedPromo(String promoCode, boolean isAutoApply);

    void processResetAndRefreshCartData();

    void processCancelAutoApply();

    Map<String, Object> generateCartDataAnalytics(CartItemData removedCartItem, String enhancedECommerceAction);

    Map<String, Object> generateCartDataAnalytics(List<CartItemData> cartItemDataList, String enhancedECommerceAction);

    CartListData getCartListData();

    void processAddToWishlist(String productId, String userId, WishListActionListener wishListActionListener);

    void processRemoveFromWishlist(String productId, String userId, WishListActionListener wishListActionListener);

    ProductPass generateProductPassProductDetailPage(CartItemData.OriginData originData);

    void setHasPerformChecklistChange();

    boolean hasPerformChecklistChange();
}
