package com.tokopedia.shop.pageheader.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.pageheader.data.model.ShopRequestUnmoderateBaseResponse
import com.tokopedia.shop.pageheader.data.model.ShopRequestUnmoderateRequestParams
import com.tokopedia.shop.pageheader.data.model.ShopRequestUnmoderateSuccessResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * author by Rafli Syam on 26/01/2021
 */
class ShopRequestUnmoderateUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository
) : GraphqlUseCase<ShopRequestUnmoderateSuccessResponse>(gqlRepository) {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ShopRequestUnmoderateSuccessResponse {
        val request = GraphqlRequest(MUTATION, ShopRequestUnmoderateSuccessResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(
                listOf(request),
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        )
        val gqlErrorResponse = gqlResponse.getError(ShopRequestUnmoderateBaseResponse::class.java)
        if (gqlErrorResponse == null || gqlErrorResponse.isEmpty()) {
            return gqlResponse.getData<ShopRequestUnmoderateSuccessResponse>(ShopRequestUnmoderateSuccessResponse::class.java)
        } else {
            throw MessageErrorException(gqlErrorResponse.joinToString(", ") { it.message })
        }
    }

    companion object {

        private const val INPUT = "input"
        private const val REQUEST_OPEN_MODERATE_STATUS = 1

        fun createRequestParams(
                shopId : Double,
                description : String
        ) : RequestParams = RequestParams.create().apply {
            putObject(INPUT, ShopRequestUnmoderateRequestParams().apply {
                shopIds = mutableListOf(shopId)
                status = REQUEST_OPEN_MODERATE_STATUS
                responseDescription = description
            })
        }

        /**
         * Mutation for request unmoderate shop
         */
        private const val MUTATION = "mutation moderateShop(\$input: ShopModerate!) {\n" +
                "  moderateShop(input:\$input) {\n" +
                "    success\n" +
                "    message\n" +
                "  }\n" +
                "}"
    }

}