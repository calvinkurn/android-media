package com.tokopedia.deals.pdp.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object DealsPDPDetailQuery: GqlQueryInterface {
    private const val OPERATION_NAME = "EventProductDetail"
    private val QUERY = """
        query $OPERATION_NAME(${'$'}urlPDP:String!){
        event_product_detail_v3(URL:${'$'}urlPDP) {
            productDetailData {
                id
                parent_id
                brand_id
                category_id
                provider_id
                checkout_business_type
                checkout_data_type
                child_category_ids
                provider_product_id
                provider_product_code
                provider_product_name
                display_name
                title
                action_text
                censor
                genre
                duration
                url
                seo_url
                image_web
                thumbnail_web
                image_app
                thumbnail_app
                seatmap_image
                location
                app_url
                web_url
                tnc
                offer_text
                short_desc
                long_rich_desc
                salient_features
                meta_title
                meta_description
                meta_keywords
                search_tags
                display_tags
                promotion_text
                autocode
                convenience_fee
                mrp
                sales_price
                seat_chart_type
                has_seat_layout
                form
                priority
                quantity
                sold_quantity
                sell_rate
                thumbs_up
                thumbs_down
                is_featured
                is_promo
                is_food_available
                is_searchable
                is_top
                use_pdf
                status
                redirect
                min_start_date
                max_end_date
                sale_start_date
                sale_end_date
                custom_labels
                custom_text_1
                custom_text_2
                custom_text_3
                custom_text_4
                custom_text_5
                created_at
                updated_at
                min_start_time
                max_end_time
                sale_start_time
                sale_end_time
                date_range
                dates
                packages{
                    id
                    name
                    product_id
                    provider_package_id
                    provider_package_name
                    description
                    status
                    start_date
                    sales_price
                    dates
                    end_date
                    forms_package {
                        id
                        product_id
                        options
                        name
                        title
                        value
                        element_type
                        help_text
                        required
                        validator_regex
                        error_message
                        status
                        created_at
                        updated_at
                    }
                    package_items{
                        id
                        product_id
                        product_package_id
                        provider_schedule_id
                        provider_ticket_id
                        name
                        description
                        tnc
                        convenience_fee
                        mrp
                        sales_price
                        available
                        provider_meta_data
                        provider_status
                        status
                        dates
                        min_qty
                        max_qty
                        start_date
                        end_date
                        provider_custom_text
                        forms_item {
                           id
                           product_id
                           options
                            name
                           title
                           value
                           element_type
                           help_text
                           required
                           validator_regex
                           error_message
                           status
                           created_at
                           updated_at
                        }
                    }
                }
                facilities{
                    id
                    title
                    description
                    icon_url
                    type
                    priority
                    status
                }
                city_name
                forms {
                    id
                    product_id
                    options
                    name
                    title
                    value
                    element_type
                    help_text
                    required
                    validator_regex
                    error_message
                    status
                    created_at
                    updated_at
                }
                media {
                    id
                    product_id
                    title
                    is_thumbnail
                    type
                    description
                    url
                    client
                    status
                    created_at
                    updated_at
                }
                outlets {
                    id
                    product_id
                    location_id
                    name
                    search_name
                    meta_title
                    meta_description
                    meta_keywords
                    district
                    gmap_address
                    neighbourhood
                    coordinates
                    state
                    country
                    is_searchable
                    location_status
                    priority
                    created_at
                    updated_at
                }
                remaining_sale_time
                rating
                likes
                is_liked
                catalog {
                    digital_category_id
                    digital_product_id
                    digital_product_code
                }
                saving
                saving_percentage
                recommendation_url
                brand {
                    title
                    featured_image
                    featured_thumbnail_image
                    city_name
                    seo_url
                }
                category {
                    id
                    title
                    media_url
                    url
                }
                message
                code
                message_error
            }
        }
    }
    """.trimIndent()

    private const val PRODUCT_ID_KEY = "urlPDP"

    @JvmStatic
    fun createRequestParam(productId: String) = HashMap<String, Any>().apply {
        put(PRODUCT_ID_KEY, productId)
    }

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
    override fun getQuery(): String = QUERY
    override fun getTopOperationName(): String = OPERATION_NAME
}