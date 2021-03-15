package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.data.source.cloud.model.PMGradeBenefitInfoResponse
import com.tokopedia.gm.common.data.source.local.model.PMGradeWithBenefitsUiModel
import com.tokopedia.gm.common.domain.mapper.PMGradeBenefitInfoMapper
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 10/03/21
 */

class GetPMGradeWithBenefitUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository,
        private val mapper: PMGradeBenefitInfoMapper
) : BaseGqlUseCase<List<PMGradeWithBenefitsUiModel>>() {

    override suspend fun executeOnBackground(): List<PMGradeWithBenefitsUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, PMGradeBenefitInfoResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val errors: List<GraphqlError>? = gqlResponse.getError(PMGradeBenefitInfoResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<PMGradeBenefitInfoResponse>()
            return mapper.getPMGradeBenefitList(response.data?.pmGradeBenefitList).orEmpty()
        } else {
            throw MessageErrorException(errors.joinToString(" - ") { it.message })
        }
    }

    companion object {
        private const val KEY_SHOP_ID = "shop_id"
        private const val KEY_SOURCE = "source"

        fun createParams(shopId: String, source: String): RequestParams {
            return RequestParams.create().apply {
                putLong(KEY_SHOP_ID, shopId.toLongOrZero())
                putString(KEY_SOURCE, source)
            }
        }

        private val QUERY = """
            query goldGetPMGradeBenefitInfo(${'$'}shop_id: Int!, ${'$'}source: String!) {
              goldGetPMGradeBenefitInfo(shop_id: ${'$'}shop_id, source: ${'$'}source) {
                pm_grade_benefit_list {
                  pm_grade_name
                  image_badge_url
                  is_active
                  benefit_list {
                    benefit_name
                    benefit_category
                    benefit_description
                    related_link_applink
                    seq_num
                    image_url
                  }
                }
              }
            }
        """.trimIndent()
    }
}