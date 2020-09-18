package com.tokopedia.affiliate.feature.explore.domain.usecase

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.feature.explore.data.pojo.section.ExploreSectionResponse
import com.tokopedia.affiliate.feature.explore.domain.mapper.ExploreSectionMapper
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by milhamj on 14/03/19.
 */
class ExploreSectionUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase,
        private val exploreSectionMapper: ExploreSectionMapper
) : UseCase<List<Visitable<*>>>() {

    private val query = """
        {
          affiliateCPAExploreSections(limit: 5) {
            explore_page_section {
              type
              title
              subtitle
              items {
                title
                subtitle
                image
                fav_icon
                app_link
                web_link
                lite_link
                ad_id
                category_id
                product_id
                commission_value
                commission_percent
                commission_value_display
                commission_percent_display
                user_id
              }
            }
          }
        }
    """

    init {
        graphqlUseCase.setCacheStrategy(
                GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                        .setExpiryTime(GraphqlConstant.ExpiryTimes.DAY.`val`())
                        .setSessionIncluded(true)
                        .build()
        )
    }

    override fun createObservable(requestParams: RequestParams?): Observable<List<Visitable<*>>> {
        val request = GraphqlRequest(query, ExploreSectionResponse::class.java)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(request)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map(exploreSectionMapper)
    }
}