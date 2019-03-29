package com.tokopedia.profile.domain.usecase

import com.tokopedia.profile.data.source.TrackAffiliateClickCloudSource
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by milhamj on 10/17/18.
 */
class TrackAffiliateClickUseCase @Inject constructor(
        private val trackAffiliateClickCloudSource: TrackAffiliateClickCloudSource)
    : UseCase<Boolean>() {
    override fun createObservable(requestParams: RequestParams?): Observable<Boolean> {
        return trackAffiliateClickCloudSource.doTrackingWithUrl(requestParams!!)
    }

    companion object {
        private const val PARAM_UNIQUE_STRING = "ustring"
        private const val PARAM_TRACKING_ID = "tracker_id"
        private const val PARAM_USER_ID = "user_id"
        private const val PARAM_EVENT = "event"
        private const val PARAM_URL = "url"

        private const val EVENT_AFFILITE = "affiliate"

        fun createRequestParams(uniqueTrackingId: String, deviceId: String, userId: String)
                : RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_UNIQUE_STRING, uniqueTrackingId)
            requestParams.putString(PARAM_TRACKING_ID, deviceId)
            requestParams.putString(PARAM_USER_ID, userId)
            requestParams.putString(PARAM_EVENT, EVENT_AFFILITE)
            return requestParams
        }

        fun createRequestParams(url: String)
                : RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_URL, url)
            return requestParams
        }
    }
}