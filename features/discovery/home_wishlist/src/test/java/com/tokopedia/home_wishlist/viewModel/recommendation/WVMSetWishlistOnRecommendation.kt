package com.tokopedia.home_wishlist.viewModel.recommendation

import com.tokopedia.home_wishlist.InstantTaskExecutorRuleSpek
import com.tokopedia.home_wishlist.data.repository.WishlistRepository
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselDataModel
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.viewModel.createWishlistTestInstance
import com.tokopedia.home_wishlist.viewModel.createWishlistViewModel
import com.tokopedia.home_wishlist.viewModel.givenRepositoryGetRecommendationDataReturnsThis
import com.tokopedia.home_wishlist.viewModel.givenRepositoryGetWishlistDataReturnsThis
import com.tokopedia.home_wishlist.viewmodel.WishlistViewModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.every
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class WVMSetWishlistOnRecommendation : Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Set wishlist on recommendation") {
        createWishlistTestInstance()
        lateinit var wishlistViewmodel: WishlistViewModel
        val addWishListUseCase by memoized<AddWishListUseCase>()
        val removeWishlistUseCase by memoized<RemoveWishListUseCase>()
        val userSessionInterface by memoized<UserSessionInterface>()
        val wishlistRepository by memoized<WishlistRepository>()

        Scenario("Set wishlist success with initial state false should update product isWishlist to true in wishlist data") {
            val mockUserId = "54321"
            val parentPositionCandidate = 4
            val childPositionCandidate = 2
            val wishlistedInitialState = false

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Repository returns wishlist data above recommendation treshold (4)") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(listOf(
                        WishlistItem(id="1"),
                        WishlistItem(id="2"),
                        WishlistItem(id="3"),
                        WishlistItem(id="4"),
                        WishlistItem(id="5"),
                        WishlistItem(id="6"),
                        WishlistItem(id="7"),
                        WishlistItem(id="8"),
                        WishlistItem(id="9")
                ))
            }
            Given("Repository returns 1 recommendation data") {
                wishlistRepository.givenRepositoryGetRecommendationDataReturnsThis(
                        listOf(
                                RecommendationItem(productId = 11),
                                RecommendationItem(productId = 22),
                                RecommendationItem(productId = 33, isWishlist = wishlistedInitialState),
                                RecommendationItem(productId = 44),
                                RecommendationItem(productId = 55)
                        )
                )
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Live data is filled by data from getWishlist") {
                wishlistViewmodel.getWishlistData()
            }
            Given("Add wishlist usecase successfully add wishlist") {
                every { addWishListUseCase.createObservable(any(), mockUserId, any()) }
                        .answers {
                            (thirdArg() as WishListActionListener).onSuccessAddWishlist(firstArg())
                        }
            }

            When("View model add a recommendation product") {
                wishlistViewmodel.setRecommendationItemWishlist(parentPositionCandidate, childPositionCandidate, wishlistedInitialState)
            }

            Then("Expect that recommendation item wishlist status is updated to true on wishlist data") {
                val recommendationCarouselDataModel =
                        wishlistViewmodel.wishlistLiveData.value!![parentPositionCandidate] as RecommendationCarouselDataModel
                val recommendationCarouselItemDataModel =
                        recommendationCarouselDataModel.list[childPositionCandidate]

                Assert.assertEquals(!wishlistedInitialState, recommendationCarouselItemDataModel.recommendationItem.isWishlist)

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
            val parentPositionCandidate = 4
            val childPositionCandidate = 3
            val wishlistedInitialState = false
            val mockErrorMessage = "OH YA"
            val mockProductId = "33"

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Repository returns wishlist data above recommendation treshold (4)") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(listOf(
                        WishlistItem(id="1"),
                        WishlistItem(id="2"),
                        WishlistItem(id="3"),
                        WishlistItem(id="4"),
                        WishlistItem(id="5"),
                        WishlistItem(id="6"),
                        WishlistItem(id="7"),
                        WishlistItem(id="8"),
                        WishlistItem(id="9")
                ))
            }
            Given("Repository returns 1 recommendation data") {
                wishlistRepository.givenRepositoryGetRecommendationDataReturnsThis(
                        listOf(
                                RecommendationItem(productId = 11),
                                RecommendationItem(productId = 22),
                                RecommendationItem(productId = mockProductId.toInt(), isWishlist = wishlistedInitialState),
                                RecommendationItem(productId = 44),
                                RecommendationItem(productId = 55)
                        )
                )
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
                wishlistViewmodel.setRecommendationItemWishlist(parentPositionCandidate, childPositionCandidate, wishlistedInitialState)
            }

            Then("Expect that recommendation item is not updated in wishlist data") {
                val recommendationCarouselDataModel =
                        wishlistViewmodel.wishlistLiveData.value!![parentPositionCandidate] as RecommendationCarouselDataModel
                val recommendationCarouselItemDataModel =
                        recommendationCarouselDataModel.list[childPositionCandidate]

                Assert.assertEquals(wishlistedInitialState, recommendationCarouselItemDataModel.recommendationItem.isWishlist)

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
            val parentPositionCandidate = 4
            val childPositionCandidate = 2
            val wishlistedInitialState = true
            val mockProductId = "33"

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Repository returns wishlist data above recommendation treshold (4)") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(listOf(
                        WishlistItem(id="1"),
                        WishlistItem(id="2"),
                        WishlistItem(id="3"),
                        WishlistItem(id="4"),
                        WishlistItem(id="5"),
                        WishlistItem(id="6"),
                        WishlistItem(id="7"),
                        WishlistItem(id="8"),
                        WishlistItem(id="9")
                ))
            }
            Given("Repository returns 1 recommendation data") {
                wishlistRepository.givenRepositoryGetRecommendationDataReturnsThis(
                        listOf(
                                RecommendationItem(productId = 11),
                                RecommendationItem(productId = 22),
                                RecommendationItem(productId = mockProductId.toInt(), isWishlist = wishlistedInitialState),
                                RecommendationItem(productId = 44),
                                RecommendationItem(productId = 55)
                        )
                )
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

            When("View model add a recommendation product") {
                wishlistViewmodel.setRecommendationItemWishlist(parentPositionCandidate, childPositionCandidate, wishlistedInitialState)
            }

            Then("Expect that recommendation item wishlist status is updated to false on wishlist data") {
                val recommendationCarouselDataModel =
                        wishlistViewmodel.wishlistLiveData.value!![parentPositionCandidate] as RecommendationCarouselDataModel
                val recommendationCarouselItemDataModel =
                        recommendationCarouselDataModel.list[childPositionCandidate]

                Assert.assertEquals(!wishlistedInitialState, recommendationCarouselItemDataModel.recommendationItem.isWishlist)
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
            val parentPositionCandidate = 4
            val childPositionCandidate = 2
            val wishlistedInitialState = true
            val mockErrorMessage = "OH YA"
            val mockProductId = "33"

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Repository returns wishlist data above recommendation treshold (4)") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(listOf(
                        WishlistItem(id="1"),
                        WishlistItem(id="2"),
                        WishlistItem(id="3"),
                        WishlistItem(id="4"),
                        WishlistItem(id="5"),
                        WishlistItem(id="6"),
                        WishlistItem(id="7"),
                        WishlistItem(id="8"),
                        WishlistItem(id="9")
                ))
            }
            Given("Repository returns 1 recommendation data") {
                wishlistRepository.givenRepositoryGetRecommendationDataReturnsThis(
                        listOf(
                                RecommendationItem(productId = 11),
                                RecommendationItem(productId = 22),
                                RecommendationItem(productId = mockProductId.toInt(), isWishlist = wishlistedInitialState),
                                RecommendationItem(productId = 44),
                                RecommendationItem(productId = 55)
                        )
                )
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
                wishlistViewmodel.setRecommendationItemWishlist(parentPositionCandidate, childPositionCandidate, wishlistedInitialState)
            }

            Then("Expect that recommendation item wishlist status is not updated in wishlist data") {
                val recommendationCarouselDataModel =
                        wishlistViewmodel.wishlistLiveData.value!![parentPositionCandidate] as RecommendationCarouselDataModel
                val recommendationCarouselItemDataModel =
                        recommendationCarouselDataModel.list[childPositionCandidate]

                Assert.assertEquals(wishlistedInitialState, recommendationCarouselItemDataModel.recommendationItem.isWishlist)
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