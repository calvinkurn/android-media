package com.tokopedia.checkout.view.view.shippingrecommendation.shippingcourier.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;

/**
 * Created by Irfan Khoirul on 06/08/18.
 */

public class ShippingCourierViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_SHIPMENT_COURIER = R.layout.item_courier;

    private TextView tvCourier;
    private TextView tvPrice;
    private ImageView imgCheck;
    private View vSeparator;

    private int cartPosition;

    public ShippingCourierViewHolder(View itemView, int cartPosition) {
        super(itemView);
        this.cartPosition = cartPosition;

        tvCourier = itemView.findViewById(R.id.tv_courier);
        tvPrice = itemView.findViewById(R.id.tv_price);
        imgCheck = itemView.findViewById(R.id.img_check);
        vSeparator = itemView.findViewById(R.id.v_separator);
    }

    public void bindData(ShippingCourierViewModel shippingCourierViewModel,
                         ShippingCourierAdapterListener shippingCourierAdapterListener) {
        tvCourier.setText(shippingCourierViewModel.getProductData().getShipperName());
        if (shippingCourierViewModel.getProductData().getError() != null &&
                shippingCourierViewModel.getProductData().getError().getErrorMessage().length() > 0) {
            tvPrice.setText(shippingCourierViewModel.getProductData().getError().getErrorMessage());
        } else {
            tvPrice.setText(shippingCourierViewModel.getProductData().getPrice().getFormattedPrice());
        }
        imgCheck.setVisibility(shippingCourierViewModel.isSelected() ? View.VISIBLE : View.GONE);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shippingCourierViewModel.setSelected(!shippingCourierViewModel.isSelected());
                shippingCourierAdapterListener.onCourierChoosen(shippingCourierViewModel, cartPosition);
            }
        });
    }
}
