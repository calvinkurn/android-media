package com.tokopedia.linter.unify


import com.android.SdkConstants
import com.tokopedia.linter.LinterConstants
import com.tokopedia.linter.LinterConstants.Classes.BOTTOM_SHEET_DIALOG
import com.tokopedia.linter.LinterConstants.Classes.DIALOG_CLASS_NAME
import com.tokopedia.linter.LinterConstants.Classes.FLOATING_ACTION_BUTTON
import com.tokopedia.linter.LinterConstants.Classes.SEARCH_BAR_VIEW
import com.tokopedia.linter.LinterConstants.Classes.SNACK_BAR
import com.tokopedia.linter.LinterConstants.UnifyClasses.BOTTOM_SHEET
import com.tokopedia.linter.LinterConstants.UnifyClasses.BUTTON
import com.tokopedia.linter.LinterConstants.UnifyClasses.DATE_PICKER_UNIFY
import com.tokopedia.linter.LinterConstants.UnifyClasses.DIALOG
import com.tokopedia.linter.LinterConstants.UnifyClasses.IMAGE_BUTTON
import com.tokopedia.linter.LinterConstants.UnifyClasses.PROGRESS_BAR
import com.tokopedia.linter.LinterConstants.UnifyClasses.SEARCH_BAR
import com.tokopedia.linter.LinterConstants.UnifyClasses.TAB
import com.tokopedia.linter.LinterConstants.UnifyClasses.TOASTER
import com.tokopedia.linter.LinterConstants.UnifyClasses.TYPOGRAPHY

object UnifyComponentsList {


    val widgetViewMapping = mapOf(
            SdkConstants.TEXT_VIEW to UnifyMapping(TYPOGRAPHY),
            SdkConstants.IMAGE_BUTTON to UnifyMapping(IMAGE_BUTTON),
            SdkConstants.BUTTON to UnifyMapping(BUTTON),
            SdkConstants.SEARCH_VIEW to UnifyMapping(SEARCH_BAR),
            SdkConstants.PROGRESS_BAR to UnifyMapping(PROGRESS_BAR)
    )

    fun getUnifyMapping(): Map<String, UnifyMapping> {
        return mapOf(
                SdkConstants.FLOATING_ACTION_BUTTON.defaultName() to UnifyMapping(LinterConstants.UnifyClasses.FLOATING_BUTTON),
                LinterConstants.Classes.FLOATING_BUTTON to UnifyMapping(LinterConstants.UnifyClasses.FLOATING_BUTTON),
                FLOATING_ACTION_BUTTON to UnifyMapping(LinterConstants.UnifyClasses.FLOATING_BUTTON),
                SdkConstants.TAB_LAYOUT.defaultName() to UnifyMapping(TAB),
                SEARCH_BAR_VIEW to UnifyMapping(SEARCH_BAR),
                BOTTOM_SHEET_DIALOG to UnifyMapping(BOTTOM_SHEET),
                LinterConstants.Classes.DATE_PICKER to UnifyMapping(LinterConstants.UnifyClasses.DATE_PICKER),
                DATE_PICKER_UNIFY to UnifyMapping(LinterConstants.UnifyClasses.DATE_TIME_PICKER_UNIFY),
                DIALOG_CLASS_NAME to UnifyMapping(DIALOG),
                SNACK_BAR to UnifyMapping(TOASTER)


        )
    }

}