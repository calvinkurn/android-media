package com.tokopedia.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsEditGroupMainPageFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsEditProductMainPageFragment;

/**
 * Created by zulfikarrahman on 8/9/17.
 */

public class TopAdsEditProductMainPageActivity extends BaseSimpleActivity {

    public static Intent createIntent(Context context, ProductAd productAd, String adId){
        Intent intent = new Intent(context, TopAdsEditProductMainPageActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD, productAd);
        return intent;
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }

    @Override
    protected Fragment getNewFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(getTagFragment());
        if(fragment != null){
            return fragment;
        }else{
            ProductAd ad = null;
            String adId = null;
            if (getIntent() != null && getIntent().getExtras() != null) {
                ad = getIntent().getExtras().getParcelable(TopAdsExtraConstant.EXTRA_AD);
                adId = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_AD_ID);
            }
            fragment = TopAdsEditProductMainPageFragment.createInstance(ad, adId);
            return fragment;
        }
    }
}
