package com.tokopedia.search.result.domain.usecase.searchproduct

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant.GQL
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.*
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.topads.sdk.domain.TopAdsParams
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1
import java.util.*

class SearchProductFirstPageGqlUseCase(
        private val graphqlUseCase: GraphqlUseCase,
        private val searchProductModelMapper: Func1<GraphqlResponse, SearchProductModel>
): UseCase<SearchProductModel>() {

    private val graphqlRequest = GraphqlRequest(GQL_QUERY, SearchProductModel::class.java)

    override fun createObservable(requestParams: RequestParams): Observable<SearchProductModel> {
        val variables = createParametersForQuery(requestParams.parameters)

        graphqlRequest.variables = variables

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map(searchProductModelMapper)
    }

    private fun createParametersForQuery(parameters: Map<String, Any>): Map<String, Any> {
        val variables = mutableMapOf<String, Any>()

        variables[GQL.KEY_QUERY] = getQueryFromParameters(parameters)
        variables[GQL.KEY_PARAMS] = UrlParamUtils.generateUrlParamString(parameters)
        variables[GQL.KEY_HEADLINE_PARAMS] = createHeadlineParams(parameters)

        return variables
    }

    private fun getQueryFromParameters(parameters: Map<String, Any>): Any {
        val query = parameters[SearchApiConst.Q]
        return query ?: ""
    }

    private fun createHeadlineParams(parameters: Map<String, Any>): String {
        val headlineParams = HashMap(parameters)

        headlineParams[TopAdsParams.KEY_EP] = HEADLINE
        headlineParams[TopAdsParams.KEY_TEMPLATE_ID] = HEADLINE_TEMPLATE_VALUE
        headlineParams[TopAdsParams.KEY_ITEM] = HEADLINE_ITEM_VALUE
        headlineParams[TopAdsParams.KEY_HEADLINE_PRODUCT_COUNT] = HEADLINE_PRODUCT_COUNT

        return UrlParamUtils.generateUrlParamString(headlineParams)
    }
    
    companion object {
        private const val HEADLINE_PRODUCT_COUNT = 3

        private const val GQL_QUERY = """
            query SearchProduct(
                    ${'$'}params: String!,
                    ${'$'}query: String!,
                    ${'$'}headline_params: String
            ) {
                ace_search_product_v4(params: ${'$'}params) {
                    header {
                        totalData
                        totalDataText
                        defaultView
                        responseCode
                        errorMessage
                        additionalParams
                        keywordProcess
                    }
                    data {
                        isQuerySafe
                        autocompleteApplink
                        redirection {
                            redirectApplink
                        }
                        ticker {
                            text
                            query
                            typeId
                        }
                        related {
                            relatedKeyword
                            position
                            otherRelated {
                                keyword
                                url
                                applink
                                product {
                                    id
                                    name
                                    price
                                    imageUrl
                                    rating
                                    countReview
                                    url
                                    applink
                                    priceStr
                                    wishlist
                                    shop {
                                        city
                                    }
                                    badges {
                                        imageUrl
                                        show
                                    }
                                    freeOngkir {
                                        isActive
                                        imgUrl
                                    }
                                }
                            }
                        }
                        suggestion {
                            suggestion
                            query
                            text
                        }
                        products {
                            id
                            name
                            ads {
                                id
                                productClickUrl
                                productWishlistUrl
                                productViewUrl
                            }
                            shop {
                                id
                                name
                                city
                                rating_average
                            }
                            freeOngkir {
                                isActive
                                imgUrl
                            }
                            imageUrl
                            imageUrl300
                            imageUrl700
                            price
                            priceInt
                            priceRange
                            categoryBreadcrumb
                            rating
                            ratingAverage
                            countReview
                            priceInt
                            originalPrice
                            discountPercentage
                            warehouseIdDefault
                            boosterList
                            source_engine
                            labelGroups {
                                title
                                position
                                type
                            }
                            badges {
                                title
                                imageUrl
                                show
                            }
                            wishlist
                            count_sold
                        }
                    }
                }
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
            
                productAds: displayAdsV3(displayParams: ${'$'}params) {
                    status {
                        error_code
                        message
                    }
                    header {
                        process_time
                        total_data
                    }
                    data{
                        id
                        ad_ref_key
                        redirect
                        sticker_id
                        sticker_image
                        product_click_url
                        product_wishlist_url
                        shop_click_url
                        product {
                            id
                            name
                            category_breadcrumb
                            wishlist
                            image {
                                m_url
                                s_url
                                xs_url
                                m_ecs
                                s_ecs
                                xs_ecs
                            }
                            uri
                            relative_uri
                            price_format
                            wholesale_price {
                                price_format
                                quantity_max_format
                                quantity_min_format
                            }
                            count_talk_format
                            count_review_format
                            category {
                                id
                            }
                            product_preorder
                            product_wholesale
                            free_return
                            product_cashback
                            product_new_label
                            product_cashback_rate
                            product_rating
                            product_rating_format
                            free_ongkir {
                              is_active
                              img_url
                            }
                            campaign {
                                original_price
                                discount_percentage
                            }
                            label_group {
                                position
                                type
                                title
                            }
                        }
                        shop{
                            id
                            name
                            domain
                            location
                            city
                            gold_shop
                            gold_shop_badge
                            lucky_shop
                            uri
                            owner_id
                            is_owner
                            shop_is_official
                            badges {
                                title
                                image_url
                                show
                            }
                        }
                        applinks
                    }
                    template {
                        is_ad
                    }
                }
            
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
            }"""
    }
}