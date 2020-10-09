package com.tokopedia.linter.unify

import com.android.SdkConstants
import com.tokopedia.linter.FLOATING_ACTION_BUTTON_OLD_NAME
import com.tokopedia.linter.FLOATING_BUTTON_OLD_NAME
import com.tokopedia.linter.SEARCHBARVIEW_OLD_NAME

object UnifyComponentsList {
    const val TYPOGRAPHY = "com.tokopedia.unifyprinciples.Typography"
    const val FLOATING_BUTTON_NEW_NAME = "com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify"
    const val IMAGE_BUTTON_NEW_NAME = "com.tokopedia.unifycomponents.UnifyImageButton"
    const val BUTTON_NEW_NAME = "com.tokopedia.unifycomponents.UnifyButton"
    const val TAB_NEW_NAME = "com.tokopedia.unifycomponents.TabsUnify"
    const val SEARCH_BAR_NEW_NAME = "com.tokopedia.unifycomponents.SearchBarUnify"
    const val PROGRESS_BAR_NEW_NAME = "com.tokopedia.unifycomponents.LoaderUnify"
}

fun getUnifyMapping(): Map<String, UnifyMapping> {
    return mapOf(
            SdkConstants.TEXT_VIEW to UnifyMapping(SdkConstants.TEXT_VIEW, UnifyComponentsList.TYPOGRAPHY),
            SdkConstants.FLOATING_ACTION_BUTTON.defaultName() to UnifyMapping(SdkConstants.FLOATING_ACTION_BUTTON.defaultName(), UnifyComponentsList.FLOATING_BUTTON_NEW_NAME),
            FLOATING_BUTTON_OLD_NAME to UnifyMapping(FLOATING_BUTTON_OLD_NAME, UnifyComponentsList.FLOATING_BUTTON_NEW_NAME),
            FLOATING_ACTION_BUTTON_OLD_NAME to UnifyMapping(FLOATING_ACTION_BUTTON_OLD_NAME, UnifyComponentsList.FLOATING_BUTTON_NEW_NAME),
            SdkConstants.IMAGE_BUTTON to UnifyMapping(SdkConstants.IMAGE_BUTTON, UnifyComponentsList.IMAGE_BUTTON_NEW_NAME),
            SdkConstants.BUTTON to UnifyMapping(SdkConstants.BUTTON, UnifyComponentsList.BUTTON_NEW_NAME),
            SdkConstants.TAB_LAYOUT.defaultName() to UnifyMapping(SdkConstants.TAB_LAYOUT.defaultName(), UnifyComponentsList.TAB_NEW_NAME),
            SdkConstants.CLASS_TAB_LAYOUT.defaultName() to UnifyMapping(SdkConstants.CLASS_TAB_LAYOUT.defaultName(), UnifyComponentsList.TAB_NEW_NAME),
            SdkConstants.SEARCH_VIEW to UnifyMapping(SdkConstants.SEARCH_VIEW, UnifyComponentsList.SEARCH_BAR_NEW_NAME),
            SEARCHBARVIEW_OLD_NAME to UnifyMapping(SEARCHBARVIEW_OLD_NAME, UnifyComponentsList.SEARCH_BAR_NEW_NAME),
            SdkConstants.PROGRESS_BAR to UnifyMapping(SdkConstants.PROGRESS_BAR, UnifyComponentsList.PROGRESS_BAR_NEW_NAME)


    )
}