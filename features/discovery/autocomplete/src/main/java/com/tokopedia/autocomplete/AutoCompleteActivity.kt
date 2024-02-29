package com.tokopedia.autocomplete

import com.tokopedia.analytics.byteio.AppLogInterface
import com.tokopedia.autocompletecomponent.BaseAutoCompleteActivity
import com.tokopedia.telemetry.ITelemetryActivity

open class AutoCompleteActivity:
    BaseAutoCompleteActivity(),
    ITelemetryActivity,
    AppLogInterface {

    override fun getTelemetrySectionName() = "autocomplete"

    override fun getPageName(): String = ""
}
