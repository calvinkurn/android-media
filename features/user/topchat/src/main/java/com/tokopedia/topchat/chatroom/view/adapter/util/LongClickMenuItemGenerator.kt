package com.tokopedia.topchat.chatroom.view.adapter.util

import com.tokopedia.topchat.chatroom.view.bottomsheet.TopchatBottomSheetBuilder.MENU_ID_COPY_TO_CLIPBOARD
import com.tokopedia.topchat.chatroom.view.bottomsheet.TopchatBottomSheetBuilder.MENU_ID_DELETE_BUBBLE
import com.tokopedia.topchat.chatroom.view.bottomsheet.TopchatBottomSheetBuilder.MENU_ID_REPLY

object LongClickMenuItemGenerator {
    fun createLongClickMenuMsgBubble(): List<Int> {
        return listOf(MENU_ID_REPLY, MENU_ID_COPY_TO_CLIPBOARD)
    }
    fun createLongClickMenuUploadImageBubble(): List<Int> {
        return listOf(MENU_ID_DELETE_BUBBLE)
    }
}