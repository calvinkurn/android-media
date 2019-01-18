package com.tokopedia.topchat.chatroom.domain.subscriber

import com.tokopedia.topchat.chatlist.viewmodel.DeleteChatListViewModel
import rx.Subscriber

class DeleteMessageAllSubscriber(val onErrorDeleteMessage: (Throwable) -> Unit,
                                 val onSuccessDeleteMessage: () -> Unit)
    : Subscriber<DeleteChatListViewModel>() {

    override fun onNext(t: DeleteChatListViewModel?) {
        onSuccessDeleteMessage()
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        onErrorDeleteMessage(e)
    }

}
