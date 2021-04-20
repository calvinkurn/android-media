package com.tokopedia.shop.settings.basicinfo.domain

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.settings.basicinfo.data.CheckShopIsOfficialModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class CheckOfficialStoreTypeUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase
): UseCase<CheckShopIsOfficialModel>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): CheckShopIsOfficialModel {
        val checkOsType = GraphqlRequest(QUERY, CheckShopIsOfficialModel::class.java, params.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(checkOsType)
        val gqlResponse = graphqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(CheckShopIsOfficialModel::class.java) ?: listOf()
        if (error == null || error.isEmpty()) {
            return gqlResponse.run {
                getData<CheckShopIsOfficialModel>(CheckShopIsOfficialModel::class.java)
            }
        } else {
            throw MessageErrorException(error.mapNotNull {
                it.message
            }.joinToString(separator = ", "))
        }
    }

    companion object {
        private const val SHOP_ID = "shop_id"
        private const val QUERY = "query getIsOfficial(\$shop_id: Int!){\n" +
                "  getIsOfficial(shop_id: \$shop_id){\n" +
                "    data{\n" +
                "      is_official\n" +
                "      expired_date\n" +
                "    }\n" +
                "    message_error\n" +
                "  }\n" +
                "}"

        fun createRequestParam(shopId: Int): RequestParams = RequestParams.create().apply {
            putInt(SHOP_ID, shopId)
        }
    }
}