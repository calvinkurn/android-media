package com.tokopedia.mvc.presentation.creation.step3

import android.content.SharedPreferences
import com.tokopedia.campaign.entity.RemoteTicker
import com.tokopedia.campaign.usecase.GetTargetedTickerUseCase
import com.tokopedia.campaign.utils.constant.TickerType
import com.tokopedia.mvc.data.mapper.GetInitiateVoucherPageMapper
import com.tokopedia.mvc.data.mapper.VoucherValidationPartialMapper
import com.tokopedia.mvc.data.response.GetInitiateVoucherPageResponse
import com.tokopedia.mvc.data.response.VoucherValidationPartialResponse
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.domain.usecase.GetInitiateVoucherPageUseCase
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
                isFinishFilledStepTwo = true,
                isVoucherPublic = false
            )

            val emittedValue = arrayListOf<VoucherCreationStepThreeUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            mockShopVoucherConfigurationInitiation()

            // Then
            val actual = emittedValue.last()
            assertEquals(expectedVoucherConfiguration, actual.voucherConfiguration)

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
            mockProductVoucherConfigurationInitiation()

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
            mockProductVoucherConfigurationInitiation()
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
            mockShopVoucherConfigurationInitiation()
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

            mockShopVoucherConfigurationInitiation()
            mockVoucherValidationPartialGQLCall()

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

            mockProductVoucherConfigurationInitiation()
            mockVoucherValidationPartialGQLCall()

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

    @Test
    fun `when handling nominal input change, should set voucher configuration with corresponding nominal data`() {
        runBlockingTest {
            // Given
            val nominalInput = 10000L

            mockShopVoucherConfigurationInitiation()
            mockVoucherValidationPartialGQLCall()

            val expectedNominal = 10000L

            val emittedValue = arrayListOf<VoucherCreationStepThreeUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(VoucherCreationStepThreeEvent.OnInputNominalChanged(nominalInput))

            // Then
            val actual = emittedValue.last()
            assertEquals(expectedNominal, actual.voucherConfiguration.benefitIdr)

            job.cancel()
        }
    }

    @Test
    fun `when handling percentage input change, should set voucher configuration with corresponding percentage data`() {
        runBlockingTest {
            // Given
            val percentageInput = 15L

            mockProductVoucherConfigurationInitiation()
            mockVoucherValidationPartialGQLCall()

            val expectedPercentage = 15

            val emittedValue = arrayListOf<VoucherCreationStepThreeUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(VoucherCreationStepThreeEvent.OnInputPercentageChanged(percentageInput))

            // Then
            val actual = emittedValue.last()
            assertEquals(expectedPercentage, actual.voucherConfiguration.benefitPercent)

            job.cancel()
        }
    }

    @Test
    fun `when handling max deduction input change, should set voucher configuration with corresponding max deduction data`() {
        runBlockingTest {
            // Given
            val maxDeductionInput = 10000L

            mockShopVoucherConfigurationInitiation()
            mockVoucherValidationPartialGQLCall()

            val expectedMaxDeduction = 10000L

            val emittedValue = arrayListOf<VoucherCreationStepThreeUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(VoucherCreationStepThreeEvent.OnInputMaxDeductionChanged(maxDeductionInput))

            // Then
            val actual = emittedValue.last()
            assertEquals(expectedMaxDeduction, actual.voucherConfiguration.benefitMax)

            job.cancel()
        }
    }

    @Test
    fun `when handling minimum purchase input change, should set voucher configuration with corresponding minimum purchase data`() {
        runBlockingTest {
            // Given
            val minPurchaseInput = 5000L

            mockShopVoucherConfigurationInitiation()
            mockVoucherValidationPartialGQLCall()

            val expectedMinPurchase = 5000L

            val emittedValue = arrayListOf<VoucherCreationStepThreeUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(VoucherCreationStepThreeEvent.OnInputMinimumBuyChanged(minPurchaseInput))

            // Then
            val actual = emittedValue.last()
            assertEquals(expectedMinPurchase, actual.voucherConfiguration.minPurchase)

            job.cancel()
        }
    }

    @Test
    fun `when handling quota input change, should set voucher configuration with corresponding quota data`() {
        runBlockingTest {
            // Given
            val quotaInput = 100L

            mockProductVoucherConfigurationInitiation()
            mockVoucherValidationPartialGQLCall()

            val expectedQuota = 100L

            val emittedValue = arrayListOf<VoucherCreationStepThreeUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(VoucherCreationStepThreeEvent.OnInputQuotaChanged(quotaInput))

            // Then
            val actual = emittedValue.last()
            assertEquals(expectedQuota, actual.voucherConfiguration.quota)

            job.cancel()
        }
    }

    @Test
    fun `when handling target buyer selection, should set voucher configuration with corresponding target buyer data`() {
        runBlockingTest {
            // Given
            val targetBuyerInput = VoucherTargetBuyer.ALL_BUYER

            mockShopVoucherConfigurationInitiation()
            mockVoucherValidationPartialGQLCall()

            val expectedTargetBuyer = VoucherTargetBuyer.ALL_BUYER

            val emittedValue = arrayListOf<VoucherCreationStepThreeUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(VoucherCreationStepThreeEvent.ChooseTargetBuyer(targetBuyerInput))

            // Then
            val actual = emittedValue.last()
            assertEquals(expectedTargetBuyer, actual.voucherConfiguration.targetBuyer)

            job.cancel()
        }
    }

    @Test
    fun `when handleCoachmark() is called, should emit the correct action accordingly`() {
        runBlockingTest {
            // Given
            val expectedEmittedAction = VoucherCreationStepThreeAction.ShowCoachmark

            viewModel.setSharedPrefCoachMarkAlreadyShown()

            val emittedAction = arrayListOf<VoucherCreationStepThreeAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedAction)
            }

            // When
            viewModel.processEvent(VoucherCreationStepThreeEvent.HandleCoachMark)

            // Then
            val actual = emittedAction.last()
            assertEquals(expectedEmittedAction, actual)

            job.cancel()
        }
    }

    @Test
    fun `when reseting input, should set nominal, percentage, max deduction, min purchase and quota back to zero`() {
        runBlockingTest {
            // Given
            val expectedVoucherConfiguration = VoucherConfiguration()

            mockProductVoucherConfigurationInitiation()
            viewModel.processEvent(VoucherCreationStepThreeEvent.ChoosePromoType(PromoType.FREE_SHIPPING))
            viewModel.processEvent(VoucherCreationStepThreeEvent.OnInputNominalChanged(1000L))
            viewModel.processEvent(VoucherCreationStepThreeEvent.OnInputPercentageChanged(10))
            viewModel.processEvent(VoucherCreationStepThreeEvent.OnInputMinimumBuyChanged(500))
            viewModel.processEvent(VoucherCreationStepThreeEvent.OnInputMaxDeductionChanged(850))
            viewModel.processEvent(VoucherCreationStepThreeEvent.OnInputQuotaChanged(100))

            val emittedValue = arrayListOf<VoucherCreationStepThreeUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(VoucherCreationStepThreeEvent.ResetInput)

            // Then
            val actual = emittedValue.last()
            assertEquals(expectedVoucherConfiguration.promoType, actual.voucherConfiguration.promoType)
            assertEquals(expectedVoucherConfiguration.benefitIdr, actual.voucherConfiguration.benefitIdr)
            assertEquals(expectedVoucherConfiguration.benefitPercent, actual.voucherConfiguration.benefitPercent)
            assertEquals(expectedVoucherConfiguration.minPurchase, actual.voucherConfiguration.minPurchase)
            assertEquals(expectedVoucherConfiguration.benefitMax, actual.voucherConfiguration.benefitMax)
            assertEquals(expectedVoucherConfiguration.quota, actual.voucherConfiguration.quota)

            job.cancel()
        }
    }

    private fun mockProductVoucherConfigurationInitiation() {
        val pageMode = PageMode.EDIT
        val startPeriod = Date().roundTimePerHalfHour().removeTime()

        val voucherConfiguration =
            VoucherConfiguration(
                startPeriod = startPeriod,
                isFinishFilledStepTwo = true,
                isVoucherProduct = true,
                promoType = PromoType.DISCOUNT
            )

        viewModel.processEvent(
            VoucherCreationStepThreeEvent.InitVoucherConfiguration(
                pageMode,
                voucherConfiguration
            )
        )
        mockGetVoucherCreationMetadataGQLCall()
    }

    private fun mockShopVoucherConfigurationInitiation() {
        val pageMode = PageMode.EDIT
        val startPeriod = Date().roundTimePerHalfHour().removeTime()

        val voucherConfiguration =
            VoucherConfiguration(
                startPeriod = startPeriod,
                isFinishFilledStepTwo = true,
                isVoucherProduct = false,
                isVoucherPublic = false
            )

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
        val remoteTickerResult = listOf(
            RemoteTicker(
                title = "some ticker title",
                description = "some ticker description",
                type = TickerType.INFO,
                actionLabel = "some ticker action label",
                actionType = "link",
                actionAppUrl = "https://tokopedia.com"
            )
        )
        coEvery { getTargetedTickerUseCase.execute(any()) } returns remoteTickerResult
    }
}
