package com.tokopedia.loginregister.login.common

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.Matcher
import org.hamcrest.Matchers.any

fun setVisible(status: Boolean): ViewAction {
    return object : ViewAction {

        override fun getConstraints(): Matcher<View> {
            return any(View::class.java)
        }

        override fun getDescription(): String {
            return "set visibility $status"
        }

        override fun perform(uiController: UiController?, view: View?) {
            when(status) {
                true -> view?.visibility = View.VISIBLE
                false -> view?.visibility = View.GONE
            }
        }

    }
}