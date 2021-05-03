package com.tokopedia.review.feature.createreputation.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.createreputation.domain.usecase.GetReviewTemplatesUseCase.Companion.REVIEW_TEMPLATES_QUERY
import com.tokopedia.review.feature.createreputation.domain.usecase.GetReviewTemplatesUseCase.Companion.REVIEW_TEMPLATES_QUERY_CLASS_NAME
import com.tokopedia.review.feature.createreputation.model.ProductrevGetReviewTemplateResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery(REVIEW_TEMPLATES_QUERY_CLASS_NAME, REVIEW_TEMPLATES_QUERY)
class GetReviewTemplatesUseCase @Inject constructor(graphqlRepository: GraphqlRepository): GraphqlUseCase<ProductrevGetReviewTemplateResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_PRODUCT_ID = "productID"
        const val PARAM_PAGE = "page"
        const val REVIEW_TEMPLATES_QUERY_CLASS_NAME = "ReviewTemplates"
        const val REVIEW_TEMPLATES_QUERY = """
            query productrevGetPersonalizedReviewTemplate(${'$'}productID: String, ${'$'}page: Int) {
              productrevGetPersonalizedReviewTemplate(productID: ${'$'}productID, page: ${'$'}page, limit: 10) {
                templates
                hasNext
              }
            }
            """
    }

    private var requestParams = RequestParams.EMPTY

    init {
        setGraphqlQuery(ReviewTemplates.GQL_QUERY)
        setTypeClass(ProductrevGetReviewTemplateResponseWrapper::class.java)
    }

    fun setParams(productId: String, page: Int) {
        requestParams.apply {
            putString(PARAM_PRODUCT_ID, productId)
            putInt(PARAM_PAGE, page)
        }
        setRequestParams(requestParams.parameters)
    }
}