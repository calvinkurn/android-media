package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import androidx.annotation.LayoutRes;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.utils.StripedUnderlineUtil;
import com.tokopedia.home.R;
import com.tokopedia.home.analytics.HomePageTracking;
import com.tokopedia.home.beranda.domain.model.Ticker;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.TickerViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class TickerViewHolder extends AbstractViewHolder<TickerViewModel> implements View.OnClickListener {

    private static final String TAG = TickerViewHolder.class.getSimpleName();
    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ticker_home;
    private TextView textMessage;
    private View btnClose;

    private HomeCategoryListener listener;
    private Timer timer;
    private Context context;
    private static final long SLIDE_DELAY = 5000;
    private boolean hasStarted = false;

    private View view;
    private String tickerId = "";
    private TimerTask tickerTimerTask;
    private int tickerContainerMaxLines = 0;
    private View tickerContainer;

    public TickerViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        this.listener = listener;
        this.timer = new Timer();
        this.context = itemView.getContext();
        this.view = itemView;
        this.tickerContainer = itemView.findViewById(R.id.ticker_frame_layout);
        textMessage = itemView.findViewById(R.id.ticker_message);
        btnClose = itemView.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);
    }

    @Override
    public void bind(TickerViewModel element) {
        if (tickerContainerMaxLines == 0) {
            tickerContainerMaxLines = calculateMaxLinesTicker(element);
            textMessage.setLines(tickerContainerMaxLines);
        }

        if (element.getTickers() != null && element.getTickers().size()>0) {
            Ticker.Tickers ticker = element.getTickers().get(0);
            textMessage.setText(ticker.getMessage());
            textMessage.setMovementMethod(createMovementMethod(context, ticker.getId()));

            StripedUnderlineUtil.stripUnderlines(textMessage);
            ViewCompat.setBackgroundTintList(btnClose, ColorStateList.valueOf(Color.parseColor(ticker.getColor())));
            if (!hasStarted && element.getTickers().size()>1) {
                if (tickerTimerTask != null) tickerTimerTask.cancel();
                tickerTimerTask = new SwitchTicker(element.getTickers());
                timer.scheduleAtFixedRate(tickerTimerTask, 0, SLIDE_DELAY);
            }
        }
    }

    private int calculateMaxLinesTicker(TickerViewModel element) {
        if (element.getTickers() == null) return 0;
        tickerContainer.measure(
                View.MeasureSpec.makeMeasureSpec(listener.getWindowWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.UNSPECIFIED);
        String maxString = "";
        for (Ticker.Tickers tickerData: element.getTickers()){
            if (tickerData.getMessage().toString().length()>maxString.length()) {
                maxString = tickerData.getMessage().toString();
            }
        }

        final Rect bounds = new Rect();
        final Paint paint = new Paint();
        paint.setTextSize(textMessage.getTextSize());
        paint.getTextBounds(maxString, 0, maxString.length(), bounds);

        return (int) Math.ceil((float) bounds.width() / textMessage.getMeasuredWidth());
    }

    @Override
    public void onViewRecycled() {
        super.onViewRecycled();
        if (tickerTimerTask != null) tickerTimerTask.cancel();
        hasStarted = false;
    }

    private class SwitchTicker extends TimerTask {
        private ArrayList<Ticker.Tickers> tickers;
        private int i = 0;

        public SwitchTicker(ArrayList<Ticker.Tickers> tickers) {
            this.tickers = tickers;
        }

        @Override
        public void run() {
            view.post(new Runnable() {
                @Override
                public void run() {
                    hasStarted = true;
                    if (i < tickers.size() - 1)
                        i++;
                    else
                        i = 0;
                    Ticker.Tickers ticker = tickers.get(i);
                    tickerId = ticker.getId();
                    textMessage.setText(ticker.getMessage());
                    StripedUnderlineUtil.stripUnderlines(textMessage);
                    ViewCompat.setBackgroundTintList(btnClose, ColorStateList.valueOf(Color.parseColor(ticker.getColor())));
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_close && getAdapterPosition() != RecyclerView.NO_POSITION) {
            HomePageTracking.eventClickOnCloseTickerHomePage(
                    context,
                    tickerId
            );
            listener.onCloseTicker();
        }
    }

    private MovementMethod createMovementMethod (Context context, String tickerId) {
        final GestureDetector detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp ( MotionEvent e ) {
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed ( MotionEvent e ) {
                return true;
            }
        });
        return new ScrollingMovementMethod() {
            @Override
            public boolean onTouchEvent (@NotNull TextView widget, @NotNull Spannable buffer, @NotNull MotionEvent event ) {
                HomePageTracking.eventClickTickerHomePage(
                        context,
                        tickerId
                );
                // check if event is a single tab
                boolean isClickEvent = detector.onTouchEvent(event);

                // detect span that was clicked
                if (isClickEvent) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    x -= widget.getTotalPaddingLeft();
                    y -= widget.getTotalPaddingTop();

                    x += widget.getScrollX();
                    y += widget.getScrollY();

                    Layout layout = widget.getLayout();
                    int line = layout.getLineForVertical(y);
                    int off = layout.getOffsetForHorizontal(line, x);

                    ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

                    if (link.length != 0) {
                        // execute click only for first clickable span
                        // can be a for each loop to execute every one
                        link[0].onClick(widget);
                        return true;
                    }
                }

                // let scroll movement handle the touch
                return super.onTouchEvent(widget, buffer, event);
            }
        };
    }
}
