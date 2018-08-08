package com.tokopedia.topads.keyword.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordDetailNegativeFragment;
import com.tokopedia.topads.keyword.view.model.KeywordAd;

/**
 * Created by zulfikarrahman on 5/26/17.
 */

public class TopAdsKeywordDetailNegativeActivity extends BaseSimpleActivity implements HasComponent<AppComponent> {

    public static Intent createInstance(Context context, KeywordAd keywordAd, String adId){
        Intent intent = new Intent(context, TopAdsKeywordDetailNegativeActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD, keywordAd);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        KeywordAd keywordAd = getIntent().getParcelableExtra(TopAdsExtraConstant.EXTRA_AD);
        String adId = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_AD_ID);
        return TopAdsKeywordDetailNegativeFragment.createInstance(keywordAd, adId);
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }
}
