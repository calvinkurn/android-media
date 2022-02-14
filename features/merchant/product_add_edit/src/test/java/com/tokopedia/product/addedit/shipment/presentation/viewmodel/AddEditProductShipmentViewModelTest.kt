package com.tokopedia.product.addedit.shipment.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.logisticCommon.data.mapper.CustomProductLogisticMapper
import com.tokopedia.logisticCommon.data.model.CustomProductLogisticModel
import com.tokopedia.logisticCommon.data.repository.CustomProductLogisticRepository
import com.tokopedia.logisticCommon.data.response.customproductlogistic.OngkirGetCPLQGLResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.common.util.AddEditProductErrorHandler
import com.tokopedia.product.addedit.draft.domain.usecase.SaveProductDraftUseCase
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddEditProductShipmentViewModelTest {

    @get:Rule
    val instantTaskExcecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var saveProductDraftUseCase: SaveProductDraftUseCase

    @RelaxedMockK
    lateinit var customProductLogisticRepository: CustomProductLogisticRepository

    private val customProductLogisticMapper: CustomProductLogisticMapper = mockk()

    private val cplListObserver: Observer<Result<CustomProductLogisticModel>> =
        mockk(relaxed = true)

    private val viewModel: AddEditProductShipmentViewModel by lazy {
        AddEditProductShipmentViewModel(saveProductDraftUseCase, customProductLogisticRepository,
            customProductLogisticMapper, CoroutineTestDispatchersProvider)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel.cplList.observeForever(cplListObserver)
    }

    @Test
    fun `isWeightValid should valid when unit is gram and weight is in allowed range`() {
        val isValid = viewModel.isWeightValid(AddEditProductShipmentConstants.MIN_WEIGHT.toString(), AddEditProductShipmentConstants.UNIT_GRAM)
        Assert.assertTrue(isValid)
    }

    @Test
    fun `isWeightValid should valid when unit is kg and weight is in allowed range`() {
        val isValid = viewModel.isWeightValid(AddEditProductShipmentConstants.MIN_WEIGHT.toString(), AddEditProductShipmentConstants.UNIT_KILOGRAM)
        Assert.assertTrue(isValid)
    }

    @Test
    fun `isWeightValid should invalid when unit is gram and weight isn't in allowed range`() {
        var isValid = viewModel.isWeightValid("${AddEditProductShipmentConstants.MIN_WEIGHT - 1}", AddEditProductShipmentConstants.MAX_WEIGHT_GRAM)
        Assert.assertFalse(isValid)

        isValid = viewModel.isWeightValid("${AddEditProductShipmentConstants.MAX_WEIGHT_GRAM + 1}", AddEditProductShipmentConstants.MAX_WEIGHT_GRAM)
        Assert.assertFalse(isValid)
    }

    @Test
    fun `isWeightValid should valid when unit is kg and weight isn't in allowed range`() {
        var isValid = viewModel.isWeightValid("${AddEditProductShipmentConstants.MIN_WEIGHT - 1}", AddEditProductShipmentConstants.UNIT_KILOGRAM)
        Assert.assertFalse(isValid)

        isValid = viewModel.isWeightValid("${AddEditProductShipmentConstants.MAX_WEIGHT_KILOGRAM + 1}", AddEditProductShipmentConstants.UNIT_KILOGRAM)
        Assert.assertFalse(isValid)
    }

    @Test
    fun `When save and get product draft are success Expect can be saved and retrieved data draft`() = runBlocking {
        val draftIdResult = 1L
        val productInputModel = ProductInputModel().apply {
            productId = 220
        }

        coEvery { saveProductDraftUseCase.executeOnBackground() } returns draftIdResult
        viewModel.saveProductDraft(productInputModel)
        coVerify { saveProductDraftUseCase.executeOnBackground() }
    }

    @Test
    fun `When save product error, should log error to crashlytics`() {
        coEvery { saveProductDraftUseCase.executeOnBackground() } throws MessageErrorException("")

        //Mock FirebaseCrashlytics because .getInstance() method is a static method
        mockkStatic(FirebaseCrashlytics::class)

        every { FirebaseCrashlytics.getInstance().recordException(any()) } returns mockk(relaxed = true)

        val productInputModel = ProductInputModel().apply {
            productId = 220
        }

        viewModel.saveProductDraft(productInputModel)

        coVerify { saveProductDraftUseCase.executeOnBackground() }

        coVerify { AddEditProductErrorHandler.logExceptionToCrashlytics(any()) }
    }


    @Test
    fun `when all boolean variables should return true and object should return the same object`() {
        val shipmentInputModel = ShipmentInputModel(
                weight = 10,
                weightUnit = 12,
                isMustInsurance = true
        )
        viewModel.isAddMode = true
        viewModel.isEditMode = true
        viewModel.isDraftMode = true
        viewModel.isFirstMoved = true
        viewModel.shipmentInputModel = shipmentInputModel

        Assert.assertTrue(viewModel.isAddMode)
        Assert.assertTrue(viewModel.isEditMode)
        Assert.assertTrue(viewModel.isDraftMode)
        Assert.assertTrue(viewModel.isFirstMoved)
        Assert.assertTrue(viewModel.shipmentInputModel == shipmentInputModel)
    }

    @Test
    fun `Get CPL List success`() {
        val testData = CustomProductLogisticModel()

        coEvery {
            customProductLogisticRepository.getCPLList(any(), any())
        } returns OngkirGetCPLQGLResponse()
        every {
            customProductLogisticMapper.mapCPLData(OngkirGetCPLQGLResponse().response.data)
        } returns testData
        viewModel.getCPLList(1234, "9876")
        verify { cplListObserver.onChanged(Success(testData)) }
    }

    @Test
    fun `Get CPL List failed`() {
        val testError = Throwable("test error")
        coEvery {
            customProductLogisticRepository.getCPLList(any(), any())
        } throws testError
        viewModel.getCPLList(1234, "9876")
        verify {
            cplListObserver.onChanged(Fail(testError))
            customProductLogisticMapper wasNot Called
        }
    }
}