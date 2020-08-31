package com.tokopedia.product.detail.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.data.model.talk.DiscussionMostHelpfulResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class DiscussionMostHelpfulUseCase @Inject constructor(query: String,
                                                       graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<DiscussionMostHelpfulResponseWrapper>(graphqlRepository) {

    init {
        setGraphqlQuery(query)
        setTypeClass(DiscussionMostHelpfulResponseWrapper::class.java)
    }

    fun createRequestParams(productId: String, shopId: String) {
        setRequestParams(RequestParams.create().apply {
            with(ProductDetailCommonConstant) {
                putString(PARAM_PRODUCT_ID, productId)
                putString(PARAM_SHOP_ID, shopId)
            }
        }.parameters)
    }

}