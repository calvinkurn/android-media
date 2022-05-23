package com.tokopedia.createpost.producttag.analytic.srp

import com.tokopedia.createpost.producttag.analytic.*
import com.tokopedia.createpost.producttag.analytic.KEY_BUSINESS_UNIT
import com.tokopedia.createpost.producttag.analytic.KEY_EVENT
import com.tokopedia.createpost.producttag.analytic.KEY_EVENT_ACTION
import com.tokopedia.createpost.producttag.analytic.KEY_EVENT_CATEGORY
import com.tokopedia.createpost.producttag.analytic.KEY_EVENT_LABEL
import com.tokopedia.createpost.producttag.analytic.KEY_SESSION_IRIS
import com.tokopedia.createpost.producttag.analytic.KEY_SRP_COMPONENT
import com.tokopedia.createpost.producttag.analytic.KEY_SRP_KEYWORD
import com.tokopedia.createpost.producttag.analytic.KEY_SRP_PAGE_SOURCE
import com.tokopedia.createpost.producttag.analytic.KEY_USER_ID
import com.tokopedia.createpost.producttag.analytic.VAL_PHYSICAL_GOODS
import com.tokopedia.createpost.producttag.view.uimodel.tracker.SRPTrackerUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 23, 2022
 */
class SRPProductTagAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface,
) : SRPProductTagAnalytic {

    override fun trackSRP(model: SRPTrackerUiModel) {
        with(model) {
            val srpPageId = getSrpPageId(navSource)
            TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                    KEY_EVENT to "clickTopNav",
                    KEY_EVENT_ACTION to "general search",
                    KEY_EVENT_CATEGORY to "top nav",
                    KEY_EVENT_LABEL to "$keyword|$treatmentType|$responseCode|$VAL_PHYSICAL_GOODS|${navSource.text}|$srpPageId|$totalData",
                    KEY_BUSINESS_UNIT to VAL_PHYSICAL_GOODS,
                    KEY_SRP_COMPONENT to srpComponentId,
                    KEY_SRP_KEYWORD to keyword,
                    KEY_SRP_PAGE_SOURCE to "feed.${navSource.text}.local_search.$srpPageId",
                    KEY_SRP_RELATED_KEYWORD to "$prevKeyword - none",
                    KEY_CURRENT_SITE to VAL_CURRENT_SITE,
                    KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                    KEY_SRP_SEARCH_FILTER to "", /** TODO: search filter? */
                )
            )
        }
    }

    private fun getSrpPageId(navSource: SRPTrackerUiModel.NavSource): String {
        return when(navSource) {
            SRPTrackerUiModel.NavSource.SHOP -> userSession.shopId
            else -> "0"
        }
    }
}