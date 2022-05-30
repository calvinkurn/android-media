package com.tokopedia.shopdiscount.set_period.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shopdiscount.bulk.data.response.GetSlashPriceBenefitResponse
import com.tokopedia.shopdiscount.bulk.domain.usecase.GetSlashPriceBenefitUseCase
import com.tokopedia.shopdiscount.common.data.response.ResponseHeader
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Period
import java.time.ZoneId
import java.util.*

@ExperimentalCoroutinesApi
class SetPeriodBottomSheetViewModelTest {

    @RelaxedMockK
    lateinit var getSlashPriceBenefitUseCase: GetSlashPriceBenefitUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = CoroutineTestRule()

    private val mockErrorMessage = "Error"

    private val viewModel by lazy {
        SetPeriodBottomSheetViewModel(
            testCoroutineRule.dispatchers,
            getSlashPriceBenefitUseCase
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `When success get seller info benefit data, should return success result and matched with mock data`() {
        coEvery {
            getSlashPriceBenefitUseCase.executeOnBackground()
        } returns getGetSlashPriceBenefitMockSuccessResponse()
        viewModel.getSlashPriceBenefit()
        val liveDataValue = viewModel.benefit.value
        assert(liveDataValue is Success)
        val liveDataSuccessValue = liveDataValue as Success
        assert(liveDataSuccessValue.data.getSlashPriceBenefit.isUseVps)
        assert(liveDataSuccessValue.data.getSlashPriceBenefit.responseHeader.success)
    }

    private fun getGetSlashPriceBenefitMockSuccessResponse(): GetSlashPriceBenefitResponse {
        return GetSlashPriceBenefitResponse(
            GetSlashPriceBenefitResponse.GetSlashPriceBenefit(
                isUseVps = true,
                responseHeader = ResponseHeader(
                    success = true
                )
            )
        )
    }

    @Test
    fun `When error get seller info benefit data, should return fail result`() {
        coEvery {
            getSlashPriceBenefitUseCase.executeOnBackground()
        } throws Exception(mockErrorMessage)
        viewModel.getSlashPriceBenefit()
        val liveDataValue = viewModel.benefit.value
        assert(liveDataValue is Fail)
        val liveDataFailValue = liveDataValue as Fail
        assert(liveDataFailValue.throwable.message == mockErrorMessage)
    }

    @Test
    fun `When onOneYearPeriodSelected called, then start date and end date diff should be 1 year`() {
        viewModel.onOneYearPeriodSelected()
        val liveDataValue = viewModel.startDate.value
        val liveDataValue2 = viewModel.endDate.value
        assert(liveDataValue == viewModel.getSelectedStartDate())
        assert(liveDataValue2 == viewModel.getSelectedEndDate())
        val yearDiff = Period.between(
            liveDataValue?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate(),
            liveDataValue2?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
        ).years
        assert(yearDiff == 1)
    }

    @Test
    fun `When onSixMonthPeriodSelected called, then start date and end date diff should be 6 months`() {
        viewModel.onSixMonthPeriodSelected()
        val liveDataValue = viewModel.startDate.value
        val liveDataValue2 = viewModel.endDate.value
        assert(liveDataValue == viewModel.getSelectedStartDate())
        assert(liveDataValue2 == viewModel.getSelectedEndDate())
        val monthsDiff = Period.between(
            liveDataValue?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate(),
            liveDataValue2?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
        ).months
        assert(monthsDiff == 6)
    }

    @Test
    fun `When onOneYearPeriodSelected called, then start date and end date diff should be 1 month`() {
        viewModel.onOneMonthPeriodSelected()
        val liveDataValue = viewModel.startDate.value
        val liveDataValue2 = viewModel.endDate.value
        assert(liveDataValue == viewModel.getSelectedStartDate())
        assert(liveDataValue2 == viewModel.getSelectedEndDate())
        val monthsDiff = Period.between(
            liveDataValue?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate(),
            liveDataValue2?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
        ).months
        assert(monthsDiff == 1)
    }

    @Test
    fun `When getCurrentSelection called, then should return given start date and end date`() {
        val mockStartDate = Date()
        val mockEndDate = Date()
        viewModel.setSelectedStartDate(mockStartDate)
        viewModel.setSelectedEndDate(mockEndDate)
        val setPeriodResultUiModel = viewModel.getCurrentSelection()
        assert(setPeriodResultUiModel.startDate.time == mockStartDate.time)
        assert(setPeriodResultUiModel.endDate.time == mockEndDate.time)
    }

    @Test
    fun `When setSelectedStartDate called, then startDate should return given start date`() {
        val mockStartDate = Date()
        viewModel.setSelectedStartDate(mockStartDate)
        assert(viewModel.startDate.value?.time == mockStartDate.time)
    }

    @Test
    fun `When setSelectedEndDate called, then endDate should return given end date`() {
        val mockEndDate = Date()
        viewModel.setSelectedEndDate(mockEndDate)
        assert(viewModel.endDate.value?.time == mockEndDate.time)
    }

    @Test
    fun `When setBenefitPackageName called, then getBenefitPackageName should return given benefit package name`() {
        val mockBenefitPackageName = "Package1"
        viewModel.setBenefitPackageName(mockBenefitPackageName)
        assert(viewModel.getBenefitPackageName() == mockBenefitPackageName)
    }

    @Test
    fun `When setMaxDate called, then getMaxDate should return given max date`() {
        val mockMaxDate = Date()
        viewModel.setMaxDate(mockMaxDate)
        assert(viewModel.getMaxDate() == mockMaxDate)
    }

    @Test
    fun `When defaultMembershipEndDate called, then should return end date 1 year from now`() {
        val defaultMembershipEndDate = viewModel.defaultMembershipEndDate
        val yearDiff = Period.between(
            Date().toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate(),
            defaultMembershipEndDate.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate(),
        ).years
        assert(yearDiff == 1)
    }

}