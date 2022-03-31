package com.tokopedia.discovery2.repository.banner

import com.tokopedia.discovery2.data.DataItem

interface BannerRepository {
    suspend fun getBanner(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String, productComponentName: String?): MutableList<DataItem>
}