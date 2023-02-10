package com.tokopedia.feedplus.domain.usecase

import com.tokopedia.feedplus.data.FeedTabsEntity
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
@GqlQuery(
    FeedTabsUseCase.QUERY_NAME_FEED_TABS,
    FeedTabsUseCase.QUERY_FEED_TABS
)
class FeedTabsUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<FeedTabsEntity.Response>(graphqlRepository) {

    init {
        setGraphqlQuery(FeedTabsQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build())
        setTypeClass(FeedTabsEntity.Response::class.java)
    }

    override suspend fun executeOnBackground(): FeedTabsEntity.Response {
        GraphqlClient.moduleName = FEED_MODULE_NAME
        setRequestParams(mapOf(SOURCE_KEY to SOURCE_VALUE))
        return super.executeOnBackground()
    }

    companion object {
        private const val FEED_MODULE_NAME = "feed"

        private const val SOURCE_KEY = "source"
        private const val SOURCE_VALUE = "immersive"

        const val QUERY_NAME_FEED_TABS = "FeedTabsQuery"
        const val QUERY_FEED_TABS = """
                query feedTabs(${'$'}source: String) {
                  feedTabs(source: ${'$'}source){
                    meta{
                      selectedIndex
                      myProfileAppLink
                      myProfileWebLink
                      myProfilePhotoURL
                      showMyProfile
                    }
                    data{
                      title
                      key
                      type
                      position
                      isActive
                    }
                  }
                }
            """
    }
}
