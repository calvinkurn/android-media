package com.tokopedia.analyticsdebugger.validator.core

import com.tokopedia.analyticsdebugger.database.GtmLogDB
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import rx.Observable

class ValidatorEngine constructor(private val dao: GtmLogDBSource) {

    enum class Mode {
        SUBSET_ALL, SUBSET_ORDER, EXACT_ALL, EXACT_ORDER;

        fun isInOrder():Boolean = (this == SUBSET_ORDER || this == EXACT_ORDER)
        fun isInExact():Boolean = (this == EXACT_ORDER || this == EXACT_ALL)
    }

    private var currentMode = Mode.SUBSET_ALL

    fun computeRx(testCases: List<Validator>, mode: String = "subset"): Observable<List<Validator>> {
        setMode(mode)

        return dao.getAllLogs().map { logs ->
            compute(testCases, logs)
        }
    }

    suspend fun computeCo(testCases: List<Validator>, mode: String = "subset"): List<Validator> {
        return compute(testCases, dao.getLogs())
    }

    fun compute(testCases: List<Validator>, logs: List<GtmLogDB>): List<Validator> {
        var ordering: Long = 0
        val newResult: MutableList<Validator> = mutableListOf()
        testCases.forEach { case ->
            val matched = logs.findAllContaining(case).map { it.toUiModel() }
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
                newResult.add(case.copy(status = status, matches = matched))
            } else {
                newResult.add(case.copy(status = Status.FAILURE))
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

    private fun List<GtmLogDB>.findAllContaining(comparator: Validator): List<GtmLogDB> {
        val resultList: MutableList<GtmLogDB> = mutableListOf()
        for (gtm in this) {
            val mapGtm = gtm.data!!.toJsonMap()
            if (comparator.data.canValidate(mapGtm, currentMode.isInExact())) {
                resultList.add(gtm)
            }
        }
        return resultList
    }

    private fun List<GtmLogDB>.findContaining(comparator: Validator): GtmLogDB? {
        for (gtm in this) {
            val mapGtm = gtm.data!!.toJsonMap()
            if (comparator.data.canValidate(mapGtm, currentMode.isInExact())) {
                return gtm
            }
        }
        return null
    }

}