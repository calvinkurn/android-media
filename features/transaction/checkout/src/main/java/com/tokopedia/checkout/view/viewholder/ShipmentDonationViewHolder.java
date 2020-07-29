package com.tokopedia.checkout.view.viewholder;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.ShipmentAdapterActionListener;
import com.tokopedia.checkout.view.uimodel.ShipmentDonationModel;
import com.tokopedia.design.component.Tooltip;

/**
 * @author Irfan Khoirul on 13/07/18.
 */

public class ShipmentDonationViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_DONATION = R.layout.item_donation;

    private CheckBox cbDonation;
    private TextView tvDonationTitle;
    private LinearLayout llContainer;

    private ShipmentAdapterActionListener shipmentAdapterActionListener;

    public ShipmentDonationViewHolder(View itemView, ShipmentAdapterActionListener shipmentAdapterActionListener) {
        super(itemView);

        this.shipmentAdapterActionListener = shipmentAdapterActionListener;

        cbDonation = itemView.findViewById(R.id.cb_donation);
        tvDonationTitle = itemView.findViewById(R.id.tv_donation_title);
        llContainer = itemView.findViewById(R.id.ll_container);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void bindViewHolder(ShipmentDonationModel shipmentDonationModel) {
        llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbDonation.setChecked(!cbDonation.isChecked());
            }
        });
        cbDonation.setChecked(shipmentDonationModel.isChecked());
        tvDonationTitle.setText(shipmentDonationModel.getDonation().getTitle());
        tvDonationTitle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int[] textLocation = new int[2];
                    tvDonationTitle.getLocationOnScreen(textLocation);
                    if (event.getRawX() >= textLocation[0] + tvDonationTitle.getWidth() - tvDonationTitle.getTotalPaddingRight()){
                        showBottomSheet(shipmentDonationModel);
                        return true;
                    }
                }
                return true;
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
        Tooltip tooltip = new Tooltip(llContainer.getContext());
        tooltip.setTitle(shipmentDonationModel.getDonation().getTitle());
        tooltip.setDesc(shipmentDonationModel.getDonation().getDescription());
        tooltip.setTextButton(llContainer.getContext().getString(com.tokopedia.purchase_platform.common.R.string.label_button_bottomsheet_close));
        tooltip.setIcon(R.drawable.checkout_module_ic_donation);
        tooltip.getBtnAction().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tooltip.dismiss();
            }
        });
        tooltip.show();
    }

}
