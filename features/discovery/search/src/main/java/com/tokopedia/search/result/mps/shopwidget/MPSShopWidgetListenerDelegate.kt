package com.tokopedia.search.result.mps.shopwidget

import android.content.Context
import com.tokopedia.discovery.common.utils.UrlParamUtils.keywords
import com.tokopedia.iris.Iris
import com.tokopedia.search.result.mps.MPSViewModel
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.trackingoptimizer.TrackingQueue

class MPSShopWidgetListenerDelegate(
    context: Context?,
    private val mpsViewModel: MPSViewModel?,
    private val trackingQueue: TrackingQueue,
    private val iris: Iris,
): MPSShopWidgetListener,
    ContextProvider by WeakReferenceContextProvider(context),
    ApplinkOpener by ApplinkOpenerDelegate {

    private val keywords
        get() = mpsViewModel?.stateFlow?.value?.parameter?.keywords() ?: ""

    private val analytics: Analytics
        get() = TrackApp.getInstance().gtm

    override fun onShopImpressed(mpsShopWidgetDataView: MPSShopWidgetDataView) {
        mpsShopWidgetDataView.impress(iris)
    }

    override fun onSeeShopClicked(mpsShopWidgetDataView: MPSShopWidgetDataView) {
        val seeShopButton = mpsShopWidgetDataView.buttonList.first()
        seeShopButton.click(analytics)

        openApplink(context, seeShopButton.applink)
    }

    override fun onSeeAllCardClicked(mpsShopWidgetDataView: MPSShopWidgetDataView) {
        mpsShopWidgetDataView.viewAllCard.click(analytics)

        openApplink(context, mpsShopWidgetDataView.viewAllCard.applink)
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
        seeOtherProductButton.click(analytics)

        openApplink(context, seeOtherProductButton.applink)
    }
}
