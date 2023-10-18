package com.tokopedia.search.result.mps.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.analytics.SearchTrackingConstant
import com.tokopedia.search.utils.orNone
import com.tokopedia.track.TrackApp
import javax.inject.Inject

class MPSTracking @Inject constructor() {
    fun trackGeneralSearch(generalSearchTracking: GeneralSearchTrackingMPS) {
        val generalSearchShopDataLayer = DataLayer.mapOf(
            SearchTrackingConstant.EVENT, SearchEventTracking.Event.CLICK_TOP_NAV,
            SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.GENERAL_SEARCH,
            SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.EVENT_TOP_NAV,
            SearchTrackingConstant.EVENT_LABEL, generalSearchTracking.eventLabel,
            SearchEventTracking.BUSINESS_UNIT, SearchEventTracking.SEARCH,
            SearchEventTracking.CURRENT_SITE, SearchEventTracking.TOKOPEDIA_MARKETPLACE,
            SearchTrackingConstant.USER_ID, generalSearchTracking.userId,
            SearchTrackingConstant.PAGE_SOURCE, generalSearchTracking.pageSource,
            SearchTrackingConstant.RELATED_KEYWORD, generalSearchTracking.relatedKeyword,
            SearchTrackingConstant.SEARCHFILTER, generalSearchTracking.searchFilter,
            SearchTrackingConstant.IS_RESULT_FOUND, generalSearchTracking.isResultFound,
            SearchTrackingConstant.RELATED_KEYWORD, generalSearchTracking.relatedKeyword,
            SearchTrackingConstant.PAGE_SOURCE, generalSearchTracking.pageSource,
            SearchTrackingConstant.SEARCHFILTER, generalSearchTracking.searchFilter,
            SearchComponentTrackingConst.COMPONENT, generalSearchTracking.componentId.orNone(),
            SearchEventTracking.EXTERNAL_REFERENCE, generalSearchTracking.externalReference.orNone(),
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(generalSearchShopDataLayer)
    }
}
