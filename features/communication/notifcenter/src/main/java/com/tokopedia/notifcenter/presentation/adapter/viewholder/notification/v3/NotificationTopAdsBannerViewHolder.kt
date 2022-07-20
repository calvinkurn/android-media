package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.uimodel.NotificationTopAdsBannerUiModel
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewImpressionListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.widget.TopAdsImageView
import com.tokopedia.unifycomponents.toPx

class NotificationTopAdsBannerViewHolder constructor(
        itemView: View?
) : AbstractViewHolder<NotificationTopAdsBannerUiModel>(itemView) {

    private val adView: TopAdsImageView? = itemView?.findViewById(R.id.notifcenter_topads_banner)

    override fun bind(element: NotificationTopAdsBannerUiModel) {
        bindAd(element)
        bindClick()
        bindImpression(element)
    }

    private fun bindClick() {
        adView?.setTopAdsImageViewClick(object : TopAdsImageViewClickListener {
            override fun onTopAdsImageViewClicked(applink: String?) {
                if (applink == null) return
                RouteManager.route(adView.context, applink)
            }
        })
    }

    private fun bindImpression(element: NotificationTopAdsBannerUiModel) {
        adView?.setTopAdsImageViewImpression(object : TopAdsImageViewImpressionListener {
            override fun onTopAdsImageViewImpression(viewUrl: String) {
                if (!element.impressHolder.isInvoke) {
                    hitTopAdsImpression(viewUrl)
                    element.impressHolder.invoke()
                }
            }
        })
    }

    private fun bindAd(element: NotificationTopAdsBannerUiModel) {
        adView?.loadImage(element.ad, 8.toPx())
    }

    private fun hitTopAdsImpression(viewUrl: String) {
        itemView.context.let {
            TopAdsUrlHitter(it).hitImpressionUrl(
                    this::class.java.simpleName,
                    viewUrl,
                    "",
                    "",
                    ""
            )
        }
    }

    companion object {
        val LAYOUT = R.layout.item_notifcenter_top_ads_banner
    }
}