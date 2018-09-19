package com.tokopedia.checkout.view.feature.cartlist;

import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartItemHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartShopHolderData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public interface ICartListPresenter {
    void attachView(ICartListView view);

    void detachView();

    void processInitialGetCartData(boolean initialLoad);

    void processDeleteCart(CartItemData cartItemData, boolean addWishList);

    void processDeleteAndRefreshCart(List<CartItemData> removedCartItems, boolean addWishList, boolean isLastItem);

    void processToUpdateCartData(List<CartItemData> cartItemDataList);

    void processToUpdateAndReloadCartData();

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

    boolean dataHasChanged();

    void setCheckedCartItemState(List<CartItemHolderData> cartItemHolderDataList);

    Map<Integer, Boolean> getCheckedCartItemState();

}
