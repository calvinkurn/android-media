package com.tokopedia.topads.keyword.view.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.keyword.view.activity.TopAdsKeywordDetailNegativeActivity;
import com.tokopedia.topads.keyword.view.model.KeywordAd;
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption;

import java.util.List;

/**
 * Created by hadi.putra on 11/05/18.
 */

public class TopAdsKeywordNegativeListFragment extends TopAdsKeywordAdListFragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showDateLabel(false);
    }

    @Override
    public boolean isPositive() {
        return false;
    }

    @Override
    public boolean isStatusShown() {
        return false;
    }

    @Override
    public String getSourceTagging() {
        return TopAdsSourceOption.SA_MANAGE_KEYWORD_NEGATIVE;
    }

    @Override
    public Visitable getDefaultEmptyViewModel() {
        EmptyModel emptyModel = (EmptyModel) super.getDefaultEmptyViewModel();
        emptyModel.setTitle(getString(R.string.top_ads_keyword_your_keyword_negative_empty));
        emptyModel.setContent(getString(R.string.top_ads_keyword_please_use_negative));
        return emptyModel;
    }

    @Override
    protected String getScreenName() {
        return getClass().getSimpleName();
    }

    @Override
    public void onAdClicked(KeywordAd ad) {
        presenter.saveSourceTagging(getSourceTagging());
        startActivityForResult(TopAdsKeywordDetailNegativeActivity.createInstance(getActivity(), ad, ""), REQUEST_CODE_AD_CHANGE);
    }
}
