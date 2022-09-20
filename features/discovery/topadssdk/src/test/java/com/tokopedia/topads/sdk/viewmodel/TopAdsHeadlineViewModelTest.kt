package com.tokopedia.topads.sdk.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.TopAdsHeadlineResponse
import com.tokopedia.topads.sdk.domain.usecase.GetTopAdsHeadlineUseCase
import com.tokopedia.topads.sdk.utils.TopAdsAddressHelper
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TopAdsHeadlineViewModelTest {
    @get:Rule
    val testRule = CoroutineTestRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val topAdsAddressHelper: TopAdsAddressHelper =
        mockk(relaxed = true)

    private val topAdsHeadlineUseCase: GetTopAdsHeadlineUseCase =
        mockk(relaxed = true)

    private val viewModel =
        spyk(TopAdsHeadlineViewModel(topAdsAddressHelper, topAdsHeadlineUseCase))

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `getTopAdsHeadlineData is success`() {
        var result = ""
        every { topAdsAddressHelper.getAddressData() } returns mockk(relaxed = true)
        every { topAdsHeadlineUseCase.setParams(any(), any()) } just Runs
        coEvery { topAdsHeadlineUseCase.executeOnBackground() } returns TopAdsHeadlineResponse(
            displayAds = CpmModel(
                data = mutableListOf(CpmData(applinks = "applink"))
            )
        )
        viewModel.getTopAdsHeadlineData(
            "",
            onSuccess = { result = it.data.first().applinks },
            onError = {})

        Assert.assertEquals(result, "applink")
    }

    @Test
    fun `getTopAdsHeadlineData is empty`() {
        var result = false
        every { topAdsAddressHelper.getAddressData() } returns mockk(relaxed = true)
        every { topAdsHeadlineUseCase.setParams(any(), any()) } just Runs
        coEvery { topAdsHeadlineUseCase.executeOnBackground() } returns TopAdsHeadlineResponse(
            displayAds = CpmModel(
                data = mutableListOf()
            )
        )
        viewModel.getTopAdsHeadlineData(
            "",
            onSuccess = {},
            onError = {result = true})

        Assert.assertTrue(result)
    }

    @Test()
    fun `getTopAdsHeadlineData is failed`() {
        var result = false
        every { topAdsAddressHelper.getAddressData() } returns mockk(relaxed = true)
        every { topAdsHeadlineUseCase.setParams(any(), any()) } just Runs
        coEvery { topAdsHeadlineUseCase.executeOnBackground() } throws Exception()
        viewModel.getTopAdsHeadlineData(
            "",
            onSuccess = {},
            onError = {result = true})

        Assert.assertTrue(result)
    }


    @After
    fun tearDown() {
        unmockkAll()
    }
}