package com.tokopedia.design.loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tokopedia.design.R;

/**
 * Created by hendry on 7/10/2017.
 */

public class LoadingStateView extends FrameLayout {

    public static final int VIEW_NOT_AVAILABLE = -1;

    public static final int VIEW_LOADING = 0;
    public static final int VIEW_ERROR = 1;
    public static final int VIEW_EMPTY = 2;
    public static final int VIEW_CONTENT = 3;

    @LayoutRes
    public static final int DEFAULT_LOADING_LAYOUT_RES = R.layout.widget_container_loading;

    private FrameLayout frameLayout;

    private View loadingView;
    private View errorView;
    private View emptyView;
    private View contentView;

    @LayoutRes
    private int loadingLayoutRes;
    @LayoutRes
    private int errorLayoutRes;
    @LayoutRes
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
        errorLayoutRes = a.getResourceId(R.styleable.LoadingStateView_lsv_error_layout, VIEW_NOT_AVAILABLE);
        emptyLayoutRes = a.getResourceId(R.styleable.LoadingStateView_lsv_empty_layout, VIEW_NOT_AVAILABLE);
        a.recycle();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_loading_state_view, this);
        frameLayout = (FrameLayout) view.findViewById(R.id.frame_content);
        setFocusableInTouchMode(true);
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
            if (contentView == null && child != loadingView && child != emptyView && child != errorView) {
                contentView = child;
                frameLayout.addView(child, 0, params);
            } else {
                frameLayout.addView(child, params);
            }
        }
    }


    public View getLoadingView() {
        return loadingView;
    }

    public void setLoadingViewRes(int loadingViewRes) {
        View emptyView = LayoutInflater.from(getContext()).inflate(loadingViewRes, frameLayout, false);
        setLoadingView(emptyView);
    }

    public void setLoadingView(View view) {
        if (loadingView != null) {
            frameLayout.removeView(loadingView);
        }
        loadingView = view;
        view.setVisibility(View.GONE);
        addView(view);
    }

    public void setErrorViewRes(@LayoutRes int layoutRes) {
        View view = LayoutInflater.from(getContext()).inflate(layoutRes, frameLayout, false);
        setErrorView(view);
    }

    public void setErrorView(View view) {
        if (errorView != null) {
            frameLayout.removeView(errorView);
        }
        errorView = view;
        view.setVisibility(View.GONE);
        addView(view);
    }

    public View getEmptyView() {
        return emptyView;
    }

    public View getErrorView() {
        return errorView;
    }

    public void setEmptyViewRes(@LayoutRes int layoutRes) {
        View emptyView = LayoutInflater.from(getContext()).inflate(layoutRes, frameLayout, false);
        setEmptyView(emptyView);
    }

    public void setEmptyView(View view) {
        if (emptyView != null) {
            frameLayout.removeView(emptyView);
        }
        emptyView = view;
        view.setVisibility(View.GONE);
        addView(view);
    }

    public View getContentView() {
        return contentView;
    }

    public void setViewState(int state) {
        switch (state) {
            case VIEW_LOADING:
                loadingView = getView(loadingView, loadingLayoutRes);
                setViewVisible(loadingView);
                break;
            case VIEW_ERROR:
                errorView = getView(errorView, errorLayoutRes);
                setViewVisible(errorView);
                break;
            case VIEW_EMPTY:
                emptyView = getView(emptyView, emptyLayoutRes);
                setViewVisible(emptyView);
                break;
            default:
                setViewVisible(contentView);
        }
    }

    private View getView(View view, @LayoutRes int layoutRes) {
        if (view != null) {
            return view;
        }
        if (layoutRes == VIEW_NOT_AVAILABLE) {
            return null;
        }
        View viewTemp = LayoutInflater.from(getContext()).inflate(layoutRes, frameLayout, false);
        viewTemp.setVisibility(View.GONE);
        ViewGroup.LayoutParams layoutParams = viewTemp.getLayoutParams();
        if (layoutParams instanceof FrameLayout.LayoutParams) {
            ((LayoutParams) layoutParams).gravity = Gravity.CENTER;
        }
        addView(viewTemp, layoutParams);
        return viewTemp;
    }

    private void setViewVisible(View view) {
        if (view == null) {
            return;
        }
        int childSize = frameLayout.getChildCount();
        for (int i = 0; i < childSize; i++) {
            View childView = frameLayout.getChildAt(i);
            if (childView == view) {
                childView.setVisibility(View.VISIBLE);
            } else {
                childView.setVisibility(View.GONE);
            }
        }
    }

}