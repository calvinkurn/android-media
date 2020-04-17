package com.tokopedia.analyticsdebugger.validator.core

import com.tokopedia.analyticsdebugger.validator.Utils

internal fun Map<String, Any>.toDefaultValidator() = Validator(
        Utils.getAnalyticsName(this),
        this
)