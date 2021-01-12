package com.tokopedia.gamification.giftbox.domain

import com.google.gson.Gson
import com.tokopedia.gamification.FAKE_COUPON_RESPONSE
import com.tokopedia.gamification.giftbox.data.di.GET_COUPON_DETAIL
import com.tokopedia.gamification.giftbox.data.entities.CouponDetailResponse
import com.tokopedia.gamification.giftbox.data.entities.GetCouponDetail
import com.tokopedia.gamification.pdp.data.GqlUseCaseWrapper
import javax.inject.Inject
import javax.inject.Named

class CouponDetailUseCase @Inject constructor(@Named(GET_COUPON_DETAIL) val catalogDetailQuery: String, val gqlWrapper: GqlUseCaseWrapper) {

    private val queryBuilder = StringBuilder()

    companion object {
        const val REPLACE_TOKEN = "param_cId"
        const val START_TOKEN = "{"
        const val END_TOKEN = "}"
    }

    suspend fun getResponse(couponIdList: List<String>): CouponDetailResponse {
        return getFakeResponse()
//        queryBuilder.clear()
//        queryBuilder.append(START_TOKEN)
//        couponIdList.map {
//            queryBuilder.append("id_${it}:")
//            queryBuilder.append(prepareQueryForCouponDetail(it))
//        }
//        queryBuilder.append(END_TOKEN)
//        val map = gqlWrapper.getResponse(Map::class.java, queryBuilder.toString(), emptyMap())
//        val gson = Gson()
//        val json = gson.toJsonTree(map).asJsonObject
//        val couponMap = HashMap<String,GetCouponDetail>()
//        json.entrySet().iterator().forEach {
//            couponMap[it.key] = (gson.fromJson(it.value, GetCouponDetail::class.java))
//        }
//        return CouponDetailResponse(couponMap)
    }

    private fun prepareQueryForCouponDetail(couponId: String): String {
        return catalogDetailQuery.replace(REPLACE_TOKEN, couponId)
    }

    fun getFakeResponse(): CouponDetailResponse {
        val response = Gson().fromJson<GetCouponDetail>(FAKE_COUPON_RESPONSE, GetCouponDetail::class.java)
        val map = HashMap<String, GetCouponDetail>()
        map["1"] = response
        return CouponDetailResponse(map)
    }
}