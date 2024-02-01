package com.tokopedia.search.result.product.inspirationlistatc.postatccarousel

import android.content.Context
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnificationDataMapper.createCarouselTrackingUnificationData
import com.tokopedia.search.result.product.inspirationlistatc.InspirationListAtcPresenter
import com.tokopedia.search.utils.SearchIdlingResource
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import javax.inject.Inject

@SearchScope
class InspirationListPostAtcListenerDelegate @Inject constructor(
    private val inspirationListAtcPresenter: InspirationListAtcPresenter,
    private val inspirationListPostAtcView: InspirationListPostAtcView,
    @SearchContext
    context: Context,
    searchParameterProvider: SearchParameterProvider
): InspirationListPostAtcListener,
    ApplinkOpener by ApplinkOpenerDelegate,
    ContextProvider by WeakReferenceContextProvider(context),
    SearchParameterProvider by searchParameterProvider{

    override fun onListAtcSeeMoreClicked(data: InspirationCarouselDataView.Option) {
        inspirationListPostAtcView.trackSeeMoreClick(data)
        openApplink(context, data.applink)
    }

    override fun onListAtcItemClicked(product: InspirationCarouselDataView.Option.Product) {
        val trackingData = createCarouselTrackingUnificationData(
                product,
                getSearchParameter()
            )
        inspirationListPostAtcView.trackItemClick(trackingData)
        if (product.isOrganicAds) inspirationListPostAtcView.trackAdsClick(product)
        openApplink(context, product.applink)
    }

    override fun onListAtcItemImpressed(product: InspirationCarouselDataView.Option.Product) {
        inspirationListPostAtcView.trackItemImpress(product)
        if (product.isOrganicAds) inspirationListPostAtcView.trackAdsImpress(product)
    }

    override fun onListAtcItemAddToCart(
        product: InspirationCarouselDataView.Option.Product,
        type: String,
    ) {
        SearchIdlingResource.increment()

        inspirationListAtcPresenter.onListAtcItemAddToCart(product, type)
    }

    override fun closeListPostAtcView(item: InspirationListPostAtcDataView) {
        inspirationListAtcPresenter.setVisibilityInspirationCarouselPostAtcOnVisitableList(
            false,
            item,
        )
        inspirationListPostAtcView.closeListPostAtcView(item)
    }

    override fun cancelCloseListPostAtcView(item: InspirationListPostAtcDataView) {
        inspirationListAtcPresenter.setVisibilityInspirationCarouselPostAtcOnVisitableList(
            true,
            item,
        )
        inspirationListPostAtcView.cancelCloseListPostAtcView(item)
    }
}
