package com.tokopedia.tokofood.home.domain.mapper

import com.tokopedia.tokofood.home.domain.constanta.TokoFoodLayoutType.Companion.BANNER_CAROUSEL
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodLayoutType.Companion.CATEGORY_WIDGET
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodLayoutType.Companion.LEGO_6_IMAGE
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodLayoutType.Companion.MERCHANT_LIST
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodLayoutType.Companion.TABS_TOKOFOOD
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodLayoutType.Companion.USP_TOKOFOOD

object TokoFoodHomeMapper {

    val SUPPORTED_LAYOUT_TYPE = listOf(
        TABS_TOKOFOOD,
        USP_TOKOFOOD,
        BANNER_CAROUSEL,
        LEGO_6_IMAGE,
        CATEGORY_WIDGET,
        MERCHANT_LIST
    )
}