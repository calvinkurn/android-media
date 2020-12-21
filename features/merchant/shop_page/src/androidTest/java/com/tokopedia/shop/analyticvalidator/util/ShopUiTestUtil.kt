package com.tokopedia.shop.analyticvalidator.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf

object ShopUiTestUtil {

    fun rvScrollToPositionWithOffset(position: Int, offset: Int = 0): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return AllOf.allOf(ViewMatchers.isDisplayed(), ViewMatchers.isAssignableFrom(RecyclerView::class.java))
            }

            override fun getDescription(): String {
                return "Scroll to item position with offset"
            }

            override fun perform(uiController: UiController?, view: View) {
                val rv = view as RecyclerView
                val layoutManager = rv.layoutManager
                layoutManager?.let{
                    if(it is StaggeredGridLayoutManager){
                        it.scrollToPositionWithOffset(position, 0)
                        return
                    }
                }
                throw RuntimeException("Layout manager not  supported")
            }
        }
    }


}