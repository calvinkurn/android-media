package com.tokopedia.abstraction.base.view.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;

/**
 * Created by User on 11/2/2017.
 */

public class LoadMoreRecyclerView extends VerticalRecyclerView {

    public LoadMoreRecyclerView(Context context) {
        super(context);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAdapter(final BaseListAdapter adapter) {
        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy <= 0) {
                    // no op when going up
                    return;
                }
                if (adapter.isLoading() || adapter.isEmpty() ) {
                    return;
                }
                int lastItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                int visibleItem = linearLayoutManager.getItemCount() - 1;
                if (lastItemPosition == visibleItem && adapter.hasNextPage()) {
                    adapter.showRetryFull(false);
                    adapter.showLoading(true);
                    adapter.loadNextPage();
                }
            }
        };
        addOnScrollListener(onScrollListener);

        super.setAdapter(adapter);
    }


}
