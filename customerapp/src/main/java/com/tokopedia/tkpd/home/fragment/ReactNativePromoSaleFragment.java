package com.tokopedia.tkpd.home.fragment;

import android.os.Bundle;

import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment;

/**
 * Created by yogieputra on 08/01/18.
 */

public class ReactNativePromoSaleFragment extends ReactNativeFragment {

    @Override
    public String getModuleName() {
        return ReactConst.MAIN_MODULE;
    }

    @Override
    protected Bundle getInitialBundle() {
        return getArguments() != null ? getArguments() : new Bundle();
    }

    public static ReactNativePromoSaleFragment createInstance(Bundle bundle){
        ReactNativePromoSaleFragment fragment = new ReactNativePromoSaleFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
