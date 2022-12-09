package com.tokopedia.tokopedianow.wishlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.tokopedianow.common.domain.model.AddToWishListResponse
import com.tokopedia.tokopedianow.common.domain.model.RemoveFromWishListResponse
import com.tokopedia.tokopedianow.common.domain.usecase.AddToWishlistUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.RemoveFromWishlistUseCase
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowWishlistViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class TokoNowWishlistViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var addToWishlistUseCase: AddToWishlistUseCase

    @RelaxedMockK
    lateinit var removeFromWishlistUseCase: RemoveFromWishlistUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    protected lateinit var viewModel : TokoNowWishlistViewModel

    protected fun onAddToWishlist_thenReturn(response: AddToWishListResponse) {
        coEvery {
            addToWishlistUseCase.execute(any(), any())
        } returns response
    }

    protected fun onAddToWishlist_thenReturn(error: Throwable) {
        coEvery {
            addToWishlistUseCase.execute(any(), any())
        } throws error
    }

    protected fun onRemoveFromWishlist_thenReturn(response: RemoveFromWishListResponse) {
        coEvery {
            removeFromWishlistUseCase.execute(any(), any())
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
        viewModel = TokoNowWishlistViewModel(
            addToWishlistUseCase,
            removeFromWishlistUseCase,
            userSession,
            CoroutineTestDispatchersProvider
        )
    }
}
