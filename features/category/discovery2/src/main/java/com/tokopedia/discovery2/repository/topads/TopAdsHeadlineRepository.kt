package com.tokopedia.discovery2.repository.topads

interface TopAdsHeadlineRepository {
    fun getHeadlineAdsParams(depId : String, paramsMobile : String) : String
}