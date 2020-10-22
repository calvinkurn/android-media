package com.tokopedia.discovery2.repository.tokopoints

import com.google.gson.reflect.TypeToken
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.discovery2.GenerateUrl
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.network.data.model.response.DataResponse
import javax.inject.Inject

class TokopointsRestRepository @Inject constructor() : BaseRepository(), TokopointsRepository {

    override suspend fun getTokopointsData(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String): ArrayList<ComponentsItem> {

        val response = getRestData<DataResponse<DiscoveryResponse>>(GenerateUrl.getComponentUrl(pageEndPoint, componentId),
                object : TypeToken<DataResponse<DiscoveryResponse>>() {}.type,
                RequestType.GET,
                queryParamterMap)
        val discoveryResponse = response

        val discoveryDataMapper = DiscoveryDataMapper()

        return discoveryDataMapper.mapListToComponentList(discoveryResponse.data.component?.data, "tokopoints_item", null)
    }
}