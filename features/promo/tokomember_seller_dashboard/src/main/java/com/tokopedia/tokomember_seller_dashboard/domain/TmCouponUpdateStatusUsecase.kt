package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.model.TMUpdateStatusRequest
import com.tokopedia.tokomember_seller_dashboard.model.TmCouponUpdateResponse
import javax.inject.Inject

class TmCouponUpdateStatusUsecase@Inject constructor(
    graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<TmCouponUpdateResponse>(graphqlRepository) {

    fun updateCouponStatus(
        success: (TmCouponUpdateResponse) -> Unit,
        failure: (Throwable) -> Unit,
        voucherId: Int,
        voucherStatus: String,
        token: String
    ){
        setTypeClass(TmCouponUpdateResponse::class.java)
        setRequestParams(getRequestParams(voucherStatus, voucherId, token))
        setGraphqlQuery(QUERY_TM_COUPON_UPDATE_STATUS)
        execute(
            { success(it) },
            { failure(it) }
        )
    }

    private fun getRequestParams(voucherStatus: String,voucherId: Int, token: String): Map<String, Any> {
        val req = TMUpdateStatusRequest(voucher_status = voucherStatus, voucher_id = voucherId, token = token, source = "android")
        return mapOf(MERCHANT_UPDATE_STATUS_INPUT to req)
    }
}

const val MERCHANT_UPDATE_STATUS_INPUT = "merchantVoucherUpdateStatusData"
const val QUERY_TM_COUPON_UPDATE_STATUS = """
    mutation merchantPromotionUpdateStatusMV(${'$'}merchantVoucherUpdateStatusData: mvUpdateStatusRequest!) {
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