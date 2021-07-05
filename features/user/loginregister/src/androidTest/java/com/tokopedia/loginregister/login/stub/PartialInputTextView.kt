package com.tokopedia.loginregister.login.stub

import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf


/**
 * Created by Yoris Prayogo on 18/02/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */

fun PartialInputTextView(): ViewAction? {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return allOf(isDisplayed(), isAssignableFrom(AutoCompleteTextView::class.java))
        }

        override fun perform(uiController: UiController?, view: View) {
            (view as EditText).setText("yoris.prayogo+3@tokopedia.com")
        }

        override fun getDescription(): String {
            return "input email phone"
        }
    }
}
