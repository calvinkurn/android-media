package com.tokopedia.checkout.view.view.shipment.viewholder;

import android.content.Context;
import android.view.View;

import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.view.view.shipment.ShipmentAdapterActionListener;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentItem;
import com.tokopedia.showcase.ShowCaseObject;

import java.util.ArrayList;

public class ShipmentItemMultipleAddressViewHolder extends ShipmentItemViewHolder {

    private Context mContext;
    private ShipmentAdapterActionListener mActionListener;


    public ShipmentItemMultipleAddressViewHolder(View itemView, Context context,
                                                 ShipmentAdapterActionListener actionListener) {
        super(itemView);
        mContext = context;
        mActionListener = actionListener;
    }

    public void bindViewHolder(ShipmentItem shipmentSingleAddressItem,
                               RecipientAddressModel recipientAddressModel,
                               ArrayList<ShowCaseObject> showCaseObjectList){
        // TODO : BIND HERE
        tvAddressName.setText("");
        tvRecipientName.setText("");
        tvRecipientAddress.setText("");
        tvRecipientPhone.setText("");
        tvChangeAddress.setVisibility(View.GONE);
    }

}
