package com.tokopedia.discovery2.repository.merchantvoucher

import com.tokopedia.discovery2.data.ComponentsItem

interface MerchantVoucherRepository {
    suspend fun getMerchantVouchers(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String, productComponentName: String?): Pair<ArrayList<ComponentsItem>,String?>
}