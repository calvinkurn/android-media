package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.utils.extensions.addOnImpressionListener
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductNotifyMeDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.databinding.PartialProductNotifyMeBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

class ProductNotifyMeViewHolder(
    private val view: View,
    private val listener: DynamicProductDetailListener
) : AbstractViewHolder<ProductNotifyMeDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.partial_product_notify_me
    }

    private val binding = PartialProductNotifyMeBinding.bind(view)
    private val campaignRibbon by lazyThreadSafetyNone { binding.upcomingCampaignRibbon }

    init {
        campaignRibbon.init(
            onCampaignEnded = { listener.showAlertCampaignEnded() },
            onRefreshPage = listener::refreshPage,
            onRemindMeClick = { data, tracker ->
                listener.onNotifyMeClicked(data, tracker)
            }
        )
    }

    override fun bind(element: ProductNotifyMeDataModel) {
        if (element.campaignID.isNotEmpty()) {
            showContainer()
            // render upcoming campaign ribbon
            val trackDataModel = ComponentTrackDataModel(element.type, element.name, adapterPosition + 1)
            campaignRibbon.setComponentTrackDataModel(trackDataModel)
            campaignRibbon.renderUpComingCampaignRibbon(listener.isOwner().orFalse(), element, element.upcomingNplData.upcomingType)
            view.addOnImpressionListener(
                holder = element.impressHolder,
                holders = listener.getImpressionHolders(),
                name = element.name,
                useHolders = listener.isRemoteCacheableActive()
            ) {
                listener.onImpressComponent(getComponentTrackData(element))
            }
        } else {
            hideContainer()
        }
    }

    private fun showContainer() = with(binding) {
        upcomingCampaignRibbon.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        upcomingCampaignRibbon.requestLayout()
    }

    private fun hideContainer() = with(binding) {
        upcomingCampaignRibbon.layoutParams?.height = 0
        upcomingCampaignRibbon.requestLayout()
    }

    override fun bind(element: ProductNotifyMeDataModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty()) {
            return
        }
        when (payloads[0] as Int) {
            ProductDetailConstant.PAYLOAD_NOTIFY_ME -> {
                val campaignRibbon = binding.upcomingCampaignRibbon
                campaignRibbon.updateRemindMeButton(listener.isOwner().orFalse(), element, element.campaignType)
            }
        }
    }

    private fun getComponentTrackData(
        element: ProductNotifyMeDataModel
    ) = ComponentTrackDataModel(element.type, element.name, adapterPosition + 1)
}
