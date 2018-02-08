package com.tokopedia.posapp.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.react.PosReactConst;

/**
 * Created by okasurya on 8/24/17.
 */

public class ReactProductListFragment extends ReactNativeFragment {

    public static final String SHOP_ID = "SHOP_ID";
    public static final String ETALASE_ID = "ETALASE_ID";
    private static final String USER_ID = "USER_ID";
    public static final String CLEAR_STATE = "clearState";

    public static ReactProductListFragment newInstance(String shopId, String etalaseId) {
        Bundle args = new Bundle();
        args.putString(SHOP_ID, shopId);
        args.putString(ETALASE_ID, etalaseId);
        args.putString(PosReactConst.Screen.PARAM_POS_PAGE, PosReactConst.Page.PRODUCT_LIST);
        ReactProductListFragment fragment = new ReactProductListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String getModuleName() {
        return ReactConst.MAIN_MODULE;
    }

    @Override
    protected Bundle getInitialBundle() {
        Bundle bundle = new Bundle();
        bundle.putAll(getArguments());
        bundle.putString(ReactConst.KEY_SCREEN, PosReactConst.Screen.MAIN_POS_O2O);
        bundle.putString(USER_ID, SessionHandler.getLoginID(getActivity()));
        return bundle;
    }

    @Override
    public void onResume() {
        super.onResume();
        clearState();
    }

    private void clearState() {
        WritableMap params = Arguments.createMap();
        params.putBoolean(CLEAR_STATE, true);
        sendEmitter(reactInstanceManager.getCurrentReactContext(), CLEAR_STATE, params);
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
