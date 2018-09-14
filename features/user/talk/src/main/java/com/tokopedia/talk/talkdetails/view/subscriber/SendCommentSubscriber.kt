package com.tokopedia.talk.talkdetails.view.subscriber

import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkViewModel
import com.tokopedia.talk.talkdetails.data.SendCommentResponse
import com.tokopedia.talk.talkdetails.view.contract.TalkDetailsContract
import rx.Subscriber

/**
 * Created by Hendri on 14/09/18.
 */
class SendCommentSubscriber (val presenter: TalkDetailsContract.Presenter,
                             val view: TalkDetailsContract.View):
        Subscriber<SendCommentResponse>() {
    override fun onNext(t: SendCommentResponse?) {
        view.onSuccessSendTalkComment(t?.comment_id?:"")
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        view.onError(e?:Throwable("Unknown Error"))
    }
}

