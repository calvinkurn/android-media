package com.tokopedia.topchat.stub.chatroom.view.customview

import android.view.View
import androidx.appcompat.widget.Toolbar
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.AttachmentMenu
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.custom.ChatMenuStickerView
import com.tokopedia.topchat.chatroom.view.custom.ChatTextAreaTabLayoutListener
import com.tokopedia.topchat.chatroom.view.customview.TopChatViewStateImpl
import com.tokopedia.topchat.chatroom.view.listener.HeaderMenuListener
import com.tokopedia.topchat.chatroom.view.listener.ImagePickerListener
import com.tokopedia.topchat.chatroom.view.listener.SendButtonListener
import com.tokopedia.topchat.chattemplate.view.listener.ChatTemplateListener
import com.tokopedia.topchat.common.analytics.TopChatAnalytics
import com.tokopedia.user.session.UserSessionInterface

class FakeTopChatViewStateImpl(
        view: View,
        typingListener: TypingListener,
        sendListener: SendButtonListener,
        templateListener: ChatTemplateListener,
        imagePickerListener: ImagePickerListener,
        attachmentMenuListener: AttachmentMenu.AttachmentMenuListener,
        stickerMenuListener: ChatMenuStickerView.StickerMenuListener,
        headerMenuListener: HeaderMenuListener,
        chatTextAreaTabLayoutListener: ChatTextAreaTabLayoutListener,
        toolbar: Toolbar,
        analytics: TopChatAnalytics,
        userSession: UserSessionInterface
) : TopChatViewStateImpl(
        view,
        typingListener,
        sendListener,
        templateListener,
        imagePickerListener,
        attachmentMenuListener,
        stickerMenuListener,
        headerMenuListener,
        chatTextAreaTabLayoutListener,
        toolbar,
        analytics, userSession) {


    override fun showHeaderMenuBottomSheet(
        chatroomViewModel: ChatroomViewModel,
        headerMenuListener: HeaderMenuListener
    ) {
        super.showHeaderMenuBottomSheet(chatroomViewModel, headerMenuListener)
        sendListener.getSupportChildFragmentManager().executePendingTransactions()
    }

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
