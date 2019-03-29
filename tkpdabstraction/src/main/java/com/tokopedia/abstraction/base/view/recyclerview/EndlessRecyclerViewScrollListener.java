package com.tokopedia.abstraction.base.view.recyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.tokopedia.abstraction.base.view.listener.EndlessLayoutManagerListener;

/**
 * @author by erry on 02/02/17.
 */
public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    protected static final int STARTING_PAGE_INDEX = 0;
    protected int visibleThreshold = 2;
    protected int currentPage = 0;
    protected int currentItemCount = 0;
    protected boolean loading = false;
    protected boolean hasNextPage = true;
    private EndlessLayoutManagerListener endlessLayoutManagerListener;

    private RecyclerView.LayoutManager layoutManager;

    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        resetState();
        this.layoutManager = layoutManager;
    }

    public EndlessRecyclerViewScrollListener(GridLayoutManager layoutManager) {
        resetState();
        this.layoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    public EndlessRecyclerViewScrollListener(StaggeredGridLayoutManager layoutManager) {
        resetState();
        this.layoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    public EndlessRecyclerViewScrollListener(RecyclerView.LayoutManager layoutManager) {
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

    public void setEndlessLayoutManagerListener(EndlessLayoutManagerListener endlessLayoutManagerListener) {
        this.endlessLayoutManagerListener = endlessLayoutManagerListener;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return (endlessLayoutManagerListener != null
                && endlessLayoutManagerListener.getCurrentLayoutManager() != null) ?
                endlessLayoutManagerListener.getCurrentLayoutManager() : layoutManager;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    protected int getLastVisibleItem(int[] lastVisibleItemPositions) {
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

        checkLoadMore(view, dx, dy);

    }

    protected void checkLoadMore(RecyclerView view, int dx, int dy) {

        // assume load more when going down or going right
        if (dy <= 0 && dx <= 0) {
            return;
        }
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

    protected boolean isDataEmpty(){
        int totalItemCount = getLayoutManager().getItemCount();
        return totalItemCount == 0;
    }

    public void loadMoreNextPage(){
        int totalItemCount = getLayoutManager().getItemCount();
        loading = true;
        onLoadMore(currentPage + 1, totalItemCount);
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
        int totalItemCount = getLayoutManager().getItemCount();
        if (totalItemCount > currentItemCount) {
            currentItemCount = totalItemCount;
            currentPage++;
        }
    }

    public abstract void onLoadMore(int page, int totalItemsCount);
}