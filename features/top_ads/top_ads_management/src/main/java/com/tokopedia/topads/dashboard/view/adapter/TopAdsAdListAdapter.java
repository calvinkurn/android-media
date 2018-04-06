package com.tokopedia.topads.dashboard.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.topads.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.topads.dashboard.view.model.Ad;
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TopAdsViewHolder;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public class TopAdsAdListAdapter<T extends Ad> extends BaseListAdapter<T> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ProductAd.TYPE:
            case GroupAd.TYPE:
                return new TopAdsViewHolder(getLayoutView(parent, R.layout.item_top_ads_ad));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }
}