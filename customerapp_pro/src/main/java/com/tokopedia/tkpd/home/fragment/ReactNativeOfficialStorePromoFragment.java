package com.tokopedia.tkpd.home.fragment;

import android.os.Bundle;

import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment;

/**
 * Created by yogieputra on 05/01/18.
 */

public class ReactNativeOfficialStorePromoFragment extends ReactNativeFragment {

    @Override
    public String getModuleName() {
        return ReactConst.MAIN_MODULE;
    }

    @Override
    protected Bundle getInitialBundle() {
        return getArguments() != null ? getArguments() : new Bundle();
    }

    public static ReactNativeOfficialStorePromoFragment createInstance(Bundle bundle){
        ReactNativeOfficialStorePromoFragment fragment = new ReactNativeOfficialStorePromoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
