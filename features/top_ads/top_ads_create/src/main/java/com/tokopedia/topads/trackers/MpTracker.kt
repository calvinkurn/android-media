package com.tokopedia.topads.trackers

import com.tokopedia.topads.trackers.MpTrackerConst.Action.CLICK_AD_ADVERTISE_CTA
import com.tokopedia.topads.trackers.MpTrackerConst.Action.CLICK_AD_CLOSE
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
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.topads.trackers.MpTrackerConst.getTrackerId


object MpTracker : BaseTrackerConst() {
    private val trackApp = TrackApp.getInstance().gtm

    private fun fillCommonData() : MutableMap<String,Any>{
        val map = mutableMapOf<String,Any>()
        map[Event.KEY] = MpTrackerConst.EVENT
        map[Category.KEY] = MpTrackerConst.EVENT_CATEGORY
        map[Label.KEY] = MpTrackerConst.EVENT_LABEL
        map[BusinessUnit.KEY] = MpTrackerConst.BUSINESS_UNIT
        map[CurrentSite.KEY] = MpTrackerConst.CURRENT_SITE
        return map
    }

    fun clickCloseAdGroupModal(){
        val map = fillCommonData()
        map[Action.KEY] = CLICK_AD_CLOSE
        map[TrackerId.KEY] = getTrackerId(CLICK_AD_CLOSE)
        trackApp.sendGeneralEvent(map)
    }

    fun clickAdSearchBar(){
        val map = fillCommonData()
        map[Action.KEY] = CLICK_AD_SEARCH
        map[TrackerId.KEY] = getTrackerId(CLICK_AD_SEARCH)
        trackApp.sendGeneralEvent(map)
    }

    fun clickAdSearchBarEnter(){
        val map = fillCommonData()
        map[Action.KEY] = CLICK_AD_SEARCH_ENTER
        map[TrackerId.KEY] = getTrackerId(CLICK_AD_SEARCH_ENTER)
        trackApp.sendGeneralEvent(map)
    }

    fun clickAdGroupSortCta(){
        val map = fillCommonData()
        map[Action.KEY] = CLICK_AD_SORT
        map[TrackerId.KEY] = getTrackerId(CLICK_AD_SORT)
        trackApp.sendGeneralEvent(map)
    }

    fun clickExistingAdGroup(){
        val map = fillCommonData()
        map[Action.KEY] = CLICK_EXISTING_AD
        map[TrackerId.KEY] = getTrackerId(CLICK_EXISTING_AD)
        trackApp.sendGeneralEvent(map)
    }

    fun clickAdGroupSortByImpression(){
        val map = fillCommonData()
        map[Action.KEY] = CLICK_AD_SORT_IMPRESSION
        map[TrackerId.KEY] = getTrackerId(CLICK_AD_SORT_IMPRESSION)
        trackApp.sendGeneralEvent(map)
    }

    fun clickAdGroupSortByClick(){
        val map = fillCommonData()
        map[Action.KEY] = CLICK_AD_SORT_CLICK
        map[TrackerId.KEY] = getTrackerId(CLICK_AD_SORT_CLICK)
        trackApp.sendGeneralEvent(map)
    }

    fun clickAdGroupSortByOrder(){
        val map = fillCommonData()
        map[Action.KEY] = CLICK_AD_SORT_ORDER
        map[TrackerId.KEY] = getTrackerId(CLICK_AD_SORT_ORDER)
        trackApp.sendGeneralEvent(map)
    }

    fun clickAdGroupSimpanCta(){
        val map = fillCommonData()
        map[Action.KEY] = CLICK_EXISTING_AD_SIMPAN_CTA
        map[TrackerId.KEY] = getTrackerId(CLICK_EXISTING_AD_SIMPAN_CTA)
        trackApp.sendGeneralEvent(map)
    }

    fun clickAdGroupCreatedStayHereCta(){
        val map = fillCommonData()
        map[Action.KEY] = CLICK_AD_CREATED_STAY
        map[TrackerId.KEY] = getTrackerId(CLICK_AD_CREATED_STAY)
        trackApp.sendGeneralEvent(map)
    }

    fun clickAdGroupCreatedManageCta(){
        val map = fillCommonData()
        map[Action.KEY] = CLICK_AD_CREATED_ENOUGH
        map[TrackerId.KEY] = getTrackerId(CLICK_AD_CREATED_ENOUGH)
        trackApp.sendGeneralEvent(map)
    }

    fun clickNewAdGroupEditName(){
        val map = fillCommonData()
        map[Action.KEY] = CLICK_AD_GROUP_EDIT_NAME
        map[TrackerId.KEY] = getTrackerId(CLICK_AD_GROUP_EDIT_NAME )
        trackApp.sendGeneralEvent(map)
    }

    fun clickNewAdGroupEditBudget(){
        val map = fillCommonData()
        map[Action.KEY] = CLICK_AD_GROUP_EDIT_BUDGET
        map[TrackerId.KEY] = getTrackerId(CLICK_AD_GROUP_EDIT_BUDGET)
        trackApp.sendGeneralEvent(map)
    }

    fun clickCreateNewAdGroupCta(){
        val map = fillCommonData()
        map[Action.KEY] = CLICK_NEW_AD_GROUP_CREATE
        map[TrackerId.KEY] = getTrackerId(CLICK_NEW_AD_GROUP_CREATE)
        trackApp.sendGeneralEvent(map)
    }

    fun clickAddCreditLaterStayCta(){
        val map = fillCommonData()
        map[Action.KEY] = CLICK_AD_CREATED_CREDIT_STAY
        map[TrackerId.KEY] = getTrackerId(CLICK_AD_CREATED_CREDIT_STAY)
        trackApp.sendGeneralEvent(map)
    }

    fun clickAddCreditCta(){
        val map = fillCommonData()
        map[Action.KEY] = CLICK_AD_CREATED_CREDIT_ADD
        map[TrackerId.KEY] = getTrackerId(CLICK_AD_CREATED_CREDIT_ADD)
        trackApp.sendGeneralEvent(map)
    }

    fun clickAdvertiseProductCta(){
        val map = fillCommonData()
        map[Action.KEY] = CLICK_AD_ADVERTISE_CTA
        map[TrackerId.KEY] = getTrackerId(CLICK_AD_ADVERTISE_CTA)
        trackApp.sendGeneralEvent(map)
    }

    fun clickMeetFirstCta(){
        val map = fillCommonData()
        map[Action.KEY] = CLICK_MEET_FIRST
        map[TrackerId.KEY] = getTrackerId(CLICK_MEET_FIRST )
        trackApp.sendGeneralEvent(map)
    }

}
