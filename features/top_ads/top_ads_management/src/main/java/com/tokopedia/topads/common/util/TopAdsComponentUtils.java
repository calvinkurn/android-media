package com.tokopedia.topads.common.util;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.topads.TopAdsComponentInstance;
import com.tokopedia.topads.TopAdsModuleRouter;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;

/**
 * Created by normansyahputa on 10/20/17.
 */

@Deprecated
public class TopAdsComponentUtils {

    public static TopAdsComponent getTopAdsComponent(BaseDaggerFragment fragment){
        return TopAdsComponentInstance.getComponent(fragment.getActivity().getApplication());
    }

    public static TopAdsComponent getTopAdsComponent(Activity activity) {
        if (activity != null && activity.getApplication() != null && activity.getApplication() instanceof TopAdsModuleRouter) {
            return ((TopAdsModuleRouter) activity.getApplication()).getTopAdsComponent();
        } else {
            return null;
        }
    }
}
