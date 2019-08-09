package com.tokopedia.topchat.chatlist.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * @author : Steven 2019-08-06
 */
interface ChatListContract {
    interface View: BaseListViewListener<Visitable<*>>, CustomerView {

    }

    interface Presenter<V: CustomerView>: CustomerPresenter<V> {

    }
}