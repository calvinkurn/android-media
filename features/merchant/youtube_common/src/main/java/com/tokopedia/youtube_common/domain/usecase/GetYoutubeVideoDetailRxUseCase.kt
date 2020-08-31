package com.tokopedia.youtube_common.domain.usecase

import android.content.Context
import com.tokopedia.common.network.data.ObservableFactory
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.youtube_common.YoutubeCommonConstant.ENDPOINT_URL
import com.tokopedia.youtube_common.YoutubeCommonConstant.ID_KEY
import com.tokopedia.youtube_common.YoutubeCommonConstant.PART_KEY
import com.tokopedia.youtube_common.YoutubeCommonConstant.PART_VALUE
import com.tokopedia.youtube_common.data.model.YoutubeVideoDetailModel
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import rx.Observable
import java.lang.reflect.Type
import java.util.*
import javax.inject.Inject

class GetYoutubeVideoDetailRxUseCase @Inject constructor(
        private val context: Context
) : UseCase<Map<Type, RestResponse>>() {

    companion object {

        fun generateRequestParam(videoId: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(ID_KEY, videoId)
            return requestParams
        }

    }

    override fun createObservable(requestParams: RequestParams): Observable<Map<Type, RestResponse>> {
        val interceptorArrayList = ArrayList<Interceptor>()
        interceptorArrayList.add(HttpLoggingInterceptor())
        val listRequest: MutableList<RestRequest> = ArrayList()
        val request = RestRequest.Builder(ENDPOINT_URL, YoutubeVideoDetailModel::class.java)
                .setQueryParams(generateQueryParam(requestParams))
                .setRequestType(RequestType.GET)
                .build()
        listRequest.add(request)
        return ObservableFactory.create(
                listRequest,
                interceptorArrayList,
                context)
    }

    private fun generateQueryParam(requestParams: RequestParams): Map<String, Any> {
        val requestParamMap = mutableMapOf<String, Any>()
        requestParamMap[ID_KEY] = getVideoId(requestParams)
        requestParamMap[PART_KEY] = PART_VALUE
        return requestParamMap
    }

    private fun getVideoId(requestParams: RequestParams): String {
        return requestParams.getString(ID_KEY, "")
    }
}