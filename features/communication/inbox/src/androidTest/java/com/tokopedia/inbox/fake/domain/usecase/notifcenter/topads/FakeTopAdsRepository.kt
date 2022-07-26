package com.tokopedia.inbox.fake.domain.usecase.notifcenter.topads

import com.google.gson.JsonObject
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.inbox.common.AndroidFileUtil
import com.tokopedia.inbox.test.R
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topads.sdk.domain.model.TopAdsBannerResponse
import com.tokopedia.topads.sdk.repository.TopAdsRepository

class FakeTopAdsRepository : TopAdsRepository() {

    var isError = false
        set(value) {
            field = value
            repository.isError = value
        }

    var response = TopAdsBannerResponse(
        TopAdsBannerResponse.TopadsDisplayBannerAdsV3(null, null)
    )
        set(value) {
            field = value
            val dataResponse = DataResponse<TopAdsBannerResponse>().apply {
                data = value
            }
            repository.response = dataResponse
        }

    val defaultResponse: TopAdsBannerResponse
        get() = AndroidFileUtil.parseRaw(
            R.raw.notifcenter_tdn, TopAdsBannerResponse::class.java
        )

    val noDataResponse: TopAdsBannerResponse
        get() {
            val responseObj: JsonObject = AndroidFileUtil.parseRaw(
                R.raw.notifcenter_tdn, JsonObject::class.java
            )
            responseObj
                .getAsJsonObject("topadsDisplayBannerAdsV3")
                .getAsJsonArray("data")
                .remove(0)
            return CommonUtil.fromJson(
                responseObj.toString(), TopAdsBannerResponse::class.java
            )
        }

    private val repository = FakeTopAdsRestRepository()

    override val restRepository: RestRepository
        get() = repository

    fun initialize() {
        this.response = defaultResponse
    }
}