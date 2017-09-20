package com.tokopedia.posapp.view.activity;

import android.os.Bundle;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.react.PosReactConst;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeActivity;

/**
 * Created by okasurya on 9/20/17.
 */

public class TransactionHistoryActivity extends ReactNativeActivity {
    @Override
    protected Bundle getPropsBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(ReactConst.KEY_SCREEN, PosReactConst.Screen.MAIN_POS_O2O);
        bundle.putString(PosReactConst.Screen.PARAM_POS_PAGE, PosReactConst.Page.TRANSACTION_HISTORY);
        bundle.putString(USER_ID, SessionHandler.getLoginID(this));

        return bundle;
    }
}
