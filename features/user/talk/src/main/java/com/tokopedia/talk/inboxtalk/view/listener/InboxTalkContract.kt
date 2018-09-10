package com.tokopedia.talk.inboxtalk.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkViewModel

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
        fun showLoadingFilter()
        fun onSuccessGetListFirstPage(listTalk: ArrayList<Visitable<*>>)
        fun hideLoadingFilter()
        fun onSuccessDeleteTalk()
        fun onSuccessDeleteCommentTalk()
    }

    interface Presenter : CustomerPresenter<View> {
        fun refreshTalk(filter: String, nav: String)
        fun getInboxTalk(filter: String, nav: String)
        fun getInboxTalkWithFilter(filter: String, nav: String)
        fun deleteTalk(shopId: String, talkId: String)
        fun deleteCommentTalk()
        fun unfollowTalk()
        fun followTalk()
    }
}