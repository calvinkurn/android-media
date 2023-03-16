package com.tokopedia.product.addedit.shipment.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.logisticCommon.data.mapper.CustomProductLogisticMapper
import com.tokopedia.logisticCommon.data.model.CustomProductLogisticModel
import com.tokopedia.logisticCommon.data.repository.CustomProductLogisticUseCase
import com.tokopedia.logisticCommon.data.response.customproductlogistic.OngkirGetCPLQGLResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.common.util.AddEditProductErrorHandler
import com.tokopedia.product.addedit.common.util.IMSResourceProvider
import com.tokopedia.product.addedit.draft.domain.usecase.SaveProductDraftUseCase
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
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
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
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
    lateinit var customProductLogisticUseCase: CustomProductLogisticUseCase

    @RelaxedMockK
    lateinit var resourceProvider: IMSResourceProvider

    private val customProductLogisticMapper: CustomProductLogisticMapper =
        CustomProductLogisticMapper()

    private val cplListObserver: Observer<Result<CustomProductLogisticModel>> =
        mockk(relaxed = true)

    private val viewModel: AddEditProductShipmentViewModel by lazy {
        AddEditProductShipmentViewModel(
            saveProductDraftUseCase,
            customProductLogisticUseCase,
            customProductLogisticMapper,
            resourceProvider,
            CoroutineTestDispatchersProvider
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel.cplList.observeForever(cplListObserver)
    }

    @Test
    fun `When save and get product draft are success Expect can be saved and retrieved data draft`() =
        runBlocking {
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

        every {
            FirebaseCrashlytics.getInstance().recordException(any())
        } returns mockk(relaxed = true)

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
        val cplParam = listOf<Long>(6, 22)
        coEvery {
            customProductLogisticUseCase(any())
        } returns OngkirGetCPLQGLResponse()
        viewModel.getCPLList(1234, 9876, null, cplParam)
        verify { cplListObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get CPL List failed`() {
        val testError = Throwable("test error")
        coEvery {
            customProductLogisticUseCase(any())
        } throws testError
        viewModel.getCPLList(1234, 9876, null, null)
        verify {
            cplListObserver.onChanged(Fail(testError))
        }
    }

    @Test
    fun `setProductInputModel should invoke shipmentInputModel and productInputModel changes`() {
        viewModel.setProductInputModel(
            ProductInputModel(
                productId = 123L,
                shipmentInputModel = ShipmentInputModel(weight = 100)
            )
        )
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
        viewModel.setProductInputModel(
            ProductInputModel(
                variantInputModel = VariantInputModel(
                    products = listOf(ProductVariantInputModel())
                )
            )
        )
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

        viewModel.setProductInputModel(
            ProductInputModel(
                variantInputModel = VariantInputModel(
                    listOf(ProductVariantInputModel())
                )
            )
        )
        viewModel.hasVariant.getOrAwaitValue()
        val resultHasVariant = viewModel.validateWeightInput("1.000")

        assertEquals("empty", resultEmpty)
        assertEquals("min range", resultZero)
        assertEquals("out range", resultOutRange)
        assertEquals("", resultInRange)
        assertEquals("", resultHasVariant)
    }

    @Test
    fun `setAllCPLProductActiveState needs to activate all CPL shipper when user choose shipment radio button`() {
        val mockResponse = CPLDataProvider.provideCPLResponse()
        val isCPLActivated = true
        coEvery {
            customProductLogisticUseCase(any())
        } returns mockResponse
        viewModel.getCPLList(1234, 9876, null, null)

        viewModel.setAllCPLProductActiveState(isCPLActivated)

        val result = (viewModel.cplList.value as Success).data.shipperList
        assertEquals(result.all { it.shipper.all { s -> s.isActive } }, isCPLActivated)


    }

    @Test
    fun `setAllCPLProductActiveState does nothing when failed to get CPL data`() {
        val testError = Throwable("test error")
        coEvery {
            customProductLogisticUseCase(any())
        } throws testError
        viewModel.getCPLList(1234, 9876, null, null)

        viewModel.setAllCPLProductActiveState(true)

        Assert.assertFalse(viewModel.cplList.value is Success)
    }

    @Test
    fun `setProductActiveState needs to refresh CPL shipper state based on shipper ids from CPL page`() {
        val mockResponse = CPLDataProvider.provideCPLResponse()
        val activatedSpIds = listOf<Long>(18)
        coEvery {
            customProductLogisticUseCase(any())
        } returns mockResponse
        viewModel.getCPLList(1234, 9876, null, null)

        viewModel.setProductActiveState(activatedSpIds)

        val activatedShipperProduct =
            (viewModel.cplList.value as Success).data.shipperList[0].shipper[1].shipperProduct[0]
        val notActivatedShipperProduct =
            (viewModel.cplList.value as Success).data.shipperList[0].shipper[2].shipperProduct[0]
        assertEquals(activatedShipperProduct.isActive, true)
        assertEquals(notActivatedShipperProduct.isActive, false)
    }

    @Test
    fun `setProductActiveState does nothing if failed to get CPL data`() {
        val testError = Throwable("test error")
        coEvery {
            customProductLogisticUseCase(any())
        } throws testError
        viewModel.getCPLList(1234, 9876, null, null)
        val activatedSpIds = listOf<Long>(18)

        viewModel.setProductActiveState(activatedSpIds)

        Assert.assertFalse(viewModel.cplList.value is Success)
    }

    @Test
    fun `setAlreadyShowOnBoarding should set onboarding flag to false`() {
        val mockResponse = CPLDataProvider.provideCPLResponse()
        val activatedSpIds = listOf<Long>(18)
        coEvery {
            customProductLogisticUseCase(any())
        } returns mockResponse
        viewModel.getCPLList(1234, 9876, null, null)

        viewModel.setAlreadyShowOnBoarding()

        val result = (viewModel.cplList.value as Success).data.shouldShowOnBoarding
        assertFalse(result)
    }

    @Test
    fun `setAlreadyShowOnBoarding does nothing if failed to get cpl data`() {
        val testError = Throwable("test error")
        coEvery {
            customProductLogisticUseCase(any())
        } throws testError
        viewModel.getCPLList(1234, 9876, null, null)

        viewModel.setAlreadyShowOnBoarding()

        Assert.assertFalse(viewModel.cplList.value is Success)
    }
}
