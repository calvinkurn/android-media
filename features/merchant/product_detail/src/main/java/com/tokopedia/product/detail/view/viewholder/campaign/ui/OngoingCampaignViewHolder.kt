package com.tokopedia.product.detail.view.viewholder.campaign.ui

import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.setLayoutHeight
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.utils.extensions.addOnImpressionListener
import com.tokopedia.product.detail.databinding.ItemCampaignBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ProductDetailPageViewHolder
import com.tokopedia.product.detail.view.viewholder.campaign.ui.model.OngoingCampaignUiModel
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.CampaignRibbon

class OngoingCampaignViewHolder(
    view: View,
    private val listener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<OngoingCampaignUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_campaign
    }

    private val binding = ItemCampaignBinding.bind(view)
    private val campaignRibbon = binding.pdpCampaignRibbon

    init {
        campaignRibbon.init(
            onCampaignEnded = {
                campaignRibbon.hideComponent()
                listener.showAlertCampaignEnded()
            },
            onRefreshPage = listener::refreshPage
        )
    }

    override fun bind(element: OngoingCampaignUiModel) = with(campaignRibbon) {
        if (!element.shouldShow) {
            hideComponent()
            return@with
        }
        setData(onGoingData = element.data, upComingData = null)
        showComponent()
        setImpression(element)
    }

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

    private fun CampaignRibbon.showComponent() {
        setLayoutHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun CampaignRibbon.hideComponent() {
        setLayoutHeight(Int.ZERO)
    }
}
