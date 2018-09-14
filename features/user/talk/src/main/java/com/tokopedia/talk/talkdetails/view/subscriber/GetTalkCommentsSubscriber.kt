package com.tokopedia.talk.talkdetails.view.subscriber

import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkViewModel
import com.tokopedia.talk.talkdetails.view.contract.TalkDetailsContract
import rx.Subscriber

/**
 * Created by Hendri on 05/09/18.
 */
class GetTalkCommentsSubscriber(val presenter:TalkDetailsContract.Presenter,
                                val view:TalkDetailsContract.View):
                                    Subscriber<InboxTalkViewModel>() {
    override fun onNext(t: InboxTalkViewModel?) {
        view.onSuccessLoadTalkDetails(t!!.listTalk)
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        view.onError(e?:Throwable("Unknown Error"))
    }
}