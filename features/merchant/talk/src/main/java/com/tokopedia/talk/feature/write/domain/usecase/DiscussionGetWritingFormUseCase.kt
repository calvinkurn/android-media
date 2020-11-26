package com.tokopedia.talk.feature.write.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.write.data.model.DiscussionGetWritingFormResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class DiscussionGetWritingFormUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<DiscussionGetWritingFormResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_PRODUCT_ID = "productID"

        private val query by lazy {
            """
                query discussionGetWritingForm(${'$'}productID: String!) {
                  discussionGetWritingForm(productID:${'$'}productID) {
                    productName
                    productThumbnailURL
                    productID
                    productPrice
                    shopID
                    minChar
                    maxChar
                    categories {
                        name
                        message
                    }
                  }
                }
            """.trimIndent()
        }
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(DiscussionGetWritingFormResponseWrapper::class.java)
    }

    fun setParams(productId: Int) {
        setRequestParams(RequestParams.create().apply {
            putString(PARAM_PRODUCT_ID, productId.toString())
        }.parameters)
    }
}