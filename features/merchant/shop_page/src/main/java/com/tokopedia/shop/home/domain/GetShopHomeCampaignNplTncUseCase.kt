package com.tokopedia.shop.home.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.*
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.home.data.model.GetMerchantCampaignTNCRequest
import com.tokopedia.shop.home.data.model.ShopHomeCampaignNplTncModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetShopHomeCampaignNplTncUseCase @Inject constructor(
        private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<ShopHomeCampaignNplTncModel>() {

    companion object {
        private const val KEY_PARAM = "param"

        @JvmStatic
        fun createParams(
                campaignId: String = ""
        ):Map<String, Any>{
            val paramGetCampaignTnc = GetMerchantCampaignTNCRequest(
                    campaignId.toIntOrZero()
            )
            return mapOf<String, Any>(
                    KEY_PARAM to paramGetCampaignTnc
            )
        }
    }

    private val query = """
            query get_merchant_campaign_tnc(${'$'}param: GetMerchantCampaignTNCRequest!){
              getMerchantCampaignTNC (params:${'$'}param){
                title,
    			messages,
    			error {
    			  error_code
    			  error_message
    			}
              }
            }
        """.trimIndent()

    var params = mapOf<String, Any>()

    override suspend fun executeOnBackground(): ShopHomeCampaignNplTncModel {
        gqlUseCase.clearRequest()
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(CacheType.CLOUD_THEN_CACHE).build())
        val gqlRequest = GraphqlRequest(query, ShopHomeCampaignNplTncModel.Response::class.java, params)
        gqlUseCase.addRequest(gqlRequest)
        val gqlResponse = gqlUseCase.executeOnBackground()

        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData<ShopHomeCampaignNplTncModel.Response>(ShopHomeCampaignNplTncModel.Response::class.java)
                    .campaignTnc
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }

    fun clearCache() {
        gqlUseCase.clearCache()
    }

}