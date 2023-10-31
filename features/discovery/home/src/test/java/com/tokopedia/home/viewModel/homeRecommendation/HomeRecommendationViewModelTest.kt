package com.tokopedia.home.viewModel.homeRecommendation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.domain.interactor.GetHomeRecommendationUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.GetHomeRecommendationCardUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.*
import com.tokopedia.home.beranda.presentation.view.uimodel.HomeRecommendationCardState
import com.tokopedia.home.beranda.presentation.viewModel.HomeRecommendationViewModel
import com.tokopedia.home.ext.observeOnce
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.Cpm
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.TopAdsHeadlineResponse
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.domain.usecase.GetTopAdsHeadlineUseCase
import com.tokopedia.topads.sdk.utils.TopAdsAddressHelper
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

/**
 * Created by Lukas on 14/05/20.
 */

class HomeRecommendationViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule = UnconfinedTestRule()

    private val getHomeRecommendationUseCase = mockk<GetHomeRecommendationUseCase>(relaxed = true)
    private val getHomeRecommendationCardUseCase =
        mockk<GetHomeRecommendationCardUseCase>(relaxed = true)
    private val topAdsImageViewUseCase = mockk<TopAdsImageViewUseCase>(relaxed = true)
    private val topAdsUrlHitter = mockk<TopAdsUrlHitter>(relaxed = true)
    private val getTopAdsHeadlineUseCase = mockk<GetTopAdsHeadlineUseCase>(relaxed = true)
    private val topAdsAddressHelper = mockk<TopAdsAddressHelper>(relaxed = true)
    private val userSessionInterface = mockk<UserSessionInterface>(relaxed = true)

    private val homeRecommendationViewModel = HomeRecommendationViewModel(
        { getHomeRecommendationUseCase },
        { getHomeRecommendationCardUseCase },
        { topAdsImageViewUseCase },
        { getTopAdsHeadlineUseCase },
        { userSessionInterface },
        { topAdsAddressHelper },
        { CoroutineTestDispatchersProvider }
    )

    @Test
    fun `Get Success Data Home Recommendation Initial Page`() {
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> =
            mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(),
                    productCardModel = ProductCardModel(),
                    position = 1
                )
            ),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.loadInitialPage("", 1, 0, sourceType = "")

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)

        homeRecommendationViewModel.homeRecommendationNetworkLiveData.observeOnce {
            assert(it.isSuccess)
        }
    }

    @Test
    fun `Get Empty Data Home Recommendation Initial Page`() {
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> =
            mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
            homeRecommendations = listOf(),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )
        homeRecommendationViewModel.loadInitialPage("", 1, 0, sourceType = "")

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationEmpty
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Error Data Home Recommendation Initial Page`() {
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> =
            mockk(relaxed = true)
        getHomeRecommendationUseCase.givenThrowReturn()

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )
        homeRecommendationViewModel.loadInitialPage("", 1, 0, sourceType = "")

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationError
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & try load more`() {
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> =
            mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(),
                    productCardModel = ProductCardModel(),
                    position = 1
                )
            ),
            isHasNextPage = true
        )
        val homeRecommendationDataModel2 = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(),
                    productCardModel = ProductCardModel(),
                    position = 1
                )
            ),
            isHasNextPage = true
        )

        getHomeRecommendationUseCase.givenDataReturn(
            homeRecommendationDataModel,
            homeRecommendationDataModel2
        )

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.loadInitialPage("", 1, 0, sourceType = "")

        homeRecommendationViewModel.loadNextData("", 1, 0, 2, sourceType = "")

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel &&
                        it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size
                }
            )
            // check on end data is home recommendation loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.last() is HomeRecommendationLoadMore &&
                        it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size + 1
                }
            )
            // check on end data is home recommendation data after load more
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.last() is HomeRecommendationItemDataModel &&
                        it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size + homeRecommendationDataModel2.homeRecommendations.size
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & error load more`() {
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> =
            mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(),
                    productCardModel = ProductCardModel(),
                    position = 1
                )
            ),
            isHasNextPage = true
        )

        getHomeRecommendationUseCase.givenDataReturn(
            homeRecommendationDataModel,
            TimeoutException()
        )

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.loadInitialPage("", 1, 0, sourceType = "")

        homeRecommendationViewModel.loadNextData("", 1, 0, 2, sourceType = "")

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel &&
                        it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size
                }
            )
            // check on end data is home recommendation loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.last() is HomeRecommendationLoadMore &&
                        it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size + 1
                }
            )
            // check on end data is home recommendation data after load more
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.last() is HomeRecommendationItemDataModel &&
                        it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & Update Wishlist With Correct Object`() {
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> =
            mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isTopAds = false
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                )
            ),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.loadInitialPage("", 1, 0, sourceType = "")

        homeRecommendationViewModel.updateWishlist("12", 0, true)

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel
                }
            )
            // check on data is home recommendation item is Wishlisted
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel &&
                        (it.homeRecommendations.first() as HomeRecommendationItemDataModel).recommendationProductItem.isWishlist
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & Update Wishlist With Incorrect position`() {
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> =
            mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isTopAds = false
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                )
            ),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.loadInitialPage("", 1, 0, sourceType = "")

        homeRecommendationViewModel.updateWishlist("12", 100, true)

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel
                }
            )
            // check on data is home recommendation item is Wishlisted
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel &&
                        (it.homeRecommendations.first() as HomeRecommendationItemDataModel).recommendationProductItem.isWishlist
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & Update Wishlist With Incorrect Product ID`() {
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> =
            mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isTopAds = false
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                )
            ),
            isHasNextPage = false
        )
        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.loadInitialPage("", 1, 0, sourceType = "")

        homeRecommendationViewModel.updateWishlist("1332", 0, true)

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & Send Impression`() {
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> =
            mockk(relaxed = true)
        val item = HomeRecommendationItemDataModel(
            recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                id = "12",
                isWishlist = false,
                trackerImageUrl = "coba",
                name = "Nama Produk",
                imageUrl = "https://images.tokopedia.com/blablabla.png"
            ),
            productCardModel = ProductCardModel(),
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

        every {
            topAdsUrlHitter.hitImpressionUrl(
                any<String>(),
                capture(slotUrl),
                capture(slotProductId),
                capture(slotProductName),
                capture(slotImageUrl)
            )
        } answers {
            url = slotUrl.captured
            productId = slotProductId.captured
            productName = slotProductName.captured
            imageUrl = slotImageUrl.captured
        }

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.loadInitialPage("", 1, 0, sourceType = "")

        homeRecommendationViewModel.updateWishlist("1332", 0, true)

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)

        topAdsUrlHitter.hitImpressionUrl(
            homeRecommendationViewModel::class.java.simpleName,
            item.recommendationProductItem.trackerImageUrl,
            item.recommendationProductItem.id,
            item.recommendationProductItem.name,
            item.recommendationProductItem.imageUrl
        )

        assert(url == item.recommendationProductItem.trackerImageUrl)
        assert(productId == item.recommendationProductItem.id)
        assert(productName == item.recommendationProductItem.name)
        assert(imageUrl == item.recommendationProductItem.imageUrl)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & Send Impression & Click`() {
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> =
            mockk(relaxed = true)
        val item = HomeRecommendationItemDataModel(
            recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                id = "12",
                isWishlist = false,
                trackerImageUrl = "coba",
                clickUrl = "clickUrl"
            ),
            productCardModel = ProductCardModel(),
            position = 1,
            tabName = ""
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
        every {
            topAdsUrlHitter.hitImpressionUrl(
                any<String>(),
                capture(slotUrl),
                capture(slotProductId),
                capture(slotProductName),
                capture(slotImageUrl)
            )
        } answers {
            url = slotUrl.captured
            productId = slotProductId.captured
            productName = slotProductName.captured
            imageUrl = slotImageUrl.captured
        }

        // set return click
        every {
            topAdsUrlHitter.hitClickUrl(
                any<String>(),
                capture(slotUrl),
                capture(slotProductId),
                capture(slotProductName),
                capture(slotImageUrl)
            )
        } answers {
            url = slotUrl.captured
            productId = slotProductId.captured
            productName = slotProductName.captured
            imageUrl = slotImageUrl.captured
        }

        // home view model
        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        // viewModel load first page data
        homeRecommendationViewModel.loadInitialPage("", 1, 0, sourceType = "")

        // Try click update wishlist
        homeRecommendationViewModel.updateWishlist("1332", 0, true)

        // Expect updated
        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)

        // View rendered and impression triggered
        topAdsUrlHitter.hitImpressionUrl(
            homeRecommendationViewModel::class.java.simpleName,
            item.recommendationProductItem.trackerImageUrl,
            item.recommendationProductItem.id,
            item.recommendationProductItem.name,
            item.recommendationProductItem.imageUrl
        )

        // Verify impression
        assert(url == item.recommendationProductItem.trackerImageUrl)
        assert(productId == item.recommendationProductItem.id)
        assert(productName == item.recommendationProductItem.name)
        assert(imageUrl == item.recommendationProductItem.imageUrl)

        // View clicked
        topAdsUrlHitter.hitClickUrl(
            homeRecommendationViewModel::class.java.simpleName,
            item.recommendationProductItem.clickUrl,
            item.recommendationProductItem.id,
            item.recommendationProductItem.name,
            item.recommendationProductItem.imageUrl
        )

        // Verify click
        assert(url == item.recommendationProductItem.clickUrl)
        assert(productId == item.recommendationProductItem.id)
        assert(productName == item.recommendationProductItem.name)
        assert(imageUrl == item.recommendationProductItem.imageUrl)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page and load topads banner`() {
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> =
            mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                HomeRecommendationBannerTopAdsDataModel(position = 1)
            ),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        topAdsImageViewUseCase.givenDataReturn(arrayListOf(TopAdsImageViewModel()))

        getTopAdsHeadlineUseCase.givenDataReturn(TopAdsHeadlineResponse())

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.loadInitialPage("", 1, 0, sourceType = "")

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel &&
                        it.homeRecommendations[1] is HomeRecommendationBannerTopAdsDataModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page and load topads banner is empty`() {
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> =
            mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                HomeRecommendationBannerTopAdsDataModel(position = 1)
            ),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        topAdsImageViewUseCase.givenDataReturn(arrayListOf())

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.loadInitialPage("", 1, 0, sourceType = "")

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel &&
                        it.homeRecommendations.size == 1
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page and load topads banner is error`() {
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> =
            mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                HomeRecommendationBannerTopAdsDataModel(position = 1)
            ),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        topAdsImageViewUseCase.givenThrows()

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.loadInitialPage("", 1, 0, sourceType = "")

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel &&
                        it.homeRecommendations.size == 1
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & try load more and get success topads banner`() {
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> =
            mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                HomeRecommendationBannerTopAdsDataModel(position = 1)
            ),
            isHasNextPage = true
        )
        val homeRecommendationDataModel2 = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 2
                ),
                HomeRecommendationBannerTopAdsDataModel(position = 1)
            ),
            isHasNextPage = true
        )

        getHomeRecommendationUseCase.givenDataReturn(
            homeRecommendationDataModel,
            homeRecommendationDataModel2
        )

        topAdsImageViewUseCase.givenDataReturn(arrayListOf(TopAdsImageViewModel()))

        getTopAdsHeadlineUseCase.givenDataReturn(TopAdsHeadlineResponse())

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.loadInitialPage("", 1, 0, sourceType = "")

        homeRecommendationViewModel.loadNextData("", 1, 0, 2, sourceType = "")

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel &&
                        it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size &&
                        it.homeRecommendations[1] is HomeRecommendationBannerTopAdsDataModel
                }
            )
            // check on end data is home recommendation loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.last() is HomeRecommendationLoadMore &&
                        it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size + 1
                }
            )
            // check on end data is home recommendation data after load more
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() &&
                        it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size + homeRecommendationDataModel2.homeRecommendations.size &&
                        it.homeRecommendations[it.homeRecommendations.size - 1] is HomeRecommendationBannerTopAdsDataModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & try load more and get success topads banner and empty for loadmore topads banner`() {
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> =
            mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 2
                ),
                HomeRecommendationBannerTopAdsDataModel(position = 2)
            ),
            isHasNextPage = true
        )
        val homeRecommendationDataModel2 = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 2
                ),
                HomeRecommendationBannerTopAdsDataModel(position = 2)
            ),
            isHasNextPage = true
        )

        getHomeRecommendationUseCase.givenDataReturn(
            homeRecommendationDataModel,
            homeRecommendationDataModel2
        )

        topAdsImageViewUseCase.givenDataReturn(arrayListOf(TopAdsImageViewModel()), arrayListOf())

        getTopAdsHeadlineUseCase.givenDataReturn(TopAdsHeadlineResponse())

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.loadInitialPage("", 1, 0, sourceType = "")

        homeRecommendationViewModel.loadNextData("", 1, 0, 2, sourceType = "")

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel &&
                        it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size &&
                        it.homeRecommendations[it.homeRecommendations.size - 1] is HomeRecommendationBannerTopAdsDataModel
                }
            )
            // check on end data is home recommendation loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.last() is HomeRecommendationLoadMore &&
                        it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size + 1
                }
            )
            // check on end data is home recommendation data after load more
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() &&
                        it.homeRecommendations[it.homeRecommendations.size - 1] !is HomeRecommendationBannerTopAdsDataModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & try load more and get success topads banner and error for loadmore topads banner`() {
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> =
            mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 2
                ),
                HomeRecommendationBannerTopAdsDataModel(position = 2)
            ),
            isHasNextPage = true
        )
        val homeRecommendationDataModel2 = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                HomeRecommendationBannerTopAdsDataModel(position = 2)
            ),
            isHasNextPage = true
        )

        getHomeRecommendationUseCase.givenDataReturn(
            homeRecommendationDataModel,
            homeRecommendationDataModel2
        )

        getTopAdsHeadlineUseCase.givenDataReturn(TopAdsHeadlineResponse())

        topAdsImageViewUseCase.givenDataReturnAndThenThrows(arrayListOf(TopAdsImageViewModel()))

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.loadInitialPage("", 1, 0, sourceType = "")

        homeRecommendationViewModel.loadNextData("", 1, 0, 2, sourceType = "")

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel &&
                        it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size &&
                        it.homeRecommendations[it.homeRecommendations.size - 1] is HomeRecommendationBannerTopAdsDataModel
                }
            )
            // check on end data is home recommendation loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.last() is HomeRecommendationLoadMore &&
                        it.homeRecommendations.size == homeRecommendationDataModel.homeRecommendations.size + 1
                }
            )
            // check on end data is home recommendation data after load more
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() &&
                        it.homeRecommendations[it.homeRecommendations.size - 1] !is HomeRecommendationBannerTopAdsDataModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page and load shopAds on empty Banner`() {
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> =
            mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                )
            ),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        val cpmModel = CpmModel()
        val cpmData = CpmData()
        val cpm = Cpm()
        cpm.position = 0
        cpmData.cpm = cpm
        cpmModel.data = mutableListOf(cpmData)
        val n = TopAdsHeadlineResponse(cpmModel)

        getTopAdsHeadlineUseCase.givenDataReturn(n)

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.loadInitialPage("", 1, 0, sourceType = "")

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
                }
            )
            // check on first data is headline ads item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationHeadlineTopAdsDataModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page  on empty headline response`() {
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> =
            mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                )
            ),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        val cpmModel = CpmModel()
        cpmModel.data = mutableListOf()
        val n = TopAdsHeadlineResponse(cpmModel)

        getTopAdsHeadlineUseCase.givenDataReturn(n)

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.loadInitialPage("", 1, 0, sourceType = "")

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
                }
            )
            // check on first data is headline ads item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page and load shopAds on data have banner but response is empty`() {
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> =
            mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                HomeRecommendationBannerTopAdsDataModel(position = 1)
            ),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        coEvery { topAdsImageViewUseCase.getImageData(any()) } returns arrayListOf()

        val cpmModel = CpmModel()
        val cpmData = CpmData()
        val cpm = Cpm()
        cpm.position = 0
        cpmData.cpm = cpm
        cpmModel.data = mutableListOf(cpmData)
        val n = TopAdsHeadlineResponse(cpmModel)

        getTopAdsHeadlineUseCase.givenDataReturn(n)

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.loadInitialPage("", 1, 0, sourceType = "")

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
                }
            )
            // check on first data is headline ads item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationHeadlineTopAdsDataModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page and load shopAds with topads Banner`() {
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> =
            mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                HomeRecommendationBannerTopAdsDataModel(position = 1)
            ),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        topAdsImageViewUseCase.givenDataReturn(arrayListOf(TopAdsImageViewModel()))

        val cpmModel = CpmModel()
        val cpmData = CpmData()
        val cpm = Cpm()
        cpm.position = 0
        cpmData.cpm = cpm
        cpmModel.data = mutableListOf(cpmData)
        val n = TopAdsHeadlineResponse(cpmModel)

        getTopAdsHeadlineUseCase.givenDataReturn(n)

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.loadInitialPage("", 1, 0, sourceType = "")

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
                }
            )

            // check on first data is home headline ads item and topAds banner at next index
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationHeadlineTopAdsDataModel &&
                        it.homeRecommendations[2] is HomeRecommendationBannerTopAdsDataModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page and load shopAds with topads Banner at position 2`() {
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> =
            mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                HomeRecommendationBannerTopAdsDataModel(position = 1),
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                )

            ),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        topAdsImageViewUseCase.givenDataReturn(arrayListOf(TopAdsImageViewModel()))

        val cpmModel = CpmModel()
        val cpmData = CpmData()
        val cpm = Cpm()
        cpm.position = 2
        cpmData.cpm = cpm
        cpmModel.data = mutableListOf(cpmData)
        val n = TopAdsHeadlineResponse(cpmModel)

        getTopAdsHeadlineUseCase.givenDataReturn(n)

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.loadInitialPage("", 1, 0, sourceType = "")

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
                }
            )

//             check on first data is home headline ads item and topAds banner at next index
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel && it.homeRecommendations[1] is HomeRecommendationBannerTopAdsDataModel &&
                        it.homeRecommendations[3] is HomeRecommendationHeadlineTopAdsDataModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page and load shopAds without topads Banner if postion is higer then main list`() {
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> =
            mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                HomeRecommendationBannerTopAdsDataModel(position = 4)
            ),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        topAdsImageViewUseCase.givenDataReturn(arrayListOf(TopAdsImageViewModel()))

        val cpmModel = CpmModel()
        val cpmData = CpmData()
        val cpm = Cpm()
        cpm.position = 0
        cpmData.cpm = cpm
        cpmModel.data = mutableListOf(cpmData)
        val n = TopAdsHeadlineResponse(cpmModel)

        getTopAdsHeadlineUseCase.givenDataReturn(n)

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.loadInitialPage("", 1, 0, sourceType = "")

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
                }
            )

            // check on first data is home headline ads item and topAds banner at next index
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationHeadlineTopAdsDataModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page and load topads Banner when headlines ads is empty`() {
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> =
            mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                HomeRecommendationBannerTopAdsDataModel(position = 2),
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                )
            ),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        topAdsImageViewUseCase.givenDataReturn(arrayListOf(TopAdsImageViewModel()))

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.loadInitialPage("", 1, 0, tabIndex = 1, sourceType = "")

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
                }
            )

            // check on first data is home recommendation item and topAds banner at index
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel &&
                        it.homeRecommendations[2] is HomeRecommendationBannerTopAdsDataModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page and load topads Banner when headlines ads is empty and banner postion is higer than list`() {
        val observerHomeRecommendation: Observer<HomeRecommendationDataModel> =
            mockk(relaxed = true)
        val homeRecommendationDataModel = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 2
                ),
                HomeRecommendationBannerTopAdsDataModel(position = 7),
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 3
                )
            ),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        topAdsImageViewUseCase.givenDataReturn(arrayListOf(TopAdsImageViewModel()))

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.loadInitialPage("", 1, 0, tabIndex = 1, sourceType = "")

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationLoading
                }
            )

            // check on first data is home recommendation item and topAds banner at index
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel &&
                        it.homeRecommendations[2] is HomeRecommendationBannerTopAdsDataModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    /**
     * New Query For You Recom
     *
     */
    @Test
    fun `Get Success Data Home Recommendation Fetch Initial Page, should return Success State`() {
        val productPage = 1
        val homeRecommendationDataModel = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(),
                    productCardModel = ProductCardModel(),
                    position = 2
                )
            ),
            isHasNextPage = true
        )

        getHomeRecommendationCardUseCase.givenDataReturn(homeRecommendationDataModel, productPage)

        // assert HomeRecommendationCardState is equal LoadingState
        assertCollectingRecommendationCardState {
            assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendationCard("", "", sourceType = "")

        assertCollectingRecommendationCardState {
            val actualResult = (it.first() as HomeRecommendationCardState.Success)
            assertTrue(it.first() is HomeRecommendationCardState.Success)
            assertEquals(
                homeRecommendationDataModel.homeRecommendations,
                actualResult.data.homeRecommendations
            )
            assertEquals(homeRecommendationDataModel.isHasNextPage, actualResult.data.isHasNextPage)
        }
    }

    @Test
    fun `Get Success Data Home Recommendation Fetch Initial Page, should return Empty State`() {
        val productPage = 1
        val homeRecommendationDataModel = HomeRecommendationDataModel(
            homeRecommendations = listOf(),
            isHasNextPage = false
        )

        getHomeRecommendationCardUseCase.givenDataReturn(homeRecommendationDataModel, productPage)

        // assert HomeRecommendationCardState is equal LoadingState
        assertCollectingRecommendationCardState {
            assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendationCard("", "", sourceType = "")

        assertCollectingRecommendationCardState {
            val actualResult = (it.first() as HomeRecommendationCardState.EmptyData)
            assertTrue(it.first() is HomeRecommendationCardState.EmptyData)
            assertEquals(
                listOf(homeRecommendationViewModel.emptyModel),
                actualResult.data.homeRecommendations
            )
            assertEquals(homeRecommendationDataModel.isHasNextPage, actualResult.data.isHasNextPage)
        }
    }

    @Test
    fun `Get Failed Data Home Recommendation Fetch Initial Page, should return Fail State`() {
        val productPage = 1
        val exception = MessageErrorException("something went wrong")

        getHomeRecommendationCardUseCase.givenThrows(exception, productPage)

        // assert HomeRecommendationCardState is equal LoadingState
        assertCollectingRecommendationCardState {
            assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendationCard("", "", sourceType = "")

        assertCollectingRecommendationCardState {
            val actualResult = (it.first() as HomeRecommendationCardState.Fail)
            assertTrue(it.first() is HomeRecommendationCardState.Fail)
            assertEquals(exception.localizedMessage, actualResult.throwable.localizedMessage)
            assertEquals(listOf(homeRecommendationViewModel.errorModel), actualResult.data.homeRecommendations)
        }
    }

    @Test
    fun `Get Success Data Home Recommendation Fetch Next Initial Page, should return SuccessNextPage State`() {
        val productPage = 1
        val homeRecommendationDataModel = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(),
                    productCardModel = ProductCardModel(),
                    position = 2
                )
            ),
            isHasNextPage = true
        )

        getHomeRecommendationCardUseCase.givenDataReturnMatch(homeRecommendationDataModel, productPage)

        // assert HomeRecommendationCardState is equal LoadingState
        assertCollectingRecommendationCardState {
            assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendationCard("", "", sourceType = "")

        assertCollectingRecommendationCardStateWithJob { it, job ->
            val actualResult = (it.first() as HomeRecommendationCardState.Success)
            assertTrue(it.first() is HomeRecommendationCardState.Success)
            assertEquals(
                homeRecommendationDataModel.homeRecommendations,
                actualResult.data.homeRecommendations
            )
            assertEquals(homeRecommendationDataModel.isHasNextPage, actualResult.data.isHasNextPage)

            val productPage = 2
            val homeRecommendationNextDataModel = HomeRecommendationDataModel(
                listOf<HomeRecommendationVisitable>(
                    HomeRecommendationItemDataModel(
                        productCardModel = ProductCardModel(),
                        recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem()
                    ),
                    HomeRecommendationItemDataModel(
                        productCardModel = ProductCardModel(),
                        recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem()
                    )
                ).toList(),
                isHasNextPage = true
            )

            getHomeRecommendationCardUseCase.givenDataReturnMatch(homeRecommendationNextDataModel, productPage)

            homeRecommendationViewModel.fetchNextHomeRecommendationCard(
                "",
                productPage,
                sourceType = "",
                locationParam = ""
            )

            assertTrue(it[1] is HomeRecommendationCardState.LoadingMore)

            val actualNextResult = (it[2] as HomeRecommendationCardState.SuccessNextPage)
            val expectedNextResult = homeRecommendationDataModel.homeRecommendations.toMutableList().apply {
                addAll(
                    homeRecommendationNextDataModel.homeRecommendations
                )
            }.toList()

            assertEquals(
                expectedNextResult,
                actualNextResult.data.homeRecommendations
            )
            assertEquals(homeRecommendationNextDataModel.isHasNextPage, actualNextResult.data.isHasNextPage)

            job.cancel()
        }
    }

    @Test
    fun `Get Failed Data Home Recommendation Fetch Next Initial Page, should return FailNextPage State`() {
        val productPage = 1
        val homeRecommendationDataModel = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(),
                    productCardModel = ProductCardModel(),
                    position = 2
                )
            ),
            isHasNextPage = true
        )

        getHomeRecommendationCardUseCase.givenDataReturnMatch(homeRecommendationDataModel, productPage)

        // assert HomeRecommendationCardState is equal LoadingState
        assertCollectingRecommendationCardState {
            assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendationCard("", "", sourceType = "")

        assertCollectingRecommendationCardStateWithJob { it, job ->
            val actualResult = (it.first() as HomeRecommendationCardState.Success)
            assertTrue(it.first() is HomeRecommendationCardState.Success)
            assertEquals(
                homeRecommendationDataModel.homeRecommendations,
                actualResult.data.homeRecommendations
            )
            assertEquals(homeRecommendationDataModel.isHasNextPage, actualResult.data.isHasNextPage)

            val productPage = 2

            val exception = MessageErrorException("something went wrong 404")

            getHomeRecommendationCardUseCase.givenThrows(exception, productPage)

            homeRecommendationViewModel.fetchNextHomeRecommendationCard(
                "",
                productPage,
                sourceType = "",
                locationParam = ""
            )

            assertTrue(it[1] is HomeRecommendationCardState.LoadingMore)

            val actualNextResult = (it[2] as HomeRecommendationCardState.FailNextPage)

            assertEquals(
                exception.localizedMessage,
                actualNextResult.throwable.localizedMessage
            )
            assertEquals(homeRecommendationDataModel.homeRecommendations, actualNextResult.data.homeRecommendations)

            job.cancel()
        }
    }

    private fun assertCollectingRecommendationCardState(block: (List<HomeRecommendationCardState<HomeRecommendationDataModel>>) -> Unit) {
        val scope = CoroutineScope(rule.dispatchers.coroutineDispatcher)
        val testCollectorList =
            mutableListOf<HomeRecommendationCardState<HomeRecommendationDataModel>>()
        val uiStateCollectorJob = scope.launch {
            homeRecommendationViewModel.homeRecommendationCardState.toList(testCollectorList)
        }
        block.invoke(testCollectorList)
        uiStateCollectorJob.cancel()
    }

    private fun assertCollectingRecommendationCardStateWithJob(
        block: (List<HomeRecommendationCardState<HomeRecommendationDataModel>>, Job) -> Unit
    ) {
        val scope = CoroutineScope(rule.dispatchers.coroutineDispatcher)
        val testCollectorList =
            mutableListOf<HomeRecommendationCardState<HomeRecommendationDataModel>>()
        val cardStateCollectorJob = scope.launch {
            homeRecommendationViewModel.homeRecommendationCardState.toList(testCollectorList)
        }
        block.invoke(testCollectorList, cardStateCollectorJob)
    }
}
