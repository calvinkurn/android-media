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
                campaignRibbon.setLayoutHeight(Int.ZERO)
                listener.showAlertCampaignEnded()
            },
            onRefreshPage = listener::refreshPage
        )
    }

    override fun bind(element: OngoingCampaignUiModel) = with(campaignRibbon) {
        if (element.shouldShow) {
            campaignRibbon.setLayoutHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
            campaignRibbon.setData(onGoingData = element.data, upComingData = null)

            itemView.addOnImpressionListener(
                holder = element.impressHolder,
                holders = listener.getImpressionHolders(),
                name = element.data?.hashCode().toString(),
                useHolders = listener.isRemoteCacheableActive()
            ) {
                listener.onImpressComponent(getComponentTrackData(element))
            }
        } else {
            campaignRibbon.setLayoutHeight(Int.ZERO)
        }
    }
}
