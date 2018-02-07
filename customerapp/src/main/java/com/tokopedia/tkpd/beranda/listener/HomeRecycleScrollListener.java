package com.tokopedia.tkpd.beranda.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by errysuprayogi on 12/4/17.
 */

public class HomeRecycleScrollListener extends RecyclerView.OnScrollListener {

    private static final String TAG = HomeRecycleScrollListener.class.getSimpleName();
    private final LinearLayoutManager layoutManager;
    private final OnSectionChangeListener sectionChangeListener;
    private int firstVisibleItemPosition = 0;
    private boolean isDragged = false;

    public HomeRecycleScrollListener(LinearLayoutManager layoutManager, OnSectionChangeListener sectionChangeListener) {
        this.layoutManager = layoutManager;
        this.sectionChangeListener = sectionChangeListener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (layoutManager.findFirstVisibleItemPosition() != firstVisibleItemPosition && isDragged) {
            firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            sectionChangeListener.onChange(firstVisibleItemPosition);
        }

    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        sectionChangeListener.onScrollStateChanged(newState, layoutManager.findFirstVisibleItemPosition());
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            isDragged = true;
        } else {
            isDragged = false;
        }
    }

}
