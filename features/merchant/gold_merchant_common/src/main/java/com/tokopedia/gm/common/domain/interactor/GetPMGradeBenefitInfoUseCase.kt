package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.data.source.cloud.model.PMGradeBenefitInfoResponse
import com.tokopedia.gm.common.data.source.local.model.PMGradeBenefitInfoUiModel
import com.tokopedia.gm.common.domain.mapper.PMGradeBenefitInfoMapper
import com.tokopedia.gql_query_annotation.GqlQuery
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

@GqlQuery("GetPMGradeBenefitInfoGqlQuery", GetPMGradeBenefitInfoUseCase.QUERY)
class GetPMGradeBenefitInfoUseCase @Inject constructor(
    private val gqlRepository: GraphqlRepository,
    private val mapper: PMGradeBenefitInfoMapper
) : BaseGqlUseCase<PMGradeBenefitInfoUiModel>() {

    override suspend fun executeOnBackground(): PMGradeBenefitInfoUiModel {
        val gqlRequest = GraphqlRequest(
            GetPMGradeBenefitInfoGqlQuery(),
            PMGradeBenefitInfoResponse::class.java,
            params.parameters
        )
        val gqlResponse = gqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val errors: List<GraphqlError>? =
            gqlResponse.getError(PMGradeBenefitInfoResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<PMGradeBenefitInfoResponse>()
            return mapper.mapRemoteModelToUiModel(response.data)
        } else {
            throw MessageErrorException(errors.joinToString(" - ") { it.message })
        }
    }

    companion object {
        internal const val QUERY = """
            query goldGetPMGradeBenefitInfo(${'$'}shop_id: Int!, ${'$'}source: String!, ${'$'}fields: [String]) {
              goldGetPMGradeBenefitInfo(shop_id: ${'$'}shop_id, source: ${'$'}source, fields: ${'$'}fields) {
                next_monthly_refresh_date
                next_quarterly_calibration_refresh_date
                current_pm_grade {
                  shop_level
                  grade_name
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
                }
                next_benefit_list {
                  benefit_name
                  seq_num
                }
              }
            }
        """
        const val FIELD_CURRENT_PM_GRADE = "current_pm_grade"
        const val FIELD_CURRENT_BENEFIT_LIST = "current_benefit_list"
        const val FIELD_NEXT_PM_GRADE = "next_pm_grade"
        const val FIELD_NEXT_BENEFIT_LIST = "next_benefit_list"

        private const val KEY_SHOP_ID = "shop_id"
        private const val KEY_SOURCE = "source"
        private const val KEY_FIELDS = "fields"

        fun createParams(shopId: String, source: String, fields: List<String>): RequestParams {
            return RequestParams.create().apply {
                putLong(KEY_SHOP_ID, shopId.toLongOrZero())
                putString(KEY_SOURCE, source)
                putObject(KEY_FIELDS, fields)
            }
        }
    }
}