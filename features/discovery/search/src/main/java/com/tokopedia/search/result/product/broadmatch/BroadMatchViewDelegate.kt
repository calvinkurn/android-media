package com.tokopedia.search.result.product.broadmatch

import android.content.Context
import com.tokopedia.analytics.byteio.search.AppLogSearch
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.iris.Iris
import com.tokopedia.product.detail.common.ProductDetailPrefetch
import com.tokopedia.productcard.reimagine.LABEL_REIMAGINE_CREDIBILITY
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.search.utils.decodeQueryParameter
import com.tokopedia.search.utils.getUserId
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

@SearchScope
class BroadMatchViewDelegate @Inject constructor(
    private val trackingQueue: TrackingQueue,
    private val iris: Iris,
    private val userSession: UserSessionInterface,
    @SearchContext
    context: Context,
    queryKeyProvider: QueryKeyProvider,
) : BroadMatchView,
    ApplinkOpener by ApplinkOpenerDelegate,
    ContextProvider by WeakReferenceContextProvider(context),
    QueryKeyProvider by queryKeyProvider {

    override fun trackEventClickBroadMatchItem(broadMatchItemDataView: BroadMatchItemDataView) {
        val broadMatchItem = ArrayList<Any>()
        broadMatchItem.add(broadMatchItemDataView.asClickObjectDataLayer())

        BroadMatchTracking.trackEventClickBroadMatchItem(
            queryKey,
            broadMatchItemDataView.alternativeKeyword,
            getUserId(userSession),
            broadMatchItemDataView.isOrganicAds,
            broadMatchItemDataView.componentId,
            broadMatchItem,
        )

        AppLogSearch.eventSearchResultClick(
            broadMatchItemDataView.asByteIOSearchResult("")
        )

        AppLogSearch.eventProductClick(
            broadMatchItemDataView.asByteIOProduct()
        )
    }

    override fun trackEventImpressionBroadMatchItem(broadMatchItemDataView: BroadMatchItemDataView) {
        val trackingQueue = trackingQueue
        val broadMatchItemAsObjectDataLayer = ArrayList<Any>()
        broadMatchItemAsObjectDataLayer.add(broadMatchItemDataView.asImpressionObjectDataLayer())

        BroadMatchTracking.trackEventImpressionBroadMatch(
            trackingQueue,
            queryKey,
            broadMatchItemDataView.alternativeKeyword,
            getUserId(userSession),
            broadMatchItemAsObjectDataLayer,
        )

        AppLogSearch.eventSearchResultShow(
            broadMatchItemDataView.asByteIOSearchResult(null)
        )

        AppLogSearch.eventProductShow(
            broadMatchItemDataView.asByteIOProduct()
        )
    }

    override fun trackEventImpressionBroadMatch(broadMatchDataView: BroadMatchDataView) {
        BroadMatchTracking.trackEventImpressionBroadMatch(
            iris,
            broadMatchDataView,
        )

        AppLogSearch.eventSearchResultShow(
            broadMatchDataView.asByteIOSearchResult(null)
        )
    }

    override fun trackEventClickSeeMoreBroadMatch(
        broadMatchDataView: BroadMatchDataView,
        aladdinButtonType: String,
    ) {
        BroadMatchTracking.trackEventClickBroadMatchSeeMore(
            broadMatchDataView,
            queryKey,
            broadMatchDataView.keyword,
            broadMatchDataView.dimension90,
        )
        AppLogSearch.eventSearchResultClick(
            broadMatchDataView.asByteIOSearchResult(aladdinButtonType),
        )
    }

    override fun openLink(applink: String, url: String) {
        if (applink.isNotEmpty())
            openApplink(context, applink.decodeQueryParameter())
        else
            openApplink(context, url)
    }

    override fun openLink(broadMatchItemDataView: BroadMatchItemDataView) {
        val finalLink = generateFinalLink(broadMatchItemDataView)
        openApplink(context, finalLink)
    }

    private fun generateFinalLink(broadMatchItemDataView: BroadMatchItemDataView): String {
        val appLink = broadMatchItemDataView.applink
        return if (appLink.isNotEmpty()) {
            val isPrefetch = ProductDetailPrefetch.validateAppLink(appLink)
            val finalAppLink = if (isPrefetch)
                generatePrefetchAppLink(broadMatchItemDataView)
            else appLink
            finalAppLink.decodeQueryParameter()
        } else broadMatchItemDataView.url
    }

    private fun generatePrefetchAppLink(broadMatchItemDataView: BroadMatchItemDataView): String {
        val data = broadMatchItemDataView.toPrefetchData()
        return ProductDetailPrefetch.process(
            context,
            broadMatchItemDataView.applink,
            data
        )
    }

    private fun BroadMatchItemDataView.toPrefetchData(): ProductDetailPrefetch.Data {
        val integrity = labelGroupDataList.find {
            it.position == LABEL_REIMAGINE_CREDIBILITY ||
                it.position == SearchConstant.ProductCardLabel.LABEL_INTEGRITY
        }?.title ?: ""
        return ProductDetailPrefetch.Data(
            image = imageUrl,
            name = name,
            price = price.toDouble(),
            slashPrice = originalPrice,
            discount = discountPercentage,
            freeShippingLogo = freeOngkirDataView.imageUrl,
            rating = ratingAverage,
            integrity = integrity
        )
    }
}
