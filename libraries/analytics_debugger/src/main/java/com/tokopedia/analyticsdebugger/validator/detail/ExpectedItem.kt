package com.tokopedia.analyticsdebugger.validator.detail

import com.tokopedia.analyticsdebugger.validator.core.GtmLogUi

internal data class ExpectedItem(
        val data: GtmLogUi,
        var expanded: Boolean = false
)