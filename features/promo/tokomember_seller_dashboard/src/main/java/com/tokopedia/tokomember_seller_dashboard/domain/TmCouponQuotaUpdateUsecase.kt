package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.model.TMUpdateQuotaRequest
import com.tokopedia.tokomember_seller_dashboard.model.TmUpdateCouponQuotaResponse
import javax.inject.Inject

class TmCouponQuotaUpdateUsecase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<TmUpdateCouponQuotaResponse>(graphqlRepository) {

    fun updateCouponQuota(
        success: (TmUpdateCouponQuotaResponse) -> Unit,
        failure : (Throwable) -> Unit,
        quota: Int,
        voucherId: Int,
        token: String
    ){
        setTypeClass(TmUpdateCouponQuotaResponse::class.java)
        setRequestParams(getRequestParams(quota, voucherId, token))
        setGraphqlQuery(QUERY_TM_COUPON_UPDATE_QUOTA)
        execute(
            { success(it) },
            { failure(it) }
        )
    }

    private fun getRequestParams(quota: Int, voucherId: Int, token: String): Map<String, Any> {
        val req = TMUpdateQuotaRequest(quota, voucherId, token, source = "android")
        return mapOf("merchantVoucherUpdateStatusData" to req)
    }
}

const val QUERY_TM_COUPON_UPDATE_QUOTA = """
    mutation merchantPromotionUpdateStatusMV(${'$'}merchantVoucherUpdateStatusData: mvUpdateStatusRequest) {
        merchantPromotionUpdateStatusMV(merchantVoucherUpdateStatusData: ${'$'}merchantVoucherUpdateStatusData) {
            status
            message
            process_time
            data {
              redirect_url
              voucher_id
              status
            }
        }
    }
"""