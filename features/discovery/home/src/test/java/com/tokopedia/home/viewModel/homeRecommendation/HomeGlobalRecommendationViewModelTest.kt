package com.tokopedia.home.viewModel.homeRecommendation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.data.mapper.HomeRecommendationCardMapper
import com.tokopedia.home.beranda.domain.interactor.GetHomeGlobalRecommendationUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.GetGlobalHomeRecommendationCardUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeGlobalRecommendationDataModel
import com.tokopedia.home.beranda.presentation.view.helper.HomeRecommendationController
import com.tokopedia.home.beranda.presentation.view.uimodel.HomeRecommendationCardState
import com.tokopedia.home.beranda.presentation.viewModel.HomeGlobalRecommendationViewModel
import com.tokopedia.home.ext.observeOnce
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.ForYouRecommendationVisitable
import com.tokopedia.recommendation_widget_common.infinite.foryou.recom.RecommendationCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.EmptyStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.ErrorStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.LoadMoreStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.ShimmeringStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.topads.model.BannerOldTopAdsModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.topads.model.HeadlineTopAdsModel
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
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkObject
import io.mockk.verifyOrder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

class HomeGlobalRecommendationViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val rule = UnconfinedTestRule()

    private val getHomeRecommendationUseCase = mockk<GetHomeGlobalRecommendationUseCase>(relaxed = true)
    private val getHomeRecommendationCardUseCase =
        mockk<GetGlobalHomeRecommendationCardUseCase>(relaxed = true)
    private val topAdsImageViewUseCase = mockk<TopAdsImageViewUseCase>(relaxed = true)
    private val topAdsUrlHitter = mockk<TopAdsUrlHitter>(relaxed = true)
    private val getTopAdsHeadlineUseCase = mockk<GetTopAdsHeadlineUseCase>(relaxed = true)
    private val topAdsAddressHelper = mockk<TopAdsAddressHelper>(relaxed = true)
    private val userSessionInterface = mockk<UserSessionInterface>(relaxed = true)

    private val homeRecommendationViewModel = HomeGlobalRecommendationViewModel(
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
        val observerHomeRecommendation: Observer<HomeGlobalRecommendationDataModel> =
            mockk(relaxed = true)
        val model = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(),
                    productCardModel = ProductCardModel(),
                    position = 1
                )
            ),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(model)

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is ShimmeringStateModel
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is RecommendationCardModel
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
        val observerHomeRecommendation: Observer<HomeGlobalRecommendationDataModel> =
            mockk(relaxed = true)
        val model = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(model)

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )
        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is ShimmeringStateModel
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is EmptyStateModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Error Data Home Recommendation Initial Page`() {
        val observerHomeRecommendation: Observer<HomeGlobalRecommendationDataModel> =
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
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is ShimmeringStateModel
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is ErrorStateModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & try load more`() {
        val observerHomeRecommendation: Observer<HomeGlobalRecommendationDataModel> =
            mockk(relaxed = true)
        val model = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(),
                    productCardModel = ProductCardModel(),
                    position = 1
                )
            ),
            isHasNextPage = true
        )
        val model2 = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(),
                    productCardModel = ProductCardModel(),
                    position = 1
                )
            ),
            isHasNextPage = true
        )

        getHomeRecommendationUseCase.givenDataReturn(
            model,
            model2
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
            existingRecommendationData = model.homeRecommendations.plus(model2.homeRecommendations)
        )

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is ShimmeringStateModel
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is RecommendationCardModel &&
                        it.homeRecommendations.size == model.homeRecommendations.size
                }
            )
            // check on end data is home recommendation loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.last() is LoadMoreStateModel &&
                        it.homeRecommendations.size == model.homeRecommendations.size + 1
                }
            )
            // check on end data is home recommendation data after load more
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.last() is RecommendationCardModel &&
                        it.homeRecommendations.size == model.homeRecommendations.size + model2.homeRecommendations.size
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & error load more`() {
        val observerHomeRecommendation: Observer<HomeGlobalRecommendationDataModel> =
            mockk(relaxed = true)
        val model = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(),
                    productCardModel = ProductCardModel(),
                    position = 1
                )
            ),
            isHasNextPage = true
        )

        getHomeRecommendationUseCase.givenDataReturn(
            model,
            TimeoutException()
        )

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

        homeRecommendationViewModel.fetchNextHomeRecommendation("", 1, 0, 2, sourceType = "", existingRecommendationData = model.homeRecommendations)

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is ShimmeringStateModel
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is RecommendationCardModel &&
                        it.homeRecommendations.size == model.homeRecommendations.size
                }
            )
            // check on end data is home recommendation loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.last() is LoadMoreStateModel &&
                        it.homeRecommendations.size == model.homeRecommendations.size + 1
                }
            )
            // check on end data is home recommendation data after load more
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.last() is RecommendationCardModel &&
                        it.homeRecommendations.size == model.homeRecommendations.size
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & Update Wishlist With Correct Object`() {
        val observerHomeRecommendation: Observer<HomeGlobalRecommendationDataModel> =
            mockk(relaxed = true)
        val model = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
                        id = "12",
                        isTopAds = false
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                )
            ),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(model)

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
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is ShimmeringStateModel
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is RecommendationCardModel
                }
            )
            // check on data is home recommendation item is Wishlisted
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is RecommendationCardModel &&
                        (it.homeRecommendations.first() as RecommendationCardModel).recommendationProductItem.isWishlist
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & Update Wishlist With Incorrect position`() {
        val observerHomeRecommendation: Observer<HomeGlobalRecommendationDataModel> =
            mockk(relaxed = true)
        val model = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
                        id = "12",
                        isTopAds = false
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                )
            ),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(model)

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
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is ShimmeringStateModel
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is RecommendationCardModel
                }
            )
            // check on data is home recommendation item is Wishlisted
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is RecommendationCardModel &&
                        (it.homeRecommendations.first() as RecommendationCardModel).recommendationProductItem.isWishlist
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & Update Wishlist With Incorrect Product ID`() {
        val observerHomeRecommendation: Observer<HomeGlobalRecommendationDataModel> =
            mockk(relaxed = true)
        val model = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
                        id = "12",
                        isTopAds = false
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                )
            ),
            isHasNextPage = false
        )
        getHomeRecommendationUseCase.givenDataReturn(model)

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
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is ShimmeringStateModel
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is RecommendationCardModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & Send Impression`() {
        val observerHomeRecommendation: Observer<HomeGlobalRecommendationDataModel> =
            mockk(relaxed = true)
        val item = RecommendationCardModel(
            recommendationProductItem = RecommendationCardModel.ProductItem(
                id = "12",
                isWishlist = false,
                trackerImageUrl = "coba",
                name = "Nama Produk",
                imageUrl = "https://images.tokopedia.com/blablabla.png"
            ),
            productCardModel = ProductCardModel(),
            position = 1
        )
        val model = HomeGlobalRecommendationDataModel(
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

        getHomeRecommendationUseCase.givenDataReturn(model)

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
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is ShimmeringStateModel
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is RecommendationCardModel
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
        val observerHomeRecommendation: Observer<HomeGlobalRecommendationDataModel> =
            mockk(relaxed = true)
        val item = RecommendationCardModel(
            recommendationProductItem = RecommendationCardModel.ProductItem(
                id = "12",
                isWishlist = false,
                trackerImageUrl = "coba",
                clickUrl = "clickUrl"
            ),
            productCardModel = ProductCardModel(),
            position = 1,
            tabName = ""
        )
        val model = HomeGlobalRecommendationDataModel(
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
        getHomeRecommendationUseCase.givenDataReturn(model)

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
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is ShimmeringStateModel
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is RecommendationCardModel
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
        val observerHomeRecommendation: Observer<HomeGlobalRecommendationDataModel> =
            mockk(relaxed = true)
        val model = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                BannerOldTopAdsModel(position = 1, bannerType = HomeRecommendationCardMapper.TYPE_BANNER)
            ),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(model)

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
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is ShimmeringStateModel
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is RecommendationCardModel &&
                        it.homeRecommendations[1] is BannerOldTopAdsModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page and load topads banner is empty`() {
        val observerHomeRecommendation: Observer<HomeGlobalRecommendationDataModel> =
            mockk(relaxed = true)
        val model = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                BannerOldTopAdsModel(position = 1, bannerType = HomeRecommendationCardMapper.TYPE_BANNER)
            ),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(model)

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
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is ShimmeringStateModel
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is RecommendationCardModel &&
                        it.homeRecommendations.size == 1
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page and load topads banner is error`() {
        val observerHomeRecommendation: Observer<HomeGlobalRecommendationDataModel> =
            mockk(relaxed = true)
        val model = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                BannerOldTopAdsModel(position = 1, bannerType = HomeRecommendationCardMapper.TYPE_BANNER)
            ),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(model)

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
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is ShimmeringStateModel
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is RecommendationCardModel &&
                        it.homeRecommendations.size == 1
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & try load more and get success topads banner`() {
        val observerHomeRecommendation: Observer<HomeGlobalRecommendationDataModel> =
            mockk(relaxed = true)
        val model = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                BannerOldTopAdsModel(position = 1, bannerType = HomeRecommendationCardMapper.TYPE_BANNER)
            ),
            isHasNextPage = true
        )
        val model2 = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 2
                ),
                BannerOldTopAdsModel(position = 1, bannerType = HomeRecommendationCardMapper.TYPE_BANNER)
            ),
            isHasNextPage = true
        )

        getHomeRecommendationUseCase.givenDataReturn(
            model,
            model2
        )

        topAdsImageViewUseCase.givenDataReturn(arrayListOf(TopAdsImageViewModel()))

        getTopAdsHeadlineUseCase.givenDataReturn(TopAdsHeadlineResponse())

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

        homeRecommendationViewModel.fetchNextHomeRecommendation("", 1, 0, 2, sourceType = "", existingRecommendationData = model.homeRecommendations)

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is ShimmeringStateModel
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is RecommendationCardModel &&
                        it.homeRecommendations.size == model.homeRecommendations.size &&
                        it.homeRecommendations[1] is BannerOldTopAdsModel
                }
            )
            // check on end data is home recommendation loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.last() is LoadMoreStateModel &&
                        it.homeRecommendations.size == model.homeRecommendations.size + 1
                }
            )
            // check on end data is home recommendation data after load more
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() &&
                        it.homeRecommendations.size == model.homeRecommendations.size + model2.homeRecommendations.size &&
                        it.homeRecommendations[it.homeRecommendations.size - 1] is BannerOldTopAdsModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & try load more and get success topads banner and empty for loadmore topads banner`() {
        val observerHomeRecommendation: Observer<HomeGlobalRecommendationDataModel> =
            mockk(relaxed = true)
        val model = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 2
                ),
                BannerOldTopAdsModel(position = 2, bannerType = HomeRecommendationCardMapper.TYPE_BANNER)
            ),
            isHasNextPage = true
        )
        val model2 = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 2
                ),
                BannerOldTopAdsModel(position = 2, bannerType = HomeRecommendationCardMapper.TYPE_BANNER)
            ),
            isHasNextPage = true
        )

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        getHomeRecommendationUseCase.givenDataReturn(model, model2)

        topAdsImageViewUseCase.givenDataReturn(arrayListOf(TopAdsImageViewModel()), arrayListOf())

        getTopAdsHeadlineUseCase.givenDataReturn(TopAdsHeadlineResponse())

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

        homeRecommendationViewModel.fetchNextHomeRecommendation("", 1, 0, 2, sourceType = "", existingRecommendationData = model.homeRecommendations)

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is ShimmeringStateModel
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is RecommendationCardModel &&
                        it.homeRecommendations.size == model.homeRecommendations.size &&
                        it.homeRecommendations[it.homeRecommendations.size - 1] is BannerOldTopAdsModel
                }
            )
            // check on end data is home recommendation loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.last() is LoadMoreStateModel &&
                        it.homeRecommendations.size == model.homeRecommendations.size + 1
                }
            )
            // check on end data is home recommendation data after load more
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() &&
                        it.homeRecommendations[it.homeRecommendations.size - 1] is BannerOldTopAdsModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page & try load more and get success topads banner and error for loadmore topads banner`() {
        val observerHomeRecommendation: Observer<HomeGlobalRecommendationDataModel> =
            mockk(relaxed = true)
        val model = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 2
                ),
                BannerOldTopAdsModel(position = 2, bannerType = HomeRecommendationCardMapper.TYPE_BANNER)
            ),
            isHasNextPage = true
        )
        val model2 = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                BannerOldTopAdsModel(position = 2, bannerType = HomeRecommendationCardMapper.TYPE_BANNER)
            ),
            isHasNextPage = true
        )

        getHomeRecommendationUseCase.givenDataReturn(model, model2)

        getTopAdsHeadlineUseCase.givenDataReturn(TopAdsHeadlineResponse())

        topAdsImageViewUseCase.givenDataReturnAndThenThrows(arrayListOf(TopAdsImageViewModel()))

        every { HomeRecommendationController.isUsingRecommendationCard() } returns false

        homeRecommendationViewModel.homeRecommendationLiveData.observeForever(
            observerHomeRecommendation
        )

        homeRecommendationViewModel.fetchHomeRecommendation("", 1, 0, sourceType = "")

        homeRecommendationViewModel.fetchNextHomeRecommendation("", 1, 0, 2, sourceType = "", existingRecommendationData = model.homeRecommendations)

        verifyOrder {
            // check on loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is ShimmeringStateModel
                }
            )
            // check on first data is home recommendation item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is RecommendationCardModel &&
                        it.homeRecommendations.size == model.homeRecommendations.size &&
                        it.homeRecommendations[it.homeRecommendations.size - 1] is BannerOldTopAdsModel
                }
            )
            // check on end data is home recommendation loading
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.last() is LoadMoreStateModel &&
                        it.homeRecommendations.size == model.homeRecommendations.size + 1
                }
            )
            // check on end data is home recommendation data after load more
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() &&
                        it.homeRecommendations[it.homeRecommendations.size - 1] is BannerOldTopAdsModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page and load shopAds on empty Banner`() {
        val observerHomeRecommendation: Observer<HomeGlobalRecommendationDataModel> =
            mockk(relaxed = true)
        val model = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
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

        getHomeRecommendationUseCase.givenDataReturn(model)

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
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is ShimmeringStateModel
                }
            )
            // check on first data is headline ads item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HeadlineTopAdsModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page  on empty headline response`() {
        val observerHomeRecommendation: Observer<HomeGlobalRecommendationDataModel> =
            mockk(relaxed = true)
        val model = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
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

        getHomeRecommendationUseCase.givenDataReturn(model)

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
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is ShimmeringStateModel
                }
            )
            // check on first data is headline ads item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is RecommendationCardModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page and load shopAds on data have banner but response is empty`() {
        val observerHomeRecommendation: Observer<HomeGlobalRecommendationDataModel> =
            mockk(relaxed = true)
        val model = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                BannerOldTopAdsModel(position = 1, bannerType = HomeRecommendationCardMapper.TYPE_BANNER)
            ),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(model)

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
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is ShimmeringStateModel
                }
            )
            // check on first data is headline ads item
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HeadlineTopAdsModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page and load shopAds with topads Banner`() {
        val observerHomeRecommendation: Observer<HomeGlobalRecommendationDataModel> =
            mockk(relaxed = true)
        val model = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                BannerOldTopAdsModel(position = 1, bannerType = HomeRecommendationCardMapper.TYPE_BANNER)
            ),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(model)

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
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is ShimmeringStateModel
                }
            )

            // check on first data is home headline ads item and topAds banner at next index
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HeadlineTopAdsModel &&
                        it.homeRecommendations[2] is BannerOldTopAdsModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page and load shopAds with topads Banner at position 2`() {
        val observerHomeRecommendation: Observer<HomeGlobalRecommendationDataModel> =
            mockk(relaxed = true)
        val model = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                BannerOldTopAdsModel(position = 1, bannerType = HomeRecommendationCardMapper.TYPE_BANNER),
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
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

        getHomeRecommendationUseCase.givenDataReturn(model)

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
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is ShimmeringStateModel
                }
            )

//             check on first data is home headline ads item and topAds banner at next index
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is RecommendationCardModel && it.homeRecommendations[1] is BannerOldTopAdsModel &&
                        it.homeRecommendations[2] is HeadlineTopAdsModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page and load shopAds without topads Banner if postion is higer then main list`() {
        val observerHomeRecommendation: Observer<HomeGlobalRecommendationDataModel> =
            mockk(relaxed = true)
        val model = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                BannerOldTopAdsModel(position = 4, bannerType = HomeRecommendationCardMapper.TYPE_BANNER)
            ),
            isHasNextPage = false
        )

        getHomeRecommendationUseCase.givenDataReturn(model)

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
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is ShimmeringStateModel
                }
            )

            // check on first data is home headline ads item and topAds banner at next index
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is HeadlineTopAdsModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page and load topads Banner when headlines ads is empty`() {
        val observerHomeRecommendation: Observer<HomeGlobalRecommendationDataModel> =
            mockk(relaxed = true)
        val model = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                BannerOldTopAdsModel(position = 2, bannerType = HomeRecommendationCardMapper.TYPE_BANNER),
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
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

        getHomeRecommendationUseCase.givenDataReturn(model)

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
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is ShimmeringStateModel
                }
            )

            // check on first data is home recommendation item and topAds banner at index
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is RecommendationCardModel &&
                        it.homeRecommendations[2] is BannerOldTopAdsModel
                }
            )
        }
        confirmVerified(observerHomeRecommendation)
    }

    @Test
    fun `Get Success Data Home Recommendation Initial Page and load topads Banner when headlines ads is empty and banner postion is higer than list`() {
        val observerHomeRecommendation: Observer<HomeGlobalRecommendationDataModel> =
            mockk(relaxed = true)
        val model = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
                        id = "12",
                        isWishlist = false,
                        trackerImageUrl = "coba",
                        clickUrl = "clickUrl"
                    ),
                    productCardModel = ProductCardModel(),
                    position = 2
                ),
                BannerOldTopAdsModel(position = 7, bannerType = HomeRecommendationCardMapper.TYPE_BANNER),
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(
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

        getHomeRecommendationUseCase.givenDataReturn(model)

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
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is ShimmeringStateModel
                }
            )

            // check on first data is home recommendation item and topAds banner at index
            observerHomeRecommendation.onChanged(
                match {
                    it.homeRecommendations.isNotEmpty() && it.homeRecommendations.first() is RecommendationCardModel &&
                        it.homeRecommendations[2] is BannerOldTopAdsModel
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
        val model = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(),
                    productCardModel = ProductCardModel(),
                    position = 2
                )
            ),
            isHasNextPage = true
        )

        getHomeRecommendationCardUseCase.givenDataReturn(model, productPage)

        every { HomeRecommendationController.isUsingRecommendationCard() } returns true

        // assert HomeRecommendationCardState is equal LoadingState
        assertCollectingRecommendationCardState {
            Assert.assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendation("", 0, 1, "", tabIndex = 1, sourceType = "")

        assertCollectingRecommendationCardState {
            val actualResult = (it.first() as HomeRecommendationCardState.Success)
            Assert.assertTrue(it.first() is HomeRecommendationCardState.Success)
            Assert.assertEquals(
                model.homeRecommendations,
                actualResult.data.homeRecommendations
            )
            Assert.assertEquals(
                model.isHasNextPage,
                actualResult.data.isHasNextPage
            )

            homeRecommendationViewModel.updateWhistlist("12", 0, true)

            val actualResultWishlist = (it.first() as HomeRecommendationCardState.Success).data.homeRecommendations.filterIsInstance<RecommendationCardModel>().first()
            Assert.assertTrue(actualResultWishlist.recommendationProductItem.id.isBlank())
        }
    }

    @Test
    fun `Get Success Data Home Recommendation Fetch Initial Page And Wishlist Product with correct productId, should return Success State`() {
        val productPage = 1
        val model = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(id = "14"),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(),
                    productCardModel = ProductCardModel(),
                    position = 2
                )
            ),
            isHasNextPage = true
        )

        getHomeRecommendationCardUseCase.givenDataReturn(model, productPage)

        every { HomeRecommendationController.isUsingRecommendationCard() } returns true

        // assert HomeRecommendationCardState is equal LoadingState
        assertCollectingRecommendationCardState {
            Assert.assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendation("", 0, 1, "", tabIndex = 1, sourceType = "")

        assertCollectingRecommendationCardState {
            val actualResult = (it.first() as HomeRecommendationCardState.Success)
            Assert.assertTrue(it.first() is HomeRecommendationCardState.Success)
            Assert.assertEquals(
                model.homeRecommendations,
                actualResult.data.homeRecommendations
            )
            Assert.assertEquals(
                model.isHasNextPage,
                actualResult.data.isHasNextPage
            )

            homeRecommendationViewModel.updateWhistlist("14", 0, true)

            val actualResultWishlist = (it.first() as HomeRecommendationCardState.Success).data.homeRecommendations.filterIsInstance<RecommendationCardModel>().first()
            val expectedResultWishlist = model.homeRecommendations.filterIsInstance<RecommendationCardModel>().first()

            Assert.assertEquals(
                expectedResultWishlist.recommendationProductItem.id,
                actualResultWishlist.recommendationProductItem.id
            )
        }
    }

    @Test
    fun `Get Success Data Home Recommendation Fetch Initial Page, should return Empty State`() {
        val productPage = 1
        val model = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(),
            isHasNextPage = false
        )

        every { HomeRecommendationController.isUsingRecommendationCard() } returns true

        getHomeRecommendationCardUseCase.givenDataReturn(model, productPage)

        // assert HomeRecommendationCardState is equal LoadingState
        assertCollectingRecommendationCardState {
            Assert.assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendation("", 0, 1, "", 1, "")

        assertCollectingRecommendationCardState {
            val actualResult = (it.first() as HomeRecommendationCardState.EmptyData)
            Assert.assertTrue(it.first() is HomeRecommendationCardState.EmptyData)
            Assert.assertEquals(
                listOf(homeRecommendationViewModel.emptyModel),
                actualResult.data.homeRecommendations
            )
            Assert.assertEquals(
                model.isHasNextPage,
                actualResult.data.isHasNextPage
            )
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
            Assert.assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendation("", 0, 1, "", tabIndex = 1, sourceType = "")

        assertCollectingRecommendationCardState {
            val actualResult = (it.first() as HomeRecommendationCardState.Fail)
            Assert.assertTrue(it.first() is HomeRecommendationCardState.Fail)
            Assert.assertEquals(exception.localizedMessage, actualResult.throwable.localizedMessage)
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
            Assert.assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendation("", 0, 1, "", tabIndex = 1, sourceType = "")

        assertCollectingRecommendationCardState {
            val actualResult = (it.first() as HomeRecommendationCardState.Fail)
            Assert.assertTrue(it.first() is HomeRecommendationCardState.Fail)
            Assert.assertEquals(exception.localizedMessage, actualResult.throwable.localizedMessage)

            homeRecommendationViewModel.updateWhistlist("14", 0, true)
            Assert.assertTrue(it.first() is HomeRecommendationCardState.Fail)
        }
    }

    @Test
    fun `Get Success Data Home Recommendation Fetch Next Initial Page, should return SuccessNextPage State`() {
        val productPage = 1
        val model = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(),
                    productCardModel = ProductCardModel(),
                    position = 2
                )
            ),
            isHasNextPage = true
        )

        every { HomeRecommendationController.isUsingRecommendationCard() } returns true

        getHomeRecommendationCardUseCase.givenDataReturnMatch(model, productPage)

        // assert HomeRecommendationCardState is equal LoadingState
        assertCollectingRecommendationCardState {
            Assert.assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendation("", 0, 1, "", tabIndex = 1, sourceType = "")

        assertCollectingRecommendationCardState {
            val actualResult = (it.first() as HomeRecommendationCardState.Success)
            Assert.assertTrue(it.first() is HomeRecommendationCardState.Success)
            Assert.assertEquals(
                model.homeRecommendations,
                actualResult.data.homeRecommendations
            )
            Assert.assertEquals(
                model.isHasNextPage,
                actualResult.data.isHasNextPage
            )

            val givenProductPage = 2
            val homeRecommendationNextDataModel = HomeGlobalRecommendationDataModel(
                listOf<ForYouRecommendationVisitable>(
                    RecommendationCardModel(
                        productCardModel = ProductCardModel(),
                        recommendationProductItem = RecommendationCardModel.ProductItem()
                    ),
                    RecommendationCardModel(
                        productCardModel = ProductCardModel(),
                        recommendationProductItem = RecommendationCardModel.ProductItem()
                    )
                ).toList(),
                isHasNextPage = true
            )

            getHomeRecommendationCardUseCase.givenDataReturnMatch(homeRecommendationNextDataModel, givenProductPage)

            homeRecommendationViewModel.fetchNextHomeRecommendation("", 0, 1, givenProductPage, locationParam = "", sourceType = "", existingRecommendationData = model.homeRecommendations)

            val actualNextResult = (it[1] as HomeRecommendationCardState.Success)
            val expectedNextResult = homeRecommendationNextDataModel.homeRecommendations.toMutableList().apply {
                addAll(model.homeRecommendations)
            }

            Assert.assertEquals(
                expectedNextResult,
                actualNextResult.data.homeRecommendations
            )
            Assert.assertEquals(
                homeRecommendationNextDataModel.isHasNextPage,
                actualNextResult.data.isHasNextPage
            )
        }
    }

    @Test
    fun `Get Failed Data Home Recommendation Fetch Next Initial Page, should return FailNextPage State`() {
        val productPage = 1
        val model = HomeGlobalRecommendationDataModel(
            homeRecommendations = listOf(
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(),
                    productCardModel = ProductCardModel(),
                    position = 1
                ),
                RecommendationCardModel(
                    recommendationProductItem = RecommendationCardModel.ProductItem(),
                    productCardModel = ProductCardModel(),
                    position = 2
                ),
                homeRecommendationViewModel.buttonRetryUiModel
            ),
            isHasNextPage = true
        )

        getHomeRecommendationCardUseCase.givenDataReturnMatch(model, productPage)

        every { HomeRecommendationController.isUsingRecommendationCard() } returns true

        // assert HomeRecommendationCardState is equal LoadingState
        assertCollectingRecommendationCardState {
            Assert.assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendation("", 0, 1, "", tabIndex = 1, sourceType = "")

        assertCollectingRecommendationCardState {
            val actualResult = (it.first() as HomeRecommendationCardState.Success)
            Assert.assertTrue(it.first() is HomeRecommendationCardState.Success)
            Assert.assertEquals(
                model.homeRecommendations,
                actualResult.data.homeRecommendations
            )
            Assert.assertEquals(
                model.isHasNextPage,
                actualResult.data.isHasNextPage
            )

            val givenProductPage = 2

            val exception = MessageErrorException("something went wrong 404")

            getHomeRecommendationCardUseCase.givenThrows(exception, givenProductPage)

            homeRecommendationViewModel.fetchNextHomeRecommendation("", 0, 1, givenProductPage, locationParam = "", sourceType = "", existingRecommendationData = model.homeRecommendations)

            val actualNextResult = (it[1] as HomeRecommendationCardState.FailNextPage)

            Assert.assertEquals(
                exception.localizedMessage,
                actualNextResult.throwable.localizedMessage
            )
            Assert.assertEquals(
                model.homeRecommendations,
                actualNextResult.data.homeRecommendations
            )
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
            Assert.assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendation("", 0, 1, "", tabIndex = 1, sourceType = "")

        assertCollectingRecommendationCardState {
            Assert.assertTrue(it.first() is HomeRecommendationCardState.Fail)

            homeRecommendationViewModel.fetchNextHomeRecommendation("", 0, 1, productPage, locationParam = "", sourceType = "", existingRecommendationData = (it.first() as HomeRecommendationCardState.Fail).data.homeRecommendations)
            Assert.assertTrue(it.first() is HomeRecommendationCardState.Fail)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun assertCollectingRecommendationCardState(block: (List<HomeRecommendationCardState<HomeGlobalRecommendationDataModel>>) -> Unit) {
        val scope = CoroutineScope(rule.dispatchers.coroutineDispatcher)
        val testCollectorList =
            mutableListOf<HomeRecommendationCardState<HomeGlobalRecommendationDataModel>>()
        val uiStateCollectorJob = scope.launch {
            homeRecommendationViewModel.homeRecommendationCardState.toList(testCollectorList)
        }
        block.invoke(testCollectorList)
        uiStateCollectorJob.cancel()
    }
}
