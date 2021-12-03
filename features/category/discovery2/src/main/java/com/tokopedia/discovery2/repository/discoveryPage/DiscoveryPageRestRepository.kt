package com.tokopedia.discovery2.repository.discoveryPage


import com.google.gson.reflect.TypeToken
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.discovery2.GenerateUrl
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.network.data.model.response.DataResponse
import javax.inject.Inject


class DiscoveryPageRestRepository @Inject constructor() : BaseRepository(), DiscoveryPageRepository {

    override suspend fun getDiscoveryPageData(pageIdentifier: String,extraParams:Map<String,Any>?): DiscoveryResponse {
        return (getRestData(GenerateUrl.getUrl(pageIdentifier),
                object : TypeToken<DataResponse<DiscoveryResponse>>() {}.type,
                RequestType.GET) as DataResponse<DiscoveryResponse>).data
    }
}