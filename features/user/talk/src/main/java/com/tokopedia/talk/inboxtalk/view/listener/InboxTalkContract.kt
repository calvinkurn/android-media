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
        fun showLoadingFull()
        fun hideLoadingFull()
        fun onSuccessGetInboxTalk(list: ArrayList<Visitable<*>>)
        fun onErrorGetInboxTalk(errorMessage: String)
        fun onEmptyTalk()
    }

    interface Presenter : CustomerPresenter<View> {
        fun refreshTalk()
        fun getInboxTalk(filter: String, nav: String)
    }
}