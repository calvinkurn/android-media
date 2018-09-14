package com.tokopedia.talk.talkdetails.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.talk.talkdetails.domain.usecase.DeleteTalkCommentsUseCase
import com.tokopedia.talk.talkdetails.domain.usecase.GetTalkCommentsUseCase
import com.tokopedia.talk.talkdetails.view.contract.TalkDetailsContract
import com.tokopedia.talk.talkdetails.view.subscriber.DeleteTalkCommentSubscriber
import com.tokopedia.talk.talkdetails.view.subscriber.GetTalkCommentsSubscriber
import com.tokopedia.talk.talkdetails.view.viewmodel.TalkDetailsProductAttachViewModel

/**
 * Created by Hendri on 28/08/18.
 */
class TalkDetailsPresenter(private val getTalkComments:GetTalkCommentsUseCase,
                           private val deleteTalkCommentsUseCase: DeleteTalkCommentsUseCase):
                               BaseDaggerPresenter<TalkDetailsContract.View>(),
                               TalkDetailsContract.Presenter {

    override fun loadTalkDetails(id: String) {
        getTalkComments.execute(GetTalkCommentsUseCase.getParameters(id),
                                GetTalkCommentsSubscriber(this,view))
    }

    override fun reportTalkComment(id: String) {
        view.goToReportTalkPage(id)
    }

    override fun deleteTalkComment(id: String) {
        deleteTalkCommentsUseCase.execute(DeleteTalkCommentsUseCase.getParameters(id),
                                          DeleteTalkCommentSubscriber(this,view))
    }

    override fun sendTalkComment(id: String, attachedProduct: List<TalkDetailsProductAttachViewModel>?, message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        getTalkComments.unsubscribe()
        deleteTalkCommentsUseCase.unsubscribe()
    }
}