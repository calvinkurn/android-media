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
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class WVMUpdateBulkProgressState : Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Enter bulk progress state") {
        createWishlistTestInstance()
        lateinit var wishlistViewmodel: WishlistViewModel
        val getWishlistDataUseCase by memoized<GetWishlistDataUseCase>()
        val getRecommendationUseCase by memoized<GetRecommendationUseCase>()

        Scenario("Enter bulk mode will update all wishlist data state into bulk mode") {
            Given("Create wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Get wishlist usecase returns wishlist data above recommendation treshold (4)") {
                getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
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
            Given("Get recommendation usecase returns 1 recommendation data") {
                getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(
                        listOf(
                                RecommendationItem(productId = 11),
                                RecommendationItem(productId = 22),
                                RecommendationItem(productId = 33),
                                RecommendationItem(productId = 44),
                                RecommendationItem(productId = 55)
                        )
                )
            }
            Given("Live data is filled by data from getWishlist") {
                wishlistViewmodel.getWishlistData()
            }

            When("Wishlistviewmodel enter bulk mode") {
                wishlistViewmodel.enterBulkMode()
            }

            Then("Expect isInBulkMode value is true") {
               Assert.assertEquals(true, wishlistViewmodel.isInBulkModeState.value)
            }
            Then("Expect all visitable is set to bulk mode") {
                wishlistViewmodel.wishlistLiveData.value!!.forEach {
                    if (it is RecommendationCarouselDataModel && !it.isOnBulkRemoveProgress) {
                        Assert.assertFalse("Item recommendation carousel not in bulk mode", true)
                    }
                    if (it is WishlistItemDataModel && !it.isOnBulkRemoveProgress) {
                        Assert.assertFalse("Item wishlist not in bulk mode", true)
                    }
                }
            }
        }

        Scenario("Exit will update all wishlist data bulk mode state to false") {
            Given("Create wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Get wishlist usecase returns wishlist data above recommendation treshold (4)") {
                getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
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
            Given("Get recommendation usecase returns 1 recommendation data") {
                getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(
                        listOf(
                                RecommendationItem(productId = 11),
                                RecommendationItem(productId = 22),
                                RecommendationItem(productId = 33),
                                RecommendationItem(productId = 44),
                                RecommendationItem(productId = 55)
                        )
                )
            }
            Given("Live data is filled by data from getWishlist") {
                wishlistViewmodel.getWishlistData()
            }
            Given("Some of items is marked") {
                wishlistViewmodel.setWishlistOnMarkDelete(0, true)
                wishlistViewmodel.setWishlistOnMarkDelete(1, true)
                wishlistViewmodel.setWishlistOnMarkDelete(2, true)
                wishlistViewmodel.setWishlistOnMarkDelete(3, true)
            }

            When("Wishlistviewmodel exit bulk mode") {
                wishlistViewmodel.exitBulkMode()
            }

            Then("Expect isInBulkMode value is false") {
                Assert.assertEquals(false, wishlistViewmodel.isInBulkModeState.value)
            }
            Then("Expect all visitable is set to bulk mode") {
                wishlistViewmodel.wishlistLiveData.value!!.forEach {
                    if (it is RecommendationCarouselDataModel && it.isOnBulkRemoveProgress) {
                        Assert.assertFalse("Item recommendation carousel not in bulk mode", true)
                    }
                    if (it is WishlistItemDataModel && it.isOnBulkRemoveProgress) {
                        Assert.assertFalse("Item wishlist not in bulk mode", true)
                    }
                }
            }
        }
    }
})