package com.tokopedia.feedcomponent.domain.usecase

import android.content.Context
import android.text.TextUtils
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXData
import com.tokopedia.feedcomponent.domain.mapper.DynamicFeedNewMapper
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.util.TopadsRollenceUtil
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
        editable
        deletable
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
        mediaRatio {
          width
          height
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
        views {
          label
          count
          countFmt
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
       ... on FeedXCardPlay {
        id
        playChannelID
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
        mediaRatio {
          width
          height
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
        views {
          label
          count
          countFmt
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
        type
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
        appLinkProductList
        webLinkProductList
        maximumDiscountPercentage
        maximumDiscountPercentageFmt
        totalProducts
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
          isCashback
          cashbackFmt
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
        deletable
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
val DETAIL_ID = "sourceID"
val SOURCE = "source"

@GqlQuery("GetFeedXHomeQuery", FEED_X_QUERY)
class GetDynamicFeedNewUseCase @Inject constructor(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<FeedXData>(graphqlRepository) {

    var context: Context? = null

    init {
        this.context = context
        setTypeClass(FeedXData::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(GetFeedXHomeQuery.GQL_QUERY)
    }

    fun setParams(cursor: String, limit: Int, detailId: String = "") {
        val queryMap = mutableMapOf(
                CURSOR to cursor,
                LIMIT to limit
        )
        if (!TextUtils.isEmpty(detailId)) {
            queryMap[DETAIL_ID] = detailId
            queryMap[SOURCE] = "detail"
        }
        val map = mutableMapOf("req" to queryMap)
        setRequestParams(map)
    }

    suspend fun execute(cursor: String = "", limit: Int = 5, detailId: String = ""):
            DynamicFeedDomainModel {
        this.setParams(cursor, limit, detailId)
        val dynamicFeedResponse = executeOnBackground()
        val shouldShowNewTopadsOnly = context?.let { TopadsRollenceUtil.shouldShowFeedNewDesignValue(it) }?:true
        return DynamicFeedNewMapper.map(dynamicFeedResponse.feedXHome, cursor, shouldShowNewTopadsOnly)
    }

}