package com.tokopedia.shop.common.constant

object ShopPagePerformanceConstant {
    const val SHOP_HEADER_TRACE_V3 = "mp_shop_header_v3" // Faster shop header using more slimmer P1 network call
    const val SHOP_PRODUCT_TAB_TRACE_V3 = "mp_shop_product_v3" // Faster shop header using more slimmer P1 network call
    const val SHOP_HOME_TAB_TRACE_V3 = "mp_shop_home_v3" // Faster shop header using more slimmer P1 network call
    const val SHOP_HEADER_TRACE_V4 = "mp_shop_header_${ShopPageConstant.ShopPageFeatureImprovementType.V4_3}"
    const val SHOP_PRODUCT_TAB_TRACE_V4 = "mp_shop_product_${ShopPageConstant.ShopPageFeatureImprovementType.V4_3}"
    const val SHOP_HOME_TAB_TRACE_V4 = "mp_shop_home_${ShopPageConstant.ShopPageFeatureImprovementType.V4_3}"
    const val SHOP_HOME_PREFETCH_V1 = "mp_shop_${ShopPageConstant.ShopPageFeatureImprovementType.PREFETCH_V1_0}"
    const val SHOP_HOME_IMAGE_SLIDER_BANNER_TRACE = "mp_shop_home_image_slider_banner"
    const val SHOP_HOME_IMAGE_SLIDER_SQUARE_TRACE = "mp_shop_home_image_slider_square"
    const val SHOP_HOME_IMAGE_MULTIPLE_COLUMN_TRACE = "mp_shop_home_image_multiple_column"

    object PltConstant {
        const val SHOP_TRACE = "mp_shop"
        const val SHOP_TRACE_V4 = "mp_shop_${ShopPageConstant.ShopPageFeatureImprovementType.V4_3}"
        const val SHOP_TRACE_PREPARE = "mp_shop_prepare"
        const val SHOP_TRACE_MIDDLE = "mp_shop_network"
        const val SHOP_TRACE_RENDER = "mp_shop_render"
        const val SHOP_V4_TRACE_PREPARE = "mp_shop_${ShopPageConstant.ShopPageFeatureImprovementType.V4_3}_prepare"
        const val SHOP_V4_TRACE_MIDDLE = "mp_shop_${ShopPageConstant.ShopPageFeatureImprovementType.V4_3}_network"
        const val SHOP_V4_TRACE_RENDER = "mp_shop_${ShopPageConstant.ShopPageFeatureImprovementType.V4_3}_render"

        const val SHOP_TRACE_ACTIVITY_PREPARE = "mp_shop_activity_prepare"
        const val SHOP_V4_TRACE_ACTIVITY_PREPARE = "mp_shop_${ShopPageConstant.ShopPageFeatureImprovementType.V4_3}_activity_prepare"
        const val SHOP_TRACE_P1_MIDDLE = "mp_shop_p1_network"
        const val SHOP_V4_TRACE_P1_MIDDLE = "mp_shop_${ShopPageConstant.ShopPageFeatureImprovementType.V4_3}_p1_network"
        const val SHOP_TRACE_HEADER_SHOP_NAME_AND_PICTURE_RENDER = "mp_shop_header_shop_name_and_picture_render"
        const val SHOP_V4_TRACE_HEADER_SHOP_NAME_AND_PICTURE_RENDER = "mp_shop_${ShopPageConstant.ShopPageFeatureImprovementType.V4_3}_header_shop_name_and_picture_render"

        const val SHOP_TRACE_HOME_V2_PREPARE = "mp_shop_home_v2_prepare"
        const val SHOP_TRACE_HOME_V2_MIDDLE = "mp_shop_home_v2_network"
        const val SHOP_TRACE_HOME_V2_RENDER = "mp_shop_home_v2_render"

        const val SHOP_TRACE_PRODUCT_PREPARE = "mp_shop_product_prepare"
        const val SHOP_TRACE_PRODUCT_MIDDLE = "mp_shop_product_network"
        const val SHOP_TRACE_PRODUCT_RENDER = "mp_shop_product_render"
    }
}
