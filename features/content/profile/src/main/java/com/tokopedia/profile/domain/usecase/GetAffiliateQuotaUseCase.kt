package com.tokopedia.profile.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.profile.R
import com.tokopedia.profile.data.pojo.affiliatequota.AffiliateQuotaData
import com.tokopedia.usecase.RequestParams
import rx.Observable
import javax.inject.Inject

/**
 * @author by milhamj on 10/9/18.
 */
class GetAffiliateQuotaUseCase @Inject constructor(@ApplicationContext val context: Context)
    : GraphqlUseCase() {
    override fun createObservable(requestParams: RequestParams?): Observable<GraphqlResponse> {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.query_af_quota)
        val request = GraphqlRequest(query, AffiliateQuotaData::class.java, false)

        this.clearRequest()
        this.addRequest(request)
        return super.createObservable(requestParams)
    }
}