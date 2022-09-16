package com.tokopedia.recentview.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.recentview.domain.usecase.RecentViewUseCase
import com.tokopedia.recentview.view.presenter.RecentViewViewModel
import com.tokopedia.recentview.view.viewmodel.RecentViewDetailProductDataModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import io.mockk.*
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 13/11/20.
 */
class RecentViewViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val recentViewUseCase = mockk<RecentViewUseCase>(relaxed = true)
    private val userSession = mockk<UserSessionInterface>(relaxed = true)
    private val addToWishlistV2UseCase = mockk<AddToWishlistV2UseCase>(relaxed = true)
    private val deleteWishlistV2UseCase = mockk<DeleteWishlistV2UseCase>(relaxed = true)
    private val dispatcher = CoroutineTestDispatchersProvider

    lateinit var viewModel: RecentViewViewModel

    @Test
    fun `get recent view success`(){

        val slot = slot<(data : ArrayList<RecentViewDetailProductDataModel>) -> Unit>()
        every { userSession.userId } returns "1"
        coEvery { recentViewUseCase.execute(capture(slot), any()) } answers {
            slot.captured.invoke(arrayListOf())
        }
        viewModel = RecentViewViewModel(dispatcher, userSession, addToWishlistV2UseCase, deleteWishlistV2UseCase, recentViewUseCase)
        viewModel.getRecentView()
        assert(viewModel.recentViewDetailProductDataResp.value is Success)
    }

    @Test
    fun `get recent view error`(){
        val slot = slot<(data : Throwable) -> Unit>()
        every { userSession.userId } returns "1"
        coEvery { recentViewUseCase.execute(any(), capture(slot)) } answers {
            slot.captured.invoke(Throwable())
        }
        viewModel = RecentViewViewModel(dispatcher, userSession, addToWishlistV2UseCase, deleteWishlistV2UseCase, recentViewUseCase)
        viewModel.getRecentView()
        assert(viewModel.recentViewDetailProductDataResp.value is Fail)
    }

    @Test
    fun verify_add_to_wishlistv2_returns_success() {
        val productId = "123"
        val resultWishlistAddV2 = AddToWishlistV2Response.Data.WishlistAddV2(success = true)

        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.executeOnBackground() } returns Success(resultWishlistAddV2)

        viewModel = RecentViewViewModel(dispatcher, userSession, addToWishlistV2UseCase, deleteWishlistV2UseCase, recentViewUseCase)
        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.addToWishlistV2(productId, mockListener)

        verify { addToWishlistV2UseCase.setParams(productId, userSession.userId) }
        coVerify { addToWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun verify_add_to_wishlistv2_returns_fail() {
        val productId = "123"
        val mockThrowable = mockk<Throwable>("fail")

        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)

        viewModel = RecentViewViewModel(dispatcher, userSession, addToWishlistV2UseCase, deleteWishlistV2UseCase, recentViewUseCase)
        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.addToWishlistV2(productId, mockListener)

        verify { addToWishlistV2UseCase.setParams(productId, userSession.userId) }
        coVerify { addToWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun verify_remove_wishlistV2_returns_success(){
        val productId = "123"
        val resultWishlistRemoveV2 = DeleteWishlistV2Response.Data.WishlistRemoveV2(success = true)

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Success(resultWishlistRemoveV2)

        viewModel = RecentViewViewModel(dispatcher, userSession, addToWishlistV2UseCase, deleteWishlistV2UseCase, recentViewUseCase)
        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.removeFromWishlistV2(productId, mockListener)

        verify { deleteWishlistV2UseCase.setParams(productId, userSession.userId) }
        coVerify { deleteWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun verify_remove_wishlistV2_returns_fail(){
        val productId = "123"
        val mockThrowable = mockk<Throwable>("fail")

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)

        viewModel = RecentViewViewModel(dispatcher, userSession, addToWishlistV2UseCase, deleteWishlistV2UseCase, recentViewUseCase)
        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.removeFromWishlistV2(productId, mockListener)

        verify { deleteWishlistV2UseCase.setParams(productId, userSession.userId) }
        coVerify { deleteWishlistV2UseCase.executeOnBackground() }
    }
}