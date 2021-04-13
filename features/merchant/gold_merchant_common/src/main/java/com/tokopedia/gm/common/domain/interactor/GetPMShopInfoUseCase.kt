package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.data.source.cloud.model.GMShopInfoResponse
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.gm.common.domain.mapper.PMShopInfoMapper
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 09/03/21
 */

class GetPMShopInfoUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository,
        private val mapper: PMShopInfoMapper
) : BaseGqlUseCase<PMShopInfoUiModel>() {

    override suspend fun executeOnBackground(): PMShopInfoUiModel {
        val gqlRequest = GraphqlRequest(QUERY, GMShopInfoResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val errors: List<GraphqlError>? = gqlResponse.getError(GMShopInfoResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<GMShopInfoResponse>()
            return mapper.mapRemoteModelToUiModel(response.goldGetPMShopInfo)
        } else {
            throw MessageErrorException(errors.joinToString(" - ") { it.message })
        }
    }

    companion object {
        private const val KEY_SHOP_ID = "shop_id"
        private const val KEY_SOURCE = "source"

        private val QUERY = """
          query goldGetPMShopInfo(${'$'}shop_id: Int!, ${'$'}source: String!) {
            goldGetPMShopInfo(shop_id: ${'$'}shop_id, source: ${'$'}source) {
              shop_id
              is_new_seller
              shop_age
              is_kyc
              kyc_status_id
              shop_score_sum
              shop_score_threshold
              is_has_active_product
              shop_level
              is_eligible_shop_score
              is_eligible_pm
            }
          }
        """.trimIndent()

        fun createParams(shopId: String, source: String): RequestParams {
            return RequestParams.create().apply {
                putLong(KEY_SHOP_ID, shopId.toLongOrZero())
                putString(KEY_SOURCE, source)
            }
        }
    }
}