package com.tokopedia.analyticsdebugger.debugger.ui.presenter

import android.net.Uri
import android.os.AsyncTask
import android.widget.Toast

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst
import com.tokopedia.analyticsdebugger.debugger.domain.DeleteFpmLogUseCase
import com.tokopedia.analyticsdebugger.debugger.domain.GetFpmAllDataUseCase
import com.tokopedia.analyticsdebugger.debugger.domain.GetFpmLogUseCase
import com.tokopedia.analyticsdebugger.debugger.ui.model.FpmDebuggerViewModel
import com.tokopedia.usecase.RequestParams

import rx.Subscriber
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class FpmDebuggerPresenter(private val getFpmLogUseCase: GetFpmLogUseCase,
                           private val deleteFpmLogUseCase: DeleteFpmLogUseCase,
                           private val getFpmAllDataUseCase: GetFpmAllDataUseCase) : FpmDebugger.Presenter {
    private var view: FpmDebugger.View? = null

    private var keyword = ""
    private var page = 0
    private val requestParams: RequestParams

    var fileSaverTask: FileSaverTask? = null

    init {
        requestParams = RequestParams.create()
    }

    override fun attachView(view: FpmDebugger.View) {
        this.view = view
    }

    override fun detachView() {
        fileSaverTask?.cancel(true)
        getFpmLogUseCase.unsubscribe()
        deleteFpmLogUseCase.unsubscribe()
        getFpmAllDataUseCase.unsubscribe()
        view = null
    }

    override fun loadMore() {
        setRequestParams(++page, keyword)
        getFpmLogUseCase.execute(requestParams, loadMoreSubscriber())
    }

    override fun search(text: String) {
        page = 0
        keyword = text
        setRequestParams(page, keyword)
        getFpmLogUseCase.execute(requestParams, reloadSubscriber())
    }

    override fun reloadData() {
        page = 0
        keyword = ""
        setRequestParams(page, keyword)
        getFpmLogUseCase.execute(requestParams, reloadSubscriber())
    }

    override fun deleteAll() {
        deleteFpmLogUseCase.execute(object : Subscriber<Boolean>() {
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

    override fun writeAllDataToFile(fileUri: Uri) {
        getFpmAllDataUseCase.execute(requestParams, object : Subscriber<List<Visitable<*>>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(visitables: List<Visitable<*>>) {
                val modelList = convertToModelList(visitables);
                fileSaverTask = FileSaverTask(view).apply {
                    execute(Pair(fileUri, modelList))
                }
            }
        })
    }

    private fun convertToModelList(visitables: List<Visitable<*>>): List<FpmDebuggerViewModel> {
        val modelList : MutableList<FpmDebuggerViewModel> = ArrayList()

        for (visitable in visitables) {
            modelList += visitable as FpmDebuggerViewModel
        }

        return modelList
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

    class FileSaverTask(val view: FpmDebugger.View?) :
            AsyncTask<Pair<Uri, List<FpmDebuggerViewModel>>, Unit, Boolean>() {

        @Suppress("NestedBlockDepth")
        override fun doInBackground(vararg params: Pair<Uri, List<FpmDebuggerViewModel>>): Boolean {
            val (uri, modelList) = params[0]
            try {
                val context = view?.getViewContext() ?: return false
                context.contentResolver.openFileDescriptor(uri, "w")?.use {
                    FileOutputStream(it.fileDescriptor).use { fos ->
                        for (model in modelList) {
                            val s = "\r\n\r\n\r\ntraceName: " + model.name +
                                    "\r\ntraceDuration: " + model.duration + "ms" +
                                    "\r\nmetrics: " + model.metrics +
                                    "\r\nattributes: " + model.attributes +
                                    "\r\ntimestamp: " + model.timestamp
                            fos.write(s.toByteArray())
                        }
                    }
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                return false
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            }
            return true
        }

        override fun onPostExecute(isSuccessful: Boolean) {
            val toastMessageId = if (isSuccessful) {
                R.string.fpm_file_saved
            } else {
                R.string.fpm_file_not_saved
            }
            Toast.makeText(view?.getViewContext(), toastMessageId, Toast.LENGTH_SHORT).show()
        }
    }
}
