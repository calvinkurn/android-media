package com.tokopedia.productcard.options

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import io.mockk.mockk
import org.junit.Rule

internal open class ProductCardOptionsViewModelTestFixtures {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    protected val addWishListUseCase = mockk<AddWishListUseCase>(relaxed = true)
    protected val removeWishListUseCase = mockk<RemoveWishListUseCase>(relaxed = true)
    protected val addToWishlistV2UseCase = mockk<AddToWishlistV2UseCase>(relaxed = true)
    protected val deleteWishlistV2UseCase = mockk<DeleteWishlistV2UseCase>(relaxed = true)
    protected val topAdsWishlistUseCase = mockk<UseCase<Boolean>>(relaxed = true)
    protected val addToCartUseCase = mockk<UseCase<AddToCartDataModel>>(relaxed = true)
    protected val userSession = mockk<UserSessionInterface>(relaxed = true)
    protected var isUsingWishlistV2 = false
    protected lateinit var productCardOptionsViewModel: ProductCardOptionsViewModel

    protected open fun createProductCardOptionsViewModel(productCardOptionsModel: ProductCardOptionsModel?) {
        productCardOptionsViewModel =  ProductCardOptionsViewModel(
                CoroutineTestDispatchersProvider,
                productCardOptionsModel,
                addWishListUseCase,
                removeWishListUseCase,
                addToWishlistV2UseCase,
                deleteWishlistV2UseCase,
                topAdsWishlistUseCase,
                addToCartUseCase,
                userSession,
                isUsingWishlistV2
        )
    }

    protected open fun setIsUsingWishlistV2(isWishlistV2: Boolean) {
        isUsingWishlistV2 = isWishlistV2
    }
}