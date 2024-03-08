package com.tokopedia.search.result.domain.usecase.getpostatccarousel

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.result.domain.model.SearchInspirationCarouselModel
import com.tokopedia.search.result.domain.model.SearchProductModel.SearchInspirationCarousel
import com.tokopedia.search.result.domain.usecase.InspirationCarouselQuery.createSearchInspirationCarouselRequest
import com.tokopedia.search.result.domain.usecase.searchproduct.sreParams
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

class GetPostATCCarouselUseCase(
    private val graphqlUseCase: GraphqlUseCase,
): UseCase<SearchInspirationCarousel>() {

    override fun createObservable(requestParams: RequestParams): Observable<SearchInspirationCarousel> =
        graphqlUseCase.run {
            val params = UrlParamUtils.generateUrlParamString(requestParams.parameters) + sreParams()
            val graphqlRequest = createSearchInspirationCarouselRequest(params)

            clearRequest()
            addRequest(graphqlRequest)
            createObservable(RequestParams.EMPTY).map { searchInspirationCarousel(it) }
        }

    private fun searchInspirationCarousel(graphqlResponse: GraphqlResponse) =
        graphqlResponse
            .getData<SearchInspirationCarouselModel?>(SearchInspirationCarouselModel::class.java)
            ?.searchInspirationCarousel
            ?: SearchInspirationCarousel()
}
