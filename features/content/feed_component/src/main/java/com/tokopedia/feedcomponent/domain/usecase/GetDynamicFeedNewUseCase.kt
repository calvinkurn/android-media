package com.tokopedia.feedcomponent.domain.usecase

import com.tokopedia.feedcomponent.data.feedrevamp.FeedXQuery
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

const val FEED_X_QUERY: String = """
query feedxhome(${'$'}cursor: String!, ${'$'}limit: Int!) {
  feedXHome(req: {cursor: ${'$'}cursor, limit: ${'$'}limit}) {
    items {
      __typename
      ... on FeedXCardBanners {
        id
        publishedAt
        mods
        items {
          id
          appLink
          webLink
          mods
          coverURL
        }
      }
      ... on FeedXCardPost {
        id
        author {
          id
          name
          type
          description
          badgeURL
          logoURL
          webLink
          appLink
        }
        title
        subTitle
        text
        appLink
        webLink
        actionButtonLabel
        actionButtonOperationWeb
        actionButtonOperationApp
        media {
          type
          id
          coverURL
          mediaURL
          appLink
          webLink
          mods
          tagging {
            tagIndex
            posX
            posY
          }
        }
        tags {
          id
          name
          coverURL
          webLink
          appLink
          star
          price
          priceFmt
          priceStruck
          priceStruckFmt
          discount
          discountFmt
          badgeTexts
          badgeImageURLs
          mods
        }
        hashtagAppLinkFmt
        hashtagWebLinkFmt
        like {
          label
          count
          countFmt
          likedBy
          isLiked
          mods
        }
        comments {
          label
          count
          countFmt
          mods
          items {
            id
            author {
              id
              type
              name
              description
              badgeURL
              logoURL
              webLink
              appLink
            }
            text
            mods
          }
        }
        share {
          label
          operation
          mods
        }
        followers {
          isFollowed
          label
          count
          countFmt
          mods
        }
        publishedAt
        mods
      }
      ... on FeedXCardPlaceholder {
        id
        type
        mods
      }
      ... on FeedXCardProductsHighlight {
        id
        author {
          id
          name
          type
          description
          badgeURL
          logoURL
          webLink
          appLink
        }
        title
        subTitle
        text
        products {
          id
          name
          coverURL
          webLink
          appLink
          star
          price
          priceFmt
          priceStruck
          priceStruckFmt
          discount
          discountFmt
          badgeTexts
          badgeImageURLs
          mods
        }
        publishedAt
        mods
      }
    }
    mods
    pagination {
      totalData
      cursor
      hasNext
    }
  }
}
"""

private const val CURSOR: String = "cursor"
private const val LIMIT = "limit"

@GqlQuery("FeedXQuery", FEED_X_QUERY)
class GetDynamicFeedNewUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<FeedXQuery>(graphqlRepository) {

    init {
        setTypeClass(FeedXQuery::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(FeedXQuery.GQL_QUERY)
    }

    fun setParams(cursor: String = "", limit: Int = 5) {
        val queryMap = mutableMapOf(
                CURSOR to cursor,
                LIMIT to limit
        )
        setRequestParams(queryMap)
    }

}