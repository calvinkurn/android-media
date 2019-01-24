package com.tokopedia.product.detail.view.fragment.productView

import android.graphics.Paint
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.ProductInfo
import com.tokopedia.product.detail.data.model.ProductParams
import com.tokopedia.product.detail.data.util.discountedPrice
import kotlinx.android.synthetic.main.partial_product_detail_header.view.*

class PartialHeaderView private constructor(private val view: View){
    companion object {
        fun build(_view: View) = PartialHeaderView(_view)
    }

    fun renderDataTemp(productParams: ProductParams){
        with(view){
            product_name.text = MethodChecker.fromHtml(productParams.productName)
            tv_price_pdp.text = productParams.productPrice
        }
    }

    fun renderData(data: ProductInfo) {
        with(view) {
            product_name.text = MethodChecker.fromHtml(data.basic.name)
            if (!data.cashback.percentage.isEmpty() && (data.cashback.percentage.toIntOrNull() ?: 0) > 0){
                text_cashback.text = context.getString(R.string.template_cashback, data.cashback.percentage)
            }
            val campaign = data.campaign
            if (campaign.isActive){
                tv_price_pdp.text = context.getString(R.string.template_price, data.basic.priceCurrency,
                        campaign.discountedPrice.toString())
                text_original_price.text = context.getString(R.string.template_price, data.basic.priceCurrency,
                        campaign.originalPrice.toString())

                text_original_price.paintFlags = text_original_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                text_discount.text = context.getString(R.string.template_campaign_off, campaign.percentage)

                text_original_price.visibility = View.VISIBLE
                text_discount.visibility = View.VISIBLE
                text_title_discount_timer.visibility = View.VISIBLE
                count_down.visibility = View.VISIBLE
            } else {
                tv_price_pdp.text = context.getString(R.string.template_price, data.basic.priceCurrency,
                        data.basic.price.toString())
                text_title_discount_timer.visibility = View.GONE
                text_original_price.visibility = View.GONE
                text_discount.visibility = View.GONE
                count_down.visibility = View.GONE
            }
        }
    }
}