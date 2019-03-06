package com.tokopedia.loyalty.view.listener;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

public abstract class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    protected static final int STARTING_PAGE_INDEX = 0;
    protected int visibleThreshold = 2;
    protected int currentPage = 0;
    protected int currentItemCount = 0;
    protected boolean loading = true;
    protected boolean hasNextPage = true;
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;

    private RecyclerView.LayoutManager layoutManager;

    public RecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        resetState();
        this.layoutManager = layoutManager;
    }

    public RecyclerViewScrollListener(GridLayoutManager layoutManager) {
        resetState();
        this.layoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    public RecyclerViewScrollListener(StaggeredGridLayoutManager layoutManager) {
        resetState();
        this.layoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    public RecyclerViewScrollListener(RecyclerView.LayoutManager layoutManager) {
        resetState();
        this.layoutManager = layoutManager;
        if (layoutManager instanceof GridLayoutManager) {
            visibleThreshold = visibleThreshold * ((GridLayoutManager)layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager){
            visibleThreshold = visibleThreshold * ((StaggeredGridLayoutManager)layoutManager).getSpanCount();
        }
    }

    protected void init(){

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
        if (dy < 1)
        {
            // don't do anything when scrolling up.
            return;
        }
        // no need load more if data is empty
        if (isDataEmpty()) {
            return;
        }
        int totalItemCount = layoutManager.getItemCount();

        int lastVisibleItemPosition = 0;
        if (layoutManager instanceof StaggeredGridLayoutManager)
        {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) layoutManager)
                    .findLastVisibleItemPositions(null);

            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        }
        else if (layoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition
                    = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition =
                    ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount)
        {
            this.currentPage = STARTING_PAGE_INDEX;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0)
            {
                this.loading = true;
            }
        }
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount >= previousTotalItemCount))
        {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount)
        {
            loadMoreNextPage();
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState)
    {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE)
        {
            // supply a positive number to recyclerView.canScrollVertically(int direction) to check if scrolling down.
            boolean canScrollDownMore = recyclerView.canScrollVertically(1);
            // If recyclerView.canScrollVertically(1) returns false it means you're at the end of the list.
            if (!canScrollDownMore)
            {
                onScrolled(recyclerView, 0, 1);
            }
        }
    }

    protected boolean isDataEmpty(){
        int totalItemCount = layoutManager.getItemCount();
        return totalItemCount == 0;
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
