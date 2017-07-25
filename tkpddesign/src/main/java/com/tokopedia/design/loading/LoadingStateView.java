package com.tokopedia.design.loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tokopedia.design.R;

/**
 * Created by hendry on 7/10/2017.
 */

public class LoadingStateView extends FrameLayout {

    public static final int VIEW_LOADING = 0;
    public static final int VIEW_EMPTY = 1;
    public static final int VIEW_CONTENT = 2;

    @LayoutRes
    public static final int DEFAULT_LOADING_LAYOUT_RES = R.layout.widget_line_chart_container_loading;

    FrameLayout frameLayout;

    private View emptyView;
    private View loadingView;
    private View contentView;

    private int loadingLayoutRes;
    private int emptyLayoutRes;

    public LoadingStateView(Context context) {
        super(context);
        apply(null, 0);
        init();
    }

    public LoadingStateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        apply(attrs, 0);
        init();
    }

    public LoadingStateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        apply(attrs, defStyleAttr);
        init();
    }

    @SuppressWarnings("ResourceType")
    private void apply(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LoadingStateView);
        loadingLayoutRes = a.getResourceId(R.styleable.LoadingStateView_lsv_loading_layout, DEFAULT_LOADING_LAYOUT_RES);
        emptyLayoutRes = a.getResourceId(R.styleable.LoadingStateView_lsv_empty_layout, 0);
        a.recycle();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_loading_state_view, this);
        frameLayout = (FrameLayout) view.findViewById(R.id.frame_content);

        setAddStatesFromChildren(true);
        frameLayout.setAddStatesFromChildren(true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        invalidate();
        requestLayout();
    }

    @Override
    public void addView(View child, int index, final ViewGroup.LayoutParams params) {
        if (child.getId() == R.id.vg_widget_loading_state) {
            // Carry on adding the View...
            super.addView(child, index, params);
        } else {
            // remove the link between child and previous parent before add (if any)
            if (child.getParent() != null) {
                ViewGroup viewParent = (ViewGroup) child.getParent();
                viewParent.removeView(child);
            }

            // check if it is content, add to the first index, so loading and empty will be on front of content view
            if (contentView == null && child != loadingView && child != emptyView) {
                contentView = child;
                frameLayout.addView(child, 0, params);
            } else {
                frameLayout.addView(child, params);
            }
        }
    }

    public void setLoadingViewRes(int loadingViewRes) {
        View emptyView = LayoutInflater.from(getContext()).inflate(loadingViewRes, frameLayout, false);
        setLoadingView(emptyView);
    }

    public View getLoadingView() {
        return loadingView;
    }

    public View getContentView() {
        return contentView;
    }

    public void setLoadingView(View loadingView) {
        if (this.loadingView != null) {
            frameLayout.removeView(this.loadingView);
        }
        this.loadingView = loadingView;
        loadingView.setVisibility(View.GONE);
        addView(loadingView);
    }

    public void setEmptyViewRes(int emptyViewRes) {
        View emptyView = LayoutInflater.from(getContext()).inflate(emptyViewRes, frameLayout, false);
        setEmptyView(emptyView);
    }

    public View getEmptyView() {
        return emptyView;
    }

    public void setEmptyView(View emptyView) {
        if (this.emptyView != null) {
            frameLayout.removeView(this.emptyView);
        }
        this.emptyView = emptyView;
        emptyView.setVisibility(View.GONE);
        addView(emptyView);
    }

    // showing that the content to loading state
    private void setLoadingState(boolean isLoading) {
        if (loadingView == null) {
            setDefaultLoadingView();
        }
        if (isLoading && this.loadingView != null) {
            if (contentView != null) {
                contentView.setVisibility(View.GONE);
            }
            if (emptyView != null) {
                emptyView.setVisibility(View.GONE);
            }
            loadingView.setVisibility(View.VISIBLE);
        } else {
            setContentVisible();
        }
    }

    private void setEmptyState(boolean isEmpty) {
        if (emptyView == null) {
            setDefaultEmptyView();
        }
        if (isEmpty && emptyView != null) {
            if (contentView != null) {
                contentView.setVisibility(View.GONE);
            }
            if (this.loadingView != null) {
                loadingView.setVisibility(View.GONE);
            }
            emptyView.setVisibility(View.VISIBLE);
        } else {
            setContentVisible();
        }
    }

    private void setContentVisible() {
        if (this.loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
        if (emptyView != null) {
            emptyView.setVisibility(View.GONE);
        }
        contentView.setVisibility(View.VISIBLE);
    }

    private void setDefaultLoadingView() {
        if (loadingLayoutRes == 0) {
            return;
        }
        this.loadingView = LayoutInflater.from(getContext()).inflate(loadingLayoutRes, frameLayout, false);
        loadingView.setVisibility(View.GONE);
        addView(loadingView);
    }

    private void setDefaultEmptyView() {
        if (emptyLayoutRes == 0) {
            return;
        }
        this.emptyView = LayoutInflater.from(getContext()).inflate(emptyLayoutRes, frameLayout, false);
        emptyView.setVisibility(View.GONE);
        addView(emptyView);
    }

    public void setViewState(int state) {
        if (state == VIEW_LOADING) {
            setLoadingState(true);
        } else if (state == VIEW_EMPTY) {
            setEmptyState(true);
        } else {
            setContentVisible();
        }
    }
}
