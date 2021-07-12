package com.tokopedia.shop.home.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.*
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.home.data.model.GetCampaignNotifyMeModel
import com.tokopedia.shop.home.data.model.GetCampaignNotifyMeRequest
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetCampaignNotifyMeUseCase @Inject constructor(
        private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<GetCampaignNotifyMeModel>() {

    companion object {
        private const val KEY_PARAMS = "params"
        private const val DEFAULT_SOURCE_VALUE = "campaign"

        @JvmStatic
        fun createParams(
                campaignId: String = "",
                source: String = DEFAULT_SOURCE_VALUE
        ): Map<String, Any> {
            val paramsGetCampaignNotifyMe = GetCampaignNotifyMeRequest(
                    campaignId.toIntOrZero(),
                    source
            )
            return mapOf<String, Any>(
                    KEY_PARAMS to paramsGetCampaignNotifyMe
            )
        }
    }

    private val query = """
            query get_campaign_notify_me(${'$'}params:GetCampaignNotifyMeRequest!){
              getCampaignNotifyMe (params: ${'$'}params ){
                campaign_id
                success
                message
                error_message
                is_available
              }
            }
        """.trimIndent()

    var params = mapOf<String, Any>()

    override suspend fun executeOnBackground(): GetCampaignNotifyMeModel {
        gqlUseCase.clearRequest()
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(CacheType.CLOUD_THEN_CACHE).build())
        val gqlRequest = GraphqlRequest(query, GetCampaignNotifyMeModel.Response::class.java, params)
        gqlUseCase.addRequest(gqlRequest)
        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData<GetCampaignNotifyMeModel.Response>(GetCampaignNotifyMeModel.Response::class.java)
                    .getCampaignNotifyMeModel
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }

    fun clearCache() {
        gqlUseCase.clearCache()
    }

}