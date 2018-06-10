package com.tokopedia.topads.keyword.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.topads.R;
import com.tokopedia.topads.keyword.view.model.KeywordAd;

/**
 * @author sebastianuskh on 5/30/17.
 */

public class TopAdsKeywordEditDetailNegativeFragment extends TopAdsKeywordEditDetailFragment {

    public static TopAdsKeywordEditDetailNegativeFragment createInstance(KeywordAd model) {
        TopAdsKeywordEditDetailNegativeFragment fragment = new TopAdsKeywordEditDetailNegativeFragment();
        fragment.setArguments(createArguments(model));
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        View costPerClickView = view.findViewById(R.id.linear_layout_cost_per_click);
        costPerClickView.setVisibility(View.GONE);
        return view;
    }

    @Override
    protected void settingTopAdsKeywordType(View view) {
        super.settingTopAdsKeywordType(view);
        topAdsKeywordType.setEntries(getResources().getStringArray(R.array.top_ads_keyword_type_list_entries));
        topAdsKeywordType.setValues(getResources().getStringArray(R.array.top_ads_keyword_type_negative_list_values));
    }

    @Override
    protected void settingTopAdsCostPerClick(View view) {
        super.settingTopAdsCostPerClick(view);
        topAdsCostPerClick.setVisibility(View.GONE);
        topAdsMaxPriceInstruction.setVisibility(View.GONE);
    }
}
