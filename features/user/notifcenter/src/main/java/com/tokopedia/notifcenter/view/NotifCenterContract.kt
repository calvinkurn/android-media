package com.tokopedia.notifcenter.view

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * @author by milhamj on 30/08/18.
 */
interface NotifCenterContract {
    interface View : CustomerView {

    }

    interface Presenter : CustomerPresenter<View> {
        fun fetchData()

        fun updatePage(page: Int)

        fun updateFilterId(filterId: String)
    }
}