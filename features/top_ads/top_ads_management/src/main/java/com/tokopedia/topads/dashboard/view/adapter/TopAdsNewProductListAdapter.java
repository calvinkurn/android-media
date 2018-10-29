package com.tokopedia.topads.dashboard.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.base.list.seller.view.adapter.BaseListAdapter;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TopAdsNewProductListViewHolder;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;

/**
 * Created by zulfikarrahman on 8/1/17.
 */

public class TopAdsNewProductListAdapter extends BaseListAdapter<TopAdsProductViewModel> {

    private final TopAdsNewProductListViewHolder.DeleteListener deleteListener;

    public TopAdsNewProductListAdapter(TopAdsNewProductListViewHolder.DeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TopAdsProductViewModel.TYPE:
                return new TopAdsNewProductListViewHolder(getLayoutView(parent, R.layout.item_top_ads_add_product), deleteListener);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

}
