package com.tokopedia.product.addedit.shipment.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.logisticCommon.data.mapper.CustomProductLogisticMapper
import com.tokopedia.logisticCommon.data.model.CPLProductModel
import com.tokopedia.logisticCommon.data.model.CustomProductLogisticModel
import com.tokopedia.logisticCommon.data.repository.CustomProductLogisticRepository
import com.tokopedia.logisticCommon.data.response.customproductlogistic.OngkirGetCPLQGLResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.common.util.AddEditProductErrorHandler
import com.tokopedia.product.addedit.common.util.IMSResourceProvider
import com.tokopedia.product.addedit.draft.domain.usecase.SaveProductDraftUseCase
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.shipment.presentation.model.CPLModel
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.product.addedit.variant.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
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

    @RelaxedMockK
    lateinit var resourceProvider: IMSResourceProvider

    private val customProductLogisticMapper: CustomProductLogisticMapper = mockk()

    private val cplListObserver: Observer<Result<CustomProductLogisticModel>> =
        mockk(relaxed = true)

    private val viewModel: AddEditProductShipmentViewModel by lazy {
        AddEditProductShipmentViewModel(
            saveProductDraftUseCase,
            customProductLogisticRepository,
            customProductLogisticMapper,
            resourceProvider,
            CoroutineTestDispatchersProvider)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel.cplList.observeForever(cplListObserver)
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
        viewModel.isAddMode = true
        viewModel.isEditMode = true
        viewModel.isDraftMode = true
        viewModel.isFirstMoved = true

        Assert.assertTrue(viewModel.isAddMode)
        Assert.assertTrue(viewModel.isEditMode)
        Assert.assertTrue(viewModel.isDraftMode)
        Assert.assertTrue(viewModel.isFirstMoved)
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

    @Test
    fun `setProductInputModel should invoke shipmentInputModel and productInputModel changes`() {
        viewModel.setProductInputModel(ProductInputModel(productId = 123L,
            shipmentInputModel = ShipmentInputModel(weight = 100)))
        assertEquals(100, viewModel.shipmentInputModel.getOrAwaitValue().weight)
        assertEquals(123L, viewModel.productInputModel?.productId)
    }

    @Test
    fun `setProductInputModel should invoke shipmentInputModel hasVariant false`() {
        viewModel.setProductInputModel(ProductInputModel())
        assertEquals(false, viewModel.hasVariant.getOrAwaitValue())
    }

    @Test
    fun `setProductInputModel should invoke shipmentInputModel hasVariant true`() {
        viewModel.setProductInputModel(ProductInputModel(variantInputModel = VariantInputModel(
            products = listOf(ProductVariantInputModel())
        )))
        assertEquals(true, viewModel.hasVariant.getOrAwaitValue())
    }

    @Test
    fun `validateWeightInput should return expected value`() {
        coEvery { resourceProvider.getEmptyProductWeightErrorMessage() } returns "empty"
        coEvery { resourceProvider.getMinLimitProductWeightErrorMessage(any()) } returns "min range"
        coEvery { resourceProvider.getMaxLimitProductWeightErrorMessage(any()) } returns "out range"

        val resultEmpty = viewModel.validateWeightInput("")
        val resultZero = viewModel.validateWeightInput("0")
        val resultOutRange = viewModel.validateWeightInput("100.000.000")
        val resultInRange = viewModel.validateWeightInput("1.000")

        viewModel.setProductInputModel(ProductInputModel(variantInputModel = VariantInputModel(
            listOf(ProductVariantInputModel())
        )))
        viewModel.hasVariant.getOrAwaitValue()
        val resultHasVariant = viewModel.validateWeightInput("1.000")

        assertEquals("empty", resultEmpty)
        assertEquals("min range", resultZero)
        assertEquals("out range", resultOutRange)
        assertEquals("", resultInRange)
        assertEquals("", resultHasVariant)
    }

    @Test
    fun `verify when get cpl list custom shipment status is correctly`() {
        val shipmentServicesId = 1L
        val shipmentServicesIds = arrayListOf(shipmentServicesId)
        val cplProduct = spyk(CPLProductModel(shipperServices = shipmentServicesIds))
        val cplProducts = arrayListOf(cplProduct)
        val customProductLogistic = spyk(CustomProductLogisticModel(cplProduct = cplProducts))
        val cplModel = spyk(CPLModel(shipmentServicesIds = shipmentServicesIds))
        val shipmentInputModel = spyk(ShipmentInputModel(cplModel = cplModel))
        val productInputModel = spyk(ProductInputModel(shipmentInputModel = shipmentInputModel))

        coEvery { customProductLogisticRepository.getCPLList(any(), any()) } returns OngkirGetCPLQGLResponse()
        every { customProductLogisticMapper.mapCPLData(any()) } returns customProductLogistic

        viewModel.setProductInputModel(productInputModel)
        viewModel.getCPLList(1234, "9876")

        verify { cplListObserver.onChanged(Success(customProductLogistic)) }
    }

    @Test
    fun `verify when get cpl list standard shipment status is correctly`() {
        val shipmentServicesIds = arrayListOf<Long>()
        val cplProduct = spyk(CPLProductModel(shipperServices = shipmentServicesIds))
        val cplProducts = arrayListOf(cplProduct)
        val customProductLogistic = spyk(CustomProductLogisticModel(cplProduct = cplProducts))
        val cplModel = spyk(CPLModel(shipmentServicesIds = shipmentServicesIds))
        val shipmentInputModel = spyk(ShipmentInputModel(cplModel = cplModel))
        val productInputModel = spyk(ProductInputModel(shipmentInputModel = shipmentInputModel))

        coEvery { customProductLogisticRepository.getCPLList(any(), any()) } returns OngkirGetCPLQGLResponse()
        every { customProductLogisticMapper.mapCPLData(any()) } returns customProductLogistic

        viewModel.setProductInputModel(productInputModel)
        viewModel.getCPLList(1234, "9876")

        verify { cplListObserver.onChanged(Success(customProductLogistic)) }
    }
}