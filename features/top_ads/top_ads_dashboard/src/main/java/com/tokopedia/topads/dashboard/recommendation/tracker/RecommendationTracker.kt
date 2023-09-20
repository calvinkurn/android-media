package com.tokopedia.topads.dashboard.recommendation.tracker

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.Action.CLICK_GROUP_LIST
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.Action.CLICK_GROUP_LIST_HOMEPAGE
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.Action.CLICK_PRODUCT_OOS
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.Action.CLICK_PRODUCT_RECOMMENDATION
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.Action.CLICK_SELENGKAPNYA_HOMEPAGE
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.Action.CLICK_SUBMIT_BID_KEYWORD
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.Action.CLICK_SUBMIT_DAILY_BUDGET
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.Action.CLICK_SUBMIT_GROUP_BID
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.Action.CLICK_SUBMIT_NEGATIVE_KEYWORD
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.Action.CLICK_SUBMIT_POSITIVE_KEYWORD
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.Action.CLICK_SUBMIT_PRODUCT_RECOMMENDATION
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.EVENT_CATEGORY_HOMEPAGE
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.EVENT_CATEGORY_INSIGHT_CENTRE
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.EVENT_LABEL_EXISTING_GROUP
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.EVENT_LABEL_NEW_GROUP
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.getTrackerId
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.util.BaseTrackerConst

object RecommendationTracker : BaseTrackerConst() {
    private val trackApp = TrackApp.getInstance().gtm

    private fun fillCommonData(): MutableMap<String, Any> {
        val map = mutableMapOf<String, Any>()
        map[Event.KEY] = RecommendationTrackerConst.EVENT
        map[BusinessUnit.KEY] = RecommendationTrackerConst.BUSINESS_UNIT
        map[CurrentSite.KEY] = RecommendationTrackerConst.CURRENT_SITE
        return map
    }

    fun clickGroupListHomepage() {
        val map = fillCommonData()
        map[Action.KEY] = CLICK_GROUP_LIST_HOMEPAGE
        map[TrackerId.KEY] = getTrackerId(CLICK_GROUP_LIST_HOMEPAGE)
        map[Label.KEY] = String.EMPTY
        map[Category.KEY] = EVENT_CATEGORY_HOMEPAGE
        trackApp.sendGeneralEvent(map)
    }

    fun clickSelengkapnyaHomepage() {
        val map = fillCommonData()
        map[Action.KEY] = CLICK_SELENGKAPNYA_HOMEPAGE
        map[TrackerId.KEY] = getTrackerId(CLICK_SELENGKAPNYA_HOMEPAGE)
        map[Label.KEY] = String.EMPTY
        map[Category.KEY] = EVENT_CATEGORY_HOMEPAGE
        trackApp.sendGeneralEvent(map)
    }

    fun clickProductRecommendation() {
        val map = fillCommonData()
        map[Action.KEY] = CLICK_PRODUCT_RECOMMENDATION
        map[TrackerId.KEY] = getTrackerId(CLICK_PRODUCT_RECOMMENDATION)
        map[Label.KEY] = String.EMPTY
        map[Category.KEY] = EVENT_CATEGORY_INSIGHT_CENTRE
        trackApp.sendGeneralEvent(map)
    }

    fun clickProductOOS() {
        val map = fillCommonData()
        map[Action.KEY] = CLICK_PRODUCT_OOS
        map[TrackerId.KEY] = getTrackerId(CLICK_PRODUCT_OOS)
        map[Label.KEY] = String.EMPTY
        map[Category.KEY] = EVENT_CATEGORY_INSIGHT_CENTRE
        trackApp.sendGeneralEvent(map)
    }

    fun clickGroupList() {
        val map = fillCommonData()
        map[Action.KEY] = CLICK_GROUP_LIST
        map[TrackerId.KEY] = getTrackerId(CLICK_GROUP_LIST)
        map[Label.KEY] = String.EMPTY
        map[Category.KEY] = EVENT_CATEGORY_INSIGHT_CENTRE
        trackApp.sendGeneralEvent(map)
    }

    fun clickSubmitBidKeyword() {
        val map = fillCommonData()
        map[Action.KEY] = CLICK_SUBMIT_BID_KEYWORD
        map[TrackerId.KEY] = getTrackerId(CLICK_SUBMIT_BID_KEYWORD)
        map[Label.KEY] = String.EMPTY
        map[Category.KEY] = EVENT_CATEGORY_INSIGHT_CENTRE
        trackApp.sendGeneralEvent(map)
    }

    fun clickSubmitDailyBudget() {
        val map = fillCommonData()
        map[Action.KEY] = CLICK_SUBMIT_DAILY_BUDGET
        map[TrackerId.KEY] = getTrackerId(CLICK_SUBMIT_DAILY_BUDGET)
        map[Label.KEY] = String.EMPTY
        map[Category.KEY] = EVENT_CATEGORY_INSIGHT_CENTRE
        trackApp.sendGeneralEvent(map)
    }

    fun clickSubmitGroupBid() {
        val map = fillCommonData()
        map[Action.KEY] = CLICK_SUBMIT_GROUP_BID
        map[TrackerId.KEY] = getTrackerId(CLICK_SUBMIT_GROUP_BID)
        map[Label.KEY] = String.EMPTY
        map[Category.KEY] = EVENT_CATEGORY_INSIGHT_CENTRE
        trackApp.sendGeneralEvent(map)
    }

    fun clickSubmitNegativeKeyword() {
        val map = fillCommonData()
        map[Action.KEY] = CLICK_SUBMIT_NEGATIVE_KEYWORD
        map[TrackerId.KEY] = getTrackerId(CLICK_SUBMIT_NEGATIVE_KEYWORD)
        map[Label.KEY] = String.EMPTY
        map[Category.KEY] = EVENT_CATEGORY_INSIGHT_CENTRE
        trackApp.sendGeneralEvent(map)
    }

    fun clickSubmitPositiveKeyword() {
        val map = fillCommonData()
        map[Action.KEY] = CLICK_SUBMIT_POSITIVE_KEYWORD
        map[TrackerId.KEY] = getTrackerId(CLICK_SUBMIT_POSITIVE_KEYWORD)
        map[Label.KEY] = String.EMPTY
        map[Category.KEY] = EVENT_CATEGORY_INSIGHT_CENTRE
        trackApp.sendGeneralEvent(map)
    }

    fun clickSubmitProductRecommendationExistingGroup() {
        val map = fillCommonData()
        map[Action.KEY] = CLICK_SUBMIT_PRODUCT_RECOMMENDATION
        map[TrackerId.KEY] = getTrackerId(CLICK_SUBMIT_PRODUCT_RECOMMENDATION)
        map[Label.KEY] = EVENT_LABEL_EXISTING_GROUP
        map[Category.KEY] = EVENT_CATEGORY_INSIGHT_CENTRE
        trackApp.sendGeneralEvent(map)
    }

    fun clickSubmitProductRecommendationNewGroup() {
        val map = fillCommonData()
        map[Action.KEY] = CLICK_SUBMIT_PRODUCT_RECOMMENDATION
        map[TrackerId.KEY] = getTrackerId(CLICK_SUBMIT_PRODUCT_RECOMMENDATION)
        map[Label.KEY] = EVENT_LABEL_NEW_GROUP
        map[Category.KEY] = EVENT_CATEGORY_INSIGHT_CENTRE
        trackApp.sendGeneralEvent(map)
    }
}
