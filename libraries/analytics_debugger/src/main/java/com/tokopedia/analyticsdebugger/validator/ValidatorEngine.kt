package com.tokopedia.analyticsdebugger.validator

import com.tokopedia.analyticsdebugger.database.GtmLogDB
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import rx.Observable

class ValidatorEngine constructor(private val dao: GtmLogDBSource) {

    fun compute(testCases: List<Validator>): Observable<List<Validator>> {
        return dao.getAllLogs().map { logs ->
            val newResult: MutableList<Validator> = mutableListOf()
            testCases.forEach { case ->
                if (logs.contains(case)) {
                    newResult.add(case.copy(status = Status.SUCCESS))
                } else {
                    newResult.add(case.copy(status = Status.FAILURE))
                }
            }
            newResult
        }
    }

    private fun List<GtmLogDB>.contains(comparator: Validator): Boolean {
        for (gtm in this) {
            val mapGtm = Utils.jsonToMap(gtm.data!!)
            var exact = true
            inner@ for (entry in comparator.data) {
                if (!mapGtm.containsKeyValue(entry)) {
                    exact = false
                    break@inner
                }
            }
            if (exact) return true
        }
        return false
    }

    private fun Map<String, Any>.containsKeyValue(kv: Map.Entry<String, Any>): Boolean =
            (this.containsKey(kv.key) && this[kv.key] == kv.value)
}