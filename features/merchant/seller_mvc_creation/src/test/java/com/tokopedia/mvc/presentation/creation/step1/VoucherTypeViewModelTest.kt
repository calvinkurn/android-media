package com.tokopedia.mvc.presentation.creation.step1

import android.content.SharedPreferences
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.VoucherCreationMetadata
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherAction
import com.tokopedia.mvc.domain.usecase.GetInitiateVoucherPageUseCase
import com.tokopedia.mvc.presentation.creation.step1.uimodel.VoucherCreationStepOneAction
import com.tokopedia.mvc.presentation.creation.step1.uimodel.VoucherCreationStepOneEvent
import com.tokopedia.mvc.presentation.creation.step1.uimodel.VoucherCreationStepOneUiState
import com.tokopedia.network.exception.MessageErrorException
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
import java.util.Date

@ExperimentalCoroutinesApi
class VoucherTypeViewModelTest {

    private lateinit var viewModel: VoucherTypeViewModel

    @RelaxedMockK
    lateinit var getInitiateVoucherPageUseCase: GetInitiateVoucherPageUseCase

    @RelaxedMockK
    lateinit var sharedPreference: SharedPreferences

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = VoucherTypeViewModel(
            CoroutineTestDispatchers,
            getInitiateVoucherPageUseCase,
            sharedPreference
        )
    }

//    @Test
//    fun `when initVoucherConfiguration is called, should set voucherConfiguration data accordingly`() {
//        runBlockingTest {
//            // Given
//            val pageMode = PageMode.CREATE
//            val voucherConfiguration = VoucherConfiguration(voucherName = "voucher")
//            val expectedVoucherConfiguration = VoucherConfiguration(voucherName = "voucher")
//
//            val emittedValue = arrayListOf<VoucherCreationStepOneUiState>()
//            val job = launch {
//                viewModel.uiState.toList(emittedValue)
//            }
//
//            // When
//            viewModel.processEvent(
//                VoucherCreationStepOneEvent.InitVoucherConfiguration(
//                    pageMode = pageMode,
//                    voucherConfiguration = voucherConfiguration
//                )
//            )
//
//            // Then
//            val actual = emittedValue.last()
//            assertEquals(expectedVoucherConfiguration, actual.voucherConfiguration)
//
//            job.cancel()
//        }
//    }

//    @Test
    fun `when handling voucher type selection, should set the voucher configuration data accordingly if the status is eligible`() {
        runBlockingTest {
            // Given
            val pageMode = PageMode.CREATE
            val action = VoucherAction.CREATE
            val voucherPrefix = "OFC"
            val isVoucherProduct = true
            val startPeriod = Date().removeTime()
            val expectedVoucherConfiguration = VoucherConfiguration(
                voucherCodePrefix = voucherPrefix,
                isVoucherProduct = isVoucherProduct,
                startPeriod = startPeriod
            )

            mockInitiateVoucherPageGqlCallWithEligibleResult(action)

            val emittedValue = arrayListOf<VoucherCreationStepOneUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(
                VoucherCreationStepOneEvent.ChooseVoucherType(
                    pageMode = pageMode,
                    isVoucherProduct = isVoucherProduct
                )
            )

            // Then
            val actual = emittedValue.last()
            assertEquals(
                expectedVoucherConfiguration,
                actual.voucherConfiguration.copy(startPeriod = startPeriod)
            )

            job.cancel()
        }
    }

    @Test
    fun `when get initiate voucher page data is failed, should return error`() {
        runBlockingTest {
            // Given
            val pageMode = PageMode.CREATE
            val action = VoucherAction.CREATE
            val isVoucherProduct = true
            val error = MessageErrorException("Server Error")

            val initiateVoucherPageParam = GetInitiateVoucherPageUseCase.Param(
                action = action,
                promoType = PromoType.FREE_SHIPPING,
                isVoucherProduct = true
            )
            coEvery { getInitiateVoucherPageUseCase.execute(initiateVoucherPageParam) } throws error

            val emittedValue = arrayListOf<VoucherCreationStepOneUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(
                VoucherCreationStepOneEvent.ChooseVoucherType(
                    pageMode = pageMode,
                    isVoucherProduct = isVoucherProduct
                )
            )

            // Then
            val actual = emittedValue.last()
            assertEquals(error, actual.error)

            job.cancel()
        }
    }

    @Test
    fun `when handling voucher type selection, should show ineligible state if the status is ineligible`() {
        runBlockingTest {
            // Given
            val pageMode = PageMode.CREATE
            val action = VoucherAction.CREATE
            val isVoucherProduct = true
            val errorMsg = ""
            val expectedEmittedAction =
                VoucherCreationStepOneAction.ShowIneligibleState(isVoucherProduct, errorMsg)

            mockInitiateVoucherPageGqlCallWithIneligibleResult(action)

            val emittedAction = arrayListOf<VoucherCreationStepOneAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedAction)
            }

            // When
            viewModel.processEvent(
                VoucherCreationStepOneEvent.ChooseVoucherType(
                    pageMode = pageMode,
                    isVoucherProduct = isVoucherProduct
                )
            )

            // Then
            val actual = emittedAction.last()
            assertEquals(expectedEmittedAction, actual)

            job.cancel()
        }
    }

    @Test
    fun `when handleCoachmark() is called, should emit the correct action accordingly`() {
        runBlockingTest {
            // Given
            val expectedEmittedAction = VoucherCreationStepOneAction.ShowCoachmark

            viewModel.setSharedPrefCoachMarkAlreadyShown()

            val emittedAction = arrayListOf<VoucherCreationStepOneAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedAction)
            }

            // When
            viewModel.processEvent(VoucherCreationStepOneEvent.HandleCoachmark)

            // Then
            val actual = emittedAction.last()
            assertEquals(expectedEmittedAction, actual)

            job.cancel()
        }
    }

    @Test
    fun `when navigateToNextStep() is called, should emit the correct action accordingly`() {
        runBlockingTest {
            // Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = generateDummyVoucherConfiguration()
            val expectedEmittedAction =
                VoucherCreationStepOneAction.NavigateToNextStep(pageMode, voucherConfiguration)

            viewModel.processEvent(
                VoucherCreationStepOneEvent.InitVoucherConfiguration(
                    pageMode = pageMode,
                    voucherConfiguration = voucherConfiguration
                )
            )

            val emittedAction = arrayListOf<VoucherCreationStepOneAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedAction)
            }

            // When
            viewModel.processEvent(VoucherCreationStepOneEvent.NavigateToNextStep)

            // Then
            val actual = emittedAction.last()
            assertEquals(expectedEmittedAction, actual)

            job.cancel()
        }
    }

    @Test
    fun `when resetVoucherCreationStepOneData() is called, should set all the navigation flagging field data to false`() {
        runBlockingTest {
            // Given
            val expectedVoucherConfiguration =
                generateDummyVoucherConfiguration().copy(
                    isFinishFilledStepOne = false,
                    isFinishFilledStepTwo = false,
                    isFinishFilledStepThree = false
                )

            val emittedValue = arrayListOf<VoucherCreationStepOneUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(VoucherCreationStepOneEvent.resetFillState)

            // Then
            val actual = emittedValue.last()
            assertEquals(
                expectedVoucherConfiguration.isFinishFilledStepOne,
                actual.voucherConfiguration.isFinishFilledStepOne
            )
            assertEquals(
                expectedVoucherConfiguration.isFinishFilledStepTwo,
                actual.voucherConfiguration.isFinishFilledStepTwo
            )
            assertEquals(
                expectedVoucherConfiguration.isFinishFilledStepThree,
                actual.voucherConfiguration.isFinishFilledStepThree
            )

            job.cancel()
        }
    }

    private fun mockInitiateVoucherPageGqlCallWithEligibleResult(action: VoucherAction) {
        val initiateVoucherPageParam = GetInitiateVoucherPageUseCase.Param(
            action = action,
            promoType = PromoType.FREE_SHIPPING,
            isVoucherProduct = true
        )
        val initiateVoucherResponse = VoucherCreationMetadata(
            accessToken = "accessToken",
            isEligible = 1,
            maxProduct = 100,
            prefixVoucherCode = "OFC",
            shopId = 1,
            token = "token",
            userId = 1,
            discountActive = true,
            message = ""
        )
        coEvery { getInitiateVoucherPageUseCase.execute(initiateVoucherPageParam) } returns initiateVoucherResponse
    }

    private fun mockInitiateVoucherPageGqlCallWithIneligibleResult(action: VoucherAction) {
        val initiateVoucherPageParam = GetInitiateVoucherPageUseCase.Param(
            action = action,
            promoType = PromoType.FREE_SHIPPING,
            isVoucherProduct = true
        )
        val initiateVoucherResponse = VoucherCreationMetadata(
            accessToken = "accessToken",
            isEligible = 0,
            maxProduct = 100,
            prefixVoucherCode = "OFC",
            shopId = 1,
            token = "token",
            userId = 1,
            discountActive = true,
            message = ""
        )
        coEvery { getInitiateVoucherPageUseCase.execute(initiateVoucherPageParam) } returns initiateVoucherResponse
    }

    private fun generateDummyVoucherConfiguration(): VoucherConfiguration {
        return VoucherConfiguration(startPeriod = Date().removeTime())
    }
}
