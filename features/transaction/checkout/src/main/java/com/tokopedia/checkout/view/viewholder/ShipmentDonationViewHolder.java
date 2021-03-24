package com.tokopedia.checkout.view.viewholder;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.ShipmentAdapterActionListener;
import com.tokopedia.checkout.view.uimodel.ShipmentDonationModel;
import com.tokopedia.iconunify.IconUnify;
import com.tokopedia.purchase_platform.common.feature.bottomsheet.GeneralBottomSheet;
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify;

import kotlin.Unit;

/**
 * @author Irfan Khoirul on 13/07/18.
 */

public class ShipmentDonationViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_DONATION = R.layout.item_donation;

    private CheckboxUnify cbDonation;
    private TextView tvDonationTitle;
    private IconUnify imgDonationInfo;
    private ViewGroup llContainer;

    private ShipmentAdapterActionListener shipmentAdapterActionListener;

    public ShipmentDonationViewHolder(View itemView, ShipmentAdapterActionListener shipmentAdapterActionListener) {
        super(itemView);

        this.shipmentAdapterActionListener = shipmentAdapterActionListener;

        cbDonation = itemView.findViewById(R.id.cb_donation);
        tvDonationTitle = itemView.findViewById(R.id.tv_donation_title);
        imgDonationInfo = itemView.findViewById(R.id.img_donation_info);
        llContainer = itemView.findViewById(R.id.ll_container);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void bindViewHolder(ShipmentDonationModel shipmentDonationModel) {
        llContainer.setOnClickListener(v -> cbDonation.setChecked(!cbDonation.isChecked()));
        cbDonation.setChecked(shipmentDonationModel.isChecked());
        cbDonation.skipAnimation();
        tvDonationTitle.setText(shipmentDonationModel.getDonation().getTitle());
        imgDonationInfo.setOnClickListener(v -> showBottomSheet(shipmentDonationModel));
        cbDonation.setOnCheckedChangeListener((buttonView, isChecked) -> shipmentAdapterActionListener.onDonationChecked(isChecked));
    }

    private void showBottomSheet(ShipmentDonationModel shipmentDonationModel) {
        GeneralBottomSheet generalBottomSheet = new GeneralBottomSheet();
        generalBottomSheet.setTitle(shipmentDonationModel.getDonation().getTitle());
        generalBottomSheet.setDesc(shipmentDonationModel.getDonation().getDescription());
        generalBottomSheet.setButtonText(llContainer.getContext().getString(com.tokopedia.purchase_platform.common.R.string.label_button_bottomsheet_close));
        generalBottomSheet.setIcon(R.drawable.checkout_module_ic_donation);
        generalBottomSheet.setButtonOnClickListener(bottomSheetUnify -> {
            bottomSheetUnify.dismiss();
            return Unit.INSTANCE;
        });
        generalBottomSheet.show(llContainer.getContext(), shipmentAdapterActionListener.getCurrentFragmentManager());
    }

}
