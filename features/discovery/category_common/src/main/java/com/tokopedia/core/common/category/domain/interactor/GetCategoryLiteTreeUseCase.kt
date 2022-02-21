package com.tokopedia.core.common.category.domain.interactor

import com.tokopedia.core.common.category.domain.model.CategoriesResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.observables.BlockingObservable
import javax.inject.Inject

class GetCategoryLiteTreeUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase) : UseCase<CategoriesResponse>() {

    companion object {
        @JvmStatic
        val unselectedCategoryId = -2
        private const val PARAM_FILTER = "filter"

        private val query by lazy {
            val filter = "\$filter"
            """
            query categoryAllListLite($filter: String!){
                categoryAllListLite(filter:$filter){
                    categories {
                        id
                        name
                        children{
                            id
                            name
                            children{
                                id
                                name
                            }
                        }
                    }
                }
            }
            """.trimIndent()
        }

        fun createRequestParams(): RequestParams {
            return RequestParams.create().apply {
                putString(PARAM_FILTER, "seller")
            }
        }
    }

    override fun createObservable(requestParams: RequestParams): Observable<CategoriesResponse> {
        val graphqlRequest = GraphqlRequest(query, CategoriesResponse::class.java, requestParams.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map { graphqlResponse ->
            graphqlResponse.getData(CategoriesResponse::class.java) as CategoriesResponse
        }
    }

    override fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

    fun getCategoryLiteData(requestParams: RequestParams): CategoriesResponse =
        createObservable(requestParams).toBlocking().first()
}