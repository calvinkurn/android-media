package com.tokopedia.analyticsdebugger.cassava.validator.detail

import com.tokopedia.analyticsdebugger.cassava.validator.core.GtmLogUi

internal data class ExpectedItem(
        val data: GtmLogUi,
        var expanded: Boolean = false
)