package com.tokopedia.product.addedit.utils

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.product.manage.common.feature.draft.constant.AddEditProductDraftConstant.DB_NAME
import com.tokopedia.product.manage.common.feature.draft.constant.AddEditProductDraftConstant.DB_TABLE
import com.tokopedia.product.manage.common.feature.draft.constant.AddEditProductDraftConstant.DB_VERSION_10
import com.tokopedia.test.application.espresso_component.CommonMatcher
import org.hamcrest.CoreMatchers

object InstrumentedTestUtil {

    fun performDialogSecondaryClick() {
        Espresso.onView(CommonMatcher
                .firstView(ViewMatchers.withId(com.tokopedia.dialog.R.id.dialog_btn_secondary)))
                .perform(ViewActions.click())
    }

    fun performDialogPrimaryClick() {
        Espresso.onView(CommonMatcher
                .firstView(ViewMatchers.withId(com.tokopedia.dialog.R.id.dialog_btn_primary)))
                .perform(ViewActions.click())
    }

    fun performClick(id: Int) {
        Espresso.onView(CommonMatcher
                .firstView(ViewMatchers.withId(id)))
                .perform(ViewActions.click())
    }

    fun performScrollAndClick(id: Int) {
        Espresso.onView(CommonMatcher
                .firstView(ViewMatchers.withId(id)))
                .perform(ModifiedScrollToAction())

        Espresso.onView(CommonMatcher
                .firstView(ViewMatchers.withId(id)))
                .perform(ViewActions.click())
    }

    fun performReplaceText(id: Int, text: String) {
        Espresso.onView(CommonMatcher
                .firstView(ViewMatchers.withId(id)))
                .perform(ViewActions.scrollTo())

        Espresso.onView(CoreMatchers.allOf(ViewMatchers.withId(com.tokopedia.unifycomponents.R.id.text_field_input), ViewMatchers.isDescendantOfA(ViewMatchers.withId(id))))
                .perform(ViewActions.typeText(text), ViewActions.closeSoftKeyboard())
    }

    fun performPressBack() {
        Espresso.pressBackUnconditionally()
    }

    fun deleteAllDraft() {
        try {
            val db = object : SQLiteOpenHelper(
                InstrumentationRegistry.getInstrumentation().context,
                DB_NAME,
                null,
                DB_VERSION_10
            ) {
                override fun onCreate(p0: SQLiteDatabase?) {}
                override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}
            }.writableDatabase
            db.execSQL("DELETE FROM $DB_TABLE")
            db.close()
        } catch (e: Exception) { }
    }
}
