package com.tokopedia.home.beranda.helper;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;

public abstract class HomeFeedEndlessScrollListener extends EndlessRecyclerViewScrollListener {
    protected HomeFeedEndlessScrollListener(RecyclerView.LayoutManager layoutManager) {
        super(layoutManager);
        resetState();
        if (layoutManager instanceof StaggeredGridLayoutManager){
            visibleThreshold = visibleThreshold * ((StaggeredGridLayoutManager)layoutManager).getSpanCount();
            visibleThreshold-=4;
        }
    }
}
