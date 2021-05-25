package com.tokopedia.search.result.domain.usecase.searchproduct

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant.GQL
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.*
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.result.domain.model.*
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

class SearchProductFirstPageGqlUseCase(
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
        val searchProductParams = requestParams.parameters[SEARCH_PRODUCT_PARAMS] as Map<String?, Any?>

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

    private fun getQueryFromParameters(parameters: Map<String?, Any?>): String {
        return parameters[SearchApiConst.Q]?.toString() ?: ""
    }

    private fun createHeadlineParams(parameters: Map<String?, Any?>): String {
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

    private fun MutableList<GraphqlRequest>.addHeadlineAdsRequest(requestParams: RequestParams, searchProductParams: Map<String?, Any?>) {
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

    companion object {
        private const val HEADLINE_PRODUCT_COUNT = 3

        private const val QUICK_FILTER_QUERY = """
            query QuickFilter(${'$'}query: String!, ${'$'}params: String!) {
                quick_filter(query: ${'$'}query, extraParams: ${'$'}params) {
                    filter {
                        title
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

        private const val HEADLINE_ADS_QUERY = """
            query HeadlineAds(${'$'}headline_params: String!) {
                headlineAds: displayAdsV3(displayParams: ${'$'}headline_params) {
                    status {
                        error_code
                        message
                    }
                    header {
                        process_time
                        total_data
                    }
                    data {
                        id
                        ad_ref_key
                        redirect
                        ad_click_url
                        headline {
                            template_id
                            name
                            image {
                                full_url
                                full_ecs
                            }
                            shop {
                                id
                                name
                                domain
                                tagline
                                slogan
                                location
                                city
                                gold_shop
                                gold_shop_badge
                                shop_is_official
                                pm_pro_shop
                                merchant_vouchers
                                product {
                                    id
                                    name
                                    price_format
                                    applinks
                                    product_cashback
                                    product_cashback_rate
                                    product_new_label
                                    count_review_format
                                    rating_average
                                    label_group {
                                        title
                                        type
                                        position
                                    }
                                    image_product{
                                        product_id
                                        product_name
                                        image_url
                                        image_click_url
                                    }
                                    campaign {
                                        original_price
                                        discount_percentage
                                    }
                                }
                                image_shop {
                                    cover
                                    s_url
                                    xs_url
                                    cover_ecs
                                    s_ecs
                                    xs_ecs
                                }
                            }
                            badges {
                                image_url
                                show
                                title
                            }
                            button_text
                            promoted_text
                            description
                            uri
                            layout
                            position
                        }
                        applinks
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
                        }
                    }
                }
            }
        """

        private const val SEARCH_INSPIRATION_CAROUSEL_QUERY = """
            query SearchInspirationCarousel(${'$'}params: String!) {
                searchInspirationCarouselV2(params: ${'$'}params) {
                    data {
                        title
                        type
                        position
                        layout
                        options {
                            title
                            url
                            applink
                            banner_image_url
                            banner_link_url
                            banner_applink_url
                            identifier
                            product {
                                id
                                name
                                price
                                price_str
                                image_url
                                rating
                                count_review
                                url
                                applink
                                description
                                rating_average
                                label_groups {
                                    title
                                    type
                                    position
                                    url
                                }
                                original_price
                                discount_percentage
                              	badges {
                                    title
                                    image_url
                                    show
                                }
                              	shop {
                                    city
                                }
                            }
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
                        type
                        position
                        options {
                            text
                            img
                            url
                            color
                            applink
                        }
                    }
                }
            }
        """
    }
}