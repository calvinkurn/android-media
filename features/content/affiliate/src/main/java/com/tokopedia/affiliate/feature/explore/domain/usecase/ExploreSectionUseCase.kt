package com.tokopedia.affiliate.feature.explore.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.explore.data.pojo.section.ExploreSectionResponse
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by milhamj on 14/03/19.
 */
class ExploreSectionUseCase @Inject constructor(@ApplicationContext val context: Context,
                                                val graphqlUseCase: GraphqlUseCase)
    : UseCase<List<Visitable<*>>>() {

    init {
        graphqlUseCase.setCacheStrategy(
                GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                        .setExpiryTime(GraphqlConstant.ExpiryTimes.DAY.`val`())
                        .setSessionIncluded(true)
                        .build()
        )
    }

    override fun createObservable(requestParams: RequestParams?): Observable<List<Visitable<*>>> {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.query_af_explore_section)
        val request = GraphqlRequest(query, ExploreSectionResponse::class.java)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(request)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map(mapResponse())
    }

    private fun mapResponse(): Func1<GraphqlResponse, List<Visitable<*>>> {
        return Func1 {
            val sections: MutableList<Visitable<*>> = arrayListOf()
            sections
        }
    }
}