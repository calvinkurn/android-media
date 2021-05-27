package com.tokopedia.discovery2.repository.discoveryPage

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.DataResponse
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.data.discoverypageresponse.DiscoveryInjectCouponResponse
import com.tokopedia.discovery2.data.gqlraw.GQL_INJECT_DATA
import javax.inject.Inject

class DiscoveryInjectCouponGQLRepository @Inject constructor(): BaseRepository(){
    suspend fun sendInjectCouponData(): DiscoveryInjectCouponResponse.SetInjectCouponModel? {
        return getGQLData(GQL_INJECT_DATA,
                DiscoveryInjectCouponResponse::class.java, mapOf()).setInjectCouponData
    }
}