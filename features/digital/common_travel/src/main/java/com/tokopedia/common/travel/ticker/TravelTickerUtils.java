package com.tokopedia.common.travel.ticker;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerViewModel;
import com.tokopedia.design.component.ticker.TickerView;

import java.util.ArrayList;

/**
 * @author by furqan on 25/02/19
 */
public class TravelTickerUtils {
    private static final int DEFAULT_POST_DELAYED_VALUE = 500;

    public static void buildTravelTicker(Context context, TravelTickerViewModel travelTickerViewModel, TickerView tickerView) {
        ArrayList<String> messages = new ArrayList<>();
        messages.add(travelTickerViewModel.getMessage());
        tickerView.setVisibility(View.INVISIBLE);
        tickerView.setListMessage(messages);
        if (travelTickerViewModel.getType() == 1) {
            tickerView.setHighLightColor(ContextCompat.getColor(context, R.color.tkpd_main_green));
            tickerView.setPageIndicatorOnColor(ContextCompat.getColor(context, R.color.light_green));
            tickerView.setPageIndicatorOffColor(ContextCompat.getColor(context, R.color.light_green));
            tickerView.setBackGroundColor(ContextCompat.getColor(context, R.color.light_green));
        } else if (travelTickerViewModel.getType() == 2) {
            tickerView.setHighLightColor(ContextCompat.getColor(context, R.color.snackbar_border_error));
            tickerView.setPageIndicatorOnColor(ContextCompat.getColor(context, R.color.colorPink));
            tickerView.setPageIndicatorOffColor(ContextCompat.getColor(context, R.color.colorPink));
            tickerView.setBackGroundColor(ContextCompat.getColor(context, R.color.colorPink));
        }

        tickerView.buildView();

        tickerView.postDelayed(() -> {
            tickerView.setItemTextAppearance(R.style.TextView_Micro);
            tickerView.setVisibility(View.VISIBLE);
        }, DEFAULT_POST_DELAYED_VALUE);
    }
}
