package com.tokopedia.wishlist.common.listener;

public interface WishListActionListener {
    void onErrorAddWishList(String errorMessage, String productId);

    void onSuccessAddWishlist(String productId);

    void onErrorRemoveWishlist(String errorMessage, String productId);

    void onSuccessRemoveWishlist(String productId);
}
