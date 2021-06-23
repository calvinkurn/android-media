package com.tokopedia.minicart.common.widget.viewmodel.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.minicart.cartlist.MiniCartListUiModelMapper
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.domain.usecase.*
import com.tokopedia.minicart.common.widget.GlobalEvent
import com.tokopedia.minicart.common.widget.MiniCartViewModel
import com.tokopedia.minicart.common.widget.viewmodel.utils.DataProvider
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetMiniCartListTest {

    private lateinit var viewModel: MiniCartViewModel
    private var dispatcher: CoroutineDispatchers = CoroutineTestDispatchersProvider

    private var uiModelMapper: MiniCartListUiModelMapper = spyk()
    private var getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase = mockk()
    private val getMiniCartListUseCase: GetMiniCartListUseCase = mockk()
    private val deleteCartUseCase: DeleteCartUseCase = mockk()
    private val undoDeleteCartUseCase: UndoDeleteCartUseCase = mockk()
    private val updateCartUseCase: UpdateCartUseCase = mockk()
    private val seamlessLoginUsecase: SeamlessLoginUsecase = mockk()

    @get: Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = MiniCartViewModel(dispatcher, getMiniCartListSimplifiedUseCase, getMiniCartListUseCase, deleteCartUseCase, undoDeleteCartUseCase, updateCartUseCase, seamlessLoginUsecase, uiModelMapper)
    }

    @Test
    fun `WHEN first load mini cart list success THEN flag isFirstLoad should be true`() {
        //given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessAllAvailable()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        //when
        viewModel.getCartList(isFirstLoad = true)

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.isFirstLoad == true)
    }

    @Test
    fun `WHEN reload mini cart list success THEN flag isFirstLoad should be false`() {
        //given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessAllAvailable()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        //when
        viewModel.getCartList()

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.isFirstLoad == false)
    }

    @Test
    fun `WHEN first load mini cart list success with all item available THEN bottom sheet title should not be empty`() {
        //given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessAllAvailable()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        //when
        viewModel.getCartList(isFirstLoad = true)

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.title?.isNotBlank() == true)
    }

    @Test
    fun `WHEN first load mini cart list success with all item available THEN should have maximum shipping weight`() {
        //given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessAllAvailable()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        //when
        viewModel.getCartList(isFirstLoad = true)

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.maximumShippingWeight ?: 0 > 0)
    }

    @Test
    fun `WHEN first load mini cart list success with all item available THEN should have maximum shipping weight error message`() {
        //given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessAllAvailable()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        //when
        viewModel.getCartList(isFirstLoad = true)

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.maximumShippingWeightErrorMessage?.isNotBlank() == true)
    }

    @Test
    fun `WHEN first load mini cart list success with all item available THEN total product count should be more than zero`() {
        //given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessAllAvailable()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        //when
        viewModel.getCartList(isFirstLoad = true)

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.miniCartWidgetUiModel?.totalProductCount ?: 0 > 0)
    }

    @Test
    fun `WHEN first load mini cart list success with all item available THEN total product error should be zero`() {
        //given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessAllAvailable()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        //when
        viewModel.getCartList(isFirstLoad = true)

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.miniCartWidgetUiModel?.totalProductError ?: 0 == 0)
    }

    @Test
    fun `WHEN first load mini cart list success with all item available THEN summary transaction quantity should be more than zero`() {
        //given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessAllAvailable()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        //when
        viewModel.getCartList(isFirstLoad = true)

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.miniCartSummaryTransactionUiModel?.qty ?: 0 > 0)
    }

    @Test
    fun `WHEN first load mini cart list success with all item available THEN visitables should not be empty`() {
        //given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessAllAvailable()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        //when
        viewModel.getCartList(isFirstLoad = true)

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.visitables?.size ?: 0 > 0)
    }

    @Test
    fun `WHEN first load mini cart list success with all item available THEN flag needToCalculateAfterLoad should be true`() {
        //given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessAllAvailable()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        //when
        viewModel.getCartList(isFirstLoad = true)

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.needToCalculateAfterLoad == true)
    }

    @Test
    fun `WHEN first load mini cart list success with all item available THEN visitables should contains available product only`() {
        //given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessAllAvailable()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        //when
        viewModel.getCartList(isFirstLoad = true)

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.getAvailableProduct()?.size ?: 0 > 0 &&
                viewModel.miniCartListBottomSheetUiModel.value?.getUnavailableProduct()?.size ?: 0 == 0
        )
    }

    @Test
    fun `WHEN first load mini cart list success with all item available THEN first product should be an available product`() {
        //given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessAllAvailable()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        //when
        viewModel.getCartList(isFirstLoad = true)

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.getAvailableProduct()?.firstOrNull()?.isProductDisabled == false)
    }

    @Test
    fun `WHEN first load mini cart list success but get out of service THEN global event should be updated accordingly`() {
        //given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessOutOfService()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        //when
        viewModel.getCartList(isFirstLoad = true)

        //then
        assert(viewModel.globalEvent.value?.state == GlobalEvent.STATE_FAILED_LOAD_MINI_CART_LIST_BOTTOM_SHEET)
    }

    @Test
    fun `WHEN first load mini cart list error THEN global event should be updated accordingly`() {
        //given
        val errorMessage = "Error Message"
        val exception = ResponseErrorException(errorMessage)
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(exception)
        }

        //when
        viewModel.getCartList(isFirstLoad = true)

        //then
        assert(viewModel.globalEvent.value?.state == GlobalEvent.STATE_FAILED_LOAD_MINI_CART_LIST_BOTTOM_SHEET)
    }

}