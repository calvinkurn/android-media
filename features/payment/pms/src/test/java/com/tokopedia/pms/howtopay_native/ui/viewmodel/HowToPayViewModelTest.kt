package com.tokopedia.pms.howtopay_native.ui.viewmodel

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.pms.howtopay_native.data.model.AppLinkPaymentInfo
import com.tokopedia.pms.howtopay_native.data.model.HowToPayData
import com.tokopedia.pms.howtopay_native.data.model.HowToPayGqlResponse
import com.tokopedia.pms.howtopay_native.domain.AppLinkPaymentUseCase
import com.tokopedia.pms.howtopay_native.domain.GetGqlHowToPayInstructions
import com.tokopedia.pms.howtopay_native.domain.GetHowToPayInstructionsMapper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class HowToPayViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val useCase: GetGqlHowToPayInstructions = mockk()
    private val appLinkPaymentUseCase: AppLinkPaymentUseCase = mockk()
    private val getHowToPayInstructionsMapper: GetHowToPayInstructionsMapper = mockk()
    private val appLinkPaymentInfo = AppLinkPaymentInfo("mcTest", "trxTest")
    private lateinit var viewModel: HowToPayViewModel

    @Before
    fun setup() {
        viewModel = HowToPayViewModel(appLinkPaymentUseCase, useCase, getHowToPayInstructionsMapper)
    }


    @Test
    fun `getMutableLiveData success`() {
        val result = mockk<HowToPayGqlResponse>()
        val resultFinal = mockk<HowToPayData>()

        coEvery { useCase.getGqlHowToPayInstruction(any(), any(), any()) }
            .coAnswers {
                firstArg<(HowToPayGqlResponse) -> Unit>().invoke(result)
            }
        coEvery { getHowToPayInstructionsMapper.getHowToPayInstruction(any(), any(), any()) }
            .coAnswers {
                secondArg<(HowToPayData) -> Unit>().invoke(resultFinal)
            }

        viewModel.getGqlHtpInstructions(appLinkPaymentInfo)
        assert(viewModel.howToPayLiveData.value is Success)
    }

    @Test
    fun `getMutableLiveData fail`() {
        val result = mockk<Throwable>()
        val appLinkPaymentInfo = mockk<AppLinkPaymentInfo>()
        coEvery { useCase.getGqlHowToPayInstruction(any(), any(), any()) }
            .coAnswers {
                secondArg<(Throwable) -> Unit>().invoke(result)
            }
        viewModel.getGqlHtpInstructions(appLinkPaymentInfo)
        assert(viewModel.howToPayLiveData.value is Fail)
    }

    @Test
    fun `getAppLinkPaymentInfoData success`() {
        val result = mockk<AppLinkPaymentInfo>()
        val bundle = mockk<Bundle>()
        coEvery { appLinkPaymentUseCase.getAppLinkPaymentInfo(any(), any(), any()) }
            .coAnswers {
                secondArg<(AppLinkPaymentInfo) -> Unit>().invoke(result)
            }
        viewModel.getAppLinkPaymentInfoData(bundle)
        assert(viewModel.appLinkPaymentLiveData.value is Success)
    }

    @Test
    fun `getAppLinkPaymentInfoData fail`() {
        val result = mockk<Throwable>()
        val bundle = mockk<Bundle>()
        coEvery { appLinkPaymentUseCase.getAppLinkPaymentInfo(any(), any(), any()) }
            .coAnswers {
                thirdArg<(Throwable) -> Unit>().invoke(result)
            }
        viewModel.getAppLinkPaymentInfoData(bundle)
        assert(viewModel.appLinkPaymentLiveData.value is Fail)
    }
}