package com.tokopedia.abstraction.base.view.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.tokopedia.abstraction.base.view.adapter.BaseListV2Adapter;
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration;

/**
 * Created by User on 11/2/2017.
 */

public class BaseListRecyclerView extends RecyclerView {

    private LinearLayoutManager linearLayoutManager;

    public BaseListRecyclerView(Context context) {
        super(context);
        init();
    }

    public BaseListRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseListRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        this.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = getItemDecoration();
        if (itemDecoration != null) {
            this.addItemDecoration(itemDecoration);
        }
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(getContext());
    }

    public void setAdapter(final BaseListV2Adapter adapter) {
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
