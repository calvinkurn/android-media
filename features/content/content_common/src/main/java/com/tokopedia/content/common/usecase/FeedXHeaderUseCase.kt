package com.tokopedia.content.common.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.content.common.model.FeedXHeaderRequestFields
import com.tokopedia.content.common.model.FeedXHeaderResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

@GqlQuery(FeedXHeaderUseCase.QUERY_NAME, FeedXHeaderUseCase.QUERY)
class FeedXHeaderUseCase @Inject constructor(
    @ApplicationContext graphqlRepository: GraphqlRepository
) : GraphqlUseCase<FeedXHeaderResponse>(graphqlRepository) {

    init {
        setTypeClass(FeedXHeaderResponse::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(FeedXHeaderQuery())
    }

    companion object {
        private const val PARAM_FIELDS = "fields"
        private const val PARAM_SOURCES = "sources"
        const val QUERY_NAME = "FeedXHeaderQuery"
        const val QUERY = """
        query FeedXHeader(${'$'}$PARAM_FIELDS: [String!]!, ${'$'}$PARAM_SOURCES: [FeedXHeaderSources]) {
          feedXHeader(req: {fields: ${'$'}$PARAM_FIELDS, sources: ${'$'}$PARAM_SOURCES}) {
            data {
              ...FeedXHeaderData
              __typename
            }
            error
            __typename
          }
        }
        
        fragment FeedXHeaderData on FeedXHeaderData {
          creation {
            ...FeedXHeaderCreation
            __typename
          }
          tab {
            ...FeedXHeaderTab
            __typename
          }
          live {
            ...FeedXHeaderLive
            __typename
          }
          userProfile {
            ...FeedXHeaderUserProfile
            __typename
          }
          browse {
            ...FeedXHeaderBrowse
            __typename
          }
          cdpTitle {
            ...FeedXHeaderCDPTitle
            __typename
          }
        }
        
        fragment FeedXHeaderCreation on FeedXHeaderCreation {
          isActive
          image
          authors {
            id
            name
            type
            image
            hasUsername
            hasAcceptTnC
            items {
              isActive
              type
              title
              image
              weblink
              applink
              __typename
            }
          }
          __typename
        }
        
        fragment FeedXHeaderTab on FeedXHeaderTab {
          isActive
          items {
            isActive
            position
            type
            title
            key
            newContentExists
            __typename
          }
          meta {
            selectedIndex
            __typename
          }
          __typename
        }
        
        fragment FeedXHeaderLive on FeedXHeaderLive {
          isActive
          title
          image
          weblink
          applink
          __typename
        }
        
        fragment FeedXHeaderUserProfile on FeedXHeaderUserProfile {
          isShown
          image
          weblink
          applink
          __typename
        }
        
        fragment FeedXHeaderBrowse on FeedXHeaderBrowse {
          isActive
          weblink
          applink
          title
          searchBar {
            isActive
            placeholder
            weblink
            applink
            __typename
          }
          __typename
        }
        
        fragment FeedXHeaderCDPTitle on FeedXHeaderCDPTitle {
          title
          __typename
        }
        """

        fun createParam(
            fields: List<String> = FeedXHeaderRequestFields.values().map { it.value },
            sources: List<Map<String, String>> = emptyList()
        ): Map<String, Any> {
            val params = mutableMapOf<String, Any>(
                PARAM_FIELDS to fields
            )
            if (sources.isNotEmpty()) {
                params[PARAM_SOURCES] = sources
            }
            return params
        }
    }
}
