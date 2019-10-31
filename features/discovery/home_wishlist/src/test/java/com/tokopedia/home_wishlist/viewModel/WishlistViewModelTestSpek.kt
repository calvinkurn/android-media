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
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
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
        val addWishlistUseCase by memoized<AddWishListUseCase>()
        val addToCartUseCase by memoized<AddToCartUseCase>()
        val bulkRemoveWishlistUseCase by memoized<BulkRemoveWishlistUseCase>()

        return WishlistViewModel(
                userSessionInterface = userSessionInterface,
                wishlistRepository = wishlistRepository,
                wishlistCoroutineDispatcherProvider = TestDispatcherProvider(),
                removeWishListUseCase = removeWishlistUseCase,
                addToCartUseCase = addToCartUseCase,
                bulkRemoveWishlistUseCase = bulkRemoveWishlistUseCase,
                addWishListUseCase = addWishlistUseCase
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
                every { removeWishlistUseCase.createObservable(mockProductId, mockUserId, any()) }
                        .answers {
                            (thirdArg() as WishListActionListener).onSuccessRemoveWishlist(firstArg())
                        }
            }

            When("View model remove a wishlist product") {
                wishlistViewmodel.removeWishlistedProduct(mockProductId)
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

        Scenario("Remove wishlist failed") {
            val mockProductId = "3"
            val mockUserId = "54321"

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
                every { removeWishlistUseCase.createObservable(mockProductId, mockUserId, any()) }
                        .answers {
                            (thirdArg() as WishListActionListener).onErrorRemoveWishlist("Error", firstArg())
                        }
            }

            When("View model remove a wishlist product") {
                wishlistViewmodel.removeWishlistedProduct(mockProductId)
            }

            Then("Expect that unwishlisted product is removed from wishlistData") {
                assertEquals(3, wishlistViewmodel.wishlistData.value!!.size)
                wishlistViewmodel.wishlistData.value!!.forEach {
                    if (it is WishlistItemDataModel) {
                        if (it.productItem.id == mockProductId) {
                            assertFalse("Unwishlisted product still in exist" , false)
                        }
                    }
                }
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

            When("Bulk remove is called to remove wishlist position 1,2,3 and 4") {
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