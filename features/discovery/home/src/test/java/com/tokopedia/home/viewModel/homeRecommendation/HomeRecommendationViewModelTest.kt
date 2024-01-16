package com.tokopedia.home.viewModel.homeRecommendation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.data.mapper.HomeRecommendationCardMapper
import com.tokopedia.home.beranda.domain.interactor.GetHomeRecommendationUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.GetHomeRecommendationCardUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.*
import com.tokopedia.home.beranda.presentation.view.helper.HomeRecommendationController
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
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

    @Before
    fun setup() {
        mockkObject(HomeRecommendationController)
    }

    @After
    fun finish() {
        unmockkObject(HomeRecommendationController)
    }

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

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

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

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )
        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

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

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )
        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

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

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

        homeRecommendationViewModel.fetchNextHomeRecommendation(
            "",
            1,
            0,
            2,
            sourceType = "",
            existingRecommendationData = homeRecommendationDataModel.homeRecommendations.plus(homeRecommendationDataModel2.homeRecommendations)
        )

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

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

        homeRecommendationViewModel.fetchNextHomeRecommendation("", 1, 0, 2, sourceType = "", existingRecommendationData = homeRecommendationDataModel.homeRecommendations)

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

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

        homeRecommendationViewModel.updateWhistlist("12", 0, true)

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

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

        homeRecommendationViewModel.updateWhistlist("12", 100, true)

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

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

        homeRecommendationViewModel.updateWhistlist("1332", 0, true)

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

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

        homeRecommendationViewModel.updateWhistlist("1332", 0, true)

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

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        // home view model
        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        // viewModel load first page data
        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

        // Try click update wishlist
        homeRecommendationViewModel.updateWhistlist("1332", 0, true)

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
                HomeRecommendationBannerTopAdsOldDataModel(position = 1, bannerType = HomeRecommendationCardMapper.TYPE_BANNER)
            ),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        topAdsImageViewUseCase.givenDataReturn(arrayListOf(TopAdsImageViewModel()))

        getTopAdsHeadlineUseCase.givenDataReturn(TopAdsHeadlineResponse())

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

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
                        it.homeRecommendations[1] is HomeRecommendationBannerTopAdsOldDataModel
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
                HomeRecommendationBannerTopAdsOldDataModel(position = 1, bannerType = HomeRecommendationCardMapper.TYPE_BANNER)
            ),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        topAdsImageViewUseCase.givenDataReturn(arrayListOf())

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

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
                HomeRecommendationBannerTopAdsOldDataModel(position = 1, bannerType = HomeRecommendationCardMapper.TYPE_BANNER)
            ),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(homeRecommendationDataModel)

        topAdsImageViewUseCase.givenThrows()

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

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
                HomeRecommendationBannerTopAdsOldDataModel(position = 1, bannerType = HomeRecommendationCardMapper.TYPE_BANNER)
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
                HomeRecommendationBannerTopAdsOldDataModel(position = 1, bannerType = HomeRecommendationCardMapper.TYPE_BANNER)
            ),
            isHasNextPage = true
        )

        getHomeRecommendationUseCase.givenDataReturn(
            homeRecommendationDataModel,
            homeRecommendationDataModel2
        )

        topAdsImageViewUseCase.givenDataReturn(arrayListOf(TopAdsImageViewModel()))

        getTopAdsHeadlineUseCase.givenDataReturn(TopAdsHeadlineResponse())

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

        homeRecommendationViewModel.fetchNextHomeRecommendation("", 1, 0, 2, sourceType = "", existingRecommendationData = homeRecommendationDataModel.homeRecommendations)

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
                        it.homeRecommendations[1] is HomeRecommendationBannerTopAdsOldDataModel
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
                        it.homeRecommendations[it.homeRecommendations.size - 1] is HomeRecommendationBannerTopAdsOldDataModel
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
                HomeRecommendationBannerTopAdsOldDataModel(position = 2, bannerType = HomeRecommendationCardMapper.TYPE_BANNER)
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
                HomeRecommendationBannerTopAdsOldDataModel(position = 2, bannerType = HomeRecommendationCardMapper.TYPE_BANNER)
            ),
            isHasNextPage = true
        )

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        getHomeRecommendationUseCase.givenDataReturn(
            homeRecommendationDataModel,
            homeRecommendationDataModel2
        )

        topAdsImageViewUseCase.givenDataReturn(arrayListOf(TopAdsImageViewModel()), arrayListOf())

        getTopAdsHeadlineUseCase.givenDataReturn(TopAdsHeadlineResponse())

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

        homeRecommendationViewModel.fetchNextHomeRecommendation("", 1, 0, 2, sourceType = "", existingRecommendationData = homeRecommendationDataModel.homeRecommendations)

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
                        it.homeRecommendations[it.homeRecommendations.size - 1] is HomeRecommendationBannerTopAdsOldDataModel
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
                        it.homeRecommendations[it.homeRecommendations.size - 1] is HomeRecommendationBannerTopAdsOldDataModel
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
                HomeRecommendationBannerTopAdsOldDataModel(position = 2, bannerType = HomeRecommendationCardMapper.TYPE_BANNER)
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
                HomeRecommendationBannerTopAdsOldDataModel(position = 2, bannerType = HomeRecommendationCardMapper.TYPE_BANNER)
            ),
            isHasNextPage = true
        )

        getHomeRecommendationUseCase.givenDataReturn(
            homeRecommendationDataModel,
            homeRecommendationDataModel2
        )

        getTopAdsHeadlineUseCase.givenDataReturn(TopAdsHeadlineResponse())

        topAdsImageViewUseCase.givenDataReturnAndThenThrows(arrayListOf(TopAdsImageViewModel()))

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

        homeRecommendationViewModel.fetchNextHomeRecommendation("", 1, 0, 2, sourceType = "", existingRecommendationData = homeRecommendationDataModel.homeRecommendations)

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
                        it.homeRecommendations[it.homeRecommendations.size - 1] is HomeRecommendationBannerTopAdsOldDataModel
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
                        it.homeRecommendations[it.homeRecommendations.size - 1] is HomeRecommendationBannerTopAdsOldDataModel
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

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

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

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

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
                HomeRecommendationBannerTopAdsOldDataModel(position = 1, bannerType = HomeRecommendationCardMapper.TYPE_BANNER)
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

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

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
                HomeRecommendationBannerTopAdsOldDataModel(position = 1, bannerType = HomeRecommendationCardMapper.TYPE_BANNER)
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

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

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
                        it.homeRecommendations[2] is HomeRecommendationBannerTopAdsOldDataModel
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
                HomeRecommendationBannerTopAdsOldDataModel(position = 1, bannerType = HomeRecommendationCardMapper.TYPE_BANNER),
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

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

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

        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

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
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HomeRecommendationItemDataModel && it.homeRecommendations[1] is HomeRecommendationBannerTopAdsOldDataModel &&
                        it.homeRecommendations[2] is HomeRecommendationHeadlineTopAdsDataModel
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
                HomeRecommendationBannerTopAdsOldDataModel(position = 4, bannerType = HomeRecommendationCardMapper.TYPE_BANNER)
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

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

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
                HomeRecommendationBannerTopAdsOldDataModel(position = 2, bannerType = HomeRecommendationCardMapper.TYPE_BANNER),
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

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, tabIndex = 1, sourceType = "")

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
                        it.homeRecommendations[2] is HomeRecommendationBannerTopAdsOldDataModel
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
                HomeRecommendationBannerTopAdsOldDataModel(position = 7, bannerType = HomeRecommendationCardMapper.TYPE_BANNER),
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

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, tabIndex = 1, sourceType = "")

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
                        it.homeRecommendations[2] is HomeRecommendationBannerTopAdsOldDataModel
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
    fun `Get Success Data Home Recommendation Fetch Initial Page And Wishlist Product with wrong productId, should return Success State`() {
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

        every { HomeRecommendationController.isUsingRecommendationCard() } returns true

        // assert HomeRecommendationCardState is equal LoadingState
        assertCollectingRecommendationCardState {
            assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendation("", 0, 1, "", tabIndex = 1, sourceType = "")

        assertCollectingRecommendationCardState {
            val actualResult = (it.first() as HomeRecommendationCardState.Success)
            assertTrue(it.first() is HomeRecommendationCardState.Success)
            assertEquals(
                homeRecommendationDataModel.homeRecommendations,
                actualResult.data.homeRecommendations
            )
            assertEquals(homeRecommendationDataModel.isHasNextPage, actualResult.data.isHasNextPage)

            homeRecommendationViewModel.updateWhistlist("12", 0, true)

            val actualResultWishlist = (it.first() as HomeRecommendationCardState.Success).data.homeRecommendations.filterIsInstance<HomeRecommendationItemDataModel>().first()
            assertTrue(actualResultWishlist.recommendationProductItem.id.isBlank())
        }
    }

    @Test
    fun `Get Success Data Home Recommendation Fetch Initial Page And Wishlist Product with correct productId, should return Success State`() {
        val productPage = 1
        val homeRecommendationDataModel = HomeRecommendationDataModel(
            homeRecommendations = listOf(
                HomeRecommendationItemDataModel(
                    recommendationProductItem = HomeRecommendationItemDataModel.HomeRecommendationProductItem(id = "14"),
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

        every { HomeRecommendationController.isUsingRecommendationCard() } returns true

        // assert HomeRecommendationCardState is equal LoadingState
        assertCollectingRecommendationCardState {
            assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendation("", 0, 1, "", tabIndex = 1, sourceType = "")

        assertCollectingRecommendationCardState {
            val actualResult = (it.first() as HomeRecommendationCardState.Success)
            assertTrue(it.first() is HomeRecommendationCardState.Success)
            assertEquals(
                homeRecommendationDataModel.homeRecommendations,
                actualResult.data.homeRecommendations
            )
            assertEquals(homeRecommendationDataModel.isHasNextPage, actualResult.data.isHasNextPage)

            homeRecommendationViewModel.updateWhistlist("14", 0, true)

            val actualResultWishlist = (it.first() as HomeRecommendationCardState.Success).data.homeRecommendations.filterIsInstance<HomeRecommendationItemDataModel>().first()
            val expectedResultWishlist = homeRecommendationDataModel.homeRecommendations.filterIsInstance<HomeRecommendationItemDataModel>().first()

            assertEquals(expectedResultWishlist.recommendationProductItem.id, actualResultWishlist.recommendationProductItem.id)
        }
    }

    @Test
    fun `Get Success Data Home Recommendation Fetch Initial Page, should return Empty State`() {
        val productPage = 1
        val homeRecommendationDataModel = HomeRecommendationDataModel(
            homeRecommendations = listOf(),
            isHasNextPage = false
        )

        every { HomeRecommendationController.isUsingRecommendationCard() } returns true

        getHomeRecommendationCardUseCase.givenDataReturn(homeRecommendationDataModel, productPage)

        // assert HomeRecommendationCardState is equal LoadingState
        assertCollectingRecommendationCardState {
            assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendation("", 0, 1, "", 1, "")

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

        every { HomeRecommendationController.isUsingRecommendationCard() } returns true

        // assert HomeRecommendationCardState is equal LoadingState
        assertCollectingRecommendationCardState {
            assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendation("", 0, 1, "", tabIndex = 1, sourceType = "")

        assertCollectingRecommendationCardState {
            val actualResult = (it.first() as HomeRecommendationCardState.Fail)
            assertTrue(it.first() is HomeRecommendationCardState.Fail)
            assertEquals(exception.localizedMessage, actualResult.throwable.localizedMessage)
        }
    }

    @Test
    fun `Get Failed Data Home Recommendation Fetch Initial Page and hit wishlist product, should return Fail State`() {
        val productPage = 1
        val exception = MessageErrorException("something went wrong")

        getHomeRecommendationCardUseCase.givenThrows(exception, productPage)

        every { HomeRecommendationController.isUsingRecommendationCard() } returns true

        // assert HomeRecommendationCardState is equal LoadingState
        assertCollectingRecommendationCardState {
            assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendation("", 0, 1, "", tabIndex = 1, sourceType = "")

        assertCollectingRecommendationCardState {
            val actualResult = (it.first() as HomeRecommendationCardState.Fail)
            assertTrue(it.first() is HomeRecommendationCardState.Fail)
            assertEquals(exception.localizedMessage, actualResult.throwable.localizedMessage)

            homeRecommendationViewModel.updateWhistlist("14", 0, true)
            assertTrue(it.first() is HomeRecommendationCardState.Fail)
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

        every { HomeRecommendationController.isUsingRecommendationCard() } returns true

        getHomeRecommendationCardUseCase.givenDataReturnMatch(homeRecommendationDataModel, productPage)

        // assert HomeRecommendationCardState is equal LoadingState
        assertCollectingRecommendationCardState {
            assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendation("", 0, 1, "", tabIndex = 1, sourceType = "")

        assertCollectingRecommendationCardState {
            val actualResult = (it.first() as HomeRecommendationCardState.Success)
            assertTrue(it.first() is HomeRecommendationCardState.Success)
            assertEquals(
                homeRecommendationDataModel.homeRecommendations,
                actualResult.data.homeRecommendations
            )
            assertEquals(homeRecommendationDataModel.isHasNextPage, actualResult.data.isHasNextPage)

            val productPage = 2
            val homeRecommendationNextDataModel = HomeRecommendationDataModel(
                listOf<BaseHomeRecommendationVisitable>(
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

            homeRecommendationViewModel.fetchNextHomeRecommendation("", 0, 1, productPage, locationParam = "", sourceType = "", existingRecommendationData = homeRecommendationDataModel.homeRecommendations)

            val actualNextResult = (it[1] as HomeRecommendationCardState.Success)
            val expectedNextResult = homeRecommendationNextDataModel.homeRecommendations.toMutableList().apply {
                addAll(homeRecommendationDataModel.homeRecommendations)
            }

            assertEquals(
                expectedNextResult,
                actualNextResult.data.homeRecommendations
            )
            assertEquals(homeRecommendationNextDataModel.isHasNextPage, actualNextResult.data.isHasNextPage)
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
                ),
                homeRecommendationViewModel.buttonRetryUiModel
            ),
            isHasNextPage = true
        )

        getHomeRecommendationCardUseCase.givenDataReturnMatch(homeRecommendationDataModel, productPage)

        every { HomeRecommendationController.isUsingRecommendationCard() } returns true

        // assert HomeRecommendationCardState is equal LoadingState
        assertCollectingRecommendationCardState {
            assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendation("", 0, 1, "", tabIndex = 1, sourceType = "")

        assertCollectingRecommendationCardState {
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

            homeRecommendationViewModel.fetchNextHomeRecommendation("", 0, 1, productPage, locationParam = "", sourceType = "", existingRecommendationData = homeRecommendationDataModel.homeRecommendations)

            val actualNextResult = (it[1] as HomeRecommendationCardState.FailNextPage)

            assertEquals(
                exception.localizedMessage,
                actualNextResult.throwable.localizedMessage
            )
            assertEquals(homeRecommendationDataModel.homeRecommendations, actualNextResult.data.homeRecommendations)
        }
    }

    @Test
    fun `When Failed Fetch Home Recommendation and Hit fetchNextHomeRecommendationCard, then no op and the state is Fail`() {
        val productPage = 1
        val exception = MessageErrorException("something went wrong")

        every { HomeRecommendationController.isUsingRecommendationCard() } returns true

        getHomeRecommendationCardUseCase.givenThrows(exception, productPage)

        // assert HomeRecommendationCardState is equal LoadingState
        assertCollectingRecommendationCardState {
            assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendation("", 0, 1, "", tabIndex = 1, sourceType = "")

        assertCollectingRecommendationCardState {
            assertTrue(it.first() is HomeRecommendationCardState.Fail)

            homeRecommendationViewModel.fetchNextHomeRecommendation("", 0, 1, productPage, locationParam = "", sourceType = "", existingRecommendationData = (it.first() as HomeRecommendationCardState.Fail).data.homeRecommendations)
            assertTrue(it.first() is HomeRecommendationCardState.Fail)
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
}
