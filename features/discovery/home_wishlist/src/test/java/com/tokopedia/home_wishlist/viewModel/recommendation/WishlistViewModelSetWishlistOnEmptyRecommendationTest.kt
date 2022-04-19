package com.tokopedia.home_wishlist.viewModel.recommendation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home_wishlist.domain.GetWishlistDataUseCase
import com.tokopedia.home_wishlist.model.datamodel.RecommendationItemDataModel
import com.tokopedia.home_wishlist.viewModel.createWishlistViewModel
import com.tokopedia.home_wishlist.viewModel.givenGetSingleRecommendationReturnsThis
import com.tokopedia.home_wishlist.viewModel.givenGetWishlistDataReturnsThis
import com.tokopedia.home_wishlist.viewmodel.WishlistViewModel
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
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
class WishlistViewModelSetWishlistOnEmptyRecommendationTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var wishlistViewmodel: WishlistViewModel
    private val addWishListUseCase = mockk<AddWishListUseCase>()
    private val removeWishlistUseCase = mockk<RemoveWishListUseCase>()
    private val userSessionInterface = mockk<UserSessionInterface>(relaxed = true)
    private val getWishlistDataUseCase = mockk<GetWishlistDataUseCase>(relaxed = true)
    private val getSingleRecommendationUseCase = mockk<GetSingleRecommendationUseCase>(relaxed = true)

    @Test
    fun `Set wishlist success with initial state false should update product isWishlist to true in wishlist data`() {
        val mockUserId = "54321"

        //4 because empty wishlist data is structured:
        // - empty data model
        // - recom title data model
        // - recom 1
        // - recom 2 ------> selected
        // - recom 3
        // - recom 4
        val childPositionCandidate = 4
        val wishlistedInitialState = false

        // Wishlist viewmodel
        wishlistViewmodel = createWishlistViewModel(
                userSessionInterface = userSessionInterface,
                getWishlistDataUseCase = getWishlistDataUseCase,
                getSingleRecommendationUseCase = getSingleRecommendationUseCase,
                addWishListUseCase = addWishListUseCase)

        // Get wishlist usecase returns empty wishlist data
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf())
        // Get single recommendation usecase returns 4 recommendation data
        getSingleRecommendationUseCase.givenGetSingleRecommendationReturnsThis(listOf(
                RecommendationItem(),
                RecommendationItem(),
                RecommendationItem(isWishlist = wishlistedInitialState),
                RecommendationItem()
        ))

        // User id
        every { userSessionInterface.userId } returns mockUserId
        // Wishlist get wishlist data
        wishlistViewmodel.getWishlistData()
        // Add wishlist usecase successfully add wishlist
        every { addWishListUseCase.createObservable(any(), mockUserId, any()) }
                .answers {
                    (thirdArg() as WishListActionListener).onSuccessAddWishlist(firstArg())
                }


        // View model set wishlist for selected recommendation product
        wishlistViewmodel.setRecommendationItemWishlist(-1, childPositionCandidate, wishlistedInitialState)
        wishlistViewmodel.setRecommendationItemWishlist(-1, childPositionCandidate - 1, !wishlistedInitialState)
        wishlistViewmodel.setEmptyWishlistRecommendationItemWishlist(childPositionCandidate, wishlistedInitialState)
        wishlistViewmodel.setEmptyWishlistRecommendationItemWishlist(0, wishlistedInitialState)
        wishlistViewmodel.setEmptyWishlistRecommendationItemWishlist(0, !wishlistedInitialState)

        // Expect that recommendation item wishlist status is updated to true on wishlist data
        val recommendationDataModel =
                wishlistViewmodel.wishlistLiveData.value!![childPositionCandidate] as RecommendationItemDataModel
        val recommendationDataModel2 =
                wishlistViewmodel.wishlistLiveData.value!![childPositionCandidate - 1] as RecommendationItemDataModel

        Assert.assertEquals(wishlistedInitialState, recommendationDataModel.recommendationItem.isWishlist)
//        Assert.assertEquals(!wishlistedInitialState, recommendationDataModel2.recommendationItem.isWishlist)

        // Expect that add wishlist action triggered
        val addWishlistAction = wishlistViewmodel.addWishlistRecommendationActionData
        Assert.assertEquals(true, addWishlistAction.value!!.peekContent().isSuccess)

        // Expect remove wishlist action event can only retrieved once
        val wishlistEventAddWishlistActionData = wishlistViewmodel.addWishlistRecommendationActionData
        wishlistEventAddWishlistActionData.value!!.getContentIfNotHandled()
        val eventAddWishlistActionDataSecond = wishlistEventAddWishlistActionData.value!!.getContentIfNotHandled()
        Assert.assertEquals(eventAddWishlistActionDataSecond, null)
    }

    @Test
    fun `Set wishlist failed with initial state false should not update product isWishlist in wishlist data`() {
        val mockUserId = "54321"

        //4 because empty wishlist data is structured:
        // - empty data model
        // - recom title data model
        // - recom 1
        // - recom 2 ------> selected
        // - recom 3
        // - recom 4
        val childPositionCandidate = 4
        val wishlistedInitialState = false
        val mockErrorMessage = "OH YA"

        // Wishlist viewmodel
        wishlistViewmodel = createWishlistViewModel(
                userSessionInterface = userSessionInterface,
                getWishlistDataUseCase = getWishlistDataUseCase,
                getSingleRecommendationUseCase = getSingleRecommendationUseCase,
                addWishListUseCase = addWishListUseCase)

        // Get wishlist usecase returns empty wishlist data
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf())
        // Get single recommendation usecase returns 4 recommendation data
        getSingleRecommendationUseCase.givenGetSingleRecommendationReturnsThis(listOf(
                RecommendationItem(),
                RecommendationItem(),
                RecommendationItem(isWishlist = wishlistedInitialState),
                RecommendationItem()
        ))

        // Live data is filled by data from getWishlist
        wishlistViewmodel.getWishlistData()
        // User id
        every { userSessionInterface.userId } returns mockUserId
        // Add wishlist usecase failed to add wishlist
        every { addWishListUseCase.createObservable(any(), mockUserId, any()) }
                .answers {
                    (thirdArg() as WishListActionListener).onErrorAddWishList(mockErrorMessage, firstArg())
                }

        // View model add a recommendation product
        wishlistViewmodel.setEmptyWishlistRecommendationItemWishlist(childPositionCandidate, wishlistedInitialState)

        // Expect that recommendation item is not updated in wishlist data
        val recommendationDataModel =
                wishlistViewmodel.wishlistLiveData.value!![childPositionCandidate] as RecommendationItemDataModel

        Assert.assertEquals(wishlistedInitialState, recommendationDataModel.recommendationItem.isWishlist)

        // Expect that remove wishlist action triggered
        Assert.assertEquals(false, wishlistViewmodel.addWishlistRecommendationActionData.value!!.peekContent().isSuccess)

        // Expect remove wishlist action event can only retrieved once
        val wishlistEventAddWishlistActionData = wishlistViewmodel.addWishlistRecommendationActionData
        wishlistEventAddWishlistActionData.value!!.getContentIfNotHandled()
        val eventAddWishlistActionDataSecond = wishlistEventAddWishlistActionData.value!!.getContentIfNotHandled()
        Assert.assertEquals(eventAddWishlistActionDataSecond, null)
    }

    @Test
    fun `Set wishlist success with initial state true should update product isWishlist to false in wishlist data`() {
        val mockUserId = "54321"

        //4 because empty wishlist data is structured:
        // - empty data model
        // - recom title data model
        // - recom 1
        // - recom 2 ------> selected
        // - recom 3
        // - recom 4
        val childPositionCandidate = 4
        val wishlistedInitialState = true

        // Wishlist viewmodel
        wishlistViewmodel = createWishlistViewModel(
                userSessionInterface = userSessionInterface,
                getWishlistDataUseCase = getWishlistDataUseCase,
                getSingleRecommendationUseCase = getSingleRecommendationUseCase,
                removeWishlistUseCase = removeWishlistUseCase)
        // Get wishlist usecase returns empty wishlist data
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf())
        // Get single recommendation usecase returns 4 recommendation data
        getSingleRecommendationUseCase.givenGetSingleRecommendationReturnsThis(listOf(
                RecommendationItem(),
                RecommendationItem(),
                RecommendationItem(isWishlist = wishlistedInitialState),
                RecommendationItem()
        ))

        // Live data is filled by data from getWishlist
        wishlistViewmodel.getWishlistData()
        // User id
        every { userSessionInterface.userId } returns mockUserId
        // Remove wishlist usecase successfully remove wishlist
        every { removeWishlistUseCase.createObservable(any(), mockUserId, any()) }
                .answers {
                    (thirdArg() as WishListActionListener).onSuccessRemoveWishlist(firstArg())
                }


        // View model set wishlist a recommendation product
        wishlistViewmodel.setEmptyWishlistRecommendationItemWishlist(childPositionCandidate, wishlistedInitialState)

        // Expect that recommendation item wishlist status is updated to false on wishlist data
        val recommendationDataModel =
                wishlistViewmodel.wishlistLiveData.value!![childPositionCandidate] as RecommendationItemDataModel

        Assert.assertEquals(!wishlistedInitialState, recommendationDataModel.recommendationItem.isWishlist)

        // Expect that remove wishlist action triggered
        val removeWishlistAction = wishlistViewmodel.removeWishlistRecommendationActionData
        Assert.assertEquals(true, removeWishlistAction.value!!.peekContent().isSuccess)

        // Expect remove wishlist action event can only retrieved once
        val wishlistEventRemoveWishlistActionData = wishlistViewmodel.removeWishlistRecommendationActionData
        wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
        val eventRemoveWishlistActionDataSecond = wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
        Assert.assertEquals(eventRemoveWishlistActionDataSecond, null)

    }

    @Test
    fun `Set wishlist failed with initial state true should not update product isWishlist in wishlist data`() {
        val mockUserId = "54321"

        //4 because empty wishlist data is structured:
        // - empty data model
        // - recom title data model
        // - recom 1
        // - recom 2 ------> selected
        // - recom 3
        // - recom 4
        val childPositionCandidate = 4
        val wishlistedInitialState = true
        val mockErrorMessage = "OH YA"

        // Wishlist viewmodel
        wishlistViewmodel = createWishlistViewModel(
                userSessionInterface = userSessionInterface,
                getWishlistDataUseCase = getWishlistDataUseCase,
                getSingleRecommendationUseCase = getSingleRecommendationUseCase,
                removeWishlistUseCase = removeWishlistUseCase)
        // Get wishlist usecase returns empty wishlist data
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf())
        // Get single recommendation usecase returns 4 recommendation data
        getSingleRecommendationUseCase.givenGetSingleRecommendationReturnsThis(listOf(
                RecommendationItem(),
                RecommendationItem(),
                RecommendationItem(isWishlist = wishlistedInitialState),
                RecommendationItem()
        ))

        // Live data is filled by data from getWishlist
        wishlistViewmodel.getWishlistData()
        // User id
        every { userSessionInterface.userId } returns mockUserId
        // Remove wishlist usecase successfully remove wishlist
        every { removeWishlistUseCase.createObservable(any(), mockUserId, any()) }
                .answers {
                    (thirdArg() as WishListActionListener).onErrorRemoveWishlist(mockErrorMessage, firstArg())
                }


        // View model add a recommendation product
        wishlistViewmodel.setEmptyWishlistRecommendationItemWishlist(childPositionCandidate, wishlistedInitialState)

        // Expect that recommendation item wishlist status is not updated in wishlist data
        val recommendationDataModel =
                wishlistViewmodel.wishlistLiveData.value!![childPositionCandidate] as RecommendationItemDataModel

        Assert.assertEquals(wishlistedInitialState, recommendationDataModel.recommendationItem.isWishlist)

        // Expect that remove wishlist action triggered
        val removeWishlistAction = wishlistViewmodel.removeWishlistRecommendationActionData
        Assert.assertEquals(false, removeWishlistAction.value!!.peekContent().isSuccess)

        // Expect remove wishlist action event can only retrieved once
        val wishlistEventRemoveWishlistActionData = wishlistViewmodel.removeWishlistRecommendationActionData
        wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
        val eventRemoveWishlistActionDataSecond = wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
        Assert.assertEquals(eventRemoveWishlistActionDataSecond, null)

    }
}