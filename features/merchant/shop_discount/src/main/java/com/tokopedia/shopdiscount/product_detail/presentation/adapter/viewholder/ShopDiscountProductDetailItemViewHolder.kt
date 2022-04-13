package com.tokopedia.shopdiscount.product_detail.presentation.adapter.viewholder

import android.graphics.Paint
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.ShopDiscountProductDetailItemLayoutBinding
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailUiModel
import com.tokopedia.shopdiscount.utils.constant.DateConstant
import com.tokopedia.shopdiscount.utils.extension.parseTo
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.date.toDate
import com.tokopedia.utils.view.binding.viewBinding

open class ShopDiscountProductDetailItemViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<ShopDiscountProductDetailUiModel.ProductDetailData>(itemView) {
    private var viewBinding: ShopDiscountProductDetailItemLayoutBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shop_discount_product_detail_item_layout
    }

    interface Listener {
        fun onClickEditProduct(model: ShopDiscountProductDetailUiModel.ProductDetailData)
    }

    override fun bind(uiModel: ShopDiscountProductDetailUiModel.ProductDetailData) {
        setProductDetailData(uiModel)
    }

    private fun setProductDetailData(uiModel: ShopDiscountProductDetailUiModel.ProductDetailData) {
        viewBinding?.apply {
            textProductName.text = uiModel.productName
            imgProduct.loadImage(uiModel.productImageUrl)
            setDiscountedPriceData(textDisplayedPrice, uiModel)
            setLabelDiscount(labelDiscount, uiModel)
            setOriginalPrice(textOriginalPrice, uiModel)
            setStockAndTotalLocationData(textStockAndTotalLocation, uiModel)
            setExpiryRange(textStartDateEndDate, uiModel)
            imgEditProduct.setOnClickListener {
                listener.onClickEditProduct(uiModel)
            }
        }
    }

    private fun setExpiryRange(
        textStartDateEndDate: Typography,
        uiModel: ShopDiscountProductDetailUiModel.ProductDetailData
    ) {
        val startDate = getFormattedDate(uiModel.startDate)
        val endDate = getFormattedDate(uiModel.endDate)
        textStartDateEndDate.text = String.format(
            getString(R.string.product_detail_start_date_end_date_format),
            startDate,
            endDate
        )
    }

    private fun setOriginalPrice(
        textOriginalPrice: Typography,
        uiModel: ShopDiscountProductDetailUiModel.ProductDetailData
    ) {
        val formattedOriginalPrice = getFormattedRangeString(
            uiModel.minOriginalPrice,
            uiModel.maxOriginalPrice, {
                it.getCurrencyFormatted()
            }, { min, max ->
                String.format(
                    getString(R.string.product_detail_original_price_format),
                    min.getCurrencyFormatted(),
                    max.getCurrencyFormatted()
                )
            }
        )
        textOriginalPrice.apply {
            text = formattedOriginalPrice
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    private fun setLabelDiscount(
        labelDiscount: Label,
        uiModel: ShopDiscountProductDetailUiModel.ProductDetailData
    ) {
        val formattedDiscountPercentage = getFormattedRangeString(
            uiModel.minDiscount,
            uiModel.maxDiscount, {
                String.format(
                    getString(R.string.shop_discount_product_detail_percent_format_non_range),
                    it
                )
            }, { min, max ->
                String.format(
                    getString(R.string.shop_discount_product_detail_percent_format_range),
                    min.thousandFormatted(),
                    max.thousandFormatted()
                )
            }
        )
        labelDiscount.text = formattedDiscountPercentage
    }

    private fun setDiscountedPriceData(
        textDisplayedPrice: Typography,
        uiModel: ShopDiscountProductDetailUiModel.ProductDetailData
    ) {
        val minDisplayedPrice: Int = uiModel.minPriceDiscounted
        val maxDisplayedPrice: Int = uiModel.maxPriceDiscounted
        val formattedDisplayedPrice = getFormattedRangeString(
            minDisplayedPrice,
            maxDisplayedPrice, {
                it.getCurrencyFormatted()
            }, { min, max ->
                String.format(
                    getString(R.string.product_detail_displayed_price_format),
                    min.getCurrencyFormatted(),
                    max.getCurrencyFormatted()
                )
            }
        )
        textDisplayedPrice.text = formattedDisplayedPrice
    }

    private fun setStockAndTotalLocationData(
        textStockAndTotalLocation: Typography,
        uiModel: ShopDiscountProductDetailUiModel.ProductDetailData
    ) {
        val totalLocation = uiModel.totalLocation
        textStockAndTotalLocation.text = if (totalLocation > 1) {
            MethodChecker.fromHtml(
                String.format(
                    getString(R.string.product_detail_stock_and_total_warehouse_format),
                    uiModel.stock,
                    uiModel.totalLocation
                )
            )
        } else {
            MethodChecker.fromHtml(
                String.format(
                    getString(R.string.product_detail_stock_format),
                    uiModel.stock
                )
            )
        }
    }

    private fun getFormattedDate(dateString: String): String {
        return dateString.toDate(DateConstant.DATE_FORMAT_WITH_TIMEZONE)
            .parseTo(DateConstant.DATE_TIME_MINUTE_PRECISION)
    }

    private fun getFormattedRangeString(
        min: Int,
        max: Int,
        formatNonRange: (min: Int) -> String,
        formatWithRange: (min: Int, max: Int) -> String
    ): String {
        return if (min.isZero() && max.isZero()) {
            ""
        } else if (min == max) {
            formatNonRange(min)
        } else {
            formatWithRange(min, max)
        }
    }

}