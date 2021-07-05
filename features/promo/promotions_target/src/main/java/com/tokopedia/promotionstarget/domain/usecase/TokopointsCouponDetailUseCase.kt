package com.tokopedia.promotionstarget.domain.usecase

import com.tokopedia.promotionstarget.data.HachikoCouponDetailsParams
import com.tokopedia.promotionstarget.data.coupon.TokopointsCouponDetailResponse
import com.tokopedia.promotionstarget.data.di.HACHIKO_COUPON_DETAIL
import com.tokopedia.promotionstarget.data.gql.GqlUseCaseWrapper
import javax.inject.Inject
import javax.inject.Named

class TokopointsCouponDetailUseCase @Inject constructor(@Named(HACHIKO_COUPON_DETAIL) val queryString: String, val gqlWrapper: GqlUseCaseWrapper) {
    private val PARAMS = HachikoCouponDetailsParams
    private val API_VERSION = "2.0.0"
    suspend fun getResponse(map: HashMap<String, Any>): TokopointsCouponDetailResponse {
        return gqlWrapper.getResponse(TokopointsCouponDetailResponse::class.java, queryString, map)
    }

    fun getQueryParams(code: String): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        variables[PARAMS.CODE] = code
        variables[PARAMS.API_VERSION] = API_VERSION
        return variables
    }
}