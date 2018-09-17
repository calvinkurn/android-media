package com.tokopedia.talk.producttalk.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * @author by Steven
 */

interface ProductTalkContract {

    interface View : CustomerView {
        fun getContext(): Context?
        fun showLoadingFull()
        fun hideLoadingFull()
        fun onEmptyTalk()
        fun onSuccessResetTalk(listThread: ArrayList<Visitable<*>>)
        fun onSuccessGetTalks(listThread: ArrayList<Visitable<*>>)
        fun onErrorGetTalks(errorMessage: String?)
        fun setCanLoad()
        fun showRefresh()
        fun hideRefresh()
        fun showLoadingAction()
        fun hideLoadingAction()
        fun onSuccessDeleteCommentTalk(talkId: String, commentId: String)
        fun onSuccessMarkTalkNotFraud(talkId: String)
        fun onSuccessMarkCommentNotFraud(talkId: String, commentId: String)
    }

    interface Presenter : CustomerPresenter<View> {

        fun getProductTalk(productId: String)
        fun resetProductTalk(productId: String)
        fun initProductTalk(productId: String)
        fun deleteCommentTalk(shopId: String, talkId: String, commentId: String)
        fun markTalkNotFraud(talkId: String)
        fun markCommentNotFraud(talkId: String, commentId: String)
        fun isLoggedIn() : Boolean
    }
}