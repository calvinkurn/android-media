package com.tokopedia.productcard.options

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import io.mockk.mockk
import org.junit.Rule

internal open class ProductCardOptionsViewModelTestFixtures {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    protected val addToWishlistV2UseCase = mockk<AddToWishlistV2UseCase>(relaxed = true)
    protected val deleteWishlistV2UseCase = mockk<DeleteWishlistV2UseCase>(relaxed = true)
    protected val addToCartUseCase = mockk<UseCase<AddToCartDataModel>>(relaxed = true)
    protected val userSession = mockk<UserSessionInterface>(relaxed = true)
    protected lateinit var productCardOptionsViewModel: ProductCardOptionsViewModel

    protected open fun createProductCardOptionsViewModel(productCardOptionsModel: ProductCardOptionsModel?) {
        productCardOptionsViewModel =  ProductCardOptionsViewModel(
                CoroutineTestDispatchersProvider,
                productCardOptionsModel,
                addToWishlistV2UseCase,
                deleteWishlistV2UseCase,
                addToCartUseCase,
                userSession
        )
    }
}
