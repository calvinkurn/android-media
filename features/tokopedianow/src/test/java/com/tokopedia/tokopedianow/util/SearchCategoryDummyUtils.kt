package com.tokopedia.tokopedianow.util

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData

object SearchCategoryDummyUtils {

    val miniCartWidgetData get() = MiniCartWidgetData(
            totalProductCount = 7,
            totalProductPrice = 1000000,
    )

    val miniCartItems get() = listOf(
            MiniCartItem(productId = "574261655", productParentId = "0", quantity = 10),

            MiniCartItem(productId = "574261652", productParentId = "0", quantity = 3),

            MiniCartItem(productId = "576261652", productParentId = "1682734974", quantity = 2),
            MiniCartItem(productId = "576261651", productParentId = "1682734974", quantity = 6),
            MiniCartItem(productId = "576261653", productParentId = "1682734974", quantity = 7),

            MiniCartItem(productId = "576861653", productParentId = "2682234972", quantity = 1),
            MiniCartItem(productId = "576861652", productParentId = "2682234972", quantity = 5),
    )

    val miniCartSimplifiedData get() = MiniCartSimplifiedData(
            miniCartWidgetData = miniCartWidgetData,
            miniCartItems = miniCartItems,
            isShowMiniCartWidget = true,
    )

    val dummyChooseAddressData get() =
            LocalCacheModel(
                    address_id = "12257",
                    city_id = "12345",
                    district_id = "2274",
                    lat = "1.1000",
                    long = "37.002",
                    postal_code = "15123",
                    shop_id = "549621",
                    warehouse_id = "11299001123",
            )
}