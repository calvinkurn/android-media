package com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.withdraw.saldowithdrawal.domain.model.CheckEligible
import com.tokopedia.withdraw.saldowithdrawal.domain.model.GqlRekeningPremiumResponse
import com.tokopedia.withdraw.saldowithdrawal.domain.usecase.GqlRekeningPremiumDataUseCase
import com.tokopedia.withdraw.util.observeOnce
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RekeningPremiumViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val useCase = mockk<GqlRekeningPremiumDataUseCase>()

    @ExperimentalCoroutinesApi
    private val rekeningPremiumViewModel = spyk(RekeningPremiumViewModel(useCase,
            TestCoroutineDispatcher()))


    @Before
    fun setUp() {
    }

    @Test
    fun `loadRekeningPremiumData check Fail Result`() {
        val data = mockk<Fail>()
        coEvery { useCase.getRekeningPremiumData() } returns data
        rekeningPremiumViewModel.loadRekeningPremiumData()
        rekeningPremiumViewModel.rekeningPremiumMutableData.observeOnce {
            assert(it is Fail)
        }
    }

    @Test
    fun `loadRekeningPremiumData check Success Result`() {
        val data = mockk<Success<GqlRekeningPremiumResponse>>()
        val checkEligibleMockk = mockk<CheckEligible>(relaxed = true)
        coEvery { useCase.getRekeningPremiumData() } returns data
        every { data.data.checkEligible } returns checkEligibleMockk
        rekeningPremiumViewModel.loadRekeningPremiumData()
        rekeningPremiumViewModel.rekeningPremiumMutableData.observeOnce {
            assert(it is Success)
        }
    }
}