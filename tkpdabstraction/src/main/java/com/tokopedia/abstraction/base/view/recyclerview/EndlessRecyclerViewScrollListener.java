package com.tokopedia.abstraction.base.view.recyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * @author by erry on 02/02/17.
 */
public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private static final int STARTING_PAGE_INDEX = 0;
    private int visibleThreshold = 2;
    private int currentPage = 0;
    private int currentItemCount = 0;
    private boolean loading = false;
    private boolean hasNextPage = true;

    private RecyclerView.LayoutManager layoutManager;

    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public EndlessRecyclerViewScrollListener(GridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    public EndlessRecyclerViewScrollListener(StaggeredGridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    public EndlessRecyclerViewScrollListener(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        if (layoutManager instanceof GridLayoutManager) {
            visibleThreshold = visibleThreshold * ((GridLayoutManager)layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager){
            visibleThreshold = visibleThreshold * ((StaggeredGridLayoutManager)layoutManager).getSpanCount();
        }
    }

    public int getCurrentPage() {
        return currentPage;
    }

    private int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        super.onScrolled(view, dx, dy);
        // assume load more when going down
        if (dy <= 0) {
            return;
        }
        if (loading) {
            return;
        }
        // no need load more if data is empty
        int totalItemCount = layoutManager.getItemCount();
        if (totalItemCount == 0) {
            return;
        }

        int lastVisibleItemPosition = 0;
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions
                    = ((StaggeredGridLayoutManager) layoutManager)
                    .findLastVisibleItemPositions(null);

            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        } else if (layoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition
                    = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition
                    = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }

        if ((lastVisibleItemPosition + visibleThreshold) > totalItemCount &&
                hasNextPage) {
            loadMoreNextPage();
        }
    }

    public void loadMoreNextPage(){
        int totalItemCount = layoutManager.getItemCount();
        onLoadMore(currentPage + 1, totalItemCount);
        loading = true;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public void resetState() {
        this.currentPage = STARTING_PAGE_INDEX;
        this.currentItemCount = 0;
        this.loading = false;
        this.hasNextPage = true;
    }

    public void updateStateAfterGetData() {
        loading = false;
        int totalItemCount = layoutManager.getItemCount();
        if (totalItemCount > currentItemCount) {
            currentItemCount = totalItemCount;
            currentPage++;
        }
    }

    public abstract void onLoadMore(int page, int totalItemsCount);
}