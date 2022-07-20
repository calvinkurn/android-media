package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.domain.gql.ProductrevDismissSuggestion
import com.tokopedia.home.beranda.domain.interactor.DismissHomeReviewUseCase
import com.tokopedia.home.beranda.domain.interactor.repository.HomeReviewSuggestedRepository
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeSuggestedReviewUseCase
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReviewResponse
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelRetryModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ReviewDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home.ext.observeOnce
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*


/**
 * Created by Lukas on 14/05/20.
 */

class HomeViewModelReviewUnitTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase>(relaxed = true)
    private val getHomeSuggestedReviewUseCase = mockk<HomeSuggestedReviewUseCase>(relaxed = true)

    private lateinit var homeViewModel: HomeRevampViewModel
    private val mockDefaultReviewList = HomeDynamicChannelModel(
            list = listOf(
                    ReviewDataModel(channel = DynamicHomeChannel.Channels())
            )
    )

    @ExperimentalCoroutinesApi
    @Test
    fun `When dismissReview then homeDataModel should remove suggested review`(){
        getHomeUseCase.givenGetHomeDataReturn(mockDefaultReviewList)
        getHomeSuggestedReviewUseCase.givenOnReviewDismissedReturn()

        homeViewModel = createHomeViewModel(
                getHomeUseCase = getHomeUseCase,
                homeSuggestedReviewUseCase = getHomeSuggestedReviewUseCase
        )
        homeViewModel.dismissReview()
        homeViewModel.homeDataModel.findWidget<ReviewDataModel>(
                actionOnFound = { model, index ->
                    Assert.assertTrue(false)
                },
                actionOnNotFound = {
                    Assert.assertTrue(true)
                }
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `When onRemoveSuggestedReview then homeDataModel should remove suggested review`(){
        getHomeUseCase.givenGetHomeDataReturn(mockDefaultReviewList)
        getHomeSuggestedReviewUseCase.givenOnReviewDismissedReturn()

        homeViewModel = createHomeViewModel(
                getHomeUseCase = getHomeUseCase,
                homeSuggestedReviewUseCase = getHomeSuggestedReviewUseCase
        )
        homeViewModel.onRemoveSuggestedReview()
        homeViewModel.homeDataModel.findWidget<ReviewDataModel>(
                actionOnFound = { model, index ->
                    Assert.assertTrue(false)
                },
                actionOnNotFound = {
                    Assert.assertTrue(true)
                }
        )
    }
}