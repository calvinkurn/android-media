package com.tokopedia.chatbot.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.common.network.data.model.CacheType
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestCacheStrategy
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.usecase.RequestParams
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.reflect.Type
import javax.inject.Inject

private const val HEADER_REFERER_KEY = "Referer"
private const val HEADER_REFERER_VALUE = "tokopedia.com/help/inbox"
class ContactUsRepository @Inject constructor() : BaseRepository() {

    suspend fun <T : Any> postMultiRestData(url: String,
                                            typeOf: Type,
                                            queryMap: Map<String, Any> = RequestParams.EMPTY.parameters,
                                            cacheType: CacheType = CacheType.ALWAYS_CLOUD,
                                            body: Map<String, RequestBody>,
                                            context: Context
    ): T {
        try {
            val restRequest = RestRequest.Builder(url, typeOf)
                    .setRequestType(RequestType.POST_MULTIPART)
                    .setBody(body)
                    .setCacheStrategy(RestCacheStrategy.Builder(cacheType).build())
                    .build()

            restRepository.updateInterceptors(listOf(), context)
            return restRepository.getResponse(restRequest).getData()
        } catch (t: Throwable) {
            throw t
        }
    }
}