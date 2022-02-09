package com.tokopedia.pdp.fintech.analytics

import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class PdpFintechWidgetAnalytics @Inject constructor(
    private val userSession: dagger.Lazy<UserSessionInterface>
) {

    fun sendAnalyticsEvent(baseAnalyticsDataClass:BaseAnalyticsData)
    {

    }


}