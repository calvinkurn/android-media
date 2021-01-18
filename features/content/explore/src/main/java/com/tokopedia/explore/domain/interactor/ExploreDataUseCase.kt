package com.tokopedia.explore.domain.interactor

import com.tokopedia.explore.domain.entity.GetExploreData
import com.tokopedia.explore.domain.raw.ExploreDataQueries
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

@GqlQuery("GetExploreDataQuery", ExploreDataQueries.GET_EXPLORE_DATA_QUERY)
class ExploreDataUseCase @Inject constructor(graphqlRepository: GraphqlRepository,
                                             val userSession: UserSessionInterface) :
        GraphqlUseCase<GetExploreData>(graphqlRepository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.WEEK.`val`())
                .setSessionIncluded(true)
                .build())
        setTypeClass(GetExploreData::class.java)
        setGraphqlQuery(GetExploreDataQuery.GQL_QUERY)
    }

    fun setParams(categoryId: Int = ExploreDataQueries.DEFAULT_CATEGORY_ID, cursor: String, search: String, limit: Int = ExploreDataQueries.LIMIT) {
        val queryMap = HashMap<String, Any?>()
        queryMap[ExploreDataQueries.PARAM_CATEGORY_ID] = categoryId
        queryMap[ExploreDataQueries.PARAM_CURSOR] = cursor
        queryMap[ExploreDataQueries.PARAM_SEARCH] = search
        queryMap[ExploreDataQueries.PARAM_LIMIT] = limit
        setRequestParams(queryMap)
    }
}