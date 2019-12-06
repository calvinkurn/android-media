package com.tokopedia.productcard.options

import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.productcard.options.testutils.TestDispatcherProvider
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.mockk
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.style.gherkin.FeatureBody

internal fun FeatureBody.createTestInstance() {
    val addWishListUseCase by memoized {
        mockk<AddWishListUseCase>(relaxed = true)
    }

    val removeWishListUseCase by memoized {
        mockk<RemoveWishListUseCase>(relaxed = true)
    }

    val userSession by memoized {
        mockk<UserSessionInterface>(relaxed = true)
    }
}

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
    val addWishListUseCase by memoized<AddWishListUseCase>()
    val removeWishListUseCase by memoized<RemoveWishListUseCase>()
    val userSession by memoized<UserSessionInterface>()

    return ProductCardOptionsViewModel(
            TestDispatcherProvider(),
            productCardOptionsModel,
            addWishListUseCase,
            removeWishListUseCase,
            userSession
    )
}

internal fun ProductCardOptionsViewModel.getOption(optionTitle: String): ProductCardOptionsItemModel {
    return this.getOptionsListLiveData().value?.single {
        it is ProductCardOptionsItemModel && it.title == optionTitle
    } as ProductCardOptionsItemModel
}