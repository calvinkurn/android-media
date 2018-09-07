package com.tokopedia.feedplus.view.fragment;

import android.os.Bundle;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment;

/**
 * Created by naveengoyal on 3/22/18.
 */

public class ReactNativeExploreContentFragment extends ReactNativeFragment {

    @Override
    public String getModuleName() {
        return ReactConst.MAIN_MODULE;
    }

    @Override
    protected Bundle getInitialBundle() {
        return getArguments() != null ? getArguments() : new Bundle();
    }

    public static ReactNativeExploreContentFragment createInstance(Bundle bundle){
        ReactNativeExploreContentFragment fragment = new ReactNativeExploreContentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
