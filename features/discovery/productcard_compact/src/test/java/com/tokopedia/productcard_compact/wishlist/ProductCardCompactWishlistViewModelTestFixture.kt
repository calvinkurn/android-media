package com.tokopedia.productcard_compact.wishlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.productcard_compact.productcard.presentation.viewmodel.ProductCardCompactWishlistViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class ProductCardCompactWishlistViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var addToWishlistUseCase: AddToWishlistV2UseCase

    @RelaxedMockK
    lateinit var removeFromWishlistUseCase: DeleteWishlistV2UseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    protected lateinit var viewModel : ProductCardCompactWishlistViewModel

    protected fun onAddToWishlist_thenReturn(response: Result<AddToWishlistV2Response.Data.WishlistAddV2>) {
        coEvery {
            addToWishlistUseCase.executeOnBackground()
        } returns response
    }

    protected fun onAddToWishlist_thenReturn(error: Throwable) {
        coEvery {
            addToWishlistUseCase.execute(any(), any())
        } throws error
    }

    protected fun onRemoveFromWishlist_thenReturn(response: Result<DeleteWishlistV2Response.Data.WishlistRemoveV2>) {
        coEvery {
            removeFromWishlistUseCase.executeOnBackground()
        } returns response
    }

    protected fun onRemoveFromWishlist_thenReturn(error: Throwable) {
        coEvery {
            removeFromWishlistUseCase.execute(any(), any())
        } throws error
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ProductCardCompactWishlistViewModel(
            addToWishlistUseCase,
            removeFromWishlistUseCase,
            userSession,
            CoroutineTestDispatchersProvider
        )
    }
}
