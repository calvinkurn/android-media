package com.tokopedia.home.viewModel.homeRecommendation

import android.content.Context
import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.domain.gql.feed.Product
import com.tokopedia.home.beranda.domain.interactor.GetHomeRecommendationUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.*
import com.tokopedia.home.beranda.presentation.viewModel.HomeRecommendationViewModel
import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import io.mockk.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.util.concurrent.TimeoutException

//@ExperimentalStdlibApi
//class HomeRecommendationViewModelTest : Spek({
//    InstantTaskExecutorRuleSpek(this)
//    Feature("Get Home Recommendation Data"){
//        lateinit var homeRecommendationViewModel: HomeRecommendationViewModel
//        createHomeRecommendationViewModelTestInstance()
//
//        val getHomeRecommendationUseCase by memoized<GetHomeRecommendationUseCase>()
//        val topAdsUrlHitter by memoized<TopAdsUrlHitter>()
//        val context by memoized<Context>()
//
//        Scenario("Get Success Data Home Recommendation Initial Page"){
//            val observerHomeRecommendation: Observer<HomeRecommendationDataModel> = mockk(relaxed = true)
//            val homeRecommendationDataModel = HomeRecommendationDataModel(
//                    homeRecommendations = listOf(
//                            HomeRecommendationItemDataModel(
//                                    Product(),
//                                    1
//                            )
//                    ),
//                    isHasNextPage = false
//            )
//
//            Given("set return recommendations"){
//                getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)
//            }
//
//            Given("home viewmodel") {
//                homeRecommendationViewModel = createHomeRecommendationViewModel()
//                homeRecommendationViewModel.homeRecommendationLiveData.observeForever(observerHomeRecommendation)
//            }
//            When("viewModel load first page data") {
//                homeRecommendationViewModel.loadInitialPage("", 1, 0)
//            }
//
//            Then("Expect updated") {
//                verifyOrder {
//                    // check on loading
//                    observerHomeRecommendation.onChanged(match {
//                        it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
//                    })
//                    // check on first data is home recommendation item
//                    observerHomeRecommendation.onChanged(match {
//                        it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel
//                    })
//                }
//                confirmVerified(observerHomeRecommendation)
//            }
//        }
//
//        Scenario("Get Empty Data Home Recommendation Initial Page"){
//            val observerHomeRecommendation: Observer<HomeRecommendationDataModel> = mockk(relaxed = true)
//            val homeRecommendationDataModel = HomeRecommendationDataModel(
//                    homeRecommendations = listOf(),
//                    isHasNextPage = false
//            )
//
//            Given("set return recommendations"){
//                getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)
//            }
//
//            Given("home viewmodel") {
//                homeRecommendationViewModel = createHomeRecommendationViewModel()
//                homeRecommendationViewModel.homeRecommendationLiveData.observeForever(observerHomeRecommendation)
//            }
//            When("viewModel load first page data") {
//                homeRecommendationViewModel.loadInitialPage("", 1, 0)
//            }
//
//            Then("Expect updated") {
//                verifyOrder {
//                    // check on loading
//                    observerHomeRecommendation.onChanged(match {
//                        it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
//                    })
//                    // check on first data is home recommendation item
//                    observerHomeRecommendation.onChanged(match {
//                        it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationEmpty
//                    })
//                }
//                confirmVerified(observerHomeRecommendation)
//            }
//        }
//
//        Scenario("Get Error Data Home Recommendation Initial Page"){
//            val observerHomeRecommendation: Observer<HomeRecommendationDataModel> = mockk(relaxed = true)
//
//            Given("set return recommendations"){
//                getHomeRecommendationUseCase.givenThrowReturn()
//            }
//
//            Given("home viewmodel") {
//                homeRecommendationViewModel = createHomeRecommendationViewModel()
//                homeRecommendationViewModel.homeRecommendationLiveData.observeForever(observerHomeRecommendation)
//            }
//            When("viewModel load first page data") {
//                homeRecommendationViewModel.loadInitialPage("",1, 0)
//            }
//
//            Then("Expect updated") {
//                verifyOrder {
//                    // check on loading
//                    observerHomeRecommendation.onChanged(match {
//                        it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
//                    })
//                    // check on first data is home recommendation item
//                    observerHomeRecommendation.onChanged(match {
//                        it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationError
//                    })
//                }
//                confirmVerified(observerHomeRecommendation)
//            }
//        }
//
//        Scenario("Get Success Data Home Recommendation Initial Page & try load more"){
//            val observerHomeRecommendation: Observer<HomeRecommendationDataModel> = mockk(relaxed = true)
//            val homeRecommendationDataModel = HomeRecommendationDataModel(
//                    homeRecommendations = listOf(
//                            HomeRecommendationItemDataModel(
//                                    Product(),
//                                    1
//                            ),
//                            HomeRecommendationItemDataModel(
//                                    Product(),
//                                    1
//                            )
//                    ),
//                    isHasNextPage = true
//            )
//            val homeRecommendationDataModel2 = HomeRecommendationDataModel(
//                    homeRecommendations = listOf(
//                            HomeRecommendationItemDataModel(
//                                    Product(),
//                                    1
//                            ),
//                            HomeRecommendationItemDataModel(
//                                    Product(),
//                                    1
//                            )
//                    ),
//                    isHasNextPage = true
//            )
//
//            Given("set return recommendations"){
//                getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel, homeRecommendationDataModel2)
//            }
//
//            Given("home viewmodel") {
//                homeRecommendationViewModel = createHomeRecommendationViewModel()
//                homeRecommendationViewModel.homeRecommendationLiveData.observeForever(observerHomeRecommendation)
//            }
//
//            When("viewModel load first page data") {
//                homeRecommendationViewModel.loadInitialPage("",1, 0)
//            }
//
//            When("viewModel try load next page data") {
//                homeRecommendationViewModel.loadNextData("",1, 0, 2)
//            }
//
//            Then("Expect updated") {
//                verifyOrder {
//                    // check on loading
//                    observerHomeRecommendation.onChanged(match {
//                        it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
//                    })
//                    // check on first data is home recommendation item
//                    observerHomeRecommendation.onChanged(match {
//                        it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel &&
//                                it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size
//                    })
//                    // check on end data is home recommendation loading
//                    observerHomeRecommendation.onChanged(match {
//                        it.homeRecommendations.isNotEmpty() && it.homeRecommendations.last() is HomeRecommendationLoadMore &&
//                                it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size + 1
//                    })
//                    // check on end data is home recommendation data after load more
//                    observerHomeRecommendation.onChanged(match {
//                        it.homeRecommendations.isNotEmpty() && it.homeRecommendations.last() is HomeRecommendationItemDataModel &&
//                                it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size + homeRecommendationDataModel2.homeRecommendations.size
//                    })
//                }
//                confirmVerified(observerHomeRecommendation)
//            }
//        }
//
//        Scenario("Get Success Data Home Recommendation Initial Page & error load more"){
//            val observerHomeRecommendation: Observer<HomeRecommendationDataModel> = mockk(relaxed = true)
//            val homeRecommendationDataModel = HomeRecommendationDataModel(
//                    homeRecommendations = listOf(
//                            HomeRecommendationItemDataModel(
//                                    Product(),
//                                    1
//                            ),
//                            HomeRecommendationItemDataModel(
//                                    Product(),
//                                    1
//                            )
//                    ),
//                    isHasNextPage = true
//            )
//
//            Given("set return recommendations"){
//                getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel, TimeoutException())
//            }
//
//            Given("home viewmodel") {
//                homeRecommendationViewModel = createHomeRecommendationViewModel()
//                homeRecommendationViewModel.homeRecommendationLiveData.observeForever(observerHomeRecommendation)
//            }
//
//            When("viewModel load first page data") {
//                homeRecommendationViewModel.loadInitialPage("",1, 0)
//            }
//
//            When("viewModel try load next page data") {
//                homeRecommendationViewModel.loadNextData("", 1, 0, 2)
//            }
//
//            Then("Expect updated") {
//                verifyOrder {
//                    // check on loading
//                    observerHomeRecommendation.onChanged(match {
//                        it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
//                    })
//                    // check on first data is home recommendation item
//                    observerHomeRecommendation.onChanged(match {
//                        it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel &&
//                                it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size
//                    })
//                    // check on end data is home recommendation loading
//                    observerHomeRecommendation.onChanged(match {
//                        it.homeRecommendations.isNotEmpty() && it.homeRecommendations.last() is HomeRecommendationLoadMore &&
//                                it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size + 1
//                    })
//                    // check on end data is home recommendation data after load more
//                    observerHomeRecommendation.onChanged(match {
//                        it.homeRecommendations.isNotEmpty() && it.homeRecommendations.last() is HomeRecommendationItemDataModel &&
//                                it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size
//                    })
//                }
//                confirmVerified(observerHomeRecommendation)
//            }
//        }
//
//        Scenario("Get Success Data Home Recommendation Initial Page & Update Wishlist With Correct Object"){
//            val observerHomeRecommendation: Observer<HomeRecommendationDataModel> = mockk(relaxed = true)
//            val homeRecommendationDataModel = HomeRecommendationDataModel(
//                    homeRecommendations = listOf(
//                            HomeRecommendationItemDataModel(
//                                    Product(id = "12", isWishlist = false),
//                                    1
//                            )
//                    ),
//                    isHasNextPage = false
//            )
//
//            Given("set return recommendations"){
//                getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)
//            }
//
//            Given("home viewmodel") {
//                homeRecommendationViewModel = createHomeRecommendationViewModel()
//                homeRecommendationViewModel.homeRecommendationLiveData.observeForever(observerHomeRecommendation)
//            }
//            When("viewModel load first page data") {
//                homeRecommendationViewModel.loadInitialPage("", 1, 0)
//            }
//
//            When("Try click update wishlist"){
//                homeRecommendationViewModel.updateWishlist("12", 0, true)
//            }
//
//            Then("Expect updated") {
//                verifyOrder {
//                    // check on loading
//                    observerHomeRecommendation.onChanged(match {
//                        it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
//                    })
//                    // check on first data is home recommendation item
//                    observerHomeRecommendation.onChanged(match {
//                        it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel
//                    })
//                    // check on data is home recommendation item is Wishlisted
//                    observerHomeRecommendation.onChanged(match {
//                        it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel &&
//                                (it.homeRecommendations.first() as HomeRecommendationItemDataModel).product.isWishlist
//                    })
//                }
//                confirmVerified(observerHomeRecommendation)
//            }
//        }
//
//        Scenario("Get Success Data Home Recommendation Initial Page & Update Wishlist With Incorrect position"){
//            val observerHomeRecommendation: Observer<HomeRecommendationDataModel> = mockk(relaxed = true)
//            val homeRecommendationDataModel = HomeRecommendationDataModel(
//                    homeRecommendations = listOf(
//                            HomeRecommendationItemDataModel(
//                                    Product(id = "12", isWishlist = false),
//                                    1
//                            )
//                    ),
//                    isHasNextPage = false
//            )
//
//            Given("set return recommendations"){
//                getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)
//            }
//
//            Given("home viewmodel") {
//                homeRecommendationViewModel = createHomeRecommendationViewModel()
//                homeRecommendationViewModel.homeRecommendationLiveData.observeForever(observerHomeRecommendation)
//            }
//            When("viewModel load first page data") {
//                homeRecommendationViewModel.loadInitialPage("", 1, 0)
//            }
//
//            When("Try click update wishlist"){
//                homeRecommendationViewModel.updateWishlist("12", 100, true)
//            }
//
//            Then("Expect updated") {
//                verifyOrder {
//                    // check on loading
//                    observerHomeRecommendation.onChanged(match {
//                        it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
//                    })
//                    // check on first data is home recommendation item
//                    observerHomeRecommendation.onChanged(match {
//                        it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel
//                    })
//                    // check on data is home recommendation item is Wishlisted
//                    observerHomeRecommendation.onChanged(match {
//                        it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel &&
//                                (it.homeRecommendations.first() as HomeRecommendationItemDataModel).product.isWishlist
//                    })
//                }
//                confirmVerified(observerHomeRecommendation)
//            }
//        }
//
//        Scenario("Get Success Data Home Recommendation Initial Page & Update Wishlist With Incorrect Product ID"){
//            val observerHomeRecommendation: Observer<HomeRecommendationDataModel> = mockk(relaxed = true)
//            val homeRecommendationDataModel = HomeRecommendationDataModel(
//                    homeRecommendations = listOf(
//                            HomeRecommendationItemDataModel(
//                                    Product(id = "12", isWishlist = false),
//                                    1
//                            )
//                    ),
//                    isHasNextPage = false
//            )
//
//            Given("set return recommendations"){
//                getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)
//            }
//
//            Given("home viewmodel") {
//                homeRecommendationViewModel = createHomeRecommendationViewModel()
//                homeRecommendationViewModel.homeRecommendationLiveData.observeForever(observerHomeRecommendation)
//            }
//            When("viewModel load first page data") {
//                homeRecommendationViewModel.loadInitialPage("", 1, 0)
//            }
//
//            When("Try click update wishlist"){
//                homeRecommendationViewModel.updateWishlist("1332", 0, true)
//            }
//
//            Then("Expect updated") {
//                verifyOrder {
//                    // check on loading
//                    observerHomeRecommendation.onChanged(match {
//                        it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
//                    })
//                    // check on first data is home recommendation item
//                    observerHomeRecommendation.onChanged(match {
//                        it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel
//                    })
//                }
//                confirmVerified(observerHomeRecommendation)
//            }
//        }
//
//        Scenario("Get Success Data Home Recommendation Initial Page & Send Impression"){
//            val observerHomeRecommendation: Observer<HomeRecommendationDataModel> = mockk(relaxed = true)
//            val item = HomeRecommendationItemDataModel(
//                    Product(id = "12", isWishlist = false, trackerImageUrl = "coba",
//                            name = "Nama Produk", imageUrl = "https://ecs.tokopedia.com/blablabla.png"),
//                    1
//            )
//            val homeRecommendationDataModel = HomeRecommendationDataModel(
//                    homeRecommendations = listOf(
//                            item
//                    ),
//                    isHasNextPage = false
//            )
//            var url = ""
//            var productId = ""
//            var productName = ""
//            var imageUrl = ""
//            val slotUrl = slot<String>()
//            val slotProductId = slot<String>()
//            val slotProductName = slot<String>()
//            val slotImageUrl = slot<String>()
//
//            Given("set return recommendations"){
//                getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)
//            }
//
//            Given("set return impression"){
//                every { topAdsUrlHitter.hitImpressionUrl(any(), capture(slotUrl), capture(slotProductId),
//                        capture(slotProductName), capture(slotImageUrl)) } answers {
//                    url = slotUrl.captured
//                    productId = slotProductId.captured
//                    productName = slotProductName.captured
//                    imageUrl = slotImageUrl.captured
//                }
//            }
//
//            Given("home viewmodel") {
//                homeRecommendationViewModel = createHomeRecommendationViewModel()
//                homeRecommendationViewModel.homeRecommendationLiveData.observeForever(observerHomeRecommendation)
//            }
//            When("viewModel load first page data") {
//                homeRecommendationViewModel.loadInitialPage("", 1, 0)
//            }
//
//            When("Try click update wishlist"){
//                homeRecommendationViewModel.updateWishlist("1332", 0, true)
//            }
//
//            Then("Expect updated") {
//                verifyOrder {
//                    // check on loading
//                    observerHomeRecommendation.onChanged(match {
//                        it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
//                    })
//                    // check on first data is home recommendation item
//                    observerHomeRecommendation.onChanged(match {
//                        it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel
//                    })
//                }
//                confirmVerified(observerHomeRecommendation)
//            }
//
//            When("View rendered and impression triggered"){
//                topAdsUrlHitter.hitImpressionUrl(context, item.product.trackerImageUrl,
//                        item.product.id, item.product.name, item.product.imageUrl)
//            }
//
//            Then("Verify impression"){
//                assert(url == item.product.trackerImageUrl)
//                assert(productId == item.product.id)
//                assert(productName == item.product.name)
//                assert(imageUrl == item.product.imageUrl)
//            }
//        }
//
//
//        Scenario("Get Success Data Home Recommendation Initial Page & Send Impression & Click"){
//            val observerHomeRecommendation: Observer<HomeRecommendationDataModel> = mockk(relaxed = true)
//            val item = HomeRecommendationItemDataModel(
//                    Product(id = "12", isWishlist = false, trackerImageUrl = "coba", clickUrl = "clickUrl"),
//                    1
//            )
//            val homeRecommendationDataModel = HomeRecommendationDataModel(
//                    homeRecommendations = listOf(
//                            item
//                    ),
//                    isHasNextPage = false
//            )
//            var url = ""
//            var productId = ""
//            var productName = ""
//            var imageUrl = ""
//            val slotUrl = slot<String>()
//            val slotProductId = slot<String>()
//            val slotProductName = slot<String>()
//            val slotImageUrl = slot<String>()
//
//            Given("set return recommendations"){
//                getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)
//            }
//
//            Given("set return impression"){
//                every { topAdsUrlHitter.hitImpressionUrl(any(), capture(slotUrl), capture(slotProductId),
//                        capture(slotProductName), capture(slotImageUrl)) } answers {
//                    url = slotUrl.captured
//                    productId = slotProductId.captured
//                    productName = slotProductName.captured
//                    imageUrl = slotImageUrl.captured
//                }
//            }
//
//            Given("set return click"){
//                every { topAdsUrlHitter.hitClickUrl(any(), capture(slotUrl), capture(slotProductId),
//                        capture(slotProductName), capture(slotImageUrl)) } answers {
//                    url = slotUrl.captured
//                    productId = slotProductId.captured
//                    productName = slotProductName.captured
//                    imageUrl = slotImageUrl.captured
//                }
//            }
//
//            Given("home viewmodel") {
//                homeRecommendationViewModel = createHomeRecommendationViewModel()
//                homeRecommendationViewModel.homeRecommendationLiveData.observeForever(observerHomeRecommendation)
//            }
//            When("viewModel load first page data") {
//                homeRecommendationViewModel.loadInitialPage("", 1, 0)
//            }
//
//            When("Try click update wishlist"){
//                homeRecommendationViewModel.updateWishlist("1332", 0, true)
//            }
//
//            Then("Expect updated") {
//                verifyOrder {
//                    // check on loading
//                    observerHomeRecommendation.onChanged(match {
//                        it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
//                    })
//                    // check on first data is home recommendation item
//                    observerHomeRecommendation.onChanged(match {
//                        it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel
//                    })
//                }
//                confirmVerified(observerHomeRecommendation)
//            }
//
//            When("View rendered and impression triggered"){
//                topAdsUrlHitter.hitImpressionUrl(context, item.product.trackerImageUrl,
//                        item.product.id, item.product.name, item.product.imageUrl)
//            }
//
//            Then("Verify impression"){
//                assert(url == item.product.trackerImageUrl)
//                assert(productId == item.product.id)
//                assert(productName == item.product.name)
//                assert(imageUrl == item.product.imageUrl)
//            }
//
//
//            When("View clicked"){
//                topAdsUrlHitter.hitClickUrl(context,
//                        item.product.clickUrl,
//                        item.product.id,
//                        item.product.name,
//                        item.product.imageUrl)
//            }
//
//            Then("Verify click"){
//                assert(url == item.product.clickUrl)
//                assert(productId == item.product.id)
//                assert(productName == item.product.name)
//                assert(imageUrl == item.product.imageUrl)
//            }
//        }
//    }
//})