package com.tokopedia.topchat.chatroom.view.activity.robot.general

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.withRecyclerView
import org.hamcrest.Matcher

object GeneralResult {
    fun assertViewInRecyclerViewAt(
        position: Int,
        viewId: Int,
        matcher: Matcher<View>
    ) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom).atPositionOnView(
                position, viewId
            )
        ).check(matches(matcher))
    }
}