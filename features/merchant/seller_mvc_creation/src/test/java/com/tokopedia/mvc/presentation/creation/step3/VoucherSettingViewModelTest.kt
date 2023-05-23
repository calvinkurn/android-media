package com.tokopedia.mvc.presentation.creation.step3

import android.content.SharedPreferences
import com.tokopedia.mvc.data.mapper.GetInitiateVoucherPageMapper
import com.tokopedia.mvc.data.mapper.VoucherValidationPartialMapper
import com.tokopedia.mvc.data.response.GetInitiateVoucherPageResponse
import com.tokopedia.mvc.data.response.VoucherValidationPartialResponse
import com.tokopedia.mvc.domain.entity.RemoteTicker
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.usecase.GetInitiateVoucherPageUseCase
import com.tokopedia.mvc.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.mvc.domain.usecase.VoucherValidationPartialUseCase
import com.tokopedia.mvc.presentation.creation.step3.uimodel.VoucherCreationStepThreeAction
import com.tokopedia.mvc.presentation.creation.step3.uimodel.VoucherCreationStepThreeEvent
import com.tokopedia.mvc.presentation.creation.step3.uimodel.VoucherCreationStepThreeUiState
import com.tokopedia.mvc.presentation.creation.util.CreationUtil.roundTimePerHalfHour
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.utils.date.removeTime
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class VoucherSettingViewModelTest {

    private lateinit var viewModel: VoucherSettingViewModel

    @RelaxedMockK
    lateinit var voucherValidationPartialUseCase: VoucherValidationPartialUseCase

    @RelaxedMockK
    lateinit var getInitiateVoucherPageUseCase: GetInitiateVoucherPageUseCase

    @RelaxedMockK
    lateinit var getTargetedTickerUseCase: GetTargetedTickerUseCase

    @RelaxedMockK
    lateinit var sharedPreferences: SharedPreferences

    @RelaxedMockK
    lateinit var mapper: VoucherValidationPartialMapper

    @RelaxedMockK
    lateinit var voucherMetadataMapper: GetInitiateVoucherPageMapper

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = VoucherSettingViewModel(
            CoroutineTestDispatchers,
            voucherValidationPartialUseCase,
            getInitiateVoucherPageUseCase,
            getTargetedTickerUseCase,
            sharedPreferences
        )
    }

    @Test
    fun `when initVoucherConfiguration is called, should set voucherConfiguration data accordingly`() {
        runBlockingTest {
            // Given
            val startPeriod = Date().roundTimePerHalfHour().removeTime()
            val expectedVoucherConfiguration = VoucherConfiguration(
                startPeriod = startPeriod,
                isFinishFilledStepTwo = true
            )

            val emittedValue = arrayListOf<VoucherCreationStepThreeUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            mockVoucherConfigurationInitiation()

            // Then
            val actual = emittedValue.last()
            Assert.assertEquals(expectedVoucherConfiguration, actual.voucherConfiguration)

            job.cancel()
        }
    }

    @Test
    fun `when getVoucherCreationMetadata() is called, should return discount promo type enablement correctly`() {
        runBlockingTest {
            // Given
            val expected = false

            mockGetVoucherCreationMetadataGQLCall()

            val emittedValue = arrayListOf<VoucherCreationStepThreeUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            mockVoucherConfigurationInitiation()

            // Then
            val actual = emittedValue.last().isDiscountPromoTypeEnabled
            assertEquals(expected, actual)

            job.cancel()
        }
    }

    @Test
    fun `when getCurrentVoucherConfiguration() is called, should return with the most updated voucher configuration data`() {
        runBlockingTest {
            // Given
            val pageMode = PageMode.CREATE
            val startPeriod = Date().roundTimePerHalfHour().removeTime()
            val voucherConfiguration = VoucherConfiguration(startPeriod = startPeriod)
            val expectedVoucherConfiguration = VoucherConfiguration(
                startPeriod = startPeriod,
                isFinishFilledStepTwo = true
            )

            val emittedValue = arrayListOf<VoucherCreationStepThreeUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(
                VoucherCreationStepThreeEvent.InitVoucherConfiguration(
                    pageMode = pageMode,
                    voucherConfiguration = voucherConfiguration
                )
            )

            // Given
            val actual = viewModel.getCurrentVoucherConfiguration()
            assertEquals(expectedVoucherConfiguration, actual)

            job.cancel()
        }
    }

    @Test
    fun `when handling navigation to previous step, should emit the correct action accordingly`() {
        runBlockingTest {
            // Given
            mockVoucherConfigurationInitiation()
            mockVoucherValidationPartialGQLCall()

            val voucherConfiguration = viewModel.getCurrentVoucherConfiguration()

            val expectedEmittedAction =
                VoucherCreationStepThreeAction.BackToPreviousStep(voucherConfiguration)

            val emittedAction = arrayListOf<VoucherCreationStepThreeAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedAction)
            }

            // When
            viewModel.processEvent(VoucherCreationStepThreeEvent.TapBackButton)

            // Then
            val actual = emittedAction.last()
            assertEquals(expectedEmittedAction, actual)

            job.cancel()
        }
    }

    @Test
    fun `when handling navigation to next step, should emit the correct action accordingly`() {
        runBlockingTest {
            // Given
            val pageMode = PageMode.CREATE

            mockVoucherConfigurationInitiation()
            mockVoucherValidationPartialGQLCall()

            val voucherConfiguration = viewModel.getCurrentVoucherConfiguration()

            val expectedEmittedAction =
                VoucherCreationStepThreeAction.ContinueToNextStep(voucherConfiguration)

            val emittedAction = arrayListOf<VoucherCreationStepThreeAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedAction)
            }

            // When
            viewModel.processEvent(
                VoucherCreationStepThreeEvent.NavigateToNextStep(
                    voucherConfiguration
                )
            )

            // Then
            val actual = emittedAction.last()
            assertEquals(expectedEmittedAction, actual)

            job.cancel()
        }
    }

    @Test
    fun `when handling promo type selection, should set voucher configuration with corresponding promo type data`() {
        runBlockingTest {
            // Given
            val promoType = PromoType.DISCOUNT

            mockVoucherConfigurationInitiation()

            val expectedPromoType = PromoType.DISCOUNT

            val emittedValue = arrayListOf<VoucherCreationStepThreeUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(VoucherCreationStepThreeEvent.ChoosePromoType(promoType))

            // Then
            val actual = emittedValue.last()
            assertEquals(expectedPromoType, actual.voucherConfiguration.promoType)

            job.cancel()
        }
    }

    @Test
    fun `when handling benefit type selection, should set voucher configuration with corresponding benefit type data`() {
        runBlockingTest {
            // Given
            val benefitType = BenefitType.NOMINAL

            mockVoucherConfigurationInitiation()

            val expectedBenefitType = BenefitType.NOMINAL

            val emittedValue = arrayListOf<VoucherCreationStepThreeUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(VoucherCreationStepThreeEvent.ChooseBenefitType(benefitType))

            // Then
            val actual = emittedValue.last()
            assertEquals(expectedBenefitType, actual.voucherConfiguration.benefitType)

            job.cancel()
        }
    }

    private fun mockVoucherConfigurationInitiation() {
        val pageMode = PageMode.CREATE
        val startPeriod = Date().roundTimePerHalfHour().removeTime()

        val voucherConfiguration =
            VoucherConfiguration(startPeriod = startPeriod, isFinishFilledStepTwo = true)

        viewModel.processEvent(
            VoucherCreationStepThreeEvent.InitVoucherConfiguration(
                pageMode,
                voucherConfiguration
            )
        )
        mockGetVoucherCreationMetadataGQLCall()
    }

    private fun mockVoucherValidationPartialGQLCall() {
        val validationResult = mapper.map(VoucherValidationPartialResponse())
        coEvery { voucherValidationPartialUseCase.execute(any()) } returns validationResult
    }

    private fun mockGetVoucherCreationMetadataGQLCall() {
        val voucherCreationMetadataResult = voucherMetadataMapper.map(
            GetInitiateVoucherPageResponse(
                getInitiateVoucherPage = GetInitiateVoucherPageResponse.GetInitiateVoucherPage(
                    data = GetInitiateVoucherPageResponse.GetInitiateVoucherPage.Data(
                        discountActive = false
                    )
                )
            )
        )
        coEvery { getInitiateVoucherPageUseCase.execute(any()) } returns voucherCreationMetadataResult
        mockGetTickerGQLCall()
    }

    private fun mockGetTickerGQLCall() {
        val remoteTickerResult = listOf(RemoteTicker("Title", "Description"))
        coEvery { getTargetedTickerUseCase.execute(any()) } returns remoteTickerResult
    }
}
