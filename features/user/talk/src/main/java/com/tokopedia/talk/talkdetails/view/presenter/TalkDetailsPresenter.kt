package com.tokopedia.talk.talkdetails.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.talk.common.adapter.viewmodel.TalkProductAttachmentViewModel
import com.tokopedia.talk.talkdetails.domain.usecase.GetTalkCommentsUseCase
import com.tokopedia.talk.talkdetails.domain.usecase.SendCommentsUseCase
import com.tokopedia.talk.talkdetails.view.contract.TalkDetailsContract
import com.tokopedia.talk.talkdetails.view.subscriber.GetTalkCommentsSubscriber
import com.tokopedia.talk.talkdetails.view.subscriber.SendCommentSubscriber

/**
 * Created by Hendri on 28/08/18.
 */
class TalkDetailsPresenter(private val getTalkComments:GetTalkCommentsUseCase,
                           private val sendCommentsUseCase: SendCommentsUseCase):
                               BaseDaggerPresenter<TalkDetailsContract.View>(),
                               TalkDetailsContract.Presenter {

    override fun loadTalkDetails(id: String) {
        getTalkComments.execute(GetTalkCommentsUseCase.getParameters(id),
                                GetTalkCommentsSubscriber(this,view))
    }

    override fun sendComment(talkId:String, productId: String, message:String,
                             attachedProduct:List<TalkProductAttachmentViewModel>,
                             userId:String) {
        sendCommentsUseCase.execute(SendCommentsUseCase.getParameters(
                talkId=talkId,
                productId = productId,
                message = message,
                userId = userId), SendCommentSubscriber(this,view))
    }

    override fun deleteTalkComment(id: String) {

    }

    override fun onDestroy() {
        getTalkComments.unsubscribe()
        sendCommentsUseCase.unsubscribe()
    }
}