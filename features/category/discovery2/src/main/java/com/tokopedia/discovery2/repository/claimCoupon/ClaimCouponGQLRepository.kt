package com.tokopedia.discovery2.repository.claimCoupon

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.claimcoupon.RedeemCouponResponse
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT_QUERY_NAME
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import javax.inject.Inject


open class ClaimCouponGQLRepository @Inject constructor(val getGQLString: (Int) -> String) : BaseRepository(), IClaimCouponGqlRepository {

    override suspend fun redeemCoupon(mapOf: Map<String, Any>): RedeemCouponResponse {
        val redeemCouponResponse = getGQLData(getGQLString(R.raw.mutation_redeem_coupon_gql),
                RedeemCouponResponse::class.java, mapOf) as RedeemCouponResponse
        return redeemCouponResponse
    }

    override suspend fun getClickCouponData(componentId: String, pageEndPoint: String): ArrayList<ComponentsItem> {
        val response = (getGQLData(GQL_COMPONENT,
                com.tokopedia.discovery2.data.DataResponse::class.java, Utils.getComponentsGQLParams(componentId, pageEndPoint, ""), GQL_COMPONENT_QUERY_NAME) as com.tokopedia.discovery2.data.DataResponse)
        return DiscoveryDataMapper().mapListToComponentList(response.data.component?.data, "claim_coupon_item", response.data.component?.properties)
    }
}


