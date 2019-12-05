package com.tokopedia.productcard.options

import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import org.spekframework.spek2.dsl.TestBody

internal fun TestBody.createProductCardOptionsViewModel(
        productCardOptionsModel: ProductCardOptionsModel? = ProductCardOptionsModel(
                hasWishlist = true,
                isWishlisted = true,
                hasSimilarSearch = true,
                keyword = "samsung",
                productId = "433759643",
                isTopAds = true
        )
): ProductCardOptionsViewModel {
    return ProductCardOptionsViewModel(
            TestDispatcherProvider(),
            productCardOptionsModel
    )
}