package com.tokopedia.kol.feature.video.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * @author by yfsx on 26/03/19.
 */
class GetVideoDetailUseCase
@Inject constructor(@ApplicationContext private val context: Context,
                    private val getDynamicFeedUseCase: GetDynamicFeedUseCase)
    :UseCase<DynamicFeedDomainModel>(){

    override fun createObservable(requestParams: RequestParams?): Observable<DynamicFeedDomainModel> {
        return getDynamicFeedUseCase.createObservable(requestParams).subscribeOn(Schedulers.io())
    }

    companion object {
        val DETAIL_ID = "detailID"
        fun createRequestParams(userId: String, detailId: String): RequestParams {
            val requestParams = GetDynamicFeedUseCase.createRequestParams(userId, "", GetDynamicFeedUseCase.SOURCE_DETAIL)
            requestParams.putString(DETAIL_ID, detailId)
            return requestParams
        }
    }
}