package com.tokopedia.checkout.view.feature.shipment.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.feature.shipment.ShipmentAdapterActionListener;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentNotifierModel;
import com.tokopedia.checkout.view.feature.webview.CheckoutWebViewActivity;

/**
 * Created by fajarnuha on 06/12/18.
 */
public class ShipmentNotifierViewHolder extends RecyclerView.ViewHolder {

    public static final int TYPE_VIEW_NOTIFIER_COD = R.layout.viewholder_general_notif_ticker;
    private TextView mText, mLinkText;
    private ShipmentAdapterActionListener mActionListener;

    public ShipmentNotifierViewHolder(View itemView, ShipmentAdapterActionListener actionListener) {
        super(itemView);
        mActionListener = actionListener;
        mText = itemView.findViewById(R.id.text_cod_description);
        mLinkText = itemView.findViewById(R.id.text_learn_more);
    }

    public void bind(ShipmentNotifierModel model) {
        mText.setText(Html.fromHtml(model.getMessage()));
        mLinkText.setOnClickListener(view -> mActionListener.onNotifierClicked(model.getUrl()));
    }

}
