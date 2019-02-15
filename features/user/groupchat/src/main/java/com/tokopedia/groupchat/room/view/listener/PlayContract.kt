package com.tokopedia.groupchat.room.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * @author : Steven 13/02/19
 */
interface PlayContract {
    interface View : BaseListViewListener<Visitable<*>>, CustomerView {
        fun onBackPressed() : Boolean
    }

    interface Presenter: CustomerPresenter<View> {

    }
}