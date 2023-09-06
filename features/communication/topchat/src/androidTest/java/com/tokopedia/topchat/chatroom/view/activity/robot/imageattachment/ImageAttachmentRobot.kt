package com.tokopedia.topchat.chatroom.view.activity.robot.imageattachment

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.robot.generalRobot

object ImageAttachmentRobot {

    fun longClickImageAt(position: Int) {
        generalRobot {
            doActionOnListItemAt(position, R.id.image, longClick())
        }
    }

    fun clickImageUploadErrorHandler(position: Int) {
        generalRobot {
            doActionOnListItemAt(position, R.id.left_action, click())
        }
    }

    fun clickRetrySendImage() {
        onView(withId(R.id.tp_topchat_retry_send_img)).perform(click())
    }
}
