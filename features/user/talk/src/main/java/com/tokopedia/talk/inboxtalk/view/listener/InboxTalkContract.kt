package com.tokopedia.talk.inboxtalk.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * @author by nisie on 8/29/18.
 */

interface InboxTalkContract {

    interface View : CustomerView {
        fun getContext(): Context?
        fun showLoading()
        fun hideLoading()
        fun onSuccessGetInboxTalk(list: ArrayList<Visitable<*>>)
        fun onErrorGetInboxTalk(errorMessage: String)
        fun onEmptyTalk()
        fun hideRefreshLoad()
        fun hideFilter()
        fun showFilter()
        fun showLoadingFilter()
        fun onSuccessGetListFirstPage(listTalk: ArrayList<Visitable<*>>)
        fun hideLoadingFilter()
    }

    interface Presenter : CustomerPresenter<View> {
        fun refreshTalk(filter: String, nav: String)
        fun getInboxTalk(filter: String, nav: String)
        fun getInboxTalkWithFilter(filter: String, nav: String)
    }
}