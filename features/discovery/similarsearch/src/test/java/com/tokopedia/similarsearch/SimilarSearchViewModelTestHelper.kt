package com.tokopedia.similarsearch

import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.mockk
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.style.gherkin.FeatureBody

@Suppress("UNUSED_VARIABLE")
internal fun FeatureBody.createTestInstance() {
    val getSimilarProductsUseCase by memoized {
        mockk<UseCase<SimilarProductModel>>(relaxed = true)
    }

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

internal fun TestBody.createSimilarSearchViewModel(): SimilarSearchViewModel {
    val getSimilarProductsUseCase by memoized<UseCase<SimilarProductModel>>()
    val addWishListUseCase by memoized<AddWishListUseCase>()
    val removeWishListUseCase by memoized<RemoveWishListUseCase>()
    val userSession by memoized<UserSessionInterface>()

    return SimilarSearchViewModel(
            TestDispatcherProvider(),
            getSimilarProductsUseCase,
            addWishListUseCase,
            removeWishListUseCase,
            userSession
    )
}