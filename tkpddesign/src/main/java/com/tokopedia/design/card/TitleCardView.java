package com.tokopedia.design.card;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.loading.LoadingStateView;

/**
 * Created by hendry on 7/10/2017.
 */

public class TitleCardView extends CardView {
    public static final int VIEW_NOT_AVAILABLE = -1;

    @LayoutRes
    public static final int DEFAULT_LOADING_TITLE_LAYOUT_RES = R.layout.widget_title_card_loading;

    private LoadingStateView loadingStateView;
    private OnArrowDownClickListener onArrowDownClickListener;
    private TextView tvTitle;
    private float titleTextSize;
    private float titleHeight;
    private ImageView ivArrowDown;
    private ViewGroup vgTitle;

    private View loadingTitleView;

    private CharSequence titleString;
    @LayoutRes
    private int loadingLayoutRes;
    @LayoutRes
    private int errorLayoutRes;
    @LayoutRes
    private int emptyLayoutRes;
    private Drawable iconDrawable;
    private boolean useGradientTitleLoading;
    private ViewGroup vgTitleContent;

    public TitleCardView(Context context) {
        super(context);
        apply(null, 0);
        init();
    }

    public TitleCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        apply(attrs, 0);
        init();
    }

    public TitleCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        apply(attrs, defStyleAttr);
        init();
    }

    @SuppressWarnings("ResourceType")
    private void apply(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TitleCardView);
        titleString = a.getString(R.styleable.TitleCardView_tcv_title);
        titleTextSize = a.getDimensionPixelSize(R.styleable.TitleCardView_tcv_title_text_size, 0);
        loadingLayoutRes = a.getResourceId(R.styleable.TitleCardView_tcv_loading_layout, VIEW_NOT_AVAILABLE);
        errorLayoutRes = a.getResourceId(R.styleable.TitleCardView_tcv_error_layout, VIEW_NOT_AVAILABLE);
        emptyLayoutRes = a.getResourceId(R.styleable.TitleCardView_tcv_empty_layout, VIEW_NOT_AVAILABLE);
        iconDrawable = a.getDrawable(R.styleable.TitleCardView_tcv_icon);
        useGradientTitleLoading = a.getBoolean(R.styleable.TitleCardView_tcv_use_gradient_title, true);
        titleHeight = a.getDimensionPixelSize(R.styleable.TitleCardView_tcv_title_height, 0);
        a.recycle();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.widget_title_card, this);
        vgTitle = (ViewGroup) view.findViewById(R.id.vg_title);
        vgTitleContent = (ViewGroup) vgTitle.findViewById(R.id.vg_title_content);
        tvTitle = (TextView) vgTitleContent.findViewById(R.id.tv_title);
        if (titleHeight!= 0) {
            ViewGroup.LayoutParams layoutParams = vgTitleContent.getLayoutParams();
            layoutParams.height = (int) titleHeight;
        }
        if (titleTextSize != 0) {
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        }
        ImageView ivIcon = (ImageView) vgTitleContent.findViewById(R.id.iv_icon);
        if (iconDrawable == null) {
            ivIcon.setVisibility(View.GONE);
        } else {
            ivIcon.setImageDrawable(iconDrawable);
            ivIcon.setVisibility(View.VISIBLE);
        }
        ivArrowDown = (ImageView) vgTitleContent.findViewById(R.id.iv_arrow_down);

        loadingStateView = (LoadingStateView) view.findViewById(R.id.loading_state_view);
        if (loadingLayoutRes != VIEW_NOT_AVAILABLE) {
            loadingStateView.setLoadingViewRes(loadingLayoutRes);
        }
        if (errorLayoutRes != VIEW_NOT_AVAILABLE) {
            loadingStateView.setErrorViewRes(errorLayoutRes);
        }
        if (emptyLayoutRes != VIEW_NOT_AVAILABLE) {
            loadingStateView.setEmptyViewRes(emptyLayoutRes);
        }

        setTitle(titleString);
        ivArrowDown.setVisibility(View.GONE);

        this.setPreventCornerOverlap(false);

        setAddStatesFromChildren(true);
        loadingStateView.setAddStatesFromChildren(true);
    }

    public void setOnArrowDownClickListener(final OnArrowDownClickListener onArrowDownClickListener) {
        this.onArrowDownClickListener = onArrowDownClickListener;
        if (onArrowDownClickListener == null) {
            vgTitleContent.setOnClickListener(null);
            ivArrowDown.setOnClickListener(null);
        } else {
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onArrowDownClickListener.onArrowDownClicked();
                }
            };
            vgTitleContent.setOnClickListener(onClickListener);
            ivArrowDown.setOnClickListener(onClickListener);
        }
        checkArrowDownVisibility();
    }

    // there are 2 condition,
    // when the title is empty, or the listener is empty,
    // arrowDown will be hidden and title cannot be clicked
    private void checkArrowDownVisibility() {
        if (onArrowDownClickListener == null || TextUtils.isEmpty(tvTitle.getText())) {
            vgTitleContent.setClickable(false);
            ivArrowDown.setVisibility(View.GONE);
        } else {
            vgTitleContent.setClickable(true);
            ivArrowDown.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    @Override
    public void addView(View child, int index, final ViewGroup.LayoutParams params) {
        if (child.getId() == R.id.vg_title_card_view) {
            // Carry on adding the View...
            super.addView(child, index, params);
        } else {
            loadingStateView.addView(child, index, params);
        }
    }

    public void setLoadingViewRes(int loadingViewRes) {
        loadingStateView.setLoadingViewRes(loadingViewRes);
    }

    public View getLoadingView() {
        return loadingStateView.getLoadingView();
    }

    public View getContentView() {
        return loadingStateView.getContentView();
    }

    public void setLoadingView(View loadingView) {
        loadingStateView.setLoadingView(loadingView);
    }

    public void setEmptyViewRes(int emptyViewRes) {
        loadingStateView.setEmptyViewRes(emptyViewRes);
    }

    public View getEmptyView() {
        return loadingStateView.getEmptyView();
    }

    public void setEmptyView(View emptyView) {
        loadingStateView.setEmptyView(emptyView);
    }

    // will animate the title to gradient, showing that the title is loading
    private void setLoadingTitleState(boolean isLoading) {
        if (loadingTitleView == null) {
            setDefaultLoadingTitleView();
        }
        if (isLoading && this.loadingTitleView != null) {
            if (vgTitleContent != null) {
                vgTitleContent.setVisibility(View.GONE);
            }
            loadingTitleView.setVisibility(View.VISIBLE);
        } else {
            if (loadingTitleView != null) {
                loadingTitleView.setVisibility(View.GONE);
            }
            if (vgTitleContent != null) {
                vgTitleContent.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setViewState(int state) {
        loadingStateView.setViewState(state);
        switch (state) {
            case LoadingStateView.VIEW_LOADING:
                if (useGradientTitleLoading) {
                    setLoadingTitleState(true);
                }
                break;
            default:
                setLoadingTitleState(false);
        }
    }

    public void setDefaultLoadingTitleView() {
        this.loadingTitleView = LayoutInflater.from(getContext()).inflate(DEFAULT_LOADING_TITLE_LAYOUT_RES, vgTitle, false);
        loadingTitleView.setVisibility(View.GONE);
        vgTitle.addView(loadingTitleView);
    }

    public void setTitle(CharSequence title) {
        titleString = title;
        tvTitle.setText(title);
        checkArrowDownVisibility();
    }

    public interface OnArrowDownClickListener {
        void onArrowDownClicked();
    }
}