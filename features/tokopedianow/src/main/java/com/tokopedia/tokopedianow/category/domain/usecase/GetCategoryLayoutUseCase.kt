package com.tokopedia.tokopedianow.category.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.category.domain.query.GetCategoryLayout
import com.tokopedia.tokopedianow.category.domain.query.GetCategoryLayout.PARAM_CLIENT_TYPE
import com.tokopedia.tokopedianow.category.domain.query.GetCategoryLayout.PARAM_IDENTIFIER
import com.tokopedia.tokopedianow.category.domain.query.GetCategoryLayout.PARAM_IS_LATEST_VERSION
import com.tokopedia.tokopedianow.category.domain.response.GetCategoryLayoutResponse
import com.tokopedia.tokopedianow.category.domain.response.GetCategoryLayoutResponse.CategoryGetDetailModular
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetCategoryLayoutUseCase @Inject constructor(graphqlRepository: GraphqlRepository) {

    companion object {
        private const val CLIENT_TYPE_TOKONOW = "tokonow"
    }

    private val graphql by lazy { GraphqlUseCase<GetCategoryLayoutResponse>(graphqlRepository) }

    init {
        graphql.apply {
            setGraphqlQuery(GetCategoryLayout)
            setTypeClass(GetCategoryLayoutResponse::class.java)
        }
    }

    suspend fun execute(categoryId: String, ): CategoryGetDetailModular {
        graphql.setRequestParams(
            RequestParams.create().apply {
                putString(PARAM_IDENTIFIER, categoryId)
                putBoolean(PARAM_IS_LATEST_VERSION, true)
                putString(PARAM_CLIENT_TYPE, CLIENT_TYPE_TOKONOW)
            }.parameters
        )

        val getCategoryLayout = graphql.executeOnBackground()
        return getCategoryLayout.response
    }
}
