package com.tokopedia.search.result.domain.usecase.searchproduct

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant.GQL
import com.tokopedia.discovery.common.constants.SearchConstant.HeadlineAds.HEADLINE_ITEM_VALUE_FIRST_PAGE
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_PARAMS
import com.tokopedia.discovery.common.reimagine.ReimagineRollence
import com.tokopedia.filter.common.helper.getSortFilterParamsString
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.result.domain.model.GlobalSearchNavigationModel
import com.tokopedia.search.result.domain.model.LastFilterModel
import com.tokopedia.search.result.domain.model.QuickFilterModel
import com.tokopedia.search.result.domain.model.SearchInspirationWidgetModel
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.UserProfileDobModel
import com.tokopedia.search.result.domain.usecase.InspirationCarouselQuery.createSearchInspirationCarouselRequest
import com.tokopedia.search.utils.SearchLogger
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.topads.sdk.domain.usecase.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel
import com.tokopedia.topads.sdk.utils.TopAdsHeadlineViewParams.createHeadlineParams
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rx.Emitter
import rx.Emitter.BackpressureMode.BUFFER
import rx.Observable
import rx.functions.Func1
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

private const val TDN_SEARCH_INVENTORY_ID = "2"
private const val TDN_SEARCH_ITEM_COUNT = 4
private const val TDN_SEARCH_DIMENSION = 3
private const val HEADLINE_IMPRESSION_COUNT_FIRST_PAGE = "0"

class SearchProductFirstPageGqlUseCase(
    private val graphqlUseCase: GraphqlUseCase,
    private val searchProductModelMapper: Func1<GraphqlResponse?, SearchProductModel?>,
    private val topAdsImageViewUseCase: TopAdsImageViewUseCase,
    private val coroutineDispatchers: CoroutineDispatchers,
    private val searchLogger: SearchLogger,
    private val reimagineRollence: ReimagineRollence,
): UseCase<SearchProductModel>(), CoroutineScope {

    private val masterJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = coroutineDispatchers.main + masterJob

    override fun createObservable(requestParams: RequestParams): Observable<SearchProductModel> {
        val searchProductParams = requestParams.parameters[SEARCH_PRODUCT_PARAMS] as Map<String?, Any?>

        val query = getQueryFromParameters(searchProductParams)
        val params = UrlParamUtils.generateUrlParamString(searchProductParams) + sreParams(
            reimagineRollence.search3ProductCard().isReimagineProductCard()
        )
        val headlineAdsParams = createHeadlineParams(
            requestParams.parameters[SEARCH_PRODUCT_PARAMS] as? Map<String, Any?>,
            HEADLINE_ITEM_VALUE_FIRST_PAGE,
            HEADLINE_IMPRESSION_COUNT_FIRST_PAGE
        ) + sreParams(
            reimagineRollence.search3ProductCard().isReimagineProductCard()
        )

        val graphqlRequestList = graphqlRequests {
            addAceSearchProductRequest(reimagineRollence, params)
            addQuickFilterRequest(query, params)
            addProductAdsRequest(requestParams, params)
            addHeadlineAdsRequest(requestParams, headlineAdsParams)
            addGlobalNavRequest(requestParams, query, params)
            addInspirationCarouselRequest(requestParams, params)
            addInspirationWidgetRequest(requestParams, params)
            addGetLastFilterRequest(requestParams, params)
            addUserProfileDobRequest(reimagineRollence)
        }

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequests(graphqlRequestList)

        val gqlSearchProductObservable = graphqlUseCase
            .createObservable(RequestParams.EMPTY)
            .map(searchProductModelMapper)
            .doOnNext {
                searchLogger.logSearchDebug(query, getSortFilterParamsString(searchProductParams))
            }

        val topAdsImageViewModelObservable = createTopAdsImageViewModelObservable(query, requestParams)

        return Observable.zip(
            gqlSearchProductObservable,
            topAdsImageViewModelObservable,
            ::setTopAdsImageViewModelList,
        )
    }

    private fun getQueryFromParameters(parameters: Map<String?, Any?>): String {
        return parameters[SearchApiConst.Q]?.toString() ?: ""
    }

    private fun MutableList<GraphqlRequest>.addQuickFilterRequest(query: String, params: String) {
        add(createQuickFilterRequest(query = query, params = params))
    }

    @GqlQuery("QuickFilter", QUICK_FILTER_QUERY)
    private fun createQuickFilterRequest(query: String, params: String): GraphqlRequest =
        GraphqlRequest(
            QuickFilter(),
            QuickFilterModel::class.java,
            mapOf(GQL.KEY_QUERY to query, GQL.KEY_PARAMS to params)
        )

    private fun MutableList<GraphqlRequest>.addGlobalNavRequest(requestParams: RequestParams, query: String, params: String) {
        if (!requestParams.isSkipGlobalNav()) {
            add(createGlobalSearchNavigationRequest(query = query, params = params))
        }
    }

    @GqlQuery("GlobalNav", GLOBAL_NAV_GQL_QUERY)
    private fun createGlobalSearchNavigationRequest(query: String, params: String): GraphqlRequest =
        GraphqlRequest(
            GlobalNav(),
            GlobalSearchNavigationModel::class.java,
            mapOf(GQL.KEY_QUERY to query, GQL.KEY_PARAMS to params)
        )

    private fun MutableList<GraphqlRequest>.addInspirationCarouselRequest(requestParams: RequestParams, params: String) {
        if (requestParams.isSkipInspirationCarousel()) return

        add(createSearchInspirationCarouselRequest(params = params))
    }

    private fun MutableList<GraphqlRequest>.addInspirationWidgetRequest(requestParams: RequestParams, params: String) {
        if (!requestParams.isSkipInspirationWidget()) {
            add(createSearchInspirationWidgetRequest(params = params))
        }
    }

    @GqlQuery("InspirationWidget", SEARCH_INSPIRATION_WIDGET_QUERY)
    private fun createSearchInspirationWidgetRequest(params: String): GraphqlRequest =
        GraphqlRequest(
            InspirationWidget(),
            SearchInspirationWidgetModel::class.java,
            mapOf(GQL.KEY_PARAMS to params)
        )

    private fun MutableList<GraphqlRequest>.addGetLastFilterRequest(
        requestParams: RequestParams,
        params: String,
    ) {
        if (!requestParams.isSkipGetLastFilterWidget())
            add(createGetLastFilterRequest(params = params))
    }

    @GqlQuery("GetLastFilter", GET_LAST_FILTER_GQL_QUERY)
    private fun createGetLastFilterRequest(params: String): GraphqlRequest =
        GraphqlRequest(
            GetLastFilter(),
            LastFilterModel::class.java,
            mapOf(GQL.KEY_PARAMS to params)
        )

    private fun createTopAdsImageViewModelObservable(
        query: String,
        requestParams: RequestParams,
    ): Observable<List<TopAdsImageUiModel>> {
        return if(requestParams.isSkipTdnBanner()) {
            Observable.just(emptyList())
        } else {
            Observable.create<List<TopAdsImageUiModel>>({ emitter ->
                try {
                    launch { emitTopAdsImageViewData(emitter, query) }
                } catch (throwable: Throwable) {
                    searchLogger.logTDNError(throwable)
                    emitter.onNext(listOf())
                }
            }, BUFFER).tdnTimeout()
        }
    }

    private suspend fun emitTopAdsImageViewData(emitter: Emitter<List<TopAdsImageUiModel>>, query: String) {
        withContext(coroutineDispatchers.io) {
            try {
                val topAdsImageViewModelList = topAdsImageViewUseCase.getImageData(
                    topAdsImageViewUseCase.getQueryMapSearch(query)
                )
                emitter.onNext(topAdsImageViewModelList)
                emitter.onCompleted()
            } catch (throwable: Throwable) {
                searchLogger.logTDNError(throwable)
                emitter.onNext(listOf())
            }
        }
    }

    private fun TopAdsImageViewUseCase.getQueryMapSearch(query: String) =
        getQueryMap(query, TDN_SEARCH_INVENTORY_ID, "", TDN_SEARCH_ITEM_COUNT, TDN_SEARCH_DIMENSION, "")

    private fun Observable<List<TopAdsImageUiModel>>.tdnTimeout(): Observable<List<TopAdsImageUiModel>> {
        val timeoutMs : Long = TDN_TIMEOUT

        return this.timeout(timeoutMs, TimeUnit.MILLISECONDS, Observable.create({ emitter ->
            searchLogger.logTDNError(RuntimeException("Timeout after $timeoutMs ms"))
            emitter.onNext(listOf())
        }, BUFFER))
    }

    private fun setTopAdsImageViewModelList(
        searchProductModel: SearchProductModel?,
        topAdsImageUiModelList: List<TopAdsImageUiModel>?
    ): SearchProductModel? {
        if (searchProductModel == null || topAdsImageUiModelList == null) return searchProductModel

        searchProductModel.setTopAdsImageViewModelList(topAdsImageUiModelList)

        return searchProductModel
    }

    @GqlQuery("UserProfileDob", USER_PROFILE_DOB_QUERY)
    private fun createUserProfileDobRequest(): GraphqlRequest =
        GraphqlRequest(
            UserProfileDob(),
            UserProfileDobModel::class.java,
        )

    private fun MutableList<GraphqlRequest>.addUserProfileDobRequest(
        reimagineRollence: ReimagineRollence,
    ) {
        if (reimagineRollence.search3ProductCard().isUseAceSearchProductV5())
            add(createUserProfileDobRequest())
    }

    override fun unsubscribe() {
        super.unsubscribe()

        if (isActive && !masterJob.isCancelled) masterJob.children.map { it.cancel() }
    }

    companion object {
        private const val TDN_TIMEOUT: Long = 2_000

        private const val QUICK_FILTER_QUERY = """
            query QuickFilter(${'$'}query: String!, ${'$'}params: String!) {
                quick_filter(query: ${'$'}query, extraParams: ${'$'}params) {
                    filter {
                        title
                        chip_name
                        options {
                            name
                            key
                            icon
                            value
                            is_new
                            input_type
                            total_data
                            val_max
                            val_min
                            hex_color 
                            image_url_active
                            image_url_inactive
                            child {
                                key
                                value
                                name
                                icon
                                input_type
                                total_data
                                child {
                                    key
                                    value
                                    name
                                    icon
                                    input_type
                                    total_data
                                }
                            }
                        }
                    }
                }
            }
        """

        private const val GLOBAL_NAV_GQL_QUERY = """
            query GlobalSearchNavigation(${'$'}query: String!, ${'$'}params: String!) {
                global_search_navigation(keyword:${'$'}query, device:"android", size:5, params:${'$'}params) {
                    data {
                        source
                        title
                        keyword
                        nav_template
                        background
                        see_all_applink
                        see_all_url
                        show_topads
                        tracking_option
                        component_id
                        info
                        list {
                            category_name
                            name
                            info
                            image_url
                            applink
                            url
                            subtitle
                            strikethrough
                            background_url
                            logo_url
                            component_id
                        }
                    }
                }
            }
        """

        private const val SEARCH_INSPIRATION_WIDGET_QUERY = """
            query SearchInspirationWidget(${'$'}params: String!) {
                searchInspirationWidget(params:${'$'}params){
                    data {
                        title
                        header_title
                        header_subtitle
                        layout
                        type
                        position
                        input_type
                        options {
                            text
                            img
                            url
                            color
                            applink
                            component_id
                            multi_filters {
                              title
                              key
                              name
                              value
                              val_min
                              val_max
                            }
                        }
                        tracking_option
                    }
                }
            }
        """

        private const val GET_LAST_FILTER_GQL_QUERY = """
            query GetLastFilter(${'$'}params:String!) {
              fetchLastFilter(param: ${'$'}params){
                data {
                  title
                  description
                  category_id_l2
                  applink
                  filters {
                    title
                    key
                    name
                    value
                  }
                  tracking_option
                  component_id
                }
              }
            }"""

        const val USER_PROFILE_DOB_QUERY = """
        query userProfileDob(){
            userProfileDob{
                userID
                age
                bday
                isDobExist
                isDobVerified
                isAdult
                error
            }
        }
    """
    }
}
