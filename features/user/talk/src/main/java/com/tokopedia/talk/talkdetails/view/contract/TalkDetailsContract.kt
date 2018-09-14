package com.tokopedia.talk.talkdetails.view.contract

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.talk.common.adapter.viewmodel.TalkProductAttachmentViewModel

/**
 * Created by Hendri on 28/08/18.
 */
class TalkDetailsContract {
    interface Presenter {
        fun loadTalkDetails(id:String)
        fun reportTalkComment(id:String, shopId:String, productId:String)
        fun deleteTalkComment(id:String)
//        fun sendTalkComment(id:String, attachedHeaderProduct:List<TalkDetailsHeaderProductViewModel>?, message:String)
        fun onDestroy()
        fun sendComment(talkId:String, productId: String, message:String,
                        attachedProduct:List<TalkProductAttachmentViewModel>,
                        userId:String)
    }
    interface View:CustomerView {
        fun onError(throwable: Throwable)
        fun onSuccessLoadTalkDetails(data:ArrayList<Visitable<*>>)
        fun onSuccessDeleteTalkComment(id:String)
        fun onSuccessSendTalkComment(commentId:String)
        fun goToReportTalkPage(id:String, shopId:String, productId: String)
        fun showLoadingAction()
        fun hideLoadingAction()
    }
}