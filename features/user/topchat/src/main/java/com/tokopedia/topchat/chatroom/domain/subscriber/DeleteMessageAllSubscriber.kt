package com.tokopedia.topchat.chatroom.domain.subscriber

import com.tokopedia.topchat.chatlist.viewmodel.DeleteChatListUiModel
import rx.Subscriber

class DeleteMessageAllSubscriber(val onErrorDeleteMessage: (Throwable) -> Unit,
                                 val onSuccessDeleteMessage: () -> Unit)
    : Subscriber<DeleteChatListUiModel>() {

    override fun onNext(t: DeleteChatListUiModel?) {
        onSuccessDeleteMessage()
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        onErrorDeleteMessage(e)
    }

}
