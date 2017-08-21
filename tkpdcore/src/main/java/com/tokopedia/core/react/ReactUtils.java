package com.tokopedia.core.react;

import android.support.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.tokopedia.core.app.MainApplication;

/**
 * @author  by alvarisi on 8/11/17.
 */

public class ReactUtils {

    public static void sendAddWishlistEmitter(String productId, String userId) {
        WritableMap params = Arguments.createMap();
        params.putString("product_id", productId);
        params.putString("user_id", userId);
        sendEmittEvent(
                MainApplication.getInstance().getReactNativeHost().getReactInstanceManager().getCurrentReactContext(),
                ReactConst.EventEmitter.WISHLIST_ADD,
                params
        );
    }

    public static void sendRemoveWishlistEmitter(String productId, String userId) {
        WritableMap params = Arguments.createMap();
        params.putString("product_id", productId);
        params.putString("user_id", userId);
        sendEmittEvent(
                MainApplication.getInstance().getReactNativeHost().getReactInstanceManager().getCurrentReactContext(),
                ReactConst.EventEmitter.WISHLIST_REMOVE,
                params
        );
    }

    public static void sendAddFavoriteEmitter(String shopId, String userId) {
        WritableMap params = Arguments.createMap();
        params.putString("shop_id", shopId);
        params.putString("user_id", userId);
        sendEmittEvent(
                MainApplication.getInstance().getReactNativeHost().getReactInstanceManager().getCurrentReactContext(),
                ReactConst.EventEmitter.FAVORITE_ADD,
                params
        );
    }

    public static void sendRemoveFavoriteEmitter(String shopId, String userId) {
        WritableMap params = Arguments.createMap();
        params.putString("shop_id", shopId);
        params.putString("user_id", userId);
        sendEmittEvent(
                MainApplication.getInstance().getReactNativeHost().getReactInstanceManager().getCurrentReactContext(),
                ReactConst.EventEmitter.FAVORITE_REMOVE,
                params
        );
    }

    public static void sendLoginEmitter(String userId) {
        WritableMap params = Arguments.createMap();
        params.putString("user_id", userId);
        sendEmittEvent(
                MainApplication.getInstance().getReactNativeHost().getReactInstanceManager().getCurrentReactContext(),
                ReactConst.EventEmitter.LOGIN,
                params
        );
    }

    private static void sendEmittEvent(ReactContext reactContext,
                                       String eventName,
                                       @Nullable WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    public static void sendDestroyPageEmitter() {
        WritableMap params = Arguments.createMap();
        sendEmittEvent(
                MainApplication.getInstance().getReactNativeHost().getReactInstanceManager().getCurrentReactContext(),
                ReactConst.EventEmitter.PAGE_DESTROYED,
                params
        );
    }
}
