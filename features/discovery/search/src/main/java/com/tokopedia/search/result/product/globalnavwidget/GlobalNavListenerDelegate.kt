package com.tokopedia.search.result.product.globalnavwidget

import android.content.Context
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.search.utils.decodeQueryParameter
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*

class GlobalNavListenerDelegate(
    private val trackingQueue: TrackingQueue?,
    context: Context?,
): GlobalNavListener,
    ApplinkOpener by ApplinkOpenerDelegate,
    ContextProvider by WeakReferenceContextProvider(context) {

    override fun onGlobalNavWidgetClicked(item: GlobalNavDataView.Item, keyword: String) {
        openApplink(context, item.applink.decodeQueryParameter())

        GlobalNavWidgetTracking.trackEventClickGlobalNavWidgetItem(
            item.getGlobalNavItemAsObjectDataLayer(item.name),
            keyword,
            item.name,
            item.applink
        )
    }

    override fun onGlobalNavWidgetClickSeeAll(globalNavDataView: GlobalNavDataView) {
        openApplink(context, globalNavDataView.seeAllApplink.decodeQueryParameter())

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