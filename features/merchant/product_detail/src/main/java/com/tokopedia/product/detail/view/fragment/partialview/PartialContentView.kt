package com.tokopedia.product.detail.view.fragment.partialview

import android.graphics.Paint
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.CampaignModular
import com.tokopedia.product.detail.common.getCurrencyFormatted
import com.tokopedia.product.detail.data.model.datamodel.ProductContentMainData
import com.tokopedia.product.detail.databinding.ItemProductContentBinding
import com.tokopedia.product.detail.databinding.WidgetCampaignRibbonLayoutBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.widget.CampaignRibbon

/**
 * Created by Yehezkiel on 25/05/20
 */
class PartialContentView(private val view: View, private val listener: DynamicProductDetailListener) : CampaignRibbon.CampaignCountDownCallback {

    companion object {
        const val PRICE_FONT_SIZE = 24F
    }

    private val context = view.context
    private val binding = ItemProductContentBinding.bind(view)

    fun renderData(data: ProductContentMainData,
                   isUpcomingNplType: Boolean, freeOngkirImgUrl: String) = with(binding) {
        txtMainPrice.textSize = PRICE_FONT_SIZE
        txtMainPrice.contentDescription = context.getString(R.string.content_desc_txt_main_price, data.price.value)
        productName.contentDescription = context.getString(R.string.content_desc_product_name, MethodChecker.fromHtml(data.productName))
        productName.text = MethodChecker.fromHtml(data.productName)

        imgFreeOngkir.shouldShowWithAction(freeOngkirImgUrl.isNotEmpty()) {
            imgFreeOngkir.loadIcon(freeOngkirImgUrl)
        }

        textCashbackGreen.shouldShowWithAction(data.cashbackPercentage > 0) {
            textCashbackGreen.text = context.getString(com.tokopedia.product.detail.common.R.string.template_cashback, data.cashbackPercentage.toString())
        }

        campaignRibbon.setCampaignCountDownCallback(this@PartialContentView)
        campaignRibbon.setDynamicProductDetailListener(listener)

        when {
            isUpcomingNplType -> {
                if (data.campaign.campaignIdentifier == CampaignRibbon.NO_CAMPAIGN || data.campaign.campaignIdentifier == CampaignRibbon.THEMATIC_CAMPAIGN) {
                    renderCampaignInactiveNpl(data.price.priceFmt)
                } else {
                    setTextCampaignActive(data.campaign)
                }
                campaignRibbon.hide()
            }
            // no campaign
            data.campaign.campaignIdentifier == CampaignRibbon.NO_CAMPAIGN -> {
                renderCampaignInactive(data.price.priceFmt)
                campaignRibbon.hide()
            }
            // thematic only
            data.campaign.campaignIdentifier == CampaignRibbon.THEMATIC_CAMPAIGN -> {
                campaignRibbon.renderOnGoingCampaign(data)
                renderCampaignInactive(data.price.priceFmt)
            }
            else -> {
                campaignRibbon.renderOnGoingCampaign(data)
                setTextCampaignActive(data.campaign)
            }
        }

        renderStockAvailable(data.campaign, data.isVariant, data.stockWording, data.isProductActive)
    }

    fun updateWishlist(wishlisted: Boolean, shouldShowWishlist: Boolean) = with(binding.fabDetailPdp) {
        if (!shouldShowWishlist) { hide() }
        else {
            if (wishlisted) {
                hide()
                isActivated = true
                setImageDrawable(MethodChecker.getDrawable(context, R.drawable.ic_pdp_wishlist_filled))
                show()
            } else {
                hide()
                isActivated = false
                setImageDrawable(MethodChecker.getDrawable(context, R.drawable.ic_pdp_wishlist_unfilled))
                show()
            }
        }
    }

    fun renderFreeOngkir(freeOngkirUrl: String) = with(binding) {
        imgFreeOngkir.shouldShowWithAction(freeOngkirUrl.isNotEmpty()) {
            imgFreeOngkir.loadIcon(freeOngkirUrl) {
                fitCenter()
            }
        }
    }

    private fun renderCampaignInactive(price: String) = with(binding) {
        txtMainPrice.text = price
        textSlashPrice.gone()
        textDiscountRed.gone()
    }

    private fun renderCampaignInactiveNpl(price: String) = with(binding) {
        txtMainPrice.text = price
        textSlashPrice.gone()
        textDiscountRed.gone()
        campaignRibbon.show()
    }

    private fun setTextCampaignActive(campaign: CampaignModular) = with(binding) {
        txtMainPrice.run {
            text = context.getString(R.string.template_price, "",
                    campaign.discountedPriceFmt)
            show()
        }

        textSlashPrice.run {
            text = context.getString(R.string.template_price, "",
                    campaign.originalPriceFmt)
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            show()
        }

        textDiscountRed.run {
            text = context.getString(com.tokopedia.product.detail.common.R.string.template_campaign_off, campaign.percentageAmount.toString())
            show()
        }
        hideGimmick(campaign)
    }

    private fun renderStockAvailable(campaign: CampaignModular, isVariant: Boolean, stockWording: String, isProductActive: Boolean) = with(binding) {
        textStockAvailable.text = MethodChecker.fromHtml(stockWording)
        textStockAvailable.showWithCondition(!campaign.activeAndHasId && !isVariant && stockWording.isNotEmpty() && isProductActive)
    }

    private fun hideGimmick(campaign: CampaignModular) = with(binding) {
        if (campaign.hideGimmick) {
            textSlashPrice.visibility = View.GONE
            textDiscountRed.visibility = View.GONE
        } else {
            textSlashPrice.visibility = View.VISIBLE
            textDiscountRed.visibility = View.VISIBLE
        }
    }

    private fun hideProductCampaign(campaign: CampaignModular) = with(binding) {
        txtMainPrice.text = context.getString(R.string.template_price, "",
                campaign.originalPrice.getCurrencyFormatted())
        campaignRibbon.hide()
        textDiscountRed.gone()
        textSlashPrice.gone()
        textStockAvailable.show()
    }

    fun renderTradein(showTradein: Boolean) = with(binding) {
        tradeinHeaderContainer.shouldShowWithAction(showTradein) {
            tradeinHeaderContainer.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(view.context, com.tokopedia.common_tradein.R.drawable.tradein_white), null, null, null)
        }
    }

    override fun onOnGoingCampaignEnded(campaign: CampaignModular) {
        hideProductCampaign(campaign = campaign)
    }
}