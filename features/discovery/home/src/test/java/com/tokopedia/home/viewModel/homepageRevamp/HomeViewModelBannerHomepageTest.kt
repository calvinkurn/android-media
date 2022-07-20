package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 14/05/20.
 */

class HomeViewModelBannerHomepageTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase> (relaxed = true)

    private lateinit var homeViewModel: HomeRevampViewModel

    @ExperimentalCoroutinesApi
    @Test
    fun `When new dynamic channel value collected on initFlow then currentNextPage token should be updated with latest value`(){
        val mockNextPageToken = "1"
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDynamicChannelModel(list = listOf(), topadsPage = mockNextPageToken)
        )
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        Assert.assertTrue(homeViewModel.currentTopAdsBannerPage == mockNextPageToken)
    }
}