package com.tokopedia.product.addedit.utils

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.product.addedit.R
import com.tokopedia.product.manage.common.feature.draft.constant.AddEditProductDraftConstant.DB_NAME
import com.tokopedia.product.manage.common.feature.draft.constant.AddEditProductDraftConstant.DB_TABLE
import com.tokopedia.product.manage.common.feature.draft.constant.AddEditProductDraftConstant.DB_VERSION_9
import com.tokopedia.test.application.espresso_component.CommonMatcher
import org.hamcrest.Matchers

object InstrumentedTestUtil {

    fun performDialogSecondaryClick() {
        Espresso.onView(CommonMatcher
                .firstView(ViewMatchers.withId(R.id.dialog_btn_secondary)))
                .perform(ViewActions.click())
    }

    fun performDialogPrimaryClick() {
        Espresso.onView(CommonMatcher
                .firstView(ViewMatchers.withId(R.id.dialog_btn_primary)))
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
                .perform(ViewActions.scrollTo())

        Espresso.onView(CommonMatcher
                .firstView(ViewMatchers.withId(id)))
                .perform(ViewActions.click())
    }

    fun performReplaceText(id: Int, text: String) {
        Espresso.onView(CommonMatcher
                .firstView(ViewMatchers.withId(id)))
                .perform(ViewActions.scrollTo())

        Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.text_field_input), ViewMatchers.isDescendantOfA(ViewMatchers.withId(id))))
                .perform(ViewActions.typeText(text), ViewActions.closeSoftKeyboard())
    }

    fun performPressBack() {
        Espresso.pressBackUnconditionally()
    }

    fun deleteAllDraft() {
        val db: SQLiteOpenHelper = object : SQLiteOpenHelper(
                InstrumentationRegistry.getInstrumentation().context,
                DB_NAME,
                null,
                DB_VERSION_9
        ) {
            override fun onCreate(p0: SQLiteDatabase?) {}
            override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}
        }

        try {
            val dbWrite1 = db.writableDatabase
            dbWrite1.execSQL("DELETE FROM $DB_TABLE")
            dbWrite1.close()
        } catch (e: Exception) { }
    }
}