package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhome.domain.gqlquery.GqlGetShopInfo
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
        val gqlRequest = GraphqlRequest(GqlGetShopInfo, GetShopInfoResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.response(listOf(gqlRequest))

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
                putLong(USER_ID, userId.toLongOrZero())
            }
        }
    }
}