package com.tokopedia.home_wishlist.viewModel.addtocart

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.home_wishlist.InstantTaskExecutorRuleSpek
import com.tokopedia.home_wishlist.data.repository.WishlistRepository
import com.tokopedia.home_wishlist.domain.GetWishlistDataUseCase
import com.tokopedia.home_wishlist.model.datamodel.WishlistItemDataModel
import com.tokopedia.home_wishlist.model.entity.Shop
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.viewModel.createWishlistTestInstance
import com.tokopedia.home_wishlist.viewModel.createWishlistViewModel
import com.tokopedia.home_wishlist.viewModel.givenGetWishlistDataReturnsThis
import com.tokopedia.home_wishlist.viewmodel.WishlistViewModel
import io.mockk.every
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Subscriber

class WVMAddToCartWishlist : Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Add to cart in wishlist") {
        createWishlistTestInstance()
        lateinit var wishlistViewmodel: WishlistViewModel
        val addToCartUseCase by memoized<AddToCartUseCase>()
        val getWishlistDataUseCase by memoized<GetWishlistDataUseCase>()

        Scenario("Add to cart product success will trigger AddToCartActionData and add to cart progress flag is set to false") {
            val mockProductCardPositionCandidate = 1
            val mockId1 = "11"
            val mockId2 = "22"

            val shopId1 = "99"
            val shopId2 = "88"
            val minimumOrder = 1

            Given("Create wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Get wishlist data returns this") {
                getWishlistDataUseCase.givenGetWishlistDataReturnsThis(
                        listOf(
                                WishlistItem(id=mockId1, shop = Shop(id = shopId1), minimumOrder = minimumOrder),
                                WishlistItem(id=mockId2, shop = Shop(id = shopId2), minimumOrder = minimumOrder)
                        )
                )
            }
            Given("WishlistViewModel get wishlist data") {
                wishlistViewmodel.getWishlistData()
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
                Assert.assertEquals(mockProductCardPositionCandidate, wishlistAddToCartActionData.peekContent().position)
                Assert.assertEquals(mockId2, wishlistAddToCartActionData.peekContent().productId.toString())
                Assert.assertEquals(true, wishlistAddToCartActionData.peekContent().isSuccess)
            }

            Then("Expect add to cart event can only retrieved once") {
                val wishlistEventData = wishlistViewmodel.addToCartActionData.value!!
                val eventAddToCartFirst = wishlistEventData.getContentIfNotHandled()
                val eventAddToCartSecond = wishlistEventData.getContentIfNotHandled()
                Assert.assertEquals(null, eventAddToCartSecond)
            }

            Then("Expect visitable item candidate add to cart progress is false") {
                val wishlistAddToCartActionData = wishlistViewmodel.wishlistLiveData.value!!
                val wishlistVisitableItem = wishlistAddToCartActionData[mockProductCardPositionCandidate]
                Assert.assertEquals(WishlistItemDataModel::class.java, wishlistVisitableItem.javaClass)
                Assert.assertEquals(false, (wishlistVisitableItem as WishlistItemDataModel).isOnAddToCartProgress)
            }

            Then("Expect selected product addToCart is not in addToCartProgress") {
                val wishlistAddToCartActionData = wishlistViewmodel.wishlistLiveData.value!!
                val wishlistVisitableItem = wishlistAddToCartActionData[mockProductCardPositionCandidate] as WishlistItemDataModel
                Assert.assertEquals(false, wishlistVisitableItem.isOnAddToCartProgress)
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
            Given("Get wishlist data returns this") {
                getWishlistDataUseCase.givenGetWishlistDataReturnsThis(
                        listOf(
                                WishlistItem(id=mockId1, shop = Shop(id = shopId1), minimumOrder = minimumOrder),
                                WishlistItem(id=mockId2, shop = Shop(id = shopId2), minimumOrder = minimumOrder)
                        )
                )
            }
            Given("WishlistViewModel get wishlist data") {
                wishlistViewmodel.getWishlistData()
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
                Assert.assertEquals(mockProductCardPositionCandidate, wishlistAddToCartActionData.value!!.peekContent().position)
                Assert.assertEquals(mockId2, wishlistAddToCartActionData.value!!.peekContent().productId.toString())
                Assert.assertEquals(mockErrorMessage, wishlistAddToCartActionData.value!!.peekContent().message)
                Assert.assertEquals(false, wishlistAddToCartActionData.value!!.peekContent().isSuccess)
            }

            Then("Expect add to cart event can only retrieved once") {
                val wishlistAddToCartActionData = wishlistViewmodel.addToCartActionData
                val eventAddToCartFirst = wishlistAddToCartActionData.value!!.getContentIfNotHandled()
                val eventAddToCartSecond = wishlistAddToCartActionData.value!!.getContentIfNotHandled()
                Assert.assertEquals(eventAddToCartSecond, null)
            }

            Then("Expect visitable item candidate add to cart progress is false") {
                val wishlistAddToCartActionData = wishlistViewmodel.wishlistLiveData.value!!
                val wishlistVisitableItem = wishlistAddToCartActionData[mockProductCardPositionCandidate]
                Assert.assertEquals(WishlistItemDataModel::class.java, wishlistVisitableItem.javaClass)
                Assert.assertEquals(false, (wishlistVisitableItem as WishlistItemDataModel).isOnAddToCartProgress)
            }

            Then("Expect selected product addToCart is not in addToCartProgress") {
                val wishlistAddToCartActionData = wishlistViewmodel.wishlistLiveData.value!!
                val wishlistVisitableItem = wishlistAddToCartActionData[mockProductCardPositionCandidate] as WishlistItemDataModel
                Assert.assertEquals(false, wishlistVisitableItem.isOnAddToCartProgress)
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
            Given("Get wishlist data returns this") {
                getWishlistDataUseCase.givenGetWishlistDataReturnsThis(
                        listOf(
                                WishlistItem(id=mockId1, shop = Shop(id = shopId1), minimumOrder = minimumOrder),
                                WishlistItem(id=mockId2, shop = Shop(id = shopId2), minimumOrder = minimumOrder)
                        )
                )
            }
            Given("WishlistViewModel get wishlist data") {
                wishlistViewmodel.getWishlistData()
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
                Assert.assertEquals(mockProductCardPositionCandidate, wishlistAddToCartActionData.value!!.peekContent().position)
                Assert.assertEquals(mockErrorMessage, wishlistAddToCartActionData.value!!.peekContent().message)
                Assert.assertEquals(false, wishlistAddToCartActionData.value!!.peekContent().isSuccess)
            }

            Then("Expect add to cart event can only retrieved once") {
                val wishlistAddToCartActionData = wishlistViewmodel.addToCartActionData
                val eventAddToCartFirst = wishlistAddToCartActionData.value!!.getContentIfNotHandled()
                val eventAddToCartSecond = wishlistAddToCartActionData.value!!.getContentIfNotHandled()
                Assert.assertEquals(eventAddToCartSecond, null)
            }

            Then("Expect visitable item candidate add to cart progress is false") {
                val wishlistAddToCartActionData = wishlistViewmodel.wishlistLiveData.value!!
                val wishlistVisitableItem = wishlistAddToCartActionData[mockProductCardPositionCandidate]
                Assert.assertEquals(WishlistItemDataModel::class.java, wishlistVisitableItem.javaClass)
                Assert.assertEquals(false, (wishlistVisitableItem as WishlistItemDataModel).isOnAddToCartProgress)
            }

            Then("Expect selected product addToCart is not in addToCartProgress") {
                val wishlistAddToCartActionData = wishlistViewmodel.wishlistLiveData.value!!
                val wishlistVisitableItem = wishlistAddToCartActionData[mockProductCardPositionCandidate] as WishlistItemDataModel
                Assert.assertEquals(false, wishlistVisitableItem.isOnAddToCartProgress)
            }
        }
    }

})