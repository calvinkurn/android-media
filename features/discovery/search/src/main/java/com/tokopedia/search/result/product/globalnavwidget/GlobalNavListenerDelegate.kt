package com.tokopedia.search.result.product.globalnavwidget

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.search.utils.decodeQueryParameter
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*

class GlobalNavListenerDelegate(
    private val trackingQueue: TrackingQueue?,
    private val context: Context?,
): GlobalNavListener {

    override fun onGlobalNavWidgetClicked(item: GlobalNavDataView.Item, keyword: String) {
        RouteManager.route(context, item.applink.decodeQueryParameter())

        GlobalNavWidgetTracking.trackEventClickGlobalNavWidgetItem(
            item.getGlobalNavItemAsObjectDataLayer(item.name),
            keyword,
            item.name,
            item.applink
        )
    }

    override fun onGlobalNavWidgetClickSeeAll(globalNavDataView: GlobalNavDataView) {
        RouteManager.route(context, globalNavDataView.seeAllApplink.decodeQueryParameter())

        GlobalNavWidgetTracking.eventUserClickSeeAllGlobalNavWidget(
            globalNavDataView.keyword,
            globalNavDataView.title,
            globalNavDataView.seeAllApplink,
        )
    }

    override fun onGlobalNavWidgetImpressed(globalNavDataView: GlobalNavDataView) {
        val trackingQueue = trackingQueue ?: return

        val dataLayerList = globalNavDataView.itemList.mapTo(ArrayList<Any>()) { item ->
            item.getGlobalNavItemAsObjectDataLayer(item.applink)
        }

        GlobalNavWidgetTracking.trackEventImpressionGlobalNavWidgetItem(
            trackingQueue,
            dataLayerList,
            globalNavDataView.keyword,
        )
    }
}