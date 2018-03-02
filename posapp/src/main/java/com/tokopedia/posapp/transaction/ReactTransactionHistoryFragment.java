package com.tokopedia.posapp.transaction;

import android.os.Bundle;

import com.tokopedia.posapp.react.PosReactConst;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment;

/**
 * Created by okasurya on 9/26/17.
 */

public class ReactTransactionHistoryFragment extends ReactNativeFragment {
    public static ReactTransactionHistoryFragment newInstance() {
        Bundle args = new Bundle();
        args.putString(PosReactConst.Screen.PARAM_POS_PAGE, PosReactConst.Page.TRANSACTION_HISTORY);
        ReactTransactionHistoryFragment fragment = new ReactTransactionHistoryFragment();
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
        return bundle;
    }
}
