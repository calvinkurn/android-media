package com.tokopedia.common_tradein.viewmodel;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import android.view.View;


import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.common_tradein.R;


import com.tokopedia.common_tradein.model.ValidateTradeInResponse;
import com.tokopedia.common_tradein.customviews.TradeInTextView;

public class TradeInResponseObserver implements Observer<ValidateTradeInResponse> {

    private TradeInTextView tradeInTextView;

    public TradeInResponseObserver(TradeInTextView view) {
        tradeInTextView = view;
    }

    @Override
    public void onChanged(@Nullable ValidateTradeInResponse response) {
        if (response != null && response.isEligible()) {
            tradeInTextView.setVisibility(View.VISIBLE);
            if (response.getUsedPrice() > 0) {
                tradeInTextView.priceTextView.setText(String.format(tradeInTextView.getContext().getResources().getString(R.string.text_price_holder),
                        CurrencyFormatUtil.convertPriceValueToIdrFormat(response.getUsedPrice(), true)));
            } else if (response.getWidgetString() != null && !response.getWidgetString().isEmpty()) {
                tradeInTextView.priceTextView.setText(response.getWidgetString());
            } else {
                tradeInTextView.priceTextView.setText(tradeInTextView.getContext().getResources().getString(R.string.trade_in_exchange));
            }

        } else {
            tradeInTextView.setVisibility(View.GONE);
        }
    }
}