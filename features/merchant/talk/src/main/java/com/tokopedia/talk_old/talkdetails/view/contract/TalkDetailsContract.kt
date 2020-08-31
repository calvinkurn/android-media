package com.tokopedia.talk_old.talkdetails.view.contract

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.talk_old.common.adapter.viewmodel.TalkProductAttachmentViewModel

/**
 * Created by Hendri on 28/08/18.
 */
class TalkDetailsContract {
    interface Presenter {
        fun loadTalkDetails(id:String)
//        fun sendTalkComment(id:String, attachedHeaderProduct:List<TalkDetailsHeaderProductViewModel>?, message:String)
        fun sendComment(talkId:String, message:String,
                        attachedProduct:List<TalkProductAttachmentViewModel>)

        fun deleteTalk(shopId: String, talkId: String)
        fun followTalk(talkId: String)
        fun unfollowTalk(talkId: String)
        fun deleteCommentTalk(shopId: String, talkId: String, commentId: String)
        fun markTalkNotFraud(talkId: String)
        fun markCommentNotFraud(talkId: String, commentId: String)
        fun refreshTalkAfterSendComment(talkId: String)
    }
    interface View:CustomerView {
        fun onError(throwable: Throwable)
        fun onSuccessLoadTalkDetails(data:ArrayList<Visitable<*>>)
        fun onSuccessRefreshTalkAfterSendTalk(data:ArrayList<Visitable<*>>)
        fun onSuccessSendTalkComment(talkId : String, commentId:String)
        fun goToReportTalkPage(talkId:String, shopId:String, productId: String, commentId : String)
        fun showLoadingAction()
        fun hideLoadingAction()
        fun onSuccessDeleteTalk(talkId: String)
        fun onSuccessDeleteCommentTalk(talkId: String, commentId: String)
        fun onSuccessUnfollowTalk(talkId: String)
        fun onSuccessFollowTalk(talkId: String)
        fun onSuccessMarkTalkNotFraud(talkId: String)
        fun onSuccessMarkCommentNotFraud(talkId: String, commentId: String)
        fun onErrorActionTalk(throwable: Throwable)
    }
}