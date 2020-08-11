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
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.CampaignModular
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.data.util.getCurrencyFormatted
import com.tokopedia.product.detail.data.util.numberFormatted
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.partial_product_detail_header.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class PartialSnapshotView(private val view: View,
                          private val listener: DynamicProductDetailListener) {

    companion object {
        const val ONE_SECOND = 1000L
    }

    fun renderData(product: DynamicProductInfoP1, nearestWarehouseStockWording: String) = with(view) {
        val data = product.data
        val basic = product.basic
        val campaign = data.campaign

        product_name.text = MethodChecker.fromHtml(data.name)

        img_free_ongkir.shouldShowWithAction(data.isFreeOngkir.isActive) {
            ImageHandler.loadImageRounded2(context, img_free_ongkir, data.isFreeOngkir.imageURL)
        }

        text_cashback.shouldShowWithAction(data.isCashback.percentage > 0) {
            text_cashback.text = context.getString(R.string.template_cashback, data.isCashback.percentage.toString())
        }

        if (campaign.isActive) {
            renderCampaignActive(campaign, nearestWarehouseStockWording)
        } else {
            renderCampaignInactive(data.price.value.getCurrencyFormatted())
        }

        renderStockAvailable(campaign, data.variant.isVariant, nearestWarehouseStockWording, basic.isActive())
        label_prescription.showWithCondition(basic.needPrescription)
        divider.show()
    }

    fun updateWishlist(wishlisted: Boolean, shouldShowWishlist: Boolean) = with(view) {
        if (!shouldShowWishlist) {
            fab_detail_pdp.hide()
        } else {
            if (wishlisted) {
                fab_detail_pdp.hide()
                fab_detail_pdp.isActivated = true
                fab_detail_pdp.setImageDrawable(MethodChecker.getDrawable(context, R.drawable.ic_wishlist_selected_pdp))
                fab_detail_pdp.show()
            } else {
                fab_detail_pdp.hide()
                fab_detail_pdp.isActivated = false
                fab_detail_pdp.setImageDrawable(MethodChecker.getDrawable(context, R.drawable.ic_wishlist_unselected_pdp))
                fab_detail_pdp.show()
            }
        }
    }

    private fun renderCampaignActive(campaign: CampaignModular, stockWording: String) = with(view) {
        tv_price_pdp.text = context.getString(R.string.template_price, "",
                campaign.discountedPrice.getCurrencyFormatted())
        text_original_price.text = context.getString(R.string.template_price, "",
                campaign.originalPrice.getCurrencyFormatted())
        text_original_price.paintFlags = text_original_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        text_discount.text = context.getString(R.string.template_campaign_off, campaign.percentageAmount.numberFormatted())

        hideGimmick(campaign)
        renderFlashSale(campaign, stockWording)
    }

    private fun renderCampaignInactive(price: String) = with(view) {
        tv_price_pdp.text = context.getString(R.string.template_price, "", price)
        text_original_price.gone()
        text_discount.gone()
        discount_timer_holder.gone()
        sale_text_stock_available.gone()
    }

    private fun renderStockAvailable(campaign: CampaignModular, isVariant: Boolean, stockWording: String, isProductActive: Boolean) = with(view) {
        text_stock_available.text = MethodChecker.fromHtml(stockWording)
        text_stock_available.showWithCondition(!campaign.activeAndHasId && !isVariant && stockWording.isNotEmpty() && isProductActive)
    }

    private fun hideGimmick(campaign: CampaignModular) = with(view) {
        if (campaign.hideGimmick) {
            text_original_price.visibility = View.GONE
            text_discount.visibility = View.GONE
        } else {
            text_original_price.visibility = View.VISIBLE
            text_discount.visibility = View.VISIBLE
        }
    }

    private fun renderFlashSale(campaign: CampaignModular, stockWording: String) = with(view) {
        if (campaign.shouldShowRibbonCampaign) {
            if (campaign.campaignID.toInt() > 0) {
                renderStockBarFlashSale(campaign, stockWording)
            } else {
                renderSlashPriceFlashSale()
            }
            text_title_discount_timer.text = campaign.campaignTypeName
            showCountDownTimer(campaign)
            discount_timer_holder.show()
        } else {
            discount_timer_holder.gone()
        }
    }

    private fun renderStockBarFlashSale(campaign: CampaignModular, stockWording: String) = with(view) {
        discount_timer_holder.setBackgroundColor(MethodChecker.getColor(view.context, R.color.Neutral_N50))
        showStockBarFlashSale()
        setProgressStockBar(campaign, stockWording)
    }

    private fun renderSlashPriceFlashSale() = with(view) {
        hideStockBarFlashSale()
        discount_timer_holder.setBackgroundColor(MethodChecker.getColor(view.context, R.color.white))
    }

    private fun hideProductCampaign(campaign: CampaignModular) = with(view) {
        tv_price_pdp.text = context.getString(R.string.template_price, "",
                campaign.originalPrice.getCurrencyFormatted())
        discount_timer_holder.gone()
        text_discount.gone()
        text_original_price.gone()
        sale_text_stock_available.gone()
        text_stock_available.show()
    }

    fun renderCod(showCod: Boolean) = with(view) {
        cod_header_container.showWithCondition(showCod)
        cod_header_container.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(view.context, R.drawable.ic_cod_white), null, null, null)
    }

    fun renderTradein(showTradein: Boolean) = with(view) {
        tradein_header_container.showWithCondition(showTradein)
        tradein_header_container.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(view.context, R.drawable.tradein_white), null, null, null)
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
            val drawableOs = MethodChecker.getDrawable(context, R.drawable.ic_pdp_new_official_store)
            drawableOs?.setBounds(0, 0, drawableSize, drawableSize)
            labelIc = context.getString(R.string.from_official_store_label)
            imageIc = ImageSpan(drawableOs, ImageSpan.ALIGN_BOTTOM)
            colorIc = ContextCompat.getColor(context, R.color.purple_official_store)
            renderTxtIcon(labelIc, colorIc, imageIc)
        } else {
            view.label_official_store.gone()
        }
    }

    private fun renderTxtIcon(labelIc: String, colorIc: Int, imageIc: ImageSpan) = with(view.label_official_store) {
        val blackString = context.getString(R.string.product_from) + "  "
        val startSpan = blackString.length
        val spanText = android.text.SpannableString(blackString + "   " +
                labelIc)

        spanText.setSpan(imageIc, startSpan, startSpan + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanText.setSpan(
                android.text.style.ForegroundColorSpan(colorIc),
                startSpan, spanText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanText.setSpan(StyleSpan(Typeface.BOLD),
                startSpan, spanText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        setText(spanText, android.widget.TextView.BufferType.SPANNABLE)

        visible()
    }

    private fun showCountDownTimer(campaign: CampaignModular) = with(view) {
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val endDateTimeMs = campaign.getEndDataInt * ONE_SECOND
            val now = System.currentTimeMillis()
            val endDate = dateFormat.parse(campaign.endDate)
            val delta = endDate.time - endDateTimeMs

            if (TimeUnit.MILLISECONDS.toDays(endDate.time - now) < 1) {
                count_down.setup(delta, endDate) {
                    hideProductCampaign(campaign)
                    listener.showAlertCampaignEnded()
                }
                discount_timer_holder.visible()
            } else {
                view.layout_discount_timer.gone()
            }
        } catch (ex: Exception) {
            discount_timer_holder.hide()
        }
    }

    private fun setProgressStockBar(campaign: CampaignModular, stockWording: String) = with(view) {
        try {
            sale_text_stock_available.text = MethodChecker.fromHtml(stockWording)
            stock_bar_sold_product.progress = campaign.stockSoldPercentage
        } catch (ex: Exception) {
            stock_bar_sold_product.hide()
        }
    }

    private fun showStockBarFlashSale() = with(view) {
        stock_bar_sold_product.show()
        sale_text_stock_available.show()
    }

    private fun hideStockBarFlashSale() = with(view) {
        stock_bar_sold_product.hide()
        sale_text_stock_available.hide()
    }
}