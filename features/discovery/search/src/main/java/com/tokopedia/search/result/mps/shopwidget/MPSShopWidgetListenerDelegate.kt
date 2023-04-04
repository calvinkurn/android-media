package com.tokopedia.search.result.mps.shopwidget

import android.content.Context
import com.tokopedia.discovery.common.utils.UrlParamUtils.keywords
import com.tokopedia.search.result.mps.MPSViewModel
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue

class MPSShopWidgetListenerDelegate(
    private val context: Context?,
    private val mpsViewModel: MPSViewModel?,
    private val trackingQueue: TrackingQueue,
): MPSShopWidgetListener,
    ApplinkOpener by ApplinkOpenerDelegate {

    private val keywords
        get() = mpsViewModel?.stateFlow?.value?.parameter?.keywords() ?: ""

    override fun onSeeShopClicked(mpsShopWidgetDataView: MPSShopWidgetDataView) {
        mpsShopWidgetDataView.buttonList.first().click(TrackApp.getInstance().gtm)

        openApplink(context, mpsShopWidgetDataView.applink)
    }

    override fun onSeeAllCardClicked(mpsShopWidgetDataView: MPSShopWidgetDataView) {

    }

    override fun onProductItemImpressed(
        mpsShopWidgetDataView: MPSShopWidgetDataView,
        mpsShopWidgetProductDataView: MPSShopWidgetProductDataView?
    ) {
        mpsShopWidgetProductDataView ?: return

        MPSShopWidgetTracking.impressionShopWidgetProduct(
            trackingQueue,
            arrayListOf(mpsShopWidgetProductDataView.asObjectDataLayer()),
            keywords,
            mpsShopWidgetDataView.id,
        )
    }

    override fun onProductItemClicked(
        mpsShopWidgetDataView: MPSShopWidgetDataView,
        mpsShopWidgetProductDataView: MPSShopWidgetProductDataView?
    ) {
        mpsShopWidgetProductDataView ?: return

        MPSShopWidgetTracking.clickShopWidgetProduct(
            mpsShopWidgetProductDataView.asObjectDataLayer(),
            mpsShopWidgetDataView.componentId,
            keywords,
            mpsShopWidgetDataView.id,
        )

        openApplink(context, mpsShopWidgetProductDataView.applink)
    }

    override fun onProductItemAddToCart(
        mpsShopWidgetDataView: MPSShopWidgetDataView,
        mpsShopWidgetProductDataView: MPSShopWidgetProductDataView?
    ) {
        mpsShopWidgetProductDataView ?: return

        mpsViewModel?.onAddToCart(mpsShopWidgetDataView, mpsShopWidgetProductDataView)
    }

    override fun onProductItemSeeOtherProductClick(
        mpsShopWidgetDataView: MPSShopWidgetDataView,
        mpsShopWidgetProductDataView: MPSShopWidgetProductDataView?
    ) {
        val seeOtherProductButton = mpsShopWidgetProductDataView?.secondaryButton() ?: return
        seeOtherProductButton.click(TrackApp.getInstance().gtm)

        openApplink(context, seeOtherProductButton.applink)
    }
}
