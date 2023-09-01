package com.tokopedia.topchat.chatroom.view.activity.robot.srw

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.withIndex
import com.tokopedia.topchat.matchers.withRecyclerView
import org.hamcrest.CoreMatchers
import com.tokopedia.coachmark.R as coachmarkR

object SrwRobot {
    fun clickSrwQuestion(position: Int) {
        onView(
            withRecyclerView(R.id.rv_srw_partial)
                .atPositionOnView(position, R.id.tp_srw_title)
        ).perform(click())
    }

    fun dismissSrwOnboarding() {
        onView(ViewMatchers.withId(coachmarkR.id.simple_ic_close))
            .inRoot(isPlatformPopup())
            .perform(click())
    }

    fun clickOnSrwPartial() {
        onView(withIndex(ViewMatchers.withId(R.id.tp_srw_container_partial), 0))
            .perform(click())
    }

    fun clickSrwPreviewItemAt(position: Int) {
        onView(
            CoreMatchers.allOf(
                withRecyclerView(R.id.rv_srw_partial).atPositionOnView(
                    position,
                    R.id.tp_srw_title
                ),
                ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.cl_attachment_preview))
            )
        ).perform(click())
    }

    fun clickSrwPreviewExpandCollapse() {
        onView(
            CoreMatchers.allOf(
                ViewMatchers.withId(R.id.rv_srw_content_container),
                ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.cl_attachment_preview))
            )
        ).perform(click())
    }

    fun clickSrwBubbleExpandCollapse(position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom).atPositionOnView(
                position,
                R.id.tp_srw_container_partial
            )
        ).perform(click())
    }
}
