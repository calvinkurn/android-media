package com.tokopedia.logisticcart.shipping.features.shippingcourier.view;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.logisticcart.R;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierViewModel;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.OntimeDeliveryGuarantee;

/**
 * Created by Irfan Khoirul on 06/08/18.
 */

public class ShippingCourierViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_SHIPMENT_COURIER = R.layout.item_courier;
    private static final int COD_ENABLE_VALUE = 1;

    private TextView tvCourier;
    private TextView tvPrice;
    private TextView tvCod;
    private TextView tvOtd;
    private ImageView ivOtd;
    private View viewOtd;
    private ImageView imgCheck;
    private TextView tvPromoPotency;

    private int cartPosition;

    public ShippingCourierViewHolder(View itemView, int cartPosition) {
        super(itemView);
        this.cartPosition = cartPosition;

        tvCourier = itemView.findViewById(R.id.tv_courier);
        tvPrice = itemView.findViewById(R.id.tv_price);
        tvCod = itemView.findViewById(R.id.tv_cod_availability);
        tvOtd = itemView.findViewById(R.id.tv_otd);
        ivOtd = itemView.findViewById(R.id.iv_icon_otd);
        viewOtd = itemView.findViewById(R.id.otdelivery);
        imgCheck = itemView.findViewById(R.id.img_check);
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

        if (shippingCourierViewModel.getProductData().getFeatures() != null &&
                shippingCourierViewModel.getProductData().getFeatures().getOntimeDeliveryGuarantee() != null) {
            OntimeDeliveryGuarantee otd = shippingCourierViewModel
                    .getProductData().getFeatures().getOntimeDeliveryGuarantee();
            viewOtd.setVisibility(otd.getAvailable() ? View.VISIBLE : View.GONE);
            tvOtd.setText(otd.getTextLabel());
            ImageHandler.LoadImage(ivOtd, otd.getIconUrl());
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
