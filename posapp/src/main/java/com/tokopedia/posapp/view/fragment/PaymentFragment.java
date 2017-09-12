package com.tokopedia.posapp.view.fragment;

import android.os.Bundle;

import com.tokopedia.core.app.ReactNativeFragmentV2;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.react.PosReactConst;

/**
 * Created by okasurya on 9/12/17.
 */

public class PaymentFragment extends ReactNativeFragmentV2 {
    public static PaymentFragment newInstance(String page) {
        Bundle args = new Bundle();
        args.putString(PosReactConst.Screen.PARAM_POS_PAGE, page);

        PaymentFragment fragment = new PaymentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public String getReactScreenName() {
        return PosReactConst.Screen.MAIN_POS_O2O;
    }

    @Override
    public String getUserId() {
        return SessionHandler.getLoginID(getContext());
    }
}
