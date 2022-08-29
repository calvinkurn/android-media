package com.tokopedia.analyticsdebugger.cassava.ui.detail

import com.tokopedia.analyticsdebugger.cassava.core.GtmLogUi

internal data class ExpectedItem(
        val data: GtmLogUi,
        var expanded: Boolean = false
)