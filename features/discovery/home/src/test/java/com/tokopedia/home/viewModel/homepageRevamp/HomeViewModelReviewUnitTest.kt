package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.domain.gql.ProductrevDismissSuggestion
import com.tokopedia.home.beranda.domain.interactor.DismissHomeReviewUseCase
import com.tokopedia.home.beranda.domain.interactor.repository.HomeReviewSuggestedRepository
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReviewResponse
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
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

    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase>(relaxed = true)
    private val getHomeReviewSuggestedUseCase = mockk<HomeReviewSuggestedRepository>(relaxed = true)
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
        val observerHome: Observer<HomeDynamicChannelModel> = mockk(relaxed = true)

        // Populate data view model
        every { userSessionInterface.isLoggedIn } returns true

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDynamicChannelModel(
                        list = listOf(review)
                )
        )
        coEvery { getHomeReviewSuggestedUseCase.executeOnBackground() } returns SuggestedProductReview(
                suggestedProductReview = SuggestedProductReviewResponse(
                        title = "Suggested Title"
                )
        )

        // home viewModel
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, homeReviewSuggestedRepository = getHomeReviewSuggestedUseCase, userSessionInterface = userSessionInterface).apply {
        }
        homeViewModel.homeLiveDynamicChannel.observeForever(observerHome)

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
            val channel: Channel<HomeDynamicChannelModel> = Channel()
            val homeData1 = HomeDynamicChannelModel(
                    isCache = true,
                    list = listOf(review)
            )
            val homeData2 = HomeDynamicChannelModel(
                    list = listOf(review),
                    isProcessingAtf = false
            )
            // Populate data view model
            //TODO fix this for unit test
//            coEvery { getHomeUseCase.getHomeData() } returns flow {
//                channel.consumeAsFlow().collect {
//                    emit(it)
//                }
//            }
            launch {
                channel.send(homeData1)
            }

            // home viewModel
            homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase).also {
                launch {
                    channel.send(homeData2)
                }
            }
            // Expect Review widget will hide on user screen
            homeViewModel.homeLiveDynamicChannel.observeOnce { homeDataModel ->
                assert(homeDataModel.list.find { it is ReviewDataModel } == null) }
            channel.close()
        }

    @Test
    fun `Test Review is visible trying to dismiss`(){
        val review = ReviewDataModel(channel = DynamicHomeChannel.Channels())
        val observerHome: Observer<HomeDynamicChannelModel> = mockk(relaxed = true)

        // Populate data view model
        //TODO fix this for unit test
//        coEvery { getHomeUseCase.getHomeData() } returns flow{
//            emit(HomeDynamicChannelModel(
//                    isCache = false,
//                    list = listOf(review)
//            ))
//        }

        coEvery { getHomeReviewSuggestedUseCase.executeOnBackground() } returns SuggestedProductReview(
                suggestedProductReview = SuggestedProductReviewResponse(
                        title = "Suggested Title"
                )
        )

        coEvery { dismissHomeReviewUseCase.executeOnBackground() } returns ProductrevDismissSuggestion()

        // home viewModel
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, dismissHomeReviewUseCase = dismissHomeReviewUseCase, homeReviewSuggestedRepository = getHomeReviewSuggestedUseCase)
        homeViewModel.homeLiveDynamicChannel.observeForever(observerHome)
        Thread.sleep(300)
        homeViewModel.dismissReview()
        assert(homeViewModel.homeDataModel.list.find { it is ReviewDataModel } == null)
    }

    @Test
    fun `Test Review is not visible`(){
        val observerHome: Observer<HomeDynamicChannelModel> = mockk(relaxed = true)

        // Populate data viewmodel
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDynamicChannelModel(
                        list = listOf()
                )
        )

        // home viewModel
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, homeReviewSuggestedRepository =  getHomeReviewSuggestedUseCase)
        homeViewModel.homeLiveDynamicChannel.observeForever(observerHome)


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
        val observerHome: Observer<HomeDynamicChannelModel> = mockk(relaxed = true)


        // Populate data view model
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDynamicChannelModel(
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
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, homeReviewSuggestedRepository = getHomeReviewSuggestedUseCase)
        homeViewModel.homeLiveDynamicChannel.observeForever(observerHome)

        homeViewModel.homeLiveDynamicChannel.observeOnce { homeDataModel ->
            homeDataModel.list.isNotEmpty() && homeDataModel.list.find { it is ReviewDataModel } != null
                    && (homeDataModel.list.find { it is ReviewDataModel } as ReviewDataModel).suggestedProductReview.suggestedProductReview.title == "Suggested Title"
        }
    }

    @Test
    fun `Test Review with data product and close the widget`(){
        val review = ReviewDataModel(channel = DynamicHomeChannel.Channels())
        val observerHome: Observer<HomeDynamicChannelModel> = mockk(relaxed = true)

        // Populate data viewmodel
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDynamicChannelModel(
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
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, homeReviewSuggestedRepository =  getHomeReviewSuggestedUseCase)
        homeViewModel.homeLiveDynamicChannel.observeForever(observerHome)

        // Close widget pressed
        homeViewModel.onRemoveSuggestedReview()

        // Expect Review widget will show on user screen
        homeViewModel.homeLiveDynamicChannel.observeOnce{ homeDataModel ->
            homeDataModel.list.find { it is ReviewDataModel } == null
        }
    }

    @Test
    fun `Test Review with data product and try remove when geolocation on`() =
        runBlockingTest {

            val review = ReviewDataModel(channel = DynamicHomeChannel.Channels())
            val observerHome: Observer<HomeDynamicChannelModel> = mockk(relaxed = true)
            val channel: Channel<HomeDynamicChannelModel> = Channel()

            // Populate data viewmodel
            val homeData1 = HomeDynamicChannelModel(
                    isCache = false,
                    list = listOf(review)
            )
            val homeData2 = HomeDynamicChannelModel(
                    isCache = true,
                    list = listOf(review)
            )

            launch {
                channel.send(homeData1)
            }
            // Populate data view model
            //TODO fix this for unit test
//            coEvery { getHomeUseCase.getHomeData() } returns flow{
//                channel.consumeAsFlow().collect{
//                    emit(it)
//                }
//            }

            // Review data
            coEvery { getHomeReviewSuggestedUseCase.executeOnBackground() } returns SuggestedProductReview(
                    suggestedProductReview = SuggestedProductReviewResponse(
                            title = "Suggested Title"
                    )
            )
            // home viewModel
            homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, homeReviewSuggestedRepository = getHomeReviewSuggestedUseCase).also {
            }


            homeViewModel.homeLiveDynamicChannel.observeForever(observerHome)

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