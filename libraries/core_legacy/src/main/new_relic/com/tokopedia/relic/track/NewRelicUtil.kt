package com.tokopedia.relic.track

import com.newrelic.agent.android.NewRelic

class NewRelicUtil {
    companion object {
        @JvmStatic
        fun sendTrack(eventName: String, map: Map<String, Any>)  = {
            NewRelic.recordBreadcrumb(eventName, map)
        }
    }
}