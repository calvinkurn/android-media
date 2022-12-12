package com.tokopedia.play.broadcaster.shorts.helper

import com.tokopedia.content.test.espresso.delay
import com.tokopedia.content.test.util.click
import com.tokopedia.play.broadcaster.shorts.const.DEFAULT_DELAY
import com.tokopedia.content.common.R as contentCommonR
import com.tokopedia.unifycomponents.R as unifyR

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
