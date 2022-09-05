package com.tokopedia.analyticsdebugger.debugger.domain.model

import com.tokopedia.analyticsdebugger.cassava.AnalyticsSource

/**
 * @author okasurya on 5/16/18.
 */
data class AnalyticsLogData (
    var data: String,
    var name: String,
    var source: String
)
