package com.tokopedia.similarsearch.viewmodel

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.similarsearch.SimilarSearchViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.similarsearch.getsimilarproducts.model.SimilarProductModel
import com.tokopedia.similarsearch.viewmodel.testinstance.getSimilarSearchQuery
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.mockk
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.style.gherkin.FeatureBody
import com.tokopedia.usecase.UseCase as RxUseCase

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

    val addToCartUseCase by memoized {
        mockk<RxUseCase<AddToCartDataModel>>(relaxed = true)
    }
}

internal fun TestBody.createSimilarSearchViewModel(): SimilarSearchViewModel {
    val getSimilarProductsUseCase by memoized<UseCase<SimilarProductModel>>()
    val addWishListUseCase by memoized<AddWishListUseCase>()
    val removeWishListUseCase by memoized<RemoveWishListUseCase>()
    val userSession by memoized<UserSessionInterface>()
    val addToCartUseCase by memoized<RxUseCase<AddToCartDataModel>>()

    return SimilarSearchViewModel(
            CoroutineTestDispatchersProvider,
            getSimilarSearchQuery(),
            getSimilarProductsUseCase,
            addWishListUseCase,
            removeWishListUseCase,
            addToCartUseCase,
            userSession
    )
}