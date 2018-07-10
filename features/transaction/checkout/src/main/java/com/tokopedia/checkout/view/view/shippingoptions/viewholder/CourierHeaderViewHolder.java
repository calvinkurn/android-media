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

    public static final int ITEM_VIEW_SHIPMENT_TYPE = R.layout.item_shipment_category;
    private static final int POSITION_HEADER_AFTER_TICKER = 1;

    private TextView tvShipmentType;
    private View vSeparator;

    public CourierHeaderViewHolder(View itemView) {
        super(itemView);

        tvShipmentType = itemView.findViewById(R.id.tv_shipment_type);
        vSeparator = itemView.findViewById(R.id.v_separator);
    }

    public void bindData(ShipmentTypeData shipmentTypeData) {
        tvShipmentType.setText(String.format("%s (%s)", shipmentTypeData.getShipmentType(), shipmentTypeData.getEtd()));
        if (getAdapterPosition() == POSITION_HEADER_AFTER_TICKER) {
            vSeparator.setVisibility(View.GONE);
        } else {
            vSeparator.setVisibility(View.VISIBLE);
        }
    }
}
