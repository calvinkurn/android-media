package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductNotifyMeDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.widget.CampaignRibbon
import kotlinx.android.synthetic.main.partial_product_notify_me.view.*

class ProductNotifyMeViewHolder(view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductNotifyMeDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.partial_product_notify_me
    }

    override fun bind(element: ProductNotifyMeDataModel) {
        if (element.campaignID.isNotEmpty()) {
            showContainer()
            // render upcoming campaign ribbon
            val campaignRibbon = itemView.findViewById<CampaignRibbon>(R.id.upcoming_campaign_ribbon)
            val trackDataModel = ComponentTrackDataModel(element.type, element.name, adapterPosition + 1)
            campaignRibbon?.setDynamicProductDetailListener(listener)
            campaignRibbon?.setComponentTrackDataModel(trackDataModel)
            campaignRibbon?.renderUpComingCampaignRibbon(element, element.upcomingNplData.upcomingType)
        } else {
            hideContainer()
        }
    }

    private fun showContainer() = with(itemView) {
        upcoming_campaign_ribbon?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        upcoming_campaign_ribbon?.requestLayout()
    }

    private fun hideContainer() = with(itemView){
        upcoming_campaign_ribbon?.layoutParams?.height = 0
        upcoming_campaign_ribbon?.requestLayout()
    }

    override fun bind(element: ProductNotifyMeDataModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty()) {
            return
        }
        when (payloads[0] as Int) {
            ProductDetailConstant.PAYLOAD_NOTIFY_ME -> {
                val campaignRibbon = itemView.upcoming_campaign_ribbon
                campaignRibbon?.updateRemindMeButton(listener, element, element.campaignType)
            }
        }
    }
}
