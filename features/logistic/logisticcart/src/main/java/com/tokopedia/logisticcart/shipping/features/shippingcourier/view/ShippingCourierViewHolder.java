package com.tokopedia.logisticcart.shipping.features.shippingcourier.view;

import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.iconunify.IconUnify;
import com.tokopedia.logisticcart.R;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel;
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData;
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.OntimeDeliveryGuarantee;
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
    private TextView tvPriceOrDuration;
    private IconUnify imgCheck;
    private TextView tvPromoPotency;
    private View separator;
    private Label codLabel;
    private Label otdLabel;
    private Label codLabelEta;
    private ImageView imgMvc;
    private Typography tvMvc;
    private Typography tvMvcError;
    private ConstraintLayout layoutMvc;
    private FrameLayout flDisableContainer;

    private int cartPosition;

    public ShippingCourierViewHolder(View itemView, int cartPosition) {
        super(itemView);
        this.cartPosition = cartPosition;

        tvCourier = itemView.findViewById(R.id.tv_courier);
        tvPriceOrDuration = itemView.findViewById(R.id.tv_price_or_duration);
        imgCheck = itemView.findViewById(R.id.img_check);
        tvPromoPotency = itemView.findViewById(R.id.tv_promo_potency);
        separator = itemView.findViewById(R.id.separator);
        codLabel = itemView.findViewById(R.id.lbl_cod_available);
        otdLabel = itemView.findViewById(R.id.lbl_otd_available);
        codLabelEta = itemView.findViewById(R.id.lbl_cod_available_eta);
        imgMvc = itemView.findViewById(R.id.img_mvc);
        tvMvc = itemView.findViewById(R.id.tv_mvc_text);
        tvMvcError = itemView.findViewById(R.id.tv_mvc_error);
        layoutMvc = itemView.findViewById(R.id.layout_mvc);
        flDisableContainer = itemView.findViewById(R.id.fl_container);
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
            layoutMvc.setVisibility(View.VISIBLE);
            flDisableContainer.setForeground(ContextCompat.getDrawable(flDisableContainer.getContext() , R.drawable.fg_enabled_item));
            ImageHandler.LoadImage(imgMvc, shippingCourierUiModel.getProductData().getFeatures().getMerchantVoucherProductData().getMvcLogo());
            tvMvc.setText(R.string.tv_mvc_text);
            tvMvcError.setVisibility(View.GONE);
        } else if (shippingCourierUiModel.getProductData().getFeatures().getMerchantVoucherProductData() != null && shippingCourierUiModel.getProductData().getFeatures().getMerchantVoucherProductData().isMvc() == -1) {
            layoutMvc.setVisibility(View.VISIBLE);
            flDisableContainer.setForeground(ContextCompat.getDrawable(flDisableContainer.getContext() , R.drawable.fg_disabled_item));
            ImageHandler.LoadImage(imgMvc, shippingCourierUiModel.getProductData().getFeatures().getMerchantVoucherProductData().getMvcLogo());
            tvMvc.setText(R.string.tv_mvc_text);
            tvMvcError.setVisibility(View.VISIBLE);
            tvMvcError.setText(shippingCourierUiModel.getProductData().getFeatures().getMerchantVoucherProductData().getMvcErrorMessage());
        } else {
            layoutMvc.setVisibility(View.GONE);
            tvMvcError.setVisibility(View.GONE);
        }

        TextAndContentDescriptionUtil.setTextAndContentDescription(tvCourier, shippingCourierUiModel.getProductData().getShipperName(), tvCourier.getContext().getString(R.string.content_desc_tv_courier));
        if (shippingCourierUiModel.getProductData().getError() != null &&
                shippingCourierUiModel.getProductData().getError().getErrorMessage().length() > 0) {
            TextAndContentDescriptionUtil.setTextAndContentDescription(tvCourier, shippingCourierUiModel.getProductData().getShipperName(), tvCourier.getContext().getString(R.string.content_desc_tv_courier));
            if (shippingCourierUiModel.getProductData().getError().getErrorId().equals(ErrorProductData.ERROR_PINPOINT_NEEDED)) {
                tvPriceOrDuration.setText(shippingCourierUiModel.getProductData().getError().getErrorMessage());
                tvPriceOrDuration.setTextColor(ContextCompat.getColor(tvCourier.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
                tvCourier.setTextColor(ContextCompat.getColor(tvCourier.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_96));
                itemView.setOnClickListener(v -> shippingCourierAdapterListener.onCourierChoosen(shippingCourierUiModel, cartPosition, true));
            } else {
                tvPriceOrDuration.setText(shippingCourierUiModel.getProductData().getError().getErrorMessage());
                tvPriceOrDuration.setTextColor(ContextCompat.getColor(tvCourier.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_R600));
                tvCourier.setTextColor(ContextCompat.getColor(tvCourier.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_44));
                itemView.setOnClickListener(null);
            }
        } else {
            /*ETA*/
            if (shippingCourierUiModel.getProductData().getEstimatedTimeArrival() != null && shippingCourierUiModel.getProductData().getEstimatedTimeArrival().getErrorCode() == 0) {
                if (!shippingCourierUiModel.getProductData().getEstimatedTimeArrival().getTextEta().isEmpty()) {
                    tvPriceOrDuration.setText(shippingCourierUiModel.getProductData().getEstimatedTimeArrival().getTextEta());
                } else {
                    tvPriceOrDuration.setText(R.string.estimasi_tidak_tersedia);
                }
                String shipperNameEta = shippingCourierUiModel.getProductData().getShipperName() + " " + "(" + shippingCourierUiModel.getProductData().getPrice().getFormattedPrice() + ")";
                TextAndContentDescriptionUtil.setTextAndContentDescription(tvCourier, shipperNameEta, tvCourier.getContext().getString(R.string.content_desc_tv_courier));
                codLabel.setVisibility(View.GONE);
                codLabelEta.setText(shippingCourierUiModel.getProductData().getCodProductData().getCodText());
                codLabelEta.setVisibility(shippingCourierUiModel.getProductData().getCodProductData().
                        getIsCodAvailable() == COD_ENABLE_VALUE ? View.VISIBLE : View.GONE);
            } else {
                TextAndContentDescriptionUtil.setTextAndContentDescription(tvCourier, shippingCourierUiModel.getProductData().getShipperName(), tvCourier.getContext().getString(R.string.content_desc_tv_courier));
                tvPriceOrDuration.setText(shippingCourierUiModel.getProductData().getPrice().getFormattedPrice());
                codLabelEta.setVisibility(View.GONE);
                codLabel.setText(shippingCourierUiModel.getProductData().getCodProductData().getCodText());
                codLabel.setVisibility(shippingCourierUiModel.getProductData().getCodProductData().
                        getIsCodAvailable() == COD_ENABLE_VALUE? View.VISIBLE : View.GONE );
            }
            tvPriceOrDuration.setTextColor(ContextCompat.getColor(tvCourier.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
            tvCourier.setTextColor(ContextCompat.getColor(tvCourier.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_96));
            itemView.setOnClickListener(v -> shippingCourierAdapterListener.onCourierChoosen(
                    shippingCourierUiModel, cartPosition, false));
        }
        imgCheck.setVisibility(shippingCourierUiModel.isSelected() ? View.VISIBLE : View.GONE);
    }
}
