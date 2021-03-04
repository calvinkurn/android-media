package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerhome.domain.mapper.ShopInfoMapper
import com.tokopedia.sellerhome.domain.model.GetShopInfoResponse
import com.tokopedia.sellerhome.view.model.ShopInfoUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 2020-03-05
 */

class GetShopInfoUseCase(
        private val gqlRepository: GraphqlRepository,
        private val mapper: ShopInfoMapper
) : BaseGqlUseCase<ShopInfoUiModel>() {

    override suspend fun executeOnBackground(): ShopInfoUiModel {
        val gqlRequest = GraphqlRequest(QUERY, GetShopInfoResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))

        val errors = gqlResponse.getError(GetShopInfoResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetShopInfoResponse>()
            val info = data.shopInfoMoengage?.info
            return mapper.mapRemoteModelToUiModel(info)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        private const val USER_ID = "userID"

        fun getRequestParam(userId: String): RequestParams {
            return RequestParams.create().apply {
                putInt(USER_ID, userId.toIntOrZero())
            }
        }

        const val QUERY = "query getShopInfoMoengage(\$userID: Int!) {\n" +
                "  shopInfoMoengage(userID: \$userID) {\n" +
                "    info {\n" +
                "      shop_name\n" +
                "      shop_avatar\n" +
                "    }\n" +
                "  }\n" +
                "}"
    }
}