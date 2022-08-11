package com.tokopedia.search.result.product.globalnavwidget

import android.content.Context
import com.tokopedia.iris.Iris
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.search.utils.decodeQueryParameter
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*

class GlobalNavListenerDelegate(
    private val trackingQueue: TrackingQueue?,
    context: Context?,
    private val iris: Iris,
): GlobalNavListener,
    ApplinkOpener by ApplinkOpenerDelegate,
    ContextProvider by WeakReferenceContextProvider(context) {

    override fun onGlobalNavWidgetClicked(item: GlobalNavDataView.Item, keyword: String) {
        openApplink(context, item.applink.decodeQueryParameter())

        if (item.componentId.isNotEmpty()) {
            item.click(TrackApp.getInstance().gtm)
        } else {
            GlobalNavWidgetTracking.trackEventClickGlobalNavWidgetItem(
                item.getGlobalNavItemAsObjectDataLayer(item.name),
                keyword,
                item.name,
                item.applink
            )
        }
    }

    override fun onGlobalNavWidgetClickSeeAll(globalNavDataView: GlobalNavDataView) {
        openApplink(context, globalNavDataView.seeAllApplink.decodeQueryParameter())

        if (globalNavDataView.componentId.isNotEmpty()) {
            globalNavDataView.click(TrackApp.getInstance().gtm)
        } else {
            GlobalNavWidgetTracking.eventUserClickSeeAllGlobalNavWidget(
                globalNavDataView.keyword,
                globalNavDataView.title,
                globalNavDataView.seeAllApplink,
            )
        }
    }

    override fun onGlobalNavWidgetImpressed(globalNavDataView: GlobalNavDataView) {
        val trackingQueue = trackingQueue ?: return

        if (globalNavDataView.componentId.isNotEmpty()) {
            globalNavDataView.impress(iris)
        } else {
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
}