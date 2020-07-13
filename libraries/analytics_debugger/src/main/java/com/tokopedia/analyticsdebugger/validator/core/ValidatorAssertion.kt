package com.tokopedia.analyticsdebugger.validator.core

import android.content.Context
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.Utils
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import rx.Observable
import rx.schedulers.Schedulers

fun assertAnalyticWithValidator(
        gtmLogDBSource: GtmLogDBSource,
        context: Context,
        queryFileName: String,
        assertValidator: (Validator) -> Unit
) {
    val testCases = getTestCases(context, queryFileName)

    val engine = ValidatorEngine(gtmLogDBSource)
    engine.compute(testCases).test {
        it.forEach { validator -> assertValidator(validator) }
    }
}

fun getAnalyticsWithQuery(gtmLogDBSource: GtmLogDBSource,
                          context: Context,
                          queryFileName: String): List<Validator> {
    val testCases = getTestCases(context, queryFileName)
    return ValidatorEngine(gtmLogDBSource)
            .compute(testCases)
            .toBlocking()
            .first()
}

fun hasAllSuccess(): Matcher<List<Validator>> {
    return object : TypeSafeMatcher<List<Validator>>(ArrayList::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("has missing anlalytics tracker")
        }

        override fun describeMismatchSafely(item: List<Validator>?, mismatchDescription: Description?) {
            mismatchDescription
                    ?.appendText(" has mismatch status on query number ")
                    ?.appendValue(item?.indexOfFirst{ it.status != Status.SUCCESS })
        }

        override fun matchesSafely(result: List<Validator>): Boolean {
            result.forEach {
                if (it.status != Status.SUCCESS) return false
            }
            return true
        }
    }
}

private fun getTestCases(context: Context, queryFileName: String): List<Validator> {
    val analyticValidatorJSON =
            Utils.getJsonDataFromAsset(context, queryFileName)
                    ?: throw AssertionError("Validator Query not found: \"$queryFileName\"")

    return analyticValidatorJSON.toJsonMap().getQueryMap().map { it.toDefaultValidator() }
}

private fun <T> Observable<T>.test(onNext: (T) -> Unit) {
    this.observeOn(Schedulers.immediate()).subscribeOn(Schedulers.immediate()).subscribe(onNext)
}