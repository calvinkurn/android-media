package com.tokopedia.topchat.chatroom.view.activity.robot.header

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.topchat.R
import org.hamcrest.CoreMatchers
import com.tokopedia.chat_common.R as chat_commonR

object HeaderResult {

    fun assertToolbarTitle(expectedTitle: String) {
        onView(
            CoreMatchers.allOf(
                ViewMatchers.withId(chat_commonR.id.title),
                ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.toolbar))
            )
        ).check(matches(ViewMatchers.withText(expectedTitle)))
    }
}
