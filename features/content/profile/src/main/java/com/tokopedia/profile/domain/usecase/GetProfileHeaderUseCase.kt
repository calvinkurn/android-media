package com.tokopedia.profile.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.profile.R
import com.tokopedia.profile.data.pojo.profileheader.ProfileHeaderData
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
                requestParams?.parameters, false
        )

        this.clearRequest()
        this.addRequest(request)
        return super.createObservable(requestParams)
    }

    companion object {
        const val PARAM_USER_ID = "userIDTarget"

        fun createRequestParams(userId: Int): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putInt(PARAM_USER_ID, userId)
            return requestParams
        }
    }
}