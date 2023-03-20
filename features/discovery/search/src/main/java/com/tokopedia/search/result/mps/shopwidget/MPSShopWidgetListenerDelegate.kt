package com.tokopedia.search.result.mps.shopwidget

import android.content.Context
import com.tokopedia.search.result.mps.MPSViewModel
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate

class MPSShopWidgetListenerDelegate(
    private val context: () -> Context?,
    private val mpsViewModel: () -> MPSViewModel?,
): MPSShopWidgetListener,
    ApplinkOpener by ApplinkOpenerDelegate {

    override fun onSeeShopClicked(mpsShopWidgetDataView: MPSShopWidgetDataView) {
        openApplink(context(), mpsShopWidgetDataView.applink)
    }

    override fun onSeeAllCardClicked(mpsShopWidgetDataView: MPSShopWidgetDataView) {

    }

    override fun onProductItemImpressed(
        mpsShopWidgetDataView: MPSShopWidgetDataView,
        mpsShopWidgetProductDataView: MPSShopWidgetProductDataView?
    ) {

    }

    override fun onProductItemClicked(
        mpsShopWidgetDataView: MPSShopWidgetDataView,
        mpsShopWidgetProductDataView: MPSShopWidgetProductDataView?
    ) {
        mpsShopWidgetProductDataView ?: return

        openApplink(context(), mpsShopWidgetProductDataView.applink)
    }

    override fun onProductItemAddToCart(
        mpsShopWidgetDataView: MPSShopWidgetDataView,
        mpsShopWidgetProductDataView: MPSShopWidgetProductDataView?
    ) {
        mpsShopWidgetProductDataView ?: return

        mpsViewModel()?.onAddToCart(mpsShopWidgetDataView, mpsShopWidgetProductDataView)
    }

    override fun onProductItemSeeOtherProductClick(
        mpsShopWidgetDataView: MPSShopWidgetDataView,
        mpsShopWidgetProductDataView: MPSShopWidgetProductDataView?
    ) {
        val seeOtherProductButton = mpsShopWidgetProductDataView?.secondaryButton() ?: return

        openApplink(context(), seeOtherProductButton.applink)
    }
}
