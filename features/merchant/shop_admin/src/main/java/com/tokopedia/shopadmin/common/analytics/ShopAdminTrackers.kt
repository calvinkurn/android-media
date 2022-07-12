package com.tokopedia.shopadmin.common.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

class ShopAdminTrackers @Inject constructor() {

    private val tracker by lazy { TrackApp.getInstance().gtm }

    fun impressInvitationPageInputEmail() {
        val mapData = mapOf(
            TrackAppUtils.EVENT to ShopAdminConstants.VIEW_PG_IRIS,
            TrackAppUtils.EVENT_ACTION to ShopAdminConstants.IMPRESSION_INVITATION_PAGE_EMAIL,
            TrackAppUtils.EVENT_CATEGORY to ShopAdminConstants.ADMIN_INVITE_PAGE,
            TrackAppUtils.EVENT_LABEL to "",
            ShopAdminConstants.TRACKER_ID to ShopAdminConstants.TRACKER_ID_33207,
            ShopAdminConstants.BUSINESS_UNIT to ShopAdminConstants.PHYSICAL_GOODS,
            ShopAdminConstants.CURRENT_SITE to ShopAdminConstants.TOKOPEDIA_MARKET_PLACE
        )
        tracker.sendGeneralEvent(mapData)
    }

    fun impressInvitationPageNoInputEmail() {
        val mapData = mapOf(
            TrackAppUtils.EVENT to ShopAdminConstants.VIEW_PG_IRIS,
            TrackAppUtils.EVENT_ACTION to ShopAdminConstants.IMPRESSION_INVITATION_PAGE_NO_EMAIL,
            TrackAppUtils.EVENT_CATEGORY to ShopAdminConstants.ADMIN_INVITE_PAGE,
            TrackAppUtils.EVENT_LABEL to "",
            ShopAdminConstants.TRACKER_ID to ShopAdminConstants.TRACKER_ID_33208,
            ShopAdminConstants.BUSINESS_UNIT to ShopAdminConstants.PHYSICAL_GOODS,
            ShopAdminConstants.CURRENT_SITE to ShopAdminConstants.TOKOPEDIA_MARKET_PLACE
        )
        tracker.sendGeneralEvent(mapData)
    }

    fun impressExpiredStatus() {
        val mapData = mapOf(
            TrackAppUtils.EVENT to ShopAdminConstants.VIEW_PG_IRIS,
            TrackAppUtils.EVENT_ACTION to ShopAdminConstants.IMPRESSION_EXPIRED_STATUS,
            TrackAppUtils.EVENT_CATEGORY to ShopAdminConstants.ADMIN_INVITE_PAGE,
            TrackAppUtils.EVENT_LABEL to "",
            ShopAdminConstants.TRACKER_ID to ShopAdminConstants.TRACKER_ID_33209,
            ShopAdminConstants.BUSINESS_UNIT to ShopAdminConstants.PHYSICAL_GOODS,
            ShopAdminConstants.CURRENT_SITE to ShopAdminConstants.TOKOPEDIA_MARKET_PLACE
        )
        tracker.sendGeneralEvent(mapData)
    }

    fun impressRejectedStatus() {
        val mapData = mapOf(
            TrackAppUtils.EVENT to ShopAdminConstants.VIEW_PG_IRIS,
            TrackAppUtils.EVENT_ACTION to ShopAdminConstants.IMPRESSION_REJECTED_STATUS,
            TrackAppUtils.EVENT_CATEGORY to ShopAdminConstants.ADMIN_INVITE_PAGE,
            TrackAppUtils.EVENT_LABEL to "",
            ShopAdminConstants.TRACKER_ID to ShopAdminConstants.TRACKER_ID_33210,
            ShopAdminConstants.BUSINESS_UNIT to ShopAdminConstants.PHYSICAL_GOODS,
            ShopAdminConstants.CURRENT_SITE to ShopAdminConstants.TOKOPEDIA_MARKET_PLACE
        )
        tracker.sendGeneralEvent(mapData)
    }

    fun clickInvitationPageAccept() {
        val mapData = mapOf(
            TrackAppUtils.EVENT to ShopAdminConstants.CLICK_PG,
            TrackAppUtils.EVENT_ACTION to ShopAdminConstants.CLICK_INVITATION_PAGE_ACCEPT,
            TrackAppUtils.EVENT_CATEGORY to ShopAdminConstants.ADMIN_INVITE_PAGE,
            TrackAppUtils.EVENT_LABEL to "",
            ShopAdminConstants.TRACKER_ID to ShopAdminConstants.TRACKER_ID_33211,
            ShopAdminConstants.BUSINESS_UNIT to ShopAdminConstants.PHYSICAL_GOODS,
            ShopAdminConstants.CURRENT_SITE to ShopAdminConstants.TOKOPEDIA_MARKET_PLACE
        )
        tracker.sendGeneralEvent(mapData)
    }

    fun clickInvitationPageReject() {
        val mapData = mapOf(
            TrackAppUtils.EVENT to ShopAdminConstants.CLICK_PG,
            TrackAppUtils.EVENT_ACTION to ShopAdminConstants.CLICK_INVITATION_PAGE_REJECT,
            TrackAppUtils.EVENT_CATEGORY to ShopAdminConstants.ADMIN_INVITE_PAGE,
            TrackAppUtils.EVENT_LABEL to "",
            ShopAdminConstants.TRACKER_ID to ShopAdminConstants.TRACKER_ID_33212,
            ShopAdminConstants.BUSINESS_UNIT to ShopAdminConstants.PHYSICAL_GOODS,
            ShopAdminConstants.CURRENT_SITE to ShopAdminConstants.TOKOPEDIA_MARKET_PLACE
        )
        tracker.sendGeneralEvent(mapData)
    }

    fun impressionAcceptedPage() {
        val mapData = mapOf(
            TrackAppUtils.EVENT to ShopAdminConstants.VIEW_PG_IRIS,
            TrackAppUtils.EVENT_ACTION to ShopAdminConstants.IMPRESSION_ACCEPTED_PAGE,
            TrackAppUtils.EVENT_CATEGORY to ShopAdminConstants.ADMIN_INVITE_PAGE,
            TrackAppUtils.EVENT_LABEL to "",
            ShopAdminConstants.TRACKER_ID to ShopAdminConstants.TRACKER_ID_33213,
            ShopAdminConstants.BUSINESS_UNIT to ShopAdminConstants.PHYSICAL_GOODS,
            ShopAdminConstants.CURRENT_SITE to ShopAdminConstants.TOKOPEDIA_MARKET_PLACE
        )
        tracker.sendGeneralEvent(mapData)
    }

    fun impressionRejectedPage() {
        val mapData = mapOf(
            TrackAppUtils.EVENT to ShopAdminConstants.VIEW_PG_IRIS,
            TrackAppUtils.EVENT_ACTION to ShopAdminConstants.IMPRESSION_REJECTED_PAGE,
            TrackAppUtils.EVENT_CATEGORY to ShopAdminConstants.ADMIN_INVITE_PAGE,
            TrackAppUtils.EVENT_LABEL to "",
            ShopAdminConstants.TRACKER_ID to ShopAdminConstants.TRACKER_ID_33214,
            ShopAdminConstants.BUSINESS_UNIT to ShopAdminConstants.PHYSICAL_GOODS,
            ShopAdminConstants.CURRENT_SITE to ShopAdminConstants.TOKOPEDIA_MARKET_PLACE
        )
        tracker.sendGeneralEvent(mapData)
    }

}