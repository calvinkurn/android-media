package com.tokopedia.shopdiscount.product_detail.presentation.adapter.viewholder

import android.graphics.Paint
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.isVisibleOnTheScreen
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.utils.formatter.RangeFormatterUtil
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
        fun onClickEditProduct(
            model: ShopDiscountProductDetailUiModel.ProductDetailData,
            position: Int
        )

        fun onClickDeleteProduct(uiModel: ShopDiscountProductDetailUiModel.ProductDetailData)

        fun onClickSubsidyInfo(uiModel: ShopDiscountProductDetailUiModel.ProductDetailData)

        fun onShowCoachMarkSubsidyInfo(view: View)

        fun onClickOptOutSubsidy(uiModel: ShopDiscountProductDetailUiModel.ProductDetailData)
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
                listener.onClickEditProduct(uiModel, adapterPosition)
            }
            imgDeleteProduct.setOnClickListener {
                listener.onClickDeleteProduct(uiModel)
            }
            icOptOutSubsidy.setOnClickListener {
                listener.onClickOptOutSubsidy(uiModel)
            }
            setSubsidyInfoSectionData(this, uiModel)
        }
    }

    private fun setSubsidyInfoSectionData(
        binding: ShopDiscountProductDetailItemLayoutBinding,
        uiModel: ShopDiscountProductDetailUiModel.ProductDetailData
    ) {
        binding.apply {
            if(uiModel.isSubsidy){
                textSubsidyStatus.apply {
                    show()
                    text = uiModel.subsidyStatusText
                    setOnClickListener {
                        listener.onClickSubsidyInfo(uiModel)
                    }
                }
                iconSubsidyInfo.apply {
                    show()
                    setOnClickListener {
                        listener.onClickSubsidyInfo(uiModel)
                    }
                }
            }
            configCoachMarkSubsidyInfo(binding)
        }
    }

    private fun configCoachMarkSubsidyInfo(binding: ShopDiscountProductDetailItemLayoutBinding) {
        if (bindingAdapterPosition == 0) {
            binding.root.isVisibleOnTheScreen(
                onViewVisible = {
                    listener.onShowCoachMarkSubsidyInfo(binding.textSubsidyStatus)
                },
                onViewNotVisible = {
                }
            )
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
        val formattedOriginalPrice = RangeFormatterUtil.getFormattedRangeString(
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
        val formattedDiscountPercentage = RangeFormatterUtil.getFormattedRangeString(
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
        val formattedDisplayedPrice = RangeFormatterUtil.getFormattedRangeString(
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

}
