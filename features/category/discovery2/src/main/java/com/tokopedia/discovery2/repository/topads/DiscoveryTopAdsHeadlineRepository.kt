package com.tokopedia.discovery2.repository.topads

class DiscoveryTopAdsHeadlineRepository : TopAdsHeadlineRepository {
    override fun getHeadlineAdsParams(depId: String, paramsMobile : String): String {
        return paramsMobile
    }
}