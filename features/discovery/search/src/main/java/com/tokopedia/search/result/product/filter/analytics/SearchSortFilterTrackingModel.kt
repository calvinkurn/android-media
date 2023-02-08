package com.tokopedia.search.result.product.filter.analytics

import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Action.CLICK
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.BUSINESSUNIT
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.CAMPAIGNCODE
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.COMPONENT
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.CURRENTSITE
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Category.SEARCH_COMPONENT
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Event.CLICKSEARCH
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.KEYWORD_ID_NAME
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.PAGEDESTINATION
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.PAGESOURCE
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.SEARCH
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.TOKOPEDIAMARKETPLACE
import com.tokopedia.search.analytics.SearchTrackingConstant.SEARCHFILTER
import com.tokopedia.search.utils.orNone
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.track.interfaces.Analytics

internal data class SearchSortFilterTrackingModel(
    val keyword: String = "",
    val valueId: String = "",
    val valueName: String = "",
    val campaignCode: String = "",
    val componentId: String = "",
    val applink: String = "",
    val dimension90: String = "",
    val searchFilters: String = "",
) {
    private fun dataLayer(
        eventName: String,
        eventAction: String,
        eventLabel: String,
    ) = mapOf(
        EVENT to eventName,
        EVENT_ACTION to eventAction,
        EVENT_CATEGORY to SEARCH_COMPONENT,
        EVENT_LABEL to eventLabel,
        BUSINESSUNIT to SEARCH,
        CURRENTSITE to TOKOPEDIAMARKETPLACE,
        CAMPAIGNCODE to campaignCode.orNone(),
        COMPONENT to componentId.orNone(),
        PAGEDESTINATION to applink.orNone(),
        PAGESOURCE to dimension90.orNone(),
        SEARCHFILTER to searchFilters,
    )

    private fun impressionClickEventLabel() =
        String.format(
            KEYWORD_ID_NAME,
            keyword.orNone(),
            valueId.orNone(),
            valueName.orNone(),
        )

    fun click(analytics: Analytics) {
        analytics.sendGeneralEvent(
            dataLayer(
                CLICKSEARCH,
                CLICK,
                impressionClickEventLabel()
            )
        )
    }
}
