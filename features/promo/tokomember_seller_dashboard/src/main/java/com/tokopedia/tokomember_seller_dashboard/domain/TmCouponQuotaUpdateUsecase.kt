package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.model.TMUpdateQuotaRequest
import com.tokopedia.tokomember_seller_dashboard.model.TmUpdateCouponQuotaDataExt
import javax.inject.Inject

class TmCouponQuotaUpdateUsecase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<TmUpdateCouponQuotaDataExt>(graphqlRepository) {

    fun updateCouponQuota(
        success: (TmUpdateCouponQuotaDataExt) -> Unit,
        failure : (Throwable) -> Unit,
        quota: Int,
        voucherId: Int,
        token: String
    ){
        setTypeClass(TmUpdateCouponQuotaDataExt::class.java)
        setRequestParams(getRequestParams(quota, voucherId, token))
        setGraphqlQuery(QUERY_TM_COUPON_UPDATE_QUOTA)
        execute(
            { success(it) },
            { failure(it) }
        )
    }

    private fun getRequestParams(quota: Int, voucherId: Int, token: String): Map<String, Any> {
        val req = TMUpdateQuotaRequest(quota = quota, voucher_id = voucherId, token = token, source = "android")
        return mapOf(MERCHANT_UPDATE_INPUT to req)
    }
}

const val MERCHANT_UPDATE_INPUT= "merchantVoucherUpdateQuotaData"

const val QUERY_TM_COUPON_UPDATE_QUOTA = """
    mutation merchantPromotionUpdateMVQuota(${'$'}merchantVoucherUpdateQuotaData: mvUpdateQuotaData!) {
        merchantPromotionUpdateMVQuota(merchantVoucherUpdateQuotaData: ${'$'}merchantVoucherUpdateQuotaData) {
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