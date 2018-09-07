package com.tokopedia.posapp.payment.checkout;

import android.os.Bundle;

import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment;

/**
 * Created by okasurya on 1/18/18.
 */

public class ReactCheckoutFragment extends ReactNativeFragment {
    public static ReactCheckoutFragment newInstance(Bundle bundle) {
        ReactCheckoutFragment paymentFragment = new ReactCheckoutFragment();
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
