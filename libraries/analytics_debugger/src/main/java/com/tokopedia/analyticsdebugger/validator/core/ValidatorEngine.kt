package com.tokopedia.analyticsdebugger.validator.core

import com.tokopedia.analyticsdebugger.database.GtmLogDB
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import rx.Observable

class ValidatorEngine constructor(private val dao: GtmLogDBSource) {

    fun compute(testCases: List<Validator>, inOrder: Boolean = false): Observable<List<Validator>> {
        return dao.getAllLogs().map { logs ->
            var ordering: Long = 0
            val newResult: MutableList<Validator> = mutableListOf()
            testCases.forEach { case ->
                logs.findContaining(case)?.let {
                    val status = when {
                        inOrder && it.timestamp < ordering -> {
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

    private fun List<GtmLogDB>.findContaining(comparator: Validator): GtmLogDB? {
        for (gtm in this) {
            val mapGtm = gtm.data!!.toJsonMap()
            if (comparator.data.canValidate(mapGtm)) {
                return gtm
            }
        }
        return null
    }

}