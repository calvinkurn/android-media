package com.tokopedia.talk.feature.write.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.write.data.model.DiscussionGetWritingFormResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class DiscussionGetWritingFormUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<DiscussionGetWritingFormResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_PRODUCT_ID = "productID"
        private const val DISCUSSION_GET_WRITING_FORM_QUERY_CLASS_NAME = "DiscussionGetWritingForm"
        private const val query =
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
            """
    }

    init {
        setupUseCase()
    }

    @GqlQuery(DISCUSSION_GET_WRITING_FORM_QUERY_CLASS_NAME, query)
    private fun setupUseCase() {
        setGraphqlQuery(DiscussionGetWritingForm.GQL_QUERY)
        setTypeClass(DiscussionGetWritingFormResponseWrapper::class.java)
    }

    fun setParams(productId: String) {
        setRequestParams(RequestParams.create().apply {
            putString(PARAM_PRODUCT_ID, productId)
        }.parameters)
    }
}