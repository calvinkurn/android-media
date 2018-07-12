package com.tokopedia.wishlist.common.listener;

public interface TkpdWishListActionListener {
    void onErrorAddWishList(String errorMessage, String productId);

    void onSuccessAddWishlist(String productId);

    void onErrorRemoveWishlist(String errorMessage, String productId);

    void onSuccessRemoveWishlist(String productId);

    String getString(int resId);
}
