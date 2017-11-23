package com.tokopedia.design.ticker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.design.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

/**
 * Created by hangnadi on 8/15/17.
 */

@SuppressWarnings("unused")
public class TickerView extends BaseCustomView {

    @ColorRes
    public static final int DEFAULT_COLOR_HIGHLIGHT_TICKER = R.color.tkpd_main_green;

    @ColorRes
    public static final int DEFAULT_COLOR_BACKGROUND_TICKER = R.color.font_white_primary_70;

    @ColorRes
    public static final int DEFAULT_COLOR_TEXT_TICKER = R.color.font_black_primary_70;

    @ColorRes
    public static final int DEFAULT_COLOR_INDICATOR_ON = R.color.tkpd_main_green;

    @ColorRes
    public static final int DEFAULT_COLOR_INDICATOR_OFF = R.color.font_white_disabled_38;


    public static final float DEFAULT_CORNER_RADIUS = 4.0f;
    private static final long SLIDE_DELAY = 5000;
    private static final String SAVED = "instance state TickerView.class";
    private static final String SAVED_STATE_VISIBILITY = "saved_state_visibility";

    private ViewPager tickerViewPager;
    private CirclePageIndicator tickerIndicator;
    private RelativeLayout tickerHighlightView;
    private View imageViewActionClose;

    private int defaultHighLightColor;
    private int defaultBackgroundColor;
    private int defaultTextColor;
    private int defaultPageIndicatorOnColor;
    private int defaultPageIndicatorOffColor;

    private ArrayList<Integer> listBackGroundColor;
    private ArrayList<Integer> listTextColor;
    private ArrayList<String> listMessage;

    private OnPartialTextClickListener onPartialTextClickListener;

    private TickerViewAdapter tickerAdapter;

    private int stateVisibility;
    private int tickerCurrentPosition;

    private Handler tickerHandler;
    private Runnable tickerRunnable;

    public void setStateVisibility(int stateVisibility) {
        this.stateVisibility = stateVisibility;
    }

    public int getStateVisibility() {
        return stateVisibility;
    }

    @SuppressWarnings("WeakerAccess")
    public interface OnPartialTextClickListener {
        void onClick(View view, String messageClick);
    }

    public interface OnPageChangeListener {
        void onScrolled(int position, float positionOffset, int positionOffsetPixels);
        void onSelected(int position);
        void onScrollStateChanged(int state);
    }

    public TickerView(@NonNull Context context) {
        super(context);
        init();
    }

    public TickerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TickerView(@NonNull Context context,
                      @Nullable AttributeSet attrs,
                      @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.TickerView);
        try {
            defaultHighLightColor = styledAttributes.getColor(
                    R.styleable.TickerView_tckv_highlight_color,
                    ContextCompat.getColor(getContext(), DEFAULT_COLOR_HIGHLIGHT_TICKER)
            );
            defaultBackgroundColor = styledAttributes.getColor(
                    R.styleable.TickerView_tckv_background_color,
                    ContextCompat.getColor(getContext(), DEFAULT_COLOR_BACKGROUND_TICKER)
            );
            defaultTextColor = styledAttributes.getColor(
                    R.styleable.TickerView_tckv_text_color,
                    ContextCompat.getColor(getContext(), DEFAULT_COLOR_TEXT_TICKER)
            );
            defaultPageIndicatorOnColor = styledAttributes.getColor(
                    R.styleable.TickerView_tckv_indicator_on_color,
                    ContextCompat.getColor(getContext(), DEFAULT_COLOR_INDICATOR_ON)
            );
            defaultPageIndicatorOffColor = styledAttributes.getColor(
                    R.styleable.TickerView_tckv_indicator_off_color,
                    ContextCompat.getColor(getContext(), DEFAULT_COLOR_INDICATOR_OFF)
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            styledAttributes.recycle();
        }
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_ticker, this);
        tickerHighlightView = (RelativeLayout) view.findViewById(R.id.parent_view);
        tickerViewPager = (ViewPager) view.findViewById(R.id.view_pager_ticker);
        tickerIndicator = (CirclePageIndicator) view.findViewById(R.id.page_indicator_ticker);
        imageViewActionClose = view.findViewById(R.id.imageview_ticker_action_close);

        listTextColor = new ArrayList<>();
        listBackGroundColor = new ArrayList<>();
        listMessage = new ArrayList<>();
        tickerAdapter = new TickerViewAdapter(
                listTextColor,
                listBackGroundColor,
                listMessage,
                onPartialTextClickListener
        );

        tickerHandler = new Handler();
        tickerRunnable = new Runnable() {
            @Override
            public void run() {
                if (tickerViewPager != null) {
                    if (tickerCurrentPosition == tickerViewPager.getAdapter().getCount() - 1) {
                        tickerCurrentPosition = -1;
                    }
                    tickerViewPager.setCurrentItem(tickerCurrentPosition + 1, true);
                    tickerHandler.postDelayed(this, SLIDE_DELAY);
                }
            }
        };
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setStateVisibility(getVisibility());
        setHighLightColor(defaultHighLightColor);
        setBackGroundColor(defaultBackgroundColor);
        setTextColor(defaultTextColor);
        setPageIndicatorOnColor(defaultPageIndicatorOnColor);
        setPageIndicatorOffColor(defaultPageIndicatorOffColor);
        prepareView();
        invalidate();
        requestLayout();
    }

    private void prepareView() {
        tickerViewPager.setAdapter(tickerAdapter);
        setOnPageChangeListener(null);
        tickerIndicator.setFillColor(defaultPageIndicatorOnColor);
        tickerIndicator.setPageColor(defaultPageIndicatorOffColor);
        tickerIndicator.setViewPager(tickerViewPager);
        imageViewActionClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setStateVisibility(GONE);
                setVisibility(GONE);
            }
        });
    }

    public void setHighLightColor(int highLightColor) {
        GradientDrawable gradientDrawable;
        try {
            gradientDrawable = (GradientDrawable) tickerHighlightView.getBackground();
            gradientDrawable.setColor(highLightColor);

            tickerHighlightView.setBackground(gradientDrawable);
        } catch (Exception e) {
            tickerHighlightView.setBackgroundColor(
                    ContextCompat.getColor(getContext(), DEFAULT_COLOR_HIGHLIGHT_TICKER)
            );
        }
    }

    public void setPageIndicatorOnColor(int pageIndicatorOnColor) {
        this.defaultPageIndicatorOnColor = pageIndicatorOnColor;
    }

    public void setPageIndicatorOffColor(int pageIndicatorOffColor) {
        this.defaultPageIndicatorOffColor = pageIndicatorOffColor;
    }

    public void setBackGroundColor(int backgroundColor) {
        this.defaultBackgroundColor = backgroundColor;
    }

    public void setTextColor(int textColor) {
        this.defaultTextColor = textColor;
    }

    public void setListBackGroundColor(ArrayList<Integer> listBackGroundColor) {
        this.listBackGroundColor = listBackGroundColor;
    }

    public void setListTextColor(ArrayList<Integer> listTextColor) {
        this.listTextColor = listTextColor;
    }

    public void setOnPartialTextClickListener(OnPartialTextClickListener onPartialTextClickListener) {
        this.onPartialTextClickListener = onPartialTextClickListener;
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        tickerViewPager.addOnPageChangeListener(getOnPageChangeListener(onPageChangeListener));
    }

    public void setListMessage(ArrayList<String> listMessage) {
        this.listMessage = listMessage;
    }

    public void buildView() {
        if (listMessage.isEmpty()) {
            throw new RuntimeException(
                    "Undefined Message. Set your message by call setListMessage(...)"
            );
        }

        if (listBackGroundColor.isEmpty()) {
            int i = 0;
            while (i < listMessage.size()) {
                listBackGroundColor.add(defaultBackgroundColor);
                i++;
            }
        }

        if (listTextColor.isEmpty()) {
            int i = 0;
            while (i < listMessage.size()) {
                listTextColor.add(defaultTextColor);
                i++;
            }
        }

        if (listMessage.size() == 1) {
            tickerIndicator.setVisibility(GONE);

            stopAutoScrollTicker();
        } else {
            tickerIndicator.setVisibility(View.VISIBLE);

            startAutoScrollTicker();
        }

        tickerAdapter.setListMessage(listMessage);
        tickerAdapter.setListTextColor(listTextColor);
        tickerAdapter.setListBackGroundColor(listBackGroundColor);
        tickerAdapter.setListener(onPartialTextClickListener);
        tickerAdapter.notifyDataSetChanged();

        invalidate();
        requestLayout();
    }

    private void startAutoScrollTicker() {
        if (tickerHandler != null && tickerRunnable != null) {
            tickerHandler.postDelayed(tickerRunnable, SLIDE_DELAY);
        }
    }

    private void stopAutoScrollTicker() {
        if (tickerHandler != null && tickerRunnable != null) {
            tickerHandler.removeCallbacks(tickerRunnable);
        }
    }

    private ViewPager.OnPageChangeListener getOnPageChangeListener(final OnPageChangeListener onPageChangeListener) {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tickerCurrentPosition = position;
                if (onPageChangeListener != null) {
                    onPageChangeListener.onScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (onPageChangeListener != null) {
                    onPageChangeListener.onSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (onPageChangeListener != null) {
                    onPageChangeListener.onScrollStateChanged(state);
                }

                if (state == ViewPager.SCROLL_STATE_DRAGGING
                        && (tickerViewPager != null && tickerViewPager.isInTouchMode())) {
                    stopAutoScrollTicker();
                }
            }
        };
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SAVED, super.onSaveInstanceState());
        bundle.putInt(SAVED_STATE_VISIBILITY, getStateVisibility());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            setStateVisibility(bundle.getInt(SAVED_STATE_VISIBILITY));
            if (getStateVisibility() != GONE) {
                setVisibility(VISIBLE);
                startAutoScrollTicker();
            } else {
                setVisibility(GONE);
            }
            state = bundle.getParcelable(SAVED);
        }
        super.onRestoreInstanceState(state);
    }

}
