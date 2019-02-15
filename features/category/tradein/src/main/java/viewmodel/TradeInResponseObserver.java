package viewmodel;

import android.arch.lifecycle.Observer;
import android.view.View;

import com.example.tradein.R;

import javax.annotation.Nullable;

import model.ValidateTradeInResponse;
import view.customview.TradeInTextView;

public class TradeInResponseObserver implements Observer<ValidateTradeInResponse> {

    private TradeInTextView tradeInTextView;

    public TradeInResponseObserver(TradeInTextView view) {
        tradeInTextView = view;
    }

    @Override
    public void onChanged(@Nullable ValidateTradeInResponse response) {
//        if (response != null && !response.isEligible()) {
//            if (response.isDiagnosed()) {
//                tradeInTextView.priceTextView.setText(String.format(tradeInTextView.getContext().getResources().getString(R.string.text_price_holder), response.getUsedPrice()));
//            } else {
//                tradeInTextView.priceTextView.setVisibility(View.GONE);
//            }
//
//        } else {
//            tradeInTextView.setVisibility(View.GONE);
//        }
    }
}