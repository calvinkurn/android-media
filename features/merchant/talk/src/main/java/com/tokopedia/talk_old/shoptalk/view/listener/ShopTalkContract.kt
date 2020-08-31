package com.tokopedia.talk_old.shoptalk.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.talk_old.inboxtalk.view.viewmodel.InboxTalkViewModel

/**
 * @author by nisie on 9/17/18.
 */

interface ShopTalkContract {

    interface View : CustomerView {
        fun showLoading()
        fun hideLoading()
        fun onEmptyTalk()
        fun onSuccessGetShopTalk(talkViewModel: InboxTalkViewModel)
        fun getContext(): Context?
        fun onErrorGetShopTalk(errorMessage: String)
        fun hideRefreshLoad()
        fun onSuccessRefreshTalk(listTalk: ArrayList<Visitable<*>>)
        fun showLoadingAction()
        fun hideLoadingAction()
        fun onSuccessMarkTalkNotFraud(talkId: String)
        fun onSuccessMarkCommentNotFraud(talkId: String, commentId: String)
        fun onSuccessUnfollowTalk(talkId: String)
        fun onSuccessFollowTalk(talkId: String)
        fun onSuccessDeleteTalk(talkId: String)
        fun onSuccessDeleteCommentTalk(talkId: String, commentId: String)
        fun onErrorActionTalk(errorMessage: String)

    }

    interface Presenter : CustomerPresenter<View> {
        fun getShopTalk(shopId: String)
        fun refreshTalk(shopId: String)
        fun unfollowTalk(talkId: String)
        fun followTalk(talkId: String)
        fun deleteTalk(shopId: String, talkId: String)
        fun deleteCommentTalk(shopId: String, talkId: String, commentId: String)
        fun markTalkNotFraud(talkId: String)
        fun markCommentNotFraud(talkId: String, commentId: String)

    }
}