package com.tokopedia.home_wishlist.viewModel.recommendation

import com.tokopedia.home_wishlist.InstantTaskExecutorRuleSpek
import com.tokopedia.home_wishlist.data.repository.WishlistRepository
import com.tokopedia.home_wishlist.domain.GetWishlistDataUseCase
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselDataModel
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.viewModel.createWishlistTestInstance
import com.tokopedia.home_wishlist.viewModel.createWishlistViewModel
import com.tokopedia.home_wishlist.viewModel.givenGetWishlistDataReturnsThis
import com.tokopedia.home_wishlist.viewModel.givenRepositoryGetRecommendationDataReturnsThis
import com.tokopedia.home_wishlist.viewmodel.WishlistViewModel
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class WVMUpdateRecommendationWishlist : Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Update wishlist in existing wishlist data") {
        createWishlistTestInstance()
        lateinit var wishlistViewmodel: WishlistViewModel
        val getWishlistDataUseCase by memoized<GetWishlistDataUseCase>()
        val getRecommendationUseCase by memoized<GetRecommendationUseCase>()
        val parentPosition1 = 4
        val childPosition1 = 2

        val parentPosition2 = 4
        val childPosition2 = 4

        Scenario("Update wishlist will change its wishlist data in wishlistLiveData") {
            val wishlistCurrentStateFor33 = true
            val wishlistCurrentStateFor55 = false

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
            Given("Get recommendation usecase returns recommendation data") {
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

            When("Update wishlist data in selected parent and child") {
                wishlistViewmodel.updateRecommendationItemWishlist(parentPosition1, childPosition1, wishlistCurrentStateFor33)
                wishlistViewmodel.updateRecommendationItemWishlist(parentPosition2, childPosition2, wishlistCurrentStateFor55)
            }

            Then("Expect product in parent position = 4 and child position = 2 updated with new wishlist status in wishlist data") {
                val recommendationCarouselDataModel =
                        wishlistViewmodel.wishlistLiveData.value!![parentPosition1] as RecommendationCarouselDataModel
                val recommendationCarouselItemDataModel =
                        recommendationCarouselDataModel.list[childPosition1]

                Assert.assertEquals(wishlistCurrentStateFor33, recommendationCarouselItemDataModel.recommendationItem.isWishlist)

                val recommendationCarouselDataModel2 =
                        wishlistViewmodel.wishlistLiveData.value!![parentPosition2] as RecommendationCarouselDataModel
                val recommendationCarouselItemDataModel2 =
                        recommendationCarouselDataModel2.list[childPosition2]

                Assert.assertEquals(wishlistCurrentStateFor55, recommendationCarouselItemDataModel2.recommendationItem.isWishlist)
            }
        }
    }
})