package com.tokopedia.topads.dashboard.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.data.ShopAd;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDetailGroupFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDetailShopFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewScheduleNewGroupFragment;

public class TopAdsDetailShopActivity extends BaseSimpleActivity {

    public static final String TAG = TopAdsDetailShopFragment.class.getSimpleName();


    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    protected Fragment getNewFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(getTagFragment());
        if(fragment != null){
            return fragment;
        }else{
            ShopAd ad = null;
            String adId = null;
            boolean isEnoughDeposit = false;
            if (getIntent() != null && getIntent().getExtras() != null) {
                ad = getIntent().getExtras().getParcelable(TopAdsExtraConstant.EXTRA_AD);
                adId = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_AD_ID);
                isEnoughDeposit = getIntent().getBooleanExtra(TopAdsNewScheduleNewGroupFragment.EXTRA_IS_ENOUGH_DEPOSIT, false);
            }
            fragment = TopAdsDetailShopFragment.createInstance(ad, adId, isEnoughDeposit);
            return fragment;
        }
    }

    @Override
    protected String getTagFragment() {
        return TAG;
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }
}