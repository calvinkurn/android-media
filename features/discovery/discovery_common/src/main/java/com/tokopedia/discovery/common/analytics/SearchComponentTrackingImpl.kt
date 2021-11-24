package com.tokopedia.discovery.common.analytics

import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Action.CLICK
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Action.CLICK_OTHER_ACTION
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Action.IMPRESSION
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.BUSINESSUNIT
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.CAMPAIGNCODE
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.COMPONENT
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.CURRENTSITE
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Category.SEARCH_COMPONENT
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Event.CLICKSEARCH
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Event.VIEWSEARCHIRIS
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.KEYWORD
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.KEYWORD_ID_NAME
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.NONE
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Options.CLICK_ONLY
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Options.IMPRESSION_AND_CLICK
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Options.IMPRESSION_ONLY
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.PAGEDESTINATION
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.PAGESOURCE
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.SEARCH
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.TOKOPEDIAMARKETPLACE
import com.tokopedia.iris.Iris
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.track.interfaces.Analytics

internal class SearchComponentTrackingImpl(
    private val trackingOption: Int,
    private val keyword: String,
    private val valueId: String,
    private val valueName: String,
    private val campaignCode: String,
    private val componentId: String,
    private val applink: String,
    private val dimension90: String,
): SearchComponentTracking {

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
    )

    private fun String.orNone(): String =
        if (this.isEmpty()) NONE else this

    override fun impress(iris: Iris) {
        if (!impressionEnabled()) return

        iris.saveEvent(
            dataLayer(
                VIEWSEARCHIRIS,
                IMPRESSION,
                impressionClickEventLabel()
            )
        )
    }

    private fun impressionEnabled() =
        trackingOption == IMPRESSION_AND_CLICK
            || trackingOption == IMPRESSION_ONLY

    private fun impressionClickEventLabel() =
        String.format(
            KEYWORD_ID_NAME,
            keyword.orNone(),
            valueId.orNone(),
            valueName.orNone(),
        )

    override fun click(analytics: Analytics) {
        if (!clickEnabled()) return

        analytics.sendGeneralEvent(
            dataLayer(
                CLICKSEARCH,
                CLICK,
                impressionClickEventLabel()
            )
        )
    }

    private fun clickEnabled() =
        trackingOption == CLICK_ONLY
            || trackingOption == IMPRESSION_AND_CLICK

    override fun clickOtherAction(analytics: Analytics) {
        if (!clickEnabled()) return

        analytics.sendGeneralEvent(
            dataLayer(
                CLICKSEARCH,
                CLICK_OTHER_ACTION,
                String.format(KEYWORD, keyword.orNone())
            )
        )
    }
}

fun searchComponentTracking(
    trackingOption: Int = IMPRESSION_AND_CLICK,
    keyword: String = "",
    valueId: String = "",
    valueName: String = "",
    campaignCode: String = "",
    componentId: String = "",
    applink: String = "",
    dimension90: String = "",
): SearchComponentTracking =
    SearchComponentTrackingImpl(
        trackingOption = trackingOption,
        keyword = keyword,
        valueId = valueId,
        valueName = valueName,
        campaignCode = campaignCode,
        componentId = componentId,
        applink = applink,
        dimension90 = dimension90,
    )