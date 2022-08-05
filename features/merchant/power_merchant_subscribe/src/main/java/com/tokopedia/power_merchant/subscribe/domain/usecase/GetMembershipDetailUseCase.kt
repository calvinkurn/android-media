package com.tokopedia.power_merchant.subscribe.domain.usecase

import com.tokopedia.gm.common.domain.interactor.BaseGqlUseCase
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.power_merchant.subscribe.domain.mapper.MembershipDetailMapper
import com.tokopedia.power_merchant.subscribe.domain.model.MembershipDetailResponse
import com.tokopedia.power_merchant.subscribe.view.model.MembershipDetailUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("GetMembershipDetailGqlQuery", GetMembershipDetailUseCase.QUERY)
class GetMembershipDetailUseCase @Inject constructor(
    private val gqlRepository: GraphqlRepository,
    private val membershipDetailMapper: MembershipDetailMapper
) : BaseGqlUseCase<MembershipDetailUiModel>() {

    override suspend fun executeOnBackground(): MembershipDetailUiModel {
        val gqlRequest = GraphqlRequest(
            GetMembershipDetailGqlQuery(),
            MembershipDetailResponse::class.java, params.parameters
        )
        val gqlResponse = gqlRepository.response(listOf(gqlRequest))

        val gqlError = gqlResponse.getError(MembershipDetailResponse::class.java)
        if (gqlError.isNullOrEmpty()) {
            val membershipDetailResponse: MembershipDetailResponse =
                gqlResponse.getData(MembershipDetailResponse::class.java)
            return membershipDetailMapper.mapToUiModel(membershipDetailResponse)
        } else {
            throw MessageErrorException(gqlError.firstOrNull()?.message.orEmpty())
        }
    }

    fun setParams(shopId: Long) {
        params = RequestParams.create().apply {
            putLong(SHOP_ID_PARAM, shopId)
            putString(SHOP_ID_STR_PARAM, shopId.toString())
        }
    }

    companion object {
        const val QUERY = """
            query MembershipDetail(${'$'}shopId: Int!, ${'$'}shopIdStr: String!) {
              shopLevel(input: {shopID: "${'$'}shopId", source: "android", lang: "id"}) {
                result {
                  period
                  itemSold
                  niv
                }
                error {
                  message
                }
              }
              goldGetPMGradeBenefitInfo(shop_id: ${'$'}shopId, source: "android-goldmerchant", lang: "id", device: "android", fields: ["current_pm_grade", "next_level_benefit_package_list"]) {
                next_monthly_refresh_date
                next_quarterly_calibration_refresh_date
                current_pm_grade {
                  grade_name
                }
              }
              goldGetPMShopInfo(shop_id: ${'$'}shopId, source: "power-merchant-subscription-android-ui", filter: {including_pm_pro_eligibility: true}) {
                item_sold_one_month
                niv_one_month
              }
              shopScoreLevel(input: {shopID: ${'$'}shopIdStr, source: "power-merchant-subscription-android-ui", calculateScore: true, getNextMinValue: false}) {
                result {
                  shopScore
                }
              }
            }
        """
        private const val SHOP_ID_PARAM = "shopId"
        private const val SHOP_ID_STR_PARAM = "shopIdStr"
    }
}