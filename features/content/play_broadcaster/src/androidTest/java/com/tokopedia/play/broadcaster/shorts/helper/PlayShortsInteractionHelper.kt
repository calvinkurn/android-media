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

/** Switch Account */
fun clickToolbar() {
    click(contentCommonR.id.text_com_toolbar_subtitle)
}

fun closeSwitchAccountBottomSheet() {
    click(unifyR.id.bottom_sheet_close)
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


/** Title Form */
fun openTitleForm() {
    clickItemRecyclerView(R.id.rv_menu, 0)
}

fun inputTitle(text: String = "pokemon") {
    type(unifyR.id.text_field_input, text)
}

fun submitTitle() {
    pressActionSoftKeyboard(unifyR.id.text_field_input)
}
