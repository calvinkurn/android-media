package com.tokopedia.shipping_recommendation.shippingcourier.view;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.shipping_recommendation.R;
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingCourierViewModel;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData;

/**
 * Created by Irfan Khoirul on 06/08/18.
 */

public class ShippingCourierViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_SHIPMENT_COURIER = R.layout.item_courier;
    private static final int COD_ENABLE_VALUE = 1;

    private TextView tvCourier;
    private TextView tvPrice;
    private TextView tvCod;
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
        tvCod = itemView.findViewById(R.id.tv_cod_availability);
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

        if (shippingCourierViewModel.getProductData().getCodProductData() != null) {
            tvCod.setText(shippingCourierViewModel.getProductData().getCodProductData().getCodText());
            tvCod.setVisibility(shippingCourierViewModel.getProductData().getCodProductData()
                    .getIsCodAvailable() == COD_ENABLE_VALUE ? View.VISIBLE : View.GONE);
        }

        tvCourier.setText(shippingCourierViewModel.getProductData().getShipperName());
        if (shippingCourierViewModel.getProductData().getError() != null &&
                shippingCourierViewModel.getProductData().getError().getErrorMessage().length() > 0) {
            if (shippingCourierViewModel.getProductData().getError().getErrorId().equals(ErrorProductData.ERROR_PINPOINT_NEEDED)) {
                tvPrice.setText(shippingCourierViewModel.getProductData().getError().getErrorMessage());
                tvPrice.setTextColor(ContextCompat.getColor(tvCourier.getContext(), R.color.black_54));
                tvCourier.setTextColor(ContextCompat.getColor(tvCourier.getContext(), R.color.black_70));
                itemView.setOnClickListener(v -> shippingCourierAdapterListener.onCourierChoosen(shippingCourierViewModel, cartPosition, true));
            } else {
                tvPrice.setText(shippingCourierViewModel.getProductData().getError().getErrorMessage());
                tvPrice.setTextColor(ContextCompat.getColor(tvCourier.getContext(), R.color.text_courier_error_red));
                tvCourier.setTextColor(ContextCompat.getColor(tvCourier.getContext(), R.color.font_disabled));
                itemView.setOnClickListener(null);
            }
        } else {
            tvPrice.setText(shippingCourierViewModel.getProductData().getPrice().getFormattedPrice());
            tvPrice.setTextColor(ContextCompat.getColor(tvCourier.getContext(), R.color.black_54));
            tvCourier.setTextColor(ContextCompat.getColor(tvCourier.getContext(), R.color.black_70));
            itemView.setOnClickListener(v -> shippingCourierAdapterListener.onCourierChoosen(
                    shippingCourierViewModel, cartPosition, false));
        }
        imgCheck.setVisibility(shippingCourierViewModel.isSelected() ? View.VISIBLE : View.GONE);
    }
}
