package com.tokopedia.feedcomponent.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.profileheader.ProfileHeaderData
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import rx.Observable
import javax.inject.Inject

/**
 * @author by milhamj on 9/21/18.
 */
class GetProfileHeaderUseCase @Inject constructor(@ApplicationContext val context: Context)
    : GraphqlUseCase() {
    override fun createObservable(requestParams: RequestParams?): Observable<GraphqlResponse> {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.query_af_profile_header)
        val request = GraphqlRequest(
                query,
                ProfileHeaderData::class.java,
                requestParams?.parameters
        )

        this.clearRequest()
        this.addRequest(request)
        return super.createObservable(requestParams)
    }

    companion object {
        const val PARAM_USER_ID_TARGET = "userIDTarget"

        fun createRequestParams(targetUserId: Long): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putLong(PARAM_USER_ID_TARGET, targetUserId)
            return requestParams
        }
    }
}