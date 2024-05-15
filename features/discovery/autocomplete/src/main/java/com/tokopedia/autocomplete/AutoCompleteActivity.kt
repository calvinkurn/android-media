package com.tokopedia.autocomplete

import com.tokopedia.analytics.byteio.AppLogInterface
import com.tokopedia.analytics.byteio.IAdsLog
import com.tokopedia.autocompletecomponent.BaseAutoCompleteActivity
import com.tokopedia.telemetry.ITelemetryActivity

open class AutoCompleteActivity:
    BaseAutoCompleteActivity(),
    ITelemetryActivity,
    AppLogInterface, IAdsLog {

    override fun getTelemetrySectionName() = "autocomplete"

    override fun getPageName(): String = ""

    override fun getAdsPageName(): String = ""
}
