package com.tokopedia.notifcenter.stub.data.repository

import com.google.gson.JsonObject
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.notifcenter.stub.common.AndroidFileUtil
import com.tokopedia.topads.sdk.domain.model.TopAdsBannerResponse
import com.tokopedia.topads.sdk.repository.TopAdsRepository
import java.lang.reflect.Type
import javax.inject.Inject

class FakeTopAdsRepository @Inject constructor(
    private val repository: FakeTopAdsRestRepository
) : TopAdsRepository() {

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
        get() = AndroidFileUtil.parse(
            "topads/notifcenter_tdn.json",
            TopAdsBannerResponse::class.java
        )

    val noDataResponse: TopAdsBannerResponse
        get() {
            val responseObj: JsonObject = AndroidFileUtil.parse(
                "topads/notifcenter_tdn.json",
                JsonObject::class.java
            )
            responseObj
                .getAsJsonObject("topadsDisplayBannerAdsV3")
                .getAsJsonArray("data")
                .remove(0)
            return CommonUtil.fromJson(
                responseObj.toString(),
                TopAdsBannerResponse::class.java
            )
        }

    override val restRepository: RestRepository
        get() = repository
}

class FakeTopAdsRestRepository : RestRepository {

    var isError = false
    var response = DataResponse<TopAdsBannerResponse>()

    override suspend fun getResponse(request: RestRequest): RestResponse {
        if (isError) {
            throw IllegalStateException("Error Get TDN")
        }
        return RestResponse(response, 200, false)
    }

    override suspend fun getResponses(requests: List<RestRequest>): Map<Type, RestResponse> {
        return HashMap()
    }
}
