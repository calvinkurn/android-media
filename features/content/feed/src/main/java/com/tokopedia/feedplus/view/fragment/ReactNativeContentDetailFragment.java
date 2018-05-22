package com.tokopedia.feedplus.view.fragment;
import android.os.Bundle;

import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment;

/**
 * Created by naveengoyal on 3/26/18.
 */

public class ReactNativeContentDetailFragment extends ReactNativeFragment {

    @Override
    public String getModuleName() {
        return ReactConst.MAIN_MODULE;
    }

    @Override
    protected Bundle getInitialBundle() {
        return getArguments() != null ? getArguments() : new Bundle();
    }

    public static ReactNativeContentDetailFragment createInstance(Bundle bundle){
        ReactNativeContentDetailFragment fragment = new ReactNativeContentDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}