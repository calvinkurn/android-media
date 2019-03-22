package com.tokopedia.profile.data.source

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.profile.data.network.TopAdsApi
import com.tokopedia.profile.data.pojo.trackaffiliate.TrackAffiliatePojo
import com.tokopedia.usecase.RequestParams
import org.json.JSONException
import rx.Observable
import javax.inject.Inject

/**
 * @author by milhamj on 10/17/18.
 */
class TrackAffiliateClickCloudSource @Inject constructor(
        private val topAdsApi: TopAdsApi,
        private val gson: Gson) {

    fun doTrackingWithUrl(requestParams: RequestParams): Observable<Boolean> {
        return topAdsApi.trackWithUrl(requestParams.getString(PARAM_URL, ""), requestParams.parameters).map {
            if (it.isSuccessful) {
                if (it.body() != null && it.body() != null) {
                    try {
                        val type = object : TypeToken<TrackAffiliatePojo>() {}.type
                        gson.fromJson<TrackAffiliatePojo>(it.body(), type).success
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        false
                    }
                } else {
                    throw RuntimeException(EMPTY_BODY)
                }
            } else {
                throw RuntimeException(NETWORK_ERROR)
            }
        }
    }

    companion object {
        const val EMPTY_BODY = "Response has empty body"
        const val NETWORK_ERROR = "Network error"
        const val PARAM_URL = "url"
    }
}
