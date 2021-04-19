package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.data.source.cloud.model.PMInterruptDataResponse
import com.tokopedia.gm.common.domain.mapper.GetPMInterruptDataMapper
import com.tokopedia.gm.common.view.model.PowerMerchantInterruptUiModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 20/03/21
 */

class GetPMInterruptDataUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository,
        private val mapper: GetPMInterruptDataMapper
) : BaseGqlUseCase<PowerMerchantInterruptUiModel>() {

    override suspend fun executeOnBackground(): PowerMerchantInterruptUiModel {
        val gqlRequest = GraphqlRequest(QUERY, PMInterruptDataResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val gqlErrors = gqlResponse.getError(PMInterruptDataResponse::class.java)
        if (gqlErrors.isNullOrEmpty()) {
            val response = gqlResponse.getData<PMInterruptDataResponse>()
            return mapper.mapRemoteModelToUiModel(response)
        } else {
            throw MessageErrorException(gqlErrors.joinToString(" - ") { it.message })
        }
    }

    companion object {
        private const val KEY_SHOP_ID = "shopId"
        private const val KEY_SOURCE = "source"
        private val QUERY = """
            query getPMInterruptData(${'$'}shopId: Int!, ${'$'}source: String!) {
              goldGetPMGradeBenefitInfo(shop_id: ${'$'}shopId, source: ${'$'}source) {
                next_monthly_refresh_date
                current_pm_grade {
                  grade_name
                  image_badge_url
                }
                potential_pm_grade {
                  grade_name
                  image_badge_url
                }
              }
              goldGetPMShopInfo(shop_id: ${'$'}shopId, source: ${'$'}source) {
                shop_score_sum
                shop_score_threshold
                shop_level
                is_eligible_pm
                is_new_seller
                shop_age
              }
              goldGetPMOSStatus(shopID: ${'$'}shopId, includeOS: false) {
                data {
                  power_merchant {
                    status
                  }
                  official_store {
                    status
                  }
                }
              }
              goldGetPMSettingInfo(shopID: ${'$'}shopId, source:${'$'}source) {
                period_type
              }
              reputation_shops(shop_ids: [${'$'}shopId]) {
                score
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