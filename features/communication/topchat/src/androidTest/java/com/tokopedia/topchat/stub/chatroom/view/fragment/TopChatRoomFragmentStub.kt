package com.tokopedia.topchat.stub.chatroom.view.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.view.listener.BaseChatViewState
import com.tokopedia.topchat.chatroom.data.ImageUploadServiceModel
import com.tokopedia.topchat.chatroom.view.fragment.TopChatRoomFragment
import com.tokopedia.topchat.stub.chatroom.view.customview.FakeTopChatViewStateImpl
import com.tokopedia.topchat.stub.chatroom.view.service.UploadImageChatServiceStub

open class TopChatRoomFragmentStub : TopChatRoomFragment() {

    /**
     * show bottomsheet immediately
     */
    override fun onRetrySendImage(element: ImageUploadUiModel) {
        super.onRetrySendImage(element)
        childFragmentManager.executePendingTransactions()
    }

    override fun showChangeAddressBottomSheet() {
        if (SUCCESS_CHANGE_ADDRESS) {
            getChangeAddressListener().onAddressDataChanged()
        }
    }

    override fun onCreateViewState(view: View): BaseChatViewState {
        return FakeTopChatViewStateImpl(
            view, this, this, this,
            this, this, this,
            this, this,
            (activity as BaseChatToolbarActivity).getToolbar(), analytics, session
        ).also {
            topchatViewState = it
        }
    }

    override fun showMsgMenu(
        msg: BaseChatUiModel,
        text: CharSequence,
        menus: List<Int>
    ) {
        super.showMsgMenu(msg, text, menus)
        childFragmentManager.executePendingTransactions()
    }

    override fun onDetach() {
        SUCCESS_CHANGE_ADDRESS = false
        super.onDetach()
    }

    override fun uploadImage(image: ImageUploadServiceModel) {
        UploadImageChatServiceStub.enqueueWork(
            context!!,
            image,
            viewModel.roomMetaData.msgId,
            isUploadImageSecure()
        )
    }

    override fun isOCCActive(): Boolean {
        return isOCCActive
    }

    companion object {
        var isOCCActive = false
        var SUCCESS_CHANGE_ADDRESS = true
        fun createInstance(
            bundle: Bundle
        ): TopChatRoomFragmentStub {
            return TopChatRoomFragmentStub().apply {
                arguments = bundle
            }
        }
    }
}
