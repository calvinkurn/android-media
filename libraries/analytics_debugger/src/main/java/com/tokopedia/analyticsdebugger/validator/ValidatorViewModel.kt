package com.tokopedia.analyticsdebugger.validator

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.analyticsdebugger.database.GtmLogDB
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import kotlin.system.measureTimeMillis

class ValidatorViewModel constructor(val context: Application) : AndroidViewModel(context) {

    private val dao: GtmLogDBSource by lazy { GtmLogDBSource(context) }
    private val disposables: CompositeSubscription by lazy { CompositeSubscription() }

    private val _gtmLog: MutableLiveData<List<GtmLogDB>> by lazy {
        MutableLiveData<List<GtmLogDB>>().also {
            fetchGtmLog()
        }
    }

    val gtmLog: LiveData<List<GtmLogDB>>
        get() = _gtmLog

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    private fun fetchGtmLog() {
        val startTime = System.currentTimeMillis()
        dao.getAllLogs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        object : Subscriber<List<GtmLogDB>>() {
                            override fun onNext(t: List<GtmLogDB>?) {
                                _gtmLog.value = t
                            }

                            override fun onCompleted() {
                                val endTime = System.currentTimeMillis()
                                Timber.i("Elapsed time %d", endTime-startTime)
                            }

                            override fun onError(e: Throwable?) {
                                Timber.w(e)
                            }
                        }
                ).toSubs()
    }

    private fun Subscription.toSubs() {
        disposables.add(this)
    }

}