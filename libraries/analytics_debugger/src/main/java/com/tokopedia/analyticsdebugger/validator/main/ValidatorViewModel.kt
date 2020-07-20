package com.tokopedia.analyticsdebugger.validator.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.Validator
import com.tokopedia.analyticsdebugger.validator.core.ValidatorEngine
import com.tokopedia.analyticsdebugger.validator.core.toDefaultValidator
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

    fun run(queries: List<Map<String, Any>>, mode: String) {
        val v = queries.map { it.toDefaultValidator() }
        _testCases.value = v

        val startTime = System.currentTimeMillis()
        engine.compute(v, mode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        object : Subscriber<List<Validator>>() {
                            override fun onNext(t: List<Validator>?) {
                                val endTime = System.currentTimeMillis()
                                Timber.i("Elapsed time %d", endTime - startTime)
                                Timber.i("Matched Result %s", t)
                                _testCases.value = t
                            }

                            override fun onCompleted() {

                            }

                            override fun onError(e: Throwable?) {
                                Timber.w(e)
                            }
                        }
                ).toSubs()
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    private fun Subscription.toSubs() {
        disposables.add(this)
    }

}