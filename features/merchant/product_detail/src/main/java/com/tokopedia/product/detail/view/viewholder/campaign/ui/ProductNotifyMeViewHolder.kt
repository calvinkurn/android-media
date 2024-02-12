package com.tokopedia.product.detail.view.viewholder.campaign.ui

import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.setLayoutHeight
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.utils.extensions.addOnImpressionListener
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.databinding.PartialProductNotifyMeBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ProductDetailPageViewHolder
import com.tokopedia.product.detail.view.viewholder.campaign.ui.model.ProductNotifyMeUiModel
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.CampaignRibbon

class ProductNotifyMeViewHolder(
    private val view: View,
    private val listener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<ProductNotifyMeUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.partial_product_notify_me
    }

    private val binding = PartialProductNotifyMeBinding.bind(view)

    init {
        binding.upcomingCampaignRibbon.init(
            onCampaignEnded = {
                binding.upcomingCampaignRibbon.setLayoutHeight(Int.ZERO)
                listener.showAlertCampaignEnded()
            },
            onRefreshPage = listener::refreshPage,
            onRemindMeClick = { data, tracker ->
                listener.onNotifyMeClicked(data, tracker)
            }
        )
    }

    override fun bind(element: ProductNotifyMeUiModel) = with(binding.upcomingCampaignRibbon) {
        if (!element.shouldShow) {
            hideComponent()
            return@with
        }
        setupCampaignRibbon(element)
        showComponent()
        setImpression(element)
    }

    private fun CampaignRibbon.setImpression(
        element: ProductNotifyMeUiModel
    ) {
        addOnImpressionListener(
            holder = element.impressHolder,
            holders = listener.getImpressionHolders(),
            name = element.name,
            useHolders = listener.isRemoteCacheableActive()
        ) {
            listener.onImpressComponent(getComponentTrackData(element))
        }
    }

    private fun CampaignRibbon.setupCampaignRibbon(element: ProductNotifyMeUiModel) {
        setComponentTrackDataModel(getComponentTrackData(element))
        setData(
            upComingData = element.data.copy(isOwner = listener.isOwner()),
            onGoingData = null
        )
    }

    private fun CampaignRibbon.hideComponent() {
        setLayoutHeight(Int.ZERO)
    }

    private fun CampaignRibbon.showComponent() {
        setLayoutHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun bind(element: ProductNotifyMeUiModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty()) {
            return
        }
        when (payloads[0] as Int) {
            ProductDetailConstant.PAYLOAD_NOTIFY_ME -> {
                val campaignRibbon = binding.upcomingCampaignRibbon
                campaignRibbon.updateRemindMeButton(element.data.copy(isOwner = listener.isOwner()))
            }
        }
    }
}
