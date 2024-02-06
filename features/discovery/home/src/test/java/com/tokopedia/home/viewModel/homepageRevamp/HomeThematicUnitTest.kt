package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.presentation.view.helper.HomeRollenceController
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home_component.usecase.thematic.ThematicModel
import com.tokopedia.home_component.usecase.thematic.ThematicUseCase
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by frenzel
 */
@FlowPreview
@ExperimentalCoroutinesApi
class HomeThematicUnitTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val homeThematicUseCase = mockk<ThematicUseCase>(relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel
    private val testDispatcher = TestCoroutineDispatcher()
    private val mockHomeThematicModel = ThematicModel(
        isShown = true,
        colorMode = "light",
        heightPercentage = 50,
        backgroundImageURL = "https://images.tokopedia.net/img/metesI/2023/10/6/9a5497e3-0090-4b5e-8a48-a9f1b93808ee.png",
        foregroundImageURL = "https://images.tokopedia.net/img/cache/300/metesI/2023/10/5/ee621656-60dd-4b12-b379-7971804fb27f.png.webp"
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkObject(HomeRollenceController)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `given success when get thematic background then update live data`() {
        coEvery { homeThematicUseCase.executeOnBackground() } returns mockHomeThematicModel
        homeViewModel = createHomeViewModel(homeThematicUseCase = homeThematicUseCase)

        assert(homeViewModel.thematicLiveData.value?.isShown == true)
    }

    @Test
    fun `given thrown error when get thematic background then do not show thematic`() {
        coEvery { homeThematicUseCase.executeOnBackground() } throws Exception()
        homeViewModel = createHomeViewModel(homeThematicUseCase = homeThematicUseCase)

        assert(homeViewModel.thematicLiveData.value?.isShown == false)
    }
}
