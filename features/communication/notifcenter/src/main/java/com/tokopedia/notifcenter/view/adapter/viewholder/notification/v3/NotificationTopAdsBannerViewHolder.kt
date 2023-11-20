package com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.uimodel.NotificationTopAdsBannerUiModel
import com.tokopedia.topads.sdk.utils.TdnHelper
import com.tokopedia.topads.sdk.widget.TdnBannerView
import com.tokopedia.unifycomponents.toPx

class NotificationTopAdsBannerViewHolder constructor(
    itemView: View?
) : AbstractViewHolder<NotificationTopAdsBannerUiModel>(itemView) {

    private val adView: TdnBannerView? = itemView?.findViewById(R.id.notifcenter_topads_banner)

    override fun bind(element: NotificationTopAdsBannerUiModel) {

        val tdnBannerList = TdnHelper.categoriesTdnBanners(element.ads)

        if (!tdnBannerList.isNullOrEmpty()) {
            adView?.renderTdnBanner(tdnBannerList.first(), 8.toPx(), onTdnBannerClicked = {
                if (it.isNotEmpty()) RouteManager.route(adView.context, it)
            })
        }
    }

    companion object {
        val LAYOUT = R.layout.item_notifcenter_top_ads_banner
    }
}
