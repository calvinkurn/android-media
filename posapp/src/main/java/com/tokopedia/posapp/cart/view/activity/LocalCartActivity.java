package com.tokopedia.posapp.cart.view.activity;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.applink.PosAppLink;
import com.tokopedia.posapp.product.productlist.view.activity.ProductListActivity;
import com.tokopedia.posapp.react.PosReactConst;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeActivity;

/**
 * Created by okasurya on 9/12/17.
 */

public class LocalCartActivity extends ReactNativeActivity {

    public static final String BACK_FROM_CART = "backFromCart";

    @DeepLink(PosAppLink.CART)
    public static TaskStackBuilder newInstance(Context context, Bundle extras) {
        Uri uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon().build();
        Intent cartIntent = new Intent(context, LocalCartActivity.class).setData(uri).putExtras(extras);
        Intent parentIntent = new Intent(context, ProductListActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntent(parentIntent);
        taskStackBuilder.addNextIntent(cartIntent);

        return taskStackBuilder;
    }

    public static TaskStackBuilder newTopInstance(Context context) {
        Intent cartIntent = new Intent(context, LocalCartActivity.class);
        Intent parentIntent = new Intent(context, ProductListActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntent(parentIntent);
        taskStackBuilder.addNextIntent(cartIntent);

        return taskStackBuilder;
    }

    @Override
    protected Bundle getPropsBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(ReactConst.KEY_SCREEN, PosReactConst.Screen.MAIN_POS_O2O);
        bundle.putString(PosReactConst.Screen.PARAM_POS_PAGE, PosReactConst.Page.LOCAL_CART);
        bundle.putString(USER_ID, SessionHandler.getLoginID(this));

        return bundle;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backFromCart();
        finish();
    }

    private void backFromCart() {
        WritableMap params = Arguments.createMap();
        params.putBoolean(BACK_FROM_CART, true);
        sendEmitter(reactInstanceManager.getCurrentReactContext(), BACK_FROM_CART, params);
    }

    private void sendEmitter(ReactContext reactContext,
                             String eventName,
                             @Nullable WritableMap params) {
        if(reactContext != null) {
            reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, params);
        }
    }
}
