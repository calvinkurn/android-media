package com.tokopedia.feedplus.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 23/02/23
 */
@GqlQuery(FeedXHomeUseCase.QUERY_NAME, FeedXHomeUseCase.QUERY)
class FeedXHomeUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<Any>(graphqlRepository) {

    init {
        setTypeClass(Any::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setGraphqlQuery(FeedXHomeQuery())
    }

    fun createParams(source: String, cursor: String, limit: Int = 0, detailId: String = "") {
        val params = mutableMapOf(
            PARAMS_SOURCE to source,
            PARAMS_CURSOR to cursor,
            PARAMS_LIMIT to limit
        )
        if (detailId.isNotEmpty()) {
            params[PARAMS_SOURCE_ID] = detailId
        }

        setRequestParams(mapOf(PARAMS_REQUEST to params))
    }

    companion object {
        private const val PARAMS_REQUEST = "req"

        private const val PARAMS_SOURCE = "source"
        private const val PARAMS_SOURCE_ID = "sourceID"
        private const val PARAMS_CURSOR = "cursor"
        private const val PARAMS_LIMIT = "limit"

        const val QUERY_NAME = "FeedXHomeQuery"
        const val QUERY = """
            query feedXHome(${'$'}req: FeedXHomeRequest!) {
              feedXHome(req:${'$'}req) {
                items {
                  __typename
                  ...FeedXCardBanners
                  ...FeedXCardPost
                  ...FeedXCardPlay
                  ...FeedXCardTopAds
                  ...FeedXCardProductsHighlight
                  ...FeedXCardPlaceholder
                }
                pagination {
                  totalData
                  cursor
                  hasNext
                }
              }
            }

            fragment FeedXCardBanners on FeedXCardBanners {
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

            fragment FeedXCardPost on FeedXCardPost {
              id
              type
              author {
                ...FeedXAuthor
              }
              title
              subTitle
              text
              appLink
              webLink
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
                    ...FeedXAuthor
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
              editable
              deletable
              detailScore {
                label
                value
              }
            }
            
            fragment FeedXCardPlay on FeedXCardPlay {
              playChannelID
              id
              type
              author {
                ...FeedXAuthor
              }
              title
              subTitle
              text
              appLink
              webLink
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
                    ...FeedXAuthor
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
              editable
              deletable
              detailScore {
                label
                value
              }
            }
            
            fragment FeedXCardTopAds on FeedXCardTopAds {
              id
              author {
                ...FeedXAuthor
              }
              promos
              items {
                id
                product {
                  ...FeedXProduct
                }
                mods
              }
              publishedAt
              mods
            }
            
            fragment FeedXCardProductsHighlight on FeedXCardProductsHighlight {
              id
              type
              author {
                ...FeedXAuthor
              }
              title
              subTitle
              text
              appLink
              webLink
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
                    ...FeedXAuthor
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
              editable
              deletable
              detailScore {
                label
                value
              }
              hasVoucher
              cta {
                text
                subtitle
                color
                colorGradient {
                  color
                  position
                }
                __typename
              }
              ribbonImageURL
              campaign {
                id
                status
                name
                shortName
                startTime
                endTime
                restrictions {
                  label
                  isActive
                  __typename
                }
              }
              appLinkProductList
              webLinkProductList
              maximumDiscountPercentage
              maximumDiscountPercentageFmt
              totalProducts
              products {
                ...FeedXProduct
                priceMasked
                priceMaskedFmt
                stockWording
                stockSoldPercentage
                cartable
                isCashback
                cashbackFmt
              }
            }
            
            fragment FeedXCardPlaceholder on FeedXCardPlaceholder {
              id
              type
              mods
            }
            
            fragment FeedXAuthor on FeedXAuthor {
              id
              type
              name
              description
              badgeURL
              logoURL
              webLink
              appLink
              encryptedUserID
            }
            
            fragment FeedXProduct on FeedXProduct {
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
              shopID
              shopName
              mods
            }
        """
    }
}
