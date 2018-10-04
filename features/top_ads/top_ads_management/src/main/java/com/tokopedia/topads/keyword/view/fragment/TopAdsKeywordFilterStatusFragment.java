package com.tokopedia.topads.keyword.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.SparseArrayCompat;

import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.keyword.constant.KeywordStatusTypeDef;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsFilterStatusFragment;

/**
 * Created by Nathaniel on 1/31/2017.
 */

public class TopAdsKeywordFilterStatusFragment extends TopAdsFilterStatusFragment {

    SparseArrayCompat<Integer> map = new SparseArrayCompat<>();

    public static TopAdsKeywordFilterStatusFragment createInstance(int status) {
        TopAdsKeywordFilterStatusFragment fragment = new TopAdsKeywordFilterStatusFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, status);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        map.put(0, KeywordStatusTypeDef.KEYWORD_STATUS_ALL);
        map.put(1, KeywordStatusTypeDef.KEYWORD_STATUS_ACTIVE);
        map.put(2, KeywordStatusTypeDef.KEYWORD_STATUS_INACTIVE);
    }

    public String[] getStatusValueList() {
        return getResources().getStringArray(R.array.top_ads_keyword_filter_status_list_values);
    }

    public String[] getStatusNameList() {
        return getResources().getStringArray(R.array.top_ads_keyword_filter_status_list_entries);
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public Intent addResult(Intent intent) {
        if (selectedAdapterPosition > -1) {
            Bundle extras = new Bundle();
            extras.putInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, map.get(selectedAdapterPosition));
            intent.putExtras(extras);
        }
        return intent;
    }
}