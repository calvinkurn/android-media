package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.CampaignModular
import com.tokopedia.product.detail.data.model.datamodel.OngoingCampaignDataModel
import com.tokopedia.product.detail.databinding.ItemCampaignBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.widget.CampaignRibbon

class OngoingCampaignViewHolder(
    view: View,
    private val listener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<OngoingCampaignDataModel>(view), CampaignRibbon.CampaignCountDownCallback {

    companion object {
        val LAYOUT = R.layout.item_campaign
    }

    private val binding = ItemCampaignBinding.bind(view)
    private val campaignRibbon = binding.pdpCampaignRibbon

    override fun bind(element: OngoingCampaignDataModel) = with(campaignRibbon) {
        val data = element.data ?: return

        setCampaignCountDownCallback(this@OngoingCampaignViewHolder)
        setDynamicProductDetailListener(listener)

        if (element.isNpl()) {
            campaignRibbon.hide()
        } else {
            when (data.campaign.campaignIdentifier) {
                CampaignRibbon.NO_CAMPAIGN -> campaignRibbon.hide()
                CampaignRibbon.THEMATIC_CAMPAIGN -> campaignRibbon.renderOnGoingCampaign(data)
                else -> campaignRibbon.renderOnGoingCampaign(data)
            }
        }

        itemView.addOnImpressionListener(element.impressHolder) {
            listener.onImpressComponent(getComponentTrackData(element))
        }
    }

    override fun onOnGoingCampaignEnded(campaign: CampaignModular) {
        campaignRibbon.hide()
    }
}
