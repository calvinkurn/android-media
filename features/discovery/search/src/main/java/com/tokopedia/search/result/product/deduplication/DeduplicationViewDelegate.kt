package com.tokopedia.search.result.product.deduplication

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst
import com.tokopedia.discovery.common.utils.Dimension90Utils
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.analytics.SearchTrackingConstant
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.utils.orNone
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

@SearchScope
class DeduplicationViewDelegate @Inject constructor(
    queryKeyProvider: QueryKeyProvider,
    searchParameterProvider: SearchParameterProvider,
    private val userSession: UserSessionInterface,
): DeduplicationView,
    QueryKeyProvider by queryKeyProvider,
    SearchParameterProvider by searchParameterProvider {

    override fun trackRemoved(
        componentID: String,
        applink: String,
        externalReference: String,
    ) {
        val searchParameter = getSearchParameter()?.getSearchParameterMap() ?: mapOf()
        val dimension90 = Dimension90Utils.getDimension90(searchParameter)

        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                SearchTrackingConstant.EVENT, SearchComponentTrackingConst.Event.CLICKSEARCH,
                SearchTrackingConstant.EVENT_CATEGORY, SearchComponentTrackingConst.Category.SEARCH_COMPONENT,
                SearchTrackingConstant.EVENT_ACTION, SearchComponentTrackingConst.Action.CLICK_OTHER_ACTION,
                SearchTrackingConstant.EVENT_LABEL, "keyword:$queryKey | value_name:removed-by-dedup",
                SearchComponentTrackingConst.COMPONENT, componentID.orNone(),
                SearchComponentTrackingConst.PAGESOURCE, dimension90.orNone(),
                SearchComponentTrackingConst.BUSINESSUNIT, SearchComponentTrackingConst.SEARCH,
                SearchComponentTrackingConst.CURRENTSITE, SearchComponentTrackingConst.TOKOPEDIAMARKETPLACE,
                SearchComponentTrackingConst.PAGEDESTINATION, applink.orNone(),
                SearchTrackingConstant.USER_ID, userSession.userId,
                SearchEventTracking.EXTERNAL_REFERENCE, externalReference.orNone(),
            )
        )
    }
}
