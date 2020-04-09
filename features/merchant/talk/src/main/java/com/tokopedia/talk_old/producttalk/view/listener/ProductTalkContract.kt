package com.tokopedia.talk_old.producttalk.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.talk_old.producttalk.view.viewmodel.ProductTalkViewModel

/**
 * @author by Steven
 */

interface ProductTalkContract {

    interface View : CustomerView {
        fun getContext(): Context?
        fun showLoadingFull()
        fun hideLoadingFull()
        fun onEmptyTalk(productTalkViewModel: ProductTalkViewModel)
        fun onSuccessResetTalk(productTalkViewModel: ProductTalkViewModel)
        fun onSuccessGetTalks(productTalkViewModel: ProductTalkViewModel)
        fun onErrorGetTalks(errorMessage: String?)
        fun setCanLoad()
        fun showRefresh()
        fun hideRefresh()
        fun showLoadingAction()
        fun hideLoadingAction()
        fun onSuccessDeleteCommentTalk(talkId: String, commentId: String)
        fun onSuccessMarkTalkNotFraud(talkId: String)
        fun onSuccessMarkCommentNotFraud(talkId: String, commentId: String)
        fun onSuccessFollowTalk(talkId: String)
        fun onSuccessUnfollowTalk(talkId: String)
        fun onSuccessDeleteTalk(talkId: String)
    }

    interface Presenter : CustomerPresenter<View> {

        fun getProductTalk(productId: String)
        fun resetProductTalk(productId: String)
        fun initProductTalk(productId: String)
        fun deleteCommentTalk(shopId: String, talkId: String, commentId: String)
        fun markTalkNotFraud(talkId: String)
        fun markCommentNotFraud(talkId: String, commentId: String)
        fun isLoggedIn(): Boolean
        fun isMyShop(shopId: String): Boolean
        fun unfollowTalk(talkId: String)
        fun followTalk(talkId: String)
        fun deleteTalk(shopId: String, talkId: String)
    }
}