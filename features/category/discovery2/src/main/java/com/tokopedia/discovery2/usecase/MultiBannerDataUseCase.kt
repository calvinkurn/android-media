package com.tokopedia.discovery2.usecase

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.multibannerresponse.PushStatusResponse
import com.tokopedia.discovery2.data.multibannerresponse.PushSubscriptionResponse
import com.tokopedia.discovery2.disocverycomponentrepos.MultiBannerRepository


class MultiBannerDataUseCase(private val resources: Resources) {

    suspend fun checkPushStatus(gqlQuery: String, campaignMap: HashMap<String, Int>): PushStatusResponse {
        return MultiBannerRepository().getGQLData(gqlQuery,
                PushStatusResponse::class.java, campaignMap) as PushStatusResponse
    }

    suspend fun subscribeToPush(campaignMap: HashMap<String, Int>): PushSubscriptionResponse {
        return MultiBannerRepository().getGQLData(GraphqlHelper.loadRawString(resources, R.raw.set_push_reminder_gql),
                PushSubscriptionResponse::class.java, campaignMap) as PushSubscriptionResponse
    }

}