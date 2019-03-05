package com.tokopedia.profile.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.profile.R
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * @author by yfsx on 15/02/19.
 */
class GetDynamicFeedProfileUseCase
@Inject constructor( @ApplicationContext val context: Context,
                     val getDynamicFeedUseCase: GetDynamicFeedUseCase)
    : UseCase<DynamicFeedDomainModel>() {

    override fun createObservable(requestParams: RequestParams?): Observable<DynamicFeedDomainModel> {
        getDynamicFeedUseCase.setQuery(R.raw.query_feed_profile_dynamic)
        return getDynamicFeedUseCase.createObservable(requestParams).subscribeOn(Schedulers.io())
    }


    companion object {
        val SOURCE_ID = "sourceID"
        fun createRequestParams(userId: String, targetUserId: String, cursor: String): RequestParams {
            val requestParams = GetDynamicFeedUseCase.createRequestParams(userId, cursor, GetDynamicFeedUseCase.SOURCE_PROFILE)
            requestParams.putInt(GetProfileHeaderUseCase.PARAM_USER_ID_TARGET, targetUserId)
            requestParams.putInt(SOURCE_ID, targetUserId)
            return requestParams
        }
    }
}