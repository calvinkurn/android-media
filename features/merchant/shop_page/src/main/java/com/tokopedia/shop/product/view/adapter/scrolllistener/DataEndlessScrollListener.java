package com.tokopedia.shop.product.view.adapter.scrolllistener;

import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;

/**
 * Created by hendry on 16/07/18.
 */

public abstract class DataEndlessScrollListener extends EndlessRecyclerViewScrollListener {

    private OnDataEndlessScrollListener onDataEndlessScrollListener;

    public interface OnDataEndlessScrollListener{
        int getEndlessDataSize();
    }

    public DataEndlessScrollListener(RecyclerView.LayoutManager layoutManager,
                                     OnDataEndlessScrollListener onDataEndlessScrollListener) {
        super(layoutManager);
        this.onDataEndlessScrollListener = onDataEndlessScrollListener;
    }

    @Override
    protected boolean isDataEmpty() {
        return onDataEndlessScrollListener.getEndlessDataSize() == 0;
    }

}
