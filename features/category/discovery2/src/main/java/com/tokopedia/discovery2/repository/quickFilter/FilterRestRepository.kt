package com.tokopedia.discovery2.repository.quickFilter

import com.google.gson.reflect.TypeToken
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.discovery2.GenerateUrl
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.network.data.model.response.DataResponse
import javax.inject.Inject

class FilterRestRepository @Inject constructor() : BaseRepository(), FilterRepository  {

    override suspend fun getFilterData(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String): DynamicFilterModel? {

        val response = getRestData<DataResponse<DiscoveryResponse>>(GenerateUrl.getComponentUrl(pageEndPoint, componentId),
                object : TypeToken<DataResponse<DiscoveryResponse>>() {}.type,
                RequestType.GET,
                queryParamterMap)
        val discoveryDataMapper = DiscoveryDataMapper()

        return discoveryDataMapper.mapFiltersToDynamicFilterModel(response.data.component?.data?.get(0))
    }
}