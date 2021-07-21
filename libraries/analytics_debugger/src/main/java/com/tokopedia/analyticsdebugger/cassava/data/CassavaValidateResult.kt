package com.tokopedia.analyticsdebugger.cassava.data

import com.tokopedia.analyticsdebugger.database.GtmLogDB

/**
 * @author by furqan on 16/07/2021
 */
data class CassavaValidateResult(
        var isValid: Boolean,
        var errorCause: String,
        var listGtmLog: List<GtmLogDB> = emptyList()
)