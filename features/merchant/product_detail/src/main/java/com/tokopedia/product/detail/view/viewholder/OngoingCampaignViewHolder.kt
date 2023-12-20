package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.utils.extensions.addOnImpressionListener
import com.tokopedia.product.detail.data.model.datamodel.OngoingCampaignDataModel
import com.tokopedia.product.detail.databinding.ItemCampaignBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.widget.CampaignRibbon

class OngoingCampaignViewHolder(
    view: View,
    private val listener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<OngoingCampaignDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_campaign
    }

    private val binding = ItemCampaignBinding.bind(view)
    private val campaignRibbon = binding.pdpCampaignRibbon

    init {
        campaignRibbon.init(
            onCampaignEnded = {
                campaignRibbon.hide()
                listener.showAlertCampaignEnded()
            },
            onRefreshPage = listener::refreshPage
        )
    }

    override fun bind(element: OngoingCampaignDataModel) = with(campaignRibbon) {
        val data = element.data ?: return

        if (element.isNpl()) {
            campaignRibbon.hide()
        } else {
            when (data.campaign.campaignIdentifier) {
                CampaignRibbon.NO_CAMPAIGN -> campaignRibbon.hide()
                CampaignRibbon.THEMATIC_CAMPAIGN -> campaignRibbon.renderOnGoingCampaign(data)
                else -> campaignRibbon.renderOnGoingCampaign(data)
            }
        }

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
