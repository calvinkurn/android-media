package com.tokopedia.checkout.view.feature.shippingrecommendation.shippingcourier.view;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.checkout.R;

/**
 * Created by Irfan Khoirul on 06/08/18.
 */

public class ShippingCourierViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_SHIPMENT_COURIER = R.layout.item_courier;

    private TextView tvCourier;
    private TextView tvPrice;
    private ImageView imgCheck;
    private View vSeparator;
    private TextView tvPromoPotency;

    private int cartPosition;
    // set true if has courier promo, whether own courier or other courier
    private boolean hasCourierPromo;

    public ShippingCourierViewHolder(View itemView, int cartPosition, boolean hasCourierPromo) {
        super(itemView);
        this.cartPosition = cartPosition;
        this.hasCourierPromo = hasCourierPromo;

        tvCourier = itemView.findViewById(R.id.tv_courier);
        tvPrice = itemView.findViewById(R.id.tv_price);
        imgCheck = itemView.findViewById(R.id.img_check);
        vSeparator = itemView.findViewById(R.id.v_separator);
        tvPromoPotency = itemView.findViewById(R.id.tv_promo_potency);
    }

    public void bindData(ShippingCourierViewModel shippingCourierViewModel,
                         ShippingCourierAdapterListener shippingCourierAdapterListener) {

        if (shippingCourierAdapterListener.isToogleYearEndPromotionOn() &&
                !TextUtils.isEmpty(shippingCourierViewModel.getProductData().getPromoCode())) {
            tvPromoPotency.setVisibility(View.VISIBLE);
        } else {
            tvPromoPotency.setVisibility(View.GONE);
        }

        tvCourier.setText(shippingCourierViewModel.getProductData().getShipperName());
        if (shippingCourierViewModel.getProductData().getError() != null &&
                shippingCourierViewModel.getProductData().getError().getErrorMessage().length() > 0) {
            tvPrice.setText(shippingCourierViewModel.getProductData().getError().getErrorMessage());
            tvPrice.setTextColor(ContextCompat.getColor(tvCourier.getContext(), R.color.text_courier_error_red));
            tvCourier.setTextColor(ContextCompat.getColor(tvCourier.getContext(), R.color.font_disabled));
            itemView.setOnClickListener(null);
        } else {
            tvPrice.setText(shippingCourierViewModel.getProductData().getPrice().getFormattedPrice());
            tvPrice.setTextColor(ContextCompat.getColor(tvCourier.getContext(), R.color.black_54));
            tvCourier.setTextColor(ContextCompat.getColor(tvCourier.getContext(), R.color.black_70));
            itemView.setOnClickListener(v -> shippingCourierAdapterListener.onCourierChoosen(
                    shippingCourierViewModel, cartPosition, hasCourierPromo));
        }
        imgCheck.setVisibility(shippingCourierViewModel.isSelected() ? View.VISIBLE : View.GONE);
    }
}
