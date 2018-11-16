package com.tokopedia.home.account.presentation.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.component.ticker.TickerView;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.viewmodel.TickerViewModel;

/**
 * @author by nisie on 16/11/18.
 */
public class TickerViewHolder extends AbstractViewHolder<TickerViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_ticker;
    private final AccountItemListener listener;

    TickerView tickerView;

    public TickerViewHolder(View itemView, AccountItemListener listener) {
        super(itemView);
        this.listener = listener;
        tickerView = itemView.findViewById(R.id.ticker);
    }

    @Override
    public void bind(TickerViewModel element) {
        tickerView.addAllMessage(element.getListMessage());
        tickerView.setOnPartialTextClickListener((view, messageClick) -> listener.onTickerLinkClicked(
                messageClick
        ));
        tickerView.buildView();
    }
}
