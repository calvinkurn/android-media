package com.tokopedia.affiliate.feature.dashboard.view.fragment;

import android.os.Bundle;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.affiliate.common.di.DaggerAffiliateComponent;
import com.tokopedia.affiliate.feature.dashboard.di.DaggerDashboardComponent;

/**
 * @author by yfsx on 13/09/18.
 */
public class DashboardFragment extends BaseDaggerFragment {

    public static DashboardFragment getInstance(Bundle bundle) {
        DashboardFragment fragment = new DashboardFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        DaggerAffiliateComponent affiliateComponent = (DaggerAffiliateComponent) DaggerAffiliateComponent.builder()
                .baseAppComponent(((BaseMainApplication)getActivity().getApplicationContext()).getBaseAppComponent()).build();

        DaggerDashboardComponent.builder()
                .affiliateComponent(affiliateComponent)
                .build().inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
