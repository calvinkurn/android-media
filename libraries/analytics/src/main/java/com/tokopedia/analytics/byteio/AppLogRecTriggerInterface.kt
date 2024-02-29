package com.tokopedia.analytics.byteio

interface AppLogRecTriggerInterface {

    /**
     * Recommendation session id from recommendation response
     */
    fun getRecommendationTriggerObject() : RecommendationTriggerObject

    /**
     * Override this method if the ViewHolder used are shared for recom & no-recom use.
     * Default value is true
     */
    fun isEligibleToTrack(): Boolean = true
}
