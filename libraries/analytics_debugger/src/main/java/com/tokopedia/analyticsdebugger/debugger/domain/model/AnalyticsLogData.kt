package com.tokopedia.analyticsdebugger.debugger.domain.model

import com.tokopedia.analyticsdebugger.AnalyticsSource

/**
 * @author okasurya on 5/16/18.
 */
class AnalyticsLogData {
    var data: String? = null
    var name: String? = null
    var category: String? = null
    @AnalyticsSource var source: String? = null
}
