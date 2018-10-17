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
        val trackAffiliateClickCloudSource: TrackAffiliateClickCloudSource)
    : UseCase<Boolean>() {
    override fun createObservable(requestParams: RequestParams?): Observable<Boolean> {
        return trackAffiliateClickCloudSource.doTracking(requestParams!!)
    }
}