package com.tokopedia.home_wishlist.viewModel

import androidx.lifecycle.LiveData
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.home_wishlist.data.repository.WishlistRepository
import com.tokopedia.home_wishlist.model.datamodel.WishlistItemDataModel
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.util.WishlistAction
import com.tokopedia.home_wishlist.util.Event
import com.tokopedia.home_wishlist.viewmodel.WishlistViewModel
import com.tokopedia.home_wishlist.InstantTaskExecutorRuleSpek
import com.tokopedia.home_wishlist.TestDispatcherProvider
import com.tokopedia.home_wishlist.model.entity.Shop
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.data.datamodel.WishlistData
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.BulkRemoveWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.style.gherkin.Feature
import org.spekframework.spek2.style.gherkin.FeatureBody
import rx.Subscriber

class WishlistViewModelTestSpek : Spek({
    InstantTaskExecutorRuleSpek(this)

    fun TestBody.createWishlistViewModel(): WishlistViewModel {
        val userSessionInterface by memoized<UserSessionInterface>()
        val wishlistRepository by memoized<WishlistRepository>()
        val removeWishlistUseCase by memoized<RemoveWishListUseCase>()
        val addToCartUseCase by memoized<AddToCartUseCase>()
        val bulkRemoveWishlistUseCase by memoized<BulkRemoveWishlistUseCase>()

        return WishlistViewModel(
                userSessionInterface = userSessionInterface,
                wishlistRepository = wishlistRepository,
                wishlistCoroutineDispatcherProvider = TestDispatcherProvider(),
                removeWishListUseCase = removeWishlistUseCase,
                addToCartUseCase = addToCartUseCase,
                bulkRemoveWishlistUseCase = bulkRemoveWishlistUseCase
        )
    }

    @Suppress("UNUSED_VARIABLE")
    fun FeatureBody.createWishlistTestInstance() {
        val userSessionInterface by memoized {
            mockk<UserSessionInterface>(relaxed = true)
        }

        val wishlistRepository by memoized {
            mockk<WishlistRepository>(relaxed = true)
        }

        val removeWishlistUseCase by memoized {
            mockk<RemoveWishListUseCase>(relaxed = true)
        }

        val addToCartUseCase by memoized {
            mockk<AddToCartUseCase>(relaxed = true)
        }

        val bulkRemoveWishlistUseCase by memoized {
            mockk<BulkRemoveWishlistUseCase>(relaxed = true)
        }
    }

    Feature("Add to cart in wishlist") {
        createWishlistTestInstance()
        lateinit var wishlistViewmodel: WishlistViewModel
        val addToCartUseCase by memoized<AddToCartUseCase>()

        Scenario("Successfully add to cart product") {
            var mockProductCardPositionCandidate = 0
            val mockId1 = "11"
            val mockId2 = "22"

            val shopId1 = "99"
            val shopId2 = "88"
            val minimumOrder = 1

            Given("Create wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Wishlist data") {
                wishlistViewmodel.wishlistData.value = listOf(
                        WishlistItemDataModel(WishlistItem(id=mockId1, shop = Shop(id = shopId1), minimumOrder = minimumOrder)),
                        WishlistItemDataModel(WishlistItem(id=mockId2, shop = Shop(id = shopId2), minimumOrder = minimumOrder))
                )
            }
            Given("Product candidate for add to cart position") {
                mockProductCardPositionCandidate = 1
            }
            Given("Add to cart success and return AddToCartDataModel") {
                every { addToCartUseCase.execute(any(), any()) }.answers {
                    (secondArg() as Subscriber<AddToCartDataModel>).onNext(
                            AddToCartDataModel(
                                    status = AddToCartDataModel.STATUS_OK,
                                    data = DataModel(success = 1, productId = mockId2.toInt(), message = arrayListOf(""))
                            )
                    )
                }
            }

            When("Add to cart from wishlist") {
                wishlistViewmodel.addToCartProduct(mockProductCardPositionCandidate)
            }

            Then("Add to cart event is triggered with selected product Id") {
                val wishlistEventData: LiveData<Event<WishlistAction>> = wishlistViewmodel.action
                assertEquals(mockProductCardPositionCandidate, wishlistEventData.value!!.peekContent()!!.position)
                assertEquals(mockId2, wishlistEventData.value!!.peekContent()!!.productId.toString())
            }

            Then("Add to cart event can only retrieved once") {
                val wishlistEventData: LiveData<Event<WishlistAction>> = wishlistViewmodel.action
                val eventItem = wishlistEventData.value!!.getContentIfNotHandled()
                val eventItemSecond = wishlistEventData.value!!.getContentIfNotHandled()
                assertEquals( null, eventItemSecond)
            }
        }

        Scenario("Failed add to cart product") {
            var mockProductCardPositionCandidate = 0
            val mockId1 = "11"
            val mockId2 = "22"

            val shopId1 = "99"
            val shopId2 = "88"
            val minimumOrder = 1

            Given("Create wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Wishlist data") {
                wishlistViewmodel.wishlistData.value = listOf(
                        WishlistItemDataModel(WishlistItem(id=mockId1, shop = Shop(id = shopId1), minimumOrder = minimumOrder)),
                        WishlistItemDataModel(WishlistItem(id=mockId2, shop = Shop(id = shopId2), minimumOrder = minimumOrder))
                )
            }
            Given("Product candidate for add to cart position") {
                mockProductCardPositionCandidate = 1
            }
            Given("Add to cart failed and return AddToCartDataModel with error") {
                every { addToCartUseCase.execute(any(), any()) }.answers {
                    (secondArg() as Subscriber<AddToCartDataModel>).onNext(
                            AddToCartDataModel(
                                    status = "NOT OK",
                                    data = DataModel(success = 0, productId = mockId2.toInt(), message = arrayListOf("")),
                                    errorMessage = arrayListOf("")
                            )
                    )
                }
            }

            When("Add to cart from wishlist") {
                wishlistViewmodel.addToCartProduct(mockProductCardPositionCandidate)
            }

            Then("Add to cart event is triggered with selected product Id and error message") {
                val wishlistEventData: LiveData<Event<WishlistAction>> = wishlistViewmodel.action
                Assert.assertEquals(mockProductCardPositionCandidate, wishlistEventData.value!!.peekContent()!!.position)
                Assert.assertEquals(mockId2, wishlistEventData.value!!.peekContent()!!.productId.toString())
            }

            Then("Add to cart event can only retrieved once") {
                val wishlistEventData: LiveData<Event<WishlistAction>> = wishlistViewmodel.action
                val eventItem = wishlistEventData.value!!.getContentIfNotHandled()
                val eventItemSecond = wishlistEventData.value!!.getContentIfNotHandled()
                assertEquals(eventItemSecond, null)
            }
        }
    }

    Feature("Remove wishlist") {
        createWishlistTestInstance()
        lateinit var wishlistViewmodel: WishlistViewModel
        val removeWishlistUseCase by memoized<RemoveWishListUseCase>()
        val userSessionInterface by memoized<UserSessionInterface>()
        Scenario("Remove wishlist success") {
            val mockProductId = "3"
            val mockUserId = "54321"

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("List of wishlist data") {
                wishlistViewmodel.wishlistData.value = listOf(
                        WishlistItemDataModel(WishlistItem(id="1")),
                        WishlistItemDataModel(WishlistItem(id="2")),
                        WishlistItemDataModel(WishlistItem(id="3")),
                        WishlistItemDataModel(WishlistItem(id="4")))
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Remove wishlist usecase successfully remove wishlist") {
                every { removeWishlistUseCase.createObservable(any(), mockUserId, any()) }
                        .answers {
                            (thirdArg() as WishListActionListener).onSuccessRemoveWishlist(mockProductId)
                        }
            }

            When("View model remove a wishlist product") {
                wishlistViewmodel.removeWishlistedProduct(mockSelectedPosition)
            }

            Then("Expect that unwishlisted product is removed from wishlistData") {
                assertEquals(3, wishlistViewmodel.wishlistData.value!!.size)
                wishlistViewmodel.wishlistData.value!!.forEach {
                    if (it is WishlistItemDataModel) {
                        if (it.productItem.id == mockProductId) {
                            assertFalse("Unwishlisted product still exist!" ,true)
                        }
                    }
                }
            }
        }

            When("View model remove a wishlist product") {
                wishlistViewmodel.removeWishlistedProduct(mockSelectedPosition)
            }

            Then("Expect that unwishlisted product is removed from wishlistData") {
                assertEquals(4, wishlistViewmodel.wishlistData.value!!.size)
                wishlistViewmodel.wishlistData.value!!.forEach {
                    if (it is WishlistItemDataModel) {
                        if (it.productItem.id == mockProductId) {
                            assertFalse("Unwishlisted product still in exist" , false)
                        }
                    }
                }
            }
            Then("Expect that remove wishlist action triggered") {
                val removeWishlistActionData = wishlistViewmodel.removeWishlistActionData
                assertEquals(false, removeWishlistActionData.value!!.peekContent().isSuccess)
                assertEquals(mockErrorMessage, removeWishlistActionData.value!!.peekContent().message)
            }
            Then("Expect remove wishlist action event can only retrieved once") {
                val wishlistEventRemoveWishlistActionData = wishlistViewmodel.removeWishlistActionData
                val eventRemoveWishlistActionData = wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
                val eventRemoveWishlistActionDataSecond = wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
                assertEquals(eventRemoveWishlistActionDataSecond, null)
            }
        }
    }

    Feature("Set wishlist on recommendation") {
        createWishlistTestInstance()
        lateinit var wishlistViewmodel: WishlistViewModel
        val addWishListUseCase by memoized<AddWishListUseCase>()
        val removeWishlistUseCase by memoized<RemoveWishListUseCase>()
        val userSessionInterface by memoized<UserSessionInterface>()

        Scenario("Add wishlist success with initial state false should update product isWishlist to true in wishlist data") {
            val mockUserId = "54321"
            val parentPositionCandidate = 4
            val childPositionCandidate = 2
            val wishlistedInitialState = false

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("List of wishlist data") {
                wishlistViewmodel.wishlistData.value = listOf(
                        WishlistItemDataModel(WishlistItem(id="1")),
                        WishlistItemDataModel(WishlistItem(id="2")),
                        WishlistItemDataModel(WishlistItem(id="3")),
                        WishlistItemDataModel(WishlistItem(id="4")),
                        RecommendationCarouselDataModel(
                                list = listOf(
                                        RecommendationCarouselItemDataModel(RecommendationItem(productId = 11)),
                                        RecommendationCarouselItemDataModel(RecommendationItem(productId = 22)),
                                        RecommendationCarouselItemDataModel(RecommendationItem(productId = 33, isWishlist = wishlistedInitialState)),
                                        RecommendationCarouselItemDataModel(RecommendationItem(productId = 44)),
                                        RecommendationCarouselItemDataModel(RecommendationItem(productId = 55))
                                )
                        ),
                        WishlistItemDataModel(WishlistItem(id="5")),
                        WishlistItemDataModel(WishlistItem(id="6")),
                        WishlistItemDataModel(WishlistItem(id="7")),
                        WishlistItemDataModel(WishlistItem(id="8"))
                )
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
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
                        wishlistViewmodel.wishlistData.value!![parentPositionCandidate] as RecommendationCarouselDataModel
                val recommendationCarouselItemDataModel =
                        recommendationCarouselDataModel.list[childPositionCandidate]

                assertEquals(!wishlistedInitialState, recommendationCarouselItemDataModel.recommendationItem.isWishlist)

            }
            Then("Expect that add wishlist action triggered") {
                val addWishlistAction = wishlistViewmodel.addWishlistRecommendationActionData
                assertEquals(true, addWishlistAction.value!!.peekContent().isSuccess)
            }
            Then("Expect remove wishlist action event can only retrieved once") {
                val wishlistEventAddWishlistActionData = wishlistViewmodel.addWishlistRecommendationActionData
                val eventAddWishlistActionData = wishlistEventAddWishlistActionData.value!!.getContentIfNotHandled()
                val eventAddWishlistActionDataSecond = wishlistEventAddWishlistActionData.value!!.getContentIfNotHandled()
                assertEquals(eventAddWishlistActionDataSecond, null)
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
            Given("List of wishlist data") {
                wishlistViewmodel.wishlistData.value = listOf(
                        WishlistItemDataModel(WishlistItem(id="1")),
                        WishlistItemDataModel(WishlistItem(id="2")),
                        WishlistItemDataModel(WishlistItem(id="3")),
                        WishlistItemDataModel(WishlistItem(id="4")),
                        RecommendationCarouselDataModel(
                                list = listOf(
                                        RecommendationCarouselItemDataModel(RecommendationItem(productId = 11)),
                                        RecommendationCarouselItemDataModel(RecommendationItem(productId = 22)),
                                        RecommendationCarouselItemDataModel(RecommendationItem(productId = mockProductId.toInt(), isWishlist = wishlistedInitialState)),
                                        RecommendationCarouselItemDataModel(RecommendationItem(productId = 44)),
                                        RecommendationCarouselItemDataModel(RecommendationItem(productId = 55))
                                )
                        ),
                        WishlistItemDataModel(WishlistItem(id="5")),
                        WishlistItemDataModel(WishlistItem(id="6")),
                        WishlistItemDataModel(WishlistItem(id="7")),
                        WishlistItemDataModel(WishlistItem(id="8"))
                )
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
                        wishlistViewmodel.wishlistData.value!![parentPositionCandidate] as RecommendationCarouselDataModel
                val recommendationCarouselItemDataModel =
                        recommendationCarouselDataModel.list[childPositionCandidate]

                assertEquals(wishlistedInitialState, recommendationCarouselItemDataModel.recommendationItem.isWishlist)

            }
            Then("Expect that remove wishlist action triggered") {
                val wishlistEventAddWishlistActionData = wishlistViewmodel.addWishlistRecommendationActionData
                assertEquals(false, wishlistEventAddWishlistActionData.value!!.peekContent().isSuccess)
            }
            Then("Expect remove wishlist action event can only retrieved once") {
                val wishlistEventAddWishlistActionData = wishlistViewmodel.addWishlistRecommendationActionData
                val eventAddWishlistActionData = wishlistEventAddWishlistActionData.value!!.getContentIfNotHandled()
                val eventAddWishlistActionDataSecond = wishlistEventAddWishlistActionData.value!!.getContentIfNotHandled()
                assertEquals(eventAddWishlistActionDataSecond, null)
            }
        }

        Scenario("Set wishlist success with initial state true should update product isWishlist to false in wishlist data") {
            val mockUserId = "54321"
            val parentPositionCandidate = 4
            val childPositionCandidate = 2
            val wishlistedInitialState = true

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("List of wishlist data") {
                wishlistViewmodel.wishlistData.value = listOf(
                        WishlistItemDataModel(WishlistItem(id="1")),
                        WishlistItemDataModel(WishlistItem(id="2")),
                        WishlistItemDataModel(WishlistItem(id="3")),
                        WishlistItemDataModel(WishlistItem(id="4")),
                        RecommendationCarouselDataModel(
                                list = listOf(
                                        RecommendationCarouselItemDataModel(RecommendationItem(productId = 11)),
                                        RecommendationCarouselItemDataModel(RecommendationItem(productId = 22)),
                                        RecommendationCarouselItemDataModel(RecommendationItem(productId = 33, isWishlist = wishlistedInitialState)),
                                        RecommendationCarouselItemDataModel(RecommendationItem(productId = 44)),
                                        RecommendationCarouselItemDataModel(RecommendationItem(productId = 55))
                                )
                        ),
                        WishlistItemDataModel(WishlistItem(id="5")),
                        WishlistItemDataModel(WishlistItem(id="6")),
                        WishlistItemDataModel(WishlistItem(id="7")),
                        WishlistItemDataModel(WishlistItem(id="8"))
                )
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
                        wishlistViewmodel.wishlistData.value!![parentPositionCandidate] as RecommendationCarouselDataModel
                val recommendationCarouselItemDataModel =
                        recommendationCarouselDataModel.list[childPositionCandidate]

                assertEquals(!wishlistedInitialState, recommendationCarouselItemDataModel.recommendationItem.isWishlist)
            }
            Then("Expect that remove wishlist action triggered") {
                val removeWishlistAction = wishlistViewmodel.removeWishlistRecommendationActionData
                assertEquals(true, removeWishlistAction.value!!.peekContent().isSuccess)
            }
            Then("Expect remove wishlist action event can only retrieved once") {
                val wishlistEventRemoveWishlistActionData = wishlistViewmodel.removeWishlistRecommendationActionData
                val eventRemoveWishlistActionData = wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
                val eventRemoveWishlistActionDataSecond = wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
                assertEquals(eventRemoveWishlistActionDataSecond, null)
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
            Given("List of wishlist data") {
                wishlistViewmodel.wishlistData.value = listOf(
                        WishlistItemDataModel(WishlistItem(id="1")),
                        WishlistItemDataModel(WishlistItem(id="2")),
                        WishlistItemDataModel(WishlistItem(id="3")),
                        WishlistItemDataModel(WishlistItem(id="4")),
                        RecommendationCarouselDataModel(
                                list = listOf(
                                        RecommendationCarouselItemDataModel(RecommendationItem(productId = 11)),
                                        RecommendationCarouselItemDataModel(RecommendationItem(productId = 22)),
                                        RecommendationCarouselItemDataModel(RecommendationItem(productId = mockProductId.toInt(), isWishlist = wishlistedInitialState)),
                                        RecommendationCarouselItemDataModel(RecommendationItem(productId = 44)),
                                        RecommendationCarouselItemDataModel(RecommendationItem(productId = 55))
                                )
                        ),
                        WishlistItemDataModel(WishlistItem(id="5")),
                        WishlistItemDataModel(WishlistItem(id="6")),
                        WishlistItemDataModel(WishlistItem(id="7")),
                        WishlistItemDataModel(WishlistItem(id="8"))
                )
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
                        wishlistViewmodel.wishlistData.value!![parentPositionCandidate] as RecommendationCarouselDataModel
                val recommendationCarouselItemDataModel =
                        recommendationCarouselDataModel.list[childPositionCandidate]

                assertEquals(wishlistedInitialState, recommendationCarouselItemDataModel.recommendationItem.isWishlist)
            }
            Then("Expect that remove wishlist action triggered") {
                val removeWishlistAction = wishlistViewmodel.removeWishlistRecommendationActionData
                assertEquals(false, removeWishlistAction.value!!.peekContent().isSuccess)
            }
            Then("Expect remove wishlist action event can only retrieved once") {
                val wishlistEventRemoveWishlistActionData = wishlistViewmodel.removeWishlistRecommendationActionData
                val eventRemoveWishlistActionData = wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
                val eventRemoveWishlistActionDataSecond = wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
                assertEquals(eventRemoveWishlistActionDataSecond, null)
            }
        }
    }

    Feature("Update bulk progress state") {
        createWishlistTestInstance()
        lateinit var wishlistViewmodel: WishlistViewModel

        Scenario("Update bulk mode to true will update all wishlist data state into bulk mode") {
            Given("Create wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Wishlist data") {
                wishlistViewmodel.wishlistData.value = listOf(
                        WishlistItemDataModel(WishlistItem(id="1")),
                        WishlistItemDataModel(WishlistItem(id="2")),
                        WishlistItemDataModel(WishlistItem(id="3")),
                        WishlistItemDataModel(WishlistItem(id="4")),
                        RecommendationCarouselDataModel())
            }

            When("Update bulk mode") {
                wishlistViewmodel.updateBulkMode(true)
            }

            Then("Expect all visitable is set to bulk mode") {
                wishlistViewmodel.wishlistData.value!!.forEach {
                    if (it is RecommendationCarouselDataModel && !it.isOnBulkRemoveProgress) {
                        assertFalse("Item recommendation carousel not in bulk mode", true)
                    }
                    if (it is WishlistItemDataModel && !it.isOnBulkRemoveProgress) {
                        assertFalse("Item wishlist not in bulk mode", true)
                    }
                }
            }
        }

        Scenario("Update bulk mode to false will update all wishlist data bulk mode state to false") {
            Given("Create wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Wishlist data") {
                wishlistViewmodel.wishlistData.value = listOf(
                        WishlistItemDataModel(WishlistItem(id="1"), isOnBulkRemoveProgress = true),
                        WishlistItemDataModel(WishlistItem(id="2"), isOnBulkRemoveProgress = true),
                        WishlistItemDataModel(WishlistItem(id="3"), isOnBulkRemoveProgress = true),
                        WishlistItemDataModel(WishlistItem(id="4"), isOnBulkRemoveProgress = true),
                        RecommendationCarouselDataModel(isOnBulkRemoveProgress = true))
            }

            When("Update bulk mode") {
                wishlistViewmodel.updateBulkMode(false)
            }

            Then("Expect all visitable is set to bulk mode") {
                wishlistViewmodel.wishlistData.value!!.forEach {
                    if (it is RecommendationCarouselDataModel && it.isOnBulkRemoveProgress) {
                        assertFalse("Item recommendation carousel not in bulk mode", true)
                    }
                    if (it is WishlistItemDataModel && it.isOnBulkRemoveProgress) {
                        assertFalse("Item wishlist not in bulk mode", true)
                    }
                }
            }
        }
    }

    Feature("Mark item for bulk remove") {
        createWishlistTestInstance()
        lateinit var wishlistViewmodel: WishlistViewModel
        val markPosition = 2

        Scenario("Mark item true will update its onChecked status in wishlist data") {
            Given("Create wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Wishlist data") {
                wishlistViewmodel.wishlistData.value = listOf(
                        WishlistItemDataModel(WishlistItem(id="1")),
                        WishlistItemDataModel(WishlistItem(id="2")),
                        WishlistItemDataModel(WishlistItem(id="3")),
                        WishlistItemDataModel(WishlistItem(id="4")),
                        RecommendationCarouselDataModel())
            }

            When("Mark true product in position 2") {
                wishlistViewmodel.setWishlistOnMarkDelete(markPosition, true)
            }

            Then("Expect product in position 2 is checked = true in wishlist data") {
                val actualProductStatus =
                        (wishlistViewmodel.wishlistData.value!![markPosition] as WishlistItemDataModel).isOnChecked
                assertEquals(true, actualProductStatus)
            }
        }

        Scenario("Mark item false will update its onChecked status in wishlist data") {
            Given("Create wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Wishlist data") {
                wishlistViewmodel.wishlistData.value = listOf(
                        WishlistItemDataModel(WishlistItem(id="1")),
                        WishlistItemDataModel(WishlistItem(id="2")),
                        WishlistItemDataModel(WishlistItem(id="3")),
                        WishlistItemDataModel(WishlistItem(id="4")),
                        RecommendationCarouselDataModel())
            }

            When("Mark false product in position 2") {
                wishlistViewmodel.setWishlistOnMarkDelete(markPosition, false)
            }

            Then("Expect product in position 2 is checked = false in wishlist data") {
                val actualProductStatus =
                        (wishlistViewmodel.wishlistData.value!![markPosition] as WishlistItemDataModel).isOnChecked
                assertEquals(false, actualProductStatus)
            }
        }
    }

    Feature("Update wishlist in existing wishlist data") {
        createWishlistTestInstance()
        lateinit var wishlistViewmodel: WishlistViewModel
        val parentPosition1 = 4
        val childPosition1 = 2

        val parentPosition2 = 4
        val childPosition2 = 4

        Scenario("Update wishlist will change its wishlist data in wishlistData") {
            val wishlistCurrentStateFor33 = true
            val wishlistCurrentStateFor55 = false

            Given("Create wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Wishlist data") {
                wishlistViewmodel.wishlistData.value = listOf(
                        WishlistItemDataModel(WishlistItem(id="1")),
                        WishlistItemDataModel(WishlistItem(id="2")),
                        WishlistItemDataModel(WishlistItem(id="3")),
                        WishlistItemDataModel(WishlistItem(id="4")),
                        RecommendationCarouselDataModel(
                                list = listOf(
                                        RecommendationCarouselItemDataModel(RecommendationItem(productId = 11)),
                                        RecommendationCarouselItemDataModel(RecommendationItem(productId = 22)),
                                        RecommendationCarouselItemDataModel(RecommendationItem(productId = 33, isWishlist = false)),
                                        RecommendationCarouselItemDataModel(RecommendationItem(productId = 44)),
                                        RecommendationCarouselItemDataModel(RecommendationItem(productId = 55, isWishlist = true))
                                )
                        ),
                        WishlistItemDataModel(WishlistItem(id="5")),
                        WishlistItemDataModel(WishlistItem(id="6")),
                        WishlistItemDataModel(WishlistItem(id="7")),
                        WishlistItemDataModel(WishlistItem(id="8"))
                )
            }

            When("Update wishlist data in selected parent and child") {
                wishlistViewmodel.updateRecommendationItemWishlist(parentPosition1, childPosition1, wishlistCurrentStateFor33)
                wishlistViewmodel.updateRecommendationItemWishlist(parentPosition2, childPosition2, wishlistCurrentStateFor55)
            }

            Then("Expect product in parent position = 4 and child position = 2 updated with new wishlist status in wishlist data") {
                val recommendationCarouselDataModel =
                        wishlistViewmodel.wishlistData.value!![parentPosition1] as RecommendationCarouselDataModel
                val recommendationCarouselItemDataModel =
                        recommendationCarouselDataModel.list[childPosition1]

                assertEquals(wishlistCurrentStateFor33, recommendationCarouselItemDataModel.recommendationItem.isWishlist)

                val recommendationCarouselDataModel2 =
                        wishlistViewmodel.wishlistData.value!![parentPosition2] as RecommendationCarouselDataModel
                val recommendationCarouselItemDataModel2 =
                        recommendationCarouselDataModel2.list[childPosition2]

                assertEquals(wishlistCurrentStateFor55, recommendationCarouselItemDataModel2.recommendationItem.isWishlist)
            }
        }
    }

    Feature("Bulk remove wishlist") {
        lateinit var wishlistViewmodel: WishlistViewModel
        val mockUserId = "12345"
        createWishlistTestInstance()
        val userSessionInterface by memoized<UserSessionInterface>()
        val bulkRemoveWishlistUseCase by memoized<BulkRemoveWishlistUseCase>()

        Scenario("Successfully bulk remove wishlist") {
            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("List of wishlist data") {
                wishlistViewmodel.wishlistData.value = listOf(
                        WishlistItemDataModel(WishlistItem(id="1")),
                        WishlistItemDataModel(WishlistItem(id="2")),
                        WishlistItemDataModel(WishlistItem(id="3")),
                        WishlistItemDataModel(WishlistItem(id="4"))
                )
            }
            Given("Bulk remove usecase returns that all data successfully removed from wishlist") {
                every { bulkRemoveWishlistUseCase.execute(any(), any()) }
                        .answers {
                            (secondArg() as Subscriber<List<WishlistData>>).onNext(
                                    listOf(
                                            WishlistData(true, 0),
                                            WishlistData(true, 1),
                                            WishlistData(true, 2),
                                            WishlistData(true, 3)
                                    )
                            )
                        }
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Mark all product position to be removed") {
                wishlistViewmodel.setWishlistOnMarkDelete(productPosition = 0, isChecked = true)
                wishlistViewmodel.setWishlistOnMarkDelete(productPosition = 1, isChecked = true)
                wishlistViewmodel.setWishlistOnMarkDelete(productPosition = 2, isChecked = true)
                wishlistViewmodel.setWishlistOnMarkDelete(productPosition = 3, isChecked = true)
            }

            When("Bulk remove is called") {
                wishlistViewmodel.bulkRemoveWishlist(
                        listOf(0,1,2,3)
                )
            }

            Then("Expect that wishlist data position 1,2,3,4 is removed") {
                assertEquals(0, wishlistViewmodel.wishlistData.value!!.size)
            }
        }

        Scenario("Failed to remove some item") {
            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("List of wishlist data") {
                wishlistViewmodel.wishlistData.value = listOf(
                        WishlistItemDataModel(WishlistItem(id="1")),
                        WishlistItemDataModel(WishlistItem(id="2")),
                        WishlistItemDataModel(WishlistItem(id="3")),
                        WishlistItemDataModel(WishlistItem(id="4"))
                )
            }
            Given("Bulk remove usecase returns that have some failed data") {
                every { bulkRemoveWishlistUseCase.execute(any(), any()) }
                        .answers {
                            (secondArg() as Subscriber<List<WishlistData>>).onNext(
                                    listOf(
                                            WishlistData(true, 0),
                                            WishlistData(false, 1),
                                            WishlistData(false, 2),
                                            WishlistData(true, 3)
                                    )
                            )
                        }
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }

            When("Bulk remove is called to remove wishlist position 1,2,3 and 4") {
                wishlistViewmodel.bulkRemoveWishlist(
                        listOf(0,1,2,3)
                )
            }

            Then("Expect that wishlist data position 1,2,3,4 is removed") {
                assertEquals(2, wishlistViewmodel.wishlistData.value!!.size)
            }
        }
    }
})