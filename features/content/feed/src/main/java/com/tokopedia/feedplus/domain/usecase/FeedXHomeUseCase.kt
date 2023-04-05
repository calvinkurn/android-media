package com.tokopedia.feedplus.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.feedplus.data.FeedXHomeEntity
import com.tokopedia.feedplus.domain.mapper.MapperFeedHome
import com.tokopedia.feedplus.presentation.model.FeedModel
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 23/02/23
 */
class FeedXHomeUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Map<String, Any>, FeedModel>(dispatcher.io) {

    override suspend fun execute(params: Map<String, Any>): FeedModel {
        val response =
            graphqlRepository.request<Map<String, Any>, FeedXHomeEntity.
            Response>(
                graphqlQuery(),
                params
            )
        return MapperFeedHome.transform(response.feedXHome)
    }

    override fun graphqlQuery(): String = """
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
              media {
                type
                id
                coverURL
                mediaURL
                appLink
                tagging {
                  tagIndex
                }
              }
              tags {
                ...FeedXProduct
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
              media {
                type
                id
                coverURL
                mediaURL
                appLink
                tagging {
                  tagIndex
                }
              }
              tags {
                ...FeedXProduct
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
              media {
                type
                id
                coverURL
                mediaURL
                appLink
                tagging {
                  tagIndex
                }
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
              isLive
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
        """.trimIndent()

    fun createParams(
        source: String,
        cursor: String,
        limit: Int = 10,
        detailId: String = ""
    ): Map<String, Any> {
        val params = mutableMapOf(
            PARAMS_SOURCE to source,
            PARAMS_CURSOR to cursor,
            PARAMS_LIMIT to limit
        )
        if (detailId.isNotEmpty()) {
            params[PARAMS_SOURCE_ID] = detailId
        }

        return mapOf(PARAMS_REQUEST to params)
    }

    fun createPostDetailParams(postId: String): Map<String, Any> {
        val params = mapOf<String, Any>(
            PARAMS_SOURCE to SOURCE_DETAIL,
            PARAMS_SOURCE_ID to postId,
            PARAMS_CURSOR to "",
            PARAMS_LIMIT to LIMIT_DETAIL,
        )

        return mapOf(PARAMS_REQUEST to params)
    }

    companion object {
        private const val PARAMS_REQUEST = "req"

        private const val PARAMS_SOURCE = "source"
        private const val PARAMS_SOURCE_ID = "sourceID"
        private const val PARAMS_CURSOR = "cursor"
        private const val PARAMS_LIMIT = "limit"

        private const val SOURCE_DETAIL = "detail-unified"

        private const val LIMIT_DETAIL = 1
    }
}
