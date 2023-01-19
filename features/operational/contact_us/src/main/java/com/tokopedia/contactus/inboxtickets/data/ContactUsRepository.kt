package com.tokopedia.contactus.inboxtickets.data

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.common.network.data.model.CacheType
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestCacheStrategy
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.usecase.RequestParams
import okhttp3.MultipartBody
import java.lang.reflect.Type
import javax.inject.Inject

private const val HEADER_REFERER_KEY = "Referer"
private const val HEADER_REFERER_VALUE = "tokopedia.com/help/inbox"
class ContactUsRepository @Inject constructor() : BaseRepository() {

    suspend fun <T : Any> postMultiRestData(
        url: String,
        typeOf: Type,
        queryMap: Map<String, Any> = RequestParams.EMPTY.parameters,
        cacheType: CacheType = CacheType.ALWAYS_CLOUD,
        body: MultipartBody.Part
    ): T {
        try {
            val restRequest = RestRequest.Builder(url, typeOf)
                .setRequestType(RequestType.POST_MULTIPART)
                .setQueryParams(queryMap)
                .setBody(body)
                .setHeaders(mapOf(HEADER_REFERER_KEY to HEADER_REFERER_VALUE))
                .setCacheStrategy(RestCacheStrategy.Builder(cacheType).build())
                .build()
            return restRepository.getResponse(restRequest).getData()
        } catch (t: Throwable) {
            throw t
        }
    }
}
