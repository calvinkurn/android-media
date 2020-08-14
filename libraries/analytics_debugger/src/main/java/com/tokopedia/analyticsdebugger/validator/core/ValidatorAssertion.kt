package com.tokopedia.analyticsdebugger.validator.core

import android.content.Context
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.Utils
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import rx.Observable
import rx.schedulers.Schedulers

fun getAnalyticsWithQuery(gtmLogDBSource: GtmLogDBSource,
                          context: Context,
                          queryFileName: String): List<Validator> {
    val testCases = getTestCases(context, queryFileName)
    return ValidatorEngine(gtmLogDBSource)
            .computeRx(testCases.first, testCases.second)
            .toBlocking()
            .first()
}

fun hasAllSuccess(): Matcher<List<Validator>> {
    return object : TypeSafeMatcher<List<Validator>>(ArrayList::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("All analytic hits are successful")
        }

        override fun describeMismatchSafely(item: List<Validator>?, mismatchDescription: Description?) {
            val indexWithFailure = item?.mapIndexed { index, validator ->
                if (validator.status != Status.SUCCESS) index else -1
            }?.filter { it >= 0 }?.joinToString()
            mismatchDescription
                    ?.appendText(" has mismatch status on query number ")
                    ?.appendValue(indexWithFailure)
        }

        override fun matchesSafely(result: List<Validator>): Boolean {
            return result.all { it.status == Status.SUCCESS }
        }
    }
}

private fun getTestCases(context: Context, queryFileName: String): Pair<List<Validator>, String> {
    val analyticValidatorJSON =
            Utils.getJsonDataFromAsset(context, queryFileName)
                    ?: throw AssertionError("Validator Query not found: \"$queryFileName\"")
    val query = analyticValidatorJSON.toJsonMap().getQueryMap().map { it.toDefaultValidator() }
    val mode = analyticValidatorJSON.toJsonMap().getMode()

    return query to mode
}

private fun <T> Observable<T>.test(onNext: (T) -> Unit) {
    this.observeOn(Schedulers.immediate()).subscribeOn(Schedulers.immediate()).subscribe(onNext)
}