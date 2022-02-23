package com.tokopedia.analyticsdebugger.debugger.ui.presenter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst
import com.tokopedia.analyticsdebugger.debugger.domain.DeleteTopAdsLogUseCase
import com.tokopedia.analyticsdebugger.debugger.domain.GetTopAdsLogUseCase
import com.tokopedia.usecase.RequestParams
import rx.Subscriber

class TopAdsDebuggerPresenter(private val getTopAdsLogUseCase: GetTopAdsLogUseCase,
                              private val deleteTopAdsLogUseCase: DeleteTopAdsLogUseCase) : TopAdsDebugger.Presenter {
    private var view: TopAdsDebugger.View? = null

    private var keyword = ""
    private var page = 0
    private val requestParams: RequestParams
    
    init {
        requestParams = RequestParams.create()
    }

    override fun attachView(view: TopAdsDebugger.View) {
        this.view = view
    }

    override fun detachView() {
        getTopAdsLogUseCase.unsubscribe()
        deleteTopAdsLogUseCase.unsubscribe()
        view = null
    }

    override fun loadMore() {
        setRequestParams(++page, keyword)
        getTopAdsLogUseCase.execute(requestParams, loadMoreSubscriber())
    }

    override fun search(text: String) {
        page = 0
        keyword = text
        setRequestParams(page, keyword)
        getTopAdsLogUseCase.execute(requestParams, reloadSubscriber())
    }

    override fun reloadData() {
        page = 0
        keyword = ""
        setRequestParams(page, keyword)
        getTopAdsLogUseCase.execute(requestParams, reloadSubscriber())
    }

    override fun deleteAll() {
        deleteTopAdsLogUseCase.execute(object : Subscriber<Boolean>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(aBoolean: Boolean?) {
                view?.onDeleteCompleted()
            }
        })
    }

    private fun setRequestParams(page: Int, keyword: String) {
        requestParams.putString(AnalyticsDebuggerConst.KEYWORD, keyword)
        requestParams.putInt(AnalyticsDebuggerConst.PAGE, page)
        requestParams.putBoolean(AnalyticsDebuggerConst.TOPADS_VERIFICATOR_BE, true)
    }

    private fun loadMoreSubscriber(): Subscriber<List<Visitable<*>>> {
        return object : Subscriber<List<Visitable<*>>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(visitables: List<Visitable<*>>) {
                view?.onLoadMoreCompleted(visitables)
            }
        }
    }

    private fun reloadSubscriber(): Subscriber<List<Visitable<*>>> {
        return object : Subscriber<List<Visitable<*>>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(visitables: List<Visitable<*>>) {
                view?.onReloadCompleted(visitables)
            }
        }
    }
}
