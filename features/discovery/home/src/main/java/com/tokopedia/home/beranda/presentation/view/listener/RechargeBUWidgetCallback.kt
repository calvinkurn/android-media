package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.analytics.v2.RechargeBUWidgetTracking
import com.tokopedia.home.beranda.domain.interactor.GetRechargeBUWidgetUseCase
import com.tokopedia.home.beranda.domain.model.recharge_bu_widget.RechargePerso
import com.tokopedia.home.beranda.domain.model.recharge_bu_widget.RechargePersoItem
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.listener.RechargeBUWidgetListener
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel

class RechargeBUWidgetCallback (val context: Context?, val viewModel: HomeViewModel,
                                val homeCategoryListener: HomeCategoryListener): RechargeBUWidgetListener {

    override fun onRechargeBUWidgetClickMore(rechargePerso: RechargePerso) {
        rechargePerso.tracking.firstOrNull { it.action == ACTION_CLICK }?.let { tracking ->
            homeCategoryListener.getTrackingQueueObj()?.let { trackingQueue ->
                RechargeBUWidgetTracking.enhanceEcommerceTracker(trackingQueue, tracking.data)
            }
        }

        context?.let {
            RouteManager.route(it, rechargePerso.applink)
        }
    }

    override fun onRechargeBUWidgetItemClick(rechargePersoItem: RechargePersoItem) {
        rechargePersoItem.tracking.firstOrNull { it.action == ACTION_CLICK }?.let { tracking ->
            homeCategoryListener.getTrackingQueueObj()?.let { trackingQueue ->
                RechargeBUWidgetTracking.enhanceEcommerceTracker(trackingQueue, tracking.data)
            }
        }

        context?.let {
            RouteManager.route(it, rechargePersoItem.applink)
        }
    }

    override fun onRechargeBUWidgetImpression(rechargePerso: RechargePerso) {
        rechargePerso.tracking.firstOrNull { it.action == ACTION_IMPRESSION }?.let { tracking ->
            homeCategoryListener.getTrackingQueueObj()?.let { trackingQueue ->
                RechargeBUWidgetTracking.enhanceEcommerceTracker(trackingQueue, tracking.data)
            }
        }
    }

    override fun getRechargeBUWidget(source: GetRechargeBUWidgetUseCase.WidgetSource) {
        viewModel.getRechargeBUWidget(source)
    }

    companion object {
        const val ACTION_IMPRESSION = "impression"
        const val ACTION_CLICK = "click"
    }

}