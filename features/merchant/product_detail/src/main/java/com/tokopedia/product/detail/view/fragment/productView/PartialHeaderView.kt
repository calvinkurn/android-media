package com.tokopedia.product.detail.view.fragment.productView

import android.graphics.Paint
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.product.ProductInfo
import com.tokopedia.product.detail.data.model.product.ProductParams
import com.tokopedia.product.detail.data.util.discountedPrice
import com.tokopedia.product.detail.data.util.getCurrencyFormatted
import kotlinx.android.synthetic.main.partial_product_detail_header.view.*

class PartialHeaderView private constructor(private val view: View){
    companion object {
        fun build(_view: View) = PartialHeaderView(_view)
    }

    init {
        with(view.label_official_store){
            val blackString = context.getString(R.string.product_from)+" "
            val startSpan = blackString.length

            val spanText = SpannableString(blackString +
                    context.getString(R.string.from_official_store_label))
            spanText.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(context, R.color.purple_official_store)),
                    startSpan, spanText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spanText.setSpan(StyleSpan(Typeface.BOLD), startSpan, spanText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            setText(spanText, TextView.BufferType.SPANNABLE)
        }
    }

    fun showOfficialStore(shown: Boolean){
        if (shown) view.label_official_store.visible()
        else view.label_official_store.gone()
    }

    fun renderData(data: ProductInfo) {
        with(view) {
            product_name.text = MethodChecker.fromHtml(data.basic.name)
            if (data.cashback.percentage > 0){
                text_cashback.text = context.getString(R.string.template_cashback, data.cashback.percentage.toString())
                text_cashback.visibility = View.VISIBLE
            } else
                text_cashback.visibility = View.GONE

            val campaign = data.campaign
            if (campaign.isActive){
                tv_price_pdp.text = context.getString(R.string.template_price, "",
                        campaign.discountedPrice.getCurrencyFormatted())
                text_original_price.text = context.getString(R.string.template_price, "",
                        campaign.originalPrice.getCurrencyFormatted())

                text_original_price.paintFlags = text_original_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                text_discount.text = context.getString(R.string.template_campaign_off, campaign.percentage)

                text_original_price.visibility = View.VISIBLE
                text_discount.visibility = View.VISIBLE
                discount_timer_holder.visibility = View.VISIBLE
            } else {
                tv_price_pdp.text = context.getString(R.string.template_price, data.basic.priceCurrency,
                        data.basic.price.toString())
                text_original_price.visibility = View.GONE
                text_discount.visibility = View.GONE
                discount_timer_holder.visibility = View.GONE
            }
        }
    }
}