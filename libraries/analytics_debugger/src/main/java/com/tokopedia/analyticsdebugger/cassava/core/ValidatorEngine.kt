package com.tokopedia.analyticsdebugger.cassava.core

import com.tokopedia.analyticsdebugger.cassava.data.CassavaValidateResult
import com.tokopedia.analyticsdebugger.cassava.utils.AnalyticsParser
import com.tokopedia.analyticsdebugger.database.GtmLogDB
import com.tokopedia.analyticsdebugger.debugger.data.repository.GtmRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ValidatorEngine @Inject constructor(
    private val repository: GtmRepo,
    private val analyticsParser: AnalyticsParser
) {

    enum class Mode {
        SUBSET_ALL, SUBSET_ORDER, EXACT_ALL, EXACT_ORDER;

        fun isInOrder(): Boolean = (this == SUBSET_ORDER || this == EXACT_ORDER)
        fun isInExact(): Boolean = (this == EXACT_ORDER || this == EXACT_ALL)
    }

    private var currentMode = Mode.EXACT_ALL

    suspend fun compute(testCases: List<Validator>, mode: String = "exact"): List<Validator> {
        setMode(mode)
        return withContext(Dispatchers.IO) {
            var ordering: Long = 0
            val newResult: MutableList<Validator> = mutableListOf()
            testCases.forEach { case ->
                val tests = repository.getLogs().findAllContaining(case)
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
                    newResult.add(
                        case.copy(
                            status = status,
                            matches = matched,
                            errors = tests.errorCause
                        )
                    )
                } else {
                    newResult.add(case.copy(status = Status.FAILURE, errors = tests.errorCause))
                }
            }
            newResult
        }
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
            val mapGtm = analyticsParser.toJsonMap(gtm.data)
            val result = comparator.data.canValidate(mapGtm, currentMode.isInExact())

            if (result.isValid) {
                resultList.add(gtm)
            } else if (result.errorCause.isNotEmpty()) {
                errors.append(
                    buildErrorStringWithEventAndLabel(
                        comparator.data[EVENT_KEY].toString(),
                        mapGtm[EVENT_LABEL_KEY].toString(),
                        result.errorCause,
                        alreadyFoundEvent
                    )
                )
            }

            alreadyFoundEvent = true

        }
        if (resultList.isEmpty() && errors.isEmpty())
            errors.append("No query match with these regex in GTM Log.").appendLine()

        return CassavaValidateResult(true, errors.toString(), resultList)
    }

    private fun List<GtmLogDB>.findContaining(comparator: Validator): GtmLogDB? {
        for (gtm in this) {
            val mapGtm = analyticsParser.toJsonMap(gtm.data)
            if (comparator.data.canValidate(mapGtm, currentMode.isInExact()).isValid) {
                return gtm
            }
        }
        return null
    }

    private fun buildErrorStringWithEventAndLabel(
        eventName: String,
        eventLabel: String,
        errorMessage: String,
        alreadyFoundEvent: Boolean
    ): String {
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