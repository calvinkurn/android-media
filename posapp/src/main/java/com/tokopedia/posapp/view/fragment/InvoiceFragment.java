package com.tokopedia.posapp.view.fragment;

import android.os.Bundle;

import com.tokopedia.posapp.react.PosReactConst;
import com.tokopedia.posapp.view.activity.InvoiceActivity;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment;

/**
 * Created by okasurya on 10/13/17.
 */

public class InvoiceFragment extends ReactNativeFragment {

    public static InvoiceFragment newInstance(String data) {
        InvoiceFragment fragment = new InvoiceFragment();
        Bundle bundle = new Bundle();
        bundle.putString(InvoiceActivity.DATA, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public String getModuleName() {
        return ReactConst.MAIN_MODULE;
    }

    @Override
    protected Bundle getInitialBundle() {
        Bundle bundle = getArguments();
        bundle.putString(ReactConst.KEY_SCREEN, PosReactConst.Screen.MAIN_POS_O2O);
        bundle.putString(PosReactConst.Screen.PARAM_POS_PAGE, PosReactConst.Page.INVOICE);

        return bundle;
    }
}
