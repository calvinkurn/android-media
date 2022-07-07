package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.data.source.cloud.model.GMShopInfoResponse
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.gm.common.domain.mapper.PMShopInfoMapper
import com.tokopedia.gql_query_annotation.GqlQuery
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

@GqlQuery("GetPMShopInfoGqlQuery", GetPMShopInfoUseCase.QUERY)
class GetPMShopInfoUseCase @Inject constructor(
    private val gqlRepository: GraphqlRepository,
    private val mapper: PMShopInfoMapper
) : BaseGqlUseCase<PMShopInfoUiModel>() {

    override suspend fun executeOnBackground(): PMShopInfoUiModel {
        val gqlRequest = GraphqlRequest(
            GetPMShopInfoGqlQuery(), GMShopInfoResponse::class.java, params.parameters
        )
        val gqlResponse = gqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val errors: List<GraphqlError>? = gqlResponse.getError(GMShopInfoResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<GMShopInfoResponse>()
            return mapper.mapRemoteModelToUiModel(response)
        } else {
            throw MessageErrorException(errors.joinToString(" - ") { it.message })
        }
    }

    companion object {
        internal const val QUERY = """
          query goldGetPMShopInfo(${'$'}shop_id: Int!, ${'$'}source: String!, ${'$'}filter: GetPMShopInfoFilter) {
            goldGetPMShopInfo(shop_id: ${'$'}shop_id, source: ${'$'}source, filter: ${'$'}filter) {
              is_new_seller
              is_30_days_first_monday
              is_kyc
              shop_age
              shop_level
              kyc_status_id
              shop_score_threshold
              shop_score_pm_pro_threshold
              is_has_active_product
              is_eligible_shop_score
              is_eligible_pm
              is_eligible_pm_pro
              item_sold_one_month
              item_sold_pm_pro_threshold
              niv_one_month
              niv_pm_pro_threshold
            }
            shopInfoByID(input: {
                shopIDs: [${'$'}shop_id]
                fields: ["create_info"]
                domain: ""
                source: "sellerapp"
            }) {
                result{
                  createInfo{
                    shopCreated
                  }
                }
                error{
                  message
                }
            }
            goldGetPMGradeBenefitInfo(shop_id: ${'$'}shop_id, source: "android-goldmerchant", lang: "id", device: "android", fields: ["current_pm_grade"]) {
                next_monthly_refresh_date
            }
          }
        """
        private const val KEY_SHOP_ID = "shop_id"
        private const val KEY_SOURCE = "source"
        private const val KEY_FILTER = "filter"
        private const val KEY_INCLUDING_PM_PRO_ELIGIBILITY = "including_pm_pro_eligibility"

        fun createParams(shopId: String, source: String): RequestParams {
            val filter: Map<String, Boolean> = mapOf(
                KEY_INCLUDING_PM_PRO_ELIGIBILITY to true
            )
            return RequestParams.create().apply {
                putLong(KEY_SHOP_ID, shopId.toLongOrZero())
                putString(KEY_SOURCE, source)
                putObject(KEY_FILTER, filter)
            }
        }
    }
}