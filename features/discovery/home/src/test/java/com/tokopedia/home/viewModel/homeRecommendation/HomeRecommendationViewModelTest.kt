package com.tokopedia.home.viewModel.homeRecommendation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.domain.gql.feed.Product
import com.tokopedia.home.beranda.domain.interactor.GetHomeRecommendationUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.*
import com.tokopedia.home.beranda.presentation.viewModel.HomeRecommendationViewModel
import com.tokopedia.home.ext.observeOnce
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import io.mockk.*
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

/**
 * Created by Lukas on 14/05/20.
 */

class HomeRecommendationViewModelTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeRecommendationUseCase = mockk<GetHomeRecommendationUseCase>(relaxed = true)
    private val topAdsImageViewUseCase = mockk< TopAdsImageViewUseCase>(relaxed = true)
    private val topAdsUrlHitter = mockk<TopAdsUrlHitter>(relaxed = true)
    private val homeRecommendationViewModel = HomeRecommendationViewModel(getHomeRecommendationUseCase, topAdsImageViewUseCase, CoroutineTestDispatchersProvider)

    @Test
    fun `Get Success Data Home Recommendation Initial Page`(){
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> = mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
                homeRecommendations = listOf(
                        HomeRecommendationItemDataModel(
                                Product(),
                                position = 1
                        )
                ),
                isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(observerHomeRecommendation)

        homeRecommendationViewModel.loadInitialPage("", 1, 0)

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
            })
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel
            })
        }
        confirmVerified(observerHomeRecommendation)

        homeRecommendationViewModel.homeRecommendationNetworkLiveData.observeOnce {
            assert(it.isSuccess)
        }
    }

    @Test
    fun `Get Empty Data Home Recommendation Initial Page`(){
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> = mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
                homeRecommendations = listOf(),
                isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(observerHomeRecommendation)
        homeRecommendationViewModel.loadInitialPage("", 1, 0)

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
            })
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationEmpty
            })
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Error Data Home Recommendation Initial Page`(){
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> = mockk(relaxed = true)
        getHomeRecommendationUseCase.givenThrowReturn()

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(observerHomeRecommendation)
        homeRecommendationViewModel.loadInitialPage("",1, 0)

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
            })
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationError
            })
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & try load more`(){
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> = mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
                homeRecommendations = listOf(
                        HomeRecommendationItemDataModel(
                                Product(),
                                position = 1
                        ),
                        HomeRecommendationItemDataModel(
                                Product(),
                                position = 1
                        )
                ),
                isHasNextPage = true
        )
        val homeRecommendationDataModel2 = HomeRecommendationDataModel(
                homeRecommendations = listOf(
                        HomeRecommendationItemDataModel(
                                Product(),
                                position = 1
                        ),
                        HomeRecommendationItemDataModel(
                                Product(),
                                position = 1
                        )
                ),
                isHasNextPage = true
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel, homeRecommendationDataModel2)

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(observerHomeRecommendation)

        homeRecommendationViewModel.loadInitialPage("",1, 0)

        homeRecommendationViewModel.loadNextData("",1, 0, 2)

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
            })
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel &&
                        it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size
            })
            // check on end data is home recommendation loading
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.last() is HomeRecommendationLoadMore &&
                        it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size + 1
            })
            // check on end data is home recommendation data after load more
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.last() is HomeRecommendationItemDataModel &&
                        it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size + homeRecommendationDataModel2.homeRecommendations.size
            })
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & error load more`(){
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> = mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
                homeRecommendations = listOf(
                        HomeRecommendationItemDataModel(
                                Product(),
                                position = 1
                        ),
                        HomeRecommendationItemDataModel(
                                Product(),
                                position = 1
                        )
                ),
                isHasNextPage = true
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel, TimeoutException())

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(observerHomeRecommendation)

        homeRecommendationViewModel.loadInitialPage("",1, 0)

        homeRecommendationViewModel.loadNextData("", 1, 0, 2)

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
            })
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel &&
                        it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size
            })
            // check on end data is home recommendation loading
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.last() is HomeRecommendationLoadMore &&
                        it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size + 1
            })
            // check on end data is home recommendation data after load more
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.last() is HomeRecommendationItemDataModel &&
                        it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size
            })
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & Update Wishlist With Correct Object`(){
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> = mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
                homeRecommendations = listOf(
                        HomeRecommendationItemDataModel(
                                Product(id = "12", isWishlist = false),
                                position = 1
                        )
                ),
                isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(observerHomeRecommendation)

        homeRecommendationViewModel.loadInitialPage("", 1, 0)

        homeRecommendationViewModel.updateWishlist("12", 0, true)

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
            })
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel
            })
            // check on data is home recommendation item is Wishlisted
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel &&
                        (it.homeRecommendations.first() as HomeRecommendationItemDataModel).product.isWishlist
            })
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & Update Wishlist With Incorrect position`(){
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> = mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
                homeRecommendations = listOf(
                        HomeRecommendationItemDataModel(
                                Product(id = "12", isWishlist = false),
                                position = 1
                        )
                ),
                isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(observerHomeRecommendation)

        homeRecommendationViewModel.loadInitialPage("", 1, 0)

        homeRecommendationViewModel.updateWishlist("12", 100, true)

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
            })
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel
            })
            // check on data is home recommendation item is Wishlisted
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel &&
                        (it.homeRecommendations.first() as HomeRecommendationItemDataModel).product.isWishlist
            })
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & Update Wishlist With Incorrect Product ID`(){
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> = mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
                homeRecommendations = listOf(
                        HomeRecommendationItemDataModel(
                                Product(id = "12", isWishlist = false),
                                position = 1
                        )
                ),
                isHasNextPage = false
        )
        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(observerHomeRecommendation)

        homeRecommendationViewModel.loadInitialPage("", 1, 0)

        homeRecommendationViewModel.updateWishlist("1332", 0, true)


        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
            })
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel
            })
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & Send Impression`(){
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> = mockk(relaxed = true)
        val item = HomeRecommendationItemDataModel(
                Product(id = "12", isWishlist = false, trackerImageUrl = "coba",
                        name = "Nama Produk", imageUrl = "https://ecs.tokopedia.com/blablabla.png"),
                position = 1
        )
        val homeRecommendationDataModel = HomeRecommendationDataModel(
                homeRecommendations = listOf(
                        item
                ),
                isHasNextPage = false
        )
        var url = ""
        var productId = ""
        var productName = ""
        var imageUrl = ""
        val slotUrl = slot<String>()
        val slotProductId = slot<String>()
        val slotProductName = slot<String>()
        val slotImageUrl = slot<String>()

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        every { topAdsUrlHitter.hitImpressionUrl(any<String>(), capture(slotUrl), capture(slotProductId),
                capture(slotProductName), capture(slotImageUrl)) } answers {
            url = slotUrl.captured
            productId = slotProductId.captured
            productName = slotProductName.captured
            imageUrl = slotImageUrl.captured
        }


        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(observerHomeRecommendation)

        homeRecommendationViewModel.loadInitialPage("", 1, 0)

        homeRecommendationViewModel.updateWishlist("1332", 0, true)

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
            })
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel
            })
        }
        confirmVerified(observerHomeRecommendation)

        topAdsUrlHitter.hitImpressionUrl(homeRecommendationViewModel::class.java.simpleName, item.product.trackerImageUrl,
                item.product.id, item.product.name, item.product.imageUrl)

        assert(url == item.product.trackerImageUrl)
        assert(productId == item.product.id)
        assert(productName == item.product.name)
        assert(imageUrl == item.product.imageUrl)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & Send Impression & Click`(){
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> = mockk(relaxed = true)
        val item = HomeRecommendationItemDataModel(
                Product(id = "12", isWishlist = false, trackerImageUrl = "coba", clickUrl = "clickUrl"),
                position = 1
        )
        val homeRecommendationDataModel = HomeRecommendationDataModel(
                homeRecommendations = listOf(
                        item
                ),
                isHasNextPage = false
        )
        var url = ""
        var productId = ""
        var productName = ""
        var imageUrl = ""
        val slotUrl = slot<String>()
        val slotProductId = slot<String>()
        val slotProductName = slot<String>()
        val slotImageUrl = slot<String>()

        // set return recommendations
        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)


        // set return impression
        every { topAdsUrlHitter.hitImpressionUrl(any<String>(), capture(slotUrl), capture(slotProductId),
                capture(slotProductName), capture(slotImageUrl)) } answers {
            url = slotUrl.captured
            productId = slotProductId.captured
            productName = slotProductName.captured
            imageUrl = slotImageUrl.captured
        }

        // set return click
        every { topAdsUrlHitter.hitClickUrl(any<String>(), capture(slotUrl), capture(slotProductId),
                capture(slotProductName), capture(slotImageUrl)) } answers {
            url = slotUrl.captured
            productId = slotProductId.captured
            productName = slotProductName.captured
            imageUrl = slotImageUrl.captured
        }


        // home view model
        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(observerHomeRecommendation)

        // viewModel load first page data
        homeRecommendationViewModel.loadInitialPage("", 1, 0)

        // Try click update wishlist
        homeRecommendationViewModel.updateWishlist("1332", 0, true)

        // Expect updated
        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
            })
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel
            })
        }
        confirmVerified(observerHomeRecommendation)


        // View rendered and impression triggered
        topAdsUrlHitter.hitImpressionUrl(homeRecommendationViewModel::class.java.simpleName, item.product.trackerImageUrl,
                item.product.id, item.product.name, item.product.imageUrl)


        // Verify impression
        assert(url == item.product.trackerImageUrl)
        assert(productId == item.product.id)
        assert(productName == item.product.name)
        assert(imageUrl == item.product.imageUrl)


        // View clicked
        topAdsUrlHitter.hitClickUrl(
                homeRecommendationViewModel::class.java.simpleName,
                item.product.clickUrl,
                item.product.id,
                item.product.name,
                item.product.imageUrl)


        // Verify click
        assert(url == item.product.clickUrl)
        assert(productId == item.product.id)
        assert(productName == item.product.name)
        assert(imageUrl == item.product.imageUrl)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page and load topads banner`(){
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> = mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
                homeRecommendations = listOf(
                        HomeRecommendationItemDataModel(
                                Product(),
                                position = 1
                        ),
                        HomeRecommendationBannerTopAdsDataModel(position = 1)
                ),
                isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        topAdsImageViewUseCase.givenDataReturn(arrayListOf(TopAdsImageViewModel()))

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(observerHomeRecommendation)

        homeRecommendationViewModel.loadInitialPage("", 1, 0)

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
            })
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel
                        && it.homeRecommendations[1] is HomeRecommendationBannerTopAdsDataModel
            })
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page and load topads banner is empty`(){
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> = mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
                homeRecommendations = listOf(
                        HomeRecommendationItemDataModel(
                                Product(),
                                position = 1
                        ),
                        HomeRecommendationBannerTopAdsDataModel(position = 1)
                ),
                isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        topAdsImageViewUseCase.givenDataReturn(arrayListOf())

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(observerHomeRecommendation)

        homeRecommendationViewModel.loadInitialPage("", 1, 0)

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
            })
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel
                        && it.homeRecommendations.size == 1
            })
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page and load topads banner is error`(){
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> = mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
                homeRecommendations = listOf(
                        HomeRecommendationItemDataModel(
                                Product(),
                                position = 1
                        ),
                        HomeRecommendationBannerTopAdsDataModel(position = 1)
                ),
                isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        topAdsImageViewUseCase.givenThrows()

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(observerHomeRecommendation)

        homeRecommendationViewModel.loadInitialPage("", 1, 0)

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
            })
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel
                        && it.homeRecommendations.size == 1
            })
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & try load more and get success topads banner`(){
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> = mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
                homeRecommendations = listOf(
                        HomeRecommendationItemDataModel(
                                Product(),
                                position = 1
                        ),
                        HomeRecommendationBannerTopAdsDataModel(position = 1)
                ),
                isHasNextPage = true
        )
        val homeRecommendationDataModel2 = HomeRecommendationDataModel(
                homeRecommendations = listOf(
                        HomeRecommendationItemDataModel(
                                Product(),
                                position = 1
                        ),
                        HomeRecommendationBannerTopAdsDataModel(position = 1)
                ),
                isHasNextPage = true
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel, homeRecommendationDataModel2)

        topAdsImageViewUseCase.givenDataReturn(arrayListOf(TopAdsImageViewModel()))

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(observerHomeRecommendation)

        homeRecommendationViewModel.loadInitialPage("",1, 0)

        homeRecommendationViewModel.loadNextData("",1, 0, 2)

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
            })
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel &&
                        it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size &&
                        it.homeRecommendations[1] is HomeRecommendationBannerTopAdsDataModel
            })
            // check on end data is home recommendation loading
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.last() is HomeRecommendationLoadMore &&
                        it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size + 1
            })
            // check on end data is home recommendation data after load more
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() &&
                        it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size + homeRecommendationDataModel2.homeRecommendations.size&&
                        it.homeRecommendations[it.homeRecommendations.size - 1] is HomeRecommendationBannerTopAdsDataModel
            })
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & try load more and get success topads banner and empty for loadmore topads banner`(){
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> = mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
                homeRecommendations = listOf(
                        HomeRecommendationItemDataModel(
                                Product(),
                                position = 1
                        ),
                        HomeRecommendationItemDataModel(
                                Product(),
                                position = 1
                        ),
                        HomeRecommendationBannerTopAdsDataModel(position = 2)
                ),
                isHasNextPage = true
        )
        val homeRecommendationDataModel2 = HomeRecommendationDataModel(
                homeRecommendations = listOf(
                        HomeRecommendationItemDataModel(
                                Product(),
                                position = 1
                        ),
                        HomeRecommendationItemDataModel(
                                Product(),
                                position = 1
                        ),
                        HomeRecommendationBannerTopAdsDataModel(position = 2)
                ),
                isHasNextPage = true
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel, homeRecommendationDataModel2)

        topAdsImageViewUseCase.givenDataReturn(arrayListOf(TopAdsImageViewModel()), arrayListOf())

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(observerHomeRecommendation)

        homeRecommendationViewModel.loadInitialPage("",1, 0)

        homeRecommendationViewModel.loadNextData("",1, 0, 2)

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
            })
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel &&
                        it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size &&
                        it.homeRecommendations[it.homeRecommendations.size - 1] is HomeRecommendationBannerTopAdsDataModel
            })
            // check on end data is home recommendation loading
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.last() is HomeRecommendationLoadMore &&
                        it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size + 1
            })
            // check on end data is home recommendation data after load more
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() &&
                        it.homeRecommendations[it.homeRecommendations.size - 1] !is HomeRecommendationBannerTopAdsDataModel
            })
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & try load more and get success topads banner and error for loadmore topads banner`(){
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> = mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
                homeRecommendations = listOf(
                        HomeRecommendationItemDataModel(
                                Product(),
                                position = 1
                        ),
                        HomeRecommendationItemDataModel(
                                Product(),
                                position = 1
                        ),
                        HomeRecommendationBannerTopAdsDataModel(position = 2)
                ),
                isHasNextPage = true
        )
        val homeRecommendationDataModel2 = HomeRecommendationDataModel(
                homeRecommendations = listOf(
                        HomeRecommendationItemDataModel(
                                Product(),
                                position = 1
                        ),
                        HomeRecommendationItemDataModel(
                                Product(),
                                position = 1
                        ),
                        HomeRecommendationBannerTopAdsDataModel(position = 2)
                ),
                isHasNextPage = true
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel, homeRecommendationDataModel2)

        topAdsImageViewUseCase.givenDataReturnAndThenThrows(arrayListOf(TopAdsImageViewModel()))

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(observerHomeRecommendation)

        homeRecommendationViewModel.loadInitialPage("",1, 0)

        homeRecommendationViewModel.loadNextData("",1, 0, 2)

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
            })
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel &&
                        it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size &&
                        it.homeRecommendations[it.homeRecommendations.size - 1] is HomeRecommendationBannerTopAdsDataModel
            })
            // check on end data is home recommendation loading
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() && it.homeRecommendations.last() is HomeRecommendationLoadMore &&
                        it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size + 1
            })
            // check on end data is home recommendation data after load more
            observerHomeRecommendation.onChanged(match {
                it.homeRecommendations.isNotEmpty() &&
                        it.homeRecommendations[it.homeRecommendations.size - 1] !is HomeRecommendationBannerTopAdsDataModel
            })
        }
        confirmVerified(observerHomeRecommendation)
    }
}