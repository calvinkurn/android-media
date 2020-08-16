package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.gm.common.data.source.cloud.model.GoldGetPmOsStatus
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetShopStatusUseCaseCoroutine @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase
): UseCase<GoldGetPmOsStatus>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): GoldGetPmOsStatus {
        val checkPowerMerchantType = GraphqlRequest(QUERY, GoldGetPmOsStatus::class.java, params.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(checkPowerMerchantType)
        val gqlResponse = graphqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(GoldGetPmOsStatus::class.java) ?: listOf()
        if (error == null || error.isEmpty()) {
            return gqlResponse.run {
                getData<GoldGetPmOsStatus>(GoldGetPmOsStatus::class.java)
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
                "      process_time\n" +
                "      messages\n" +
                "      reason\n" +
                "      error_code\n" +
                "    }\n" +
                "    data {\n" +
                "      shopID\n" +
                "      power_merchant{\n" +
                "        status\n" +
                "        auto_extend{\n" +
                "          status\n" +
                "          tkpd_product_id\n" +
                "        }\n" +
                "        expired_time\n" +
                "        shop_popup\n" +
                "      }\n" +
                "      official_store{\n" +
                "        status\n" +
                "        error\n" +
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