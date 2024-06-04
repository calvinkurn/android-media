package com.tokopedia.catalogcommon.viewholder

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetItemSellerOfferingBinding
import com.tokopedia.catalogcommon.listener.SellerOfferingListener
import com.tokopedia.catalogcommon.uimodel.SellerOfferingUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.strikethrough
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding

class SellerOfferingViewHolder(
    itemView: View,
    private val sellerOfferingListener: SellerOfferingListener? = null
) : AbstractViewHolder<SellerOfferingUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_item_seller_offering
    }

    private val binding by viewBinding<WidgetItemSellerOfferingBinding>()
    private var productId: String = ""
    private var shopId: String = ""
    init {
        binding?.apply {
            btnAtc.setOnClickListener {
                sellerOfferingListener?.onSellerOfferingAtcButtonClicked(productId, shopId)
            }
            btnChat.setOnClickListener {
                sellerOfferingListener?.onSellerOfferingChatButtonClicked()
            }
            ivProduct.setOnClickListener {
                sellerOfferingListener?.onSellerOfferingProductImageClicked(productId)
            }
            lnVariant.setOnClickListener {
                sellerOfferingListener?.onSellerOfferingVariantArrowClicked(productId, shopId)
            }
            clProductCard.setOnClickListener {
                sellerOfferingListener?.onSellerOfferingProductInfo(productId, shopId)
            }
        }
    }

    override fun bind(element: SellerOfferingUiModel) {
        productId = element.productId
        shopId = element.shopId
        sellerOfferingListener?.onImpressionSellerOffering(productId,shopId)
        binding?.apply {
            setStyleWidget(element)
            lnVariant.showWithCondition(element.variantsName.isNotEmpty())
            tvVariantValue.text = element.variantsName
            ivBadge.loadImage(element.shopBadge)
            tvShopName.text = element.shopName
            tvShopLocation.text = element.shopLocation
            vPointShopLocationAndShopName.isVisible = element.shopLocation.isNotEmpty()
            tvShopResponsiveChat.setTextAndCheckShow(element.chatResponseTime)
            tvShopResponsiveOrder.setTextAndCheckShow(element.orderProcessTime)
            tvShopCredibility.showWithCondition(
                element.chatResponseTime.isNotEmpty() ||
                    element.orderProcessTime.isNotEmpty()
            )
            vPointResponseCredibility.showWithCondition(element.orderProcessTime.isNotEmpty())
            ivProduct.loadImage(element.productImage)
            tvProductName.text = itemView.resources.getString(
                R.string.catalog_prefix_title_section,
                element.productName
            )
            tvPrice.text = element.productPrice
            tvSlashPrice.setTextAndCheckShow(element.productSlashPrice)
            tvSlashPrice.strikethrough()
            tvLabelPromo.setTextAndCheckShow(element.labelPromo)
            lnPromoBmsm.showWithCondition(element.labelPromo.isNotEmpty() || element.labelTotalDisc.isNotEmpty())
            lnRating.showWithCondition(
                element.shopRating.isNotEmpty() ||
                    element.totalShopRating.isNotEmpty() || element.totalSold.isNotEmpty()
            )

            clFreeOngkirEtaAndAddService.showWithCondition(
                element.freeOngkir.isNotEmpty() ||
                    element.additionalService.isNotEmpty() || element.courier.isNotEmpty()
            )

            tvLabelDisc.text = element.labelTotalDisc
            tvSalesRatingFloat.text = element.shopRating
            tvTotalSold.text = element.totalSold
            salesRatingFloatLine.showWithCondition(element.totalSold.isNotEmpty())
            ivFreeOngkir.loadImage(element.freeOngkir)
            ivFreeOngkir.showWithCondition(element.freeOngkir.isNotEmpty())
            tvAdditionalService.setTextAndCheckShow(element.additionalService)
            ivInstallment.loadImage(element.paymentOption.icon)
            vGuarantee.showWithCondition(element.additionalService.isNotEmpty())
            vCourier.showWithCondition(element.courier.isNotEmpty())
            tvCourier.text = element.courier
            tvInstallment.text = element.paymentOption.desc
            lnInstallment.showWithCondition(element.paymentOption.desc.isNotEmpty())
            tvEstimation.setTextAndCheckShow(element.estimationShipping)
            progressProduct.setProgressIcon(
                icon = ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.catalog_ic_stockbar_progress_top
                ),
                width = 16.toPx(),
                height = 15.toPx()
            )
            progressProduct.setValue(element.stockBar.stockRemaining)
            tvStockAlert.text = element.stockBar.stockAlertText
            lnStockBar.showWithCondition(!element.stockBar.isHidden)
            if (element.totalShopRating.isNotEmpty()) {
                tvSalesRatingCount.visible()
                tvSalesRatingCount.text = "(${element.totalShopRating})"
            } else {
                tvSalesRatingCount.gone()
            }
            ivRating.showWithCondition(element.shopRating.isNotEmpty())
            tvSalesRatingFloat.showWithCondition(element.shopRating.isNotEmpty())
        }
    }

    private fun setStyleWidget(element: SellerOfferingUiModel) = binding?.apply {
        clProductCard.setBackgroundResource(
            getBackgroundTheme(
                element.darkMode,
                R.drawable.bg_rounded_border_light,
                R.drawable.bg_rounded_border_dark
            )
        )
        val drawableCircle =
            ContextCompat.getDrawable(itemView.context, R.drawable.bg_circle_border_light)
        if (drawableCircle is GradientDrawable) {
            if (element.darkMode) {
                drawableCircle.setColor(ContextCompat.getColor(itemView.context, R.color.dms_static_color_tertiary_for_dark))
            } else {
                drawableCircle.setColor(ContextCompat.getColor(itemView.context, R.color.dms_static_color_tertiary_for_light))
            }
        }

        vPointResponseCredibility.background = drawableCircle
        vPointShopLocationAndShopName.background = drawableCircle

        tvVariantValue.setTextColor(
            getColor(
                element.darkMode,
                lightColor = R.color.dms_static_text_color_light,
                darkColor = R.color.dms_static_text_color_dark
            )
        )
        tvShopName.setTextColor(
            getColor(
                element.darkMode,
                lightColor = R.color.dms_static_text_color_light,
                darkColor = R.color.dms_static_text_color_dark
            )
        )
        tvShopLocation.setTextColor(
            getColor(
                element.darkMode,
                lightColor = R.color.dms_static_text_color_light,
                darkColor = R.color.dms_static_text_color_dark
            )
        )
        tvShopResponsiveChat.setTextColor(
            getColor(
                element.darkMode,
                lightColor = R.color.dms_static_color_secondary,
                darkColor = R.color.dms_static_nn_600
            )
        )
        tvShopResponsiveOrder.setTextColor(
            getColor(
                element.darkMode,
                lightColor = R.color.dms_static_color_secondary,
                darkColor = R.color.dms_static_nn_600
            )
        )
        tvStockAlert.setTextColor(
            getColor(
                element.darkMode,
                lightColor = R.color.dms_static_color_secondary,
                darkColor = R.color.dms_static_nn_600
            )
        )
        tvProductName.setTextColor(
            getColor(
                element.darkMode,
                lightColor = R.color.dms_static_text_color_light,
                darkColor = R.color.dms_static_text_color_dark
            )
        )
        tvPrice.setTextColor(
            getColor(
                element.darkMode,
                lightColor = R.color.dms_static_text_color_light,
                darkColor = R.color.dms_static_text_color_dark
            )
        )
        tvSlashPrice.setTextColor(
            getColor(
                element.darkMode,
                lightColor = R.color.dms_static_color_secondary,
                darkColor = R.color.dms_static_nn_600
            )
        )
        tvLabelPromo.setTextColor(
            getColor(
                element.darkMode,
                lightColor = R.color.dms_static_catalog_color_error_light,
                darkColor = R.color.dms_static_catalog_color_error_dark
            )
        )
        tvLabelPromo.setBackgroundResource(
            getBackgroundTheme(
                element.darkMode,
                R.drawable.bg_red_rounded_border_light,
                R.drawable.bg_red_rounded_border_dark
            )
        )
        tvSalesRatingFloat.setTextColor(
            getColor(
                element.darkMode,
                lightColor = R.color.dms_static_color_secondary,
                darkColor = R.color.dms_static_nn_600
            )
        )
        tvSalesRatingCount.setTextColor(
            getColor(
                element.darkMode,
                lightColor = R.color.dms_static_color_secondary,
                darkColor = R.color.dms_static_nn_600
            )
        )
        tvTotalSold.setTextColor(
            getColor(
                element.darkMode,
                lightColor = R.color.dms_static_color_secondary,
                darkColor = R.color.dms_static_nn_600
            )
        )
        tvEstimation.setTextColor(
            getColor(
                element.darkMode,
                lightColor = R.color.dms_static_text_color_light,
                darkColor = R.color.dms_static_text_color_dark
            )
        )
        tvAdditionalService.setTextColor(
            getColor(
                element.darkMode,
                lightColor = R.color.dms_static_text_color_light,
                darkColor = R.color.dms_static_text_color_dark
            )
        )
        tvInstallment.setTextColor(
            getColor(
                element.darkMode,
                lightColor = R.color.dms_static_color_secondary,
                darkColor = R.color.dms_static_nn_600
            )
        )
        tvCourier.setTextColor(
            getColor(
                element.darkMode,
                lightColor = R.color.dms_static_text_color_light,
                darkColor = R.color.dms_static_text_color_dark
            )
        )
        ivButtonRightVariant.setImage(
            IconUnify.CHEVRON_RIGHT,
            newLightEnable = getColor(
                element.darkMode,
                lightColor = R.color.dms_static_text_color_light,
                darkColor = R.color.dms_static_text_color_dark
            ),
            newDarkEnable = getColor(
                element.darkMode,
                lightColor = R.color.dms_static_text_color_light,
                darkColor = R.color.dms_static_text_color_dark
            )
        )

        ivChat.setImage(
            IconUnify.CHAT,
            newLightEnable = getColor(
                element.darkMode,
                lightColor = R.color.dms_static_text_color_light,
                darkColor = R.color.dms_static_text_color_dark
            ),
            newDarkEnable = getColor(
                element.darkMode,
                lightColor = R.color.dms_static_text_color_light,
                darkColor = R.color.dms_static_text_color_dark
            )
        )
        btnChat.setBackgroundResource(
            getBackgroundTheme(
                element.darkMode,
                R.drawable.bg_rounded_border_color_primary_light,
                R.drawable.bg_rounded_border_color_primary_dark
            )
        )

        val drawable =
            ContextCompat.getDrawable(itemView.context, R.drawable.bg_rounded_border_dynamic_color)
        if (drawable is GradientDrawable) {
            if (element.cardColor.isNotEmpty()) {
                drawable.setColor(Color.parseColor(element.cardColor))
                clProductCard.background = drawable
            }
        }
    }

    private fun getColor(isDark: Boolean, lightColor: Int, darkColor: Int): Int {
        return if (isDark) {
            MethodChecker.getColor(itemView.context, darkColor)
        } else {
            MethodChecker.getColor(itemView.context, lightColor)
        }
    }

    private fun getBackgroundTheme(isDark: Boolean, lightColor: Int, darkColor: Int): Int {
        return if (isDark) {
            darkColor
        } else {
            lightColor
        }
    }
}
