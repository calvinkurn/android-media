package com.tokopedia.review.feature.reading.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.reading.data.ShopReviewList
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery(
        GetShopReviewListUseCase.GET_SHOP_REVIEW_LIST_USE_CASE_CLASS_NAME,
        GetShopReviewListUseCase.GET_SHOP_REVIEW_LIST_QUERY
)
class GetShopReviewListUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository
): GraphqlUseCase<ShopReviewList>(graphqlRepository) {

    companion object {
        const val PARAM_SHOP_ID = "shopID"
        const val PARAM_PAGE = "page"
        const val PARAM_LIMIT = "limit"
        const val PARAM_SORT = "sortBy"
        const val PARAM_FILTER = "filterBy"
        const val DEFAULT_LIMIT = 10
        const val GET_SHOP_REVIEW_LIST_USE_CASE_CLASS_NAME = "ShopReviewListQuery"
        const val GET_SHOP_REVIEW_LIST_QUERY = """
            query productrevGetShopReviewReadingList(${'$'}shopID: String!, ${'$'}page: Int!, ${'$'}limit: Int!, ${'$'}sortBy: String, ${'$'}filterBy: String) {
              productrevGetShopReviewReadingList(shopID: ${'$'}shopID, page: ${'$'}page, limit: ${'$'}limit, sortBy: ${'$'}sortBy, filterBy: ${'$'}filterBy) {
                shopName
                list {
                    reviewID
                    product {
                      productID
                      productName
                      productImageURL
                      isDeletedProduct
                      productVariant {
                        variantID
                        variantName
                      }
                    }
                    rating
                    reviewTime
                    reviewText
                    reviewerID
                    reviewerName
                    replyText
                    replyTime
                    attachments{
                      thumbnailURL
                      fullsizeURL
                    }
                    state {
                      isReportable 
                      isAutoReply
                      isAnonymous
                    }
                    likeDislike {
                      likeStatus
                      totalLike
                    }
                    badRatingReasonFmt
                }
                hasNext
                dataStatus {
                  likeStatusFailed
                  likeTotalFailed
                  variantProductFailed
                  productFailed
                }
              }
            }
        """
    }

    init {
        setGraphqlQuery(ShopReviewListQuery.GQL_QUERY)
        setTypeClass(ShopReviewList::class.java)
    }

    private val requestParams = RequestParams.create()

    fun setParams(shopId: String, page: Int, sort: String, filter: String) {
        requestParams.apply {
            putString(PARAM_SHOP_ID, shopId)
            putInt(PARAM_PAGE, page)
            putInt(PARAM_LIMIT, DEFAULT_LIMIT)
            putString(PARAM_SORT, sort)
            putString(PARAM_FILTER, filter)
        }
        setRequestParams(requestParams.parameters)
    }
}