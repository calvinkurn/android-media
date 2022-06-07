
package com.tokopedia.tokomember_seller_dashboard.tracker

interface TmTrackerImpl {

    fun viewIntroPage(shopId:String)
    fun clickIntroDaftar(shopId: String)
    fun clickIntroLanjut(shopId: String)
    fun clickDismissBsNonOs(shopId: String)
    fun clickButtonBsNonOs(shopId: String)
    fun viewBSNoAccess(shopId: String)
    fun clickBackHomeBSNoAccess(shopId: String)

    fun clickCardCreationBack(shopId: String)
    fun clickCardCreationButton(shopId: String)
    fun clickCardCancelPrimary(shopId: String)
    fun clickCardCancelSecondary(shopId: String)

    fun clickPopupCancelPrimary(shopId: String)
    fun clickPopupCancelSecondary(shopId: String)

    fun clickProgramExtensionBack(shopId: String , programId:String)
    fun clickProgramExtensionButton(shopId: String, programId: String)
    fun clickProgramExtensionPopUpPrimary(shopId: String,programId: String)
    fun clickProgramExtensionPopUpSecondary(shopId: String, programId: String)

    fun clickProgramCreationButton(shopId: String, programId: String)
    fun clickProgramCreationBack(shopId: String)

    fun clickCouponCreationBack(shopId: String)
    fun clickCouponCreationButton(shopId: String)

    fun clickSummaryBack(shopId: String)
    fun clickSummaryButton(shopId: String, programId: String)

    fun viewBottomSheetHome(shopId: String)
    fun clickDismissBottomSheetHome(shopId: String)
    fun viewHomeTabsSection(shopId: String)

    fun viewProgramListTabSection(shopId: String)
    fun clickProgramListButton(shopId: String)
    fun clickProgramCreationButtonFromProgramList(shopId: String, programId: String)
    fun clickProgramCreationBackFromProgramList(shopId: String)
    fun clickProgramCreationCancelPopupPrimary(shopId: String)
    fun clickProgramCreationCancelPopupSecondary(shopId: String)
    fun clickCouponCreationFromProgramList(shopId: String)
    fun clickBackCouponCreationFromProgramList(shopId: String)
    fun clickSummaryButtonFromProgramList(shopId: String, programId: String)
    fun clickSummaryBackFromProgramList(shopId: String)

    fun clickProgramThreeDot(shopId: String)
    fun clickProgramBsOption(shopId: String)
    fun clickProgramItemButton(shopId: String)

    fun clickProgramChangeButton(shopId: String,programId: String)
    fun clickProgramChangeBack(shopId: String,programId: String)
    fun clickProgramChangePopUpPrimary(shopId: String,programId: String)
    fun clickProgramChangePopUpSecondary(shopId: String,programId: String)

    fun clickCouponChangeBack(shopId: String)
    fun clickCouponChangeButton(shopId: String)

    fun viewCouponListTabSection(shopId: String)
    fun clickCouponListBack(shopId: String)
    fun clickCouponOptionBsQuota(shopId: String)
    fun clickCouponItemButton(shopId: String)
    fun clickBsAddQuotaButton(shopId: String)

    fun clickCouponCancelPopUpPrimary(shopId: String)
    fun clickCouponCancelPopUpSecondary(shopId: String)

    fun viewProgramDetail(shopId: String)

}
