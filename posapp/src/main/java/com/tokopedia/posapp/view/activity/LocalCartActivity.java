package com.tokopedia.posapp.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.app.ReactNativeActivity;
import com.tokopedia.core.react.ReactConst;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.react.PosReactConst;

/**
 * Created by okasurya on 9/12/17.
 */

public class LocalCartActivity extends ReactNativeActivity {
    @Override
    protected Bundle getPropsBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(ReactConst.KEY_SCREEN, PosReactConst.Screen.MAIN_POS_O2O);
        bundle.putString(PosReactConst.Screen.PARAM_POS_PAGE, PosReactConst.Page.LOCAL_CART);
        bundle.putString(USER_ID, SessionHandler.getLoginID(this));

        return bundle;
    }
}
