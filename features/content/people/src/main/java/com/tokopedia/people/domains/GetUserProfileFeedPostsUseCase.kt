package com.tokopedia.people.domains

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.people.model.UserFeedPostsModel
import javax.inject.Inject

/**
 * created by fachrizalmrsln on 14/11/2022
 */
@GqlQuery(GetUserProfileFeedPostsUseCase.QUERY_NAME, GetUserProfileFeedPostsUseCase.QUERY)
class GetUserProfileFeedPostsUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<UserFeedPostsModel>(graphqlRepository) {

    init {
        setGraphqlQuery(GetUserProfileFeedPostsUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(UserFeedPostsModel::class.java)
    }

    suspend fun executeOnBackground(userID: String, cursor: String): UserFeedPostsModel {
        val request = mapOf(
            KEY_SOURCE_ID to userID,
            KEY_CURSOR to cursor,
            KEY_LIMIT to VAL_LIMIT,
        )
        setRequestParams(request)

        return executeOnBackground()
    }

    companion object {
        private const val KEY_SOURCE_ID = "sourceID"
        private const val KEY_CURSOR = "cursor"
        private const val KEY_LIMIT = "limit"
        private const val VAL_LIMIT = 10
        const val QUERY_NAME = "GetUserProfileFeedPostsUseCaseQuery"
        const val QUERY = """
            query FeedXProfileGetProfilePosts(
                ${"$${KEY_SOURCE_ID}"}: String!, 
                ${"$${KEY_CURSOR}"}: String!, 
                ${"$${KEY_LIMIT}"}: Int!
            ) {
            feedXProfileGetProfilePosts(req: {
                ${KEY_SOURCE_ID}: ${"$${KEY_SOURCE_ID}"}, 
                ${KEY_CURSOR}: ${"$${KEY_CURSOR}"}, 
                ${KEY_LIMIT}: ${"$${KEY_LIMIT}"}}) {
                posts {
                  __typename
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
                      encryptedUserID
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
                    tags {
                      id
                      shopID
                      shopName
                      shopType
                      shopTypeName
                      shopBadgeURL
                      name
                      coverURL
                      webLink
                      appLink
                      star
                      countReview
                      countReviewFmt
                      price
                      priceFmt
                      isDiscount
                      discount
                      discountFmt
                      priceOriginal
                      priceOriginalFmt
                      priceDiscount
                      priceDiscountFmt
                      countReview
                      countReviewFmt
                      totalSold
                      totalSoldFmt
                      isBebasOngkir
                      bebasOngkirStatus
                      bebasOngkirURL
                      isBestSeller
                      bestSellerFmt
                      isCashback
                      cashbackFmt
                      isHalal
                      halalFmt
                      isPricePerPiece
                      pricePerPieceFmt
                      etaShipping
                      mods
                    }
                    hashtagAppLinkFmt
                    hashtagWebLinkFmt
                    views{
                      label
                      count
                      countFmt
                      mods
                    }
                    mediaRatio{
                      width
                      height
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
                    reportable
                    editable
                    deletable
                    mods
                  }
                }
                
                pagination {
                  cursor
                  hasNext
                  totalData
                }
              }
            }
        """
    }
}
