package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.data.source.cloud.model.PMGradeBenefitInfoResponse
import com.tokopedia.gm.common.data.source.local.model.PMGradeBenefitInfoUiModel
import com.tokopedia.gm.common.domain.mapper.PMGradeBenefitInfoMapper
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 10/03/21
 */

class GetPMGradeAndBenefitInfoUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository,
        private val mapper: PMGradeBenefitInfoMapper
) : BaseGqlUseCase<PMGradeBenefitInfoUiModel>() {

    override suspend fun executeOnBackground(): PMGradeBenefitInfoUiModel {
        val gqlRequest = GraphqlRequest(QUERY, PMGradeBenefitInfoResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val errors: List<GraphqlError>? = gqlResponse.getError(PMGradeBenefitInfoResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<PMGradeBenefitInfoResponse>()
            return mapper.mapRemoteModelToUiModel(response.data)
        } else {
            throw MessageErrorException(errors.joinToString(" - ") { it.message })
        }
    }

    companion object {
        private const val KEY_SHOP_ID = "shop_id"
        private const val KEY_SOURCE = "source"
        private const val KEY_FIELDS = "fields"

        fun createParams(shopId: String, source: String, fields: Array<String>): RequestParams {
            return RequestParams.create().apply {
                putLong(KEY_SHOP_ID, shopId.toLongOrZero())
                putString(KEY_SOURCE, source)
                putObject(KEY_FIELDS, fields)
            }
        }

        private val QUERY = """
            query goldGetPMGradeBenefitInfo(${'$'}shop_id: Int!, ${'$'}source: String!, ${'$'}fields: [String]) {
              goldGetPMGradeBenefitInfo(shop_id: ${'$'}shop_id, source: ${'$'}source, fields: ${'$'}fields) {
                shop_id
                next_monthly_refresh_date
                next_quarterly_calibration_refresh_date
                current_pm_grade {
                  shop_level
                  shop_score
                  grade_name
                  image_badge_url
                  image_badge_background_mobile_url
                }
                current_benefit_list {
                  benefit_category
                  benefit_name
                  benefit_description
                  related_link_applink
                  seq_num
                  image_url
                }
                next_pm_grade {
                  shop_level
                  shop_score_min
                  shop_score_max
                  grade_name
                  image_badge_url
                  image_badge_background_mobile_url
                }
                next_benefit_list {
                  benefit_category
                  benefit_name
                  benefit_description
                  related_link_applink
                  seq_num
                  image_url
                }
                potential_pm_grade {
                  shop_level
                  shop_score
                  grade_name
                  image_badge_background_mobile_url
                  image_badge_url
                }
                potential_benefit_list {
                  benefit_name
                  benefit_category
                  benefit_description
                  related_link_applink
                  seq_num
                  image_url
                }
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