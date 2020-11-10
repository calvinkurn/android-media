package com.tokopedia.discovery2.repository.chipfilter

import com.google.gson.reflect.TypeToken
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.GenerateUrl
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.network.data.model.response.DataResponse
import javax.inject.Inject

class ChipFilterRestRepository @Inject constructor() : BaseRepository(), ChipFilterRepository {
    override suspend fun getChipFilterData(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String, position : Int, componentName : String?): ArrayList<ComponentsItem> {
        val response = getRestData<DataResponse<DiscoveryResponse>>(GenerateUrl.getComponentUrl(pageEndPoint, componentId),
                object : TypeToken<DataResponse<DiscoveryResponse>>() {}.type,
                RequestType.GET,
                queryParamterMap)
        response.data.component?.data?.let {
            return DiscoveryDataMapper.mapListToComponentList(itemList = it, subComponentName = ComponentNames.ChipsFilterItem.componentName, parentComponentName = componentName, position = position)
        }
        return arrayListOf()
    }
}