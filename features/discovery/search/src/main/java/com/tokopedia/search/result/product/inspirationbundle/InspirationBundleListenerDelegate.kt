package com.tokopedia.search.result.product.inspirationbundle

import android.content.Context
import com.tokopedia.iris.Iris
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.inspirationbundle.InspirationProductBundlingDataViewMapper.toProductModel
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnification
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnificationDataMapper.createCarouselTrackingUnificationData
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.shop.common.widget.bundle.model.BundleProductUiModel
import com.tokopedia.track.TrackApp

class InspirationBundleListenerDelegate(
    context: Context?,
    private val inspirationCarouselTrackingUnification: InspirationCarouselTrackingUnification,
    private val iris: Iris,
    searchParameterProvider: SearchParameterProvider,
) : InspirationBundleListener,
    ApplinkOpener by ApplinkOpenerDelegate,
    SearchParameterProvider by searchParameterProvider,
    ContextProvider by WeakReferenceContextProvider(context) {

    override fun onSeeBundleClicked(
        bundle: InspirationProductBundleDataView.Bundle,
        selectedProducts: List<BundleProductUiModel>,
    ) {
        openApplink(context, bundle.activeApplink)

        bundle.click(TrackApp.getInstance().gtm)
    }


    override fun onBundleImpressed(
        bundle: InspirationProductBundleDataView.Bundle,
    ) {
        bundle.impress(iris)
    }

    override fun onBundleProductClicked(
        bundle: InspirationProductBundleDataView.Bundle,
        bundleProduct: BundleProductUiModel
    ) {
        openApplink(context, bundleProduct.productAppLink)
        val data = createCarouselTrackingUnificationData(
            bundle.toProductModel(bundleProduct),
            getSearchParameter()
        )

        inspirationCarouselTrackingUnification.trackCarouselClick(data)
    }
}