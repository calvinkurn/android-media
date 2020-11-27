package com.tokopedia.vouchercreation.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.vouchercreation.common.base.BaseGqlUseCase
import com.tokopedia.vouchercreation.create.domain.model.BasicShopInfoResponse
import com.tokopedia.vouchercreation.create.domain.model.ShopInfo
import javax.inject.Inject

class BasicShopInfoUseCase @Inject constructor(private val gqlRepository: GraphqlRepository) : BaseGqlUseCase<ShopInfo>() {

    companion object {

        const val QUERY = "query ShopInfo(\$userId: Int!) {\n" +
                "  shopInfoMoengage(userID: \$userId) {\n" +
                "    info {\n" +
                "      shop_name\n" +
                "      shop_avatar\n" +
                "    }\n" +
                "  }\n" +
                "}"

        private const val USER_ID_KEY = "userId"

        fun createRequestParams(userId: Int) = RequestParams().apply {
            putInt(USER_ID_KEY, userId)
        }

    }

    override suspend fun executeOnBackground(): ShopInfo {
        val request = GraphqlRequest(QUERY, BasicShopInfoResponse::class.java, params.parameters)
        val response = gqlRepository.getReseponse(listOf(request))

        val error = response.getError(BasicShopInfoResponse::class.java)
        if (error.isNullOrEmpty()) {
            val data = response.getData<BasicShopInfoResponse>()
            return data.mapToShopInfo()
        } else {
            throw MessageErrorException(error.joinToString(", ") {
                it.message
            })
        }
    }

    private fun BasicShopInfoResponse.mapToShopInfo(): ShopInfo = shopInfoMoengage.shopInfo
}