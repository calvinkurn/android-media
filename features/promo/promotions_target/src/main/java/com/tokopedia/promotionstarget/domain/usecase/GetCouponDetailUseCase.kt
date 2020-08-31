package com.tokopedia.promotionstarget.domain.usecase

import com.google.gson.Gson
import com.tokopedia.promotionstarget.data.coupon.GetCouponDetail
import com.tokopedia.promotionstarget.data.coupon.GetCouponDetailResponse
import com.tokopedia.promotionstarget.data.di.GET_COUPON_DETAIL
import com.tokopedia.promotionstarget.data.gql.GqlUseCaseWrapper
import javax.inject.Inject
import javax.inject.Named


class GetCouponDetailUseCase @Inject constructor(@Named(GET_COUPON_DETAIL) val catalogDetailQuery: String,val gqlWrapper:GqlUseCaseWrapper) {

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
        val map = gqlWrapper.getResponse(Map::class.java, queryBuilder.toString(), emptyMap())
        val gson = Gson()
        val json = gson.toJsonTree(map).asJsonObject
        val couponList = ArrayList<GetCouponDetail>()
        json.entrySet().iterator().forEach {
            couponList.add(gson.fromJson(it.value, GetCouponDetail::class.java))
        }
        return GetCouponDetailResponse(couponList)
    }

    private fun prepareQueryForCouponDetail(couponId: String): String {
        return catalogDetailQuery.replace(REPLACE_TOKEN, couponId)
    }
}