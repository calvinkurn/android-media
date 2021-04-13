package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.data.source.cloud.model.PMGradeBenefitInfoResponse
import com.tokopedia.gm.common.data.source.local.model.PMCurrentAndNextShopGradeUiModel
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

class GetPMCurrentAndNextShopGradeUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository,
        private val mapper: PMGradeBenefitInfoMapper
) : BaseGqlUseCase<PMCurrentAndNextShopGradeUiModel>() {

    override suspend fun executeOnBackground(): PMCurrentAndNextShopGradeUiModel {
        val gqlRequest = GraphqlRequest(QUERY, PMGradeBenefitInfoResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val errors: List<GraphqlError>? = gqlResponse.getError(PMGradeBenefitInfoResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<PMGradeBenefitInfoResponse>()
            val data = mapper.mapRemoteModelToUiModel(response.data)
            return PMCurrentAndNextShopGradeUiModel(
                    nextMonthlyRefreshDate = data.nextMonthlyRefreshDate,
                    nextQuarterlyCalibrationRefreshDate = data.nextQuarterlyCalibrationRefreshDate,
                    currentPMGrade = data.currentPMGrade,
                    currentPMBenefits = data.currentPMBenefits,
                    nextPMGrade = data.nextPMGrade,
                    nextPMBenefits = data.nextPMBenefits
            )
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
                  related_link_applink
                  seq_num
                }
                next_pm_grade {
                  shop_level
                  shop_score_min
                  grade_name
                  image_badge_url
                }
                next_benefit_list {
                  benefit_name
                  seq_num
                }
              }
            }
        """.trimIndent()
    }
}