package com.tokopedia.topads.keyword.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.base.list.seller.view.adapter.BaseListAdapter;
import com.tokopedia.topads.R;
import com.tokopedia.topads.keyword.view.adapter.viewholder.TopAdsOldKeywordViewHolder;
import com.tokopedia.topads.keyword.view.model.KeywordAd;
import com.tokopedia.topads.keyword.view.model.NegativeKeywordAd;

/**
 * @author normansyahputa on 5/19/17.
 */

public class TopAdsKeywordAdapter<T extends KeywordAd> extends BaseListAdapter<T> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case KeywordAd.TYPE:
            case NegativeKeywordAd.TYPE:
                return new TopAdsOldKeywordViewHolder(getLayoutView(parent, R.layout.item_top_ads_keyword_main));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }
}