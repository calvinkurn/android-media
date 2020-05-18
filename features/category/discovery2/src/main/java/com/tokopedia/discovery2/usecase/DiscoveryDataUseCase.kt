package com.tokopedia.discovery2.usecase

import com.google.gson.reflect.TypeToken
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.discovery2.GenerateUrl
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.network.data.model.response.DataResponse

import com.tokopedia.usecase.RequestParams

import javax.inject.Inject

class DiscoveryDataUseCase @Inject constructor() {

    @Inject
    lateinit var repository: BaseRepository

    suspend fun getDiscoveryData(pageIdentifier: String): DiscoveryResponse {
        return (repository.getRestData(GenerateUrl.getUrl(pageIdentifier),
                object : TypeToken<DataResponse<DiscoveryResponse>>() {}.type,
                RequestType.GET)  as DataResponse<DiscoveryResponse>).data!!
    }

}
