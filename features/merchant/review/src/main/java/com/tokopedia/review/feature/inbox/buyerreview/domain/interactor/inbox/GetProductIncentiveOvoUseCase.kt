package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.review.R
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.ProductRevIncentiveOvoDomain
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class GetProductIncentiveOvoUseCase @Inject constructor(
        @ApplicationContext private val context: Context,
        private val graphqlUseCase: GraphqlUseCase
) : UseCase<ProductRevIncentiveOvoDomain>() {

    override fun createObservable(requestParams: RequestParams?): Observable<ProductRevIncentiveOvoDomain> {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.gql_product_rev_incentive_ovo)
        val request = GraphqlRequest(query, ProductRevIncentiveOvoDomain::class.java, false)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(request)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map { it.getData(ProductRevIncentiveOvoDomain::class.java) as ProductRevIncentiveOvoDomain }
    }
}