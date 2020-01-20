package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.home.beranda.domain.gql.ProductrevDismissSuggestion
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class DismissHomeReviewUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase,
                                                   @Named("dismiss_raw") private val rawQuery: String) :
        UseCase<String>() {

    override fun createObservable(requestParams: RequestParams?): Observable<String> {
        val graphqlRequest = GraphqlRequest(rawQuery, ProductrevDismissSuggestion::class.java, RequestParams.EMPTY.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map {
            val data: ProductrevDismissSuggestion? = it.getData(ProductrevDismissSuggestion::class.java)
                    ?: ProductrevDismissSuggestion()
            val error: List<GraphqlError> = it.getError(GraphqlError::class.java) ?: listOf()

            if (error.isNotEmpty() && error.first().message.isNotEmpty()) {
                throw MessageErrorException(error.first().message)
            }

            data?.statusDismiss
        }
    }
}