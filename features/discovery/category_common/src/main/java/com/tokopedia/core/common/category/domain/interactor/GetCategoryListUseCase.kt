package com.tokopedia.core.common.category.domain.interactor

import com.tokopedia.core.common.category.domain.model.CategoriesResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
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