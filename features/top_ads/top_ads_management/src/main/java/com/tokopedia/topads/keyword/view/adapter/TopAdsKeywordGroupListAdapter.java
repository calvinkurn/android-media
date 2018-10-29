package com.tokopedia.topads.keyword.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.base.list.seller.view.adapter.BaseListAdapter;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.keyword.view.adapter.viewholder.TopAdsKeywordGroupViewHolder;

/**
 * @author normansyahputa on 5/26/17.
 */

public class TopAdsKeywordGroupListAdapter extends BaseListAdapter<GroupAd> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case GroupAd.TYPE:
                return new TopAdsKeywordGroupViewHolder(getLayoutView(parent, R.layout.item_top_ads_keyword_group_name_item));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }
    public interface Listener {
        void notifySelect(GroupAd groupAd, int position);
    }
}