package com.tokopedia.topads.keyword.view.fragment;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.topads.R;
import com.tokopedia.topads.keyword.view.activity.TopAdsKeywordDetailActivity;
import com.tokopedia.topads.keyword.view.model.KeywordAd;
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption;

/**
 * Created by hadi.putra on 11/05/18.
 */

public class TopAdsKeywordPositiveListFragment extends TopAdsKeywordAdListFragment {
    @Override
    public boolean isPositive() {
        return true;
    }

    @Override
    public boolean isStatusShown() {
        return true;
    }

    @Override
    public String getSourceTagging() {
        return TopAdsSourceOption.SA_MANAGE_KEYWORD_POSITIVE;
    }

    @Override
    public Visitable getDefaultEmptyViewModel() {
        EmptyModel emptyModel = (EmptyModel) super.getDefaultEmptyViewModel();
        emptyModel.setTitle(getString(R.string.top_ads_keyword_your_keyword_empty));
        emptyModel.setContent(getString(R.string.top_ads_keyword_please_use));
        return emptyModel;
    }

    @Override
    protected String getScreenName() {
        return getClass().getSimpleName();
    }

    @Override
    public void onAdClicked(KeywordAd ad) {
        presenter.saveSourceTagging(getSourceTagging());
        startActivityForResult(TopAdsKeywordDetailActivity.createInstance(getActivity(), ad, ad.getId()), REQUEST_CODE_AD_CHANGE);
    }
}
