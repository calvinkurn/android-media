package com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.withdraw.saldowithdrawal.domain.model.*
import com.tokopedia.withdraw.saldowithdrawal.domain.usecase.GQLValidateWithdrawalUseCase
import com.tokopedia.withdraw.saldowithdrawal.domain.usecase.GetBankListUseCase
import com.tokopedia.withdraw.saldowithdrawal.domain.usecase.GetTopadsAutoTopupWithdrawRecomUseCase
import com.tokopedia.withdraw.saldowithdrawal.domain.usecase.GetWDBannerUseCase
import com.tokopedia.withdraw.util.observeOnce
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test


class SaldoWithdrawalViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val bankListUseCase = mockk<GetBankListUseCase>()
    private val rekeningBannerUseCase = mockk<GetWDBannerUseCase>()
    private val validatePopUpUseCase = mockk<GQLValidateWithdrawalUseCase>()
    private val topadsAutoTopupWithdrawRecomUseCase = mockk<GetTopadsAutoTopupWithdrawRecomUseCase>()

    @ExperimentalCoroutinesApi
    private val saldoWithdrawalViewModel = spyk(SaldoWithdrawalViewModel(bankListUseCase,
            rekeningBannerUseCase,
            validatePopUpUseCase,
            topadsAutoTopupWithdrawRecomUseCase,
            TestCoroutineDispatcher()))


    @Test
    fun `getRekeningBannerDataList fail`() {
        val result = mockk<Fail>()
        coEvery { rekeningBannerUseCase.getRekeningProgramBanner() } returns result
        saldoWithdrawalViewModel.getRekeningBannerDataList()
        saldoWithdrawalViewModel.bannerListLiveData.observeOnce {
            assert(it is Fail)
        }
    }

    @Test
    fun `getRekeningBannerDataList success`() {
        val result = mockk<Success<GetWDBannerResponse>>()
        val mockedBannerList = mockk<ArrayList<BannerData>>()
        coEvery { rekeningBannerUseCase.getRekeningProgramBanner() } returns result
        every { result.data.richieGetWDBanner.data } returns mockedBannerList
        saldoWithdrawalViewModel.getRekeningBannerDataList()
        saldoWithdrawalViewModel.bannerListLiveData.observeOnce {
            when (it) {
                is Success -> {
                    assertEquals(it.data, mockedBannerList)
                }
                else -> assert(false)
            }
        }
    }

    @Test
    fun `getBankList fail`() {
        val result = mockk<Fail>()
        coEvery { bankListUseCase.getBankList(false) } returns result
        saldoWithdrawalViewModel.getBankList()
        saldoWithdrawalViewModel.bankListResponseMutableData.observeOnce {
            assert(it is Fail)
        }
    }

    @Test
    fun `getBankList success`() {
        val result = mockk<Success<GqlGetBankDataResponse>>()
        val bankAccountList = mockk<ArrayList<BankAccount>>()
        coEvery { bankListUseCase.getBankList(false) } returns result
        every { result.data.bankAccount.bankAccountList } returns bankAccountList
        saldoWithdrawalViewModel.getBankList()
        saldoWithdrawalViewModel.bankListResponseMutableData.observeOnce {
            when (it) {
                is Success -> {
                    assertEquals(it.data, result.data.bankAccount)
                }
                else -> assert(false)
            }
        }
    }

    @Test
    fun `getValidatePopUpData fail`() {
        val result = mockk<Fail>()
        val bankAccount = mockk<BankAccount>()
        coEvery { validatePopUpUseCase.getValidatePopUpData(bankAccount) } returns result
        saldoWithdrawalViewModel.getValidatePopUpData(bankAccount, "", 0, false)
        saldoWithdrawalViewModel.validatePopUpWithdrawalMutableData.observeOnce {
            assert(it is Fail)
        }
    }

    @Test
    fun `getValidatePopUpData success`() {
        val result = mockk<Success<GqlGetValidatePopUpResponse>>()
        val bankAccount = mockk<BankAccount>()
        val validatePopUpWithdrawal = mockk<ValidatePopUpWithdrawal>()
        coEvery { validatePopUpUseCase.getValidatePopUpData(bankAccount) } returns result
        every { result.data.validatePopUpWithdrawal } returns validatePopUpWithdrawal
        saldoWithdrawalViewModel.getValidatePopUpData(bankAccount, "", 0, false)
        saldoWithdrawalViewModel.bankListResponseMutableData.observeOnce {
            when (it) {
                is Success -> {
                    assertEquals(it.data, validatePopUpWithdrawal)
                }
                else -> assert(false)
            }
        }
    }

    @Test
    fun `getAutoTopAdsRecomWD success and should show bottomsheet`() {
        val bankAccount = mockk<BankAccount>()
        val withdrawalAmount = 60000
        val recomAmount = 50000.0
        val shopId = "150"

        val mockResponse = mockk<Success<GqlTopadsAutoTopupWithdrawRecomResponse>>()
        every {
            mockResponse.data.topAdsAutoTopupWithdrawalRecom.data.autoTopUpStatus
        } returns 1
        every {
            mockResponse.data.topAdsAutoTopupWithdrawalRecom.data.recommendationValue
        } returns recomAmount

        coEvery {
            topadsAutoTopupWithdrawRecomUseCase(shopId)
        } returns mockResponse

        saldoWithdrawalViewModel.getValidatePopUpData(
            bankAccount,
            shopId,
            withdrawalAmount.toLong(),
            true
        )

        saldoWithdrawalViewModel.shouldOpenTopadsAutoTopupWithdrawRecomBottomSheet.observeOnce {
            assertTrue(it.first)
            assertTrue(it.second == recomAmount.toLong())
        }
    }

    @Test
    fun `getAutoTopAdsRecomWD fail and should not show bottomsheet`() {
        val bankAccount = mockk<BankAccount>()
        val withdrawalAmount = 60000
        val shopId = "150"

        val mockResponse = mockk<Fail>()

        coEvery {
            topadsAutoTopupWithdrawRecomUseCase(shopId)
        } returns mockResponse

        saldoWithdrawalViewModel.getValidatePopUpData(
            bankAccount,
            shopId,
            withdrawalAmount.toLong(),
            true
        )

        saldoWithdrawalViewModel.shouldOpenTopadsAutoTopupWithdrawRecomBottomSheet.observeOnce {
            assertTrue(!it.first)
            assertTrue(it.second == 0L)
        }
    }
}
