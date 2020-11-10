package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.uimodel.NotificationTopAdsBannerUiModel
import com.tokopedia.topads.sdk.widget.TopAdsImageView

class NotificationTopAdsBannerViewHolder constructor(
        itemView: View?
) : AbstractViewHolder<NotificationTopAdsBannerUiModel>(itemView) {

    private val adView: TopAdsImageView? = itemView?.findViewById(R.id.notifcenter_topads_banner)

    override fun bind(element: NotificationTopAdsBannerUiModel) {
        bindAd(element)
    }

    private fun bindAd(element: NotificationTopAdsBannerUiModel) {
        adView?.loadImage(element.ad)
    }

    companion object {
        val LAYOUT = R.layout.item_notifcenter_top_ads_banner
    }
}