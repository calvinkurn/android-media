package com.tokopedia.shop.info.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.info.data.GetEpharmacyShopInfoResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetEpharmacyShopInfoUseCase @Inject constructor(
    private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<GetEpharmacyShopInfoResponse>() {

    companion object {
        private const val SHOP_ID = "shop_id"

        fun createParams(shopId: Int) = mapOf<String, Any>(
            SHOP_ID to shopId
        )
    }

    private val QUERY = """
        getEpharmacyShopInfo(shop_id: ${'$'}shop_id) {
            header {
                process_time
                error_code
                error_message
            }
            data {
                sia_number
                sipa_number
                apj
                epharmacy_working_hours
                epharmacy_working_hours_fmt      
            }
          }
    """.trimIndent()

    var params = mapOf<String, Any>()

    override suspend fun executeOnBackground(): GetEpharmacyShopInfoResponse {
        gqlUseCase.clearRequest()
        gqlUseCase.setCacheStrategy(
            GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build()
        )
        val gqlRequest = GraphqlRequest(QUERY, GetEpharmacyShopInfoResponse::class.java, params)
        gqlUseCase.addRequest(gqlRequest)
        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData(GetEpharmacyShopInfoResponse::class.java)
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }
}
