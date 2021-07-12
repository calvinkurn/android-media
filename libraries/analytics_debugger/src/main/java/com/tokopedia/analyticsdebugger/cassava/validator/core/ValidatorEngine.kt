package com.tokopedia.analyticsdebugger.cassava.validator.core

import com.tokopedia.analyticsdebugger.database.GtmLogDB
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rx.Observable
import javax.inject.Inject

class ValidatorEngine @Inject constructor(private val dao: GtmLogDBSource) {

    enum class Mode {
        SUBSET_ALL, SUBSET_ORDER, EXACT_ALL, EXACT_ORDER;

        fun isInOrder(): Boolean = (this == SUBSET_ORDER || this == EXACT_ORDER)
        fun isInExact(): Boolean = (this == EXACT_ORDER || this == EXACT_ALL)
    }

    private var currentMode = Mode.EXACT_ALL

    @Deprecated("do not use it, we will remove rx java dependency")
    fun computeRx(testCases: List<Validator>, mode: String = "exact"): Observable<List<Validator>> {
        setMode(mode)

        return dao.getAllLogs().map { logs ->
            compute(testCases, logs)
        }
    }

    suspend fun computeCo(testCases: List<Validator>, mode: String = "exact"): List<Validator> {
        setMode(mode)

        return withContext(Dispatchers.IO) {
            compute(testCases, dao.getLogs())
        }
    }

    private fun compute(testCases: List<Validator>, logs: List<GtmLogDB>): List<Validator> {
        var ordering: Long = 0
        val newResult: MutableList<Validator> = mutableListOf()
        testCases.forEach { case ->
            val tests = logs.findAllContaining(case)
            val matched = tests.first.map { it.toUiModel() }
            if (matched.isNotEmpty()) {
                // in order mode still need to be reviewed (not being used). taking last found for mvp
                val status = when {
                    currentMode.isInOrder() && matched.last().timestamp < ordering -> {
                        ordering = Long.MAX_VALUE
                        Status.FAILURE
                    }
                    else -> {
                        ordering = matched.last().timestamp
                        Status.SUCCESS
                    }
                }
                newResult.add(case.copy(status = status, matches = matched, errors = tests.second))
            } else {
                newResult.add(case.copy(status = Status.FAILURE, errors = tests.second))
            }
        }
        return newResult
    }

    private fun setMode(s: String) {
        currentMode = when (s) {
            "subset" -> Mode.SUBSET_ALL
            "exact" -> Mode.EXACT_ALL
            else -> throw IllegalArgumentException("Wrong format $s")
        }
    }

    private fun List<GtmLogDB>.findAllContaining(comparator: Validator): Pair<List<GtmLogDB>, String> {
        val resultList: MutableList<GtmLogDB> = mutableListOf()
        val errors = StringBuilder()
        var alreadyFoundEvent = false
        for (gtm in this) {
            val mapGtm = gtm.data.toJsonMap()
            if (comparator.data.haveSameEventAndLabel(mapGtm)) {
                val result = comparator.data.canValidateWithErrorMessage(mapGtm, currentMode.isInExact())
                if (result.first) resultList.add(gtm)
                else errors.append(buildErrorStringWithEventAndLabel(
                        comparator.data[EVENT_KEY].toString(),
                        mapGtm[EVENT_LABEL_KEY].toString(),
                        result.second,
                        alreadyFoundEvent))
                alreadyFoundEvent = true
            } else if (comparator.data.canValidate(mapGtm, currentMode.isInExact())) {
                resultList.add(gtm)
            }
        }
        if (resultList.isEmpty() && errors.isEmpty())
            errors.append("No query match with these regex in GTM Log.").appendLine()

        return Pair(resultList, errors.toString())
    }

    private fun List<GtmLogDB>.findContaining(comparator: Validator): GtmLogDB? {
        for (gtm in this) {
            val mapGtm = gtm.data.toJsonMap()
            if (comparator.data.canValidate(mapGtm, currentMode.isInExact())) {
                return gtm
            }
        }
        return null
    }

    private fun buildErrorStringWithEventAndLabel(eventName: String, eventLabel: String, errorMessage: String, alreadyFoundEvent: Boolean): String {
        val sb = StringBuilder()
        if (!alreadyFoundEvent) {
            sb.append("Found event name \"$eventName\" with label \"$eventLabel\",")
                    .appendLine()
                    .append("but: ")
                    .appendLine()
        }
        sb.append("- ").append(errorMessage).appendLine()

        return sb.toString()
    }

    companion object {
        const val EVENT_KEY = "event"
        const val EVENT_LABEL_KEY = "eventLabel"
    }

}