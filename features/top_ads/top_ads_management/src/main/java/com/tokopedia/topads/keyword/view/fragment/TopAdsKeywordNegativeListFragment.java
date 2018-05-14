package com.tokopedia.topads.keyword.view.fragment;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.topads.R;
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption;

/**
 * Created by hadi.putra on 11/05/18.
 */

public class TopAdsKeywordNegativeListFragment extends TopAdsKeywordAdListFragment {
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
        emptyModel.setContent(getString(R.string.top_ads_empty_product_promo_content_text));
        return emptyModel;
    }

    @Override
    protected String getScreenName() {
        return getClass().getSimpleName();
    }
}
