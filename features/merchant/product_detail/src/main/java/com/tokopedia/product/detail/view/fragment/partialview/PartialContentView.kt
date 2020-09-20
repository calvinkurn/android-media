package com.tokopedia.product.detail.view.fragment.partialview

import android.graphics.Paint
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.CampaignModular
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.data.model.datamodel.UpcomingNplDataModel
import com.tokopedia.product.detail.data.util.getCurrencyFormatted
import com.tokopedia.product.detail.data.util.numberFormatted
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.isGivenDateIsBelowThan24H
import com.tokopedia.product.detail.view.viewholder.ProductNotifyMeViewHolder
import kotlinx.android.synthetic.main.item_product_content.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Yehezkiel on 25/05/20
 */
class PartialContentView(private val view: View,
                         private val listener: DynamicProductDetailListener) {
    companion object {
        const val ONE_SECOND = 1000L
    }

    fun renderData(product: DynamicProductInfoP1, isUpcomingNplType: Boolean, upcomingNplData: UpcomingNplDataModel) = with(view) {
        val data = product.data
        val basic = product.basic
        val campaign = data.campaign

        product_name.text = MethodChecker.fromHtml(data.name)

        img_free_ongkir.shouldShowWithAction(data.isFreeOngkir.isActive) {
            ImageHandler.loadImageRounded2(context, img_free_ongkir, data.isFreeOngkir.imageURL)
        }

        text_cashback_green.shouldShowWithAction(data.isCashback.percentage > 0) {
            text_cashback_green.text = context.getString(R.string.template_cashback, data.isCashback.percentage.toString())
        }

        when {
            isUpcomingNplType -> {
                renderNplRibbon(upcomingNplData.ribbonCopy, upcomingNplData.startDate, campaign)
                renderCampaignInactiveNpl(data.price.value.getCurrencyFormatted())
            }
            campaign.isActive -> {
                renderCampaignActive(campaign, data.stock.stockWording)
            }
            else -> {
                renderCampaignInactive(data.price.value.getCurrencyFormatted())
            }
        }

        renderStockAvailable(campaign, data.variant.isVariant, data.stock.stockWording, basic.isActive())
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
        setTextCampaignActive(campaign)
        renderFlashSale(campaign, stockWording)
    }

    private fun renderCampaignInactive(price: String) = with(view) {
        txt_main_price.text = price
        text_slash_price.gone()
        text_discount_red.gone()
        discount_timer_holder.gone()
    }

    private fun renderCampaignInactiveNpl(price: String) = with(view) {
        txt_main_price.text = price
        discount_timer_holder.show()
        text_slash_price.gone()
        text_discount_red.gone()
    }

    private fun setTextCampaignActive(campaign: CampaignModular) = with(view) {
        txt_main_price?.run {
            text = context.getString(R.string.template_price, "",
                    campaign.discountedPrice.getCurrencyFormatted())
            show()
        }

        text_slash_price?.run {
            text = context.getString(R.string.template_price, "",
                    campaign.originalPrice.getCurrencyFormatted())
            paintFlags = text_slash_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            show()
        }

        text_discount_red?.run {
            text = context.getString(R.string.template_campaign_off, campaign.percentageAmount.numberFormatted())
            show()
        }
        hideGimmick(campaign)
    }

    private fun renderStockAvailable(campaign: CampaignModular, isVariant: Boolean, stockWording: String, isProductActive: Boolean) = with(view) {
        text_stock_available.text = MethodChecker.fromHtml(stockWording)
        text_stock_available.showWithCondition(!campaign.activeAndHasId && !isVariant && stockWording.isNotEmpty() && isProductActive)
    }

    private fun hideGimmick(campaign: CampaignModular) = with(view) {
        if (campaign.hideGimmick) {
            text_slash_price.visibility = View.GONE
            text_discount_red.visibility = View.GONE
        } else {
            text_slash_price.visibility = View.VISIBLE
            text_discount_red.visibility = View.VISIBLE
        }
    }

    private fun renderFlashSale(campaign: CampaignModular, stockWording: String) = with(view) {
        if (campaign.isCampaignNewUser && !campaign.shouldShowRibbonCampaign) {
            renderFlashSaleNewUserAbove24H(campaign, stockWording)
        } else if (campaign.shouldShowRibbonCampaign) {
            if (campaign.campaignID.toInt() > 0) {
                renderStockBarFlashSale(campaign, stockWording)
            } else {
                renderSlashPriceFlashSale()
            }
            showCountDownTimer(campaign)
            text_title_discount_timer.text = campaign.campaignTypeName
            discount_timer_holder.show()
        } else {
            discount_timer_holder.gone()
        }
    }

    private fun renderFlashSaleNewUserAbove24H(campaign: CampaignModular, stockWording: String) = with(view) {
        renderStockBarFlashSale(campaign, stockWording)
        count_down.hide()
        discount_timer_holder.show()
        text_title_discount_timer.text = campaign.campaignTypeName
    }

    private fun renderStockBarFlashSale(campaign: CampaignModular, stockWording: String) = with(view) {
        showStockBarFlashSale()
        discount_timer_holder.setBackgroundColor(MethodChecker.getColor(view.context, R.color.Neutral_N50))
        setProgressStockBar(campaign, stockWording)
    }

    private fun renderSlashPriceFlashSale() {
        hideStockBarAndBackgroundColor()
    }

    private fun renderNplRibbon(ribbonCopy: String, startDate: String, campaign: CampaignModular) = with(view) {
        if (startDate.isGivenDateIsBelowThan24H()) {
            text_title_discount_timer.text = context.getString(R.string.campaign_npl_start)
            showCountDownTimerUpcomingNpl(startDate, campaign)
        } else {
            text_title_discount_timer.text = MethodChecker.fromHtml(ribbonCopy)
            count_down.hide()
        }

        hideStockBarAndBackgroundColor()
        discount_timer_holder.show()
    }

    private fun hideProductCampaign(campaign: CampaignModular) = with(view) {
        txt_main_price.text = context.getString(R.string.template_price, "",
                campaign.originalPrice.getCurrencyFormatted())
        discount_timer_holder.gone()
        text_discount_red.gone()
        text_slash_price.gone()
        sale_text_stock_available.gone()
        text_stock_available.show()
    }

    fun renderTradein(showTradein: Boolean) = with(view) {
        tradein_header_container.showWithCondition(showTradein)
        tradein_header_container.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(view.context, R.drawable.tradein_white), null, null, null)
    }

    private fun showCountDownTimerUpcomingNpl(startDateData: String, campaign: CampaignModular) = with(view) {
        try {
            val now = System.currentTimeMillis()
            val startTime = startDateData.toLongOrZero() * ProductNotifyMeViewHolder.SECOND
            val startDate = Date(startTime)
            val delta = startDate.time - startTime

            if (TimeUnit.MILLISECONDS.toDays(startDate.time - now) < 1) {
                count_down.show()
                count_down.setup(delta, startDate) {
                    hideProductCampaign(campaign)
                    listener.showAlertCampaignEnded()
                }
                discount_timer_holder.show()
            } else {
                layout_discount_timer.gone()
            }
        } catch (e: Throwable) {
            discount_timer_holder.hide()
        }
    }

    private fun showCountDownTimer(campaign: CampaignModular) = with(view) {
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val endDateTimeMs = campaign.getEndDataInt * ONE_SECOND
            val now = System.currentTimeMillis()
            val endDate = dateFormat.parse(campaign.endDate)
            val delta = endDate.time - endDateTimeMs

            if (TimeUnit.MILLISECONDS.toDays(endDate.time - now) < 1) {
                count_down.show()
                count_down.setup(delta, endDate) {
                    hideProductCampaign(campaign)
                    listener.showAlertCampaignEnded()
                }
                discount_timer_holder.show()
            } else {
                layout_discount_timer.gone()
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

    private fun hideStockBarAndBackgroundColor() = with(view) {
        hideStockBarFlashSale()
        discount_timer_holder.setBackgroundColor(MethodChecker.getColor(view.context, R.color.white))
    }
}