package com.tokopedia.talk.feature.write.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.write.data.model.DiscussionSubmitFormResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class DiscussionSubmitFormUseCase @Inject constructor(grapqhlRepository: GraphqlRepository) : GraphqlUseCase<DiscussionSubmitFormResponseWrapper>(grapqhlRepository) {

    companion object {
        const val PARAM_CATEGORY = "category"
        const val PARAM_TEXT = "text"
        const val PARAM_PRODUCT_ID = "productID"
        private const val DISCUSSION_SUBMIT_FORM_MUTATION_CLASS_NAME = "DiscussionSubmitForm"
        private const val query =
            """
                mutation discussionSubmitForm(${'$'}text: String!, ${'$'}productID: String!, ${'$'}category: String!) {
                  discussionSubmitForm(text:${'$'}text, productID: ${'$'}productID, category: ${'$'}category) {
                    discussionID
                  }
                }

            """
    }

    init {
        setupUseCase()
    }

    @GqlQuery(DISCUSSION_SUBMIT_FORM_MUTATION_CLASS_NAME, query)
    private fun setupUseCase() {
        setGraphqlQuery(DiscussionSubmitForm.GQL_QUERY)
        setTypeClass(DiscussionSubmitFormResponseWrapper::class.java)
    }

    fun setParams(text: String, category: String, productId: String) {
        setRequestParams(RequestParams.create().apply {
            putString(PARAM_TEXT, text)
            putString(PARAM_PRODUCT_ID, productId.toString())
            putString(PARAM_CATEGORY, category)
        }.parameters)
    }
}