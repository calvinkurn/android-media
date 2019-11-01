package com.tokopedia.home_wishlist.viewModel

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.home_wishlist.data.repository.WishlistRepository
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.viewmodel.WishlistViewModel
import com.tokopedia.home_wishlist.InstantTaskExecutorRuleSpek
import com.tokopedia.home_wishlist.TestDispatcherProvider
import com.tokopedia.home_wishlist.model.datamodel.*
import com.tokopedia.home_wishlist.model.entity.Shop
import com.tokopedia.home_wishlist.model.entity.WishlistData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.data.datamodel.WishlistActionData
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.BulkRemoveWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
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
        val addWishListUseCase by memoized<AddWishListUseCase>()

        return WishlistViewModel(
                userSessionInterface = userSessionInterface,
                wishlistRepository = wishlistRepository,
                wishlistCoroutineDispatcherProvider = TestDispatcherProvider(),
                removeWishListUseCase = removeWishlistUseCase,
                addToCartUseCase = addToCartUseCase,
                bulkRemoveWishlistUseCase = bulkRemoveWishlistUseCase,
                addWishListUseCase = addWishListUseCase
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

        val addWishListUseCase by memoized {
            mockk<AddWishListUseCase>(relaxed = true)
        }
    }

    Feature("Add to cart in wishlist") {
        createWishlistTestInstance()
        lateinit var wishlistViewmodel: WishlistViewModel
        val addToCartUseCase by memoized<AddToCartUseCase>()

        Scenario("Add to cart product success will trigger AddToCartActionData and add to cart progress flag is set to false") {
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

            Then("Expect add to cart event is triggered with selected product Id") {
                val wishlistAddToCartActionData = wishlistViewmodel.addToCartActionData.value!!
                assertEquals(mockProductCardPositionCandidate, wishlistAddToCartActionData.peekContent().position)
                assertEquals(mockId2, wishlistAddToCartActionData.peekContent().productId.toString())
                assertEquals(true, wishlistAddToCartActionData.peekContent().isSuccess)
            }

            Then("Expect add to cart event can only retrieved once") {
                val wishlistEventData = wishlistViewmodel.addToCartActionData.value!!
                val eventAddToCartFirst = wishlistEventData.getContentIfNotHandled()
                val eventAddToCartSecond = wishlistEventData.getContentIfNotHandled()
                assertEquals( null, eventAddToCartSecond)
            }

            Then("Expect visitable item candidate add to cart progress is false") {
                val wishlistAddToCartActionData = wishlistViewmodel.wishlistData.value!!
                val wishlistVisitableItem = wishlistAddToCartActionData[mockProductCardPositionCandidate]
                assertEquals(WishlistItemDataModel::class.java, wishlistVisitableItem.javaClass)
                assertEquals(false, (wishlistVisitableItem as WishlistItemDataModel).isOnAddToCartProgress)
            }
        }

        Scenario("Add to cart product failed will trigger AddToCartActionData and add to cart progress flag is set to false") {
            var mockProductCardPositionCandidate = 0
            val mockId1 = "11"
            val mockId2 = "22"

            val shopId1 = "99"
            val shopId2 = "88"
            val minimumOrder = 1

            val mockErrorMessage = "NOT YA"

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
            Given("Add to cart failed and return AddToCartDataModel with error and add to cart progress flag is set to false") {
                every { addToCartUseCase.execute(any(), any()) }.answers {
                    (secondArg() as Subscriber<AddToCartDataModel>).onNext(
                            AddToCartDataModel(
                                    status = "NOT OK",
                                    data = DataModel(success = 0, productId = mockId2.toInt(), message = arrayListOf(mockErrorMessage)),
                                    errorMessage = arrayListOf(mockErrorMessage)
                            )
                    )
                }
            }

            When("Add to cart from wishlist") {
                wishlistViewmodel.addToCartProduct(mockProductCardPositionCandidate)
            }

            Then("Expect add to cart event is triggered with selected product Id and error message") {
                val wishlistAddToCartActionData = wishlistViewmodel.addToCartActionData
                assertEquals(mockProductCardPositionCandidate, wishlistAddToCartActionData.value!!.peekContent().position)
                assertEquals(mockId2, wishlistAddToCartActionData.value!!.peekContent().productId.toString())
                assertEquals(mockErrorMessage, wishlistAddToCartActionData.value!!.peekContent().message)
                assertEquals(false, wishlistAddToCartActionData.value!!.peekContent().isSuccess)
            }

            Then("Expect add to cart event can only retrieved once") {
                val wishlistAddToCartActionData = wishlistViewmodel.addToCartActionData
                val eventAddToCartFirst = wishlistAddToCartActionData.value!!.getContentIfNotHandled()
                val eventAddToCartSecond = wishlistAddToCartActionData.value!!.getContentIfNotHandled()
                assertEquals(eventAddToCartSecond, null)
            }

            Then("Expect visitable item candidate add to cart progress is false") {
                val wishlistAddToCartActionData = wishlistViewmodel.wishlistData.value!!
                val wishlistVisitableItem = wishlistAddToCartActionData[mockProductCardPositionCandidate]
                assertEquals(WishlistItemDataModel::class.java, wishlistVisitableItem.javaClass)
                assertEquals(false, (wishlistVisitableItem as WishlistItemDataModel).isOnAddToCartProgress)
            }
        }

        Scenario("Add to cart throws error will trigger AddToCartActionData and add to cart progress flag is set to false") {
            var mockProductCardPositionCandidate = 0
            val mockId1 = "11"
            val mockId2 = "22"

            val shopId1 = "99"
            val shopId2 = "88"
            val minimumOrder = 1

            val mockErrorMessage = "NOT YA"

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
            Given("Add to cart failed and return AddToCartDataModel with error and add to cart progress flag is set to false") {
                every { addToCartUseCase.execute(any(), any()) }.answers {
                    (secondArg() as Subscriber<AddToCartDataModel>).onError(Throwable(mockErrorMessage))
                }
            }

            When("Add to cart from wishlist") {
                wishlistViewmodel.addToCartProduct(mockProductCardPositionCandidate)
            }

            Then("Expect add to cart event is triggered with error message") {
                val wishlistAddToCartActionData = wishlistViewmodel.addToCartActionData
                assertEquals(mockProductCardPositionCandidate, wishlistAddToCartActionData.value!!.peekContent().position)
                assertEquals(mockErrorMessage, wishlistAddToCartActionData.value!!.peekContent().message)
                assertEquals(false, wishlistAddToCartActionData.value!!.peekContent().isSuccess)
            }

            Then("Expect add to cart event can only retrieved once") {
                val wishlistAddToCartActionData = wishlistViewmodel.addToCartActionData
                val eventAddToCartFirst = wishlistAddToCartActionData.value!!.getContentIfNotHandled()
                val eventAddToCartSecond = wishlistAddToCartActionData.value!!.getContentIfNotHandled()
                assertEquals(eventAddToCartSecond, null)
            }

            Then("Expect visitable item candidate add to cart progress is false") {
                val wishlistAddToCartActionData = wishlistViewmodel.wishlistData.value!!
                val wishlistVisitableItem = wishlistAddToCartActionData[mockProductCardPositionCandidate]
                assertEquals(WishlistItemDataModel::class.java, wishlistVisitableItem.javaClass)
                assertEquals(false, (wishlistVisitableItem as WishlistItemDataModel).isOnAddToCartProgress)
            }
        }
    }

    Feature("Remove wishlist") {
        createWishlistTestInstance()
        lateinit var wishlistViewmodel: WishlistViewModel
        val removeWishlistUseCase by memoized<RemoveWishListUseCase>()
        val userSessionInterface by memoized<UserSessionInterface>()
        Scenario("Remove wishlist success should remove data from wishlistdata") {
            val mockSelectedPosition = 2
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
            Then("Expect that remove wishlist action triggered") {
                val removeWishlistActionData = wishlistViewmodel.removeWishlistActionData
                assertEquals(true, removeWishlistActionData.value!!.peekContent().isSuccess)
            }
            Then("Expect remove wishlist action event can only retrieved once") {
                val wishlistEventRemoveWishlistActionData = wishlistViewmodel.removeWishlistActionData
                val eventRemoveWishlistActionData = wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
                val eventRemoveWishlistActionDataSecond = wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
                assertEquals(eventRemoveWishlistActionDataSecond, null)
            }
        }

        Scenario("Remove wishlist failed should not remove data from wishlistdata") {
            val mockSelectedPosition = 2
            val mockProductId = "3"
            val mockUserId = "54321"
            val mockErrorMessage = "NOT YA"

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
            Given("Remove wishlist usecase failed to remove wishlist") {
                every { removeWishlistUseCase.createObservable(any(), mockUserId, any()) }
                        .answers {
                            (thirdArg() as WishListActionListener).onErrorRemoveWishlist(mockErrorMessage, mockProductId)
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

        Scenario("Successfully bulk remove all selected wishlist") {
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
                            (secondArg() as Subscriber<List<WishlistActionData>>).onNext(
                                    listOf(
                                            WishlistActionData(true, 0),
                                            WishlistActionData(true, 1),
                                            WishlistActionData(true, 2),
                                            WishlistActionData(true, 3)
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
            Then("Expect that wishlist action for bulk remove is success and not partially failed") {
                val bulkRemoveWishlistActionData = wishlistViewmodel.bulkRemoveWishlistActionData.value
                assertEquals(true, bulkRemoveWishlistActionData!!.peekContent().isSuccess)
                assertEquals(false, bulkRemoveWishlistActionData!!.peekContent().isPartiallyFailed)
            }
        }

        Scenario("Failed to remove some item of selected wishlist") {
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
                            (secondArg() as Subscriber<List<WishlistActionData>>).onNext(
                                    listOf(
                                            WishlistActionData(true, 0),
                                            WishlistActionData(false, 1),
                                            WishlistActionData(false, 2),
                                            WishlistActionData(true, 3)
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
            Then("Expect that wishlist action for bulk remove is success but partially failed") {
                val bulkRemoveWishlistActionData = wishlistViewmodel.bulkRemoveWishlistActionData.value
                assertEquals(true, bulkRemoveWishlistActionData!!.peekContent().isSuccess)
                assertEquals(true, bulkRemoveWishlistActionData!!.peekContent().isPartiallyFailed)
            }
        }

        Scenario("Bulk remove wishlist throws error") {
            val mockErrorMessage = "NOT OKAY"
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
                            (secondArg() as Subscriber<List<WishlistActionData>>).onError(
                                    Throwable(mockErrorMessage)
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

            Then("Expect that wishlist data is still same as initial value") {
                assertEquals(4, wishlistViewmodel.wishlistData.value!!.size)
            }
            Then("Expect that wishlist action for bulk remove is failed and not partially failed") {
                val bulkRemoveWishlistActionData = wishlistViewmodel.bulkRemoveWishlistActionData.value
                assertEquals(false, bulkRemoveWishlistActionData!!.peekContent().isSuccess)
                assertEquals(false, bulkRemoveWishlistActionData!!.peekContent().isPartiallyFailed)
                assertEquals(mockErrorMessage, bulkRemoveWishlistActionData!!.peekContent().message)
            }
        }
    }

    Feature("Get wishlist data") {
        lateinit var wishlistViewmodel: WishlistViewModel
        createWishlistTestInstance()
        val mockUserId = "12345"
        val userSessionInterface by memoized<UserSessionInterface>()
        val wishlistRepository by memoized<WishlistRepository>()

        Scenario("Get wishlist data success with empty initial wishlist data will add new wishlist data") {

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Empty list of wishlist data") {
                wishlistViewmodel.wishlistData.value = listOf()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository returns 3 wishlist item data") {
                coEvery { wishlistRepository.getData(any(), any()) } returns WishlistData(
                        items = listOf(
                                WishlistItem(id="1"),
                                WishlistItem(id="2"),
                                WishlistItem(id="3")
                        ),
                        hasNextPage = false
                )
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getWishlistData()
            }

            Then("Expect wishlistdata has only 3 wishlist data") {
                assertEquals(3, wishlistViewmodel.wishlistData.value!!.size)
            }
        }

        Scenario("Get wishlist data failed with empty initial wishlist data will set wishlist data with error model") {

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Empty list of wishlist data") {
                wishlistViewmodel.wishlistData.value = listOf()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository returns 3 wishlist item data") {
                coEvery { wishlistRepository.getData(any(), any()) } returns WishlistData(isSuccess = false, errorMessage = "")
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getWishlistData()
            }

            Then("Expect wishlistdata has only 1 error viewmodel") {
                assertEquals(1, wishlistViewmodel.wishlistData.value!!.size)
                assertEquals(ErrorWishlistDataModel::class.java,
                        wishlistViewmodel.wishlistData.value!![0].javaClass)            }
        }

        Scenario("Get wishlist data with empty initial wishlist data success and received empty wishlist will set wishlist data with empty model") {

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Wishlist data with empty values") {
                wishlistViewmodel.wishlistData.value = listOf()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository returns empty wishlist item data") {
                coEvery { wishlistRepository.getData(any(), any()) } returns WishlistData(
                        items = listOf(),
                        hasNextPage = false
                )
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getWishlistData()
            }

            Then("Expect wishlistdata has only 1 empty wishlist model") {
                assertEquals(1, wishlistViewmodel.wishlistData.value!!.size)
                assertEquals(EmptyWishlistDataModel::class.java,
                        wishlistViewmodel.wishlistData.value!![0].javaClass)
            }
        }

        Scenario("Get wishlist data with non-empty initial wishlist data and received empty wishlist will reset wishlistdata with empty model") {

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Wishlist data with values") {
                wishlistViewmodel.wishlistData.value = listOf(
                        WishlistItemDataModel(WishlistItem(id="1")),
                        WishlistItemDataModel(WishlistItem(id="2")),
                        WishlistItemDataModel(WishlistItem(id="3")),
                        WishlistItemDataModel(WishlistItem(id="4"))
                )
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository returns empty wishlist item data") {
                coEvery { wishlistRepository.getData(any(), any()) } returns WishlistData(
                        items = listOf(),
                        hasNextPage = false
                )
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getWishlistData()
            }

            Then("Expect wishlistdata is reset and has empty viewmodel") {
                assertEquals(1, wishlistViewmodel.wishlistData.value!!.size)
                assertEquals(EmptyWishlistDataModel::class.java,
                        wishlistViewmodel.wishlistData.value!![0].javaClass)
            }
        }

        Scenario("Get wishlist data success with non-empty wishlistdata will overrided by new data") {

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Wishlist data with 4 values") {
                wishlistViewmodel.wishlistData.value = listOf(
                        WishlistItemDataModel(WishlistItem(id="1")),
                        WishlistItemDataModel(WishlistItem(id="2")),
                        WishlistItemDataModel(WishlistItem(id="3")),
                        WishlistItemDataModel(WishlistItem(id="4"))
                )
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository returns 3 wishlist item data") {
                coEvery { wishlistRepository.getData(any(), any()) } returns WishlistData(
                        items = listOf(
                                WishlistItem(id="5"),
                                WishlistItem(id="6"),
                                WishlistItem(id="7")
                        ),
                        hasNextPage = false
                )
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getWishlistData()
            }

            Then("Expect wishlistdata has 3 new wishlist data") {
                assertEquals(3, wishlistViewmodel.wishlistData.value!!.size)
            }
        }

        Scenario("Get wishlist data failed with non-empty wishlistdata will reset wishlist data and add error model") {

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Wishlist data with 4 values") {
                wishlistViewmodel.wishlistData.value = listOf(
                        WishlistItemDataModel(WishlistItem(id="1")),
                        WishlistItemDataModel(WishlistItem(id="2")),
                        WishlistItemDataModel(WishlistItem(id="3")),
                        WishlistItemDataModel(WishlistItem(id="4"))
                )
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository throws error") {
                coEvery { wishlistRepository.getData(any(), any()) } returns WishlistData(isSuccess = false, errorMessage = "")
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getWishlistData()
            }

            Then("Expect wishlistdata is reset and has error model") {
                assertEquals(1, wishlistViewmodel.wishlistData.value!!.size)
                assertEquals(ErrorWishlistDataModel::class.java,
                        wishlistViewmodel.wishlistData.value!![0].javaClass)
            }
        }

        Scenario("Recommendation widget is positioned in position 4 in every page request when fetch recom success") {

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Wishlist data with empty values") {
                wishlistViewmodel.wishlistData.value = listOf()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository returns 9 wishlist item data in a request") {
                coEvery { wishlistRepository.getData(any(), any()) } returns WishlistData(
                        items = listOf(
                                WishlistItem(id="1"),
                                WishlistItem(id="2"),
                                WishlistItem(id="3"),
                                WishlistItem(id="4"),
                                WishlistItem(id="5"),
                                WishlistItem(id="6"),
                                WishlistItem(id="7"),
                                WishlistItem(id="8"),
                                WishlistItem(id="9")
                        ),
                        hasNextPage = false
                )
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf(
                        RecommendationWidget()
                )
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getWishlistData()
            }

            Then("Expect wishlistdata has 10 items (9 wishlist data + 1 recommendation widget)") {
                assertEquals(10, wishlistViewmodel.wishlistData.value!!.size)
            }
            Then("Expect every 4 product recommendation widget is showed") {
                assertEquals(RecommendationCarouselDataModel::class.java,
                        wishlistViewmodel.wishlistData.value!![4].javaClass)
            }
        }

        Scenario("Recommendation widget is not showed when fetch recom return empty") {

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Wishlist data with empty values") {
                wishlistViewmodel.wishlistData.value = listOf()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository returns 9 wishlist item data in a request") {
                coEvery { wishlistRepository.getData(any(), any()) } returns WishlistData(
                        items = listOf(
                                WishlistItem(id="1"),
                                WishlistItem(id="2"),
                                WishlistItem(id="3"),
                                WishlistItem(id="4"),
                                WishlistItem(id="5"),
                                WishlistItem(id="6"),
                                WishlistItem(id="7"),
                                WishlistItem(id="8"),
                                WishlistItem(id="9")
                        ),
                        hasNextPage = false
                )
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getWishlistData()
            }

            Then("Expect wishlistdata has 9 items of wishlist data") {
                assertEquals(9, wishlistViewmodel.wishlistData.value!!.size)
            }
            Then("Expect no recommendation widget is showing") {
                wishlistViewmodel.wishlistData.value!!.forEach {
                    if (it is RecommendationCarouselDataModel) {
                        assertFalse("Recommendation widget should not existed", true)
                    }
                }
            }
        }

        Scenario("Get wishlist data increase page number when success") {

            val defaultGetWishlistPageNumber = 0
            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Wishlist initial value empty data") {
                wishlistViewmodel.wishlistData.value = listOf()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository successfully returns 3 wishlist item data") {
                coEvery { wishlistRepository.getData(any(), any()) } returns WishlistData(
                        items = listOf(
                                WishlistItem(id="1"),
                                WishlistItem(id="2"),
                                WishlistItem(id="3")
                        ),
                        hasNextPage = true
                )
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getWishlistData()
            }

            Then("Expect page number become 3") {
                assertEquals(defaultGetWishlistPageNumber+1, wishlistViewmodel.currentPage)
            }
        }

        Scenario("Get wishlist data doesn't increase page number when failed") {

            val defaultGetWishlistPageNumber = 0
            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Wishlist data with currentPage = 2") {
                wishlistViewmodel.currentPage = 2
            }
            Given("Wishlist initial value empty data") {
                wishlistViewmodel.wishlistData.value = listOf()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository failed to fetch data") {
                coEvery { wishlistRepository.getData(any(), any()) } returns WishlistData(
                        isSuccess = false,
                        errorMessage = ""
                )
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getWishlistData()
            }

            Then("Expect page number still in default page number") {
                assertEquals(defaultGetWishlistPageNumber, wishlistViewmodel.currentPage)
            }
        }
    }

    Feature("Get wishlist next page data") {
        lateinit var wishlistViewmodel: WishlistViewModel
        createWishlistTestInstance()
        val mockUserId = "12345"
        val userSessionInterface by memoized<UserSessionInterface>()
        val wishlistRepository by memoized<WishlistRepository>()

        Scenario("Get next page data and received empty wishlist would not change wishlist data value") {

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Wishlist data with values") {
                wishlistViewmodel.wishlistData.value = listOf(
                        WishlistItemDataModel(WishlistItem(id="1")),
                        WishlistItemDataModel(WishlistItem(id="2")),
                        WishlistItemDataModel(WishlistItem(id="3"))
                )
            }
            Given("Current wishlist page request") {
                wishlistViewmodel.currentPage = 99
                wishlistViewmodel.keywordSearch = "contoh"
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository returns empty wishlist item data") {
                coEvery { wishlistRepository.getData(any(), any()) } returns WishlistData(
                        items = listOf(),
                        hasNextPage = false,
                        isSuccess = true
                )
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getNextPageWishlistData()
            }

            Then("Expect wishlist data is still same as initial value") {
                assertEquals(3, wishlistViewmodel.wishlistData.value!!.size)
            }
        }

        Scenario("Get next page data success will add existing data with new data") {

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Wishlist data with 4 values") {
                wishlistViewmodel.wishlistData.value = listOf(
                        WishlistItemDataModel(WishlistItem(id="1")),
                        WishlistItemDataModel(WishlistItem(id="2")),
                        WishlistItemDataModel(WishlistItem(id="3")),
                        WishlistItemDataModel(WishlistItem(id="4"))
                )
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Current wishlist page request") {
                wishlistViewmodel.currentPage = 99
                wishlistViewmodel.keywordSearch = "contoh"
            }
            Given("Wishlist repository returns 3 wishlist item data") {
                coEvery { wishlistRepository.getData(any(), any()) } returns WishlistData(
                        items = listOf(
                                WishlistItem(id="5"),
                                WishlistItem(id="6"),
                                WishlistItem(id="7")
                        ),
                        hasNextPage = false,
                        isSuccess = true
                )
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getNextPageWishlistData()
            }

            Then("Expect wishlistdata has 3 new wishlist data") {
                assertEquals(7, wishlistViewmodel.wishlistData.value!!.size)
            }
        }

        Scenario("Get next page data failed and would not change wishlist data value and trigger load more action data") {

            val mockErrorMessage = "NOT OKAY"
            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Wishlist data with 4 values") {
                wishlistViewmodel.wishlistData.value = listOf(
                        WishlistItemDataModel(WishlistItem(id="1")),
                        WishlistItemDataModel(WishlistItem(id="2")),
                        WishlistItemDataModel(WishlistItem(id="3")),
                        WishlistItemDataModel(WishlistItem(id="4"))
                )
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Current wishlist page request") {
                wishlistViewmodel.currentPage = 99
                wishlistViewmodel.keywordSearch = "contoh"
            }
            Given("Wishlist repository throws error") {
                coEvery { wishlistRepository.getData(any(), any()) } returns WishlistData(isSuccess = false, errorMessage = mockErrorMessage)
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getNextPageWishlistData()
            }

            Then("Expect wishlist data is not changed") {
                assertEquals(4, wishlistViewmodel.wishlistData.value!!.size)
            }
            Then("Expect load more action data is triggered with error message") {
                assertEquals(false, wishlistViewmodel.loadMoreWishlistAction.value!!.peekContent().isSuccess)
                assertEquals(mockErrorMessage, wishlistViewmodel.loadMoreWishlistAction.value!!.peekContent().message)
            }
            Then("Expect load more wishlist action data can only retrieved once") {
                val wishlistEventData = wishlistViewmodel.loadMoreWishlistAction.value!!
                val eventLoadMoreFirst = wishlistEventData.getContentIfNotHandled()
                val eventLoadMoreSecond = wishlistEventData.getContentIfNotHandled()
                assertEquals( null, eventLoadMoreSecond)
            }
        }

        Scenario("Get next page data throws error and would not change wishlist data value and trigger load more action data") {

            val mockErrorMessage = "NOT OKAY"
            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Wishlist data with 4 values") {
                wishlistViewmodel.wishlistData.value = listOf(
                        WishlistItemDataModel(WishlistItem(id="1")),
                        WishlistItemDataModel(WishlistItem(id="2")),
                        WishlistItemDataModel(WishlistItem(id="3")),
                        WishlistItemDataModel(WishlistItem(id="4"))
                )
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Current wishlist page request") {
                wishlistViewmodel.currentPage = 99
                wishlistViewmodel.keywordSearch = "contoh"
            }
            Given("Wishlist repository throws error") {
                coEvery { wishlistRepository.getData(any(), any()) } answers {
                    throw Throwable(mockErrorMessage)
                }
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getNextPageWishlistData()
            }

            Then("Expect wishlist data is not changed") {
                assertEquals(4, wishlistViewmodel.wishlistData.value!!.size)
            }
            Then("Expect load more action data is triggered with error message") {
                assertEquals(false, wishlistViewmodel.loadMoreWishlistAction.value!!.peekContent().isSuccess)
                assertEquals(mockErrorMessage, wishlistViewmodel.loadMoreWishlistAction.value!!.peekContent().message)
            }
            Then("Expect load more wishlist action data can only retrieved once") {
                val wishlistEventData = wishlistViewmodel.loadMoreWishlistAction.value!!
                val eventLoadMoreFirst = wishlistEventData.getContentIfNotHandled()
                val eventLoadMoreSecond = wishlistEventData.getContentIfNotHandled()
                assertEquals( null, eventLoadMoreSecond)
            }
        }

        Scenario("Recommendation widget is positioned in position 4 in every load more page request when fetch recom success") {

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Wishlist data with empty values") {
                wishlistViewmodel.wishlistData.value = listOf()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Current wishlist page request") {
                wishlistViewmodel.currentPage = 99
                wishlistViewmodel.keywordSearch = "contoh"
            }
            Given("Wishlist repository returns 9 wishlist item data in a request") {
                coEvery { wishlistRepository.getData(any(), any()) } returns WishlistData(
                        items = listOf(
                                WishlistItem(id="1"),
                                WishlistItem(id="2"),
                                WishlistItem(id="3"),
                                WishlistItem(id="4"),
                                WishlistItem(id="5"),
                                WishlistItem(id="6"),
                                WishlistItem(id="7"),
                                WishlistItem(id="8"),
                                WishlistItem(id="9")
                        ),
                        hasNextPage = false
                )
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf(
                        RecommendationWidget()
                )
            }

            When("Viewmodel get next page wishlist data") {
                wishlistViewmodel.getNextPageWishlistData()
            }

            Then("Expect wishlistdata has 10 items (9 wishlist data + 1 recommendation widget)") {
                assertEquals(10, wishlistViewmodel.wishlistData.value!!.size)
            }
            Then("Expect every 4 product recommendation widget is showed") {
                assertEquals(RecommendationCarouselDataModel::class.java,
                        wishlistViewmodel.wishlistData.value!![4].javaClass)
            }
        }

        Scenario("Recommendation widget is not showed when fetch recom return empty") {

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Wishlist data with empty values") {
                wishlistViewmodel.wishlistData.value = listOf()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Current wishlist page request") {
                wishlistViewmodel.currentPage = 99
                wishlistViewmodel.keywordSearch = "contoh"
            }
            Given("Wishlist repository returns 9 wishlist item data in a request") {
                coEvery { wishlistRepository.getData(any(), any()) } returns WishlistData(
                        items = listOf(
                                WishlistItem(id="1"),
                                WishlistItem(id="2"),
                                WishlistItem(id="3"),
                                WishlistItem(id="4"),
                                WishlistItem(id="5"),
                                WishlistItem(id="6"),
                                WishlistItem(id="7"),
                                WishlistItem(id="8"),
                                WishlistItem(id="9")
                        ),
                        hasNextPage = false
                )
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getNextPageWishlistData()
            }

            Then("Expect wishlistdata has 9 items of wishlist data") {
                assertEquals(9, wishlistViewmodel.wishlistData.value!!.size)
            }
            Then("Expect no recommendation widget is showing") {
                wishlistViewmodel.wishlistData.value!!.forEach {
                    if (it is RecommendationCarouselDataModel) {
                        assertFalse("Recommendation widget should not existed", true)
                    }
                }
            }
        }

        Scenario("Get next page data increase page number when fetch data success") {

            val mockCurrentPage = 88
            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Wishlist initial value empty data") {
                wishlistViewmodel.wishlistData.value = listOf()
            }
            Given("Current wishlist page request") {
                wishlistViewmodel.currentPage = mockCurrentPage
                wishlistViewmodel.keywordSearch = "contoh"
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository successfully returns 3 wishlist item data") {
                coEvery { wishlistRepository.getData(any(), any()) } returns WishlistData(
                        items = listOf(
                                WishlistItem(id="1"),
                                WishlistItem(id="2"),
                                WishlistItem(id="3")
                        ),
                        hasNextPage = true,
                        isSuccess = true
                )
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }

            When("Viewmodel get next page wishlist data") {
                wishlistViewmodel.getNextPageWishlistData()
            }

            Then("Expect page number increased") {
                assertEquals(mockCurrentPage+1, wishlistViewmodel.currentPage)
            }
        }

        Scenario("Get next page data doesn't increase page number when fetch data failed") {

            val mockCurrentPage = 88
            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Wishlist initial value empty data") {
                wishlistViewmodel.wishlistData.value = listOf()
            }
            Given("Current wishlist page request") {
                wishlistViewmodel.currentPage = mockCurrentPage
                wishlistViewmodel.keywordSearch = "contoh"
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository successfully returns 3 wishlist item data") {
                coEvery { wishlistRepository.getData(any(), any()) } returns WishlistData(
                        items = listOf(),
                        hasNextPage = true,
                        isSuccess = false
                )
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }

            When("Viewmodel get next page wishlist data") {
                wishlistViewmodel.getNextPageWishlistData()
            }

            Then("Expect page number not increased") {
                assertEquals(mockCurrentPage, wishlistViewmodel.currentPage)
            }
        }

        Scenario("Get next page data doesn't increase page number when fetch data throws error") {

            val mockCurrentPage = 88
            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Wishlist initial value empty data") {
                wishlistViewmodel.wishlistData.value = listOf()
            }
            Given("Current wishlist page request") {
                wishlistViewmodel.currentPage = mockCurrentPage
                wishlistViewmodel.keywordSearch = "contoh"
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository successfully returns 3 wishlist item data") {
                coEvery { wishlistRepository.getData(any(), any()) } answers {
                    throw Throwable("Error")
                }
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }

            When("Viewmodel get next page wishlist data") {
                wishlistViewmodel.getNextPageWishlistData()
            }

            Then("Expect page number not increased") {
                assertEquals(mockCurrentPage, wishlistViewmodel.currentPage)
            }
        }
    }
})