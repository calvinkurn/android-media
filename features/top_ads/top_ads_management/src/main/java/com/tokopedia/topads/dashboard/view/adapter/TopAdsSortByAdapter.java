package com.tokopedia.topads.dashboard.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.base.list.seller.view.adapter.BaseListAdapter;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.SortTopAdsOption;
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TopAdsSortByViewHolder;
import com.tokopedia.topads.dashboard.view.model.TopAdsSortByModel;

/**
 * Created by nakama on 10/04/18.
 */

public class TopAdsSortByAdapter extends BaseListAdapter<TopAdsSortByModel> implements TopAdsSortByViewHolder.ListenerCheckedSort{

    @SortTopAdsOption
    private String sortTopAdsOption = SortTopAdsOption.LATEST;

    public void setSortTopAdsOption(String sortTopAdsOption) {
        this.sortTopAdsOption = sortTopAdsOption;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TopAdsSortByModel.TYPE:
                TopAdsSortByViewHolder topAdsSortByViewHolder = new TopAdsSortByViewHolder(
                        getLayoutView(parent, R.layout.item_product_manage_list_sort));
                topAdsSortByViewHolder.setListenerCheckedSort(this);
                return topAdsSortByViewHolder;
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public boolean isItemChecked(@SortTopAdsOption String productSortId) {
        return productSortId.equals(sortTopAdsOption);
    }
}
