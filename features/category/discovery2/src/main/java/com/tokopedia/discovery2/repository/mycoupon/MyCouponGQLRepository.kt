package com.tokopedia.discovery2.repository.mycoupon

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT_QUERY_NAME
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import javax.inject.Inject

open class MyCouponGQLRepository @Inject constructor() : BaseRepository(), MyCouponRepository {

    override suspend fun getCouponData(
            componentId: String,
            queryParamterMap: MutableMap<String, Any>,
            pageEndPoint: String): ArrayList<ComponentsItem> {
        val response = (getGQLData(GQL_COMPONENT,
                com.tokopedia.discovery2.data.DataResponse::class.java, Utils.getComponentsGQLParams(componentId, pageEndPoint, Utils.getQueryString(queryParamterMap)), GQL_COMPONENT_QUERY_NAME) as com.tokopedia.discovery2.data.DataResponse)
        return DiscoveryDataMapper().mapListToComponentList(response.data.component?.data, "claim_coupon_item", response.data.component?.properties)
    }
}


