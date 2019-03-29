package com.tokopedia.feedplus.view.adapter.viewholder.topads;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.viewmodel.topads.FeedTopAdsViewModel;
import com.tokopedia.topads.sdk.listener.TopAdsInfoClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.widget.TopAdsFeedWidgetView;

/**
 * @author by nisie on 10/30/17.
 */

public class FeedTopadsViewHolder extends AbstractViewHolder<FeedTopAdsViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.feed_topads_layout;
    private final TopAdsFeedWidgetView topAdsWidgetView;

    public FeedTopadsViewHolder(View itemView,
                                TopAdsItemClickListener itemClickListener,
                                TopAdsInfoClickListener infoClickListener) {
        super(itemView);
        topAdsWidgetView = itemView.findViewById(R.id.topads_view);
        topAdsWidgetView.setItemClickListener(itemClickListener);
        topAdsWidgetView.setInfoClickListener(infoClickListener);
    }

    @Override
    public void bind(FeedTopAdsViewModel element) {
        topAdsWidgetView.setData(element.getList());
        topAdsWidgetView.setAdapterPosition(getAdapterPosition());
        topAdsWidgetView.notifyDataChange();
    }

    public void onViewRecycled() {
        topAdsWidgetView.onViewRecycled();
    }
}
