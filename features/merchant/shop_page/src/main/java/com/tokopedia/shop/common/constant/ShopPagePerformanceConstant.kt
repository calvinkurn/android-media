package com.tokopedia.shop.common.constant

object ShopPagePerformanceConstant {
    const val SHOP_HEADER_TRACE = "mp_shop_header"
    const val SHOP_PRODUCT_TAB_TRACE = "mp_shop_product"
    const val SHOP_HOME_TAB_TRACE = "mp_shop_home"
    const val SHOP_HOME_WEB_VIEW_TRACE = "mp_shop_home_web_view"
    const val SHOP_HOME_IMAGE_SLIDER_BANNER_TRACE = "mp_shop_home_image_slider_banner"
    const val SHOP_HOME_IMAGE_SLIDER_SQUARE_TRACE = "mp_shop_home_image_slider_square"
    const val SHOP_HOME_IMAGE_MULTIPLE_COLUMN_TRACE = "mp_shop_home_image_multiple_column"

    object PltConstant{
        const val SHOP_TRACE = "mp_shop"
        const val SHOP_TRACE_PREPARE = "mp_shop_prepare"
        const val SHOP_TRACE_MIDDLE = "mp_shop_network"
        const val SHOP_TRACE_RENDER = "mp_shop_render"

        const val SHOP_TRACE_ACTIVITY_PREPARE = "mp_shop_activity_prepare"
        const val SHOP_TRACE_P1_MIDDLE = "mp_shop_p1_network"
        const val SHOP_TRACE_HEADER_SHOP_NAME_AND_PICTURE_RENDER = "mp_shop_header_shop_name_and_picture_render"
        const val SHOP_TRACE_HEADER_CONTENT_DATA_MIDDLE = "mp_shop_header_content_data_network"
        const val SHOP_TRACE_HEADER_CONTENT_DATA_RENDER = "mp_shop_header_content_data_render"

        const val SHOP_TRACE_HOME_PREPARE = "mp_shop_home_prepare"
        const val SHOP_TRACE_HOME_MIDDLE = "mp_shop_home_network"
        const val SHOP_TRACE_HOME_RENDER = "mp_shop_home_render"

        const val SHOP_TRACE_PRODUCT_PREPARE = "mp_shop_product_prepare"
        const val SHOP_TRACE_PRODUCT_MIDDLE = "mp_shop_product_network"
        const val SHOP_TRACE_PRODUCT_RENDER = "mp_shop_product_render"
    }
}