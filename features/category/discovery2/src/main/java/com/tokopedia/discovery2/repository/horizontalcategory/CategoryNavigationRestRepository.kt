package com.tokopedia.discovery2.repository.horizontalcategory

import com.google.gson.reflect.TypeToken
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.categorynavigationresponse.CategoryNavigationResponse
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject


open class CategoryNavigationRestRepository @Inject constructor() : BaseRepository(), CategoryNavigationRepository {


    override suspend fun getCategoryNavigationData(categoryDetailUrl: String): ArrayList<ComponentsItem> {
        val response = getRestData<DataResponse<CategoryNavigationResponse>>(categoryDetailUrl,
                object : TypeToken<DataResponse<CategoryNavigationResponse>>() {}.type,
                RequestType.GET,
                RequestParams.EMPTY.parameters)
        val categoryNavigationResponse = response
        val data = categoryNavigationResponse.data
        val discoveryDataMapper = DiscoveryDataMapper()
        return discoveryDataMapper.mapListToComponentList(data?.child)

    }
}


