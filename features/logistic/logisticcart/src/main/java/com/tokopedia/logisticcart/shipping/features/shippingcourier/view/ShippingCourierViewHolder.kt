package com.tokopedia.logisticcart.shipping.features.shippingcourier.view

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticcart.databinding.ItemCourierBinding
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil
import com.tokopedia.logisticcart.R as logisticcartR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by Irfan Khoirul on 06/08/18.
 */
class ShippingCourierViewHolder(private val binding: ItemCourierBinding, private val cartPosition: Int) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindData(
        shippingCourierUiModel: ShippingCourierUiModel,
        shippingCourierAdapterListener: ShippingCourierAdapterListener?,
        isLastItem: Boolean
    ) {
        binding.run {
            if (isLastItem) {
                separator.visibility = View.GONE
            } else {
                separator.visibility = View.VISIBLE
            }

            /*cod label*/
            lblCodAvailable.text = shippingCourierUiModel.productData.codProductData.codText
            lblCodAvailable.visibility =
                if (shippingCourierUiModel.productData.codProductData.isCodAvailable == COD_ENABLE_VALUE) View.VISIBLE else View.GONE

            val otd = shippingCourierUiModel
                .productData.features.ontimeDeliveryGuarantee
            lblOtdAvailable.visibility = if (otd.available) View.VISIBLE else View.GONE

            if (shippingCourierUiModel.productData.features.dynamicPriceData.textLabel.isEmpty()
            ) {
                lblDynamicPricing.visibility = View.GONE
            } else {
                lblDynamicPricing.visibility = View.VISIBLE
                lblDynamicPricing.text =
                    shippingCourierUiModel.productData.features.dynamicPriceData.textLabel
            }

            if (shippingCourierUiModel.productData.features.merchantVoucherProductData.isMvc == 1) {
                layoutMvc.visibility = View.VISIBLE
                flContainer.foreground = ContextCompat.getDrawable(
                    flContainer.context,
                    logisticcartR.drawable.fg_enabled_item
                )
                imgMvc.loadImage(shippingCourierUiModel.productData.features.merchantVoucherProductData.mvcLogo)
                tvMvcText.setText(logisticcartR.string.tv_mvc_text)
                tvMvcError.visibility = View.GONE
            } else if (shippingCourierUiModel.productData.features.merchantVoucherProductData.isMvc == -1) {
                layoutMvc.visibility = View.VISIBLE
                flContainer.foreground = ContextCompat.getDrawable(
                    flContainer.context,
                    logisticcartR.drawable.fg_disabled_item
                )
                imgMvc.loadImage(
                    shippingCourierUiModel.productData.features.merchantVoucherProductData.mvcLogo
                )
                tvMvcText.setText(logisticcartR.string.tv_mvc_text)
                tvMvcError.visibility = View.VISIBLE
                tvMvcError.text =
                    shippingCourierUiModel.productData.features.merchantVoucherProductData.mvcErrorMessage
            } else {
                layoutMvc.visibility = View.GONE
                tvMvcError.visibility = View.GONE
            }

            TextAndContentDescriptionUtil.setTextAndContentDescription(
                tvCourier,
                shippingCourierUiModel.productData.shipperName,
                tvCourier.context.getString(logisticcartR.string.content_desc_tv_courier)
            )

            if (shippingCourierUiModel.productData.error.errorMessage.isNotEmpty()) {
                TextAndContentDescriptionUtil.setTextAndContentDescription(
                    tvCourier,
                    shippingCourierUiModel.productData.shipperName,
                    tvCourier.context.getString(logisticcartR.string.content_desc_tv_courier)
                )
                if (shippingCourierUiModel.productData.error.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED) {
                    tvPriceOrDuration.text = shippingCourierUiModel.productData.error.errorMessage
                    tvPriceOrDuration.setTextColor(
                        ContextCompat.getColor(
                            tvCourier.context,
                            unifyprinciplesR.color.Unify_NN950_68
                        )
                    )
                    tvCourier.setTextColor(
                        ContextCompat.getColor(
                            tvCourier.context,
                            unifyprinciplesR.color.Unify_NN950_96
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
                            unifyprinciplesR.color.Unify_RN500
                        )
                    )
                    tvCourier.setTextColor(
                        ContextCompat.getColor(
                            tvCourier.context,
                            unifyprinciplesR.color.Unify_NN950_44
                        )
                    )
                    itemView.setOnClickListener(null)
                }
            } else {
                /*ETA*/
                if (shippingCourierUiModel.productData.estimatedTimeArrival.errorCode == 0) {
                    if (shippingCourierUiModel.productData.estimatedTimeArrival.textEta.isNotEmpty()) {
                        tvPriceOrDuration.text =
                            shippingCourierUiModel.productData.estimatedTimeArrival.textEta
                    } else {
                        tvPriceOrDuration.setText(logisticcartR.string.estimasi_tidak_tersedia)
                    }
                    val shipperNameEta =
                        shippingCourierUiModel.productData.shipperName + " " + "(" + shippingCourierUiModel.productData.price.formattedPrice + ")"
                    TextAndContentDescriptionUtil.setTextAndContentDescription(
                        tvCourier,
                        shipperNameEta,
                        tvCourier.context.getString(logisticcartR.string.content_desc_tv_courier)
                    )
                    lblCodAvailable.text = shippingCourierUiModel.productData.codProductData.codText
                    lblCodAvailable.visibility =
                        if (shippingCourierUiModel.productData.codProductData.isCodAvailable == COD_ENABLE_VALUE) View.VISIBLE else View.GONE
                } else {
                    TextAndContentDescriptionUtil.setTextAndContentDescription(
                        tvCourier,
                        shippingCourierUiModel.productData.shipperName,
                        tvCourier.context.getString(logisticcartR.string.content_desc_tv_courier)
                    )
                    tvPriceOrDuration.text = shippingCourierUiModel.productData.price.formattedPrice
                    lblCodAvailable.text = shippingCourierUiModel.productData.codProductData.codText
                    lblCodAvailable.visibility =
                        if (shippingCourierUiModel.productData.codProductData.isCodAvailable == COD_ENABLE_VALUE) View.VISIBLE else View.GONE
                }
                tvPriceOrDuration.setTextColor(
                    ContextCompat.getColor(
                        tvCourier.context,
                        unifyprinciplesR.color.Unify_NN950_68
                    )
                )
                tvCourier.setTextColor(
                    ContextCompat.getColor(
                        tvCourier.context,
                        unifyprinciplesR.color.Unify_NN950_96
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
    }

    companion object {
        val ITEM_VIEW_SHIPMENT_COURIER = 7
        private const val COD_ENABLE_VALUE = 1
    }
}
