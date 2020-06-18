package com.tokopedia.discovery2.repository.quickcoupon

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.push.PushStatusResponse
import javax.inject.Inject


open class QuickCouponGQLRepository @Inject constructor(val getGQLString: (Int) -> String) : BaseRepository(), QuickCouponRepository {
    override suspend fun getQuickCouponDetailData(pageIdentifier: String) {
        return getGQLData(getGQLString(R.raw.quick_coupon_gql),
                PushStatusResponse::class.java, mapOf("discovery_page" to pageIdentifier))
    }

}


