package com.tokopedia.tokomart.search.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tokomart.search.domain.model.SearchModel
import com.tokopedia.tokomart.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.usecase.coroutines.UseCase

class GetSearchFirstPageUseCase(
        private val graphqlUseCase: MultiRequestGraphqlUseCase
): UseCase<SearchModel>() {

    override suspend fun executeOnBackground(): SearchModel {
        graphqlUseCase.addRequest(GraphqlRequest(
                GQL_QUERY,
                AceSearchProductModel::class.java,
                mapOf()
        ))

        val graphqlResponse = graphqlUseCase.executeOnBackground()

        return SearchModel(
                searchProduct = graphqlResponse.getData<AceSearchProductModel?>(AceSearchProductModel::class.java)?.searchProduct ?: AceSearchProductModel.SearchProduct()
        )
    }

    companion object {

        private const val GQL_QUERY = """
            query aceSearchProductV4 {
              ace_search_product_v4(params:"st=product&q=gtx+1050&ob=23&page=1&device=android&use_page=true&source=search"){
                data {
                  products {
                    imageUrl300
                    id
                    name
                    price
                  }
                }
              }
            }
        """
    }
}