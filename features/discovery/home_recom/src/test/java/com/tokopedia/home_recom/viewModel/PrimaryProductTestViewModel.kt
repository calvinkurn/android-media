package com.tokopedia.home_recom.viewModel

import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.home_recom.viewmodel.PrimaryProductViewModel
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * Created by Lukas on 2019-07-08
 */
@RunWith(PowerMockRunner::class)
@PrepareForTest(PrimaryProductViewModel::class)
@ExperimentalCoroutinesApi
class PrimaryProductTestViewModel : Spek({
    lateinit var viewModel: PrimaryProductViewModel


    lateinit var removeWishListUseCase: RemoveWishListUseCase

    lateinit var addWishListUseCase: AddWishListUseCase

    lateinit var addToCartUseCase: AddToCartUseCase

    val productId = "316960043"
    val defaultErrorMessage = "ERROR_MESSAGE"


    Feature(""){
//        viewModel = PrimaryProductViewModel(any(), mock(), addWishListUseCase, removeWishListUseCase, addToCartUseCase, "", Dispatchers.Unconfined)
    }


})


//@Test
//fun addWishlistSuccess(){
//    val spy = Mockito.spy(viewModel)
//    val mockInvoke = mock<(String?) -> Unit>()
//    `when`(mockInvoke.invoke(Matchers.any())).thenAnswer {
//        Assert.assertNotNull(it.arguments[0])
//        null
//    }
//    `when`(spy.addWishList(mockString, null, mockInvoke)).thenAnswer{
//        val completion = it.arguments[2] as ((message: String?) -> Unit)
//        completion.invoke(productId)
//        null
//    }
//    spy.addWishList(mockString, null, mockInvoke)
//    verify(spy, times(1)).addWishList(mockString, null, mockInvoke)
//}
//
//@Test
//fun addWishlistError(){
//    val spy = Mockito.spy(viewModel)
//    val mockInvoke = mock<(String?) -> Unit>()
//    `when`(mockInvoke.invoke(Matchers.any())).thenAnswer {
//        Assert.assertEquals(it.arguments[0], defaultErrorMessage)
//        null
//    }
//    `when`(spy.addWishList(mockString, mockInvoke, null)).thenAnswer{
//        val completion = it.arguments[1] as ((errorMessage: String?) -> Unit)
//        completion.invoke(defaultErrorMessage)
//        null
//    }
//    spy.addWishList(mockString, mockInvoke, null)
//    verify(spy, times(1)).addWishList(mockString, mockInvoke, null)
//}
//
//@Test
//fun removeWishlistSuccess(){
//    val spy = Mockito.spy(viewModel)
//    val mockInvoke = mock<(String?) -> Unit>()
//    `when`(mockInvoke.invoke(Matchers.any())).thenAnswer {
//        Assert.assertEquals(it.arguments[0], productId)
//        null
//    }
//    `when`(spy.removeWishList(mockString, mockInvoke, null)).thenAnswer{
//        val completion = it.arguments[1] as ((message: String?) -> Unit)
//        completion.invoke(productId)
//        null
//    }
//    spy.removeWishList(mockString, mockInvoke, null)
//    verify(spy, times(1)).removeWishList(mockString, mockInvoke, null)
//}
//
//@Test
//fun removeWishlistError(){
//    val spy = Mockito.spy(viewModel)
//    val mockInvoke = mock<(String?) -> Unit>()
//    `when`(mockInvoke.invoke(Matchers.any())).thenAnswer {
//        Assert.assertEquals(it.arguments[0], defaultErrorMessage)
//        null
//    }
//    `when`(spy.removeWishList(mockString, null, mockInvoke)).thenAnswer{
//        val completion = it.arguments[2] as ((errorMessage: String?) -> Unit)
//        completion.invoke(defaultErrorMessage)
//        null
//    }
//    spy.removeWishList(mockString, null, mockInvoke)
//    verify(spy, times(1)).removeWishList(mockString, null, mockInvoke)
//}
//
//@Test
//fun checkIsNotLogin(){
//    `when`(viewModel.isLoggedIn()).thenReturn(false)
//    Assert.assertFalse(viewModel.isLoggedIn())
//}
//
//@Test
//fun checkIsLogin(){
//    `when`(viewModel.isLoggedIn()).thenReturn(true)
//    Assert.assertTrue(viewModel.isLoggedIn())
//}
//
//@Test
//fun testOverrideUseCaseErrorRemoveWishlist(){
//    `when`(removeWishListUseCase.createObservable(any(), any(), any())).thenAnswer {
//        val listener = it.arguments[2] as WishListActionListener
//        listener.onErrorAddWishList(defaultErrorMessage, productId)
//    }
//    viewModel.removeWishList(productId, null, {
//        Assert.assertEquals(it, defaultErrorMessage)
//    })
//}
//
//@Test
//fun testOverrideUseCaseSuccessRemoveWishlist(){
//    `when`(removeWishListUseCase.createObservable(any(), any(), any())).thenAnswer {
//        val listener = it.arguments[2] as WishListActionListener
//        listener.onSuccessAddWishlist(productId)
//    }
//    viewModel.removeWishList(productId, {
//        Assert.assertEquals(it, productId)
//    }, null)
//}
//
//@Test
//fun testOverrideUseCaseErrorAddWishlist(){
//    `when`(addWishListUseCase.createObservable(any(), any(), any())).thenAnswer {
//        val listener = it.arguments[2] as WishListActionListener
//        listener.onErrorAddWishList(defaultErrorMessage, productId)
//    }
//    viewModel.addWishList(productId, {
//        Assert.assertEquals(it, defaultErrorMessage)
//    }, null)
//}
//
//@Test
//fun testOverrideUseCaseSuccessAddWishlist(){
//    `when`(addWishListUseCase.createObservable(any(), any(), any())).thenAnswer {
//        val listener = it.arguments[2] as WishListActionListener
//        listener.onSuccessAddWishlist(productId)
//    }
//    viewModel.addWishList(productId, null, {
//        Assert.assertEquals(it, productId)
//    })
//}