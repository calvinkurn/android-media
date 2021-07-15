package com.tokopedia.minicart.common.widget.viewmodel.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.minicart.cartlist.MiniCartListUiModelMapper
import com.tokopedia.minicart.cartlist.uimodel.MiniCartTickerWarningUiModel
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.domain.usecase.*
import com.tokopedia.minicart.common.widget.MiniCartViewModel
import com.tokopedia.minicart.common.widget.viewmodel.utils.DataProvider
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CalculationTest {

    private lateinit var viewModel: MiniCartViewModel
    private var dispatcher: CoroutineDispatchers = CoroutineTestDispatchersProvider

    private var uiModelMapper: MiniCartListUiModelMapper = spyk()
    private var getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase = mockk()
    private val getMiniCartListUseCase: GetMiniCartListUseCase = mockk()
    private val deleteCartUseCase: DeleteCartUseCase = mockk()
    private val undoDeleteCartUseCase: UndoDeleteCartUseCase = mockk()
    private val updateCartUseCase: UpdateCartUseCase = mockk()
    private val addToCartOccMultiUseCase: AddToCartOccMultiUseCase = mockk()

    @get: Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = MiniCartViewModel(dispatcher, getMiniCartListSimplifiedUseCase, getMiniCartListUseCase, deleteCartUseCase, undoDeleteCartUseCase, updateCartUseCase, addToCartOccMultiUseCase, uiModelMapper)
    }

    @Test
    fun `WHEN calculate product after first load mini cart list THEN isFirstLoad flag should be true`() {
        //given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessAllAvailable()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        //when
        viewModel.getCartList(isFirstLoad = true)
        viewModel.calculateProduct()

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.isFirstLoad == true)
    }

    @Test
    fun `WHEN calculate product after first load mini cart list THEN needToCalculateAfterLoad flag should be false`() {
        //given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessAllAvailable()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        //when
        viewModel.getCartList(isFirstLoad = true)
        viewModel.calculateProduct()

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.needToCalculateAfterLoad == false)
    }

    @Test
    fun `WHEN change quantity and calculate product THEN total price should be calculated correctly`() {
        //given
        val expectedTotalPrice = 8000L
        val productId = "1920796612"
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)

        //when
        viewModel.updateProductQty(productId, 5)
        viewModel.calculateProduct()

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.miniCartWidgetUiModel?.totalProductPrice == expectedTotalPrice)
    }

    @Test
    fun `WHEN change quantity and calculate product THEN total product count should be calculated correctly`() {
        //given
        val expectedTotalProductCount = 7
        val productId = "1920796612"
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)

        //when
        viewModel.updateProductQty(productId, 5)
        viewModel.calculateProduct()

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.miniCartWidgetUiModel?.totalProductCount == expectedTotalProductCount)
    }

    @Test
    fun `WHEN change quantity and calculate product THEN total quantity on summary transaction be calculated correctly`() {
        //given
        val expectedTotalProductCount = 7
        val productId = "1920796612"
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)

        //when
        viewModel.updateProductQty(productId, 5)
        viewModel.calculateProduct()

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.miniCartSummaryTransactionUiModel?.qty ?: 0 == expectedTotalProductCount)
    }

    @Test
    fun `WHEN change quantity and calculate product THEN total value on summary transaction be calculated correctly`() {
        //given
        val expectedTotalValue = 6000L
        val productId = "1920796612"
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)

        //when
        viewModel.updateProductQty(productId, 3)
        viewModel.calculateProduct()

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.miniCartSummaryTransactionUiModel?.totalValue ?: 0 == expectedTotalValue)
    }

    @Test
    fun `WHEN change quantity and calculate product THEN payment total on summary transaction be calculated correctly`() {
        //given
        val expectedPaymentTotal = 6000L
        val productId = "1920796612"
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)

        //when
        viewModel.updateProductQty(productId, 3)
        viewModel.calculateProduct()

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.miniCartSummaryTransactionUiModel?.paymentTotal ?: 0 == expectedPaymentTotal)
    }

    @Test
    fun `WHEN change quantity and calculate product with slash price THEN discount value on summary transaction be calculated correctly`() {
        //given
        val expectedDiscountTotal = 1500L
        val productId = "1925675638"
        val miniCartListUiModel = DataProvider.provideGetMiniCartListSuccessWithSlashPrice()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)

        //when
        viewModel.updateProductQty(productId, 3)
        viewModel.calculateProduct()

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.miniCartSummaryTransactionUiModel?.discountValue ?: 0 == expectedDiscountTotal)
    }

    @Test
    fun `WHEN change quantity and calculate product with slash price THEN payment total on summary transaction be calculated correctly`() {
        //given
        val expectedPaymentTotal = 1500L
        val productId = "1925675638"
        val miniCartListUiModel = DataProvider.provideGetMiniCartListSuccessWithSlashPrice()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)

        //when
        viewModel.updateProductQty(productId, 3)
        viewModel.calculateProduct()

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.miniCartSummaryTransactionUiModel?.paymentTotal ?: 0 == expectedPaymentTotal)
    }

    @Test
    fun `WHEN change quantity and calculate product with slash price THEN total value on summary transaction be calculated correctly`() {
        //given
        val expectedTotalVALUE = 3000L
        val productId = "1925675638"
        val miniCartListUiModel = DataProvider.provideGetMiniCartListSuccessWithSlashPrice()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)

        //when
        viewModel.updateProductQty(productId, 3)
        viewModel.calculateProduct()

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.miniCartSummaryTransactionUiModel?.totalValue ?: 0 == expectedTotalVALUE)
    }

    // Test wholesale variant case
    @Test
    fun `WHEN change quantity and calculate wholesale product and eligible for wholesale THEN wholesale price should be set accordingly`() {
        //given
        val productId = "1894482358"
        val expectedWholesalePrice = 200L
        val miniCartListUiModels = DataProvider.provideGetMiniCartListSuccessWithWholesaleVariant()
        viewModel.setMiniCartListUiModel(miniCartListUiModels)

        //when
        viewModel.updateProductQty(productId, 101)
        viewModel.calculateProduct()

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.getMiniCartProductUiModelByProductId(productId)?.productWholeSalePrice == expectedWholesalePrice)
    }

    @Test
    fun `WHEN change quantity and calculate wholesale product and not eligible for wholesale THEN wholesale price should be set accordingly`() {
        //given
        val productId = "1894482358"
        val expectedWholesalePrice = 0L
        val miniCartListUiModels = DataProvider.provideGetMiniCartListSuccessWithWholesaleVariant()
        viewModel.setMiniCartListUiModel(miniCartListUiModels)

        //when
        viewModel.updateProductQty(productId, 50)
        viewModel.calculateProduct()
        viewModel.updateProductQty(productId, 1)
        viewModel.calculateProduct()

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.getMiniCartProductUiModelByProductId(productId)?.productWholeSalePrice == expectedWholesalePrice)
    }

    // Test overweight case
    @Test
    fun `WHEN change quantity and calculate product got weight exceed limit and have no ticker warning overweight THEN ticker warning overweight should be added to list`(){
        //given
        val productId = "1920796612"
        val miniCartListUiModels = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModels)

        //when
        viewModel.updateProductQty(productId, 10)
        viewModel.calculateProduct()

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.getMiniCartTickerWarningUiModel() != null)
    }

    @Test
    fun `WHEN change quantity and calculate product got weight exceed limit THEN ticker warning overweight should contain over weight value`(){
        //given
        val productId = "1920796612"
        val overWeight = "0,2"
        val miniCartListUiModels = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModels)

        //when
        viewModel.updateProductQty(productId, 10)
        viewModel.calculateProduct()

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.getMiniCartTickerWarningUiModel()?.warningMessage?.contains(overWeight) == true)
    }

    @Test
    fun `WHEN change quantity and calculate product got no weight exceed limit THEN ticker warning overweight should be removed`(){
        //given
        val productId = "1920796612"
        val miniCartListUiModels = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModels)

        //when
        viewModel.updateProductQty(productId, 10)
        viewModel.calculateProduct()
        viewModel.updateProductQty(productId, 1)
        viewModel.calculateProduct()

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.getMiniCartTickerWarningUiModel() == null)
    }

    @Test
    fun `WHEN already overweight and change quantity still overweight THEN ticker warning overweight should be updated`(){
        //given
        val productId = "1920796612"
        val overWeight = "1,2"
        val miniCartListUiModels = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModels)

        //when
        viewModel.updateProductQty(productId, 10)
        viewModel.calculateProduct()
        viewModel.updateProductQty(productId, 20)
        viewModel.calculateProduct()

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.getMiniCartTickerWarningUiModel()?.warningMessage?.contains(overWeight) == true)
    }

    @Test
    fun `WHEN mini cart has no unavailable product and change quantity got overweight THEN ticker warning overweight should be on first index`(){
        //given
        val productId = "1920796612"
        val miniCartListUiModels = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModels)

        //when
        viewModel.updateProductQty(productId, 10)
        viewModel.calculateProduct()

        //then
        val miniCartTickerWarningUiModel = viewModel.miniCartListBottomSheetUiModel.value?.getMiniCartTickerWarningUiModel() ?: MiniCartTickerWarningUiModel()
        assert(viewModel.miniCartListBottomSheetUiModel.value?.visitables?.indexOf(miniCartTickerWarningUiModel) == 0)
    }

    @Test
    fun `WHEN mini cart has unavailable product and change quantity got overweight THEN ticker warning overweight should be on second index`(){
        //given
        val productId = "1920796612"
        val miniCartListUiModels = DataProvider.provideMiniCartListUiModelAvailableAndUnavailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModels)

        //when
        viewModel.updateProductQty(productId, 10)
        viewModel.calculateProduct()

        //then
        val miniCartTickerWarningUiModel = viewModel.miniCartListBottomSheetUiModel.value?.getMiniCartTickerWarningUiModel() ?: MiniCartTickerWarningUiModel()
        assert(viewModel.miniCartListBottomSheetUiModel.value?.visitables?.indexOf(miniCartTickerWarningUiModel) == 1)
    }

}