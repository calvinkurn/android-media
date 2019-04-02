package com.tokopedia.design.component.ticker;

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
import android.widget.FrameLayout;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.design.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

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

    private static final long DEFAULT_POST_DELAYED_VALUE = 500;

    private TouchViewPager tickerViewPager;
    private CirclePageIndicator tickerIndicator;
    private FrameLayout tickerHighlightView;
    private View container;
    private View imageViewActionClose;

    private int defaultHighLightColor;
    private int defaultBackgroundColor;
    private int defaultTextColor;
    private int defaultLinkColor;
    private int defaultPageIndicatorOnColor;
    private int defaultPageIndicatorOffColor;
    private float contentTextSize;
    private boolean isShowCloseButton;
    private boolean isUnderlinedLink;

    private ArrayList<Integer> listTextColor;
    private ArrayList<String> listMessage;

    private OnPartialTextClickListener onPartialTextClickListener;

    private TickerViewAdapter tickerAdapter;

    private int stateVisibility;
    private int tickerCurrentPosition;

    private Handler tickerHandler;
    private Runnable tickerRunnable;
    private int tickerHeight;

    public void setStateVisibility(int stateVisibility) {
        this.stateVisibility = stateVisibility;
    }

    public int getStateVisibility() {
        return stateVisibility;
    }

    public void clearMessage() {
        listMessage.clear();
        listTextColor.clear();
        tickerAdapter.notifyDataSetChanged();
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
            defaultLinkColor = styledAttributes.getColor(R.styleable.TickerView_tckv_link_color,
                    ContextCompat.getColor(getContext(), DEFAULT_COLOR_HIGHLIGHT_TICKER));
            isShowCloseButton = styledAttributes.getBoolean(R.styleable.TickerView_tckv_show_close_button, true);
            isUnderlinedLink = styledAttributes.getBoolean(R.styleable.TickerView_tckv_show_link_underline, true);
            contentTextSize = styledAttributes.getDimension(R.styleable.TickerView_tckv_content_text_size, getResources().getDimension(R.dimen.sp_14));
            tickerHeight = styledAttributes.getDimensionPixelSize(R.styleable.TickerView_tckv_content_height,
                    getResources().getDimensionPixelSize(R.dimen.dp_96));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            styledAttributes.recycle();
        }
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_ticker, this);
        tickerHighlightView = (FrameLayout) view.findViewById(R.id.parent_view);
        container = view.findViewById(R.id.container);
        tickerViewPager = (TouchViewPager) view.findViewById(R.id.view_pager_ticker);
        tickerIndicator = (CirclePageIndicator) view.findViewById(R.id.page_indicator_ticker);
        imageViewActionClose = view.findViewById(R.id.imageview_ticker_action_close);

        listTextColor = new ArrayList<>();
        listMessage = new ArrayList<>();
        tickerAdapter = new TickerViewAdapter(
                listTextColor,
                defaultBackgroundColor,
                defaultLinkColor,
                contentTextSize,
                listMessage,
                onPartialTextClickListener
        );
        tickerAdapter.setIsUnderlinedLink(isUnderlinedLink);

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

    private void updateTicker(){
        for (int i = 0; i < listMessage.size() - listTextColor.size(); ++i)
            listTextColor.add(defaultTextColor);

        this.setVisibility(listMessage.size() < 1? View.GONE : View.VISIBLE);

        if (listMessage.size() == 1) {
            tickerIndicator.setVisibility(GONE);

            stopAutoScrollTicker();
        } else {
            tickerIndicator.setVisibility(View.VISIBLE);

            startAutoScrollTicker();
        }
    }

    public void addMessage(String message){
        listMessage.add(message);
        updateTicker();
        tickerAdapter.notifyDataSetChanged();

    }

    public void addMessage(int pos, String message){
        listMessage.add(pos, message);
        updateTicker();
        tickerAdapter = new TickerViewAdapter(
                listTextColor,
                defaultBackgroundColor,
                defaultLinkColor,
                contentTextSize,
                listMessage,
                onPartialTextClickListener
        );
        tickerAdapter.setIsUnderlinedLink(isUnderlinedLink);
        tickerViewPager.setAdapter(tickerAdapter);
    }

    public void addAllMessage(List<String> messages){
        listMessage.addAll(messages);
        updateTicker();
        tickerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setStateVisibility(getVisibility());
        setContainerColor(defaultBackgroundColor);
        setHighLightColor(defaultHighLightColor);
        setBackGroundColor(defaultBackgroundColor);
        setTextColor(defaultTextColor);
        setPageIndicatorOnColor(defaultPageIndicatorOnColor);
        setPageIndicatorOffColor(defaultPageIndicatorOffColor);
        //setTickerHeight(tickerHeight);
        prepareView();
        invalidate();
        requestLayout();
    }

    private void setContainerColor(int backgroundColor) {
        GradientDrawable gradientDrawable;
        try {
            gradientDrawable = (GradientDrawable) container.getBackground();
            gradientDrawable.setColor(backgroundColor);

            container.setBackground(gradientDrawable);
        } catch (Exception e) {
            container.setBackgroundColor(
                    ContextCompat.getColor(getContext(), DEFAULT_COLOR_BACKGROUND_TICKER)
            );
        }
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
        imageViewActionClose.setVisibility(isShowCloseButton? VISIBLE : GONE);
    }

    public void setOnActionCloseListener(OnClickListener listener){
        imageViewActionClose.setOnClickListener(listener);
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
        tickerAdapter.setBackgroundColor(defaultBackgroundColor);
        tickerAdapter.setListener(onPartialTextClickListener);
        tickerAdapter.setDefaultLinkColor(defaultLinkColor);
        tickerAdapter.setIsUnderlinedLink(isUnderlinedLink);
        tickerAdapter.notifyDataSetChanged();

        tickerViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (tickerViewPager != null) {
                    tickerViewPager.setCurrentItem(0);
                    if(listMessage.size() > 0) {
                        setVisibility(VISIBLE);
                    }
                }
            }
        }, DEFAULT_POST_DELAYED_VALUE);
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

    public void setTickerHeight(int height) {
        this.tickerHeight = height;
        ViewGroup.LayoutParams layoutParams = tickerHighlightView.getLayoutParams();
        layoutParams.height = height;

        tickerHighlightView.setLayoutParams(layoutParams);
        tickerHighlightView.invalidate();
        tickerHighlightView.requestLayout();
    }

    public void setItemPadding(int top, int right, int bottom, int left) {
        tickerAdapter.setPadding(top, right, bottom, left);
    }

    public void setItemTextAppearance(int appearance) {
        tickerAdapter.setTextAppearance(appearance);
    }

    public void hideCloseButton() {
        imageViewActionClose.setVisibility(GONE);
    }

    public boolean contains(String message){
        return listMessage!= null && !listMessage.isEmpty() && listMessage.contains(message);
    }

    public int getCount(){
        return listMessage.size();
    }
}
