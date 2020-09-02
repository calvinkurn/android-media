package com.tokopedia.shop.home.domain

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.*
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.home.GqlQueryConstant.GQL_GET_SHOP_NPL_CAMPAIGN_TNC
import com.tokopedia.shop.home.data.model.GetMerchantCampaignTNCRequest
import com.tokopedia.shop.home.data.model.ShopHomeCampaignNplTncModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetShopHomeCampaignNplTncUseCase @Inject constructor(
        @Named(GQL_GET_SHOP_NPL_CAMPAIGN_TNC)
        val gqlQuery: String,
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

    var params = mapOf<String, Any>()

    override suspend fun executeOnBackground(): ShopHomeCampaignNplTncModel {
        gqlUseCase.clearRequest()
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(CacheType.CLOUD_THEN_CACHE).build())
        val gqlRequest = GraphqlRequest(gqlQuery, ShopHomeCampaignNplTncModel.Response::class.java, params)
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