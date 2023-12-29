package com.tokopedia.scp_rewards_touchpoints.bottomsheet.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.model.CouponAutoApplyResponseModel
import javax.inject.Inject

@GqlQuery("ScpRewardsCouponAutoApply", SCP_REWARDS_COUPON_APPLY_QUERY)
class CouponAutoApplyUseCase @Inject constructor() : GraphqlUseCase<CouponAutoApplyResponseModel>() {

    suspend fun applyCoupon(shopID: Int?, code: String): CouponAutoApplyResponseModel {
        setTypeClass(CouponAutoApplyResponseModel::class.java)
        setGraphqlQuery(ScpRewardsCouponAutoApply())
        setRequestParams(getRequestParams(shopID, code))
        return executeOnBackground()
    }

    companion object {
        private const val SHOP_ID_KEY = "shopID"
        private const val CODE_KEY = "code"
    }

    private fun getRequestParams(shopID: Int?, code: String) = mapOf(
        SHOP_ID_KEY to shopID,
        CODE_KEY to code
    )
}

private const val SCP_REWARDS_COUPON_APPLY_QUERY = """
    mutation scpRewardsCouponAutoApply(${'$'}shopID:Int, ${'$'}code:String!) {
        scpRewardsCouponAutoApply(input: {shopID: ${'$'}shopID, code: ${'$'}code}) {
            resultStatus {
                code
                status
            }
            couponAutoApply {
                isSuccess
                infoMessage {
                    title
                    subtitle
                }
            }
        }
    }
"""
