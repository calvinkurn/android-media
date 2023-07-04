package com.tokopedia.product.manage.common.feature.category.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.manage.common.feature.category.model.CategoriesResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetCategoryListUseCase @Inject constructor(
        repository: GraphqlRepository
) : GraphqlUseCase<CategoriesResponse>(repository) {

    companion object {
        const val PARAM_FILTER = "filter"
        private val query by lazy {
            val filter = "\$filter"
            """
            query categoryAllListLite($filter: String!){
                categoryAllListLite(filter:$filter){
                    categories {
                        id
                        name
                    }
                }
            }
            """.trimIndent()
        }
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(CategoriesResponse::class.java)
    }

    fun setParams(filter: String = "seller") {
        val requestParams = RequestParams.create()
        requestParams.putString(PARAM_FILTER, filter)
        setRequestParams(requestParams.parameters)
    }
}
