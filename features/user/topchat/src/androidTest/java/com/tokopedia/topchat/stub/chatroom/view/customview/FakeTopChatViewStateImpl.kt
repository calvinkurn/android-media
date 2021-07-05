package com.tokopedia.topchat.stub.chatroom.view.customview

import android.view.View
import androidx.appcompat.widget.Toolbar
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.AttachmentMenu
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.custom.ChatMenuStickerView
import com.tokopedia.topchat.chatroom.view.customview.TopChatViewStateImpl
import com.tokopedia.topchat.chatroom.view.listener.HeaderMenuListener
import com.tokopedia.topchat.chatroom.view.listener.ImagePickerListener
import com.tokopedia.topchat.chatroom.view.listener.SendButtonListener
import com.tokopedia.topchat.chattemplate.view.listener.ChatTemplateListener
import com.tokopedia.topchat.common.analytics.TopChatAnalytics

class FakeTopChatViewStateImpl(
        view: View,
        typingListener: TypingListener,
        sendListener: SendButtonListener,
        templateListener: ChatTemplateListener,
        imagePickerListener: ImagePickerListener,
        attachmentMenuListener: AttachmentMenu.AttachmentMenuListener,
        stickerMenuListener: ChatMenuStickerView.StickerMenuListener,
        headerMenuListener: HeaderMenuListener,
        toolbar: Toolbar,
        analytics: TopChatAnalytics,
) : TopChatViewStateImpl(
        view,
        typingListener,
        sendListener,
        templateListener,
        imagePickerListener,
        attachmentMenuListener,
        stickerMenuListener,
        headerMenuListener,
        toolbar,
        analytics) {

    override fun onGlobalLayout() {
        TopchatRoomTest.keyboardStateIdling?.increment()
        super.onGlobalLayout()
    }

    override fun onKeyboardOpened() {
        super.onKeyboardOpened()
        TopchatRoomTest.keyboardStateIdling?.decrement()
    }

    override fun onKeyboardClosed() {
        super.onKeyboardClosed()
        TopchatRoomTest.keyboardStateIdling?.decrement()
    }
}