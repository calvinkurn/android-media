package com.tokopedia.topads.keyword.view.fragment;

import android.view.View;

import com.tokopedia.topads.R;
import com.tokopedia.topads.keyword.view.model.KeywordAd;

/**
 * @author sebastianuskh on 5/26/17.
 */

public class TopAdsKeywordEditDetailPositiveFragment extends TopAdsKeywordEditDetailFragment {

    public static TopAdsKeywordEditDetailPositiveFragment createInstance(KeywordAd model) {
        TopAdsKeywordEditDetailPositiveFragment fragment = new TopAdsKeywordEditDetailPositiveFragment();
        fragment.setArguments(createArguments(model));
        return fragment;
    }

    @Override
    protected void settingTopAdsKeywordType(View view) {
        super.settingTopAdsKeywordType(view);
        topAdsKeywordType.setEntries(getResources().getStringArray(R.array.top_ads_keyword_type_list_entries));
        topAdsKeywordType.setValues(getResources().getStringArray(R.array.top_ads_keyword_type_positive_list_values));
    }
}
