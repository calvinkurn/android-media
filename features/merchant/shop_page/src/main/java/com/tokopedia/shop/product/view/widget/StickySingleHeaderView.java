package com.tokopedia.shop.product.view.widget;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
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
    private StaggeredGridLayoutManager layoutManagerStag;

    private int stickyPosition = 0;
    private RecyclerView.OnScrollListener onScrollListener;
    private boolean isEnable = true;
    private boolean refreshSticky;

    public StickySingleHeaderView(Context context) {
        super(context);
    }

    public StickySingleHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickySingleHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getContainerHeight() {
        mHeaderContainer.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        return mHeaderContainer.getMeasuredHeight();
    }

    public interface OnStickySingleHeaderAdapter {
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
        setClipToPadding(false);
        View view = getChildAt(0);
        if (!(view instanceof RecyclerView))
            throw new RuntimeException("RecyclerView should be the first child view.");
        mRecyclerView = (RecyclerView) view;
        mHeaderContainer = new FrameLayout(getContext());
        mHeaderContainer.setBackgroundColor(Color.WHITE);
        mHeaderContainer.setClipToPadding(false);
        mHeaderContainer.setLayoutParams(
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mHeaderContainer.setPadding(mRecyclerView.getPaddingLeft(), 0, mRecyclerView.getPaddingRight(), 0);
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
                    if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                        layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    } else if (mRecyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                        layoutManagerStag = (StaggeredGridLayoutManager) mRecyclerView.getLayoutManager();
                    }
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
        if (mHeaderHeight == -1 || adapter == null || (layoutManager == null && layoutManagerStag == null))
            return;
        int firstCompletelyVisiblePosition = -1;
        if (layoutManager != null) {
            firstCompletelyVisiblePosition = layoutManager.findFirstVisibleItemPosition();
        } else {
            firstCompletelyVisiblePosition = layoutManagerStag.findFirstVisibleItemPositions(null)[0];
        }
        if (firstCompletelyVisiblePosition > -1) {
            if (firstCompletelyVisiblePosition > (stickyPosition - 1)) {
                // make the etalase label always visible
                if (!isStickyShowed() || refreshSticky) {
                    showSticky();
                    mHeaderContainer.setVisibility(View.VISIBLE);
                    refreshSticky = false;
                }
            } else {
                // make the etalase label always gone
                if (isStickyShowed() || refreshSticky) {
                    clearHeaderView();
                    mHeaderContainer.setVisibility(View.GONE);
                    refreshSticky = false;
                }
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

    public boolean isStickyShowed() {
        return mHeaderContainer.getChildCount() > 0;
    }

    private void showSticky() {
        clearHeaderView();
        RecyclerView.ViewHolder viewHolder = adapter.createStickyViewHolder(mHeaderContainer);
        mHeaderContainer.addView(viewHolder.itemView);
        mHeaderHeight = mHeaderContainer.getHeight();
        adapter.bindSticky(viewHolder);
    }

    @Override
    public void refreshSticky() {
        if (isEnable) {
            refreshSticky = true;
            onScrolled(mRecyclerView, 0, 0);
        }
    }

    // Remove the Header View
    public void clearHeaderView() {
        mHeaderContainer.removeAllViews();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        initView();
        super.onLayout(changed, left, top, right, bottom);
    }

}
