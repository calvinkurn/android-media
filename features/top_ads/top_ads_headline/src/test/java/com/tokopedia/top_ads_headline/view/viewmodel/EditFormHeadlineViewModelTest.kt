package com.tokopedia.top_ads_headline.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.top_ads_headline_usecase.CreateHeadlineAdsUseCase
import com.tokopedia.top_ads_headline_usecase.model.TopAdsManageHeadlineInput
import com.tokopedia.top_ads_headline_usecase.model.TopadsManageHeadlineAdResponse
import com.tokopedia.topads.common.data.response.Error
import io.mockk.*
import org.junit.Assert.*

import org.junit.After
import org.junit.Rule
import org.junit.Test

class EditFormHeadlineViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private var createHeadlineAdsUseCase: CreateHeadlineAdsUseCase = mockk(relaxed = true)
    private var viewModel = spyk(EditFormHeadlineViewModel(createHeadlineAdsUseCase))

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `createHeadlineAd success test`() {
        var successCalled = false
        val obj = mockk<TopadsManageHeadlineAdResponse.Data>()

        every { obj.topadsManageHeadlineAd.success.id } returns "123"
        coEvery { createHeadlineAdsUseCase.executeOnBackground() } returns obj

        viewModel.editHeadlineAd(mockk(), { successCalled = true }, {})
        assertTrue(successCalled)
    }

    @Test
    fun `createHeadlineAd error test`() {
        var actual = ""
        val expected = "err"
        val obj = mockk<TopadsManageHeadlineAdResponse.Data>()

        every { obj.topadsManageHeadlineAd.errors } returns listOf(Error().apply {
            detail = expected
        })
        every { obj.topadsManageHeadlineAd.success.id } returns ""
        coEvery { createHeadlineAdsUseCase.executeOnBackground() } returns obj

        viewModel.editHeadlineAd(mockk(), {}, { actual = it })
        assertEquals(expected, actual)
    }

    @Test
    fun `createHeadlineAd exception test`() {
        var actual = ""
        val expected = "err"

        every { createHeadlineAdsUseCase.setParams(any<TopAdsManageHeadlineInput>()) } throws Throwable(
            expected)

        viewModel.editHeadlineAd(mockk(), {}, { actual = it })
        assertEquals(expected, actual)
    }
}