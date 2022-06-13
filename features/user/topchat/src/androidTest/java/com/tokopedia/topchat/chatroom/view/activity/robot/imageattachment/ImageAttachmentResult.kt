package com.tokopedia.topchat.chatroom.view.activity.robot.imageattachment

import com.tokopedia.test.application.matcher.hasViewHolderItemAtPosition
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralResult.assertChatRecyclerview
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.TopchatImageUploadViewHolder

object ImageAttachmentResult {

    fun assertExistAt(position: Int) {
        assertChatRecyclerview(
            hasViewHolderItemAtPosition(
                position, TopchatImageUploadViewHolder::class.java
            )
        )
    }
}