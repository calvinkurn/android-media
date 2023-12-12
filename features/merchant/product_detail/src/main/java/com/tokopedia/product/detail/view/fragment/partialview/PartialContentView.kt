package com.tokopedia.product.detail.view.fragment.partialview

import android.graphics.Paint
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.CampaignModular
import com.tokopedia.product.detail.data.model.datamodel.ProductContentMainData
import com.tokopedia.product.detail.databinding.ItemProductContentBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.widget.CampaignRibbon
import com.tokopedia.common_tradein.R as common_tradeinR
import com.tokopedia.product.detail.common.R as productdetailcommonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by Yehezkiel on 25/05/20
 */
class PartialContentView(
    private val view: View,
    private val listener: DynamicProductDetailListener
) : CampaignRibbon.CampaignCountDownCallback {

    private val context = view.context
    private val binding = ItemProductContentBinding.bind(view)

    fun renderData(
        data: ProductContentMainData,
        isUpcomingNplType: Boolean,
        freeOngkirImgUrl: String,
        shouldShowCampaign: Boolean
    ) = with(binding) {
        txtMainPrice.contentDescription = context.getString(R.string.content_desc_txt_main_price, data.price.value)
        productName.contentDescription = context.getString(R.string.content_desc_product_name, MethodChecker.fromHtml(data.productName))
        productName.text = MethodChecker.fromHtml(data.productName)

        renderFreeOngkir(freeOngkirImgUrl)

        textCashbackGreen.shouldShowWithAction(data.cashbackPercentage > 0) {
            textCashbackGreen.text = context.getString(productdetailcommonR.string.template_cashback, data.cashbackPercentage.toString())
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

        if (!shouldShowCampaign) {
            campaignRibbon.hide()
        }
    }

    fun updateWishlist(wishlisted: Boolean, shouldShowWishlist: Boolean) = with(binding.fabDetailPdp) {
        showWithCondition(shouldShowWishlist)
        if (shouldShowWishlist && activeState != wishlisted) {
            activeState = wishlisted
        }
    }

    fun renderFreeOngkir(freeOngkirUrl: String) = with(binding) {
        imgFreeOngkir.shouldShowWithAction(freeOngkirUrl.isNotEmpty()) {
            imgFreeOngkir.loadImageWithoutPlaceholder(freeOngkirUrl)
        }
    }

    fun updateUniversalShareWidget(shouldShow: Boolean) = with(binding.universalShareWidget) {
        if (shouldShow) {
            listener.onUniversalShareWidget(this)
            setColorShareIcon(unifyprinciplesR.color.Unify_NN700)
            show()
        } else {
            hide()
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
            text = campaign.priceFmt
            show()
        }

        textSlashPrice.run {
            text = campaign.slashPriceFmt
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            show()
        }

        textDiscountRed.run {
            text = campaign.discPercentageFmt
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
        txtMainPrice.text = campaign.slashPriceFmt
        campaignRibbon.hide()
        textDiscountRed.gone()
        textSlashPrice.gone()
        textStockAvailable.show()
    }

    fun renderTradein(showTradein: Boolean) = with(binding) {
        tradeinHeaderContainer.shouldShowWithAction(showTradein) {
            tradeinHeaderContainer.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(view.context, common_tradeinR.drawable.tradein_white), null, null, null)
        }
    }

    override fun onOnGoingCampaignEnded(campaign: CampaignModular) {
        hideProductCampaign(campaign = campaign)
    }
}
