package com.tokopedia.promotionstarget.usecase

import com.tokopedia.promotionstarget.data.coupon.GetCouponDetailResponse
import com.tokopedia.promotionstarget.di.GET_COUPON_DETAIL
import com.tokopedia.promotionstarget.gql.GqlUseCaseWrapper
import javax.inject.Inject
import javax.inject.Named


class GetCouponDetailUseCase @Inject constructor(@Named(GET_COUPON_DETAIL) val catalogDetailQuery: String) {

    private val gqlWrapper = GqlUseCaseWrapper()
    private val queryBuilder = StringBuilder()

    companion object {
        const val REPLACE_TOKEN = "param_cId"
        const val START_TOKEN = "{"
        const val END_TOKEN = "}"
    }

    suspend fun getResponse(couponIdList: List<String>): GetCouponDetailResponse {
        queryBuilder.clear()
        queryBuilder.append(START_TOKEN)
        couponIdList.map {
            queryBuilder.append("id_${it}:")
            queryBuilder.append(prepareQueryForCouponDetail(it))
        }
        queryBuilder.append(END_TOKEN)
        return gqlWrapper.getResponse(GetCouponDetailResponse::class.java, queryBuilder.toString(), emptyMap())
    }

    private fun prepareQueryForCouponDetail(couponId: String): String {
        return catalogDetailQuery.replace(REPLACE_TOKEN, couponId)
    }
}