package com.tokopedia.checkout.view.feature.cornerlist

import com.tokopedia.checkout.domain.usecase.GetCornerList
import javax.inject.Inject


/**
 * Created by fajarnuha on 2019-05-26.
 */
class CornerListPresenter @Inject constructor(val usecase: GetCornerList) : CornerContract.Presenter {

    private var mView: CornerContract.View? = null
    private var currentQuery: String = ""

    override fun attachView(view: CornerContract.View) {
        this.mView = view
    }

    override fun detachView() {
        usecase.unsubscribe()
        this.mView = null
    }

    override fun getList(query: String) {
        usecase.execute(query)
                .doOnSubscribe { mView?.setLoadingState(true) }
                .doOnTerminate { mView?.setLoadingState(false) }
                .subscribe(
                        {
                            if (it.listAddress.isNotEmpty()) mView?.showData(it.listAddress)
                            else mView?.showEmptyView()
                            currentQuery = query
                        },
                        { e -> mView?.showError(e) }, {}
                )
    }

    override fun loadMore(page: Int) {
        usecase.loadMore(currentQuery, page)
                .doOnSubscribe { mView?.setLoadingState(true) }
                .doOnTerminate { mView?.setLoadingState(false) }
                .subscribe(
                        {
                            if (it.listAddress.isNotEmpty()) mView?.appendData(it.listAddress)
                            else mView?.notifyHasNotNextPage()
                        },
                        { e -> mView?.showError(e) }, {}
                )
    }
}