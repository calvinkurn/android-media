package com.tokopedia.posapp.payment.invoice;

import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.posapp.react.PosReactConst;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment;

/**
 * Created by okasurya on 10/13/17.
 */

public class ReactInvoiceFragment extends ReactNativeFragment {

    public static ReactInvoiceFragment newInstance(Bundle bundle) {
        ReactInvoiceFragment fragment = new ReactInvoiceFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public String getModuleName() {
        return ReactConst.MAIN_MODULE;
    }

    @Override
    protected Bundle getInitialBundle() {
        Bundle bundle = getFilteredBundle();

        bundle.putString(ReactConst.KEY_SCREEN, PosReactConst.Screen.MAIN_POS_O2O);
        bundle.putString(PosReactConst.Screen.PARAM_POS_PAGE, PosReactConst.Page.INVOICE);

        return bundle;
    }

    private Bundle getFilteredBundle() {
        Bundle bundle = new Bundle();
        for(String key: getArguments().keySet()) {
            if(!key.equals(DeepLink.IS_DEEP_LINK)
                    && !key.equals(DeepLink.REFERRER_URI)
                    && !key.equals(DeepLink.URI)) {
                if(getArguments().get(key) instanceof String) {
                    bundle.putString(key, getArguments().getString(key));
                } else if(getArguments().get(key) instanceof Boolean) {
                    bundle.putBoolean(key, getArguments().getBoolean(key));
                }
            }
        }
        return bundle;
    }
}
