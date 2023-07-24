package com.tokopedia.logisticcart.shipping.features.shippingcourier.view

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil.setTextAndContentDescription

/**
 * Created by Irfan Khoirul on 06/08/18.
 */
class ShippingCourierViewHolder(itemView: View, private val cartPosition: Int) :
    RecyclerView.ViewHolder(itemView) {

    private val tvCourier: TextView = itemView.findViewById(R.id.tv_courier)
    private val tvPriceOrDuration: TextView = itemView.findViewById(R.id.tv_price_or_duration)
    private val imgCheck: IconUnify = itemView.findViewById(R.id.img_check)
    private val separator: View = itemView.findViewById(R.id.separator)
    private val codLabel: Label = itemView.findViewById(R.id.lbl_cod_available)
    private val otdLabel: Label = itemView.findViewById(R.id.lbl_otd_available)
    private val codLabelEta: Label = itemView.findViewById(R.id.lbl_cod_available_eta)
    private val imgMvc: ImageView = itemView.findViewById(R.id.img_mvc)
    private val tvMvc: Typography = itemView.findViewById(R.id.tv_mvc_text)
    private val tvMvcError: Typography = itemView.findViewById(R.id.tv_mvc_error)
    private val layoutMvc: ConstraintLayout = itemView.findViewById(R.id.layout_mvc)
    private val flDisableContainer: FrameLayout = itemView.findViewById(R.id.fl_container)
    private val dynamicPriceLabel: Label = itemView.findViewById(R.id.lbl_dynamic_pricing)

    fun bindData(
        shippingCourierUiModel: ShippingCourierUiModel,
        shippingCourierAdapterListener: ShippingCourierAdapterListener?,
        isLastItem: Boolean
    ) {
        if (isLastItem) {
            separator.visibility = View.GONE
        } else {
            separator.visibility = View.VISIBLE
        }

        if (shippingCourierUiModel.productData.codProductData != null) {
            /*cod label*/
            codLabel.text = shippingCourierUiModel.productData.codProductData.codText
            codLabel.visibility =
                if (shippingCourierUiModel.productData.codProductData.isCodAvailable == COD_ENABLE_VALUE) View.VISIBLE else View.GONE
        }

        if (shippingCourierUiModel.productData.features != null &&
            shippingCourierUiModel.productData.features.ontimeDeliveryGuarantee != null
        ) {
            val otd = shippingCourierUiModel
                .productData.features.ontimeDeliveryGuarantee
            otdLabel.visibility = if (otd.available) View.VISIBLE else View.GONE
        }

        if (shippingCourierUiModel.productData.features.dynamicPriceData == null ||
            shippingCourierUiModel.productData.features.dynamicPriceData.textLabel.isEmpty()
        ) {
            dynamicPriceLabel.visibility = View.GONE
        } else {
            dynamicPriceLabel.visibility = View.VISIBLE
            dynamicPriceLabel.text =
                shippingCourierUiModel.productData.features.dynamicPriceData.textLabel
        }

        if (shippingCourierUiModel.productData.features.merchantVoucherProductData != null && shippingCourierUiModel.productData.features.merchantVoucherProductData.isMvc == 1) {
            layoutMvc.visibility = View.VISIBLE
            flDisableContainer.foreground = ContextCompat.getDrawable(
                flDisableContainer.context,
                R.drawable.fg_enabled_item
            )
            imgMvc.loadImage(shippingCourierUiModel.productData.features.merchantVoucherProductData.mvcLogo)
            tvMvc.setText(R.string.tv_mvc_text)
            tvMvcError.visibility = View.GONE
        } else if (shippingCourierUiModel.productData.features.merchantVoucherProductData != null && shippingCourierUiModel.productData.features.merchantVoucherProductData.isMvc == -1) {
            layoutMvc.visibility = View.VISIBLE
            flDisableContainer.foreground = ContextCompat.getDrawable(
                flDisableContainer.context,
                R.drawable.fg_disabled_item
            )
            imgMvc.loadImage(
                shippingCourierUiModel.productData.features.merchantVoucherProductData.mvcLogo
            )
            tvMvc.setText(R.string.tv_mvc_text)
            tvMvcError.visibility = View.VISIBLE
            tvMvcError.text =
                shippingCourierUiModel.productData.features.merchantVoucherProductData.mvcErrorMessage
        } else {
            layoutMvc.visibility = View.GONE
            tvMvcError.visibility = View.GONE
        }

        setTextAndContentDescription(
            tvCourier,
            shippingCourierUiModel.productData.shipperName,
            tvCourier.context.getString(R.string.content_desc_tv_courier)
        )

        if (shippingCourierUiModel.productData.error != null &&
            shippingCourierUiModel.productData.error.errorMessage.isNotEmpty()
        ) {
            setTextAndContentDescription(
                tvCourier,
                shippingCourierUiModel.productData.shipperName,
                tvCourier.context.getString(R.string.content_desc_tv_courier)
            )
            if (shippingCourierUiModel.productData.error.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED) {
                tvPriceOrDuration.text = shippingCourierUiModel.productData.error.errorMessage
                tvPriceOrDuration.setTextColor(
                    ContextCompat.getColor(
                        tvCourier.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN950_68
                    )
                )
                tvCourier.setTextColor(
                    ContextCompat.getColor(
                        tvCourier.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN950_96
                    )
                )
                itemView.setOnClickListener {
                    shippingCourierAdapterListener?.onCourierChoosen(
                        shippingCourierUiModel,
                        cartPosition,
                        true
                    )
                }
            } else {
                tvPriceOrDuration.text = shippingCourierUiModel.productData.error.errorMessage
                tvPriceOrDuration.setTextColor(
                    ContextCompat.getColor(
                        tvCourier.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_RN500
                    )
                )
                tvCourier.setTextColor(
                    ContextCompat.getColor(
                        tvCourier.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN950_44
                    )
                )
                itemView.setOnClickListener(null)
            }
        } else {
            /*ETA*/
            if (shippingCourierUiModel.productData.estimatedTimeArrival != null && shippingCourierUiModel.productData.estimatedTimeArrival.errorCode == 0) {
                if (shippingCourierUiModel.productData.estimatedTimeArrival.textEta.isNotEmpty()) {
                    tvPriceOrDuration.text =
                        shippingCourierUiModel.productData.estimatedTimeArrival.textEta
                } else {
                    tvPriceOrDuration.setText(R.string.estimasi_tidak_tersedia)
                }
                val shipperNameEta =
                    shippingCourierUiModel.productData.shipperName + " " + "(" + shippingCourierUiModel.productData.price.formattedPrice + ")"
                setTextAndContentDescription(
                    tvCourier,
                    shipperNameEta,
                    tvCourier.context.getString(R.string.content_desc_tv_courier)
                )
                codLabel.visibility = View.GONE
                codLabelEta.text = shippingCourierUiModel.productData.codProductData.codText
                codLabelEta.visibility =
                    if (shippingCourierUiModel.productData.codProductData.isCodAvailable == COD_ENABLE_VALUE) View.VISIBLE else View.GONE
            } else {
                setTextAndContentDescription(
                    tvCourier,
                    shippingCourierUiModel.productData.shipperName,
                    tvCourier.context.getString(R.string.content_desc_tv_courier)
                )
                tvPriceOrDuration.text = shippingCourierUiModel.productData.price.formattedPrice
                codLabelEta.visibility = View.GONE
                codLabel.text = shippingCourierUiModel.productData.codProductData.codText
                codLabel.visibility =
                    if (shippingCourierUiModel.productData.codProductData.isCodAvailable == COD_ENABLE_VALUE) View.VISIBLE else View.GONE
            }
            tvPriceOrDuration.setTextColor(
                ContextCompat.getColor(
                    tvCourier.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN950_68
                )
            )
            tvCourier.setTextColor(
                ContextCompat.getColor(
                    tvCourier.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN950_96
                )
            )
            itemView.setOnClickListener {
                shippingCourierAdapterListener?.onCourierChoosen(
                    shippingCourierUiModel,
                    cartPosition,
                    false
                )
            }
        }
        imgCheck.visibility = if (shippingCourierUiModel.isSelected) View.VISIBLE else View.GONE
    }

    companion object {
        val ITEM_VIEW_SHIPMENT_COURIER = R.layout.item_courier
        private const val COD_ENABLE_VALUE = 1
    }
}
