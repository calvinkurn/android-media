package com.tokopedia.wishlist.common.listener;

@Deprecated /*this interface will be deleted, use WishlistV2ActionListener from new depencendy : implementation projectOrAar(rootProject.ext.features.wishlistCommonV2) */
public interface WishListActionListener {
    void onErrorAddWishList(String errorMessage, String productId);

    void onSuccessAddWishlist(String productId);

    void onErrorRemoveWishlist(String errorMessage, String productId);

    void onSuccessRemoveWishlist(String productId);
}
