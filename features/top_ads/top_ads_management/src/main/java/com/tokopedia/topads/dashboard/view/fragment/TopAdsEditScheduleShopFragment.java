package com.tokopedia.topads.dashboard.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.topads.R;
import com.tokopedia.seller.base.view.model.StepperModel;
import com.tokopedia.topads.common.util.TopAdsComponentUtils;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailShopViewModel;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailEditShopPresenter;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsEditScheduleShopFragment extends TopAdsNewScheduleFragment<StepperModel, TopAdsDetailShopViewModel, TopAdsDetailEditShopPresenter> {

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
    protected void initView(View view) {
        super.initView(view);
        headerText.setVisibility(View.GONE);
        submitButton.setText(getString(R.string.label_top_ads_save));
    }

    @Override
    protected void onNextClicked() {
        super.onNextClicked();
        if(detailAd != null) {
            daggerPresenter.saveAd(detailAd);
        }
    }

    @Override
    public void onSaveAdSuccess(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        super.onSaveAdSuccess(topAdsDetailAdViewModel);
        getActivity().finish();
    }

    @Override
    public void onSuggestionSuccess(GetSuggestionResponse s) {
        /* just deal with abstraction */
    }

    @Override
    public void onSuggestionError(@Nullable Throwable t) {
        /* just deal with abstraction */
    }

    @Override
    protected TopAdsDetailShopViewModel initiateDetailAd() {
        return new TopAdsDetailShopViewModel();
    }

    public static Fragment createInstance(String shopId) {
        Fragment fragment = new TopAdsEditScheduleShopFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, shopId);
        fragment.setArguments(bundle);
        return fragment;
    }
}
