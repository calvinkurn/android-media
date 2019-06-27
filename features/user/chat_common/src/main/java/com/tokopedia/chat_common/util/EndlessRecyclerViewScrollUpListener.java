package com.tokopedia.chat_common.util;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;

/**
 * @author by erry on 02/02/17.
 */
public abstract class EndlessRecyclerViewScrollUpListener extends EndlessRecyclerViewScrollListener {

    public EndlessRecyclerViewScrollUpListener(LinearLayoutManager layoutManager) {
        super(layoutManager);
    }

    public EndlessRecyclerViewScrollUpListener(GridLayoutManager layoutManager) {
        super(layoutManager);

    }

    public EndlessRecyclerViewScrollUpListener(StaggeredGridLayoutManager layoutManager) {
        super(layoutManager);

    }

    public EndlessRecyclerViewScrollUpListener(RecyclerView.LayoutManager layoutManager) {
        super(layoutManager);

    }

    @Override
    protected void checkLoadMore(RecyclerView view, int dx, int dy) {

        if (loading) {
            return;
        }

        // no need load more if data is empty
        if (isDataEmpty()) {
            return;
        }
        int totalItemCount = getLayoutManager().getItemCount();

        int lastVisibleItemPosition = 0;
        if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions
                    = ((StaggeredGridLayoutManager) getLayoutManager())
                    .findLastVisibleItemPositions(null);

            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        } else if (getLayoutManager() instanceof GridLayoutManager) {
            lastVisibleItemPosition
                    = ((GridLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        } else if (getLayoutManager() instanceof LinearLayoutManager) {
            lastVisibleItemPosition
                    = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        }

        if ((lastVisibleItemPosition + visibleThreshold) > totalItemCount &&
                hasNextPage) {
            loadMoreNextPage();
        }
    }
}