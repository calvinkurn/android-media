package com.tokopedia.feedplus.view.util;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

/**
 * Created by milhamj on 18/01/18.
 */


public abstract class EndlessScrollRecycleListener extends RecyclerView.OnScrollListener {
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    private boolean loading = true;
    public int visibleThreshold = 3;
    private int startingPageIndex = 0;
    private int currentPage = 0;

    public void setVisibleThreshold(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
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
        RecyclerView.LayoutManager layoutManager = view.getLayoutManager();
        int lastVisibleItemPosition = 0;
        int totalItemCount = layoutManager.getItemCount();

        if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions
                    = ((StaggeredGridLayoutManager) layoutManager)
                    .findLastVisibleItemPositions(null);

            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        } else if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            lastVisibleItemPosition
                    = gridLayoutManager.findLastVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            lastVisibleItemPosition
                    = linearLayoutManager.findLastVisibleItemPosition();
        }
        Log.d("Endless Scroll Listener","start scroll");
        Log.d("Endless Scroll Listener","start func 1");
        Log.d("init","totalItemCount :" + totalItemCount );
        Log.d("init","previousTotalItemCount :" + previousTotalItemCount );
        Log.d("init","loading :" + loading );
        if (totalItemCount < previousTotalItemCount) {
            Log.d("Endless Scroll Listener","process func 1 - init");
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }
        Log.d("func 1","totalItemCount :" + totalItemCount );
        Log.d("func 1","previousTotalItemCount :" + previousTotalItemCount );
        Log.d("func 1","loading :" + loading );
        Log.d("Endless Scroll Listener","end func 1");
        Log.d("Endless Scroll Listener","start func 2");
        if (loading && (totalItemCount > previousTotalItemCount)) {
            Log.d("Endless Scroll Listener","process func 2 - reset to loadmore");
            loading = false;
            previousTotalItemCount = totalItemCount;
        }
        Log.d("func 2","totalItemCount :" + totalItemCount );
        Log.d("func 2","previousTotalItemCount :" + previousTotalItemCount );
        Log.d("func 2","loading :" + loading );
        Log.d("Endless Scroll Listener","end func 2");
        Log.d("Endless Scroll Listener","start func 3");
        if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            Log.d("Endless Scroll Listener","process func 3 - do loadmore");
            currentPage++;
            onLoadMore(currentPage, totalItemCount);
            loading = true;
        }
        Log.d("func 3","totalItemCount :" + totalItemCount );
        Log.d("func 3","previousTotalItemCount :" + previousTotalItemCount );
        Log.d("func 3","loading :" + loading );
        Log.d("Endless Scroll Listener","end func 3");
        Log.d("Endless Scroll Listener","end scroll");

        onScroll(lastVisibleItemPosition);
    }

    public void resetState() {
        this.currentPage = this.startingPageIndex;
        this.previousTotalItemCount = 0;
        this.loading = true;
    }

    public abstract void onLoadMore(int page, int totalItemsCount);

    public abstract void onScroll(int lastVisiblePosition);
}
