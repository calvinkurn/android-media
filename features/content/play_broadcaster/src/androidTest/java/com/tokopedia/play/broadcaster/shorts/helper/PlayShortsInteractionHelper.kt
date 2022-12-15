package com.tokopedia.play.broadcaster.shorts.helper

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.content.test.util.*
import com.tokopedia.play.broadcaster.shorts.const.DEFAULT_DELAY
import com.tokopedia.content.common.R as contentCommonR
import com.tokopedia.unifycomponents.R as unifyR
import com.tokopedia.play.broadcaster.R
import com.tokopedia.dialog.R as dialogR
import org.hamcrest.CoreMatchers

/**
 * Created By : Jonathan Darwin on December 12, 2022
 */

/** General */
fun clickCloseBottomSheet() {
    click(unifyR.id.bottom_sheet_close)
}

fun clickClosePreparationPage() {
    click(contentCommonR.id.img_com_toolbar_nav_icon)
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

/** Switch Account */
fun clickToolbar() {
    click(contentCommonR.id.text_com_toolbar_subtitle)
}

fun clickShopAccount() {
    clickItemRecyclerView(contentCommonR.id.rv_feed_account, 0)
}

fun clickUserAccount() {
    clickItemRecyclerView(contentCommonR.id.rv_feed_account, 1)
}

fun clickCancelSwitchAccount() {
    click(dialogR.id.dialog_btn_primary)
}

/** Ugc Onboarding */
fun clickTextFieldUsername() {
    click(unifyR.id.text_field_input)
}

fun inputUsername(username: String = "pokemon") {
    type(unifyR.id.text_field_input, username)
}

fun clickAcceptTnc() {
    click(contentCommonR.id.cbx_tnc)
}

fun clickSubmitUgcOnboarding() {
    click(contentCommonR.id.btn_continue)
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
    type(unifyR.id.text_field_input, text)
}

fun clearTitle() {
    click(unifyR.id.text_field_icon_close)
}

fun submitTitle() {
    pressActionSoftKeyboard(unifyR.id.text_field_input)
}

fun clickBackTitleForm() {
    click(R.id.ic_close_title_form)
}

/** Cover Form */
fun clickBackCoverForm() {
    click(R.id.ic_close_cover_form)
}

fun clickSelectCover() {
    click(R.id.cl_cover_form_preview)
}

/** Product Tag */
fun selectProduct(idx: Int = 0) {
    clickItemRecyclerView(R.id.rv_products, idx)
}

fun clickSubmitProductTag() {
    click(R.id.btn_next)
}

fun clickCloseProductSummaryBottomSheet() {
    click(unifyR.id.bottom_sheet_close)
}
