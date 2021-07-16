package com.tokopedia.analyticsdebugger.cassava.validator.core

import com.tokopedia.analyticsdebugger.cassava.data.CassavaValidateResult
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
            val matched = tests.listGtmLog.map { it.toUiModel() }
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
                newResult.add(case.copy(status = status, matches = matched, errors = tests.errorCause))
            } else {
                newResult.add(case.copy(status = Status.FAILURE, errors = tests.errorCause))
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

    private fun List<GtmLogDB>.findAllContaining(comparator: Validator): CassavaValidateResult {
        val resultList: MutableList<GtmLogDB> = mutableListOf()
        val errors = StringBuilder()
        var alreadyFoundEvent = false
        for (gtm in this) {
            val mapGtm = gtm.data.toJsonMap()
            val result = comparator.data.canValidate(mapGtm, currentMode.isInExact())

            if (result.isValid) {
                resultList.add(gtm)
            } else {
                errors.append(buildErrorStringWithEventAndLabel(
                        comparator.data[EVENT_KEY].toString(),
                        mapGtm[EVENT_LABEL_KEY].toString(),
                        result.errorCause,
                        alreadyFoundEvent))
            }

            alreadyFoundEvent = true

        }
        if (resultList.isEmpty() && errors.isEmpty())
            errors.append("No query match with these regex in GTM Log.").appendLine()

        return CassavaValidateResult(true, errors.toString(), resultList)
    }

    private fun List<GtmLogDB>.findContaining(comparator: Validator): GtmLogDB? {
        for (gtm in this) {
            val mapGtm = gtm.data.toJsonMap()
            if (comparator.data.canValidate(mapGtm, currentMode.isInExact()).isValid) {
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