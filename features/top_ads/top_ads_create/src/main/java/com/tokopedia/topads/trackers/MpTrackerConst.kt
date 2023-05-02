package com.tokopedia.topads.trackers

import com.tokopedia.topads.trackers.MpTrackerConst.Action.CLICK_AD_ADVERTISE_CTA
import com.tokopedia.topads.trackers.MpTrackerConst.Action.CLICK_AD_CLOSE
import com.tokopedia.topads.trackers.MpTrackerConst.Action.CLICK_AD_CREATE
import com.tokopedia.topads.trackers.MpTrackerConst.Action.CLICK_AD_CREATED_CREDIT_ADD
import com.tokopedia.topads.trackers.MpTrackerConst.Action.CLICK_AD_CREATED_CREDIT_STAY
import com.tokopedia.topads.trackers.MpTrackerConst.Action.CLICK_AD_CREATED_ENOUGH
import com.tokopedia.topads.trackers.MpTrackerConst.Action.CLICK_AD_CREATED_STAY
import com.tokopedia.topads.trackers.MpTrackerConst.Action.CLICK_AD_GROUP_EDIT_BUDGET
import com.tokopedia.topads.trackers.MpTrackerConst.Action.CLICK_AD_GROUP_EDIT_NAME
import com.tokopedia.topads.trackers.MpTrackerConst.Action.CLICK_AD_SEARCH
import com.tokopedia.topads.trackers.MpTrackerConst.Action.CLICK_AD_SEARCH_ENTER
import com.tokopedia.topads.trackers.MpTrackerConst.Action.CLICK_AD_SORT
import com.tokopedia.topads.trackers.MpTrackerConst.Action.CLICK_AD_SORT_CLICK
import com.tokopedia.topads.trackers.MpTrackerConst.Action.CLICK_AD_SORT_IMPRESSION
import com.tokopedia.topads.trackers.MpTrackerConst.Action.CLICK_AD_SORT_ORDER
import com.tokopedia.topads.trackers.MpTrackerConst.Action.CLICK_EXISTING_AD
import com.tokopedia.topads.trackers.MpTrackerConst.Action.CLICK_EXISTING_AD_SIMPAN_CTA
import com.tokopedia.topads.trackers.MpTrackerConst.Action.CLICK_MEET_FIRST
import com.tokopedia.topads.trackers.MpTrackerConst.Action.CLICK_NEW_AD_GROUP_CREATE


object MpTrackerConst {

    const val EVENT = "clickTopAds"
    const val EVENT_CATEGORY = "product list page"
    const val EVENT_LABEL = ""
    const val BUSINESS_UNIT = "ads solution"
    const val CURRENT_SITE = "tokopediaseller"

    object Action{
        const val CLICK_AD_CLOSE = "click - create ad close modal"
        const val CLICK_AD_SEARCH  = "click - create ad search bar"
        const val CLICK_AD_SEARCH_ENTER = "click - create ad search/enter on keyboard"
        const val CLICK_AD_SORT = "click - create ad sort options"
        const val CLICK_AD_CREATE = "click - create ad create new ad group"
        const val CLICK_EXISTING_AD = "click - create ad existing ad group"
        const val CLICK_AD_SORT_IMPRESSION = "click - create ad sort by impression"
        const val CLICK_AD_SORT_CLICK = "click - create ad sort by click"
        const val CLICK_AD_SORT_ORDER = "click - create ad sort by order"
        const val CLICK_EXISTING_AD_SIMPAN_CTA = "click - create ad save to existing ad group"
        const val CLICK_AD_CREATED_STAY = "click - ad created credit enough stay on page"
        const val CLICK_AD_CREATED_ENOUGH = "click - ad created credit enough manage ad group"
        const val CLICK_AD_GROUP_EDIT_NAME = "click - create ad new group edit name"
        const val CLICK_AD_GROUP_EDIT_BUDGET = "click - create ad new group edit daily budget"
        const val CLICK_NEW_AD_GROUP_CREATE = "click - create ad new group create group"
        const val CLICK_AD_CREATED_CREDIT_STAY = "click - ad created credit not enough stay on page"
        const val CLICK_AD_CREATED_CREDIT_ADD = "click - ad created credit not enough add credit"
        const val CLICK_AD_ADVERTISE_CTA = "click - advertise your product"
        const val CLICK_MEET_FIRST = "click - yuk kenalan dulu"
    }

    private val trackerIds = mapOf(
        CLICK_AD_CLOSE to "38877",
        CLICK_AD_SEARCH to "38878",
        CLICK_AD_SEARCH_ENTER to "38879",
        CLICK_AD_SORT to "38880",
        CLICK_AD_CREATE to "38882",
        CLICK_EXISTING_AD to "38883",
        CLICK_AD_SORT_IMPRESSION to "38885",
        CLICK_AD_SORT_CLICK to "38886",
        CLICK_AD_SORT_ORDER to "38887",
        CLICK_EXISTING_AD_SIMPAN_CTA to "38891",
        CLICK_AD_CREATED_STAY to "38892",
        CLICK_AD_CREATED_ENOUGH to "38893",
        CLICK_AD_GROUP_EDIT_NAME to "38894",
        CLICK_AD_GROUP_EDIT_BUDGET to "38895",
        CLICK_NEW_AD_GROUP_CREATE to "38896",
        CLICK_AD_CREATED_CREDIT_STAY to "38897",
        CLICK_AD_CREATED_CREDIT_ADD to "38898",
        CLICK_AD_ADVERTISE_CTA to "38899",
        CLICK_MEET_FIRST to "38900"
    )

    fun getTrackerId(action:String) : String{
        return trackerIds[action] ?: ""
    }
}
