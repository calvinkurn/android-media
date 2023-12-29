package com.tokopedia.notifcenter.stub.data.repository

import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.notifcenter.stub.common.AndroidFileUtil
import com.tokopedia.topads.sdk.domain.model.TopAdsBannerResponse
import com.tokopedia.topads.sdk.repository.TopAdsRepository
import javax.inject.Inject

@ActivityScope
class FakeTopAdsRepository @Inject constructor() : TopAdsRepository() {

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
            repository.response = value
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

    private val repository = FakeTopAdsRestRepository()

    override val graphqlRepository: GraphqlRepository
        get() = repository
}
