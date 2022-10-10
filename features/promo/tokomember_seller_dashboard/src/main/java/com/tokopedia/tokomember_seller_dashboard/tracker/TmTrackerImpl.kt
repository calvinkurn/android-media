
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

    fun clickProgramCreationButton(shopId: String, programId: String)
    fun clickProgramCreationBack(shopId: String)

    fun clickCouponCreationBack(shopId: String)
    fun clickCouponCreationButton(shopId: String)

    fun clickSummaryBack(shopId: String)
    fun clickSummaryButton(shopId: String, programId: String)

    fun viewBottomSheetHome(shopId: String)
    fun clickDismissBottomSheetHome(shopId: String)
    fun viewHomeTabsSection(shopId: String)
    fun clickHomeUbahKartu(shopId: String)
    fun clickHomeFeedback(shopId: String)

    fun viewProgramListTabSection(shopId: String)
    fun clickProgramListButton(shopId: String)
    fun clickProgramCreationButtonFromProgramList(shopId: String, programId: String)
    fun clickProgramCreationBackFromProgramList(shopId: String)
    fun clickProgramCreationCancelPopupPrimary(shopId: String)
    fun clickProgramCreationCancelPopupSecondary(shopId: String)
    fun clickCouponCreationFromProgramList(shopId: String)
    fun clickCouponCreationBackFromProgramList(shopId: String)
    fun clickSummaryButtonFromProgramList(shopId: String, programId: String)
    fun clickSummaryBackFromProgramList(shopId: String)

    fun clickCouponChangeBack(shopId: String)
    fun clickCouponChangeButton(shopId: String)

    fun viewCouponListTabSection(shopId: String)
    fun clickButtonCouponList(shopId: String)
    fun clickCreateCouponList(shopId: String)
    fun clickBackCouponList(shopId: String)
    fun clickCouponCancelPopUpPrimary(shopId: String)
    fun clickCouponCancelPopUpSecondary(shopId: String)
    fun clickSpecificCoupon(shopId: String)

    fun clickProgramActiveThreeDot(shopId: String, programId: String)

    //Program Extension
    fun clickProgramBsExtension(shopId: String, programId: String)
    fun clickProgramExtensionButton(shopId: String, programId: String)
    fun clickProgramExtensionCreateButton(shopId: String, programId: String)
    fun clickProgramExtensionBack(shopId: String , programId:String)
    fun clickProgramExtensionPopUpPrimary(shopId: String,programId: String)
    fun clickProgramExtensionPopUpSecondary(shopId: String, programId: String)
    fun clickProgramExtensionCouponCreation(shopId: String, programId: String)
    fun clickProgramExtensionCouponBack(shopId: String, programId: String)
    fun clickProgramExtensionSummaryButton(shopId: String, programId: String)
    fun clickProgramExtensionSummaryBack(shopId: String, programId: String)

    //Program Edit
    fun clickProgramEdit(shopId: String, programId: String)
    fun clickProgramEditButton(shopId: String, programId: String)
    fun clickProgramEditBack(shopId: String, programId: String)
    fun clickProgramEditPopUpPrimary(shopId: String, programId: String)
    fun clickProgramEditPopUpSecondary(shopId: String, programId: String)

    fun clickProgramWaitingThreeDot(shopId: String, programId: String)

    //Program Cancel
    fun clickProgramBsCancel(shopId: String, programId: String)
    fun clickProgramCancelPopUpPrimary(shopId: String, programId: String)
    fun clickProgramCancelPopUpSecondary(shopId: String, programId: String)

    fun clickCouponListThreeDot(shopId: String)

    //Coupon Quota
    fun clickCouponOptionBsQuota(shopId: String)
    fun clickAddQuotaButton(shopId: String)
    fun clickAddQuotaCTA(shopId: String)

    //Program Detail
    fun clickProgramItemButton(shopId: String, programId: String)
    fun viewProgramDetail(shopId: String, programId: String)

    //Coupon Detail
    fun viewCouponDetail(shopId:String)
    fun clickCouponDetailTambahKuota(shopId:String)
    fun clickAddQuotaCouponDetail(shopId:String)

    //Member List
    fun viewMemberList(shopId: String)

    //Edit Card
    fun clickSimpanEditCard(shopId:String)

}
