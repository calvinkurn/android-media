package com.tokopedia.core.customView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by hangnadi on 11/14/16.
 */
public abstract class EndLessScrollBehavior extends RecyclerView.OnScrollListener {

    private final LinearLayoutManager layoutManager;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private static final int visibleThreshold = 20;

    public EndLessScrollBehavior(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = layoutManager.getItemCount();

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if (isOnScrollDown(dy) && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            setOnLoadMore();
        }
    }

    /**
     *
     * @param dy The amount of vertical scroll
     * @return dy > 0
     */
    public boolean isOnScrollDown(int dy) {
        return dy > 0;
    }

    /**
     *
     * @param dy The amount of vertical scroll
     * @return dy < 0
     */
    public boolean isOnScrollUp(int dy) {
        return dy < 0;
    }

    protected abstract void setOnLoadMore();

}