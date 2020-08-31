package com.tokopedia.analyticsdebugger.debugger.ui.presenter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst
import com.tokopedia.analyticsdebugger.debugger.domain.DeleteApplinkLogUseCase
import com.tokopedia.analyticsdebugger.debugger.domain.GetApplinkLogUseCase
import com.tokopedia.usecase.RequestParams
import rx.Subscriber

class ApplinkDebuggerPresenter(private val getApplinkLogUseCase: GetApplinkLogUseCase,
                               private val deleteApplinkLogUseCase: DeleteApplinkLogUseCase) : ApplinkDebugger.Presenter {
    private var view: ApplinkDebugger.View? = null

    private var keyword = ""
    private var page = 0
    private val requestParams: RequestParams

    init {
        requestParams = RequestParams.create()
    }

    override fun attachView(view: ApplinkDebugger.View) {
        this.view = view
    }

    override fun detachView() {
        getApplinkLogUseCase.unsubscribe()
        deleteApplinkLogUseCase.unsubscribe()
        view = null
    }

    override fun loadMore() {
        setRequestParams(++page, keyword)
        getApplinkLogUseCase.execute(requestParams, loadMoreSubscriber())
    }

    override fun search(text: String) {
        page = 0
        keyword = text
        setRequestParams(page, keyword)
        getApplinkLogUseCase.execute(requestParams, reloadSubscriber())
    }

    override fun reloadData() {
        page = 0
        keyword = ""
        setRequestParams(page, keyword)
        getApplinkLogUseCase.execute(requestParams, reloadSubscriber())
    }

    override fun deleteAll() {
        deleteApplinkLogUseCase.execute(object : Subscriber<Boolean>() {
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
