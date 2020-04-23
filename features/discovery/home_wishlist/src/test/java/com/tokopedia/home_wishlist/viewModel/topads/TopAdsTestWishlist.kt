package com.tokopedia.home_wishlist.viewModel.topads

import com.tokopedia.home_wishlist.InstantTaskExecutorRuleSpek
import com.tokopedia.home_wishlist.domain.GetWishlistDataUseCase
import com.tokopedia.home_wishlist.domain.SendTopAdsUseCase
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.viewModel.createWishlistTestInstance
import com.tokopedia.home_wishlist.viewModel.createWishlistViewModel
import com.tokopedia.home_wishlist.viewModel.givenGetWishlistDataReturnsThis
import com.tokopedia.home_wishlist.viewModel.givenRepositoryGetRecommendationDataReturnsThis
import com.tokopedia.home_wishlist.viewmodel.WishlistViewModel
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import io.mockk.every
import io.mockk.slot
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class TopAdsTestWishlist : Spek({
    InstantTaskExecutorRuleSpek(this)
    Feature("Impression Task"){
        lateinit var viewModel: WishlistViewModel
        createWishlistTestInstance()
        val sendTopAdsUseCase by memoized<SendTopAdsUseCase>()
        val getWishlistDataUseCase by memoized<GetWishlistDataUseCase>()
        val getRecommendationUseCase by memoized<GetRecommendationUseCase>()
        Scenario("View impressed into render layer and should send impression task"){
            var url = ""
            val slotUrl = slot<String>()
            val listRecommendationCarousel = listOf(
                    RecommendationItem(productId = 11, trackerImageUrl = "image url dong")
            )
            Given("Wishlist viewmodel") {
                viewModel = createWishlistViewModel()
            }
            Given("mock topads usecase"){
                every { sendTopAdsUseCase.executeOnBackground(capture(slotUrl)) } answers {
                    url = slotUrl.captured
                }
            }
            Given("Get wishlist usecase returns 9 wishlist data above recommendation treshold (4)") {
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
                        listRecommendationCarousel
                )
            }
            When("send tracker was called"){
                viewModel.sendTopAds("image url dong")
            }
            Then("the url should same with item"){
                assert(url == listRecommendationCarousel.first().trackerImageUrl)
            }
        }
    }

    Feature("Click Task"){
        lateinit var viewModel: WishlistViewModel
        createWishlistTestInstance()
        val sendTopAdsUseCase by memoized<SendTopAdsUseCase>()
        val getWishlistDataUseCase by memoized<GetWishlistDataUseCase>()
        val getRecommendationUseCase by memoized<GetRecommendationUseCase>()
        Scenario("View clicked and should send click topads task"){
            var url = ""
            val slotUrl = slot<String>()
            val listRecommendationCarousel = listOf(
                    RecommendationItem(productId = 11, clickUrl = "image url dong")
            )
            Given("Wishlist viewmodel") {
                viewModel = createWishlistViewModel()
            }
            Given("mock topads usecase"){
                every { sendTopAdsUseCase.executeOnBackground(capture(slotUrl)) } answers {
                    url = slotUrl.captured
                }
            }
            Given("Get wishlist usecase returns 9 wishlist data above recommendation treshold (4)") {
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
                        listRecommendationCarousel
                )
            }
            When("send tracker was called"){
                viewModel.sendTopAds("image url dong")
            }
            Then("the url should same with item"){
                assert(url.isNotBlank())
            }
        }
    }
})