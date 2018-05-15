package com.tokopedia.checkout.view.view.shipment.viewholder;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.view.shipment.ShipmentAdapterActionListener;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentInsuranceTncModel;

/**
 * @author Aghny A. Putra on 09/05/18
 */

public class ShipmentInsuranceTncViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_INSURANCE_TNC = R.layout.item_insurance_tnc;

    private ShipmentAdapterActionListener shipmentAdapterActionListener;

    private TextView tvInsuranceTnc;
    private CardView cardViewContainer;

    public ShipmentInsuranceTncViewHolder(View itemView, ShipmentAdapterActionListener actionListener) {
        super(itemView);
        this.shipmentAdapterActionListener = actionListener;

        tvInsuranceTnc = itemView.findViewById(R.id.tv_insurance_tnc);
        cardViewContainer = itemView.findViewById(R.id.card_view_container);
    }

    public void bindViewHolder(ShipmentInsuranceTncModel shipmentInsuranceTncModel) {
        if (shipmentInsuranceTncModel.isVisible()) {
            String formatText = tvInsuranceTnc.getContext().getString(R.string.text_tos_agreement);
            String messageTosAgreement = tvInsuranceTnc.getContext().getString(R.string.message_tos_agreement);
            int startSpan = messageTosAgreement.indexOf(formatText);
            int endSpan = messageTosAgreement.indexOf(formatText) + formatText.length();
            Spannable tosAgreementText = new SpannableString(messageTosAgreement);
            int color = ContextCompat.getColor(tvInsuranceTnc.getContext(), R.color.tkpd_main_green);
            tosAgreementText.setSpan(new ForegroundColorSpan(color), startSpan, endSpan,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tosAgreementText.setSpan(new StyleSpan(Typeface.BOLD), startSpan, endSpan,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tosAgreementText.setSpan(new ForegroundColorSpan(color), startSpan, endSpan,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvInsuranceTnc.setMovementMethod(LinkMovementMethod.getInstance());
            tvInsuranceTnc.setText(tosAgreementText);
            cardViewContainer.setVisibility(View.VISIBLE);
            tvInsuranceTnc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shipmentAdapterActionListener.onInsuranceTncClicked();
                }
            });
        } else {
            cardViewContainer.setVisibility(View.GONE);
        }
    }

}
