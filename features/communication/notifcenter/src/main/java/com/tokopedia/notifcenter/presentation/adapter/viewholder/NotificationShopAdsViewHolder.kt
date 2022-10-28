package com.tokopedia.notifcenter.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.model.NotifTopAdsHeadline
import com.tokopedia.topads.sdk.widget.TopAdsHeadlineView

class NotificationShopAdsViewHolder(itemView: View?) :
    AbstractViewHolder<NotifTopAdsHeadline>(itemView) {

    private val notifCenterTopAdsHeadline: TopAdsHeadlineView? =
        itemView?.findViewById(R.id.notifCenterTopAdsHeadline)

    override fun bind(element: NotifTopAdsHeadline) {
        notifCenterTopAdsHeadline?.let {
            notifCenterTopAdsHeadline.hideShimmerView()
            notifCenterTopAdsHeadline.displayAds(element.cpmModel)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_notifcenter_topads_headline_view
    }
}

