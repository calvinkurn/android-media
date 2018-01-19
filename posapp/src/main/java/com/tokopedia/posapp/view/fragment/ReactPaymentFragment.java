package com.tokopedia.posapp.view.fragment;

import android.os.Bundle;

import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment;

/**
 * Created by okasurya on 1/18/18.
 */

public class ReactPaymentFragment extends ReactNativeFragment {
    public static ReactPaymentFragment newInstance(Bundle bundle) {
        ReactPaymentFragment paymentFragment = new ReactPaymentFragment();
        paymentFragment.setArguments(bundle);

        return paymentFragment;
    }

    @Override
    public String getModuleName() {
        return ReactConst.MAIN_MODULE;
    }

    @Override
    protected Bundle getInitialBundle() {
        return getArguments() != null ? getArguments() : new Bundle();
    }
}
