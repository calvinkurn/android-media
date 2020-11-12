package com.tokopedia.categorylevels.analytics

import com.tokopedia.discovery2.analytics.DiscoveryAnalytics
import com.tokopedia.discovery2.analytics.EMPTY_STRING
import com.tokopedia.trackingoptimizer.TrackingQueue

class CategoryPageAnalyticss(pageType: String = EMPTY_STRING, pagePath: String = EMPTY_STRING, pageIdentifier: String = EMPTY_STRING, campaignCode: String = EMPTY_STRING, sourceIdentifier: String = EMPTY_STRING, trackingQueue: TrackingQueue) : DiscoveryAnalytics(pageType, pagePath, pageIdentifier, campaignCode, sourceIdentifier, trackingQueue) {

}