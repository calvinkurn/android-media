package com.tokopedia.tokomember_seller_dashboard.tracker

import com.tokopedia.tokomember_seller_dashboard.tracker.Tracker.Category.TM_DASHBOARD_HOME
import com.tokopedia.tokomember_seller_dashboard.tracker.Tracker.Event.EVENT_CLICK_BGP_IRIS
import com.tokopedia.tokomember_seller_dashboard.tracker.Tracker.Event.EVENT_VIEW_BGP_IRIS

open class DefaultTmTrackerImpl : TmTrackerImpl {

    override fun viewIntroPage(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_VIEW_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_DASHBOARD_INTRO
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_INTRO_PAGE
        map[Tracker.Constants.EVENT_LABEL] = shopId
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickIntroDaftar(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_DASHBOARD_INTRO
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_INTRO_DAFTAR
        map[Tracker.Constants.EVENT_LABEL] = shopId
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickIntroLanjut(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_DASHBOARD_INTRO
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_INTRO_LANJUT
        map[Tracker.Constants.EVENT_LABEL] = shopId
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickDismissBsNonOs(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_DASHBOARD_INTRO
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_DISMISS_BS_NON_OS
        map[Tracker.Constants.EVENT_LABEL] = shopId
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickButtonBsNonOs(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_DASHBOARD_INTRO
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_BUTTON_BS_NON_OS
        map[Tracker.Constants.EVENT_LABEL] = shopId
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun viewBSNoAccess(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_VIEW_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_DASHBOARD_INTRO
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_BS_NON_ACCESS
        map[Tracker.Constants.EVENT_LABEL] = shopId
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickBackHomeBSNoAccess(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_DASHBOARD_INTRO
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_BS_NON_OS_BACK
        map[Tracker.Constants.EVENT_LABEL] = shopId
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickCardCreationBack(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_DASHBOARD_DAFTAR
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_CARD_CREATION_BACK
        map[Tracker.Constants.EVENT_LABEL] = shopId
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickCardCreationButton(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_DASHBOARD_DAFTAR
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_CARD_CREATION_BUTTON
        map[Tracker.Constants.EVENT_LABEL] = shopId
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickCardCancelPrimary(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_DASHBOARD_DAFTAR
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_CARD_CANCEL_PRIMARY
        map[Tracker.Constants.EVENT_LABEL] = shopId
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickCardCancelSecondary(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_DASHBOARD_DAFTAR
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_CARD_CANCEL_SECONDARY
        map[Tracker.Constants.EVENT_LABEL] = shopId
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickPopupCancelPrimary(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_DASHBOARD_DAFTAR
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_POPUP_CANCEL_PRIMARY
        map[Tracker.Constants.EVENT_LABEL] = shopId
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickPopupCancelSecondary(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_DASHBOARD_DAFTAR
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_POPUP_CANCEL_SECONDARY
        map[Tracker.Constants.EVENT_LABEL] = shopId
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickProgramCreationBack(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_DASHBOARD_DAFTAR
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_BACK_PROGRAM_CREATION_FROM_P_LIST
        map[Tracker.Constants.EVENT_LABEL] = shopId
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickProgramCreationButton(shopId: String, programId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_DASHBOARD_DAFTAR
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_PROGRAM_CREATION_BUTTON
        map[Tracker.Constants.EVENT_LABEL] = "$shopId - $programId"
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickCouponCreationBack(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_DASHBOARD_DAFTAR
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_COUPON_CREATION_BACK
        map[Tracker.Constants.EVENT_LABEL] = shopId
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickCouponCreationButton(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_DASHBOARD_DAFTAR
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_COUPON_CREATION_BUTTON
        map[Tracker.Constants.EVENT_LABEL] = shopId
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickSummaryBack(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_DASHBOARD_DAFTAR
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_SUMMARY_BACK
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickSummaryButton(shopId: String, programId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_DASHBOARD_DAFTAR
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_SUMMARY_BUTTON
        map[Tracker.Constants.EVENT_LABEL] = "$shopId - $programId"
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun viewBottomSheetHome(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_VIEW_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = TM_DASHBOARD_HOME
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_HOME_BOTTOM_SHEET
        map[Tracker.Constants.EVENT_LABEL] = shopId
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickDismissBottomSheetHome(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = TM_DASHBOARD_HOME
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_DISMISS_HOME_BOTTOM_SHEET
        map[Tracker.Constants.EVENT_LABEL] = shopId
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun viewHomeTabsSection(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_VIEW_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = TM_DASHBOARD_HOME
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_HOME_TAB_SECTION
        map[Tracker.Constants.EVENT_LABEL] = shopId
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun viewProgramListTabSection(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_VIEW_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = TM_DASHBOARD_HOME
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_PROGRAM_LIST_TAB_SECTION
        map[Tracker.Constants.EVENT_LABEL] = shopId
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickProgramListButton(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = TM_DASHBOARD_HOME
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_PROGRAM_LIST_BUTTON
        map[Tracker.Constants.EVENT_LABEL] = shopId
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickProgramCreationButtonFromProgramList(shopId: String, programId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_DASHBOARD_CREATE_PROGRAM
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_PROGRAM_CREATION_BUTTON
        map[Tracker.Constants.EVENT_LABEL] = "$shopId - $programId"
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickProgramCreationBackFromProgramList(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_DASHBOARD_CREATE_PROGRAM
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_BACK_PROGRAM_CREATION_FROM_P_LIST
        map[Tracker.Constants.EVENT_LABEL] = shopId
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickProgramCreationCancelPopupPrimary(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_DASHBOARD_CREATE_PROGRAM
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_PROGRAM_CREATION_CANCEL_POPUP_PRIMARY
        map[Tracker.Constants.EVENT_LABEL] = shopId
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickProgramCreationCancelPopupSecondary(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_DASHBOARD_CREATE_PROGRAM
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_PROGRAM_CREATION_CANCEL_POPUP_SECONDARY
        map[Tracker.Constants.EVENT_LABEL] = shopId
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickCouponCreationFromProgramList(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_DASHBOARD_CREATE_PROGRAM
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_COUPON_CREATION_BUTTON_FROM_P_LIST
        map[Tracker.Constants.EVENT_LABEL] = shopId
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickBackCouponCreationFromProgramList(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_DASHBOARD_CREATE_PROGRAM
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_COUPON_CREATION_BACK_FROM_P_LIST
        map[Tracker.Constants.EVENT_LABEL] = shopId
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickSummaryButtonFromProgramList(shopId: String, programId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_DASHBOARD_CREATE_PROGRAM
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_SUMMARY_BUTTON
        map[Tracker.Constants.EVENT_LABEL] = "$shopId - $programId"
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickSummaryBackFromProgramList(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.TM_DASHBOARD_CREATE_PROGRAM
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_SUMMARY_BACK
        map[Tracker.Constants.EVENT_LABEL] = shopId
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }





    override fun clickProgramThreeDot(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickProgramBsOption(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickProgramExtensionBack(shopId: String, programId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickProgramExtensionButton(shopId: String, programId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickProgramExtensionPopUpPrimary(shopId: String, programId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickProgramExtensionPopUpSecondary(shopId: String, programId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = EVENT_CLICK_BGP_IRIS
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickProgramItemButton(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickProgramChangeButton(shopId: String, programId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickProgramChangeBack(shopId: String, programId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickProgramChangePopUpPrimary(shopId: String, programId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickProgramChangePopUpSecondary(shopId: String, programId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickCouponChangeBack(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickCouponChangeButton(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun viewCouponListTabSection(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickCouponListBack(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickCouponOptionBsQuota(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickCouponItemButton(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickBsAddQuotaButton(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickCouponCancelPopUpPrimary(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickCouponCancelPopUpSecondary(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun viewProgramDetail(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }
}
