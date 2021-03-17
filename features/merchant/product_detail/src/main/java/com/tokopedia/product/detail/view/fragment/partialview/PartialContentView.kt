package com.tokopedia.product.detail.view.fragment.partialview

import android.graphics.Paint
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.CampaignModular
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductContentMainData
import com.tokopedia.product.detail.data.model.datamodel.UpcomingNplDataModel
import com.tokopedia.product.detail.data.util.getCurrencyFormatted
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.widget.CampaignRibbon
import kotlinx.android.synthetic.main.item_product_content.view.*

/**
 * Created by Yehezkiel on 25/05/20
 */
class PartialContentView(private val view: View, private val listener: DynamicProductDetailListener) : CampaignRibbon.CampaignCountDownCallback {
    companion object {
    }

    fun renderData(data: ProductContentMainData,
                   isUpcomingNplType: Boolean,
                   upcomingNplData: UpcomingNplDataModel) = with(view) {
        txt_main_price.contentDescription = context.getString(R.string.content_desc_txt_main_price, data.price.value)
        product_name.contentDescription = context.getString(R.string.content_desc_product_name, MethodChecker.fromHtml(data.productName))
        product_name.text = MethodChecker.fromHtml(data.productName)

        img_free_ongkir.shouldShowWithAction(data.freeOngkir.isActive) {
            Glide.with(view.context)
                    .load(data.freeOngkir.imageURL)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .fitCenter()
                    .into(view.img_free_ongkir)
        }

        text_cashback_green.shouldShowWithAction(data.cashbackPercentage > 0) {
            text_cashback_green.text = context.getString(R.string.template_cashback, data.cashbackPercentage.toString())
        }

        val campaignRibbon = campaign_ribbon
        campaignRibbon.setCampaignCountDownCallback(this@PartialContentView)
        campaignRibbon.setDynamicProductDetailListener(listener)

        when {
            isUpcomingNplType -> {
                renderCampaignInactiveNpl(data.price.priceFmt)
            }
            // campaign identifier = 0
            data.campaign.campaignIdentifier == 0 -> {
                renderCampaignInactive(data.price.priceFmt)
            }
            else -> {
                campaignRibbon.renderOnGoingCampaign(data)
                renderCampaignActive(data.campaign, data.stockWording)
            }
        }

        renderStockAvailable(data.campaign, data.isVariant, data.stockWording, data.isProductActive)
    }

    fun updateWishlist(wishlisted: Boolean, shouldShowWishlist: Boolean) = with(view) {
        if (!shouldShowWishlist) {
            fab_detail_pdp.hide()
        } else {
            if (wishlisted) {
                fab_detail_pdp.hide()
                fab_detail_pdp.isActivated = true
                fab_detail_pdp.setImageDrawable(MethodChecker.getDrawable(context, R.drawable.ic_pdp_wishlist_filled))
                fab_detail_pdp.show()
            } else {
                fab_detail_pdp.hide()
                fab_detail_pdp.isActivated = false
                fab_detail_pdp.setImageDrawable(MethodChecker.getDrawable(context, R.drawable.ic_pdp_wishlist_unfilled))
                fab_detail_pdp.show()
            }
        }
    }

    fun renderShareButton(componentTrackDataModel: ComponentTrackDataModel?) = with(view) {
        if (!listener.isNavOld()) {
            share_product_pdp.show()
            share_product_pdp.setOnClickListener {
                listener.shareProductFromContent(componentTrackDataModel)
            }
        } else {
            share_product_pdp.hide()
        }
    }

    private fun renderCampaignActive(campaign: CampaignModular, stockWording: String) = with(view) {
        setTextCampaignActive(campaign)
        campaign_ribbon.show()
    }

    private fun renderCampaignInactive(price: String) = with(view) {
        txt_main_price.text = price
        text_slash_price.gone()
        text_discount_red.gone()
        campaign_ribbon.gone()
    }

    private fun renderCampaignInactiveNpl(price: String) = with(view) {
        txt_main_price.text = price
        text_slash_price.gone()
        text_discount_red.gone()
        campaign_ribbon.show()
    }

    private fun setTextCampaignActive(campaign: CampaignModular) = with(view) {
        txt_main_price?.run {
            text = context.getString(R.string.template_price, "",
                    campaign.discountedPriceFmt)
            show()
        }

        text_slash_price?.run {
            text = context.getString(R.string.template_price, "",
                    campaign.originalPriceFmt)
            paintFlags = text_slash_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            show()
        }

        text_discount_red?.run {
            text = context.getString(R.string.template_campaign_off, campaign.percentageAmount.toString())
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

    private fun hideProductCampaign(campaign: CampaignModular) = with(view) {
        txt_main_price.text = context.getString(R.string.template_price, "",
                campaign.originalPrice.getCurrencyFormatted())
        campaign_ribbon.hide()
        text_discount_red.gone()
        text_slash_price.gone()
        text_stock_available.show()
    }

    fun renderTradein(showTradein: Boolean) = with(view) {
        tradein_header_container.showWithCondition(showTradein)
        tradein_header_container.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(view.context, com.tokopedia.common_tradein.R.drawable.tradein_white), null, null, null)
    }

    override fun onOnGoingCampaignEnded(campaign: CampaignModular) {
        hideProductCampaign(campaign = campaign)
    }
}