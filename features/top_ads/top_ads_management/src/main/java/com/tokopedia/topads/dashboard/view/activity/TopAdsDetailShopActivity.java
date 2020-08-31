package com.tokopedia.topads.dashboard.view.activity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.topads.common.view.activity.TopAdsBaseActivity;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.ShopAd;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDetailShopFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewScheduleNewGroupFragment;

public class TopAdsDetailShopActivity extends TopAdsBaseActivity {

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

}