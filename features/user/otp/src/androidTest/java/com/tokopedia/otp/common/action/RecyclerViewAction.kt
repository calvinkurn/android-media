package com.tokopedia.otp.common.action

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher
import org.hamcrest.Matchers

object RecyclerViewAction {

    fun scrollToPositionViewAction(position: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return Matchers.allOf(ViewMatchers.isAssignableFrom(RecyclerView::class.java), ViewMatchers.isDisplayed())
            }

            override fun getDescription(): String {
                return "Scroll RecyclerView to position $position"
            }

            override fun perform(uiController: UiController, view: View) {
                val recyclerView: RecyclerView = view as RecyclerView
                recyclerView.scrollToPosition(position)
            }
        }
    }
}