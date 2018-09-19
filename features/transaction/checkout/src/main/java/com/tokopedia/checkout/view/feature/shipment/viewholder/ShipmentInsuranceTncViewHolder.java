package com.tokopedia.checkout.view.feature.shipment.viewholder;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.feature.shipment.ShipmentAdapterActionListener;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentInsuranceTncModel;

/**
 * @author Irfan Khoirul on 09/05/18
 */

public class ShipmentInsuranceTncViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_INSURANCE_TNC = R.layout.item_insurance_tnc;

    private ShipmentAdapterActionListener shipmentAdapterActionListener;
    private ShipmentInsuranceTncModel shipmentInsuranceTncModel;

    private TextView tvInsuranceTnc;
    private LinearLayout llContainer;

    public ShipmentInsuranceTncViewHolder(View itemView, ShipmentAdapterActionListener actionListener) {
        super(itemView);
        this.shipmentAdapterActionListener = actionListener;

        tvInsuranceTnc = itemView.findViewById(R.id.tv_insurance_tnc);
        llContainer = itemView.findViewById(R.id.ll_container);
    }

    public void bindViewHolder(ShipmentInsuranceTncModel shipmentInsuranceTncModel) {
        this.shipmentInsuranceTncModel = shipmentInsuranceTncModel;
        if (shipmentInsuranceTncModel.isVisible()) {
            String formatText = tvInsuranceTnc.getContext().getString(R.string.text_tos_agreement);
            String messageTosAgreement = tvInsuranceTnc.getContext().getString(R.string.message_tos_agreement);
            int startSpan = messageTosAgreement.indexOf(formatText);
            int endSpan = messageTosAgreement.indexOf(formatText) + formatText.length();
            Spannable tosAgreementText = new SpannableString(messageTosAgreement);
            int color = ContextCompat.getColor(tvInsuranceTnc.getContext(), R.color.tkpd_main_green);
            tosAgreementText.setSpan(new ForegroundColorSpan(color), startSpan, endSpan,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tosAgreementText.setSpan(new TypefaceSpan("sans-serif-medium"), startSpan, endSpan,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tosAgreementText.setSpan(new ForegroundColorSpan(color), startSpan, endSpan,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvInsuranceTnc.setMovementMethod(LinkMovementMethod.getInstance());
            tvInsuranceTnc.setText(tosAgreementText);
            llContainer.setVisibility(View.VISIBLE);
            tvInsuranceTnc.setOnClickListener(view -> shipmentAdapterActionListener.onInsuranceTncClicked());
            llContainer.setOnClickListener(v -> shipmentAdapterActionListener.onInsuranceTncClicked());
        } else {
            llContainer.setVisibility(View.GONE);
        }
    }

    public boolean isShow() {
        return shipmentInsuranceTncModel != null && shipmentInsuranceTncModel.isVisible();
    }

}
