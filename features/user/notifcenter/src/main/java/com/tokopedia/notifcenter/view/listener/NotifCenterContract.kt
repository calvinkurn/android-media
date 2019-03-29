package com.tokopedia.notifcenter.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * @author by milhamj on 30/08/18.
 */
interface NotifCenterContract {
    interface View : CustomerView {
        fun getContext() : Context?

        fun onSuccessFetchData(visitables: List<Visitable<*>>, canLoadMore: Boolean)

        fun onErrorFetchData(message: String)

        fun showRefreshing()

        fun showLoading()

        fun hideLoading()

        fun openRedirectUrl(url: String, notifId: String)
    }

    interface Presenter : CustomerPresenter<View> {
        fun fetchData()

        fun fetchDataWithoutCache()

        fun updatePage(lastNotifId: String)

        fun updateFilterId(filterId: Int)

        fun resetCursor()

        fun resetParam()
    }
}