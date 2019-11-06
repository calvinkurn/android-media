package com.tokopedia.common_tradein.viewmodel;

import androidx.lifecycle.Observer;
import android.view.View;


import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.common_tradein.R;

import android.support.annotation.Nullable;

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
            } else {
                tradeInTextView.priceTextView.setText(tradeInTextView.getContext().getResources().getString(R.string.trade_in_exchange));
            }

        } else {
            tradeInTextView.setVisibility(View.GONE);
        }
    }
}