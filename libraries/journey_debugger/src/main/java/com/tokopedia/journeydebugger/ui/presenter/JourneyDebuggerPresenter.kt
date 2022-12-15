package com.tokopedia.journeydebugger.ui.presenter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.journeydebugger.JourneyDebuggerConst
import com.tokopedia.journeydebugger.domain.DeleteJourneyLogUseCase
import com.tokopedia.journeydebugger.domain.GetJourneyLogUseCase
import com.tokopedia.usecase.RequestParams
import rx.Subscriber

class JourneyDebuggerPresenter(private val getJourneyLogUseCase: GetJourneyLogUseCase,
                               private val deleteJourneyLogUseCase: DeleteJourneyLogUseCase
) : JourneyDebugger.Presenter {
    private var view: JourneyDebugger.View? = null

    private var keyword = ""
    private var page = 0
    private val requestParams: RequestParams

    init {
        requestParams = RequestParams.create()
    }

    override fun attachView(view: JourneyDebugger.View) {
        this.view = view
    }

    override fun detachView() {
        getJourneyLogUseCase.unsubscribe()
        deleteJourneyLogUseCase.unsubscribe()
        view = null
    }

    override fun loadMore() {
        setRequestParams(++page, keyword)
        getJourneyLogUseCase.execute(requestParams, loadMoreSubscriber())
    }

    override fun search(text: String) {
        page = 0
        keyword = text
        setRequestParams(page, keyword)
        getJourneyLogUseCase.execute(requestParams, reloadSubscriber())
    }

    override fun reloadData() {
        page = 0
        keyword = ""
        setRequestParams(page, keyword)
        getJourneyLogUseCase.execute(requestParams, reloadSubscriber())
    }

    override fun deleteAll() {
        deleteJourneyLogUseCase.execute(object : Subscriber<Boolean>() {
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
        requestParams.putString(JourneyDebuggerConst.KEYWORD, keyword)
        requestParams.putInt(JourneyDebuggerConst.PAGE, page)
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
