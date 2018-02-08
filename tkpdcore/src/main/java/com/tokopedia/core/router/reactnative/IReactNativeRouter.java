package com.tokopedia.core.router.reactnative;

/**
 * Created by alvarisi on 9/8/17.
 */

public interface IReactNativeRouter {
    void sendAddWishlistEmitter(String productId, String userId);

    void sendRemoveWishlistEmitter(String productId, String userId);

    void sendAddFavoriteEmitter(String shopId, String userId);

    void sendRemoveFavoriteEmitter(String shopId, String userId);

    void sendLoginEmitter(String userId);
}
