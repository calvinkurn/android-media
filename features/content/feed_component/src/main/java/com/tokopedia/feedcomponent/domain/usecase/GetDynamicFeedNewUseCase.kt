package com.tokopedia.feedcomponent.domain.usecase

import com.tokopedia.feedcomponent.data.feedrevamp.FeedXData
import com.tokopedia.feedcomponent.domain.mapper.DynamicFeedNewMapper
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

const val FEED_X_QUERY: String = """
query feedxhome(${'$'}req: FeedXHomeRequest!) {
  feedXHome(req:${'$'}req) {
    items {
      __typename
      ... on FeedXCardBanners {
        id
        items {
          id
          appLink
          webLink
          coverURL
          mods
        }
        publishedAt
        mods
      }
      ... on FeedXCardPost {
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
        title
        subTitle
        text
        appLink
        webLink
        actionButtonLabel
        actionButtonOperationWeb
        actionButtonOperationApp
        reportable
        media {
          id
          type
          coverURL
          mediaURL
          appLink
          webLink
          tagging {
            tagIndex
            posX
            posY
          }
          mods
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
          isDiscount
          discount
          discountFmt
          priceOriginal
          priceOriginalFmt
          priceDiscount
          priceDiscountFmt
          totalSold
          isBebasOngkir
          bebasOngkirStatus
          bebasOngkirURL
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
        comm: comments {
          label
          count
          countFmt
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
          mods
        }
        sh: share {
          label
          operation
          mods
        }
        fol: followers {
          label
          isFollowed
          count
          countFmt
          mods
        }
        publishedAt
        mods
      }
      ... on FeedXCardTopAds {
        a: id
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
        promos
        items {
          id
          product {
            id
            name
            coverURL
            webLink
            appLink
            star
            price
            priceFmt
            isDiscount
            discount
            discountFmt
            priceOriginal
            priceOriginalFmt
            priceDiscount
            priceDiscountFmt
            totalSold
            isBebasOngkir
            bebasOngkirStatus
            bebasOngkirURL
            mods
          }
          mods
        }
        publishedAt
        mods
      }
      ... on FeedXCardProductsHighlight {
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
        title
        subTitle
        text
        appLink
        products {
          id
          name
          coverURL
          webLink
          appLink
          star
          price
          priceFmt
          isDiscount
          discount
          discountFmt
          priceOriginal
          priceOriginalFmt
          priceDiscount
          priceDiscountFmt
          totalSold
          isBebasOngkir
          bebasOngkirStatus
          bebasOngkirURL
          mods
        }
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
          mods
        }
        share {
          label
          operation
          mods
        }
        followers {
          label
          isFollowed
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
    }
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

@GqlQuery("GetFeedXHomeQuery", FEED_X_QUERY)
class GetDynamicFeedNewUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<FeedXData>(graphqlRepository) {

    init {
        setTypeClass(FeedXData::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(GetFeedXHomeQuery.GQL_QUERY)
    }

    fun setParams(cursor: String, limit: Int) {
        val queryMap = mutableMapOf(
                CURSOR to cursor,
                LIMIT to limit
        )
        val map = mutableMapOf("req" to queryMap)
        setRequestParams(map)
    }

    suspend fun execute(cursor: String = "", limit: Int = 5):
            DynamicFeedDomainModel {
        this.setParams(cursor, limit)
        val dynamicFeedResponse = executeOnBackground()
        return DynamicFeedNewMapper.map(dynamicFeedResponse.feedXHome)
    }

}