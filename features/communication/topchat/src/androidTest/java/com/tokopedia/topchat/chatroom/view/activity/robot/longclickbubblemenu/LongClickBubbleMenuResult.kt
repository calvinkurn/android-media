package com.tokopedia.topchat.chatroom.view.activity.robot.longclickbubblemenu

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.topchat.R

object LongClickBubbleMenuResult {

    fun assertNoBottomSheet() {
        onView(withId(R.id.rvMenu)).check(doesNotExist())
    }

}