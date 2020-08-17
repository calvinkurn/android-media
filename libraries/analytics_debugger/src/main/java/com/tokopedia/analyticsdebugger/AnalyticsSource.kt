package com.tokopedia.analyticsdebugger

import androidx.annotation.StringDef
import com.tokopedia.analyticsdebugger.AnalyticsSource.Companion.APPS_FLYER
import com.tokopedia.analyticsdebugger.AnalyticsSource.Companion.BRANCH_IO
import com.tokopedia.analyticsdebugger.AnalyticsSource.Companion.GTM


@Retention(AnnotationRetention.SOURCE)
@StringDef(GTM, BRANCH_IO, APPS_FLYER)
annotation class AnalyticsSource {
    companion object {
        const val GTM = "gtm"
        const val BRANCH_IO = "branch_io"
        const val APPS_FLYER = "apps_flyer"
    }
}