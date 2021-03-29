package com.tokopedia.home_wishlist.viewModel.recommendation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home_wishlist.domain.GetWishlistDataUseCase
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselDataModel
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
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 25/07/20.
 */
class WishlistViewModelOnRecommendationTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var wishlistViewModel: WishlistViewModel
    private val addWishListUseCase = mockk<AddWishListUseCase>(relaxed = true)
    private val removeWishlistUseCase = mockk<RemoveWishListUseCase>(relaxed = true)
    private val userSessionInterface = mockk<UserSessionInterface>(relaxed = true)
    private val getWishlistDataUseCase = mockk<GetWishlistDataUseCase>(relaxed = true)
    private val getRecommendationUseCase = mockk<GetRecommendationUseCase>(relaxed = true)
    private val topAdsImageViewUseCase = mockk<TopAdsImageViewUseCase>(relaxed = true)

    @Test
    fun `Set wishlist success with initial state false should update product isWishlist to true in wishlist data`() {
        val mockUserId = "54321"
        val parentPositionCandidate = 25
        val childPositionCandidate = 2
        val wishlistedInitialState = false

        // Wishlist viewmodel
        wishlistViewModel = createWishlistViewModel(
                addWishListUseCase = addWishListUseCase,
                removeWishlistUseCase = removeWishlistUseCase,
                userSessionInterface = userSessionInterface,
                getWishlistDataUseCase = getWishlistDataUseCase,
                getRecommendationUseCase = getRecommendationUseCase,
                topAdsImageViewUseCase = topAdsImageViewUseCase
        )

        // topads set image
        topAdsImageViewUseCase.givenGetImageData(arrayListOf(TopAdsImageViewModel(imageUrl = "")))

        // Get wishlist usecase returns wishlist data above recommendation threshold (4)
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                WishlistItem(id="1"), WishlistItem(id="2"), WishlistItem(id="3"), WishlistItem(id="4"), WishlistItem(id="5"),
                WishlistItem(id="6"), WishlistItem(id="7"), WishlistItem(id="8"), WishlistItem(id="9"), WishlistItem(id="10"),
                WishlistItem(id="11"), WishlistItem(id="12"), WishlistItem(id="13"), WishlistItem(id="14"), WishlistItem(id="15"),
                WishlistItem(id="16"), WishlistItem(id="17"), WishlistItem(id="18"), WishlistItem(id="19"), WishlistItem(id="20")
        ))
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                WishlistItem(id="1"), WishlistItem(id="2"), WishlistItem(id="3"), WishlistItem(id="4"), WishlistItem(id="5"),
                WishlistItem(id="6"), WishlistItem(id="7"), WishlistItem(id="8"), WishlistItem(id="9"), WishlistItem(id="10"),
                WishlistItem(id="11"), WishlistItem(id="12"), WishlistItem(id="13"), WishlistItem(id="14"), WishlistItem(id="15"),
                WishlistItem(id="16"), WishlistItem(id="17"), WishlistItem(id="18"), WishlistItem(id="19"), WishlistItem(id="20")
        ), page = 2)

        // Get recommendation usecase returns recommendation data
        getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(
                listOf(
                        RecommendationItem(productId = 11),
                        RecommendationItem(productId = 22),
                        RecommendationItem(productId = 33, isWishlist = wishlistedInitialState),
                        RecommendationItem(productId = 44),
                        RecommendationItem(productId = 55)
                )
        )

        // User id
        every { userSessionInterface.userId } returns mockUserId
        // Add wishlist usecase successfully add wishlist
        every { addWishListUseCase.createObservable(any(), mockUserId, any()) }
                .answers {
                    (thirdArg() as WishListActionListener).onSuccessAddWishlist(firstArg())
                }

        // Live data is filled by data from getWishlist
        wishlistViewModel.getWishlistData()

        // get recommendation
        wishlistViewModel.getNextPageWishlistData()


        // View model add a recommendation product
        wishlistViewModel.setRecommendationItemWishlist(parentPositionCandidate, childPositionCandidate, wishlistedInitialState)
        wishlistViewModel.setRecommendationItemWishlist(-1, childPositionCandidate, wishlistedInitialState)
        wishlistViewModel.setRecommendationItemWishlist(-1, 25, wishlistedInitialState)

        // Expect that recommendation item wishlist status is updated to true on wishlist data
        val recommendationCarouselDataModel =
                wishlistViewModel.wishlistLiveData.value!![parentPositionCandidate] as RecommendationCarouselDataModel
        val recommendationCarouselItemDataModel =
                recommendationCarouselDataModel.list[childPositionCandidate]

        Assert.assertEquals(!wishlistedInitialState, recommendationCarouselItemDataModel.recommendationItem.isWishlist)


        // Expect that add wishlist action triggered
        val addWishlistAction = wishlistViewModel.addWishlistRecommendationActionData
        Assert.assertEquals(true, addWishlistAction.value!!.peekContent().isSuccess)

        // Expect remove wishlist action event can only retrieved once
        val wishlistEventAddWishlistActionData = wishlistViewModel.addWishlistRecommendationActionData
        wishlistEventAddWishlistActionData.value!!.getContentIfNotHandled()
        val eventAddWishlistActionDataSecond = wishlistEventAddWishlistActionData.value!!.getContentIfNotHandled()
        Assert.assertEquals(eventAddWishlistActionDataSecond, null)

    }

    @Test
    fun `Set wishlist failed with initial state false should not update product isWishlist in wishlist data`() {
        val mockUserId = "54321"
        val parentPositionCandidate = 25
        val childPositionCandidate = 3
        val wishlistedInitialState = false
        val mockErrorMessage = "OH YA"
        val mockProductId = "33"

        // Wishlist viewmodel
        wishlistViewModel = createWishlistViewModel(
                addWishListUseCase = addWishListUseCase,
                removeWishlistUseCase = removeWishlistUseCase,
                userSessionInterface = userSessionInterface,
                getWishlistDataUseCase = getWishlistDataUseCase,
                getRecommendationUseCase = getRecommendationUseCase,
                topAdsImageViewUseCase = topAdsImageViewUseCase
        )

        // topads set image
        topAdsImageViewUseCase.givenGetImageData(arrayListOf(TopAdsImageViewModel(imageUrl = "")))

        // Get wishlist usecase returns wishlist data above recommendation threshold (4)
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                WishlistItem(id="1"), WishlistItem(id="2"), WishlistItem(id="3"), WishlistItem(id="4"), WishlistItem(id="5"),
                WishlistItem(id="6"), WishlistItem(id="7"), WishlistItem(id="8"), WishlistItem(id="9"), WishlistItem(id="10"),
                WishlistItem(id="11"), WishlistItem(id="12"), WishlistItem(id="13"), WishlistItem(id="14"), WishlistItem(id="15"),
                WishlistItem(id="16"), WishlistItem(id="17"), WishlistItem(id="18"), WishlistItem(id="19"), WishlistItem(id="20")
        ))
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                WishlistItem(id="1"), WishlistItem(id="2"), WishlistItem(id="3"), WishlistItem(id="4"), WishlistItem(id="5"),
                WishlistItem(id="6"), WishlistItem(id="7"), WishlistItem(id="8"), WishlistItem(id="9"), WishlistItem(id="10"),
                WishlistItem(id="11"), WishlistItem(id="12"), WishlistItem(id="13"), WishlistItem(id="14"), WishlistItem(id="15"),
                WishlistItem(id="16"), WishlistItem(id="17"), WishlistItem(id="18"), WishlistItem(id="19"), WishlistItem(id="20")
        ), page = 2)

        // Get recommendation usecase returns recommendation data
        getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(
                listOf(
                        RecommendationItem(productId = 11),
                        RecommendationItem(productId = 22),
                        RecommendationItem(productId = mockProductId.toInt(), isWishlist = wishlistedInitialState),
                        RecommendationItem(productId = 44),
                        RecommendationItem(productId = 55)
                )
        )

        // User id
        every { userSessionInterface.userId } returns mockUserId
        // Add wishlist usecase failed to add wishlist
        every { addWishListUseCase.createObservable(any(), mockUserId, any()) }
                .answers {
                    (thirdArg() as WishListActionListener).onErrorAddWishList(mockErrorMessage, firstArg())
                }

        // Live data is filled by data from getWishlist
        wishlistViewModel.getWishlistData()

        wishlistViewModel.getNextPageWishlistData()

        // View model add a recommendation product
        wishlistViewModel.setRecommendationItemWishlist(parentPositionCandidate, childPositionCandidate, wishlistedInitialState)

        // Expect that recommendation item is not updated in wishlist data
        val recommendationCarouselDataModel =
                wishlistViewModel.wishlistLiveData.value!![parentPositionCandidate] as RecommendationCarouselDataModel
        val recommendationCarouselItemDataModel =
                recommendationCarouselDataModel.list[childPositionCandidate]

        Assert.assertEquals(wishlistedInitialState, recommendationCarouselItemDataModel.recommendationItem.isWishlist)


        // Expect that remove wishlist action triggered
        Assert.assertEquals(false, wishlistViewModel.addWishlistRecommendationActionData.value!!.peekContent().isSuccess)

        // Expect remove wishlist action event can only retrieved once
        val wishlistEventAddWishlistActionData = wishlistViewModel.addWishlistRecommendationActionData
        wishlistEventAddWishlistActionData.value!!.getContentIfNotHandled()
        val eventAddWishlistActionDataSecond = wishlistEventAddWishlistActionData.value!!.getContentIfNotHandled()
        Assert.assertEquals(eventAddWishlistActionDataSecond, null)

    }

    @Test
    fun `Set wishlist success with initial state true should update product isWishlist to false in wishlist data`() {
        val mockUserId = "54321"
        val parentPositionCandidate = 25
        val childPositionCandidate = 2
        val wishlistedInitialState = true
        val mockProductId = "33"

        // Wishlist viewmodel

        wishlistViewModel = createWishlistViewModel(
                addWishListUseCase = addWishListUseCase,
                removeWishlistUseCase = removeWishlistUseCase,
                userSessionInterface = userSessionInterface,
                getWishlistDataUseCase = getWishlistDataUseCase,
                getRecommendationUseCase = getRecommendationUseCase,
                topAdsImageViewUseCase = topAdsImageViewUseCase
        )

        // topads set image
        topAdsImageViewUseCase.givenGetImageData(arrayListOf(TopAdsImageViewModel(imageUrl = "")))

        // Get wishlist usecase returns wishlist data above recommendation threshold (4)
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                WishlistItem(id="1"), WishlistItem(id="2"), WishlistItem(id="3"), WishlistItem(id="4"), WishlistItem(id="5"),
                WishlistItem(id="6"), WishlistItem(id="7"), WishlistItem(id="8"), WishlistItem(id="9"), WishlistItem(id="10"),
                WishlistItem(id="11"), WishlistItem(id="12"), WishlistItem(id="13"), WishlistItem(id="14"), WishlistItem(id="15"),
                WishlistItem(id="16"), WishlistItem(id="17"), WishlistItem(id="18"), WishlistItem(id="19"), WishlistItem(id="20")
        ))
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                WishlistItem(id="1"), WishlistItem(id="2"), WishlistItem(id="3"), WishlistItem(id="4"), WishlistItem(id="5"),
                WishlistItem(id="6"), WishlistItem(id="7"), WishlistItem(id="8"), WishlistItem(id="9"), WishlistItem(id="10"),
                WishlistItem(id="11"), WishlistItem(id="12"), WishlistItem(id="13"), WishlistItem(id="14"), WishlistItem(id="15"),
                WishlistItem(id="16"), WishlistItem(id="17"), WishlistItem(id="18"), WishlistItem(id="19"), WishlistItem(id="20")
        ), page = 2)

        // Get recommendation usecase returns recommendation data
        getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(
                listOf(
                        RecommendationItem(productId = 11),
                        RecommendationItem(productId = 22),
                        RecommendationItem(productId = mockProductId.toInt(), isWishlist = wishlistedInitialState),
                        RecommendationItem(productId = 44),
                        RecommendationItem(productId = 55)
                )
        )

        // User id
        every { userSessionInterface.userId } returns mockUserId
        // Remove wishlist usecase successfully remove wishlist
        every { removeWishlistUseCase.createObservable(any(), mockUserId, any()) }
                .answers {
                    (thirdArg() as WishListActionListener).onSuccessRemoveWishlist(firstArg())
                }

        // Live data is filled by data from getWishlist
        wishlistViewModel.getWishlistData()
        wishlistViewModel.getNextPageWishlistData()


        // View model add a recommendation product
        wishlistViewModel.setRecommendationItemWishlist(parentPositionCandidate, childPositionCandidate, wishlistedInitialState)

        // Expect that recommendation item wishlist status is updated to false on wishlist data
        val recommendationCarouselDataModel =
                wishlistViewModel.wishlistLiveData.value!![parentPositionCandidate] as RecommendationCarouselDataModel
        val recommendationCarouselItemDataModel =
                recommendationCarouselDataModel.list[childPositionCandidate]

        Assert.assertEquals(!wishlistedInitialState, recommendationCarouselItemDataModel.recommendationItem.isWishlist)

        // Expect that remove wishlist action triggered
        val removeWishlistAction = wishlistViewModel.removeWishlistRecommendationActionData
        Assert.assertEquals(true, removeWishlistAction.value!!.peekContent().isSuccess)

        // Expect remove wishlist action event can only retrieved once
        val wishlistEventRemoveWishlistActionData = wishlistViewModel.removeWishlistRecommendationActionData
        wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
        val eventRemoveWishlistActionDataSecond = wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
        Assert.assertEquals(eventRemoveWishlistActionDataSecond, null)

    }

    @Test
    fun `Set wishlist failed with initial state true should not update product isWishlist in wishlist data`() {
        val mockUserId = "54321"
        val parentPositionCandidate = 25
        val childPositionCandidate = 2
        val wishlistedInitialState = true
        val mockErrorMessage = "OH YA"
        val mockProductId = "33"

        // Wishlist viewmodel

        wishlistViewModel = createWishlistViewModel(
                addWishListUseCase = addWishListUseCase,
                removeWishlistUseCase = removeWishlistUseCase,
                userSessionInterface = userSessionInterface,
                getWishlistDataUseCase = getWishlistDataUseCase,
                getRecommendationUseCase = getRecommendationUseCase,
                topAdsImageViewUseCase = topAdsImageViewUseCase
        )

        // topads set image
        topAdsImageViewUseCase.givenGetImageData(arrayListOf(TopAdsImageViewModel(imageUrl = "")))

        // Get wishlist usecase returns wishlist data above recommendation threshold (4)
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                WishlistItem(id="1"), WishlistItem(id="2"), WishlistItem(id="3"), WishlistItem(id="4"), WishlistItem(id="5"),
                WishlistItem(id="6"), WishlistItem(id="7"), WishlistItem(id="8"), WishlistItem(id="9"), WishlistItem(id="10"),
                WishlistItem(id="11"), WishlistItem(id="12"), WishlistItem(id="13"), WishlistItem(id="14"), WishlistItem(id="15"),
                WishlistItem(id="16"), WishlistItem(id="17"), WishlistItem(id="18"), WishlistItem(id="19"), WishlistItem(id="20")
        ))
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                WishlistItem(id="1"), WishlistItem(id="2"), WishlistItem(id="3"), WishlistItem(id="4"), WishlistItem(id="5"),
                WishlistItem(id="6"), WishlistItem(id="7"), WishlistItem(id="8"), WishlistItem(id="9"), WishlistItem(id="10"),
                WishlistItem(id="11"), WishlistItem(id="12"), WishlistItem(id="13"), WishlistItem(id="14"), WishlistItem(id="15"),
                WishlistItem(id="16"), WishlistItem(id="17"), WishlistItem(id="18"), WishlistItem(id="19"), WishlistItem(id="20")
        ), page = 2)

        // Get recommendation usecase returns recommendation data
        getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(
                listOf(
                        RecommendationItem(productId = 11),
                        RecommendationItem(productId = 22),
                        RecommendationItem(productId = mockProductId.toInt(), isWishlist = wishlistedInitialState),
                        RecommendationItem(productId = 44),
                        RecommendationItem(productId = 55)
                )
        )

        // Live data is filled by data from getWishlist
        wishlistViewModel.getWishlistData()
        wishlistViewModel.getNextPageWishlistData()

        // User id
        every { userSessionInterface.userId } returns mockUserId
        // Remove wishlist usecase successfully remove wishlist
        every { removeWishlistUseCase.createObservable(any(), mockUserId, any()) }
                .answers {
                    (thirdArg() as WishListActionListener).onErrorRemoveWishlist(mockErrorMessage, firstArg())
                }


        // View model add a recommendation product
        wishlistViewModel.setRecommendationItemWishlist(parentPositionCandidate, childPositionCandidate, wishlistedInitialState)

        // Expect that recommendation item wishlist status is not updated in wishlist data
        val recommendationCarouselDataModel =
                wishlistViewModel.wishlistLiveData.value!![parentPositionCandidate] as RecommendationCarouselDataModel
        val recommendationCarouselItemDataModel =
                recommendationCarouselDataModel.list[childPositionCandidate]

        Assert.assertEquals(wishlistedInitialState, recommendationCarouselItemDataModel.recommendationItem.isWishlist)

        // Expect that remove wishlist action triggered
        val removeWishlistAction = wishlistViewModel.removeWishlistRecommendationActionData
        Assert.assertEquals(false, removeWishlistAction.value!!.peekContent().isSuccess)

        // Expect remove wishlist action event can only retrieved once
        val wishlistEventRemoveWishlistActionData = wishlistViewModel.removeWishlistRecommendationActionData
        wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
        val eventRemoveWishlistActionDataSecond = wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
        Assert.assertEquals(eventRemoveWishlistActionDataSecond, null)

    }
}