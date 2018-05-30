package com.tokopedia.checkout.view.view.shippingoptions.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.view.shippingoptions.viewmodel.ShipmentTypeData;

/**
 * @author Irfan Khoirul on 04/05/18.
 */

public class CourierHeaderViewHolder extends RecyclerView.ViewHolder {

    private TextView tvShipmentType;

    public CourierHeaderViewHolder(View itemView) {
        super(itemView);

        tvShipmentType = itemView.findViewById(R.id.tv_shipment_type);
    }

    public void bindData(ShipmentTypeData shipmentTypeData) {
        tvShipmentType.setText(shipmentTypeData.getShipmentType());
    }
}
