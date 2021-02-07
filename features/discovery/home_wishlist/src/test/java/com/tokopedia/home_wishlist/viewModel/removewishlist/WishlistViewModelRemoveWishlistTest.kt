package com.tokopedia.home_wishlist.viewModel.removewishlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home_wishlist.domain.GetWishlistDataUseCase
import com.tokopedia.home_wishlist.model.datamodel.BannerTopAdsDataModel
import com.tokopedia.home_wishlist.model.datamodel.WishlistItemDataModel
import com.tokopedia.home_wishlist.model.entity.WishlistEntityData
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.viewModel.createWishlistViewModel
import com.tokopedia.home_wishlist.viewModel.givenGetImageData
import com.tokopedia.home_wishlist.viewModel.givenGetWishlistDataReturnsThis
import com.tokopedia.home_wishlist.viewModel.givenRepositoryGetRecommendationDataReturnsThis
import com.tokopedia.home_wishlist.viewmodel.WishlistViewModel
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 25/07/20.
 */
class WishlistViewModelRemoveWishlistTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var wishlistViewModel: WishlistViewModel
    private val removeWishlistUseCase = mockk<RemoveWishListUseCase>(relaxed = true)
    private val userSessionInterface = mockk<UserSessionInterface>(relaxed = true)
    private val getWishlistDataUseCase = mockk<GetWishlistDataUseCase>(relaxed = true)
    private val getRecommendationUseCase = mockk<GetRecommendationUseCase>(relaxed = true)
    private val topAdsImageViewUseCase = mockk<TopAdsImageViewUseCase>(relaxed = true)

    @Test
    fun `Remove wishlist success should remove data from wishlistLiveData`() {
        val mockSelectedPosition = 2
        val mockProductId = "3"
        val mockUserId = "54321"

        // Wishlist viewmodel
        wishlistViewModel = createWishlistViewModel(removeWishlistUseCase = removeWishlistUseCase, userSessionInterface = userSessionInterface, getWishlistDataUseCase = getWishlistDataUseCase, getRecommendationUseCase = getRecommendationUseCase, topAdsImageViewUseCase = topAdsImageViewUseCase)

        topAdsImageViewUseCase.givenGetImageData(arrayListOf(TopAdsImageViewModel(imageUrl = "")))

        // Get wishlist usecase returns wishlist data below recommendation threshold (4)
        coEvery { getWishlistDataUseCase.getData(any()) } returns WishlistEntityData(
                items = listOf(
                        WishlistItem(id="1"), WishlistItem(id="2"), WishlistItem(id="3"), WishlistItem(id="4"), WishlistItem(id="5"),
                        WishlistItem(id="6"), WishlistItem(id="7"), WishlistItem(id="8"), WishlistItem(id="9"), WishlistItem(id="10"),
                        WishlistItem(id="11"), WishlistItem(id="12"), WishlistItem(id="13"), WishlistItem(id="14"), WishlistItem(id="15"),
                        WishlistItem(id="16"), WishlistItem(id="17"), WishlistItem(id="18"), WishlistItem(id="19"), WishlistItem(id="20")
                )
        ) andThen WishlistEntityData(
                items = listOf(
                        WishlistItem(id="21"), WishlistItem(id="22"), WishlistItem(id="23"), WishlistItem(id="24"), WishlistItem(id="25"),
                        WishlistItem(id="26"), WishlistItem(id="27"), WishlistItem(id="28"), WishlistItem(id="29"), WishlistItem(id="30"),
                        WishlistItem(id="31"), WishlistItem(id="32"), WishlistItem(id="33"), WishlistItem(id="34"), WishlistItem(id="35"),
                        WishlistItem(id="36"), WishlistItem(id="37"), WishlistItem(id="38"), WishlistItem(id="39"), WishlistItem(id="40")
                )
        )

        getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(listOf(
                RecommendationItem()
        ))

        // wishlistViewModel get wishlist data
        wishlistViewModel.getWishlistData()
        wishlistViewModel.getNextPageWishlistData()
        // User id
        every { userSessionInterface.userId } returns mockUserId
        // Remove wishlist usecase successfully remove wishlist
        every { removeWishlistUseCase.createObservable(any(), mockUserId, any()) }
                .answers {
                    (thirdArg() as WishListActionListener).onSuccessRemoveWishlist(mockProductId)
                }

        // View model remove a wishlist product
        wishlistViewModel.removeWishlistedProduct(4)
        wishlistViewModel.removeWishlistedProduct(-1)
        wishlistViewModel.removeWishlistedProduct(mockSelectedPosition)

        // Expect that unwishlisted product is removed from wishlistLiveData
        Assert.assertEquals(41, wishlistViewModel.wishlistLiveData.value!!.size)
        wishlistViewModel.wishlistLiveData.value!!.forEach {
            if (it is WishlistItemDataModel) {
                if (it.productItem.id == mockProductId) {
                    Assert.assertFalse("Unwishlisted product still exist!", true)
                }
            }
        }

        // Expect that remove wishlist action triggered
        val removeWishlistActionData = wishlistViewModel.removeWishlistActionData
        Assert.assertEquals(true, removeWishlistActionData.value!!.peekContent().isSuccess)

        // Expect remove wishlist action event can only retrieved once
        val wishlistEventRemoveWishlistActionData = wishlistViewModel.removeWishlistActionData
        wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
        val eventRemoveWishlistActionDataSecond = wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
        Assert.assertEquals(eventRemoveWishlistActionDataSecond, null)

    }

    @Test
    fun `Remove wishlist failed should not remove data from wishlistLiveData`() {
        val mockSelectedPosition = 2
        val mockProductId = "3"
        val mockUserId = "54321"
        val mockErrorMessage = "NOT YA"

        // Wishlist viewmodel
        wishlistViewModel = createWishlistViewModel(removeWishlistUseCase = removeWishlistUseCase, userSessionInterface = userSessionInterface, getWishlistDataUseCase = getWishlistDataUseCase, getRecommendationUseCase = getRecommendationUseCase, topAdsImageViewUseCase = topAdsImageViewUseCase)

        topAdsImageViewUseCase.givenGetImageData(arrayListOf(TopAdsImageViewModel(imageUrl = "")))

        // Get wishlist usecase returns wishlist data below recommendation threshold (4)
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(
                listOf(
                        WishlistItem(id="1"),
                        WishlistItem(id="2"),
                        WishlistItem(id="3")
                )
        )

        // wishlistViewModel get wishlist data
        wishlistViewModel.getWishlistData()
        // User id
        every { userSessionInterface.userId } returns mockUserId
        // Remove wishlist usecase failed to remove wishlist
        every { removeWishlistUseCase.createObservable(any(), mockUserId, any()) }
                .answers {
                    (thirdArg() as WishListActionListener).onErrorRemoveWishlist(mockErrorMessage, mockProductId)
                }


        // View model remove a wishlist product
        wishlistViewModel.removeWishlistedProduct(mockSelectedPosition)

        // Expect that unwishlisted product is not removed from wishlistLiveData
        Assert.assertEquals(3, wishlistViewModel.wishlistLiveData.value!!.size)
        wishlistViewModel.wishlistLiveData.value!!.forEach {
            if (it is WishlistItemDataModel) {
                if (it.productItem.id == mockProductId) {
                    Assert.assertFalse("Unwishlisted product still in exist", false)
                }
            }
        }

        // Expect that remove wishlist action triggered
        val removeWishlistActionData = wishlistViewModel.removeWishlistActionData
        Assert.assertEquals(false, removeWishlistActionData.value!!.peekContent().isSuccess)
        Assert.assertEquals(mockErrorMessage, removeWishlistActionData.value!!.peekContent().message)

        // Expect remove wishlist action event can only retrieved once
        val wishlistEventRemoveWishlistActionData = wishlistViewModel.removeWishlistActionData
        wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
        val eventRemoveWishlistActionDataSecond = wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
        Assert.assertEquals(eventRemoveWishlistActionDataSecond, null)

    }

    @Test
    fun `Remove wishlist data should not change recommendation widget position`() {
        val mockSelectedPosition = 2
        val mockProductId = "3"
        val mockUserId = "54321"

        // Wishlist viewmodel
        wishlistViewModel = createWishlistViewModel(removeWishlistUseCase = removeWishlistUseCase, userSessionInterface = userSessionInterface, getWishlistDataUseCase = getWishlistDataUseCase, getRecommendationUseCase = getRecommendationUseCase, topAdsImageViewUseCase = topAdsImageViewUseCase)

        topAdsImageViewUseCase.givenGetImageData(arrayListOf(TopAdsImageViewModel(imageUrl = "")))

        // Get wishlist usecase returns wishlist data below recommendation threshold (4)
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(
                useDefaultWishlistItem = true
        )

        // Get recommendation usecase returns 1 recommendation widget
        getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(listOf())
        // wishlistViewModel get wishlist data
        wishlistViewModel.getWishlistData()
        // User id
        every { userSessionInterface.userId } returns mockUserId
        // Remove wishlist usecase successfully remove wishlist
        every { removeWishlistUseCase.createObservable(any(), mockUserId, any()) }
                .answers {
                    (thirdArg() as WishListActionListener).onSuccessRemoveWishlist(mockProductId)
                }


        // View model remove a wishlist product
        wishlistViewModel.removeWishlistedProduct(mockSelectedPosition)

        // Expect that unwishlisted product is removed from wishlistLiveData
        Assert.assertEquals(20, wishlistViewModel.wishlistLiveData.value!!.size)
        wishlistViewModel.wishlistLiveData.value!!.forEach {
            if (it is WishlistItemDataModel) {
                if (it.productItem.id == mockProductId) {
                    Assert.assertFalse("Unwishlisted product still in exist", false)
                }
            }
        }

        // Expect that recommendation section position is not changed in position 4
        Assert.assertEquals(BannerTopAdsDataModel::class.java,
                wishlistViewModel.wishlistLiveData.value!![3].javaClass)

    }

    @Test
    fun `Remove wishlist data only 1 item`() {
        val mockSelectedPosition = 0
        val mockProductId = "1"
        val mockUserId = "54321"

        // Wishlist viewmodel
        wishlistViewModel = createWishlistViewModel(removeWishlistUseCase = removeWishlistUseCase, userSessionInterface = userSessionInterface, getWishlistDataUseCase = getWishlistDataUseCase, getRecommendationUseCase = getRecommendationUseCase, topAdsImageViewUseCase = topAdsImageViewUseCase)

        // Get wishlist usecase returns wishlist data below recommendation threshold (4)
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(
                wishlistItems = listOf(
                        WishlistItem(id = "1")
                )
        )

        // wishlistViewModel get wishlist data
        wishlistViewModel.getWishlistData()
        // User id
        every { userSessionInterface.userId } returns mockUserId
        // Remove wishlist usecase successfully remove wishlist
        every { removeWishlistUseCase.createObservable(any(), mockUserId, any()) }
                .answers {
                    (thirdArg() as WishListActionListener).onSuccessRemoveWishlist(mockProductId)
                }


        // View model remove a wishlist product
        wishlistViewModel.removeWishlistedProduct(mockSelectedPosition)

        // Expect that unwishlisted product is removed from wishlistLiveData
        Assert.assertEquals(1, wishlistViewModel.wishlistLiveData.value!!.size)
        wishlistViewModel.wishlistLiveData.value!!.forEach {
            if (it is WishlistItemDataModel) {
                if (it.productItem.id == mockProductId) {
                    Assert.assertFalse("Unwishlisted product still in exist", false)
                }
            }
        }

    }

}