package com.tokopedia.tokomember_seller_dashboard.tracker

open class DefaultTmTrackerImpl : TmTrackerImpl {

    override fun viewIntroPage(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickIntroDaftar(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickIntroLanjut(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickDismissBsNonOs(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickButtonBsNonOs(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickBsNonOsBack(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickCardCreationBack(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickCardCreationButton(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickCardCancelPrimary(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickCardCancelSecondary(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickProgramCreationBack(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickProgramCreationButton(shopId: String) {
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
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickProgramExtensionButton(shopId: String, programId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickProgramExtensionPopUpPrimary(shopId: String, programId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickProgramExtensionPopUpSecondary(shopId: String, programId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickCouponCreationBack(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickCouponCreationButton(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickSummaryBack(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickSummaryButton(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun viewBottomSheetHome(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickDismissBottomSheetHome(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun viewHomeTabsSection(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun viewProgramListTabSection(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
        Tracker.fillCommonItems(map)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun viewProgramListButton(shopId: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = ""
        map[Tracker.Constants.EVENT_CATEGORY] = ""
        map[Tracker.Constants.EVENT_ACTION] = ""
        map[Tracker.Constants.EVENT_LABEL] = ""
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
