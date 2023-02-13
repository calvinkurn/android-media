package com.tokopedia.search.result.product.inspirationlistatc

import android.content.Context
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnificationDataMapper.createCarouselTrackingUnificationData
import com.tokopedia.search.utils.SearchIdlingResource
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import javax.inject.Inject

@SearchScope
class InspirationListAtcListenerDelegate @Inject constructor(
    private val inspirationListAtcPresenter: InspirationListAtcPresenter,
    private val inspirationListAtcView: InspirationListAtcView,
    @SearchContext
    context: Context,
    searchParameterProvider: SearchParameterProvider
): InspirationListAtcListener,
    ApplinkOpener by ApplinkOpenerDelegate,
    ContextProvider by WeakReferenceContextProvider(context),
    SearchParameterProvider by searchParameterProvider{

    override fun onListAtcSeeMoreClicked(data: InspirationCarouselDataView.Option) {
        inspirationListAtcView.trackSeeMoreClick(data)
        openApplink(context, data.applink)
    }

    override fun onListAtcItemClicked(product: InspirationCarouselDataView.Option.Product) {
        val trackingData = createCarouselTrackingUnificationData(
                product,
                getSearchParameter()
            )
        inspirationListAtcView.trackItemClick(trackingData)
        if (product.isOrganicAds) inspirationListAtcView.trackAdsClick(product)
        openApplink(context, product.applink)
    }

    override fun onListAtcItemImpressed(product: InspirationCarouselDataView.Option.Product) {
        inspirationListAtcView.trackItemImpress(product)
        if (product.isOrganicAds) inspirationListAtcView.trackAdsImpress(product)
    }

    override fun onListAtcItemAddToCart(
        product: InspirationCarouselDataView.Option.Product,
        type: String,
    ) {
        SearchIdlingResource.increment()

        inspirationListAtcPresenter.onListAtcItemAddToCart(product, type)
    }
}
