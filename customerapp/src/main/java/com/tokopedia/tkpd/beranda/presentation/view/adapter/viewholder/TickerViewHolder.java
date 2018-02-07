package com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.network.entity.home.Ticker;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.beranda.listener.HomeCategoryListener;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.TickerViewModel;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class TickerViewHolder extends AbstractViewHolder<TickerViewModel> {

    private static final String TAG = TickerViewHolder.class.getSimpleName();
    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ticker;
    @BindView(R.id.ticker_message)
    TextView textMessage;
    @BindView(R.id.btn_close)
    RelativeLayout btnClose;

    private HomeCategoryListener listener;
    private Timer timer;
    private Context context;
    private static final long SLIDE_DELAY = 5000;
    private boolean hasStarted = false;

    public TickerViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;
        this.timer = new Timer();
        this.context = itemView.getContext();
    }

    @Override
    public void bind(TickerViewModel element) {
        Ticker.Tickers ticker = element.getTickers().get(0);
        textMessage.setText(ticker.getMessage());
        textMessage.setMovementMethod(LinkMovementMethod.getInstance());
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
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hasStarted = true;
                    if (i < tickers.size() - 1)
                        i++;
                    else
                        i = 0;
                    Ticker.Tickers ticker = tickers.get(i);
                    textMessage.setText(ticker.getMessage());
                    ViewCompat.setBackgroundTintList(btnClose, ColorStateList.valueOf(Color.parseColor(ticker.getColor())));
                }
            });
        }
    }

    @OnClick(R.id.btn_close)
    void closeTicker() {
        listener.onCloseTicker(getAdapterPosition());
    }
}
