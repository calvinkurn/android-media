package com.tokopedia.search.result.product.inspirationlistatc

import android.content.Context
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.search.R
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationListener
import com.tokopedia.search.result.product.ClassNameProvider
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.addtocart.AddToCartVariantBottomSheetLauncher
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTracking
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTracking.getInspirationCarouselUnificationListName
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnificationDataMapper
import com.tokopedia.search.utils.FragmentProvider
import com.tokopedia.search.utils.SearchIdlingResource
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

@SearchScope
class InspirationListAtcViewDelegate @Inject constructor(
    private val trackingQueue: TrackingQueue,
    private val searchNavigationListener: SearchNavigationListener?,
    private val topAdsUrlHitter: TopAdsUrlHitter,
    private val atcVariantLauncher: AddToCartVariantBottomSheetLauncher,
    searchParameterProvider: SearchParameterProvider,
    classNameProvider: ClassNameProvider,
    @SearchContext
    context: Context,
    fragmentProvider: FragmentProvider,
    queryKeyProvider: QueryKeyProvider,
): InspirationListAtcView,
    SearchParameterProvider by searchParameterProvider,
    ContextProvider by WeakReferenceContextProvider(context),
    FragmentProvider by fragmentProvider,
    QueryKeyProvider by queryKeyProvider,
    ApplinkOpener by ApplinkOpenerDelegate,
    ClassNameProvider by classNameProvider {

    override fun trackSeeMoreClick(data: InspirationCarouselDataView.Option) {
        InspirationCarouselTracking.trackCarouselClickSeeAll(data.keyword, data)
    }

    override fun trackItemClick(trackingData: InspirationCarouselTracking.Data) {
        InspirationCarouselTracking.trackCarouselClick(trackingData)
    }

    override fun trackItemImpress(product: InspirationCarouselDataView.Option.Product) {
        val trackingData =
            InspirationCarouselTrackingUnificationDataMapper.createCarouselTrackingUnificationData(
                product,
                getSearchParameter()
            )
        InspirationCarouselTracking.trackCarouselImpression(trackingQueue, trackingData)
    }

    override fun trackAddToCart(trackingData: InspirationCarouselTracking.Data) {
        InspirationCarouselTracking.trackCarouselClickAtc(trackingData)
    }

    override fun openAddToCartToaster(message: String, isSuccess: Boolean) {
        getFragment().view?.let {
            Toaster.build(
                it,
                message,
                Snackbar.LENGTH_SHORT,
                Toaster.TYPE_NORMAL,
                if (isSuccess) getFragment().getString(R.string.search_see_cart) else "",
            ) {
                if (isSuccess) openApplink(context, ApplinkConst.CART)
            }.show()
        }

        SearchIdlingResource.decrement()
    }

    override fun openVariantBottomSheet(
        product: InspirationCarouselDataView.Option.Product,
        type: String
    ) {
        atcVariantLauncher.launch(
            productId = product.id,
            shopId = product.shopId,
            trackerCDListName = getInspirationCarouselUnificationListName(
                type,
                product.componentId,
            )
        ) {
            val trackingData =
                InspirationCarouselTrackingUnificationDataMapper.createCarouselTrackingUnificationData(
                    product,
                    getSearchParameter(),
                    it.cartId,
                    product.minOrder.toIntOrZero(),
                )

            trackItemClick(trackingData)
            trackAddToCart(trackingData)
        }

        SearchIdlingResource.decrement()
    }

    override fun trackAddToCartVariant(product: InspirationCarouselDataView.Option.Product) {
        product.asSearchComponentTracking(queryKey).click(TrackApp.getInstance().gtm)
    }

    override fun updateSearchBarNotification() {
        searchNavigationListener?.updateSearchBarNotification()
    }

    override fun trackAdsClick(product: InspirationCarouselDataView.Option.Product) {
        topAdsUrlHitter.hitClickUrl(
            className,
            product.topAdsClickUrl,
            product.id,
            product.name,
            product.imgUrl,
            SearchConstant.TopAdsComponent.ORGANIC_ADS
        )
    }

    override fun trackAdsImpress(product: InspirationCarouselDataView.Option.Product) {
        topAdsUrlHitter.hitImpressionUrl(
            className,
            product.topAdsViewUrl,
            product.id,
            product.name,
            product.imgUrl,
            SearchConstant.TopAdsComponent.ORGANIC_ADS
        )
    }
}
