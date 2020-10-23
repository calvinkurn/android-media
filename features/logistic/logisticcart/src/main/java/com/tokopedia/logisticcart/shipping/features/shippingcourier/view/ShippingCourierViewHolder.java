package com.tokopedia.logisticcart.shipping.features.shippingcourier.view;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.logisticcart.R;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.OntimeDeliveryGuarantee;
import com.tokopedia.unifycomponents.Label;
import com.tokopedia.unifyprinciples.Typography;
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil;

/**
 * Created by Irfan Khoirul on 06/08/18.
 */

public class ShippingCourierViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_SHIPMENT_COURIER = R.layout.item_courier;
    private static final int COD_ENABLE_VALUE = 1;

    private TextView tvCourier;
    private TextView tvPrice;
    private ImageView imgCheck;
    private TextView tvPromoPotency;
    private View separator;
    private Label codLabel;
    private Label otdLabel;
    private ImageView imgMvc;
    private Typography tvMvc;

    private int cartPosition;

    public ShippingCourierViewHolder(View itemView, int cartPosition) {
        super(itemView);
        this.cartPosition = cartPosition;

        tvCourier = itemView.findViewById(R.id.tv_courier);
        tvPrice = itemView.findViewById(R.id.tv_price);
        imgCheck = itemView.findViewById(R.id.img_check);
        tvPromoPotency = itemView.findViewById(R.id.tv_promo_potency);
        separator = itemView.findViewById(R.id.separator);
        codLabel = itemView.findViewById(R.id.lbl_cod_available);
        otdLabel = itemView.findViewById(R.id.lbl_otd_available);
        imgMvc = itemView.findViewById(R.id.img_mvc);
        tvMvc = itemView.findViewById(R.id.tv_mvc_text);
    }

    public void bindData(ShippingCourierUiModel shippingCourierUiModel,
                         ShippingCourierAdapterListener shippingCourierAdapterListener,
                         boolean isLastItem) {

        if (isLastItem) {
            separator.setVisibility(View.GONE);
        } else {
            separator.setVisibility(View.VISIBLE);
        }

        if (shippingCourierAdapterListener.isToogleYearEndPromotionOn() &&
                !TextUtils.isEmpty(shippingCourierUiModel.getProductData().getPromoCode())) {
            tvPromoPotency.setVisibility(View.VISIBLE);
        } else {
            tvPromoPotency.setVisibility(View.GONE);
        }

        if (shippingCourierUiModel.getProductData().getCodProductData() != null) {
            /*cod label*/
            codLabel.setText(shippingCourierUiModel.getProductData().getCodProductData().getCodText());
            codLabel.setVisibility(shippingCourierUiModel.getProductData().getCodProductData().
                    getIsCodAvailable() == COD_ENABLE_VALUE? View.VISIBLE : View.GONE );
        }

        if (shippingCourierUiModel.getProductData().getFeatures() != null &&
                shippingCourierUiModel.getProductData().getFeatures().getOntimeDeliveryGuarantee() != null) {
            OntimeDeliveryGuarantee otd = shippingCourierUiModel
                    .getProductData().getFeatures().getOntimeDeliveryGuarantee();
            otdLabel.setVisibility(otd.getAvailable()? View.VISIBLE : View.GONE);
        }

        if (shippingCourierUiModel.getProductData().getFeatures().getMerchantVoucherProductData() != null && shippingCourierUiModel.getProductData().getFeatures().getMerchantVoucherProductData().isMvc() == 1) {
            imgMvc.setVisibility(View.VISIBLE);
            ImageHandler.LoadImage(imgMvc, shippingCourierUiModel.getProductData().getFeatures().getMerchantVoucherProductData().getMvcLogo());
            tvMvc.setVisibility(View.VISIBLE);
        } else if (shippingCourierUiModel.getProductData().getFeatures().getMerchantVoucherProductData() != null && shippingCourierUiModel.getProductData().getFeatures().getMerchantVoucherProductData().isMvc() == 0){
            imgMvc.setVisibility(View.VISIBLE);
            ImageHandler.LoadImage(imgMvc, shippingCourierUiModel.getProductData().getFeatures().getMerchantVoucherProductData().getMvcLogo());
            tvMvc.setVisibility(View.VISIBLE);
            ContextCompat.getColor(imgMvc.getContext(), R.color.font_disabled);
            tvMvc.setTextColor(ContextCompat.getColor(tvMvc.getContext(), R.color.font_disabled));
        } else {
            imgMvc.setVisibility(View.GONE);
            tvMvc.setVisibility(View.GONE);
        }

        TextAndContentDescriptionUtil.setTextAndContentDescription(tvCourier, shippingCourierUiModel.getProductData().getShipperName(), tvCourier.getContext().getString(R.string.content_desc_tv_courier));
        if (shippingCourierUiModel.getProductData().getError() != null &&
                shippingCourierUiModel.getProductData().getError().getErrorMessage().length() > 0) {
            if (shippingCourierUiModel.getProductData().getError().getErrorId().equals(ErrorProductData.ERROR_PINPOINT_NEEDED)) {
                tvPrice.setText(shippingCourierUiModel.getProductData().getError().getErrorMessage());
                tvPrice.setTextColor(ContextCompat.getColor(tvCourier.getContext(), R.color.black_54));
                tvCourier.setTextColor(ContextCompat.getColor(tvCourier.getContext(), R.color.black_70));
                itemView.setOnClickListener(v -> shippingCourierAdapterListener.onCourierChoosen(shippingCourierUiModel, cartPosition, true));
            } else {
                tvPrice.setText(shippingCourierUiModel.getProductData().getError().getErrorMessage());
                tvPrice.setTextColor(ContextCompat.getColor(tvCourier.getContext(), R.color.text_courier_error_red));
                tvCourier.setTextColor(ContextCompat.getColor(tvCourier.getContext(), R.color.font_disabled));
                itemView.setOnClickListener(null);
            }
        } else {
            tvPrice.setText(shippingCourierUiModel.getProductData().getPrice().getFormattedPrice());
            tvPrice.setTextColor(ContextCompat.getColor(tvCourier.getContext(), R.color.black_54));
            tvCourier.setTextColor(ContextCompat.getColor(tvCourier.getContext(), R.color.black_70));
            itemView.setOnClickListener(v -> shippingCourierAdapterListener.onCourierChoosen(
                    shippingCourierUiModel, cartPosition, false));
        }
        imgCheck.setVisibility(shippingCourierUiModel.isSelected() ? View.VISIBLE : View.GONE);
    }
}
