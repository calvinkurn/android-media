package com.tokopedia.discovery2.repository.discoveryPage

import com.google.gson.reflect.TypeToken
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.discovery2.GenerateUrl
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataResponse
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

//TODO change version name and from DataName
class DiscoveryDataGQLRepository @Inject constructor(val getGQLString: (Int) -> String) : BaseRepository(), DiscoveryPageRepository {
    lateinit var userSession: UserSession
    override suspend fun getDiscoveryPageData(pageIdentifier: String): DiscoveryResponse {
        DiscoveryPageRepository.discoveryResponseData =  (getGQLData(getGQLString(R.raw.query_discovery_data),
                DataResponse::class.java, mapOf("identifier" to pageIdentifier, "version" to "3.78", "device" to "Android"), "discoveryPageInfo") as DataResponse).data
       return DiscoveryPageRepository.discoveryResponseData

    }
}




