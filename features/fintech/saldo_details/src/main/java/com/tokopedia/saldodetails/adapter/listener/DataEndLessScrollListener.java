package com.tokopedia.saldodetails.adapter.listener;

import android.support.v7.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;

public abstract class DataEndLessScrollListener extends EndlessRecyclerViewScrollListener {

    private OnDataEndlessScrollListener onDataEndlessScrollListener;

    public interface OnDataEndlessScrollListener{
        int getEndlessDataSize();
    }

    protected DataEndLessScrollListener(RecyclerView.LayoutManager layoutManager,
                                        OnDataEndlessScrollListener onDataEndlessScrollListener) {
        super(layoutManager);
        this.onDataEndlessScrollListener = onDataEndlessScrollListener;
    }

    @Override
    protected boolean isDataEmpty() {
        return onDataEndlessScrollListener.getEndlessDataSize() == 0;
    }

}
