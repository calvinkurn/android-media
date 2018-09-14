package com.tokopedia.talk.talkdetails.view.subscriber

import com.tokopedia.talk.talkdetails.view.contract.TalkDetailsContract
import com.tokopedia.talk.talkdetails.view.viewmodel.TalkDetailsViewModel
import rx.Subscriber

class DeleteTalkCommentSubscriber(val presenter: TalkDetailsContract.Presenter,
                                val view: TalkDetailsContract.View):
        Subscriber<String>() {
    override fun onNext(t: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCompleted() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError(e: Throwable?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}