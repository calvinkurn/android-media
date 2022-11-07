package com.tokopedia.top_ads_headline.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topads.common.data.response.Error
import com.tokopedia.topads.common.data.response.ResponseGroupValidateName
import com.tokopedia.topads.common.domain.usecase.TopAdsGroupValidateNameUseCase
import io.mockk.*
import org.junit.Assert.*

import org.junit.After
import org.junit.Rule
import org.junit.Test

class EditAdOthersViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val topAdsGroupValidateNameUseCase: TopAdsGroupValidateNameUseCase =
        mockk(relaxed = true)

    private val viewModel = spyk(EditAdOthersViewModel(topAdsGroupValidateNameUseCase))

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `validateGroup success test`() {
        val expected = ResponseGroupValidateName()

        coEvery { topAdsGroupValidateNameUseCase.executeOnBackground() } returns expected

        var successCalled = false

        viewModel.validateGroup("", {
            successCalled = true
        }, {})
        assertTrue(successCalled)
    }

    @Test
    fun `validateGroup error test`() {
        val expected = spyk(ResponseGroupValidateName())

        every { expected.topAdsGroupValidateName.errors } returns listOf(Error())
        coEvery { topAdsGroupValidateNameUseCase.executeOnBackground() } returns expected

        var successCalled = false

        viewModel.validateGroup("", { successCalled = true }, {})
        assertTrue(!successCalled)
    }

    @Test
    fun `validateGroup exception test`() {
        every { topAdsGroupValidateNameUseCase.setParams(any(), any()) } throws Throwable()

        var successCalled = false

        viewModel.validateGroup("", { successCalled = true }, {})
        assertTrue(!successCalled)
    }
}