package com.tokopedia.discovery2.repository.tokopoints

import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.discovery2.GenerateUrl
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.tradein_common.repository.BaseRepository
import javax.inject.Inject

class TokopointsRestRepository @Inject constructor() : BaseRepository(), TokopointsRepository {

    override suspend fun getTokopointsData(componentId: Int, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String): ArrayList<ComponentsItem> {

        val response = getRestData(GenerateUrl.getComponentUrl(pageEndPoint, componentId),
                object : TypeToken<DataResponse<DiscoveryResponse>>() {}.type,
                RequestType.GET,
                queryParamterMap)
        val discoveryResponse = response?.getData() as DataResponse<DiscoveryResponse>


        return DiscoveryDataMapper.mapListToComponentList(discoveryResponse.data.component?.data!! , "tokopoints_item")
    }
}