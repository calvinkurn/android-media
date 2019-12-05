package com.tokopedia.productcard.options

import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.productcard.options.testutils.TestDispatcherProvider
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

internal fun ProductCardOptionsViewModel.getOption(optionTitle: String): ProductCardOptionsItemModel? {
    return this.getOptionsListLiveData().value?.single {
        it is ProductCardOptionsItemModel && it.title == optionTitle
    } as ProductCardOptionsItemModel?
}