package com.tokopedia.cart.view.viewmodel

import androidx.lifecycle.Observer
import com.tokopedia.cartrevamp.view.uimodel.LoadWishlistV2State
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcommon.data.response.GetWishlistV2Response
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class GetWishlistV2Test : BaseCartViewModelTest() {

    private lateinit var wishlistV2StateObserver: Observer<LoadWishlistV2State>

    override fun setUp() {
        super.setUp()
        wishlistV2StateObserver = mockk { every { onChanged(any()) } just Runs }
        cartViewModel.wishlistV2State.observeForever(wishlistV2StateObserver)
    }

    @Test
    fun `WHEN processGetWishlistV2Data success THEN should send success event`() {
        // GIVEN
        val wishlistData = GetWishlistV2Response.Data.WishlistV2()
        coEvery { getWishlistV2UseCase.setParams(any()) } just Runs
        coEvery { getWishlistV2UseCase.executeOnBackground() } returns Success(wishlistData)

        // WHEN
        cartViewModel.processGetWishlistV2Data()

        // THEN
        coVerify {
            getWishlistV2UseCase.executeOnBackground()
            wishlistV2StateObserver.onChanged(
                LoadWishlistV2State.Success(
                    wishlistData.items, true
                )
            )
        }
    }

    @Test
    fun `WHEN processGetWishlistV2Data success with LCA THEN should send success event`() {
        // GIVEN
        cartViewModel.cartModel.lca = LocalCacheModel()
        val wishlistData = GetWishlistV2Response.Data.WishlistV2()
        coEvery { getWishlistV2UseCase.setParams(any()) } just Runs
        coEvery { getWishlistV2UseCase.executeOnBackground() } returns Success(wishlistData)

        // WHEN
        cartViewModel.processGetWishlistV2Data()

        // THEN
        coVerify {
            getWishlistV2UseCase.executeOnBackground()
            wishlistV2StateObserver.onChanged(
                LoadWishlistV2State.Success(
                    wishlistData.items, true
                )
            )
        }
    }

    @Test
    fun `WHEN processGetWishlistV2Data failed THEN should send failed event`() {
        // GIVEN
        val exception = ResponseErrorException()
        coEvery { getWishlistV2UseCase.setParams(any()) } just Runs
        coEvery { getWishlistV2UseCase.executeOnBackground() } returns Fail(exception)

        // WHEN
        cartViewModel.processGetWishlistV2Data()

        // THEN
        coVerify {
            getWishlistV2UseCase.executeOnBackground()
            wishlistV2StateObserver.onChanged(LoadWishlistV2State.Failed)
        }
    }

    override fun tearDown() {
        super.tearDown()
        cartViewModel.wishlistV2State.removeObserver(wishlistV2StateObserver)
    }
}
