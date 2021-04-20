package com.tokopedia.entertainment.common.util

object EventQuery {
    fun getEventHomeQuery() = """
            query EventCategories(${'$'}category:String!){
        event_child_category(categoryName: "event", categoryID: 1, verticalID: 3){
            categories{
                id
                name
                title
                media_url
                app_url
                priority
            }
        }
        event_location_search(
                searchParams: [
                {key: "size", value: "8"}, {key: "page_no", value: "1"},
                {key: "category_id", value: "1"}]) {
            locations {
                id
                name
                coordinates
                city_id
                priority
                image_app
                address
            }
            count
        }
        event_home(category: ${'$'}category) {
            layout {
                id
                title
                category_url
                is_card
                app_url
                items {
                    id
                    rating
                    display_name
                    title
                    image_app
                    sales_price
                    city_name
                    price
                    location
                    schedule
                    app_url
                    url
                    is_promo
                    seo_url
                }
            }
        }
    }"""


    fun mutationEventCheckoutV2()="""
            mutation checkout_general_v2(${'$'}params:CheckoutGeneralV2Params!){
        checkout_general_v2(params:${'$'}params) {
            header {
                process_time
                reason
                error_code
            }
            data {
                success
                error
                error_state
                message
                data{
                    callback_url
                    parameter{
                        amount
                    }
                    price_validation{
                        is_updated
                        message{
                            action
                            desc
                            title
                        }
                    }
                    product_list{
                        id
                        name
                        price
                        quantity
                    }
                    query_string
                    redirect_url
                }
            }
            status
            error_reporter {
                eligible
            }
        }
    }"""

    fun mutationEventCheckoutInstant()="""
        mutation checkout_general_v2_instant(${'$'}params: CheckoutGeneralV2InstantParams) {
            checkout_general_v2_instant(params: ${'$'}params){
                header {
                    process_time
                    reason
                    messages
                    error_code
                 }
                data {
                    success
                    error
                    error_state
                    message
                    data {
                        redirect_url
                        method
                        content_type
                        payload
                    }
                }
                status
                error_reporter {
                eligible
                texts {
                submit_title
                submit_description
                submit_button
                cancel_button
                  }
             }
             }
        }"""

    fun mutationVerifyV2()="""
        mutation verify_v2(${'$'}eventVerify: VerifyRequest!) {
        event_verify(verifyRequestParam: ${'$'}eventVerify)
        {
            error
            error_description
            status
            metadata{
                product_ids
                product_names
                provider_ids
                item_ids
                category_name
                quantity
                total_price
                item_map{
                    id
                    name
                    product_id
                    category_id
                    child_category_ids
                    provider_id
                    product_name
                    package_name
                    end_time
                    start_time
                    price
                    quantity
                    total_price
                    location_name
                    location_desc
                    product_app_url
                    web_app_url
                    product_web_url
                    product_image
                    flag_id
                    package_id
                    order_trace_id
                    error
                    email
                    mobile
                    schedule_timestamp
                    description
                    provider_ticket_id
                    provider_schedule_id
                    base_price
                    commission
                    commission_type
                    currency_price
                    passenger_forms{
                        passenger_informations{
                            id
                            product_id
                            name
                            title
                            value
                            element_type
                            help_text
                            required
                            validator_regex
                            error_message
                        }
                    }
                    invoice_item_id
                    provider_order_id
                    provider_invoice_code
                    provider_package_id
                    invoice_id
                    provider_invoice_code
                    invoice_status
                    payment_type
                    buttons{
                        label
                        action
                        color
                        text_color
                        body{
                            app_url
                            web_url
                            method
                            voucher_codes
                        }
                        metadata{
                            key
                            value
                        }
                    }
                }
                error
                order_title
                order_subTitle
                buttons{
                    label
                    action
                    color
                    text_color
                    border_color
                    body{
                        app_url
                        web_url
                        method
                        voucher_codes
                    }
                    metadata{
                        key
                        value
                    }
                }
            }
            gateway_code
        }
    }"""


    fun eventContentById() ="""
        query EventContentById(${'$'}typeValue:String!)
    {
        event_content_by_id(QueryInput:{typeID: "4", typeValue: ${'$'}typeValue})
        {
            data{
                title,
                meta_description,
                meta_title,
                meta_index,
                meta_follow,
                section_data {
                    priority
                    section
                    content {
                        id
                        content_section_id
                        priority
                        content_type_id
                        value_image {
                            url
                            redirect_url
                        }
                        value_header_image {
                            header_text
                            columns {
                                image_url
                                title
                                description
                            }
                            footer_text
                        }
                        value_accordion{
                            title
                            content
                        }
                        value_text
                        value_label_bullet{
                            label
                            url
                        }
                        value_image_bullet{
                            image_url
                            url
                            name
                        }
                        value_image {
                            url
                            redirect_url
                        }
                        value_carousel {
                            url
                            redirect_url
                        }
                        value_bullet_list {
                            bullet
                            title
                            description
                        }
                    }
                }
            }
        }
    }"""

    fun eventPDPV3() = """query EventProductDetail(${'$'}urlPDP:String!){
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
    }"""


    fun eventQueryFullLocation() = """query searchEventLocation{
        event_location_search(searchParams:[
                {
                    key: "category_id",
                    value: "1"
                },
                {
                    key:"name",
                    value:""},
                {
                    key: "sort_by",
                    value: "priority"
                },
                {
                    key: "order",
                    value: "desc"
                },
                {
                    key: "size",
                    value: "20"
                }
        ])
        {
            locations {
                id
                name
                search_name
                city_id
                city_name
                icon_app
                image_app
            }
            count
        }
    }"""

    fun getEventHistory() = """query{
        TravelCollectiveRecentSearches(product:EVENTS){
            items {
                product
                title
                subtitle
                prefix
                prefixStyling
                value
                appURL
                imageURL
            }
        }
    }"""

    fun getEventSearchLocation() = """query searchEventLocation(${'$'}search_query:String!){
        event_location_search(searchParams:[
                {
                    key: "category_id",
                    value: "1"
                },
                {
                    key:"name",
                    value:${'$'}search_query},
                {
                    key: "sort_by",
                    value: "priority"
                },
                {
                    key: "order",
                    value: "desc"
                },
                {
                    key: "size",
                    value: "2"
                }
        ])
        {
            locations {
                id
                name
                search_name
                city_id
                city_name
                icon_app
                image_app
            }
            count
        }
        event_search(searchParams: [
                {key: "category", value: "event"},
                {key: "page_size", value: "5"},
                {key: "tags", value:${'$'}search_query}
        ]) {
            products {
                id
                category_id
                child_category_ids
                city_ids
                display_name
                title
                action_text
                genre
                duration
                url
                seo_url
                image_app
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
                min_start_time
                max_end_time
                sale_start_time
                sale_end_time
                date_range
                city_name
                remaining_sale_time
                rating
                likes
                is_liked
                saving
                saving_percentage
                recommendation_url
                message
                buy_enabled
                has_popup
                no_promo
                price
                location
                schedule
                web_url
                app_url
            }
            count
        }
    }"""


    fun getEventSearchCategory() = """query searchEventCategory(${'$'}category_ids:String!, ${'$'}cities:String!, ${'$'}page:String!){
        event_child_category(categoryName: "event", categoryID: 1, verticalID: 3){
            categories{
                id
                name
                title
                media_url
                app_url
                priority
            }
        }
        event_search(searchParams: [
                {key: "category", value: "event"},
                {key: "page", value: ${'$'}page},
                {key: "page_size", value: "20"},
                {key: "cities", value: ${'$'}cities},
                {key: "child_category_ids", value: ${'$'}category_ids}
        ]) {
            products {
                id
                category_id
                child_category_ids
                city_ids
                display_name
                title
                action_text
                genre
                duration
                url
                seo_url
                image_app
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
                min_start_time
                max_end_time
                sale_start_time
                sale_end_time
                date_range
                city_name
                remaining_sale_time
                rating
                likes
                is_liked
                saving
                saving_percentage
                recommendation_url
                message
                buy_enabled
                has_popup
                no_promo
                price
                location
                schedule
                web_url
                app_url
            }
            count
        }
    }"""

}