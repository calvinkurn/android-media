package com.tokopedia.shop.product.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by tlh on 2017/1/21 :)
 * https://github.com/TellH/RecyclerStickyHeaderView
 */

public class StickySingleHeaderView extends FrameLayout
        implements OnStickySingleHeaderListener {
    private boolean hasInit = false;
    private FrameLayout mHeaderContainer;
    private RecyclerView mRecyclerView;
    private int mHeaderHeight = -1;
    private OnStickySingleHeaderAdapter adapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView.ViewHolder mViewHolderCache;

    private int stickyPosition = 0;
    private RecyclerView.OnScrollListener onScrollListener;
    private boolean isEnable = true;

    public StickySingleHeaderView(Context context) {
        super(context);
    }

    public StickySingleHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickySingleHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface OnStickySingleHeaderAdapter{
        int getStickyHeaderPosition();
        RecyclerView.ViewHolder createStickyViewHolder(ViewGroup parent);
        void bindSticky(RecyclerView.ViewHolder viewHolder);
        void setListener(OnStickySingleHeaderListener onStickySingleHeaderViewListener);
    }

    private void initView() {
        if (hasInit) {
            return;
        }
        hasInit = true;
        View view = getChildAt(0);
        if (!(view instanceof RecyclerView))
            throw new RuntimeException("RecyclerView should be the first child view.");
        mRecyclerView = (RecyclerView) view;
        mHeaderContainer = new FrameLayout(getContext());
        mHeaderContainer.setBackgroundColor(Color.WHITE);
        mHeaderContainer.setLayoutParams(
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(mHeaderContainer);
        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mHeaderHeight == -1 || adapter == null || layoutManager == null) {
                    mHeaderHeight = mHeaderContainer.getHeight();
                    RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
                    if (!(adapter instanceof OnStickySingleHeaderAdapter))
                        throw new RuntimeException
                                ("Your RecyclerView.Adapter should be the type of StickyHeaderViewAdapter.");
                    StickySingleHeaderView.this.adapter = (OnStickySingleHeaderAdapter) adapter;
                    StickySingleHeaderView.this.adapter.setListener(StickySingleHeaderView.this);
                    stickyPosition = StickySingleHeaderView.this.adapter.getStickyHeaderPosition();
                    layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                StickySingleHeaderView.this.onScrolled(recyclerView, dx, dy);
            }
        };
        mRecyclerView.addOnScrollListener(onScrollListener);
    }

    private void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (mHeaderHeight == -1 || adapter == null || layoutManager == null)
            return;

        int firstCompletelyVisiblePosition = layoutManager.findFirstVisibleItemPosition();
        if (firstCompletelyVisiblePosition > -1) {
            if (firstCompletelyVisiblePosition > (stickyPosition - 1)) {
                // make the etalase label always visible
                Log.i("TESTABCD", "show etalase label");
                if (!isStickyShowed()) {
                    showSticky();
                    mHeaderContainer.setVisibility(View.VISIBLE);
                }
            } else {
                // make the etalase label always gone
                if (isStickyShowed()) {
                    clearHeaderView();
                    mHeaderContainer.setVisibility(View.GONE);
                }
                Log.i("TESTABCD", "hide etalase label");
            }
        }
    }

    public void disable(){
        if (isEnable) {
            isEnable = false;
            clearHeaderView();
            mHeaderContainer.setVisibility(View.GONE);
            mRecyclerView.removeOnScrollListener(onScrollListener);
        }
    }

    public void enable(){
        if (!isEnable) {
            isEnable = true;
            mRecyclerView.addOnScrollListener(onScrollListener);
            refreshSticky();
        }
    }

    private boolean isStickyShowed() {
        return mHeaderContainer.getChildCount() > 0;
    }

    private void showSticky() {
        clearHeaderView();
        RecyclerView.ViewHolder viewHolder = mViewHolderCache;
        if (viewHolder == null) {
            viewHolder = adapter.createStickyViewHolder(mHeaderContainer);
            mViewHolderCache = viewHolder;
        }
        mHeaderContainer.addView(viewHolder.itemView);
        mHeaderHeight = mHeaderContainer.getHeight();
        adapter.bindSticky(viewHolder);
    }

    @Override
    public void refreshSticky() {
        if (isEnable) {
            onScrolled(mRecyclerView, 0, 0);
        }
    }

    // Remove the Header View
    private void clearHeaderView() {
        mHeaderContainer.removeAllViews();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        initView();
        super.onLayout(changed, left, top, right, bottom);
    }

}
