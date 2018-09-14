package com.tokopedia.talk.talkdetails.view.subscriber

import com.tokopedia.talk.talkdetails.view.contract.TalkDetailsContract
import com.tokopedia.talk.talkdetails.view.viewmodel.TalkDetailsViewModel
import rx.Subscriber

/**
 * Created by Hendri on 05/09/18.
 */
class GetTalkCommentsSubscriber(val presenter:TalkDetailsContract.Presenter,
                                val view:TalkDetailsContract.View):
                                    Subscriber<TalkDetailsViewModel>() {
    override fun onNext(t: TalkDetailsViewModel?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCompleted() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError(e: Throwable?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}