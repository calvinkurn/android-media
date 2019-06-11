package com.tokopedia.checkout.view.feature.shipment.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.feature.shipment.ShipmentAdapterActionListener;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentDonationModel;
import com.tokopedia.design.component.Tooltip;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;

/**
 * @author Irfan Khoirul on 13/07/18.
 */

public class ShipmentDonationViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_DONATION = R.layout.item_donation;

    private CheckBox cbDonation;
    private TextView tvDonationTitle;
    private ImageView imgDonationInfo;
    private LinearLayout llContainer;

    private ShipmentAdapterActionListener shipmentAdapterActionListener;

    public ShipmentDonationViewHolder(View itemView, ShipmentAdapterActionListener shipmentAdapterActionListener) {
        super(itemView);

        this.shipmentAdapterActionListener = shipmentAdapterActionListener;

        cbDonation = itemView.findViewById(R.id.cb_donation);
        tvDonationTitle = itemView.findViewById(R.id.tv_donation_title);
        imgDonationInfo = itemView.findViewById(R.id.img_donation_info);
        llContainer = itemView.findViewById(R.id.ll_container);
    }

    public void bindViewHolder(ShipmentDonationModel shipmentDonationModel) {
        llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbDonation.setChecked(!cbDonation.isChecked());
            }
        });
        cbDonation.setChecked(shipmentDonationModel.isChecked());
        tvDonationTitle.setText(shipmentDonationModel.getDonation().getTitle());
        imgDonationInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(shipmentDonationModel);
            }
        });

        cbDonation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                shipmentAdapterActionListener.onDonationChecked(isChecked);
            }
        });
    }

    private void showBottomSheet(ShipmentDonationModel shipmentDonationModel) {
        Tooltip tooltip = new Tooltip(imgDonationInfo.getContext());
        tooltip.setTitle(shipmentDonationModel.getDonation().getTitle());
        tooltip.setDesc(shipmentDonationModel.getDonation().getDescription());
        tooltip.setTextButton(imgDonationInfo.getContext().getString(R.string.label_button_bottomsheet_close));
        tooltip.setIcon(R.drawable.ic_donation);
        tooltip.getBtnAction().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tooltip.dismiss();
            }
        });
        tooltip.show();
    }

}
