package com.tokopedia.home_recom.testViewModel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.content.Context
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_recom.viewmodel.PrimaryProductViewModel
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.CoroutineDispatcher
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Matchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

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
    lateinit var mockViewModel: PrimaryProductViewModel

    @Mock
    lateinit var mockString: String

    private val PRODUCT_ID = "[]"
    private val DEFAULT_ERROR_MESSAGE = "ERROR_MESSAGE"

    @Test
    fun addWishlistSuccess(){
        val mockInvoke = mock<(String?) -> Unit>()
        `when`(mockInvoke.invoke(Matchers.any())).thenAnswer {
            Assert.assertNotNull(it.arguments[0])
            null
        }
        `when`(mockViewModel.addWishList(mockString, null, mockInvoke)).thenAnswer{
            val completion = it.arguments[2] as ((message: String?) -> Unit)
            completion.invoke(PRODUCT_ID)
            null
        }
        mockViewModel.addWishList(mockString, null, mockInvoke)
        verify(mockViewModel, times(1)).addWishList(mockString, null, mockInvoke)
    }

    @Test
    fun addWishlistError(){
        val mockInvoke = mock<(String?) -> Unit>()
        `when`(mockInvoke.invoke(Matchers.any())).thenAnswer {
            Assert.assertEquals(it.arguments[0], DEFAULT_ERROR_MESSAGE)
            null
        }
        `when`(mockViewModel.addWishList(mockString, mockInvoke, null)).thenAnswer{
            val completion = it.arguments[1] as ((errorMessage: String?) -> Unit)
            completion.invoke(DEFAULT_ERROR_MESSAGE)
            null
        }
        mockViewModel.addWishList(mockString, mockInvoke, null)
        verify(mockViewModel, times(1)).addWishList(mockString, mockInvoke, null)
    }

    @Test
    fun removeWishlistSuccess(){
        val mockInvoke = mock<(String?) -> Unit>()
        `when`(mockInvoke.invoke(Matchers.any())).thenAnswer {
            Assert.assertEquals(it.arguments[0], PRODUCT_ID)
            null
        }
        `when`(mockViewModel.removeWishList(mockString, mockInvoke, null)).thenAnswer{
            val completion = it.arguments[1] as ((message: String?) -> Unit)
            completion.invoke(PRODUCT_ID)
            null
        }
        mockViewModel.removeWishList(mockString, mockInvoke, null)
        verify(mockViewModel, times(1)).removeWishList(mockString, mockInvoke, null)
    }

    @Test
    fun removeWishlistError(){
        val mockInvoke = mock<(String?) -> Unit>()
        `when`(mockInvoke.invoke(Matchers.any())).thenAnswer {
            Assert.assertEquals(it.arguments[0], DEFAULT_ERROR_MESSAGE)
            null
        }
        `when`(mockViewModel.removeWishList(mockString, null, mockInvoke)).thenAnswer{
            val completion = it.arguments[2] as ((errorMessage: String?) -> Unit)
            completion.invoke(DEFAULT_ERROR_MESSAGE)
            null
        }
        mockViewModel.removeWishList(mockString, null, mockInvoke)
        verify(mockViewModel, times(1)).removeWishList(mockString, null, mockInvoke)
    }

    @Test
    fun checkIsNotLogin(){
        `when`(mockViewModel.isLoggedIn()).thenReturn(false)
        Assert.assertFalse(mockViewModel.isLoggedIn())
    }

    @Test
    fun checkIsLogin(){
        `when`(mockViewModel.isLoggedIn()).thenReturn(true)
        Assert.assertTrue(mockViewModel.isLoggedIn())
    }

}