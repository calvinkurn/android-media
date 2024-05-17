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
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductContentMainData
import com.tokopedia.product.detail.databinding.ItemDynamicProductContentBinding
import com.tokopedia.product.detail.view.listener.ProductDetailListener
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.CampaignRibbon
import com.tokopedia.product.detail.view.widget.productNameDelegate
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.product.detail.common.R as productdetailcommonR

/**
 * Created by Yehezkiel on 25/05/20
 */
class PartialContentView(
    private val binding: ItemDynamicProductContentBinding
) {

    private val context = binding.root.context
    private val productNameDelegate by binding.productNameDelegate()

    fun renderData(
        data: ProductContentMainData,
        isUpcomingNplType: Boolean,
        freeOngkirImgUrl: String,
        listener: ProductDetailListener,
        componentTrackData: ComponentTrackDataModel
    ) = with(binding) {
        txtMainPrice.contentDescription =
            context.getString(R.string.content_desc_txt_main_price, data.price.value)
        productName.contentDescription = context.getString(
            R.string.content_desc_product_name,
            MethodChecker.fromHtml(data.productName)
        )

        renderProductName(data = data, listener = listener, componentTrackData = componentTrackData)

        renderPriceCampaignSection(
            data = data,
            isUpcomingNplType = isUpcomingNplType,
            freeOngkirImgUrl = freeOngkirImgUrl
        )

        renderStockAvailable(data.campaign, data.isVariant, data.stockWording, data.isProductActive)
    }

    fun updateWishlist(wishlisted: Boolean, shouldShowWishlist: Boolean) =
        with(binding.fabDetailPdp) {
            showWithCondition(shouldShowWishlist)
            if (shouldShowWishlist && activeState != wishlisted) {
                activeState = wishlisted
            }
        }

    fun renderFreeOngkir(freeOngkirImgUrl: String, isShowPrice: Boolean) = with(binding) {
        imgFreeOngkir.shouldShowWithAction(freeOngkirImgUrl.isNotEmpty() && isShowPrice) {
            imgFreeOngkir.loadImageWithoutPlaceholder(freeOngkirImgUrl)
        }
    }

    private fun renderProductName(
        data: ProductContentMainData,
        listener: ProductDetailListener,
        componentTrackData: ComponentTrackDataModel
    ) = with(binding) {
        productNameDelegate.setTitle(
            title = data.productName,
            labelIcons = data.labelIcons,
            collapse = data.productNameCollapsed,
            listener = listener,
            componentTrackData = componentTrackData
        )

        pdpContentContainer.post {
            if (productName.lineCount == 2) {
                pdpContentContainer.setPadding(0, 0, 0, 6.toPx())
            } else {
                pdpContentContainer.setPadding(0, 0, 0, 2.toPx())
            }
        }
    }

    private fun renderPriceCampaignSection(
        data: ProductContentMainData,
        isUpcomingNplType: Boolean,
        freeOngkirImgUrl: String
    ) {
        if (data.isShowPrice) {
            showPriceSection(
                data = data,
                isUpcomingNplType = isUpcomingNplType,
                cashbackPercentage = data.cashbackPercentage,
                freeOngkirImgUrl = freeOngkirImgUrl
            )
        } else {
            // means we are rendering promo price in different component
            hidePriceSection()
        }
    }

    private fun showPriceSection(
        data: ProductContentMainData,
        isUpcomingNplType: Boolean,
        cashbackPercentage: Int,
        freeOngkirImgUrl: String
    ) = with(binding) {
        txtMainPrice.show()
        imgFreeOngkir.show()
        discountContainer.show()

        renderCampaignPrice(
            data = data,
            isUpcomingNplType = isUpcomingNplType
        )
        renderCashBackSection(cashbackPercentage)
        renderFreeOngkir(
            freeOngkirImgUrl = freeOngkirImgUrl,
            isShowPrice = data.isShowPrice
        )
    }

    private fun renderCashBackSection(cashbackPercentage: Int) = with(binding) {
        textCashbackGreen.shouldShowWithAction(cashbackPercentage > 0) {
            textCashbackGreen.text = context.getString(
                productdetailcommonR.string.template_cashback,
                cashbackPercentage.toString()
            )
        }
    }

    private fun renderCampaignPrice(
        data: ProductContentMainData,
        isUpcomingNplType: Boolean
    ) {
        when {
            isUpcomingNplType -> {
                if (data.campaign.campaignIdentifier == CampaignRibbon.NO_CAMPAIGN ||
                    data.campaign.campaignIdentifier == CampaignRibbon.THEMATIC_CAMPAIGN
                ) {
                    renderCampaignInactiveNpl(data.price.priceFmt)
                } else {
                    setTextCampaignActive(data.campaign)
                }
            }
            // no campaign
            data.campaign.campaignIdentifier == CampaignRibbon.NO_CAMPAIGN -> {
                renderCampaignInactive(data.price.priceFmt)
            }
            // thematic only
            data.campaign.campaignIdentifier == CampaignRibbon.THEMATIC_CAMPAIGN -> {
                renderCampaignInactive(data.price.priceFmt)
            }

            else -> {
                setTextCampaignActive(data.campaign)
            }
        }
    }

    private fun hidePriceSection() = with(binding) {
        txtMainPrice.hide()
        imgFreeOngkir.hide()
        discountContainer.hide()
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

    private fun renderStockAvailable(
        campaign: CampaignModular,
        isVariant: Boolean,
        stockWording: String,
        isProductActive: Boolean
    ) = with(binding) {
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

    fun onViewRecycled() {
    }
}
