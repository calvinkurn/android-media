package com.tokopedia.recentview.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.recentview.domain.usecase.RecentViewUseCase
import com.tokopedia.recentview.view.presenter.RecentViewViewModel
import com.tokopedia.recentview.view.viewmodel.RecentViewDetailProductDataModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
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
    private val addWishListUseCase = mockk<AddWishListUseCase>(relaxed = true)
    private val removeWishListUseCase = mockk<RemoveWishListUseCase>(relaxed = true)
    private val dispatcher = CoroutineTestDispatchersProvider

    lateinit var viewModel: RecentViewViewModel

    @Test
    fun `get recent view success`(){

        val slot = slot<(data : ArrayList<RecentViewDetailProductDataModel>) -> Unit>()
        every { userSession.userId } returns "1"
        coEvery { recentViewUseCase.execute(capture(slot), any()) } answers {
            slot.captured.invoke(arrayListOf())
        }
        viewModel = RecentViewViewModel(dispatcher, userSession, addWishListUseCase, removeWishListUseCase, recentViewUseCase)
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
        viewModel = RecentViewViewModel(dispatcher, userSession, addWishListUseCase, removeWishListUseCase, recentViewUseCase)
        viewModel.getRecentView()
        assert(viewModel.recentViewDetailProductDataResp.value is Fail)
    }

    @Test
    fun `add wishlist success`(){
        val slot = slot<WishListActionListener>()
        every { userSession.userId } returns "1"
        coEvery { addWishListUseCase.createObservable(any(), any(), capture(slot)) } answers {
            slot.captured.onSuccessAddWishlist("1")
        }
        viewModel = RecentViewViewModel(dispatcher, userSession, addWishListUseCase, removeWishListUseCase, recentViewUseCase)
        viewModel.addToWishlist(1, "1")
        assert(viewModel.addWishlistResponse.value is Success && (viewModel.addWishlistResponse.value as Success<String>).data == "1")
    }

    @Test
    fun `add wishlist error`(){
        val errorMessage = "Error message"
        val slot = slot<WishListActionListener>()
        every { userSession.userId } returns "1"
        coEvery { addWishListUseCase.createObservable(any(), any(), capture(slot)) } answers {
            slot.captured.onErrorAddWishList(errorMessage, "1")
        }
        viewModel = RecentViewViewModel(dispatcher, userSession, addWishListUseCase, removeWishListUseCase, recentViewUseCase)
        viewModel.addToWishlist(1, "1")
        assert(viewModel.addWishlistResponse.value is Fail && (viewModel.addWishlistResponse.value as Fail).throwable.message == errorMessage)
    }

    @Test
    fun `remove wishlist success`(){
        val slot = slot<WishListActionListener>()
        every { userSession.userId } returns "1"
        coEvery { removeWishListUseCase.createObservable(any(), any(), capture(slot)) } answers {
            slot.captured.onSuccessRemoveWishlist("1")
        }
        viewModel = RecentViewViewModel(dispatcher, userSession, addWishListUseCase, removeWishListUseCase, recentViewUseCase)
        viewModel.removeFromWishlist(1, "1")
        assert(viewModel.removeWishlistResponse.value is Success && (viewModel.removeWishlistResponse.value as Success<String>).data == "1")
    }

    @Test
    fun `remove wishlist error`(){
        val errorMessage = "Error message"
        val slot = slot<WishListActionListener>()
        every { userSession.userId } returns "1"
        coEvery { removeWishListUseCase.createObservable(any(), any(), capture(slot)) } answers {
            slot.captured.onErrorRemoveWishlist(errorMessage, "1")
        }
        viewModel = RecentViewViewModel(dispatcher, userSession, addWishListUseCase, removeWishListUseCase, recentViewUseCase)
        viewModel.removeFromWishlist(1, "1")
        assert(viewModel.removeWishlistResponse.value is Fail && (viewModel.removeWishlistResponse.value as Fail).throwable.message == errorMessage)
    }
}