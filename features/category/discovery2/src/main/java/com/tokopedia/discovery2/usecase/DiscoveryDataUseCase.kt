package com.tokopedia.discovery2.usecase

import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.discovery2.GenerateUrl
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.tradein_common.repository.BaseRepository
import com.tokopedia.usecase.RequestParams

class DiscoveryDataUseCase {

    suspend fun getDiscoveryData(repository: BaseRepository, pageIdentifier: String):DiscoveryResponse{
        val response = repository.getRestData(GenerateUrl.getUrl(pageIdentifier),
                object : TypeToken<DataResponse<DiscoveryResponse>>() {}.type,
                RequestType.GET,
                RequestParams.EMPTY.parameters)
        val discoveryResponse = response?.getData() as DataResponse<DiscoveryResponse>
        return discoveryResponse.data
    }
}
