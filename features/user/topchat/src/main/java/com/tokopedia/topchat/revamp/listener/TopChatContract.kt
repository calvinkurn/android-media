package com.tokopedia.topchat.revamp.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.view.listener.BaseChatContract

/**
 * @author : Steven 11/12/18
 */
interface TopChatContract {

    interface View : BaseChatContract.View {

        fun developmentView()

        fun addDummyMessage(visitable : Visitable<*>)

        fun removeDummy(visitable : Visitable<*>)

        fun clearEditText()

        fun getStringResource(id: Int): String

        fun showSnackbarError(stringResource: String)

        fun onSuccessGetTemplate(it: List<Visitable<Any>>)

        fun onErrorGetTemplate()

        fun onErrorUploadImage(errorMessage: String)

//        fun onSuccessLoadFirstTime(dummyList: ArrayList<Visitable<*>>)
    }

    interface Presenter : BaseChatContract.Presenter<View> {
        fun connectWebSocket(messageId: String)

        fun startTyping()

        fun stopTyping()

        fun getExistingChat(
                messageId: String,
                onError: (Throwable) -> Unit,
                onSuccess: (ChatroomViewModel) -> Unit
        )

        fun startUploadImages(it: ImageUploadViewModel)

    }
}