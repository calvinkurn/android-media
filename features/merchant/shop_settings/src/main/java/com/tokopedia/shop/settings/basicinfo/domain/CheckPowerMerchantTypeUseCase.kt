package com.tokopedia.shop.settings.basicinfo.domain

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.settings.basicinfo.data.CheckShopIsPowerMerchantModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class CheckPowerMerchantTypeUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase
): UseCase<CheckShopIsPowerMerchantModel>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): CheckShopIsPowerMerchantModel {
        val checkPowerMerchantType = GraphqlRequest(QUERY, CheckShopIsPowerMerchantModel::class.java, params.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(checkPowerMerchantType)
        val gqlResponse = graphqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(CheckShopIsPowerMerchantModel::class.java) ?: listOf()
        if (error == null || error.isEmpty()) {
            return gqlResponse.run {
                getData<CheckShopIsPowerMerchantModel>(CheckShopIsPowerMerchantModel::class.java)
            }
        } else {
            throw MessageErrorException(error.mapNotNull {
                it.message
            }.joinToString(separator = ", "))
        }
    }

    companion object {
        private const val SHOP_ID = "shopID"
        private const val INCLUDE_OS = "includeOS"
        private const val QUERY = "query goldGetPMOSStatus(\$shopID: Int!, \$includeOS: Boolean!){\n" +
                "  goldGetPMOSStatus(shopID: \$shopID, includeOS: \$includeOS) {\n" +
                "    header {\n" +
                "    \tmessages\n" +
                "      reason\n" +
                "    }\n" +
                "    data {\n" +
                "      power_merchant {\n" +
                "        status\n" +
                "        expired_time\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}"

        fun createRequestParam(shopID: Int, includeOS: Boolean): RequestParams = RequestParams.create().apply {
            putInt(SHOP_ID, shopID)
            putBoolean(INCLUDE_OS, includeOS)
        }
    }
}