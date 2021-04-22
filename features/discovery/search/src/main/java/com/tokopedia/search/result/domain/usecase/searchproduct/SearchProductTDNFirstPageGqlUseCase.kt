package com.tokopedia.search.result.domain.usecase.searchproduct

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant.GQL
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.*
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.result.domain.model.*
import com.tokopedia.search.result.domain.usecase.searchproduct.SearchProductFirstPageGqlUseCase.Companion.GLOBAL_NAV_GQL_QUERY
import com.tokopedia.search.result.domain.usecase.searchproduct.SearchProductFirstPageGqlUseCase.Companion.HEADLINE_ADS_QUERY
import com.tokopedia.search.result.domain.usecase.searchproduct.SearchProductFirstPageGqlUseCase.Companion.HEADLINE_PRODUCT_COUNT
import com.tokopedia.search.result.domain.usecase.searchproduct.SearchProductFirstPageGqlUseCase.Companion.QUICK_FILTER_QUERY
import com.tokopedia.search.result.domain.usecase.searchproduct.SearchProductFirstPageGqlUseCase.Companion.SEARCH_INSPIRATION_CAROUSEL_QUERY
import com.tokopedia.search.result.domain.usecase.searchproduct.SearchProductFirstPageGqlUseCase.Companion.SEARCH_INSPIRATION_WIDGET_QUERY
import com.tokopedia.search.utils.SearchLogger
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.topads.sdk.domain.TopAdsParams
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import kotlinx.coroutines.*
import rx.Emitter
import rx.Emitter.BackpressureMode.BUFFER
import rx.Observable
import rx.functions.Func1
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

private const val TDN_SEARCH_INVENTORY_ID = "2"
private const val TDN_SEARCH_ITEM_COUNT = 4
private const val TDN_SEARCH_DIMENSION = 3

class SearchProductTDNFirstPageGqlUseCase(
        private val graphqlUseCase: GraphqlUseCase,
        private val searchProductModelMapper: Func1<GraphqlResponse?, SearchProductModel?>,
        private val topAdsImageViewUseCase: TopAdsImageViewUseCase,
        private val coroutineDispatchers: CoroutineDispatchers,
        private val searchLogger: SearchLogger
): UseCase<SearchProductModel>(), CoroutineScope {

    private val masterJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = coroutineDispatchers.main + masterJob

    override fun createObservable(requestParams: RequestParams): Observable<SearchProductModel> {
        val searchProductParams = requestParams.parameters[SEARCH_PRODUCT_PARAMS] as Map<String, Any>

        val query = getQueryFromParameters(searchProductParams)
        val params = UrlParamUtils.generateUrlParamString(searchProductParams)

        val graphqlRequestList = graphqlRequests {
            addAceSearchProductRequest(params)
            addQuickFilterRequest(query, params)
            addProductAdsRequest(requestParams, params)
            addHeadlineAdsRequest(requestParams, searchProductParams)
            addGlobalNavRequest(requestParams, query, params)
            addInspirationCarouselRequest(requestParams, params)
            addInspirationWidgetRequest(requestParams, params)
        }

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequests(graphqlRequestList)

        val gqlSearchProductObservable = graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map(searchProductModelMapper)

        val topAdsImageViewModelObservable = createTopAdsImageViewModelObservable(query)

        return Observable.zip(gqlSearchProductObservable, topAdsImageViewModelObservable, this::setTopAdsImageViewModelList)
    }

    private fun getQueryFromParameters(parameters: Map<String, Any>): String {
        return parameters[SearchApiConst.Q]?.toString() ?: ""
    }

    private fun createHeadlineParams(parameters: Map<String, Any>): String {
        val headlineParams = HashMap(parameters)

        headlineParams[TopAdsParams.KEY_EP] = HEADLINE
        headlineParams[TopAdsParams.KEY_TEMPLATE_ID] = HEADLINE_TEMPLATE_VALUE
        headlineParams[TopAdsParams.KEY_ITEM] = HEADLINE_ITEM_VALUE
        headlineParams[TopAdsParams.KEY_HEADLINE_PRODUCT_COUNT] = HEADLINE_PRODUCT_COUNT

        return UrlParamUtils.generateUrlParamString(headlineParams)
    }

    private fun MutableList<GraphqlRequest>.addQuickFilterRequest(query: String, params: String) {
        add(createQuickFilterRequest(query = query, params = params))
    }

    private fun createQuickFilterRequest(query: String, params: String) =
            GraphqlRequest(
                    QUICK_FILTER_QUERY,
                    QuickFilterModel::class.java,
                    mapOf(GQL.KEY_QUERY to query, GQL.KEY_PARAMS to params)
            )

    private fun MutableList<GraphqlRequest>.addHeadlineAdsRequest(requestParams: RequestParams, searchProductParams: Map<String, Any>) {
        if (!requestParams.isSkipHeadlineAds()) {
            val headlineParams = createHeadlineParams(searchProductParams)
            add(createHeadlineAdsRequest(headlineParams = headlineParams))
        }
    }

    private fun createHeadlineAdsRequest(headlineParams: String) =
            GraphqlRequest(
                    HEADLINE_ADS_QUERY,
                    HeadlineAdsModel::class.java,
                    mapOf(GQL.KEY_HEADLINE_PARAMS to headlineParams)
            )

    private fun MutableList<GraphqlRequest>.addGlobalNavRequest(requestParams: RequestParams, query: String, params: String) {
        if (!requestParams.isSkipGlobalNav()) {
            add(createGlobalSearchNavigationRequest(query = query, params = params))
        }
    }

    private fun createGlobalSearchNavigationRequest(query: String, params: String) =
            GraphqlRequest(
                    GLOBAL_NAV_GQL_QUERY,
                    GlobalSearchNavigationModel::class.java,
                    mapOf(GQL.KEY_QUERY to query, GQL.KEY_PARAMS to params)
            )

    private fun MutableList<GraphqlRequest>.addInspirationCarouselRequest(requestParams: RequestParams, params: String) {
        if (!requestParams.isSkipInspirationCarousel()) {
            add(createSearchInspirationCarouselRequest(params = params))
        }
    }

    private fun createSearchInspirationCarouselRequest(params: String) =
            GraphqlRequest(
                    SEARCH_INSPIRATION_CAROUSEL_QUERY,
                    SearchInspirationCarouselModel::class.java,
                    mapOf(GQL.KEY_PARAMS to params)
            )

    private fun MutableList<GraphqlRequest>.addInspirationWidgetRequest(requestParams: RequestParams, params: String) {
        if (!requestParams.isSkipInspirationWidget()) {
            add(createSearchInspirationWidgetRequest(params = params))
        }
    }

    private fun createSearchInspirationWidgetRequest(params: String) =
            GraphqlRequest(
                    SEARCH_INSPIRATION_WIDGET_QUERY,
                    SearchInspirationWidgetModel::class.java,
                    mapOf(GQL.KEY_PARAMS to params)
            )

    private fun createTopAdsImageViewModelObservable(query: String): Observable<List<TopAdsImageViewModel>> {
        return Observable.create<List<TopAdsImageViewModel>>({ emitter ->
            try {
                launch { emitTopAdsImageViewData(emitter, query) }
            }
            catch (throwable: Throwable) {
                searchLogger.logTDNError(throwable)
                emitter.onNext(listOf())
            }
        }, BUFFER).tdnTimeout()
    }

    private suspend fun emitTopAdsImageViewData(emitter: Emitter<List<TopAdsImageViewModel>>, query: String) {
        withContext(coroutineDispatchers.io) {
            try {
                val topAdsImageViewModelList = topAdsImageViewUseCase.getImageData(
                        topAdsImageViewUseCase.getQueryMapSearch(query)
                )
                emitter.onNext(topAdsImageViewModelList)
                emitter.onCompleted()
            }
            catch (throwable: Throwable) {
                searchLogger.logTDNError(throwable)
                emitter.onNext(listOf())
            }
        }
    }

    private fun TopAdsImageViewUseCase.getQueryMapSearch(query: String) =
            getQueryMap(query, TDN_SEARCH_INVENTORY_ID, "", TDN_SEARCH_ITEM_COUNT, TDN_SEARCH_DIMENSION, "")

    private fun Observable<List<TopAdsImageViewModel>>.tdnTimeout(): Observable<List<TopAdsImageViewModel>> {
        val timeoutMs : Long = 2_000

        return this.timeout(timeoutMs, TimeUnit.MILLISECONDS, Observable.create({ emitter ->
            searchLogger.logTDNError(RuntimeException("Timeout after $timeoutMs ms"))
            emitter.onNext(listOf())
        }, BUFFER))
    }

    private fun setTopAdsImageViewModelList(
            searchProductModel: SearchProductModel?,
            topAdsImageViewModelList: List<TopAdsImageViewModel>?
    ): SearchProductModel? {
        if (searchProductModel == null || topAdsImageViewModelList == null) return searchProductModel

        searchProductModel.setTopAdsImageViewModelList(topAdsImageViewModelList)

        return searchProductModel
    }

    override fun unsubscribe() {
        super.unsubscribe()

        if (isActive && !masterJob.isCancelled) masterJob.children.map { it.cancel() }
    }
}