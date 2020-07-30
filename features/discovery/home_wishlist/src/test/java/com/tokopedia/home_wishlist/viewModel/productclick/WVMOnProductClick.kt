package com.tokopedia.home_wishlist.viewModel.productclick

import com.nhaarman.mockitokotlin2.any
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

class WVMOnProductClick : Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Product click feature") {
        createWishlistTestInstance()
        lateinit var wishlistViewmodel: WishlistViewModel
        val getWishlistDataUseCase by memoized<GetWishlistDataUseCase>()
        val getRecommendationUseCase by memoized<GetRecommendationUseCase>()

        val parentPosition = 4
        val position = 2
        val productId = 99

        Scenario("On product click will trigger onProductClick livedata action with selected position") {
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

            When("Wishlistviewmodel onProductClick called") {
                wishlistViewmodel.onProductClick(
                        productId,
                        parentPosition,
                        position
                )
            }

            Then("Expect onProductClickActionData value is filled by parameter") {
                Assert.assertEquals(parentPosition, wishlistViewmodel.productClickActionData.value?.peekContent()?.parentPosition)
                Assert.assertEquals(position, wishlistViewmodel.productClickActionData.value?.peekContent()?.position)
                Assert.assertEquals(productId, wishlistViewmodel.productClickActionData.value?.peekContent()?.productId)
            }
        }
    }
})