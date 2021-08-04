package com.tokopedia.shop.home.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.*
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.home.data.model.CheckCampaignNotifyMeModel
import com.tokopedia.shop.home.data.model.CheckCampaignNotifyMeRequest
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class CheckCampaignNotifyMeUseCase @Inject constructor(
        private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<CheckCampaignNotifyMeModel>() {

    companion object {
        private const val KEY_PARAMS = "params"
        private const val DEFAULT_SOURCE_VALUE = "campaign"
        private const val DEFAULT_REQUEST_TYPE_VALUE = "CAMPAIGN"

        @JvmStatic
        fun createParams(
                campaignId: String = "",
                action: String = "",
                source: String = DEFAULT_SOURCE_VALUE
        ): Map<String, Any> {
            val paramsGetCampaignNotifyMe = CheckCampaignNotifyMeRequest(
                    campaignId.toIntOrZero(),
                    action,
                    source,
                    DEFAULT_REQUEST_TYPE_VALUE
            )
            return mapOf<String, Any>(
                    KEY_PARAMS to paramsGetCampaignNotifyMe
            )
        }
    }

    private val query = """
            mutation check_campaign_notify_me(${'$'}params : CheckCampaignNotifyMeRequest!){
              checkCampaignNotifyMe(params:${'$'}params ) {
                campaign_id
                success
                message
                error_message
              }
            }
        """.trimIndent()

    var params = mapOf<String, Any>()

    override suspend fun executeOnBackground(): CheckCampaignNotifyMeModel {
        gqlUseCase.clearRequest()
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(CacheType.CLOUD_THEN_CACHE).build())
        val gqlRequest = GraphqlRequest(query, CheckCampaignNotifyMeModel.Response::class.java, params)
        gqlUseCase.addRequest(gqlRequest)
        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData<CheckCampaignNotifyMeModel.Response>(CheckCampaignNotifyMeModel.Response::class.java)
                    .checkCampaignNotifyMeModel
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }

    fun clearCache() {
        gqlUseCase.clearCache()
    }

}