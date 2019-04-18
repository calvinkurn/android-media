package com.tokopedia.product.detail.view.fragment.partialview

import android.app.Activity
import android.graphics.Paint
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.content.res.AppCompatResources
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.component.Dialog
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.product.Campaign
import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.product.detail.common.data.model.warehouse.MultiOriginWarehouse
import com.tokopedia.product.detail.data.model.shop.ShopInfo
import com.tokopedia.product.detail.data.util.getCurrencyFormatted
import com.tokopedia.product.detail.data.util.numberFormatted
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

    fun showOfficialStore(goldOs: ShopInfo.GoldOS){
        var imageIc: ImageSpan? = null
        var colorIc: Int? = null
        var labelIc = ""
        val context = view.context
        val drawableSize = context.resources.getDimension(R.dimen.dp_16).toInt()

        if (goldOs.isGoldBadge == 1 && goldOs.isOfficial == 0) {
            val drawablePm = MethodChecker.getDrawable(context, R.drawable.ic_power_merchant)
            drawablePm?.setBounds(0, 0, drawableSize, drawableSize)
            labelIc = context.getString(R.string.from_power_badge_label)
            imageIc = ImageSpan(drawablePm, ImageSpan.ALIGN_BOTTOM)
            colorIc = ContextCompat.getColor(context, R.color.green_power_badge)
            renderTxtIcon(labelIc, colorIc, imageIc)
        } else if (goldOs.isOfficial == 1) {
            val drawableOs = MethodChecker.getDrawable(context, R.drawable.ic_badge_shop_official)
            drawableOs?.setBounds(0, 0, drawableSize, drawableSize)
            labelIc = context.getString(R.string.from_official_store_label)
            imageIc = ImageSpan(drawableOs, ImageSpan.ALIGN_BOTTOM)
            colorIc = ContextCompat.getColor(context, R.color.purple_official_store)
            renderTxtIcon(labelIc, colorIc, imageIc)
        } else {
            view.label_official_store.gone()
        }

    }

    fun renderTxtIcon(labelIc: String, colorIc: Int, imageIc: ImageSpan) {

        with(view.label_official_store) {
            val blackString = context.getString(com.tokopedia.product.detail.R.string.product_from) + "  "
            val startSpan = blackString.length
            val spanText = android.text.SpannableString(blackString + "  " +
                    labelIc)

            spanText.setSpan(imageIc, startSpan - 1, startSpan, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spanText.setSpan(
                    android.text.style.ForegroundColorSpan(colorIc),
                    startSpan + 2, spanText.length, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spanText.setSpan(StyleSpan(Typeface.BOLD),
                    startSpan+1, spanText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            setText(spanText, android.widget.TextView.BufferType.SPANNABLE)
        }
        view.label_official_store.visible()

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
            if (campaign.activeAndHasId){
                tv_price_pdp.text = context.getString(R.string.template_price, "",
                        campaign.discountedPrice.getCurrencyFormatted())
                text_original_price.text = context.getString(R.string.template_price, "",
                        campaign.originalPrice.getCurrencyFormatted())

                text_original_price.paintFlags = text_original_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                text_discount.text = context.getString(R.string.template_campaign_off, campaign.percentage.numberFormatted())

                text_original_price.visibility = View.VISIBLE
                text_discount.visibility = View.VISIBLE
                discount_timer_holder.visibility = View.VISIBLE
                showCountDownTimer(data.campaign)
                sale_text_stock_available.text = MethodChecker.fromHtml(data.stock.stockWording)
                text_stock_available.text = MethodChecker.fromHtml(data.stock.stockWording)
                sale_text_stock_available.visible()
                text_stock_available.gone()
            } else {
                tv_price_pdp.text = context.getString(R.string.template_price, "",
                        data.basic.price.getCurrencyFormatted())
                text_original_price.visibility = View.GONE
                text_discount.visibility = View.GONE
                discount_timer_holder.visibility = View.GONE
                text_stock_available.text = MethodChecker.fromHtml(data.stock.stockWording)
                sale_text_stock_available.gone()
                text_stock_available.visible()
            }
            divider.visible()
        }
    }

    fun renderCod(showCod: Boolean){
        if (showCod) view.layout_cod_content.visible() else view.layout_cod_content.gone()
    }

    private fun showCountDownTimer(campaign: Campaign) {
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
            val endDateTimeMs = campaign.endDateUnix * ONE_SECOND
            val now = System.currentTimeMillis()
            val endDate = dateFormat.parse(campaign.endDate)
            val delta = endDate.time - endDateTimeMs

            if (TimeUnit.MICROSECONDS.toDays(now - endDate.time) < 1){
                view.count_down.setup(delta, endDate){
                    hideProductCampaign(campaign)
                    showAlertCampaignEnded()
                }
                view.discount_timer_holder.visible()
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
            sale_text_stock_available.gone()
            text_stock_available.visible()
        }
    }

    fun updateStockAndPriceWarehouse(nearestWarehouse: MultiOriginWarehouse, campaign: Campaign) {
        with(view) {
            if (campaign.activeAndHasId) {
                tv_price_pdp.text = context.getString(R.string.template_price, "",
                        nearestWarehouse.price.getCurrencyFormatted())
                sale_text_stock_available.text = MethodChecker.fromHtml(nearestWarehouse.stockWording)
            } else {
                tv_price_pdp.text = context.getString(R.string.template_price, "",
                        nearestWarehouse.price.getCurrencyFormatted())
                text_stock_available.text = MethodChecker.fromHtml(nearestWarehouse.stockWording)
            }
        }
    }
}