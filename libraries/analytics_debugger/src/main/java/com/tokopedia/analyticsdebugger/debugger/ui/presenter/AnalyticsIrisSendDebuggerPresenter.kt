package com.tokopedia.analyticsdebugger.debugger.ui.presenter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst
import com.tokopedia.analyticsdebugger.debugger.domain.DeleteIrisSendLogUseCase
import com.tokopedia.analyticsdebugger.debugger.domain.GetIrisSendCountLogUseCase
import com.tokopedia.analyticsdebugger.debugger.domain.GetIrisSendLogUseCase
import com.tokopedia.analyticsdebugger.debugger.ui.AnalyticsDebugger
import com.tokopedia.usecase.RequestParams

import rx.Subscriber

class AnalyticsIrisSendDebuggerPresenter(private val getUseCase: GetIrisSendLogUseCase,
                                         private val getCountUseCase: GetIrisSendCountLogUseCase,
                                         private val deleteUseCase: DeleteIrisSendLogUseCase) : AnalyticsDebugger.Presenter {
    private var view: AnalyticsDebugger.View? = null

    private var keyword = ""
    private var page = 0
    private val requestParams: RequestParams

    init {
        requestParams = RequestParams.create()
    }

    override fun attachView(view: AnalyticsDebugger.View) {
        this.view = view
    }

    override fun detachView() {
        getUseCase.unsubscribe()
        deleteUseCase.unsubscribe()
        view = null
    }

    override fun loadMore() {
        setRequestParams(page++, keyword)
        getUseCase.execute(requestParams, loadMoreSubscriber())
    }

    override fun search(text: String) {
        page = 0
        keyword = text
        setRequestParams(page, keyword)
        getUseCase.execute(requestParams, reloadSubscriber())
    }

    override fun reloadData() {
        page = 0
        keyword = ""
        setRequestParams(page, keyword)
        getUseCase.execute(requestParams, reloadSubscriber())
        getCountUseCase.execute(RequestParams.create(), countSubscriber())
    }

    override fun deleteAll() {
        deleteUseCase.execute(object : Subscriber<Boolean>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(aBoolean: Boolean?) {
                view!!.onDeleteCompleted()
            }
        })
    }

    private fun setRequestParams(page: Int, keyword: String) {
        requestParams.putString(AnalyticsDebuggerConst.KEYWORD, keyword)
        requestParams.putInt(AnalyticsDebuggerConst.PAGE, page)
    }

    private fun loadMoreSubscriber(): Subscriber<List<Visitable<*>>> {
        return object : Subscriber<List<Visitable<*>>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(visitables: List<Visitable<*>>) {
                view!!.onLoadMoreCompleted(visitables)
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
                view!!.onReloadCompleted(visitables)
            }
        }
    }

    private fun countSubscriber(): Subscriber<Int> {
        return object : Subscriber<Int>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(visitables: Int) {

            }
        }
    }
}
