package com.tokopedia.search.result.product.inspirationbundle

import android.content.Context
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.iris.Iris
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.inspirationbundle.InspirationProductBundleDataView.BundleDataView
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTracking
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnificationDataMapper.createCarouselTrackingUnificationData
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.shop.common.widget.bundle.model.BundleProductUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue

class InspirationBundleListenerDelegate(
    context: Context?,
    private val iris: Iris,
    private val trackingQueue: TrackingQueue,
    searchParameterProvider: SearchParameterProvider,
) : InspirationBundleListener,
    ApplinkOpener by ApplinkOpenerDelegate,
    SearchParameterProvider by searchParameterProvider,
    ContextProvider by WeakReferenceContextProvider(context) {

    override fun onSeeBundleClicked(
        bundle: BundleDataView,
        selectedProducts: List<BundleProductUiModel>,
    ) {
        openApplink(context, bundle.activeApplink)

        bundle.asSearchComponentTracking()
            .click(TrackApp.getInstance().gtm)
    }

    override fun onBundleImpressed(
        bundle: BundleDataView,
    ) {
        bundle.asSearchComponentTracking()
            .impress(iris)
    }

    override fun onBundleProductImpressed(
        bundle: BundleDataView,
        bundleProduct: BundleProductUiModel
    ) {
        val product = bundle.option.product.firstOrNull { it.id == bundleProduct.productId } ?: return
        val data = createCarouselTrackingUnificationData(
            product,
            getSearchParameter()
        )
        InspirationCarouselTracking.trackCarouselImpression(trackingQueue, data)
    }

    override fun onBundleProductClicked(
        bundle: BundleDataView,
        bundleProduct: BundleProductUiModel
    ) {
        val product = bundle.option.product.firstOrNull { it.id == bundleProduct.productId } ?: return
        openApplink(context, product.applink)
        val data = createCarouselTrackingUnificationData(
            product,
            getSearchParameter()
        )

        InspirationCarouselTracking.trackCarouselClick(data)
    }

    private fun BundleDataView.asSearchComponentTracking() : SearchComponentTracking =
        searchComponentTracking(
            trackingOption = trackingOption,
            componentId = componentId,
            dimension90 = dimension90,
            applink = activeApplink,
            keyword = keyword,
            valueName = "$carouselTitle - ${bundle.bundleName}",
        )
}
