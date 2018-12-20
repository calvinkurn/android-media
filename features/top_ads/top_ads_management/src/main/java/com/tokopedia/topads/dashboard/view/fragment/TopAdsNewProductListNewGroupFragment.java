package com.tokopedia.topads.dashboard.view.fragment;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.topads.common.util.TopAdsComponentUtils;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductListStepperModel;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsGetProductDetailPresenter;

/**
 * Created by zulfikarrahman on 8/7/17.
 */

public class TopAdsNewProductListNewGroupFragment extends TopAdsNewProductListFragment<TopAdsProductListStepperModel, TopAdsGetProductDetailPresenter>{

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerTopAdsCreatePromoComponent.builder()
                .topAdsCreatePromoModule(new TopAdsCreatePromoModule())
                .topAdsComponent(TopAdsComponentUtils.getTopAdsComponent(this))
                .build()
                .inject(this);
        daggerPresenter.attachView(this);
    }

    @Override
    protected void initiateStepperModel() {
        stepperModel = new TopAdsProductListStepperModel();
    }

    @Override
    protected void goToNextPage() {
        UnifyTracking.eventTopAdsProductAddPromoStep1(getActivity());
        hideLoading();
        stepperListener.goToNextPage(stepperModel);
    }

    @Override
    protected boolean isHideExistingGroup() {
        return false;
    }
}
