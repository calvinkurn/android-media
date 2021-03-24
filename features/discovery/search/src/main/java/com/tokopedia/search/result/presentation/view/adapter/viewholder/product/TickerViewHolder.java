package com.tokopedia.search.result.presentation.view.adapter.viewholder.product;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.LayoutRes;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.model.TickerDataView;
import com.tokopedia.search.result.presentation.view.listener.TickerListener;
import com.tokopedia.unifycomponents.ticker.Ticker;
import com.tokopedia.unifycomponents.ticker.TickerCallback;

import org.jetbrains.annotations.NotNull;

public class TickerViewHolder extends AbstractViewHolder<TickerDataView> {

    @LayoutRes
    public static final int LAYOUT = R.layout.search_result_product_ticker_layout;
    private Ticker tickerView;
    private TickerListener tickerListener;

    public TickerViewHolder(View itemView,
                            TickerListener tickerListener) {
        super(itemView);
        this.tickerListener = tickerListener;
        tickerView = itemView.findViewById(R.id.tickerView);
    }

    @Override
    public void bind(final TickerDataView element) {
        bindTickerView(element);
    }

    private void bindTickerView(TickerDataView element) {
        if (tickerListener == null || tickerListener.isTickerHasDismissed()
                || TextUtils.isEmpty(element.getText())) {
            itemView.setVisibility(View.GONE);
            return;
        }

        tickerView.setHtmlDescription(element.getText());
        tickerView.setDescriptionClickEvent(new TickerCallback() {
            @Override
            public void onDescriptionViewClick(@NotNull CharSequence charSequence) {
                if (tickerListener != null && !TextUtils.isEmpty(element.getQuery())) {
                    tickerListener.onTickerClicked(element);
                }
            }

            @Override
            public void onDismiss() {
                itemView.setVisibility(View.GONE);
                if (tickerListener != null) {
                    tickerListener.onTickerDismissed();
                }
            }
        });
        itemView.setVisibility(View.VISIBLE);
    }
}
