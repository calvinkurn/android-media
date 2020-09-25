package com.tokopedia.promotionstarget.domain.usecase

import com.google.gson.Gson
import com.tokopedia.promotionstarget.data.AutoApplyParams
import com.tokopedia.promotionstarget.data.coupon.TokopointsCouponDetailResponse
import com.tokopedia.promotionstarget.data.di.HACHIKO_COUPON_DETAIL
import com.tokopedia.promotionstarget.data.gql.GqlUseCaseWrapper
import com.tokopedia.promotionstarget.fake.FakeResponse
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Named

class TokopointsCouponDetailUseCase @Inject constructor(@Named(HACHIKO_COUPON_DETAIL) val queryString: String, val gqlWrapper: GqlUseCaseWrapper) {
    private val PARAMS = AutoApplyParams

    suspend fun getResponse(map: HashMap<String, Any>): TokopointsCouponDetailResponse {
        return gqlWrapper.getResponse(TokopointsCouponDetailResponse::class.java, queryString, map)
    }

    suspend fun getFakeResponse(map: HashMap<String, Any>): TokopointsCouponDetailResponse {
        val gson = Gson()
        val response = FakeResponse.HACHIKO_COUPON_DETAIL
        val json = JSONObject(response)
        return gson.fromJson(json.getJSONObject("data").toString(), TokopointsCouponDetailResponse::class.java)
    }

    fun getQueryParams(code: String): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        return variables
    }
}