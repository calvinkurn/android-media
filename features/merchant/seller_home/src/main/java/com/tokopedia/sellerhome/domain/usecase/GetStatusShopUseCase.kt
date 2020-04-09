package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerhome.domain.model.GetShopStatusResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 2020-02-27
 */

class GetStatusShopUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository
) : BaseGqlUseCase<GetShopStatusResponse>() {

    override suspend fun executeOnBackground(): GetShopStatusResponse {
        val gqlRequest = GraphqlRequest(QUERY, GetShopStatusResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))

        val errors: List<GraphqlError>? = gqlResponse.getError(GetShopStatusResponse::class.java)
        if (errors.isNullOrEmpty()) {
            return gqlResponse.getData(GetShopStatusResponse::class.java)
        } else throw MessageErrorException(errors.joinToString(", ") { it.message })
    }

    companion object {
        private const val SHOP_ID = "shop_id"

        fun createRequestParams(shopId: String): RequestParams {
            return RequestParams.create().apply {
                putInt(SHOP_ID, shopId.toIntOrNull() ?: 0)
            }
        }

        const val QUERY = "query goldGetPMOSStatus(\$shop_id: Int!) {\n" +
                "  goldGetPMOSStatus(shopID : \$shop_id, includeOS: true) {\n" +
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
    }
}