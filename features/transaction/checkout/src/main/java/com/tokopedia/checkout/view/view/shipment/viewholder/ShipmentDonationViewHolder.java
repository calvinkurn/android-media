package com.tokopedia.checkout.view.view.shipment.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.view.shipment.ShipmentAdapterActionListener;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentDonationModel;
import com.tokopedia.design.bottomsheet.BottomSheetView;

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
                BottomSheetView bottomSheetView = new BottomSheetView(imgDonationInfo.getContext());
                bottomSheetView.renderBottomSheet(new BottomSheetView.BottomSheetField
                        .BottomSheetFieldBuilder()
                        .setTitle(shipmentDonationModel.getDonation().getTitle())
                        .setBody(shipmentDonationModel.getDonation().getDescription())
                        .setImg(R.drawable.ic_dropshipper)
                        .build());

                bottomSheetView.show();
            }
        });

        cbDonation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                shipmentAdapterActionListener.onDonationChecked(isChecked);
            }
        });
    }

}
