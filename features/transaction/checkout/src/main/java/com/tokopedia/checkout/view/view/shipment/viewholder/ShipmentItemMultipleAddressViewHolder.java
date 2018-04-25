package com.tokopedia.checkout.view.view.shipment.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.checkout.view.view.shipment.ShipmentAdapterActionListener;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentMultipleAddressItem;
import com.tokopedia.design.pickuppoint.PickupPointLayout;

public class ShipmentItemMultipleAddressViewHolder extends ShipmentItemViewHolder {

    private Context mContext;
    private ShipmentAdapterActionListener mActionListener;


    public ShipmentItemMultipleAddressViewHolder(View itemView, Context context,
                                                 ShipmentAdapterActionListener actionListener) {
        super(itemView);
        mContext = context;
        mActionListener = actionListener;
    }

    public void bindViewHolder(ShipmentMultipleAddressItem shipmentMultipleAddressItem){
        // TODO : BIND HERE
        tvAddressName.setText("");
        tvRecipientName.setText("");
        tvRecipientAddress.setText("");
        tvRecipientPhone.setText("");
        tvChangeAddress.setVisibility(View.GONE);
    }

}
