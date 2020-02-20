package com.tokopedia.home_wishlist.viewModel.removewishlist

import com.tokopedia.home_wishlist.InstantTaskExecutorRuleSpek
import com.tokopedia.home_wishlist.data.repository.WishlistRepository
import com.tokopedia.home_wishlist.domain.GetWishlistDataUseCase
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselDataModel
import com.tokopedia.home_wishlist.model.datamodel.WishlistItemDataModel
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.viewModel.createWishlistTestInstance
import com.tokopedia.home_wishlist.viewModel.createWishlistViewModel
import com.tokopedia.home_wishlist.viewModel.givenGetWishlistDataReturnsThis
import com.tokopedia.home_wishlist.viewModel.givenRepositoryGetRecommendationDataReturnsThis
import com.tokopedia.home_wishlist.viewmodel.WishlistViewModel
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.every
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class WVMRemoveWishlist : Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Remove wishlist") {
        createWishlistTestInstance()
        lateinit var wishlistViewmodel: WishlistViewModel
        val removeWishlistUseCase by memoized<RemoveWishListUseCase>()
        val userSessionInterface by memoized<UserSessionInterface>()
        val getWishlistDataUseCase by memoized<GetWishlistDataUseCase>()
        val getRecommendationUseCase by memoized<GetRecommendationUseCase>()

        Scenario("Remove wishlist success should remove data from wishlistLiveData") {
            val mockSelectedPosition = 2
            val mockProductId = "3"
            val mockUserId = "54321"

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Get wishlist usecase returns wishlist data below recommendation treshold (4)") {
                getWishlistDataUseCase.givenGetWishlistDataReturnsThis(
                        listOf(
                                WishlistItem(id="1"),
                                WishlistItem(id="2"),
                                WishlistItem(id="3")
                        )
                )
            }
            Given("WishlistViewModel get wishlist data") {
                wishlistViewmodel.getWishlistData()
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

            Then("Expect that unwishlisted product is removed from wishlistLiveData") {
                Assert.assertEquals(2, wishlistViewmodel.wishlistLiveData.value!!.size)
                wishlistViewmodel.wishlistLiveData.value!!.forEach {
                    if (it is WishlistItemDataModel) {
                        if (it.productItem.id == mockProductId) {
                            Assert.assertFalse("Unwishlisted product still exist!", true)
                        }
                    }
                }
            }
            Then("Expect that remove wishlist action triggered") {
                val removeWishlistActionData = wishlistViewmodel.removeWishlistActionData
                Assert.assertEquals(true, removeWishlistActionData.value!!.peekContent().isSuccess)
            }
            Then("Expect remove wishlist action event can only retrieved once") {
                val wishlistEventRemoveWishlistActionData = wishlistViewmodel.removeWishlistActionData
                val eventRemoveWishlistActionData = wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
                val eventRemoveWishlistActionDataSecond = wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
                Assert.assertEquals(eventRemoveWishlistActionDataSecond, null)
            }
        }

        Scenario("Remove wishlist failed should not remove data from wishlistLiveData") {
            val mockSelectedPosition = 2
            val mockProductId = "3"
            val mockUserId = "54321"
            val mockErrorMessage = "NOT YA"

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Get wishlist usecase returns wishlist data below recommendation treshold (4)") {
                getWishlistDataUseCase.givenGetWishlistDataReturnsThis(
                        listOf(
                                WishlistItem(id="1"),
                                WishlistItem(id="2"),
                                WishlistItem(id="3")
                        )
                )
            }
            Given("WishlistViewModel get wishlist data") {
                wishlistViewmodel.getWishlistData()
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

            Then("Expect that unwishlisted product is not removed from wishlistLiveData") {
                Assert.assertEquals(3, wishlistViewmodel.wishlistLiveData.value!!.size)
                wishlistViewmodel.wishlistLiveData.value!!.forEach {
                    if (it is WishlistItemDataModel) {
                        if (it.productItem.id == mockProductId) {
                            Assert.assertFalse("Unwishlisted product still in exist", false)
                        }
                    }
                }
            }
            Then("Expect that remove wishlist action triggered") {
                val removeWishlistActionData = wishlistViewmodel.removeWishlistActionData
                Assert.assertEquals(false, removeWishlistActionData.value!!.peekContent().isSuccess)
                Assert.assertEquals(mockErrorMessage, removeWishlistActionData.value!!.peekContent().message)
            }
            Then("Expect remove wishlist action event can only retrieved once") {
                val wishlistEventRemoveWishlistActionData = wishlistViewmodel.removeWishlistActionData
                val eventRemoveWishlistActionData = wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
                val eventRemoveWishlistActionDataSecond = wishlistEventRemoveWishlistActionData.value!!.getContentIfNotHandled()
                Assert.assertEquals(eventRemoveWishlistActionDataSecond, null)
            }
        }

        Scenario("Remove wishlist data should not change recommendation widget position") {
            val mockSelectedPosition = 2
            val mockProductId = "3"
            val mockUserId = "54321"
            val mockErrorMessage = "NOT YA"

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Get wishlist usecase returns wishlist data below recommendation treshold (4)") {
                getWishlistDataUseCase.givenGetWishlistDataReturnsThis(
                        useDefaultWishlistItem = true
                )
            }
            Given("Get recommendation usecase returns 1 recommendation widget") {
                getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(listOf())
            }
            Given("WishlistViewModel get wishlist data") {
                wishlistViewmodel.getWishlistData()
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

            Then("Expect that unwishlisted product is removed from wishlistLiveData") {
                Assert.assertEquals(20, wishlistViewmodel.wishlistLiveData.value!!.size)
                wishlistViewmodel.wishlistLiveData.value!!.forEach {
                    if (it is WishlistItemDataModel) {
                        if (it.productItem.id == mockProductId) {
                            Assert.assertFalse("Unwishlisted product still in exist", false)
                        }
                    }
                }
            }
            Then("Expect that recommendation section position is not changed in position 4") {
                Assert.assertEquals(RecommendationCarouselDataModel::class.java,
                        wishlistViewmodel.wishlistLiveData.value!![4].javaClass)
            }
        }
    }

})