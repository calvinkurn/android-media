package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.data.usecase.HomeRevampUseCase
import com.tokopedia.home.beranda.domain.gql.ProductrevDismissSuggestion
import com.tokopedia.home.beranda.domain.interactor.DismissHomeReviewUseCase
import com.tokopedia.home.beranda.domain.interactor.GetHomeReviewSuggestedUseCase
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReviewResponse
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ReviewDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home.ext.observeOnce
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * Created by Lukas on 14/05/20.
 */

class HomeViewModelReviewUnitTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeRevampUseCase>(relaxed = true)
    private val getHomeReviewSuggestedUseCase = mockk<GetHomeReviewSuggestedUseCase>(relaxed = true)
    private val userSessionInterface = mockk<UserSessionInterface>(relaxed = true)
    private val dismissHomeReviewUseCase: DismissHomeReviewUseCase = mockk(relaxed = true)
    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var homeViewModel: HomeRevampViewModel

    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `Test Review is visible`(){
        val review = ReviewDataModel(channel = DynamicHomeChannel.Channels())
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)

        // Populate data view model
        every { userSessionInterface.isLoggedIn } returns true

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(review)
                )
        )
        coEvery { getHomeReviewSuggestedUseCase.executeOnBackground() } returns SuggestedProductReview(
                suggestedProductReview = SuggestedProductReviewResponse(
                        title = "Suggested Title"
                )
        )

        // home viewModel
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getHomeReviewSuggestedUseCase = getHomeReviewSuggestedUseCase, userSessionInterface = userSessionInterface).apply {
            setNeedToShowGeolocationComponent(false)
        }
        homeViewModel.homeLiveData.observeForever(observerHome)

        // Expect Review widget will show on user screen
        verifyOrder {
            // check on home data initial first channel is dynamic channel
            observerHome.onChanged(match { homeDataModel ->
                homeDataModel.list.isNotEmpty() && homeDataModel.list.find { it is ReviewDataModel } != null
            })
        }
        confirmVerified(observerHome)
    }

    @Test
    fun `Test Review is not visible when geolocation show`() =
        runBlockingTest {
            val review = ReviewDataModel(channel = DynamicHomeChannel.Channels())
            val channel: Channel<HomeDataModel> = Channel()
            val homeData1 = HomeDataModel(
                    isCache = true,
                    list = listOf(review)
            )
            val homeData2 = HomeDataModel(
                    list = listOf(review),
                    isProcessingAtf = false
            )
            // Populate data view model
            coEvery { getHomeUseCase.getHomeData() } returns flow {
                channel.consumeAsFlow().collect {
                    emit(it)
                }
            }
            launch {
                channel.send(homeData1)
            }

            // home viewModel
            homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase).also {
                it.setNeedToShowGeolocationComponent(true)
                launch {
                    channel.send(homeData2)
                }
            }
            // Expect Review widget will hide on user screen
            homeViewModel.homeLiveData.observeOnce { homeDataModel ->
                assert(homeDataModel.list.find { it is ReviewDataModel } == null) }
            channel.close()
        }

    @Test
    fun `Test Review is visible trying to dismiss`(){
        val review = ReviewDataModel(channel = DynamicHomeChannel.Channels())
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)

        // Populate data view model
        coEvery { getHomeUseCase.getHomeData() } returns flow{
            emit(HomeDataModel(
                    isCache = false,
                    list = listOf(review)
            ))
        }

        coEvery { getHomeReviewSuggestedUseCase.executeOnBackground() } returns SuggestedProductReview(
                suggestedProductReview = SuggestedProductReviewResponse(
                        title = "Suggested Title"
                )
        )

        coEvery { dismissHomeReviewUseCase.executeOnBackground() } returns ProductrevDismissSuggestion()

        // home viewModel
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, dismissHomeReviewUseCase = dismissHomeReviewUseCase, getHomeReviewSuggestedUseCase = getHomeReviewSuggestedUseCase)
        homeViewModel.homeLiveData.observeForever(observerHome)
        Thread.sleep(300)
        homeViewModel.dismissReview()
        // Expect Review widget will show on user screen
        verifyOrder {
            // check on home data initial first channel is dynamic channel
            observerHome.onChanged(match { homeDataModel ->
                homeDataModel.list.find { it is ReviewDataModel } != null
            })
            observerHome.onChanged(match { homeDataModel ->
                homeDataModel.list.find { it is ReviewDataModel } == null
            })
        }
        confirmVerified(observerHome)
    }

    @Test
    fun `Test Review is not visible`(){
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)

        // Populate data viewmodel
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf()
                )
        )

        // home viewModel
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getHomeReviewSuggestedUseCase =  getHomeReviewSuggestedUseCase)
        homeViewModel.homeLiveData.observeForever(observerHome)


        // Expect Review widget will show on user screen
        verifyOrder {
            // check on home data initial first channel is dynamic channel
            observerHome.onChanged(match { homeDataModel ->
                homeDataModel.list.find { it is ReviewDataModel } == null
            })
        }
        confirmVerified(observerHome)

    }

    @Test
    fun `Test Review with data product`(){
        val review = ReviewDataModel(channel = DynamicHomeChannel.Channels())
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)


        // Populate data view model
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(review)
                )
        )

        // Review keyword data
        coEvery { getHomeReviewSuggestedUseCase.executeOnBackground() } returns SuggestedProductReview(
                suggestedProductReview = SuggestedProductReviewResponse(
                        title = "Suggested Title"
                )
        )

        // home viewModel
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getHomeReviewSuggestedUseCase = getHomeReviewSuggestedUseCase)
        homeViewModel.homeLiveData.observeForever(observerHome)

        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            homeDataModel.list.isNotEmpty() && homeDataModel.list.find { it is ReviewDataModel } != null
                    && (homeDataModel.list.find { it is ReviewDataModel } as ReviewDataModel).suggestedProductReview.suggestedProductReview.title == "Suggested Title"
        }
    }

    @Test
    fun `Test Review with data product and close the widget`(){
        val review = ReviewDataModel(channel = DynamicHomeChannel.Channels())
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)

        // Populate data viewmodel
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(review)
                )
        )

        // Review data
        coEvery { getHomeReviewSuggestedUseCase.executeOnBackground() } returns SuggestedProductReview(
                suggestedProductReview = SuggestedProductReviewResponse(
                        title = "Suggested Title"
                )
        )

        // home viewModel
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getHomeReviewSuggestedUseCase =  getHomeReviewSuggestedUseCase)
        homeViewModel.homeLiveData.observeForever(observerHome)

        // Close widget pressed
        homeViewModel.onRemoveSuggestedReview()

        // Expect Review widget will show on user screen
        homeViewModel.homeLiveData.observeOnce{ homeDataModel ->
            homeDataModel.list.find { it is ReviewDataModel } == null
        }
    }

    @Test
    fun `Test Review with data product and try remove when geolocation on`() =
        runBlockingTest {

            val review = ReviewDataModel(channel = DynamicHomeChannel.Channels())
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            val channel: Channel<HomeDataModel> = Channel()

            // Populate data viewmodel
            val homeData1 = HomeDataModel(
                    isCache = false,
                    list = listOf(review)
            )
            val homeData2 = HomeDataModel(
                    isCache = true,
                    list = listOf(review)
            )

            launch {
                channel.send(homeData1)
            }
            // Populate data view model
            coEvery { getHomeUseCase.getHomeData() } returns flow{
                channel.consumeAsFlow().collect{
                    emit(it)
                }
            }

            // Review data
            coEvery { getHomeReviewSuggestedUseCase.executeOnBackground() } returns SuggestedProductReview(
                    suggestedProductReview = SuggestedProductReviewResponse(
                            title = "Suggested Title"
                    )
            )
            // home viewModel
            homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getHomeReviewSuggestedUseCase = getHomeReviewSuggestedUseCase).also {
                it.setNeedToShowGeolocationComponent(true)
            }


            homeViewModel.homeLiveData.observeForever(observerHome)

            launch {
                channel.send(homeData2)
            }
            // Expect Review widget will show on user screen
            verifyOrder {
                // check on home data initial first channel is dynamic channel
                observerHome.onChanged(match { homeDataModel ->
                    homeDataModel.list.find { it is ReviewDataModel } != null
                })
            }
            confirmVerified(observerHome)
            channel.close()
        }
}