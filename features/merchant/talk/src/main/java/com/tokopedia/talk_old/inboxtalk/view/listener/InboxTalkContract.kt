package com.tokopedia.talk_old.inboxtalk.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.talk_old.inboxtalk.view.viewmodel.InboxTalkViewModel

/**
 * @author by nisie on 8/29/18.
 */

interface InboxTalkContract {

    interface View : CustomerView {
        fun getContext(): Context?
        fun showLoading()
        fun hideLoading()
        fun onSuccessGetInboxTalk(talkViewModel: InboxTalkViewModel)
        fun onErrorGetInboxTalk(errorMessage: String)
        fun onEmptyTalk()
        fun hideRefreshLoad()
        fun hideFilter()
        fun showFilter()
        fun showLoadingAction()
        fun onSuccessGetListFirstPage(talkViewModel: InboxTalkViewModel)
        fun hideLoadingAction()
        fun onSuccessDeleteTalk(talkId: String)
        fun onSuccessDeleteCommentTalk(talkId: String, commentId: String)
        fun onSuccessUnfollowTalk(talkId: String)
        fun onSuccessFollowTalk(talkId: String)
        fun onSuccessMarkTalkNotFraud(talkId: String)
        fun onSuccessMarkCommentNotFraud(talkId: String, commentId: String)
        fun onErrorActionTalk(errorMessage: String)
    }

    interface Presenter : CustomerPresenter<View> {
        fun refreshTalk(filter: String, nav: String)
        fun getInboxTalk(filter: String, nav: String)
        fun getInboxTalkWithFilter(filter: String, nav: String)
        fun deleteTalk(shopId: String, talkId: String)
        fun deleteCommentTalk(shopId: String, talkId: String, commentId: String)
        fun unfollowTalk(talkId: String)
        fun followTalk(talkId: String)
        fun markTalkNotFraud(talkId: String)
        fun markCommentNotFraud(talkId: String, commentId: String)
    }
}