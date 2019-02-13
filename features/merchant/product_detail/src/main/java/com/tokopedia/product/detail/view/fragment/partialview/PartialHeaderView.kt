package com.tokopedia.product.detail.view.fragment.partialview

import android.app.Activity
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
import com.tokopedia.design.component.Dialog
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.Campaign
import com.tokopedia.product.detail.common.data.model.ProductInfo
import com.tokopedia.product.detail.data.util.discountedPrice
import com.tokopedia.product.detail.data.util.getCurrencyFormatted
import kotlinx.android.synthetic.main.partial_product_detail_header.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class PartialHeaderView private constructor(private val view: View,
                                            private val activity: Activity? = null){

    var onRefreshHandler: (() -> Unit)? = null
    var backToHomeHandler: (() -> Unit)? = null

    companion object {
        const val ONE_SECOND = 1000L
        fun build(_view: View, _activity: Activity?) = PartialHeaderView(_view, _activity)
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
                showCountDownTimer(data.campaign)
            } else {
                tv_price_pdp.text = context.getString(R.string.template_price, data.basic.priceCurrency,
                        data.basic.price.toString())
                text_original_price.visibility = View.GONE
                text_discount.visibility = View.GONE
                discount_timer_holder.visibility = View.GONE
            }
        }
    }

    private fun showCountDownTimer(campaign: Campaign) {
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX'Z'", Locale.getDefault())
            val serverTimeMs = campaign.endDateUnix * ONE_SECOND
            val serverTimeOffset = serverTimeMs - Date().time
            val endDate = dateFormat.parse(campaign.endDate)
            val endDelta = endDate.time - serverTimeMs

            if (TimeUnit.MICROSECONDS.toDays(endDelta) < 1){
                view.count_down.setup(serverTimeOffset, endDate){
                    hideProductCampaign(campaign)
                    showAlertCampaignEnded()
                }
            }
        } catch (ex: Exception){
            view.discount_timer_holder.visibility = View.GONE
        }
    }

    private fun showAlertCampaignEnded() {
        Dialog(activity, Dialog.Type.LONG_PROMINANCE).apply {
            setTitle(view.context.getString(R.string.campaign_expired_title))
            setDesc(view.context.getString(R.string.campaign_expired_descr))
            setBtnOk(view.context.getString(R.string.exp_dialog_ok))
            setBtnCancel(view.context.getString(R.string.close))
            setOnCancelClickListener { onRefreshHandler?.invoke(); dismiss() }
            setOnOkClickListener { dismiss(); backToHomeHandler?.invoke() }
        }.show()
    }

    private fun hideProductCampaign(campaign: Campaign) {
        with(view){
            discount_timer_holder.visibility = View.GONE
            text_discount.visibility = View.GONE
            text_original_price.visibility = View.GONE
            tv_price_pdp.text = context.getString(R.string.template_price, "",
                    campaign.originalPrice.getCurrencyFormatted())
        }
    }
}