package com.tokopedia.topchat.chatroom.view.activity.robot.srw

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.withRecyclerView

object SrwRobot {
    fun clickSrwQuestion(position: Int) {
        Espresso.onView(
            withRecyclerView(R.id.rv_srw_partial)
                .atPositionOnView(position, R.id.tp_srw_title)
        ).perform(ViewActions.click())
    }
}