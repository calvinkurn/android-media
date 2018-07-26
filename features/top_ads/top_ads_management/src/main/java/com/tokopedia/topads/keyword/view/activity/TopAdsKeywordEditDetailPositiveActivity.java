package com.tokopedia.topads.keyword.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordEditDetailPositiveFragment;
import com.tokopedia.topads.keyword.view.model.KeywordAd;

/**
 * @author sebastianuskh on 5/23/17.
 */

public class TopAdsKeywordEditDetailPositiveActivity extends BaseSimpleActivity implements HasComponent<AppComponent> {

    public static Intent createInstance(Context context, KeywordAd keywordAd){
        Intent intent = new Intent(context, TopAdsKeywordEditDetailPositiveActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD, keywordAd);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        KeywordAd keywordAd = getIntent().getParcelableExtra(TopAdsExtraConstant.EXTRA_AD);
        return TopAdsKeywordEditDetailPositiveFragment.createInstance(keywordAd);
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