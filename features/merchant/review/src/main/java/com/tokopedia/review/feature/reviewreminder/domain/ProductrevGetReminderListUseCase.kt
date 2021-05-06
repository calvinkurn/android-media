package com.tokopedia.review.feature.reviewreminder.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderListResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ProductrevGetReminderListUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ProductrevGetReminderListResponseWrapper>(graphqlRepository) {
    companion object {
        const val LIMIT_VALUE = 20

        const val PARAM_LIMIT = "limit"
        const val PARAM_LAST_PRODUCT_ID = "lastProductID"
        const val GET_REMINDER_LIST_QUERY_CLASS_NAME = "ReminderList"
        const val GET_REMINDER_LIST_QUERY = """
            query productrevGetReminderList(${'$'}limit: Int!,${'$'}lastProductID: String!) {
              productrevGetReminderList(limit:${'$'}limit, lastProductID:${'$'}lastProductID) {
                list {
                  productID
                  productName
                  productThumbnail
                  avgRating
                  ratingCount
                  buyerCount
                }
                lastProductID
                hasNext
              }
            }
        """
    }

    init {
        init()
    }

    @GqlQuery(GET_REMINDER_LIST_QUERY_CLASS_NAME, GET_REMINDER_LIST_QUERY)
    private fun init() {
        setTypeClass(ProductrevGetReminderListResponseWrapper::class.java)
        setGraphqlQuery(ReminderList.GQL_QUERY)
    }

    fun setParams(lastProductId: String) {
        setRequestParams(RequestParams.create().apply {
            putInt(PARAM_LIMIT, LIMIT_VALUE)
            putString(PARAM_LAST_PRODUCT_ID, lastProductId)
        }.parameters)
    }


}