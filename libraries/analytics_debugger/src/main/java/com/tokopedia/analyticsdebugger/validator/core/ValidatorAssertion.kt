package com.tokopedia.analyticsdebugger.validator.core

import android.content.Context
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.Utils
import rx.Observable
import rx.Subscriber
import rx.schedulers.Schedulers

fun assertAnalyticWithValidator(
        gtmLogDBSource: GtmLogDBSource,
        context: Context,
        queryFileName: String,
        assertValidator:(Validator) -> Unit
) {
    val testCases = getTestCases(context, queryFileName)

    val engine = ValidatorEngine(gtmLogDBSource)
    engine.compute(testCases).test {
        it.forEach { validator -> assertValidator(validator) }
    }
}

private fun getTestCases(context: Context, queryFileName: String): List<Validator> {
    val analyticValidatorJSON =
            Utils.getJsonDataFromAsset(context, queryFileName)
                    ?: throw AssertionError("Validator Query not found: \"$queryFileName\"")

    return analyticValidatorJSON.toJsonMap().getQueryMap().map { it.toDefaultValidator() }
}

private fun Map<String, Any>.getQueryMap(): List<Map<String, Any>> {
    return this["query"] as List<Map<String, Any>>
}

private fun <T> Observable<T>.test(onNext: (T) -> Unit) {
    this.observeOn(Schedulers.immediate()).subscribeOn(Schedulers.immediate()).subscribe(object: Subscriber<T>() {
        override fun onNext(t: T) {
            onNext(t)
        }

        override fun onCompleted() {
            // no op
        }

        override fun onError(e: Throwable?) {
            // no op
        }
    })
}