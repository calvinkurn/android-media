package com.tokopedia.talk.feature.reading.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.reading.data.model.DiscussionDataResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetDiscussionDataUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<DiscussionDataResponse>(graphqlRepository) {

    companion object {

        const val PARAM_PRODUCT_ID = "productID"
        const val PARAM_SHOP_ID = "shopID"
        const val PARAM_PAGE = "page"
        const val PARAM_LIMIT = "limit"
        const val PARAM_SORT = "sortBy"
        const val PARAM_CATEGORY = "category"

        private val query by lazy {
            val productID = "\$productID"
            val shopID = "\$shopID"
            val page = "\$page"
            val limit = "\$limit"
            val sortBy = "\$sortBy"
            val category = "\$category"

            """
            query discussionDataByProductID(
                $productID: Int!,
                $shopID: Int!,
                $page: Int!,
                $limit: Int!,
                $sortBy: String,
                $category: String
            ) {
                discussionDataByProductID(
                            productID: $productID,
                            shopID: $shopID,
                            page: $page,
                            limit: $limit!,
                            sortBy: $sortBy,
                            category: $category
                hasNext
                totalQuestion
                question {
                    content
                    maskedContent
                    userName
                    userID
                    createTime
                    createTimeFormatted
                    
                    likeCount
                    state {
                        isMasked
                        isLiked
                        allowLike
                    }
                    
                    totalAnswer
                    answer {
                        content
                        userName
                        userThumbnail
                        userID
                        isSeller
                        createTime
                        createTimeFormatted
                        attachedProductCount
                    }
                }
            }
        """.trimIndent()
        }
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(DiscussionDataResponse::class.java)
    }

    fun setParams(productId: Int, shopId: Int, page: Int, limit: Int, sortBy: String, category: String) {
        val requestParams = RequestParams()
        requestParams.putInt(PARAM_PRODUCT_ID, productId)
        requestParams.putInt(PARAM_SHOP_ID, shopId)
        requestParams.putInt(PARAM_PAGE, page)
        requestParams.putInt(PARAM_LIMIT, limit)
        requestParams.putString(PARAM_SORT, sortBy)
        requestParams.putString(PARAM_CATEGORY, category)
        setRequestParams(requestParams.parameters)
    }
}