package com.tokopedia.inbox.fake.domain.usecase.notifcenter.topads

import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.inbox.common.AndroidFileUtil
import com.tokopedia.inbox.test.R
import com.tokopedia.topads.sdk.domain.model.TopAdsmageViewResponse
import com.tokopedia.topads.sdk.repository.TopAdsRepository

class FakeTopAdsRepository : TopAdsRepository() {

    var response = TopAdsmageViewResponse(null, null, null)
        set(value) {
            field = value
            repository.response = value
        }

    val defaultResponse: TopAdsmageViewResponse
        get() = AndroidFileUtil.parseRaw(
            R.raw.notifcenter_tdn, TopAdsmageViewResponse::class.java
        )

    private val repository = FakeTopAdsRestRepository()

    override val restRepository: RestRepository
        get() = repository

    fun initialize() {
        this.response = defaultResponse
    }
}