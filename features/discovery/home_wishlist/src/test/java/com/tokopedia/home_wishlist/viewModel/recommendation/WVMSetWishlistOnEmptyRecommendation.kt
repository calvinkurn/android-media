package com.tokopedia.home_wishlist.viewModel.recommendation

import com.tokopedia.home_wishlist.InstantTaskExecutorRuleSpek
import com.tokopedia.home_wishlist.data.repository.WishlistRepository
import com.tokopedia.home_wishlist.domain.GetWishlistDataUseCase
import com.tokopedia.home_wishlist.model.datamodel.RecommendationItemDataModel
import com.tokopedia.home_wishlist.viewModel.createWishlistTestInstance
import com.tokopedia.home_wishlist.viewModel.createWishlistViewModel
import com.tokopedia.home_wishlist.viewModel.givenGetSingleRecommendationReturnsThis
import com.tokopedia.home_wishlist.viewModel.givenGetWishlistDataReturnsThis
import com.tokopedia.home_wishlist.viewmodel.WishlistViewModel
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.every
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class WVMSetWishlistOnEmptyRecommendation : Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Set wishlist on empty recommendation") {
        createWishlistTestInstance()
        lateinit var wishlistViewmodel: WishlistViewModel
        val addWishListUseCase by memoized<AddWishListUseCase>()
        val removeWishlistUseCase by memoized<RemoveWishListUseCase>()
        val userSessionInterface by memoized<UserSessionInterface>()
        val getWishlistDataUseCase by memoized<GetWishlistDataUseCase>()
        val getSingleRecommendationUseCase by memoized<GetSingleRecommendationUseCase>()

        Scenario("Set wishlist success with initial state false should update product isWishlist to true in wishlist data") {
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

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Get wishlist usecase returns empty wishlist data") {
                getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf())
            }
            Given("Get single recommendation usecase returns 4 recommendation data") {
                getSingleRecommendationUseCase.givenGetSingleRecommendationReturnsThis(listOf(
                        RecommendationItem(),
                        RecommendationItem(),
                        RecommendationItem(isWishlist = wishlistedInitialState),
                        RecommendationItem()
                ))
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist get wishlist data") {
                wishlistViewmodel.getWishlistData()
            }
            Given("Add wishlist usecase successfully add wishlist") {
                every { addWishListUseCase.createObservable(any(), mockUserId, any()) }
                        .answers {
                            (thirdArg() as WishListActionListener).onSuccessAddWishlist(firstArg())
                        }
            }

            When("View model set wishlist for selected recommendation product") {
                wishlistViewmodel.setEmptyWishlistRecommendationItemWishlist(childPositionCandidate, wishlistedInitialState)
            }

            Then("Expect that recommendation item wishlist status is updated to true on wishlist data") {
                val recommendationDataModel =
                        wishlistViewmodel.wishlistLiveData.value!![childPositionCandidate] as RecommendationItemDataModel

                Assert.assertEquals(!wishlistedInitialState, recommendationDataModel.recommendationItem.isWishlist)
            }
            Then("Expect that add wishlist action triggered") {
                val addWishlistAction = wishlistViewmodel.addWishlistRecommendationActionData
                Assert.assertEquals(true, addWishlistAction.value!!.peekContent().isSuccess)
            }
            Then("Expect remove wishlist action event can only retrieved once") {
                val wishlistEventAddWishlistActionData = wishlistViewmodel.addWishlistRecommendationActionData
                val eventAddWishlistActionData = wishlistEventAddWishlistActionData.value!!.getContentIfNotHandled()
                val eventAddWishlistActionDataSecond = wishlistEventAddWishlistActionData.value!!.getContentIfNotHandled()
                Assert.assertEquals(eventAddWishlistActionDataSecond, null)
            }
        }

        Scenario("Set wishlist failed with initial state false should not update product isWishlist in wishlist data") {
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
            val mockProductId = "33"

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Get wishlist usecase returns empty wishlist data") {
                getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf())
            }
            Given("Get single recommendation usecase returns 4 recommendation data") {
                getSingleRecommendationUseCase.givenGetSingleRecommendationReturnsThis(listOf(
                        RecommendationItem(),
                        RecommendationItem(),
                        RecommendationItem(isWishlist = wishlistedInitialState),
                        RecommendationItem()
                ))
            }
            Given("Live data is filled by data from getWishlist") {
                wishlistViewmodel.getWishlistData()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Add wishlist usecase failed to add wishlist") {
                every { addWishListUseCase.createObservable(any(), mockUserId, any()) }
                        .answers {
                            (thirdArg() as WishListActionListener).onErrorAddWishList(mockErrorMessage, firstArg())
                        }
            }

            When("View model add a recommendation product") {
                wishlistViewmodel.setEmptyWishlistRecommendationItemWishlist(childPositionCandidate, wishlistedInitialState)
            }

            Then("Expect that recommendation item is not updated in wishlist data") {
                val recommendationDataModel =
                        wishlistViewmodel.wishlistLiveData.value!![childPositionCandidate] as RecommendationItemDataModel

                Assert.assertEquals(wishlistedInitialState, recommendationDataModel.recommendationItem.isWishlist)
            }
            Then("Expect that remove wishlist action triggered") {
                val wishlistEventAddWishlistActionData = wishlistViewmodel.addWishlistRecommendationActionData
                Assert.assertEquals(false, wishlistEventAddWishlistActionData.value!!.peekContent().isSuccess)
            }
            Then("Expect remove wishlist action event can only retrieved once") {
                val wishlistEventAddWishlistActionData = wishlistViewmodel.addWishlistRecommendationActionData
                val eventAddWishlistActionData = wishlistEventAddWishlistActionData.value!!.getContentIfNotHandled()
                val eventAddWishlistActionDataSecond = wishlistEventAddWishlistActionData.value!!.getContentIfNotHandled()
                Assert.assertEquals(eventAddWishlistActionDataSecond, null)
            }
        }

        Scenario("Set wishlist success with initial state true should update product isWishlist to false in wishlist data") {
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
            val mockProductId = "33"

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Get wishlist usecase returns empty wishlist data") {
                getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf())
            }
            Given("Get single recommendation usecase returns 4 recommendation data") {
                getSingleRecommendationUseCase.givenGetSingleRecommendationReturnsThis(listOf(
                        RecommendationItem(),
                        RecommendationItem(),
                        RecommendationItem(isWishlist = wishlistedInitialState),
                        RecommendationItem()
                ))
            }
            Given("Live data is filled by data from getWishlist") {
                wishlistViewmodel.getWishlistData()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Remove wishlist usecase successfully remove wishlist") {
                every { removeWishlistUseCase.createObservable(any(), mockUserId, any()) }
                        .answers {
                            (thirdArg() as WishListActionListener).onSuccessRemoveWishlist(firstArg())
                        }
            }

            When("View model set wishlist a recommendation product") {
                wishlistViewmodel.setEmptyWishlistRecommendationItemWishlist(childPositionCandidate, wishlistedInitialState)
            }

            Then("Expect that recommendation item wishlist status is updated to false on wishlist data") {
                val recommendationDataModel =
                        wishlistViewmodel.wishlistLiveData.value!![childPositionCandidate] as RecommendationItemDataModel

                Assert.assertEquals(!wishlistedInitialState, recommendationDataModel.recommendationItem.isWishlist)
            }
            Then("Expect that remove wishlist action triggered") {
                val removeWishlistAction = wishlistViewmodel.removeWishlistRecommendationActionData
                Assert.assertEquals(true, removeWishlistAction.value!!.peekContent().isSuccess)
            }
            Then("Expect remove wishlist action event can only retrieved once") {
                val wishlistEventRemoveWishlistActionData = wishlistViewmodel.removeWishlistRecommendationActionData
                val eventRemoveWishlistActionData = wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
                val eventRemoveWishlistActionDataSecond = wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
                Assert.assertEquals(eventRemoveWishlistActionDataSecond, null)
            }
        }

        Scenario("Set wishlist failed with initial state true should not update product isWishlist in wishlist data") {
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
            val mockProductId = "33"

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Get wishlist usecase returns empty wishlist data") {
                getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf())
            }
            Given("Get single recommendation usecase returns 4 recommendation data") {
                getSingleRecommendationUseCase.givenGetSingleRecommendationReturnsThis(listOf(
                        RecommendationItem(),
                        RecommendationItem(),
                        RecommendationItem(isWishlist = wishlistedInitialState),
                        RecommendationItem()
                ))
            }
            Given("Live data is filled by data from getWishlist") {
                wishlistViewmodel.getWishlistData()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Remove wishlist usecase successfully remove wishlist") {
                every { removeWishlistUseCase.createObservable(any(), mockUserId, any()) }
                        .answers {
                            (thirdArg() as WishListActionListener).onErrorRemoveWishlist(mockErrorMessage, firstArg())
                        }
            }

            When("View model add a recommendation product") {
                wishlistViewmodel.setEmptyWishlistRecommendationItemWishlist(childPositionCandidate, wishlistedInitialState)
            }

            Then("Expect that recommendation item wishlist status is not updated in wishlist data") {
                val recommendationDataModel =
                        wishlistViewmodel.wishlistLiveData.value!![childPositionCandidate] as RecommendationItemDataModel

                Assert.assertEquals(wishlistedInitialState, recommendationDataModel.recommendationItem.isWishlist)
            }
            Then("Expect that remove wishlist action triggered") {
                val removeWishlistAction = wishlistViewmodel.removeWishlistRecommendationActionData
                Assert.assertEquals(false, removeWishlistAction.value!!.peekContent().isSuccess)
            }
            Then("Expect remove wishlist action event can only retrieved once") {
                val wishlistEventRemoveWishlistActionData = wishlistViewmodel.removeWishlistRecommendationActionData
                val eventRemoveWishlistActionData = wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
                val eventRemoveWishlistActionDataSecond = wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
                Assert.assertEquals(eventRemoveWishlistActionDataSecond, null)
            }
        }
    }
})