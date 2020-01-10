package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.data.multibannerresponse.PushStatusResponse
import com.tokopedia.discovery2.data.multibannerresponse.PushSubscriptionResponse
import com.tokopedia.discovery2.disocverycomponentrepos.MultiBannerRepository


class MultiBannerDataUseCase() {

    suspend fun checkPushStatus(gqlQuery: String, campaignMap: HashMap<String, Int>): PushStatusResponse {
        return MultiBannerRepository().getGQLData(gqlQuery,
                PushStatusResponse::class.java, campaignMap) as PushStatusResponse
    }

    suspend fun subscribeToPush(gqlQuery: String, campaignMap: HashMap<String, Int>): PushSubscriptionResponse {
        return MultiBannerRepository().getGQLData(gqlQuery,
                PushSubscriptionResponse::class.java, campaignMap) as PushSubscriptionResponse
    }

}