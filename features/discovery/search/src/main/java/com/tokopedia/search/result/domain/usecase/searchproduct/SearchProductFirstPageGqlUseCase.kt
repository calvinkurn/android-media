package com.tokopedia.search.result.domain.usecase.searchproduct

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant.GQL
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.*
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.result.domain.model.*
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.topads.sdk.domain.TopAdsParams
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1
import java.util.*

class SearchProductFirstPageGqlUseCase(
        private val graphqlUseCase: GraphqlUseCase,
        private val searchProductModelMapper: Func1<GraphqlResponse?, SearchProductModel?>
): UseCase<SearchProductModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<SearchProductModel> {
        val query = getQueryFromParameters(requestParams.parameters)
        val params = UrlParamUtils.generateUrlParamString(requestParams.parameters)
        val headlineParams = createHeadlineParams(requestParams.parameters)

        val graphqlRequestList = listOf(
                createAceSearchProductRequest(params = params),
                createQuickFilterRequest(query = query, params = params),
                createTopAdsProductRequest(params = params),
                createHeadlineAdsRequest(headlineParams = headlineParams),
                createGlobalSearchNavigationRequest(query = query, params = params),
                createSearchInspirationCarouselRequest(params = params),
                createSearchInspirationWidgetRequest(params = params)
        )

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequests(graphqlRequestList)

        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map(searchProductModelMapper)
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

    private fun createQuickFilterRequest(query: String, params: String) =
            GraphqlRequest(
                    QUICK_FILTER_QUERY,
                    QuickFilterModel::class.java,
                    mapOf(GQL.KEY_QUERY to query, GQL.KEY_PARAMS to params)
            )

    private fun createHeadlineAdsRequest(headlineParams: String) =
            GraphqlRequest(
                    HEADLINE_ADS_QUERY,
                    HeadlineAdsModel::class.java,
                    mapOf(GQL.KEY_HEADLINE_PARAMS to headlineParams)
            )

    private fun createGlobalSearchNavigationRequest(query: String, params: String) =
            GraphqlRequest(
                    GLOBAL_NAV_GQL_QUERY,
                    GlobalSearchNavigationModel::class.java,
                    mapOf(GQL.KEY_QUERY to query, GQL.KEY_PARAMS to params)
            )

    private fun createSearchInspirationCarouselRequest(params: String) =
            GraphqlRequest(
                    SEARCH_INSPIRATION_CAROUSEL_QUERY,
                    SearchInspirationCarouselModel::class.java,
                    mapOf(GQL.KEY_PARAMS to params)
            )

    private fun createSearchInspirationWidgetRequest(params: String) =
            GraphqlRequest(
                    SEARCH_INSPIRATION_WIDGET_QUERY,
                    SearchInspirationWidgetModel::class.java,
                    mapOf(GQL.KEY_PARAMS to params)
            )

    companion object {
        private const val HEADLINE_PRODUCT_COUNT = 3

        private const val QUICK_FILTER_QUERY = """
            query QuickFilter(${'$'}query: String!, ${'$'}params: String!) {
                quick_filter(query: ${'$'}query, extraParams: ${'$'}params) {
                    filter {
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
                        headline{
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
                            product {
                                id
                                name
                                price_format
                                applinks
                                product_rating
                                product_cashback
                                product_cashback_rate
                                product_new_label
                                count_review_format
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
                        badges{
                        image_url
                        show
                        title
                        }
                        button_text
                        promoted_text
                        description
                        uri
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
                searchInspirationCarousel(params: ${'$'}params) {
                    data {
                        title
                        type
                        position
                        layout
                        options {
                            title
                            url
                            applink
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