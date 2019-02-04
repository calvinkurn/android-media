package com.tokopedia.tkpd.campaign.domain.shake

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.tkpd.R
import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by nisie on 04/02/19.
 */
class GetCampaignUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase,
                                             private val resources: Resources) {

    companion object {

        const val IS_AUDIO = "is_audio"
        const val SCREEN_NAME = "source"
        const val PARAM_LATITUDE = "latitude"
        const val PARAM_LONGITUDE = "longitude"

        fun generateParam(source: String = "",
                          latitude: Double = 0.0,
                          longitude: Double = 0.0,
                          isAudio: Boolean = false):
                Map<String,
                        Any> {
            val requestParams = HashMap<String, Any>()
            requestParams[IS_AUDIO] = isAudio
            requestParams[SCREEN_NAME] = source
            requestParams[PARAM_LATITUDE] = latitude
            requestParams[PARAM_LONGITUDE] = longitude
            return requestParams
        }
    }

    fun execute(requestParams: Map<String, Any>, subscriber: Subscriber<GraphqlResponse>) {
        val query = GraphqlHelper.loadRawString(resources, R.raw.query_get_campaign_list)
        val graphqlRequest = GraphqlRequest(query,
                CampaignResponseEntity::class.java, requestParams)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }
}