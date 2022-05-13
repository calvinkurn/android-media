package com.tokopedia.discovery2.repository.bannerinfinite

import com.tokopedia.discovery2.data.ComponentsItem

interface BannerInfiniteRepository {
    suspend fun getBanners(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String, bannerComponentName: String?): Pair<ArrayList<ComponentsItem>,String?>
}