package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class GetHomeReviewSuggestedUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase,
                                                        @Named("suggested_review") private val rawQuery: String) : UseCase<SuggestedProductReview>() {

    override fun createObservable(requestParams: RequestParams): Observable<SuggestedProductReview> {
        val graphqlRequest = GraphqlRequest(rawQuery, SuggestedProductReview::class.java, RequestParams.EMPTY.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map {
            val data: SuggestedProductReview? = it.getData(SuggestedProductReview::class.java)
                    ?: SuggestedProductReview()
            val error: List<GraphqlError> = it.getError(GraphqlError::class.java) ?: listOf()

            if (data == null) {
                throw RuntimeException()
            } else if (error.isNotEmpty() && error.first().message.isNotEmpty()) {
                throw MessageErrorException(error.first().message)
            }

            data
        }
    }
}