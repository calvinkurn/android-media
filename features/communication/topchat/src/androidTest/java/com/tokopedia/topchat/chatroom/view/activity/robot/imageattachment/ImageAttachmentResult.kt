package com.tokopedia.topchat.chatroom.view.activity.robot.imageattachment

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.test.application.matcher.hasViewHolderItemAtPosition
import com.tokopedia.topchat.R
import com.tokopedia.topchat.assertion.atPositionIsInstanceOf
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.TopchatImageUploadViewHolder
import org.hamcrest.Matcher

object ImageAttachmentResult {

    fun assertExistAt(position: Int) {
        generalResult {
            assertChatRecyclerview(
                hasViewHolderItemAtPosition(
                    position,
                    TopchatImageUploadViewHolder::class.java
                )
            )
        }
    }

    fun assertImageUploadAt(position: Int) {
        onView(withId(R.id.recycler_view_chatroom)).check(
            atPositionIsInstanceOf(position, ImageUploadUiModel::class.java)
        )
    }

    fun assertImageContainer(position: Int, matcher: Matcher<View>) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.fl_image_container, matcher)
        }
    }
}
