package com.tokopedia.home.viewModel.homeRecommendation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.domain.interactor.usecase.GetGlobalHomeRecommendationCardUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeGlobalRecommendationDataModel
import com.tokopedia.home.beranda.presentation.view.uimodel.HomeRecommendationCardState
import com.tokopedia.home.beranda.presentation.viewModel.HomeGlobalRecommendationViewModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.byteio.RefreshType
import com.tokopedia.recommendation_widget_common.infinite.foryou.ForYouRecommendationVisitable
import com.tokopedia.recommendation_widget_common.infinite.foryou.recom.RecommendationCardModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class HomeGlobalRecommendationViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val rule = UnconfinedTestRule()

    private val getHomeRecommendationCardUseCase = mockk<GetGlobalHomeRecommendationCardUseCase>(relaxed = true)

    private val homeRecommendationViewModel = HomeGlobalRecommendationViewModel(
        { getHomeRecommendationCardUseCase },
        { CoroutineTestDispatchersProvider }
    )

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

        // assert HomeRecommendationCardState is equal LoadingState
        assertCollectingRecommendationCardState {
            Assert.assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendation("", "", sourceType = "", refreshType = RefreshType.OPEN)

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

        // assert HomeRecommendationCardState is equal LoadingState
        assertCollectingRecommendationCardState {
            Assert.assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendation("", "", tabIndex = 1, sourceType = "", refreshType = RefreshType.OPEN)

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

        getHomeRecommendationCardUseCase.givenDataReturn(model, productPage)

        // assert HomeRecommendationCardState is equal LoadingState
        assertCollectingRecommendationCardState {
            Assert.assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendation("", "", 1, "", refreshType = RefreshType.OPEN)

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

        // assert HomeRecommendationCardState is equal LoadingState
        assertCollectingRecommendationCardState {
            Assert.assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendation("", "", tabIndex = 1, sourceType = "", refreshType = RefreshType.OPEN)

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

        // assert HomeRecommendationCardState is equal LoadingState
        assertCollectingRecommendationCardState {
            Assert.assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendation("", "", sourceType = "", tabIndex = 1, refreshType = RefreshType.OPEN)

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

        getHomeRecommendationCardUseCase.givenDataReturnMatch(model, productPage)

        // assert HomeRecommendationCardState is equal LoadingState
        assertCollectingRecommendationCardState {
            Assert.assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendation("", "", sourceType = "", tabIndex = 1, refreshType = RefreshType.OPEN)

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

            homeRecommendationViewModel.fetchNextHomeRecommendation(1, "", givenProductPage, locationParam = "", sourceType = "", existingRecommendationData = model.homeRecommendations)

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

        // assert HomeRecommendationCardState is equal LoadingState
        assertCollectingRecommendationCardState {
            Assert.assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendation("", "", sourceType = "", tabIndex = 1, refreshType = RefreshType.OPEN)

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

            homeRecommendationViewModel.fetchNextHomeRecommendation(1, "", givenProductPage, locationParam = "", sourceType = "", existingRecommendationData = model.homeRecommendations)

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

        getHomeRecommendationCardUseCase.givenThrows(exception, productPage)

        // assert HomeRecommendationCardState is equal LoadingState
        assertCollectingRecommendationCardState {
            Assert.assertTrue(it.first() is HomeRecommendationCardState.Loading)
        }

        homeRecommendationViewModel.fetchHomeRecommendation("", "", sourceType = "", tabIndex = 1, refreshType = RefreshType.OPEN)

        assertCollectingRecommendationCardState {
            Assert.assertTrue(it.first() is HomeRecommendationCardState.Fail)

            homeRecommendationViewModel.fetchNextHomeRecommendation(1, "", productPage, locationParam = "", sourceType = "", existingRecommendationData = (it.first() as HomeRecommendationCardState.Fail).data.homeRecommendations)
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
