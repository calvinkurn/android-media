package com.tokopedia.discovery2.repository.claimCoupon

import com.google.gson.reflect.TypeToken
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject


open class ClaimCouponRestRepository @Inject constructor() : BaseRepository(), IClaimCouponRepository {

    override suspend fun getClickCouponData(url: String): ArrayList<ComponentsItem> {
        val response = getRestData<DataResponse<DiscoveryResponse>>(url,
                object : TypeToken<DataResponse<DiscoveryResponse>>() {}.type,
                RequestType.GET,
                RequestParams.EMPTY.parameters)
        val categoryNavigationResponse = response
        val discoveryDataMapper = DiscoveryDataMapper()
        return discoveryDataMapper.mapListToComponentList(categoryNavigationResponse.data.component?.data, "claim_coupon_item", categoryNavigationResponse.data.component?.properties)
    }

}


