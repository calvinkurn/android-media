package com.tokopedia.discovery2.repository.banner

import com.tokopedia.discovery2.data.ComponentsItem

interface BannerRepository {
    suspend fun getBanner(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String, productComponentName: String?): ComponentsItem?
}