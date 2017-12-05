package com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder;

import android.content.res.ColorStateList;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.network.entity.home.Ticker;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.beranda.listener.HomeCategoryListener;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.BannerViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.TickerViewModel;

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
    ImageView btnClose;
    private HomeCategoryListener listener;

    public TickerViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;
    }

    @Override
    public void bind(TickerViewModel element) {
        Ticker.Tickers ticker = element.getTickers().get(0);
        textMessage.setText(ticker.getMessage());
    }

    @OnClick(R.id.btn_close)
    void closeTicker(){
        listener.onCloseTicker(getAdapterPosition());
    }
}
