package com.tokopedia.checkout.view.feature.shipment.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.feature.shipment.ShipmentAdapterActionListener;
import com.tokopedia.design.component.Tooltip;
import com.tokopedia.shipping_recommendation.domain.shipping.EgoldAttributeModel;

public class ShipmentEmasViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_DONATION = R.layout.checkout_holder_item_emas;

    private CheckBox buyEmas;
    private TextView tvDonationTitle;
    private ImageView imgDonationInfo;
    private LinearLayout llContainer;

    private ShipmentAdapterActionListener shipmentAdapterActionListener;

    public ShipmentEmasViewHolder(View itemView, ShipmentAdapterActionListener shipmentAdapterActionListener) {
        super(itemView);

        this.shipmentAdapterActionListener = shipmentAdapterActionListener;

        buyEmas = itemView.findViewById(R.id.cb_donation);
        tvDonationTitle = itemView.findViewById(R.id.tv_donation_title);
        imgDonationInfo = itemView.findViewById(R.id.img_donation_info);
        llContainer = itemView.findViewById(R.id.ll_container);
    }

    public void bindViewHolder(EgoldAttributeModel egoldAttributeModel) {
        llContainer.setOnClickListener(v -> buyEmas.setChecked(!buyEmas.isChecked()));
        buyEmas.setChecked(egoldAttributeModel.isChecked());
        tvDonationTitle.setText(egoldAttributeModel.getTitleText());
        imgDonationInfo.setOnClickListener(v -> showBottomSheet(egoldAttributeModel));

        buyEmas.setOnCheckedChangeListener((buttonView, isChecked) -> shipmentAdapterActionListener.onEgoldChecked(isChecked));
    }

    private void showBottomSheet(EgoldAttributeModel egoldAttributeModel) {
        Tooltip tooltip = new Tooltip(imgDonationInfo.getContext());
        tooltip.setTitle(egoldAttributeModel.getTooltipText());
        tooltip.setDesc(egoldAttributeModel.getTickerText());
        tooltip.setTextButton(imgDonationInfo.getContext().getString(R.string.label_button_bottomsheet_close));
        tooltip.setIcon(R.drawable.ic_donation);
        tooltip.getBtnAction().setOnClickListener(v -> tooltip.dismiss());
        tooltip.show();
    }

}
