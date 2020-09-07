package com.tokopedia.productcard.options

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.productcard.options.testutils.TestDispatcherProvider
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.mockk
import org.junit.Rule

internal open class ProductCardOptionsViewModelTestFixtures {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    protected val addWishListUseCase = mockk<AddWishListUseCase>(relaxed = true)
    protected val removeWishListUseCase = mockk<RemoveWishListUseCase>(relaxed = true)
    protected val topAdsWishlistUseCase = mockk<UseCase<Boolean>>(relaxed = true)
    protected val userSession = mockk<UserSessionInterface>(relaxed = true)
    protected lateinit var productCardOptionsViewModel: ProductCardOptionsViewModel

    protected open fun createProductCardOptionsViewModel(productCardOptionsModel: ProductCardOptionsModel?) {
        productCardOptionsViewModel =  ProductCardOptionsViewModel(
                TestDispatcherProvider(),
                productCardOptionsModel,
                addWishListUseCase,
                removeWishListUseCase,
                topAdsWishlistUseCase,
                userSession
        )
    }
}