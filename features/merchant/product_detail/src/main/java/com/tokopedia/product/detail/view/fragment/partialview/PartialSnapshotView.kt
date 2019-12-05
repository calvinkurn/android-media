package com.tokopedia.product.detail.view.fragment.partialview

import android.graphics.Paint
import android.graphics.Typeface
import android.text.Spannable
import android.text.style.ImageSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.CampaignModular
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.warehouse.MultiOriginWarehouse
import com.tokopedia.product.detail.data.util.getCurrencyFormatted
import com.tokopedia.product.detail.data.util.numberFormatted
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.partial_product_detail_header.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class PartialSnapshotView(private val view: View,
                          private val listener: DynamicProductDetailListener) {

    fun renderData(product: DynamicProductInfoP1) {
        val data = product.data
        val basic = product.basic
        with(view) {
            product_name.text = MethodChecker.fromHtml(data.name)
            when {
                data.isFreeOngkir.isActive -> {
                    ImageHandler.loadImageRounded2(context, img_free_ongkir, data.isFreeOngkir.imageURL)
                    img_free_ongkir.visibility = View.VISIBLE
                }
                else -> img_free_ongkir.visibility = View.GONE
            }
            if (data.isCashback.percentage > 0) {
                text_cashback.text = context.getString(R.string.template_cashback, data.isCashback.percentage.toString())
                text_cashback.visibility = View.VISIBLE
            } else
                text_cashback.visibility = View.GONE

            val campaign = data.campaign
            if (campaign.isActive) {
                tv_price_pdp.text = context.getString(R.string.template_price, "",
                        campaign.discountedPrice.getCurrencyFormatted())
                text_title_discount_timer.text = data.campaign.campaignTypeName
                if (campaign.hideGimmick) {
                    text_original_price.visibility = View.GONE
                    text_discount.visibility = View.GONE
                } else {
                    text_original_price.visibility = View.VISIBLE
                    text_discount.visibility = View.VISIBLE
                }

                text_original_price.text = context.getString(R.string.template_price, "",
                        campaign.originalPrice.getCurrencyFormatted())

                text_original_price.paintFlags = text_original_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                text_discount.text = context.getString(R.string.template_campaign_off, campaign.percentageAmount.numberFormatted())

                sale_text_stock_available.text = MethodChecker.fromHtml(data.stock.stockWording)
                text_stock_available.text = MethodChecker.fromHtml(data.stock.stockWording)
                if (campaign.activeAndHasId) {
                    discount_timer_holder.visibility = View.VISIBLE
                    showCountDownTimer(data.campaign)
                    sale_text_stock_available.visible()
                    text_stock_available.gone()
                } else {
                    discount_timer_holder.gone()
                    sale_text_stock_available.gone()
                    text_stock_available.visible()
                }
            } else {
                tv_price_pdp.text = context.getString(R.string.template_price, "",
                        data.price.value.getCurrencyFormatted())
                text_original_price.visibility = View.GONE
                text_discount.visibility = View.GONE
                discount_timer_holder.visibility = View.GONE
                text_stock_available.text = MethodChecker.fromHtml(data.stock.stockWording)
                sale_text_stock_available.gone()
                text_stock_available.visible()
            }
            label_prescription.showWithCondition(basic.needPrescription)
            divider.visible()
        }
    }

    private fun hideProductCampaign(campaign: CampaignModular) {
        with(view) {
            discount_timer_holder.visibility = View.GONE
            text_discount.visibility = View.GONE
            text_original_price.visibility = View.GONE
            tv_price_pdp.text = context.getString(R.string.template_price, "",
                    campaign.originalPrice.getCurrencyFormatted())
            sale_text_stock_available.gone()
            text_stock_available.visible()
        }
    }

    fun updateStockAndPriceWarehouse(nearestWarehouse: MultiOriginWarehouse, campaign: CampaignModular) {
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

    fun renderCod(showCod: Boolean) {
        if (showCod) view.layout_cod_content.visible() else view.layout_cod_content.gone()
    }

    fun renderTradein(showTradein: Boolean) {
        if (showTradein) {
            view.tv_trade_in_promo.visible()
            view.tv_available_at?.visible()
        } else {
            view.tv_trade_in_promo.gone()
            view.tv_available_at?.gone()
        }
    }

    fun showOfficialStore(isGoldMerchant: Boolean, isOfficialStore: Boolean) {
        val imageIc: ImageSpan
        val colorIc: Int
        val labelIc: String
        val context = view.context
        val drawableSize = context.resources.getDimension(R.dimen.dp_16).toInt()

        if (isGoldMerchant && !isOfficialStore) {
            val drawablePm = MethodChecker.getDrawable(context, R.drawable.ic_power_merchant)
            drawablePm?.setBounds(0, 0, drawableSize, drawableSize)
            labelIc = context.getString(R.string.from_power_badge_label)
            imageIc = ImageSpan(drawablePm, ImageSpan.ALIGN_BOTTOM)
            colorIc = ContextCompat.getColor(context, R.color.green_power_badge)
            renderTxtIcon(labelIc, colorIc, imageIc)
        } else if (isOfficialStore) {
            val drawableOs = MethodChecker.getDrawable(context, R.drawable.ic_badge_shop_official)
            drawableOs?.setBounds(0, 0, drawableSize, drawableSize)
            labelIc = context.getString(R.string.from_official_store_label)
            imageIc = ImageSpan(drawableOs, ImageSpan.ALIGN_BOTTOM)
            colorIc = ContextCompat.getColor(context, R.color.purple_official_store)
            renderTxtIcon(labelIc, colorIc, imageIc)
            view.layout_guarantee.visible()
            view.layout_guarantee.setOnClickListener {
            listener.onValuePropositionClicked(R.id.layout_guarantee)
            }
        } else {
            view.layout_guarantee.gone()
            view.label_official_store.gone()
        }

    }

    private fun renderTxtIcon(labelIc: String, colorIc: Int, imageIc: ImageSpan) {
        with(view.label_official_store) {
            val blackString = context.getString(R.string.product_from) + "  "
            val startSpan = blackString.length
            val spanText = android.text.SpannableString(blackString + "   " +
                    labelIc)

            spanText.setSpan(imageIc, startSpan, startSpan + 1, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spanText.setSpan(
                    android.text.style.ForegroundColorSpan(colorIc),
                    startSpan, spanText.length, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spanText.setSpan(StyleSpan(Typeface.BOLD),
                    startSpan, spanText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            setText(spanText, android.widget.TextView.BufferType.SPANNABLE)
        }
        view.label_official_store.visible()

    }

    private fun showCountDownTimer(campaign: CampaignModular) {
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
            val endDateTimeMs = campaign.getEndDataInt * PartialHeaderView.ONE_SECOND
            val now = System.currentTimeMillis()
            val endDate = dateFormat.parse(campaign.endDate)
            val delta = endDate.time - endDateTimeMs

            if (TimeUnit.MICROSECONDS.toDays(now - endDate.time) < 1) {
                view.count_down.setup(delta, endDate) {
                    hideProductCampaign(campaign)
                    listener.showAlertCampaignEnded()
                }
                view.discount_timer_holder.visible()
            }
        } catch (ex: Exception) {
            view.discount_timer_holder.visibility = View.GONE
        }
    }

}