package com.tokopedia.shop.common.constant

object ShopPagePerformanceConstant {
    const val SHOP_HEADER_TRACE = "mp_shop_header_${ShopPageConstant.ShopPageFeatureImprovementType.V3_1_IMPROVEMENT}" // Faster shop header using more slimmer P1 network call
    const val SHOP_PRODUCT_TAB_TRACE = "mp_shop_product_${ShopPageConstant.ShopPageFeatureImprovementType.V3_1_IMPROVEMENT}" // Faster shop header using more slimmer P1 network call
    const val SHOP_HOME_TAB_TRACE = "mp_shop_home_${ShopPageConstant.ShopPageFeatureImprovementType.V3_1_IMPROVEMENT}" // Faster shop header using more slimmer P1 network call
    const val SHOP_REIMAGINED_HEADER_TRACE = "mp_shop_header_${ShopPageConstant.ShopPageFeatureImprovementType.V4_1_REIMAGINED}"
    const val SHOP_REIMAGINED_PRODUCT_TAB_TRACE= "mp_shop_product_${ShopPageConstant.ShopPageFeatureImprovementType.V4_1_REIMAGINED}"
    const val SHOP_REIMAGINED_HOME_TAB_TRACE= "mp_shop_home_${ShopPageConstant.ShopPageFeatureImprovementType.V4_1_REIMAGINED}"
    const val SHOP_HOME_IMAGE_SLIDER_BANNER_TRACE = "mp_shop_home_image_slider_banner"
    const val SHOP_HOME_IMAGE_SLIDER_SQUARE_TRACE = "mp_shop_home_image_slider_square"
    const val SHOP_HOME_IMAGE_MULTIPLE_COLUMN_TRACE = "mp_shop_home_image_multiple_column"

    object PltConstant {
        const val SHOP_TRACE = "mp_shop_${ShopPageConstant.ShopPageFeatureImprovementType.V3_1_IMPROVEMENT}"
        const val SHOP_REIMAGINED_TRACE = "mp_shop_${ShopPageConstant.ShopPageFeatureImprovementType.V4_1_REIMAGINED}"
        const val SHOP_TRACE_PREPARE = "mp_shop_${ShopPageConstant.ShopPageFeatureImprovementType.V3_1_IMPROVEMENT}_prepare"
        const val SHOP_TRACE_MIDDLE = "mp_shop_${ShopPageConstant.ShopPageFeatureImprovementType.V3_1_IMPROVEMENT}_network"
        const val SHOP_TRACE_RENDER = "mp_shop_${ShopPageConstant.ShopPageFeatureImprovementType.V3_1_IMPROVEMENT}_render"
        const val SHOP_REIMAGINED_TRACE_PREPARE = "mp_shop_${ShopPageConstant.ShopPageFeatureImprovementType.V4_1_REIMAGINED}_prepare"
        const val SHOP_REIMAGINED_TRACE_MIDDLE = "mp_shop_${ShopPageConstant.ShopPageFeatureImprovementType.V4_1_REIMAGINED}_network"
        const val SHOP_REIMAGINED_TRACE_RENDER = "mp_shop_${ShopPageConstant.ShopPageFeatureImprovementType.V4_1_REIMAGINED}_render"

        const val SHOP_TRACE_ACTIVITY_PREPARE = "mp_shop_${ShopPageConstant.ShopPageFeatureImprovementType.V3_1_IMPROVEMENT}_activity_prepare"
        const val SHOP_REIMAGINED_TRACE_ACTIVITY_PREPARE = "mp_shop_${ShopPageConstant.ShopPageFeatureImprovementType.V4_1_REIMAGINED}_activity_prepare"
        const val SHOP_TRACE_P1_MIDDLE = "mp_shop_${ShopPageConstant.ShopPageFeatureImprovementType.V3_1_IMPROVEMENT}_p1_network"
        const val SHOP_REIMAGINED_TRACE_P1_MIDDLE = "mp_shop_${ShopPageConstant.ShopPageFeatureImprovementType.V4_1_REIMAGINED}_p1_network"
        const val SHOP_TRACE_HEADER_SHOP_NAME_AND_PICTURE_RENDER = "mp_shop_${ShopPageConstant.ShopPageFeatureImprovementType.V3_1_IMPROVEMENT}_header_shop_name_and_picture_render"
        const val SHOP_REIMAGINED_TRACE_HEADER_SHOP_NAME_AND_PICTURE_RENDER = "mp_shop_${ShopPageConstant.ShopPageFeatureImprovementType.V4_1_REIMAGINED}_header_shop_name_and_picture_render"

        const val SHOP_TRACE_HOME_V2_PREPARE = "mp_shop_home_v2_prepare"
        const val SHOP_TRACE_HOME_V2_MIDDLE = "mp_shop_home_v2_network"
        const val SHOP_TRACE_HOME_V2_RENDER = "mp_shop_home_v2_render"

        const val SHOP_TRACE_PRODUCT_PREPARE = "mp_shop_product_prepare"
        const val SHOP_TRACE_PRODUCT_MIDDLE = "mp_shop_product_network"
        const val SHOP_TRACE_PRODUCT_RENDER = "mp_shop_product_render"
    }
}
