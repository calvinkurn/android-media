package com.tokopedia.product.detail.view.viewholder.campaign.ui

import android.graphics.drawable.Drawable
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.extensions.getDrawableChecker
import com.tokopedia.product.detail.common.utils.extensions.addOnImpressionListener
import com.tokopedia.product.detail.databinding.ItemCampaignBinding
import com.tokopedia.product.detail.view.listener.ProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ProductDetailPageViewHolder
import com.tokopedia.product.detail.view.viewholder.campaign.ui.model.OngoingCampaignUiModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.common_tradein.R as common_tradeinR
import com.tokopedia.product.detail.R as productdetailR

class OngoingCampaignViewHolder(
    view: View,
    private val listener: ProductDetailListener
) : ProductDetailPageViewHolder<OngoingCampaignUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_campaign
    }

    private val binding = ItemCampaignBinding.bind(view)
    private val campaignRibbon = binding.pdpCampaignRibbon
    private val tradeIn = binding.pdpTradein

    // trade in attributes
    private val tradeInDrawableStart by lazy {
        MethodChecker.getDrawable(view.context, common_tradeinR.drawable.tradein_white)
    }
    private val tradeInBackground by lazy {
        view.context.getDrawableChecker(
            productdetailR.drawable.bg_tradein_half_rounded
        )
    }

    init {
        campaignRibbon.init(
            onCampaignEnded = {
                campaignRibbon.hideComponent()
                listener.showAlertCampaignEnded()
            },
            onRefreshPage = listener::refreshPage
        )
    }

    override fun bind(element: OngoingCampaignUiModel) {
        setCampaign(element = element)
        setTradeIn(element = element)
        setImpression(element = element)
    }

    override fun bind(element: OngoingCampaignUiModel?, payloads: MutableList<Any>) {
        val newElement = element ?: return
        val payloadId = payloads.firstOrNull() ?: return

        if (payloadId == OngoingCampaignUiModel.PAYLOAD_WISHLIST) {
            setCampaign(element = newElement)
            setTradeIn(element = newElement)
        }
    }

    // region campaign
    private fun setCampaign(element: OngoingCampaignUiModel) = with(campaignRibbon) {
        if (!element.shouldShowCampaign) {
            hideComponent()
            return@with
        }
        setData(onGoingData = element.data, upComingData = null)
        showComponent()
    }
    // endregion campaign

    // region trade-in
    private fun setTradeIn(element: OngoingCampaignUiModel) = with(tradeIn) {
        if (element.shouldShowTradeIn) {
            showTradeIn()
            setTradeInClick(element = element)
        } else {
            hideTradeIn()
        }
    }

    private fun setTradeInClick(element: OngoingCampaignUiModel) {
        tradeIn.setOnClickListener {
            listener.txtTradeinClicked(getComponentTrackData(element))
        }
    }

    private fun Typography.showTradeIn() {
        background = tradeInBackground
        setDrawableStart(drawable = tradeInDrawableStart)
        show()
    }

    private fun Typography.hideTradeIn() {
        hide()
        background = null
        setDrawableStart(drawable = null)
    }

    private fun Typography.setDrawableStart(drawable: Drawable?) {
        setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
    }
    // endregion

    private fun setImpression(element: OngoingCampaignUiModel) {
        itemView.addOnImpressionListener(
            holder = element.impressHolder,
            holders = listener.getImpressionHolders(),
            name = element.data?.hashCode().toString(),
            useHolders = listener.isRemoteCacheableActive()
        ) {
            listener.onImpressComponent(getComponentTrackData(element))
        }
    }
}
