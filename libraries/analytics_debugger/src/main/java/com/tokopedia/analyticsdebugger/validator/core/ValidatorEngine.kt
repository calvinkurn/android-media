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

    fun compute(testCases: List<Validator>, mode: String = "subset"): Observable<List<Validator>> {
        setMode(mode)

        return dao.getAllLogs().map { logs ->
            var ordering: Long = 0
            val newResult: MutableList<Validator> = mutableListOf()
            testCases.forEach { case ->
                logs.findContaining(case)?.let {
                    val status = when {
                        currentMode.isInOrder() && it.timestamp < ordering -> {
                            ordering = Long.MAX_VALUE
                            Status.FAILURE
                        }
                        else -> {
                            ordering = it.timestamp
                            Status.SUCCESS
                        }
                    }
                    newResult.add(case.copy(status = status, match = it))
                } ?: newResult.add(case.copy(status = Status.FAILURE))
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