package com.tokopedia.top_ads_headline.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topads.common.domain.usecase.CreateHeadlineAdsUseCase
import com.tokopedia.topads.common.domain.model.createheadline.TopAdsManageHeadlineInput
import com.tokopedia.topads.common.domain.model.createheadline.TopadsManageHeadlineAdResponse
import com.tokopedia.topads.common.data.response.Error
import com.tokopedia.topads.common.domain.model.GetVariantByIdResponse
import com.tokopedia.topads.common.domain.usecase.GetVariantByIdUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*

import org.junit.After
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AdScheduleAndBudgetViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule = CoroutineTestRule()

    @get:Rule
    val testCoroutineRule = UnconfinedTestRule()

    private var createHeadlineAdsUseCase: CreateHeadlineAdsUseCase = mockk(relaxed = true)
    private val getVariantByIdUseCase: GetVariantByIdUseCase = mockk(relaxed = true)
    private var viewModel = spyk(
        AdScheduleAndBudgetViewModel(
            createHeadlineAdsUseCase,
            rule.dispatchers,
            getVariantByIdUseCase
        )
    )

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

        viewModel.createHeadlineAd(mockk(), { successCalled = true }, {})
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

        viewModel.createHeadlineAd(mockk(), {}, { actual = it })
        assertEquals(expected, actual)
    }

    @Test
    fun `createHeadlineAd exception test`() {
        var actual = ""
        val expected = "err"

        every { createHeadlineAdsUseCase.setParams(any<TopAdsManageHeadlineInput>()) } throws Throwable(
            expected
        )

        viewModel.createHeadlineAd(mockk(), {}, { actual = it })
        assertEquals(expected, actual)
    }

    @Test
    fun `getVariantById success test`() {
        val obj = mockk<GetVariantByIdResponse>(relaxed = true)
        val variants = obj.getVariantById.shopIdVariants
        coEvery { getVariantByIdUseCase() } returns obj
        viewModel.getVariantById()
        assertEquals(variants, viewModel.shopVariant.value)
    }

    @Test
    fun `getVariantById error test`() {
        val variants = emptyList<GetVariantByIdResponse.GetVariantById.ExperimentVariant>()
        coEvery { getVariantByIdUseCase() } throws Throwable()
        viewModel.getVariantById()
        assertEquals(variants, viewModel.shopVariant.value)
    }
}
