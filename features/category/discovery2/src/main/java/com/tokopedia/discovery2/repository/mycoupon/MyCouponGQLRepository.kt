package com.tokopedia.discovery2.repository.mycoupon

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.mycoupon.MyCouponResponse
import com.tokopedia.discovery2.data.mycoupon.MyCouponsRequest
import javax.inject.Inject

open class MyCouponGQLRepository @Inject constructor(val getGQLString: (Int) -> String) : BaseRepository(), MyCouponRepository {

    override suspend fun getCouponData(myCouponsRequest: MyCouponsRequest): MyCouponResponse {
        return getGQLData(getGQLString(R.raw.my_coupon_gql), MyCouponResponse::class.java, mapOf("input" to myCouponsRequest))
    }
}


