package com.tokopedia.checkout.view.feature.cornerlist

import com.tokopedia.checkout.domain.usecase.GetCornerList
import javax.inject.Inject

const val EMPTY_QUERY = ""

/**
 * Created by fajarnuha on 2019-05-26.
 */
class CornerListPresenter @Inject constructor(val usecase: GetCornerList) : CornerContract.Presenter {

    private var mView: CornerContract.View? = null
    private var currentQuery: String = EMPTY_QUERY

    override fun attachView(view: CornerContract.View) {
        this.mView = view
    }

    override fun detachView() {
        usecase.unsubscribe()
        this.mView = null
    }

    override fun getData() {
        usecase.execute(EMPTY_QUERY)
                .doOnSubscribe { mView?.setLoadingState(true) }
                .doOnTerminate { mView?.setLoadingState(false) }
                .subscribe(
                        {
                            mView?.showData(it.listAddress)
                            currentQuery = EMPTY_QUERY
                        },
                        { e -> mView?.showError(e) }, {})
    }

    override fun loadMore(page: Int) {
        usecase.loadMore(currentQuery, page)
                .doOnSubscribe { mView?.setLoadingState(true) }
                .doOnTerminate { mView?.setLoadingState(false) }
                .subscribe(
                        { mView?.appendData(it.listAddress) },
                        { e -> mView?.showError(e) }, {}
                )
    }

    override fun searchQuery(query: String) {
        usecase.execute(query)
                .doOnSubscribe { mView?.setLoadingState(true) }
                .doOnTerminate { mView?.setLoadingState(false) }
                .subscribe(
                        {
                            mView?.showData(it.listAddress)
                            currentQuery = query
                        },
                        { e -> mView?.showError(e) }, {})
    }
}