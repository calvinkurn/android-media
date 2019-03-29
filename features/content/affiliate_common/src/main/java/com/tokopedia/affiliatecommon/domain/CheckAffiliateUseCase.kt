package com.tokopedia.affiliatecommon.domain

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.affiliatecommon.R
import com.tokopedia.affiliatecommon.data.pojo.checkaffiliate.AffiliateCheckData
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by yfsx on 06/03/19.
 */
class CheckAffiliateUseCase @Inject
constructor(@param:ApplicationContext private val context: Context,
            private val graphqlUseCase: GraphqlUseCase) : UseCase<Boolean>() {

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        val graphqlRequest = GraphqlRequest(
                GraphqlHelper.loadRawString(context.resources, R.raw.query_affiliate_check),
                AffiliateCheckData::class.java
        )
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map({
            val data: AffiliateCheckData? = it.getData(AffiliateCheckData::class.java)
            if (data?.affiliateCheck == null) {
                throw RuntimeException()
            }

            data.affiliateCheck!!.isIsAffiliate
        })
    }
}