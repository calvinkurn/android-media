package com.tokopedia.home_recom.testViewModel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.content.Context
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.tokopedia.home_recom.viewmodel.PrimaryProductViewModel
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.Dispatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Matchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import rx.Observable

/**
 * Created by Lukas on 2019-07-08
 */
@RunWith(PowerMockRunner::class)
@PrepareForTest(PrimaryProductViewModel::class)
class PrimaryProductTestViewModel {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var viewModel: PrimaryProductViewModel

    @Mock
    lateinit var mockString: String

    @Mock
    lateinit var removeWishListUseCase: RemoveWishListUseCase

    @Mock
    lateinit var addWishListUseCase: AddWishListUseCase

    private val PRODUCT_ID = "[]"
    private val DEFAULT_ERROR_MESSAGE = "ERROR_MESSAGE"

    @Before
    fun setup(){
        viewModel = PrimaryProductViewModel(mock(), mock(), addWishListUseCase, removeWishListUseCase, Dispatchers.Unconfined)
    }

    @Test
    fun addWishlistSuccess(){
        val spy = Mockito.spy(viewModel)
        val mockInvoke = mock<(String?) -> Unit>()
        `when`(mockInvoke.invoke(Matchers.any())).thenAnswer {
            Assert.assertNotNull(it.arguments[0])
            null
        }
        `when`(spy.addWishList(mockString, null, mockInvoke)).thenAnswer{
            val completion = it.arguments[2] as ((message: String?) -> Unit)
            completion.invoke(PRODUCT_ID)
            null
        }
        spy.addWishList(mockString, null, mockInvoke)
        verify(spy, times(1)).addWishList(mockString, null, mockInvoke)
    }

    @Test
    fun addWishlistError(){
        val spy = Mockito.spy(viewModel)
        val mockInvoke = mock<(String?) -> Unit>()
        `when`(mockInvoke.invoke(Matchers.any())).thenAnswer {
            Assert.assertEquals(it.arguments[0], DEFAULT_ERROR_MESSAGE)
            null
        }
        `when`(spy.addWishList(mockString, mockInvoke, null)).thenAnswer{
            val completion = it.arguments[1] as ((errorMessage: String?) -> Unit)
            completion.invoke(DEFAULT_ERROR_MESSAGE)
            null
        }
        spy.addWishList(mockString, mockInvoke, null)
        verify(spy, times(1)).addWishList(mockString, mockInvoke, null)
    }

    @Test
    fun removeWishlistSuccess(){
        val spy = Mockito.spy(viewModel)
        val mockInvoke = mock<(String?) -> Unit>()
        `when`(mockInvoke.invoke(Matchers.any())).thenAnswer {
            Assert.assertEquals(it.arguments[0], PRODUCT_ID)
            null
        }
        `when`(spy.removeWishList(mockString, mockInvoke, null)).thenAnswer{
            val completion = it.arguments[1] as ((message: String?) -> Unit)
            completion.invoke(PRODUCT_ID)
            null
        }
        spy.removeWishList(mockString, mockInvoke, null)
        verify(spy, times(1)).removeWishList(mockString, mockInvoke, null)
    }

    @Test
    fun removeWishlistError(){
        val spy = Mockito.spy(viewModel)
        val mockInvoke = mock<(String?) -> Unit>()
        `when`(mockInvoke.invoke(Matchers.any())).thenAnswer {
            Assert.assertEquals(it.arguments[0], DEFAULT_ERROR_MESSAGE)
            null
        }
        `when`(spy.removeWishList(mockString, null, mockInvoke)).thenAnswer{
            val completion = it.arguments[2] as ((errorMessage: String?) -> Unit)
            completion.invoke(DEFAULT_ERROR_MESSAGE)
            null
        }
        spy.removeWishList(mockString, null, mockInvoke)
        verify(spy, times(1)).removeWishList(mockString, null, mockInvoke)
    }

    @Test
    fun checkIsNotLogin(){
        `when`(viewModel.isLoggedIn()).thenReturn(false)
        Assert.assertFalse(viewModel.isLoggedIn())
    }

    @Test
    fun checkIsLogin(){
        `when`(viewModel.isLoggedIn()).thenReturn(true)
        Assert.assertTrue(viewModel.isLoggedIn())
    }

    @Test
    fun testOverrideUseCaseErrorRemoveWishlist(){
        `when`(removeWishListUseCase.createObservable(any(), any(), any())).thenAnswer {
            val listener = it.arguments[2] as WishListActionListener
            listener.onErrorAddWishList(DEFAULT_ERROR_MESSAGE, PRODUCT_ID)
        }
        viewModel.removeWishList(PRODUCT_ID, null, {
            Assert.assertEquals(it, DEFAULT_ERROR_MESSAGE)
        })
    }

    @Test
    fun testOverrideUseCaseSuccessRemoveWishlist(){
        `when`(removeWishListUseCase.createObservable(any(), any(), any())).thenAnswer {
            val listener = it.arguments[2] as WishListActionListener
            listener.onSuccessAddWishlist(PRODUCT_ID)
        }
        viewModel.removeWishList(PRODUCT_ID, {
            Assert.assertEquals(it, PRODUCT_ID)
        }, null)
    }

    @Test
    fun testOverrideUseCaseErrorAddWishlist(){
        `when`(addWishListUseCase.createObservable(any(), any(), any())).thenAnswer {
            val listener = it.arguments[2] as WishListActionListener
            listener.onErrorAddWishList(DEFAULT_ERROR_MESSAGE, PRODUCT_ID)
        }
        viewModel.addWishList(PRODUCT_ID, {
            Assert.assertEquals(it, DEFAULT_ERROR_MESSAGE)
        }, null)
    }

    @Test
    fun testOverrideUseCaseSuccessAddWishlist(){
        `when`(addWishListUseCase.createObservable(any(), any(), any())).thenAnswer {
            val listener = it.arguments[2] as WishListActionListener
            listener.onSuccessAddWishlist(PRODUCT_ID)
        }
        viewModel.addWishList(PRODUCT_ID, null, {
            Assert.assertEquals(it, PRODUCT_ID)
        })
    }

}