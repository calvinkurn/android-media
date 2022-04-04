package com.tokopedia.shopdiscount.product_detail.presentation.adapter.viewholder

import android.graphics.Paint
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.ShopDiscountProductDetailItemLayoutBinding
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailUiModel
import com.tokopedia.shopdiscount.utils.constant.DateConstant
import com.tokopedia.shopdiscount.utils.extension.parseTo
import com.tokopedia.utils.date.toDate
import com.tokopedia.utils.view.binding.viewBinding

open class ShopDiscountProductDetailItemViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<ShopDiscountProductDetailUiModel>(itemView) {
    private var viewBinding: ShopDiscountProductDetailItemLayoutBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shop_discount_product_detail_item_layout
    }

    interface Listener {
        fun onClickEditProduct(model: ShopDiscountProductDetailUiModel)
    }

    override fun bind(uiModel: ShopDiscountProductDetailUiModel) {
        setProductDetailData(uiModel)
    }

    private fun setProductDetailData(uiModel: ShopDiscountProductDetailUiModel) {
        viewBinding?.apply {
            textProductName.text = uiModel.productName
            imgProduct.loadImage(uiModel.productImageUrl)
            textOriginalPrice.text = String.format(
                getString(R.string.product_detail_original_price_format),
                uiModel.minPrice,
                uiModel.maxPrice
            )
            textOriginalPrice.paintFlags =
                textOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            textStockAndTotalLocation.text = MethodChecker.fromHtml(
                String.format(
                    getString(R.string.product_detail_stock_and_total_warehouse_format),
                    uiModel.stock,
                    uiModel.totalLocation
                )
            )
            val startDate = getFormattedDate(uiModel.startDate)
            val endDate = getFormattedDate(uiModel.endDate)
            textStartDateEndDate.text = String.format(
                getString(R.string.product_detail_start_date_end_date_format),
                startDate,
                endDate
            )
            imgEditProduct.setOnClickListener {
                listener.onClickEditProduct(uiModel)
            }
        }
    }

    private fun getFormattedDate(dateString: String): String {
        return dateString.toDate(DateConstant.DATE_FORMAT_WITH_TIMEZONE)
            .parseTo(DateConstant.DATE_TIME_MINUTE_PRECISION)
    }

}