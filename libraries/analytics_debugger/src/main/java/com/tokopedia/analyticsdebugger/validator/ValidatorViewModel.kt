package com.tokopedia.analyticsdebugger.validator

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import timber.log.Timber

class ValidatorViewModel constructor(val context: Application) : AndroidViewModel(context) {

    private val dao: GtmLogDBSource by lazy { GtmLogDBSource(context) }
    private val engine: ValidatorEngine by lazy { ValidatorEngine(dao) }

    private val disposables: CompositeSubscription by lazy { CompositeSubscription() }

    private val _testCases: MutableLiveData<List<Validator>> by lazy {
        MutableLiveData<List<Validator>>()
    }

    val testCases: LiveData<List<Validator>>
        get() = _testCases

    fun run(queries: List<Map<String, Any>>) {
        val v = queries.map { it.toDefaultValidator() }
        _testCases.value = v
        fetchGtmLog(v)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    private fun fetchGtmLog(param: List<Validator>) {
        val startTime = System.currentTimeMillis()
        engine.compute(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        object : Subscriber<List<Validator>>() {
                            override fun onNext(t: List<Validator>?) {
                                _testCases.value = t
                            }

                            override fun onCompleted() {
                                val endTime = System.currentTimeMillis()
                                Timber.i("Elapsed time %d", endTime - startTime)
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