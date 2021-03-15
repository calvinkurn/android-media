package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductNotifyMeDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.partial_product_notify_me.view.*

class ProductNotifyMeViewHolder(view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductNotifyMeDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.partial_product_notify_me
    }

    override fun bind(element: ProductNotifyMeDataModel) {
        if (element.campaignID.isNotEmpty() && !element.isUpcomingNplType()) {
            itemView.layout_notify_me?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
            itemView.layout_notify_me?.requestLayout()
            // render upcoming campaign ribbon
            val campaignRibbon = itemView.layout_notify_me?.upcoming_campaign_ribbon
            val trackDataModel = ComponentTrackDataModel(element.type, element.name, adapterPosition + 1)
            campaignRibbon?.renderUpComingCampaign(element)
            campaignRibbon?.setDynamicProductDetailListener(listener)
            campaignRibbon?.setComponentTrackDataModel(trackDataModel)
        } else {
            hideContainer()
        }
    }

    private fun hideContainer() {
        itemView.layout_notify_me?.layoutParams?.height = 0
        itemView.layout_notify_me?.requestLayout()
    }

    override fun bind(element: ProductNotifyMeDataModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty()) {
            return
        }
        when (payloads[0] as Int) {
            ProductDetailConstant.PAYLOAD_NOTIFY_ME -> {
                val campaignRibbon = itemView.layout_notify_me?.upcoming_campaign_ribbon
                campaignRibbon?.updateRemindMeButton(listener, element)
            }
        }
    }
}
