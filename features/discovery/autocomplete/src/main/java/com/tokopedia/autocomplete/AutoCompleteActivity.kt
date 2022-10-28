package com.tokopedia.autocomplete

import com.tokopedia.autocompletecomponent.BaseAutoCompleteActivity
import com.tokopedia.telemetry.ITelemetryActivity

open class AutoCompleteActivity: BaseAutoCompleteActivity(), ITelemetryActivity {
    override fun getTelemetrySectionName() = "autocomplete"
}