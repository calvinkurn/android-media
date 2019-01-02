package com.tokopedia.topads.dashboard.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.base.list.seller.view.adapter.BaseListAdapter;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TopAdsAddingPromoOptionViewHolder;
import com.tokopedia.topads.dashboard.view.model.TopAdsAddingPromoOptionModel;

/**
 * Created by hadi.putra on 26/04/18.
 */

public class TopAdsAddingPromoOptionAdapter extends BaseListAdapter<TopAdsAddingPromoOptionModel> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TopAdsAddingPromoOptionModel.TYPE){
            return new TopAdsAddingPromoOptionViewHolder(getLayoutView(parent, R.layout.item_top_ads_adding_option));
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }
}
