package com.tokopedia.home_wishlist.viewModel.addtocart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.home_wishlist.domain.GetWishlistDataUseCase
import com.tokopedia.home_wishlist.model.datamodel.WishlistItemDataModel
import com.tokopedia.home_wishlist.model.entity.Shop
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.viewModel.createWishlistViewModel
import com.tokopedia.home_wishlist.viewModel.givenGetImageData
import com.tokopedia.home_wishlist.viewModel.givenGetWishlistDataReturnsThis
import com.tokopedia.home_wishlist.viewmodel.WishlistViewModel
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import rx.Subscriber

/**
 * @author by Lukas on 25/07/20.
 */

class WishlistViewModelAddToCartTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var wishlistViewModel: WishlistViewModel
    private val addToCartUseCase = mockk<AddToCartUseCase>(relaxed = true)
    private val getWishlistDataUseCase = mockk<GetWishlistDataUseCase>(relaxed = true)
    private val updateCartCounterUseCase = mockk<UpdateCartCounterUseCase>(relaxed = true)
    private val topAdsImageViewUseCase = mockk<TopAdsImageViewUseCase>(relaxed = true)

    @Test
    fun `Add to cart product success will trigger AddToCartActionData and add to cart progress flag is set to false`(){
        val mockProductCardPositionCandidate = 1
        val mockId1 = "11"
        val mockId2 = "22"

        val shopId1 = "99"
        val shopId2 = "88"
        val minimumOrder = 1

        // Create wishlist view model
        wishlistViewModel = createWishlistViewModel(addToCartUseCase = addToCartUseCase, getWishlistDataUseCase = getWishlistDataUseCase, updateCartCounterUseCase = updateCartCounterUseCase)

        // Get wishlist data returns this
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(
                listOf(
                        WishlistItem(id=mockId1, shop = Shop(id = shopId1), minimumOrder = minimumOrder),
                        WishlistItem(id=mockId2, shop = Shop(id = shopId2), minimumOrder = minimumOrder)
                )
        )

        every { updateCartCounterUseCase.createObservable(any()).subscribeOn(any())
                .unsubscribeOn(any())
                .observeOn(any()).subscribe(any<Subscriber<Int>>()) }.answers {
            (firstArg() as Subscriber<Int>).onNext(10)
            firstArg()
        }

        // wishlistViewModel get wishlist data
        wishlistViewModel.getWishlistData()
        // Add to cart success and return AddToCartDataModel
        every { addToCartUseCase.execute(any(), any()) }.answers {
            (secondArg() as Subscriber<AddToCartDataModel>).onNext(
                    AddToCartDataModel(
                            status = AddToCartDataModel.STATUS_OK,
                            data = DataModel(success = 1, productId = mockId2.toLong(), message = arrayListOf(""))
                    )
            )
        }

        // Add to cart from wishlist
        wishlistViewModel.addToCartProduct(mockProductCardPositionCandidate)

        // Expect add to cart event is triggered with selected product Id
        val wishlistAddToCartActionData = wishlistViewModel.addToCartActionData.value!!
        Assert.assertEquals(mockProductCardPositionCandidate, wishlistAddToCartActionData.peekContent().position)
        Assert.assertEquals(mockId2, wishlistAddToCartActionData.peekContent().productId.toString())
        Assert.assertEquals(true, wishlistAddToCartActionData.peekContent().isSuccess)

        // Expect add to cart event can only retrieved once
        val wishlistEventData = wishlistViewModel.addToCartActionData.value!!
        wishlistEventData.getContentIfNotHandled()
        val eventAddToCartSecond = wishlistEventData.getContentIfNotHandled()
        Assert.assertEquals(null, eventAddToCartSecond)

        // Expect visitable item candidate add to cart progress is false
        val wishlistData = wishlistViewModel.wishlistLiveData.value!!
        val wishlistVisitableItem = wishlistData[mockProductCardPositionCandidate]
        Assert.assertEquals(WishlistItemDataModel::class.java, wishlistVisitableItem.javaClass)
        Assert.assertEquals(false, (wishlistVisitableItem as WishlistItemDataModel).isOnAddToCartProgress)

        // Expect selected product addToCart is not in addToCartProgress
        val wishlistLiveData = wishlistViewModel.wishlistLiveData.value!!
        val wishlistAddToCartActionVisitableItem = wishlistLiveData[mockProductCardPositionCandidate] as WishlistItemDataModel
        Assert.assertEquals(false, wishlistAddToCartActionVisitableItem.isOnAddToCartProgress)
        Assert.assertTrue(wishlistViewModel.updateCartCounterActionData.value != null)
    }

    @Test
    fun `Add to cart product success will trigger AddToCartActionData and add to cart progress flag is set to false and product position -1`(){
        val mockProductCardPositionCandidate = -1
        val mockId1 = "11"
        val mockId2 = "22"

        val shopId1 = "99"
        val shopId2 = "88"
        val minimumOrder = 1

        // Create wishlist view model
        wishlistViewModel = createWishlistViewModel(addToCartUseCase = addToCartUseCase, getWishlistDataUseCase = getWishlistDataUseCase, updateCartCounterUseCase = updateCartCounterUseCase)

        // Get wishlist data returns this
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(
                listOf(
                        WishlistItem(id=mockId1, shop = Shop(id = shopId1), minimumOrder = minimumOrder),
                        WishlistItem(id=mockId2, shop = Shop(id = shopId2), minimumOrder = minimumOrder)
                )
        )

        every { updateCartCounterUseCase.createObservable(any()).subscribeOn(any())
                .unsubscribeOn(any())
                .observeOn(any()).subscribe(any<Subscriber<Int>>()) }.answers {
            (firstArg() as Subscriber<Int>).onNext(10)
            firstArg()
        }

        // wishlistViewModel get wishlist data
        wishlistViewModel.getWishlistData()
        // Add to cart success and return AddToCartDataModel
        every { addToCartUseCase.execute(any(), any()) }.answers {
            (secondArg() as Subscriber<AddToCartDataModel>).onNext(
                    AddToCartDataModel(
                            status = AddToCartDataModel.STATUS_OK,
                            data = DataModel(success = 1, productId = mockId2.toLong(), message = arrayListOf(""))
                    )
            )
            (secondArg() as Subscriber<AddToCartDataModel>).onCompleted()
        }

        // Add to cart from wishlist
        wishlistViewModel.addToCartProduct(mockProductCardPositionCandidate)

        // Expect add to cart event is triggered with selected product Id
        assert(wishlistViewModel.addToCartActionData.value == null)
    }

    @Test
    fun `Add to cart product success will trigger AddToCartActionData and add to cart progress flag is set to false and product position more than item`(){
        val mockProductCardPositionCandidate = 100
        val mockId1 = "11"
        val mockId2 = "22"
        val mockId3 = "33"
        val mockId4 = "44"
        val mockId5 = "55"
        val mockId6 = "66"
        val mockId7 = "77"

        val shopId1 = "99"
        val shopId2 = "88"
        val minimumOrder = 1

        // Create wishlist view model
        wishlistViewModel = createWishlistViewModel(addToCartUseCase = addToCartUseCase, getWishlistDataUseCase = getWishlistDataUseCase, updateCartCounterUseCase = updateCartCounterUseCase, topAdsImageViewUseCase = topAdsImageViewUseCase)

        topAdsImageViewUseCase.givenGetImageData(arrayListOf(TopAdsImageViewModel(imageUrl = "")))

        // Get wishlist data returns this
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(
                listOf(
                        WishlistItem(id=mockId1, shop = Shop(id = shopId1), minimumOrder = minimumOrder),
                        WishlistItem(id=mockId2, shop = Shop(id = shopId2), minimumOrder = minimumOrder),
                        WishlistItem(id=mockId3, shop = Shop(id = shopId2), minimumOrder = minimumOrder),
                        WishlistItem(id=mockId4, shop = Shop(id = shopId2), minimumOrder = minimumOrder),
                        WishlistItem(id=mockId5, shop = Shop(id = shopId2), minimumOrder = minimumOrder),
                        WishlistItem(id=mockId6, shop = Shop(id = shopId2), minimumOrder = minimumOrder),
                        WishlistItem(id=mockId7, shop = Shop(id = shopId2), minimumOrder = minimumOrder)
                )
        )

        every { updateCartCounterUseCase.createObservable(any()).subscribeOn(any())
                .unsubscribeOn(any())
                .observeOn(any()).subscribe(any<Subscriber<Int>>()) }.answers {
            (firstArg() as Subscriber<Int>).onNext(10)
            firstArg()
        }

        // wishlistViewModel get wishlist data
        wishlistViewModel.getWishlistData()
        // Add to cart success and return AddToCartDataModel
        every { addToCartUseCase.execute(any(), any()) }.answers {
            (secondArg() as Subscriber<AddToCartDataModel>).onNext(
                    AddToCartDataModel(
                            status = AddToCartDataModel.STATUS_OK,
                            data = DataModel(success = 1, productId = mockId2.toLong(), message = arrayListOf(""))
                    )
            )
            (secondArg() as Subscriber<AddToCartDataModel>).onCompleted()
        }

        // Add to cart from wishlist
        wishlistViewModel.addToCartProduct(mockProductCardPositionCandidate)

        // Expect add to cart event is triggered with selected product Id
        assert(wishlistViewModel.addToCartActionData.value == null)

        // Add to cart from wishlist
        wishlistViewModel.addToCartProduct(4)

        // Expect add to cart event is triggered with selected product Id
        assert(wishlistViewModel.addToCartActionData.value != null)
    }
    
    @Test
    fun `Add to cart product failed will trigger AddToCartActionData and add to cart progress flag is set to false`(){
        val mockId1 = "11"
        val mockId2 = "22"

        val shopId1 = "99"
        val shopId2 = "88"
        val minimumOrder = 1

        val mockErrorMessage = "NOT YA"

        //Create wishlist view model
        wishlistViewModel = createWishlistViewModel(getWishlistDataUseCase = getWishlistDataUseCase, addToCartUseCase = addToCartUseCase)
        
        // Get wishlist data returns this
            getWishlistDataUseCase.givenGetWishlistDataReturnsThis(
                    listOf(
                            WishlistItem(id=mockId1, shop = Shop(id = shopId1), minimumOrder = minimumOrder),
                            WishlistItem(id=mockId2, shop = Shop(id = shopId2), minimumOrder = minimumOrder)
                    )
            )
        
        // WishlistViewModel get wishlist data
        wishlistViewModel.getWishlistData()
        
        // Product candidate for add to cart position
        val mockProductCardPositionCandidate = 1
        // Add to cart failed and return AddToCartDataModel with error and add to cart progress flag is set to false
        every { addToCartUseCase.execute(any(), any()) }.answers {
            (secondArg() as Subscriber<AddToCartDataModel>).onNext(
                    AddToCartDataModel(
                            status = "NOT OK",
                            data = DataModel(success = 0, productId = mockId2.toLong(), message = arrayListOf(mockErrorMessage)),
                            errorMessage = arrayListOf(mockErrorMessage)
                    )
            )
        }

        // Add to cart from wishlist
        wishlistViewModel.addToCartProduct(mockProductCardPositionCandidate)

        // Expect add to cart event is triggered with selected product Id and error message
        Assert.assertEquals(mockProductCardPositionCandidate, wishlistViewModel.addToCartActionData.value!!.peekContent().position)
        Assert.assertEquals(mockId2, wishlistViewModel.addToCartActionData.value!!.peekContent().productId.toString())
        Assert.assertEquals(mockErrorMessage, wishlistViewModel.addToCartActionData.value!!.peekContent().message)
        Assert.assertEquals(false, wishlistViewModel.addToCartActionData.value!!.peekContent().isSuccess)

        // Expect add to cart event can only retrieved once
        wishlistViewModel.addToCartActionData.value!!.getContentIfNotHandled()
        val eventAddToCartSecond = wishlistViewModel.addToCartActionData.value!!.getContentIfNotHandled()
        Assert.assertEquals(eventAddToCartSecond, null)

        // Expect visitable item candidate add to cart progress is false
        val wishlistDataModel = wishlistViewModel.wishlistLiveData.value!![mockProductCardPositionCandidate]
        Assert.assertEquals(WishlistItemDataModel::class.java, wishlistDataModel.javaClass)
        Assert.assertEquals(false, (wishlistDataModel as WishlistItemDataModel).isOnAddToCartProgress)

        // Expect selected product addToCart is not in addToCartProgress
        val wishlistAddToCartActionData = wishlistViewModel.wishlistLiveData.value!!
        val wishlistVisitableItem = wishlistAddToCartActionData[mockProductCardPositionCandidate] as WishlistItemDataModel
        Assert.assertEquals(false, wishlistVisitableItem.isOnAddToCartProgress)
    }
    
    @Test
    fun `Add to cart throws error will trigger AddToCartActionData and add to cart progress flag is set to false`(){
        val mockId1 = "11"
        val mockId2 = "22"

        val shopId1 = "99"
        val shopId2 = "88"
        val minimumOrder = 1

        val mockErrorMessage = "NOT YA"

        // Create wishlist view model
        wishlistViewModel = createWishlistViewModel(getWishlistDataUseCase = getWishlistDataUseCase, addToCartUseCase = addToCartUseCase)

        // Get wishlist data returns this
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(
                listOf(
                        WishlistItem(id=mockId1, shop = Shop(id = shopId1), minimumOrder = minimumOrder),
                        WishlistItem(id=mockId2, shop = Shop(id = shopId2), minimumOrder = minimumOrder)
                )
        )

        // WishlistViewModel get wishlist data
        wishlistViewModel.getWishlistData()
        // Product candidate for add to cart position
        val mockProductCardPositionCandidate = 1

        // Add to cart failed and return AddToCartDataModel with error and add to cart progress flag is set to false
        every { addToCartUseCase.execute(any(), any()) }.answers {
            (secondArg() as Subscriber<AddToCartDataModel>).onError(Throwable(mockErrorMessage))
        }

        // Add to cart from wishlist
        wishlistViewModel.addToCartProduct(mockProductCardPositionCandidate)

        // Expect add to cart event is triggered with error message
        Assert.assertEquals(mockProductCardPositionCandidate, wishlistViewModel.addToCartActionData.value!!.peekContent().position)
        Assert.assertEquals(false, wishlistViewModel.addToCartActionData.value!!.peekContent().isSuccess)

        // Expect add to cart event can only retrieved once
        wishlistViewModel.addToCartActionData.value!!.getContentIfNotHandled()
        val eventAddToCartSecond = wishlistViewModel.addToCartActionData.value!!.getContentIfNotHandled()
        Assert.assertEquals(eventAddToCartSecond, null)


        // Expect visitable item candidate add to cart progress is false
        val wishlistDataModel = wishlistViewModel.wishlistLiveData.value!![mockProductCardPositionCandidate]
        Assert.assertEquals(WishlistItemDataModel::class.java, wishlistDataModel.javaClass)
        Assert.assertEquals(false, (wishlistDataModel as WishlistItemDataModel).isOnAddToCartProgress)

        // Expect selected product addToCart is not in addToCartProgress
        val wishlistAddToCartActionData = wishlistViewModel.wishlistLiveData.value!!
        val wishlistVisitableItem = wishlistAddToCartActionData[mockProductCardPositionCandidate] as WishlistItemDataModel
        Assert.assertEquals(false, wishlistVisitableItem.isOnAddToCartProgress)
    }
}