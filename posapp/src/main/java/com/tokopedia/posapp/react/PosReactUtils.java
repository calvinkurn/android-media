package com.tokopedia.posapp.react;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.react.*;

/**
 * Created by okasurya on 9/12/17.
 */

public class PosReactUtils extends ReactUtils {
    void openLocalCart(boolean status){
        WritableMap params = Arguments.createMap();
        params.putBoolean("status", status);

        sendEmittEvent(
                MainApplication.getInstance().getReactNativeHost().getReactInstanceManager().getCurrentReactContext(),
                PosReactConst.EventEmitter.LOCAL_CART_OPEN,
                params
        );
    }
}
