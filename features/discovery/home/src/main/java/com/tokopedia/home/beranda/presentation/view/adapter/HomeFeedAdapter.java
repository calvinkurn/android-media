package com.tokopedia.home.beranda.presentation.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeFeedTypeFactory;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.HomeFeedViewHolder;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedViewModel;

import java.util.List;

public class HomeFeedAdapter extends BaseListAdapter<HomeFeedViewModel, HomeFeedTypeFactory> {

    public HomeFeedAdapter(HomeFeedTypeFactory baseListAdapterTypeFactory) {
        super(baseListAdapterTypeFactory);
    }

    public HomeFeedAdapter(HomeFeedTypeFactory baseListAdapterTypeFactory, OnAdapterInteractionListener<HomeFeedViewModel> onAdapterInteractionListener) {
        super(baseListAdapterTypeFactory, onAdapterInteractionListener);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
        layoutParams.setFullSpan(getItemViewType(position) != HomeFeedViewHolder.LAYOUT);
        super.onBindViewHolder(holder, position);
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractViewHolder holder, int position, @NonNull List<Object> payloads) {
        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
        layoutParams.setFullSpan(getItemViewType(position) != HomeFeedViewHolder.LAYOUT);
        super.onBindViewHolder(holder, position, payloads);
    }
}
