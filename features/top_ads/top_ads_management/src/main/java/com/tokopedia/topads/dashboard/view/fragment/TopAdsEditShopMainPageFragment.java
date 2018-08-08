package com.tokopedia.topads.dashboard.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.ShopAd;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsShopAdInteractorImpl;
import com.tokopedia.topads.dashboard.view.activity.TopAdsEditCostShopActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsEditScheduleShopActivity;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailShopPresenterImpl;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsEditShopMainPageFragment extends TopAdsDetailEditMainPageFragment<ShopAd> {

    public static Fragment createInstance(String adId) {
        Fragment fragment = new TopAdsEditShopMainPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initialPresenter() {
        super.initialPresenter();
        presenter = new TopAdsDetailShopPresenterImpl(getActivity(), this,
                new TopAdsShopAdInteractorImpl(getActivity()));
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_edit_shop_main_page;
    }

    @Override
    protected void refreshAd() {
        if (ad != null) {
            presenter.refreshAd(startDate, endDate, ad.getId());
        } else {
            presenter.refreshAd(startDate, endDate, adId);
        }
    }

    @Override
    protected void onScheduleClicked() {
        Intent intent;
        if(ad!= null) {
            intent = TopAdsEditScheduleShopActivity.createIntent(getActivity(), String.valueOf(ad.getId()));
        }else{
            intent = TopAdsEditScheduleShopActivity.createIntent(getActivity(), adId);
        }
        startActivityForResult(intent, REQUEST_CODE_AD_EDIT);
    }

    @Override
    protected void onCostClicked() {
        Intent intent;
        if(ad!= null) {
            intent = TopAdsEditCostShopActivity.createIntent(getActivity(), String.valueOf(ad.getId()));
        }else{
            intent = TopAdsEditCostShopActivity.createIntent(getActivity(), adId);
        }
        startActivityForResult(intent, REQUEST_CODE_AD_EDIT);
    }
}
