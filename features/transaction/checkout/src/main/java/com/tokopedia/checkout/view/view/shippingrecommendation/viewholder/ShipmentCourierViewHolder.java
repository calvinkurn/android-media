package com.tokopedia.checkout.view.view.shippingrecommendation.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.checkout.R;

/**
 * Created by Irfan Khoirul on 06/08/18.
 */

public class ShipmentCourierViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_SHIPMENT_COURIER = R.layout.item_courier;

    private TextView tvCourier;
    private TextView tvPrice;
    private ImageView imgCheck;
    private View vSeparator;

    public ShipmentCourierViewHolder(View itemView) {
        super(itemView);

        tvCourier = itemView.findViewById(R.id.tv_courier);
        tvPrice = itemView.findViewById(R.id.tv_price);
        imgCheck = itemView.findViewById(R.id.img_check);
        vSeparator = itemView.findViewById(R.id.v_separator);
    }

    public void bindData() {

    }
}
