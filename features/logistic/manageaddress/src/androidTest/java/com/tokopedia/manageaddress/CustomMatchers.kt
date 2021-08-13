package com.tokopedia.manageaddress

import android.view.View
import androidx.test.espresso.matcher.BoundedMatcher
import com.tokopedia.unifycomponents.CardUnify
import org.hamcrest.Description

object CustomMatchers {
    fun isCardUnifyChecked() = object : BoundedMatcher<View, CardUnify>(CardUnify::class.java) {
        override fun describeTo(description: Description?) {
            description?.appendText("is card unify having a checked state")
        }

        override fun matchesSafely(item: CardUnify?): Boolean {
            return item!!.hasCheckIcon
        }
    }
}