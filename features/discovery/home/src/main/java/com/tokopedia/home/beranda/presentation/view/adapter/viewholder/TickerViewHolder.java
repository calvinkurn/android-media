package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.utils.StripedUnderlineUtil;
import com.tokopedia.home.R;
import com.tokopedia.home.analytics.HomePageTracking;
import com.tokopedia.home.beranda.domain.model.Ticker;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.TickerViewModel;

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
    private String tickerTitle = "";

    public TickerViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        this.listener = listener;
        this.timer = new Timer();
        this.context = itemView.getContext();
        this.view = itemView;
        textMessage = itemView.findViewById(R.id.ticker_message);
        btnClose = itemView.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);
    }

    @Override
    public void bind(TickerViewModel element) {
        Ticker.Tickers ticker = element.getTickers().get(0);
        textMessage.setText(ticker.getMessage());
        textMessage.setMovementMethod(new TickerLinkMovementMethod(
                ticker.getTitle()
        ));
        StripedUnderlineUtil.stripUnderlines(textMessage);
        ViewCompat.setBackgroundTintList(btnClose, ColorStateList.valueOf(Color.parseColor(ticker.getColor())));
        if (!hasStarted)
            timer.scheduleAtFixedRate(new SwitchTicker(element.getTickers()), 0, SLIDE_DELAY);
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
                    tickerTitle = ticker.getTitle();
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
                    tickerTitle
            );
            listener.onCloseTicker(getAdapterPosition());
        }
    }

    private class TickerLinkMovementMethod extends LinkMovementMethod {
        String tickerTitle;

        public TickerLinkMovementMethod(String tickerTitle) {
            this.tickerTitle = tickerTitle;
        }

        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            HomePageTracking.eventClickTickerHomePage(
                    context,
                    tickerTitle
            );
            return super.onTouchEvent(widget, buffer, event);
        }
    }
}
