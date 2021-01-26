package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.analytics.v2.RechargeBUWidgetTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.recharge_component.listener.RechargeBUWidgetListener
import com.tokopedia.recharge_component.model.RechargeBUWidgetDataModel
import com.tokopedia.recharge_component.model.WidgetSource

class RechargeBUWidgetCallback (val context: Context?,
                                val homeCategoryListener: HomeCategoryListener): RechargeBUWidgetListener {

    override fun onRechargeBUWidgetImpression(data: RechargeBUWidgetDataModel) {
        homeCategoryListener.getTrackingQueueObj()?.let { trackingQueue ->
            RechargeBUWidgetTracking.homeRechargeBUWidgetImpressionTracker(trackingQueue, data, homeCategoryListener.userId)
        }
    }

    override fun onRechargeBUWidgetBannerImpression(data: RechargeBUWidgetDataModel) {
        homeCategoryListener.getTrackingQueueObj()?.let { trackingQueue ->
            RechargeBUWidgetTracking.homeRechargeBUWidgetBannerImpressionTracker(trackingQueue, data, homeCategoryListener.userId)
        }
    }

    override fun onRechargeBUWidgetClickSeeAllButton(data: RechargeBUWidgetDataModel) {
        RechargeBUWidgetTracking.homeRechargeBUWidgetViewAllButtonClickTracker(data, homeCategoryListener.userId)
        context?.let {
            RouteManager.route(it, data.data.applink)
        }
    }

    override fun onRechargeBUWidgetClickSeeAllCard(data: RechargeBUWidgetDataModel) {
        RechargeBUWidgetTracking.homeRechargeBUWidgetViewAllCardClickTracker(data, homeCategoryListener.userId)
        context?.let {
            RouteManager.route(it, data.data.applink)
        }
    }

    override fun onRechargeBUWidgetClickBanner(data: RechargeBUWidgetDataModel) {
        homeCategoryListener.getTrackingQueueObj()?.let { trackingQueue ->
            RechargeBUWidgetTracking.homeRechargeBUWidgetBannerClickTracker(trackingQueue, data, homeCategoryListener.userId)
        }

        context?.let {
            RouteManager.route(it, data.data.bannerApplink)
        }
    }

    override fun onRechargeBUWidgetItemClick(data: RechargeBUWidgetDataModel, position: Int) {
        if (position < data.data.items.size) {
            val rechargePersoItem = data.data.items[position]
            homeCategoryListener.getTrackingQueueObj()?.let { trackingQueue ->
                RechargeBUWidgetTracking.homeRechargeBUWidgetProductCardClickTracker(trackingQueue, data, position, homeCategoryListener.userId)
            }

            context?.let {
                RouteManager.route(it, rechargePersoItem.applink)
            }
        }
    }

    override fun getRechargeBUWidget(source: WidgetSource) {
        homeCategoryListener.getRechargeBUWidget(source)
    }

    companion object {
        const val ACTION_IMPRESSION = "impression"
        const val ACTION_CLICK = "click"
    }

}