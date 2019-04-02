package com.tokopedia.checkout.view.feature.shippingoptions.viewholder;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.feature.shippingoptions.viewmodel.ShipmentTickerInfoData;

/**
 * @author Irfan Khoirul on 25/05/18.
 */

public class CourierTickerViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_SHIPMENT_TICKER = R.layout.item_courier_ticker;

    private static final String BOLD_TEXT = "pk 14:00";

    private TextView tvTickerInfo;

    public CourierTickerViewHolder(View itemView) {
        super(itemView);
        tvTickerInfo = itemView.findViewById(R.id.tv_ticker_info);
    }

    public void bindData(ShipmentTickerInfoData shipmentTickerInfoData) {
        tvTickerInfo.setText(shipmentTickerInfoData.getTickerInfo());

        int startSpan = tvTickerInfo.getText().toString().indexOf(BOLD_TEXT);
        int endSpan = tvTickerInfo.getText().toString().indexOf(BOLD_TEXT) + BOLD_TEXT.length();

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(tvTickerInfo.getText().toString());
        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), startSpan, endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTickerInfo.setText(spannableStringBuilder);
    }

}
