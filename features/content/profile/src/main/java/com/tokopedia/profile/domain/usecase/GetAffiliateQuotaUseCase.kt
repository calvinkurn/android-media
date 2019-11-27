package com.tokopedia.profile.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.profile.data.pojo.affiliatequota.AffiliateQuotaData
import com.tokopedia.usecase.RequestParams
import rx.Observable
import javax.inject.Inject

/**
 * @author by milhamj on 10/9/18.
 */
class GetAffiliateQuotaUseCase @Inject constructor(@ApplicationContext val context: Context)
    : GraphqlUseCase() {

    //region query
    private val query by lazy {
        """
            query {
                affiliatePostQuota() {
                    formatted
                    number
                    format
                }
            }
        """.trimIndent()
    }
    //endregion

    override fun createObservable(requestParams: RequestParams?): Observable<GraphqlResponse> {
        val request = GraphqlRequest(query, AffiliateQuotaData::class.java)

        this.clearRequest()
        this.addRequest(request)
        return super.createObservable(requestParams)
    }
}