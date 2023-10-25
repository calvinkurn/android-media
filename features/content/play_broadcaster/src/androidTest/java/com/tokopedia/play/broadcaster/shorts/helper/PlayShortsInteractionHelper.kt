package com.tokopedia.play.broadcaster.shorts.helper

import androidx.test.espresso.Espresso
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.content.test.util.click
import com.tokopedia.content.test.util.clickItemRecyclerView
import com.tokopedia.content.test.util.type
import com.tokopedia.content.product.picker.R as contentproductpickerR
import com.tokopedia.play.broadcaster.R
import com.tokopedia.content.common.R as contentcommonR
import com.tokopedia.dialog.R as dialogR
import com.tokopedia.unifycomponents.R as unifycomponentsR

/**
 * Created By : Jonathan Darwin on December 12, 2022
 */

/** General */
fun clickCloseBottomSheet() {
    click(unifycomponentsR.id.bottom_sheet_close)
}

fun clickClosePreparationPage() {
    click(contentcommonR.id.img_com_toolbar_nav_icon)
}

fun clickContinueOnLeaveConfirmationPopup() {
    click(dialogR.id.dialog_btn_primary)
}

fun completeMandatoryMenu() {
    clickMenuTitle()
    inputTitle()
    submitTitle()

    clickMenuProduct()
    delay()
    selectProduct()
    delay()
    clickSubmitProductTag()
    delay()
    clickCloseProductSummaryBottomSheet()
}

fun clickContinueOnPreparationPage() {
    click(R.id.btn_next)
}

/** Switch Account */
fun clickToolbar() {
    click(contentcommonR.id.text_com_toolbar_subtitle)
}

fun clickShopAccount() {
    clickItemRecyclerView(contentcommonR.id.rv_feed_account, 0)
}

fun clickUserAccount() {
    clickItemRecyclerView(contentcommonR.id.rv_feed_account, 1)
}

fun clickCancelSwitchAccount() {
    click(dialogR.id.dialog_btn_primary)
}

/** Ugc Onboarding */
fun clickTextFieldUsername() {
    click(unifycomponentsR.id.text_field_input)
}

fun inputUsername(username: String = "pokemon") {
    type(unifycomponentsR.id.text_field_input, username)
}

fun clickAcceptTnc() {
    Espresso.closeSoftKeyboard()
    click(contentcommonR.id.cbx_tnc)
}

fun clickSubmitUgcOnboarding() {
    click(contentcommonR.id.btn_continue)
}

/** Menu */
fun clickMenuTitle() {
    clickItemRecyclerView(R.id.rv_menu, 0)
}

fun clickMenuProduct() {
    clickItemRecyclerView(R.id.rv_menu, 1)
}

fun clickMenuCover() {
    clickItemRecyclerView(R.id.rv_menu, 2)
}

/** Title Form */
fun inputTitle(text: String = "pokemon") {
    type(unifycomponentsR.id.text_field_input, text)
}

fun clearTitle() {
    click(unifycomponentsR.id.text_field_icon_close)
}

fun submitTitle() {
    Espresso.closeSoftKeyboard()
    click(R.id.btn_setup_title)
}

fun clickBackTitleForm() {
    click(unifycomponentsR.id.bottom_sheet_close)
}

/** Cover Form */
fun clickBackCoverForm() {
    delay()
    click(unifycomponentsR.id.bottom_sheet_close)
}

fun clickSelectCover() {
    delay()
    click(R.id.cl_cover_form_preview)
}

/** Product Picker SGC */
fun selectProduct(idx: Int = 0) {
    delay()
    clickItemRecyclerView(contentproductpickerR.id.rv_products, idx)
}

fun clickSubmitProductTag() {
    Espresso.closeSoftKeyboard()
    click(contentproductpickerR.id.btn_next)
}

fun clickCloseProductSummaryBottomSheet() {
    click(unifycomponentsR.id.bottom_sheet_close)
}

fun clickSearchBarProductPickerSGC() {
    click(contentproductpickerR.id.search_bar)
}

fun clickSortChip() {
    click(contentproductpickerR.id.chips_sort)
}

fun clickEtalaseAndCampaignChip() {
    click(contentproductpickerR.id.chips_etalase)
}

fun selectSortType() {
    clickItemRecyclerView(contentproductpickerR.id.rv_sort, 1)
}

fun clickSaveSort() {
    click(unifycomponentsR.id.bottom_sheet_action)
}

fun selectEtalaseOrCampaign(idx: Int) {
    clickItemRecyclerView(contentproductpickerR.id.rv_etalase, idx)
}

fun clickAddMoreProduct() {
    click(unifycomponentsR.id.bottom_sheet_action)
}

fun clickDeleteOnFirstProduct() {
    clickItemRecyclerView(contentproductpickerR.id.rv_product_summaries, 1, contentproductpickerR.id.ic_product_summary_delete)
}

fun clickNextOnProductPickerSummary() {
    click(contentproductpickerR.id.btn_done)
}


/** Product Picker UGC */
fun clickBreadcrumb() {
    click(contentcommonR.id.tv_cc_product_tag_product_source)
}

fun clickCloseBreadcrumb() {
    click(unifycomponentsR.id.bottom_sheet_close)
}

fun clickProductTagSourceTokopedia() {
    click(contentcommonR.id.cl_global_search)
}

/** Summary page */
fun clickBackOnSummaryPage() {
    click(R.id.ic_back)
}

fun clickContentTag(idx: Int = 0) {
    clickItemRecyclerView(R.id.rv_tags_recommendation, 0)
}

fun clickUploadVideo() {
    click(R.id.btn_upload_video)
}

fun clickRefreshContentTag() {
    click(unifycomponentsR.id.refreshID)
}
