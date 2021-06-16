package com.tokopedia.product.detail.view.fragment.partialview

import android.graphics.Paint
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.CampaignModular
import com.tokopedia.product.detail.data.model.datamodel.ProductContentMainData
import com.tokopedia.product.detail.common.getCurrencyFormatted
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.widget.CampaignRibbon
import kotlinx.android.synthetic.main.item_product_content.view.*

/**
 * Created by Yehezkiel on 25/05/20
 */
class PartialContentView(private val view: View, private val listener: DynamicProductDetailListener) : CampaignRibbon.CampaignCountDownCallback {
    private var campaignRibbon: CampaignRibbon? = null

    fun renderData(data: ProductContentMainData,
                   isUpcomingNplType: Boolean, freeOngkirImgUrl: String) = with(view) {
        txt_main_price.contentDescription = context.getString(R.string.content_desc_txt_main_price, data.price.value)
        product_name.contentDescription = context.getString(R.string.content_desc_product_name, MethodChecker.fromHtml(data.productName))
        product_name.text = MethodChecker.fromHtml(data.productName)

        view.img_free_ongkir.shouldShowWithAction(freeOngkirImgUrl.isNotEmpty()) {
            view.img_free_ongkir.loadIcon(freeOngkirImgUrl)
        }

        text_cashback_green.shouldShowWithAction(data.cashbackPercentage > 0) {
            text_cashback_green.text = context.getString(R.string.template_cashback, data.cashbackPercentage.toString())
        }

        campaignRibbon = findViewById(R.id.campaign_ribbon)
        campaignRibbon?.setCampaignCountDownCallback(this@PartialContentView)
        campaignRibbon?.setDynamicProductDetailListener(listener)

        when {
            isUpcomingNplType -> {
                if (data.campaign.campaignIdentifier == CampaignRibbon.NO_CAMPAIGN || data.campaign.campaignIdentifier == CampaignRibbon.THEMATIC_CAMPAIGN) {
                    renderCampaignInactiveNpl(data.price.priceFmt)
                } else {
                    setTextCampaignActive(data.campaign)
                }
                campaignRibbon?.hide()
            }
            // no campaign
            data.campaign.campaignIdentifier == CampaignRibbon.NO_CAMPAIGN -> {
                renderCampaignInactive(data.price.priceFmt)
                campaignRibbon?.hide()
            }
            // thematic only
            data.campaign.campaignIdentifier == CampaignRibbon.THEMATIC_CAMPAIGN -> {
                campaignRibbon?.renderOnGoingCampaign(data)
                renderCampaignInactive(data.price.priceFmt)
            }
            else -> {
                campaignRibbon?.renderOnGoingCampaign(data)
                setTextCampaignActive(data.campaign)
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

    fun renderFreeOngkir(freeOngkirUrl: String) = with(view) {
        img_free_ongkir.shouldShowWithAction(freeOngkirUrl.isNotEmpty()) {
            view.img_free_ongkir.loadIcon(freeOngkirUrl) {
                fitCenter()
            }
        }
    }

    private fun renderCampaignInactive(price: String) = with(view) {
        txt_main_price.text = price
        text_slash_price.gone()
        text_discount_red.gone()
    }

    private fun renderCampaignInactiveNpl(price: String) = with(view) {
        txt_main_price.text = price
        text_slash_price.gone()
        text_discount_red.gone()
        campaignRibbon?.show()
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
            text = context.getString(com.tokopedia.product.detail.common.R.string.template_campaign_off, campaign.percentageAmount.toString())
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
        campaignRibbon?.hide()
        text_discount_red.gone()
        text_slash_price.gone()
        text_stock_available.show()
    }

    fun renderTradein(showTradein: Boolean) = with(view) {
        tradein_header_container.shouldShowWithAction(showTradein) {
            tradein_header_container.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(view.context, com.tokopedia.common_tradein.R.drawable.tradein_white), null, null, null)
        }
    }

    override fun onOnGoingCampaignEnded(campaign: CampaignModular) {
        hideProductCampaign(campaign = campaign)
    }
}