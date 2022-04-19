package com.tokopedia.activation.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.activation.domain.GetShippingEditorUseCase
import com.tokopedia.activation.domain.GetShopFeatureUseCase
import com.tokopedia.activation.domain.UpdateShopFeatureUseCase
import com.tokopedia.activation.model.ActivationPageState
import com.tokopedia.activation.model.ShippingEditorModel
import com.tokopedia.activation.model.ShopFeatureModel
import com.tokopedia.activation.model.UpdateFeatureModel
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ActivationPageViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val getShopFeatureUseCase: GetShopFeatureUseCase = mockk(relaxed = true)
    private val updateShopFeatureUseCase: UpdateShopFeatureUseCase = mockk(relaxed = true)
    private val getShippingEditorUseCase: GetShippingEditorUseCase = mockk(relaxed = true)

    private lateinit var activationPageViewModel: ActivationPageViewModel

    @Before
    fun setUp() {
        activationPageViewModel = ActivationPageViewModel(getShopFeatureUseCase, updateShopFeatureUseCase, getShippingEditorUseCase)
    }

    @Test
    fun `Get Shop Feature Success`() {
        val response = ShopFeatureModel()
        every {
            getShopFeatureUseCase.execute(any(), any(), any())
        } answers {
            assertEquals(ActivationPageState.Loading, activationPageViewModel.shopFeature.value)
            (args[0] as ((ShopFeatureModel) -> Unit)).invoke(response)
        }

        activationPageViewModel.getShopFeature("6550320")

        assertEquals(ActivationPageState.Success(response), activationPageViewModel.shopFeature.value)
    }

    @Test
    fun `Get Shop Feature Failed`() {
        val response = Throwable()
        every {
            getShopFeatureUseCase.execute(any(), any(), any())
        } answers {
            assertEquals(ActivationPageState.Loading, activationPageViewModel.shopFeature.value)
            (args[1] as ((Throwable) -> Unit)).invoke(response)
        }

        activationPageViewModel.getShopFeature("6550320")

        assertEquals(ActivationPageState.Fail(response, ""), activationPageViewModel.shopFeature.value)
    }

    @Test
    fun `Update Shop Feature Success`() {
        val response = UpdateFeatureModel()
        every {
            updateShopFeatureUseCase.execute(any(), any(), any())
        } answers {
            assertEquals(ActivationPageState.Loading, activationPageViewModel.updateShopFeature.value)
            (args[0] as ((UpdateFeatureModel) -> Unit)).invoke(response)
        }

        activationPageViewModel.updateShopFeature(true)

        assertEquals(ActivationPageState.Success(response), activationPageViewModel.updateShopFeature.value)
    }

    @Test
    fun `Update Shop Feature Failed`() {
        val response = Throwable()
        every {
            updateShopFeatureUseCase.execute(any(), any(), any())
        } answers {
            assertEquals(ActivationPageState.Loading, activationPageViewModel.updateShopFeature.value)
            (args[1] as ((Throwable) -> Unit)).invoke(response)
        }

        activationPageViewModel.updateShopFeature(true)

        assertEquals(ActivationPageState.Fail(response, ""), activationPageViewModel.updateShopFeature.value)
    }

    @Test
    fun `Get Activated Shipping Success`() {
        val response = ShippingEditorModel()
        every {
            getShippingEditorUseCase.execute(any(), any(), any())
        } answers {
            assertEquals(ActivationPageState.Loading, activationPageViewModel.activatedShipping.value)
            (args[0] as ((ShippingEditorModel) -> Unit)).invoke(response)
        }

        activationPageViewModel.validateActivatedShipping(123)

        assertEquals(ActivationPageState.Success(response), activationPageViewModel.activatedShipping.value)
    }

    @Test
    fun `Get Activated Shipping Failed`() {
        val response = Throwable()
        every {
            getShippingEditorUseCase.execute(any(), any(), any())
        } answers {
            assertEquals(ActivationPageState.Loading, activationPageViewModel.activatedShipping.value)
            (args[1] as ((Throwable) -> Unit)).invoke(response)
        }

        activationPageViewModel.validateActivatedShipping(123)

        assertEquals(ActivationPageState.Fail(response, ""), activationPageViewModel.activatedShipping.value)
    }
}