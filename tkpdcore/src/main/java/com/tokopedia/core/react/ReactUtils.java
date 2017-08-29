package com.tokopedia.core.react;

import android.content.Context;
import android.support.annotation.Nullable;

import com.facebook.react.ReactApplication;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.tokopedia.core.app.MainApplication;

/**
 * @author  by alvarisi on 8/11/17.
 */

public class ReactUtils {
    private static ReactUtils instance;
    private ReactApplication reactApplication;

    protected ReactUtils(ReactApplication reactApplication) {
        this.reactApplication = reactApplication;
    }

    public static ReactUtils init(ReactApplication reactApplication) {
        if(instance == null) instance = new ReactUtils(reactApplication);

        return instance;
    }

    public void sendAddWishlistEmitter(String productId, String userId) {
        WritableMap params = Arguments.createMap();
        params.putString("product_id", productId);
        params.putString("user_id", userId);
        sendEmittEvent(
                reactApplication.getReactNativeHost().getReactInstanceManager().getCurrentReactContext(),
                ReactConst.EventEmitter.WISHLIST_ADD,
                params
        );
    }

    public void sendRemoveWishlistEmitter(String productId, String userId) {
        WritableMap params = Arguments.createMap();
        params.putString("product_id", productId);
        params.putString("user_id", userId);
        sendEmittEvent(
                reactApplication.getReactNativeHost().getReactInstanceManager().getCurrentReactContext(),
                ReactConst.EventEmitter.WISHLIST_REMOVE,
                params
        );
    }

    public void sendAddFavoriteEmitter(String shopId, String userId) {
        WritableMap params = Arguments.createMap();
        params.putString("shop_id", shopId);
        params.putString("user_id", userId);
        sendEmittEvent(
                reactApplication.getReactNativeHost().getReactInstanceManager().getCurrentReactContext(),
                ReactConst.EventEmitter.FAVORITE_ADD,
                params
        );
    }

    public void sendRemoveFavoriteEmitter(String shopId, String userId) {
        WritableMap params = Arguments.createMap();
        params.putString("shop_id", shopId);
        params.putString("user_id", userId);
        sendEmittEvent(
                reactApplication.getReactNativeHost().getReactInstanceManager().getCurrentReactContext(),
                ReactConst.EventEmitter.FAVORITE_REMOVE,
                params
        );
    }

    public void sendLoginEmitter(String userId) {
        WritableMap params = Arguments.createMap();
        params.putString("user_id", userId);
        sendEmittEvent(
                reactApplication.getReactNativeHost().getReactInstanceManager().getCurrentReactContext(),
                ReactConst.EventEmitter.LOGIN,
                params
        );
    }

    private void sendEmittEvent(ReactContext reactContext,
                                       String eventName,
                                       @Nullable WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    public void sendDestroyPageEmitter() {
        WritableMap params = Arguments.createMap();
        sendEmittEvent(
                reactApplication.getReactNativeHost().getReactInstanceManager().getCurrentReactContext(),
                ReactConst.EventEmitter.PAGE_DESTROYED,
                params
        );
    }
}
