package com.tokopedia.deals.search.ui.adapter.viewholder

import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.common.utils.DealsUtils
import com.tokopedia.deals.search.listener.DealsSearchListener
import com.tokopedia.deals.search.model.visitor.VoucherModel
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

class VoucherViewHolder(itemView: View, private val searchListener: DealsSearchListener) :
    AbstractViewHolder<VoucherModel>(itemView) {

    private var tvDealTitle = itemView.findViewById<Typography>(R.id.tv_simple_item)
    private var tvBrandName = itemView.findViewById<Typography>(R.id.tv_brand_name)
    private var brandImage = itemView.findViewById<ImageView>(R.id.iv_brand)
    private var salesPrice = itemView.findViewById<Typography>(R.id.tv_sales_price)
    private var mrpPrice = itemView.findViewById<Typography>(R.id.mrp)
    private var discount = itemView.findViewById<Label>(R.id.tv_off)

    private var price = 0L
    private var mrp = 0L

    override fun bind(element: VoucherModel) {
        tvDealTitle.text = element.voucherName
        tvBrandName.text = element.merchantName
        brandImage.loadImage(element.voucherThumbnail)

        checkAndConvertPrice(element)

        if (mrp > price) {
            mrpPrice.visibility = View.VISIBLE
            mrpPrice.text = DealsUtils.convertToCurrencyString(element.mrp.toLong())
            mrpPrice.paintFlags = mrpPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            mrpPrice.visibility = View.GONE
        }
        if (element.discountText.isNotEmpty() && !element.discountText.startsWith(ZERO_PERCENT)) {
            discount.visibility = View.VISIBLE
            discount.text = element.discountText
            discount.background =
                ContextCompat.getDrawable(itemView.context, R.drawable.bg_deals_softpink_box)
        } else {
            discount.visibility = View.GONE
            discount.background = null
        }
        var realPrice = 0L
        if (element.realPrice.isNotEmpty()) {
            realPrice = element.realPrice.toLong()
        }
        salesPrice.text = DealsUtils.convertToCurrencyString(realPrice)
        element.let { searchListener.onVoucherClicked(itemView, element) }
    }

    private fun checkAndConvertPrice(element: VoucherModel) {
        if (element.realPrice.isNotEmpty()) {
            price = element.realPrice.toLong()
        }
        if (element.mrp.isNotEmpty()) {
            mrp = element.mrp.toLong()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_deals_voucher
        private const val ZERO_PERCENT = "0%"
    }
}