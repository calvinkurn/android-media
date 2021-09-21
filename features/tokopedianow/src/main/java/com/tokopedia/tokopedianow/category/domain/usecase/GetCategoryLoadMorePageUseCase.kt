package com.tokopedia.tokopedianow.category.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.tokopedianow.category.domain.model.CategoryModel
import com.tokopedia.tokopedianow.searchcategory.data.createAceSearchProductRequest
import com.tokopedia.tokopedianow.searchcategory.data.getTokonowQueryParam
import com.tokopedia.tokopedianow.searchcategory.data.mapper.getSearchProduct
import com.tokopedia.usecase.coroutines.UseCase

class GetCategoryLoadMorePageUseCase(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
): UseCase<CategoryModel>() {

    override suspend fun executeOnBackground(): CategoryModel {
        val queryParams = getTokonowQueryParam(useCaseRequestParams)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(createAceSearchProductRequest(queryParams))

        val graphqlResponse = graphqlUseCase.executeOnBackground()

        return CategoryModel(
                searchProduct = getSearchProduct(graphqlResponse)
        )
    }
}