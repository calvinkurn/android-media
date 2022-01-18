package com.tokopedia.product_ar.viewmodel

import android.graphics.Color
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.modiface.mfemakeupkit.effects.MFEMakeupProduct
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.product_ar.model.state.AnimatedTextIconClickMode
import com.tokopedia.product_ar.model.state.ArGlobalErrorMode
import com.tokopedia.product_ar.model.state.ModifaceViewMode
import com.tokopedia.product_ar.usecase.GetProductArUseCase
import com.tokopedia.product_ar.util.ProductArAssertAssistant
import com.tokopedia.product_ar.util.TestUtil
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductArViewModelTest {

    @RelaxedMockK
    private lateinit var addToCartUseCase: AddToCartUseCase

    @RelaxedMockK
    private lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    private lateinit var chosenAddressRequestHelper: ChosenAddressRequestHelper

    @RelaxedMockK
    private lateinit var getProductArUseCase: GetProductArUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val gson = Gson()
    private val assertAssistant = ProductArAssertAssistant()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    companion object {
        private const val INITIAL_PRODUCT_ID = "2684167590"
        private const val INITIAL_SHOP_ID = "123"
    }

    private val viewModel by lazy {
        ProductArViewModel(
                dispatchers = CoroutineTestDispatchersProvider,
                initialProductId = INITIAL_PRODUCT_ID,
                shopId = INITIAL_SHOP_ID,
                addToCartUseCase = addToCartUseCase,
                chosenAddressRequestHelper = chosenAddressRequestHelper,
                getProductArUseCase = getProductArUseCase,
                userSessionInterface = userSessionInterface
        )
    }

    @Test
    fun `success get ar data`() {
        val mockData = TestUtil.getProductArDataMock(gson)
        val bottomLoadingState = viewModel.bottomLoadingState
        val animatedTextIconState = viewModel.animatedTextIconState.value
        val modifaceLoadingState = viewModel.modifaceLoadingState.value

        coEvery {
            getProductArUseCase.executeOnBackground(any())
        } returns mockData

        Assert.assertEquals(bottomLoadingState.value, true)
        Assert.assertEquals(animatedTextIconState.view1ClickMode, null)
        Assert.assertEquals(animatedTextIconState.view2ClickMode, null)
        Assert.assertEquals(modifaceLoadingState, true)

        viewModel.getArData()

        coVerify { getProductArUseCase.executeOnBackground(any()) }
        Assert.assertNotNull(viewModel.userSessionInterface)
    }

    @Test
    fun `fail get ar data`() {
        coEvery {
            getProductArUseCase.executeOnBackground(any())
        } throws Throwable()

        viewModel.getArData()

        coVerify { getProductArUseCase.executeOnBackground(any()) }

        val globalState = viewModel.globalErrorState
        Assert.assertTrue(globalState.value.state == ArGlobalErrorMode.ERROR)
    }

    @Test
    fun `success get initial data`() {
        `success get ar data`()

        val animatedTextIconState = viewModel.animatedTextIconState.value
        Assert.assertEquals(animatedTextIconState.view1ClickMode, null)
        Assert.assertEquals(animatedTextIconState.view2ClickMode,
                AnimatedTextIconClickMode.CHOOSE_FROM_GALLERY)

        val globalState = viewModel.globalErrorState
        Assert.assertTrue(globalState.value.state == ArGlobalErrorMode.SUCCESS)

        val bottomLoadingState = viewModel.bottomLoadingState
        Assert.assertEquals(bottomLoadingState.value, false)

        val arListData = (viewModel.productArList.value as Success).data
        assertAssistant.assertMakeUpLook(arListData)
        assertAssistant.assertSelectedModifaceUiModel(
                data = arListData,
                expectedProductId = INITIAL_PRODUCT_ID,
                expectedBackgroundUrl = "https://images.tokopedia.net/img/pdp/icons/ar/bglipcolor.png",
                expectedProductName = "Merah",
                expectedModifaceType = "lipcolor"
        )

        val selectedMfMakeUpLook = viewModel.selectedMfMakeUpLook.value
        Assert.assertNotNull(selectedMfMakeUpLook)
        val colorRgb = Color.argb(255, 255, 0, 0)
        assertAssistant.assertSelectedModifaceLook(selectedMfMakeUpLook!!,
                colorRgb)

        val selectedProductArData = viewModel.selectedProductArData.value
        Assert.assertNotNull(selectedProductArData)
        assertAssistant.assertSelectedArData(
                data = selectedProductArData!!,
                expectedProductId = INITIAL_PRODUCT_ID,
                expectedName = "Merah",
                expectedPrice = 50000.0,
                expectedUnavailableCopy = "",
                expectedButtonText = "+ Keranjang",
                expectedButtonType = "ar_atc",
                expectedButtonColor = "primary_green",
                expectedCampaignActive = false
        )

        viewModel.setLoadingState(false)
        val modifaceLoadingState = viewModel.modifaceLoadingState.value
        Assert.assertEquals(modifaceLoadingState, false)
    }

    @Test
    fun `fail get initial data`() {
        `fail get ar data`()

        val animatedTextIconState = viewModel.animatedTextIconState.value
        Assert.assertEquals(animatedTextIconState.view1ClickMode, null)
        Assert.assertEquals(animatedTextIconState.view2ClickMode, null)

        val globalState = viewModel.globalErrorState
        Assert.assertTrue(globalState.value.state == ArGlobalErrorMode.ERROR)

        val bottomLoadingState = viewModel.bottomLoadingState
        Assert.assertEquals(bottomLoadingState.value, true)

        Assert.assertTrue(viewModel.productArList.value == null)

        val selectedMfMakeUpLook = viewModel.selectedMfMakeUpLook.value
        Assert.assertEquals(selectedMfMakeUpLook, null)

        val selectedProductArData = viewModel.selectedProductArData.value
        Assert.assertTrue(selectedProductArData == null)
    }

    @Test
    fun `success change variant to product unavailable`() {
        `success get initial data`()

        viewModel.onVariantClicked(
                productId = "2684217811", //ungu
                uiModel = viewModel.getProductArUiModel(),
                currentList = (viewModel.productArList.value as Success).data,
                mfeMakeUpProduct = viewModel.selectedMfMakeUpLook.value?.lipLayers?.firstOrNull()?.product
                        ?: MFEMakeupProduct()
        )

        val selectedMfMakeUpLook = viewModel.selectedMfMakeUpLook.value
        Assert.assertNotNull(selectedMfMakeUpLook)
        val colorRgb = Color.argb(255, 191, 0, 255)
        assertAssistant.assertSelectedModifaceLook(selectedMfMakeUpLook!!,
                colorRgb)

        val arListData = (viewModel.productArList.value as Success).data
        Assert.assertNotNull(arListData)
        assertAssistant.assertSelectedModifaceUiModel(
                data = arListData,
                expectedProductId = "2684217811",
                expectedBackgroundUrl = "https://images.tokopedia.net/img/pdp/icons/ar/bglipcolor.png",
                expectedProductName = "Ungu",
                expectedModifaceType = "lipcolor"
        )

        val selectedProductArData = viewModel.selectedProductArData.value
        Assert.assertNotNull(selectedProductArData)
        assertAssistant.assertSelectedArData(
                data = selectedProductArData!!,
                expectedProductId = "2684217811",
                expectedName = "Ungu",
                expectedPrice = 60000.0,
                expectedUnavailableCopy = "Barang ini sedang tidak dijual untuk sementara waktu.",
                expectedButtonText = "Barang Tidak Tersedia",
                expectedButtonType = "default_oos",
                expectedButtonColor = "disabled",
                expectedCampaignActive = false
        )
    }

    @Test
    fun `success change variant to product campaign`() {
        `success get initial data`()

        viewModel.onVariantClicked(
                productId = "2684217809", //Orange
                uiModel = viewModel.getProductArUiModel(),
                currentList = (viewModel.productArList.value as Success).data,
                mfeMakeUpProduct = viewModel.selectedMfMakeUpLook.value?.lipLayers?.firstOrNull()?.product
                        ?: MFEMakeupProduct()
        )

        val selectedMfMakeUpLook = viewModel.selectedMfMakeUpLook.value
        Assert.assertNotNull(selectedMfMakeUpLook)
        val colorRgb = Color.argb(255, 191, 0, 255)
        assertAssistant.assertSelectedModifaceLook(selectedMfMakeUpLook!!,
                colorRgb)

        val arListData = (viewModel.productArList.value as Success).data
        Assert.assertNotNull(arListData)
        assertAssistant.assertSelectedModifaceUiModel(
                data = arListData,
                expectedProductId = "2684217809",
                expectedBackgroundUrl = "https://images.tokopedia.net/img/pdp/icons/ar/bglipcolor.png",
                expectedProductName = "Orange",
                expectedModifaceType = "lipcolor"
        )

        val selectedProductArData = viewModel.selectedProductArData.value
        Assert.assertNotNull(selectedProductArData)
        assertAssistant.assertSelectedArData(
                data = selectedProductArData!!,
                expectedProductId = "2684217809",
                expectedName = "Orange",
                expectedPrice = 100.0,
                expectedUnavailableCopy = "",
                expectedButtonText = "+ Keranjang",
                expectedButtonType = "ar_atc",
                expectedButtonColor = "primary_green",
                expectedCampaignActive = true
        )
    }

    @Test
    fun `success change mode to image`() {
        `success get initial data`()

        viewModel.changeMode(
                modifaceViewMode = ModifaceViewMode.IMAGE,
                pathImageDrawable = "path_image"
        )

        val modifaceViewState = viewModel.modifaceViewState.value
        Assert.assertEquals(modifaceViewState.mode, ModifaceViewMode.IMAGE)
        Assert.assertEquals(modifaceViewState.imageDrawablePath, "path_image")

        val animatedTextIconState = viewModel.animatedTextIconState.value
        Assert.assertEquals(animatedTextIconState.view1ClickMode, AnimatedTextIconClickMode.USE_CAMERA)
        Assert.assertEquals(animatedTextIconState.view2ClickMode, AnimatedTextIconClickMode.CHANGE_PHOTO)
    }

    @Test
    fun `success change mode to live`() {
        `success change mode to image`()

        viewModel.changeMode(
                modifaceViewMode = ModifaceViewMode.LIVE
        )

        val modifaceLoadingState = viewModel.modifaceLoadingState.value
        Assert.assertEquals(modifaceLoadingState, true)

        val modifaceViewState = viewModel.modifaceViewState.value
        Assert.assertEquals(modifaceViewState.mode, ModifaceViewMode.LIVE)
        Assert.assertEquals(modifaceViewState.imageDrawablePath, "")

        val animatedTextIconState = viewModel.animatedTextIconState.value
        Assert.assertNull(animatedTextIconState.view1ClickMode)
        Assert.assertEquals(animatedTextIconState.view2ClickMode, AnimatedTextIconClickMode.CHOOSE_FROM_GALLERY)
    }

}