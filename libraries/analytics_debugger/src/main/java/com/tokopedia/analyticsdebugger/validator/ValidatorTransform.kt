package com.tokopedia.analyticsdebugger.validator

internal fun Map<String, Any>.toDefaultValidator() = Validator(
        Utils.getAnalyticsName(this),
        this
)