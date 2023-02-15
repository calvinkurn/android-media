package com.tokopedia.minicart.common.widget.viewmodel.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartBundleUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.minicart.cartlist.MiniCartListUiModelMapper
import com.tokopedia.minicart.chatlist.MiniCartChatListUiModelMapper
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListUseCase
import com.tokopedia.minicart.common.domain.usecase.GetProductBundleRecomUseCase
import com.tokopedia.minicart.common.widget.GlobalEvent
import com.tokopedia.minicart.common.widget.MiniCartViewModel
import com.tokopedia.minicart.common.widget.viewmodel.utils.DataProvider
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetMiniCartListTest {

    private lateinit var viewModel: MiniCartViewModel
    private var dispatcher: CoroutineDispatchers = CoroutineTestDispatchersProvider

    private var miniCartListUiModelMapper: MiniCartListUiModelMapper = spyk()
    private var miniCartChatListUiModelMapper: MiniCartChatListUiModelMapper = spyk()
    private var getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase = mockk()
    private val getMiniCartListUseCase: GetMiniCartListUseCase = mockk()
    private val deleteCartUseCase: DeleteCartUseCase = mockk()
    private val undoDeleteCartUseCase: UndoDeleteCartUseCase = mockk()
    private val updateCartUseCase: UpdateCartUseCase = mockk()
    private val addToCartOccMultiUseCase: AddToCartOccMultiUseCase = mockk()
    private val getProductBundleRecomUseCase: GetProductBundleRecomUseCase = mockk()
    private val addToCartBundleUseCase: AddToCartBundleUseCase = mockk()
    private val userSession: UserSessionInterface = mockk()

    @get: Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = MiniCartViewModel(
            dispatcher,
            getMiniCartListSimplifiedUseCase,
            getMiniCartListUseCase,
            deleteCartUseCase,
            undoDeleteCartUseCase,
            updateCartUseCase,
            getProductBundleRecomUseCase,
            addToCartBundleUseCase,
            addToCartOccMultiUseCase,
            miniCartListUiModelMapper,
            miniCartChatListUiModelMapper,
            userSession
        )
    }

    @Test
    fun `WHEN first load mini cart list success THEN flag isFirstLoad should be true`() {
        // given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessAllAvailable()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getCartList(isFirstLoad = true)

        // then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.isFirstLoad == true)
    }

    @Test
    fun `WHEN reload mini cart list success THEN flag isFirstLoad should be false`() {
        // given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessAllAvailable()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getCartList()

        // then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.isFirstLoad == false)
    }

    @Test
    fun `WHEN reload mini cart list error THEN global state should not be updated`() {
        // given
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(ResponseErrorException())
        }
        viewModel.initializeGlobalState()

        // when
        viewModel.getCartList()

        // then
        assert(viewModel.globalEvent.value?.state == 0)
    }

    @Test
    fun `WHEN first load mini cart list success with all item available THEN bottom sheet title should not be empty`() {
        // given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessAllAvailable()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getCartList(isFirstLoad = true)

        // then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.title?.isNotBlank() == true)
    }

    @Test
    fun `WHEN first load mini cart list success with all item available THEN should have maximum shipping weight`() {
        // given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessAllAvailable()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getCartList(isFirstLoad = true)

        // then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.maximumShippingWeight ?: 0 > 0)
    }

    @Test
    fun `WHEN first load mini cart list success with all item available THEN should have maximum shipping weight error message`() {
        // given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessAllAvailable()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getCartList(isFirstLoad = true)

        // then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.maximumShippingWeightErrorMessage?.isNotBlank() == true)
    }

    @Test
    fun `WHEN first load mini cart list success with all item available THEN total product count should be more than zero`() {
        // given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessAllAvailable()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getCartList(isFirstLoad = true)

        // then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.miniCartWidgetUiModel?.totalProductCount ?: 0 > 0)
    }

    @Test
    fun `WHEN first load mini cart list success with all item available THEN total product error should be zero`() {
        // given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessAllAvailable()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getCartList(isFirstLoad = true)

        // then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.miniCartWidgetUiModel?.totalProductError ?: 0 == 0)
    }

    @Test
    fun `WHEN first load mini cart list success with all item available THEN summary transaction quantity should be more than zero`() {
        // given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessAllAvailable()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getCartList(isFirstLoad = true)

        // then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.miniCartSummaryTransactionUiModel?.qty ?: 0 > 0)
    }

    @Test
    fun `WHEN first load mini cart list success with all item available THEN visitables should not be empty`() {
        // given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessAllAvailable()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getCartList(isFirstLoad = true)

        // then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.visitables?.size ?: 0 > 0)
    }

    @Test
    fun `WHEN first load mini cart list success with all item available THEN flag needToCalculateAfterLoad should be true`() {
        // given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessAllAvailable()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getCartList(isFirstLoad = true)

        // then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.needToCalculateAfterLoad == true)
    }

    @Test
    fun `WHEN first load mini cart list success with all item available THEN visitables should contains available product only`() {
        // given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessAllAvailable()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getCartList(isFirstLoad = true)

        // then
        assert(
            viewModel.miniCartListBottomSheetUiModel.value?.getAvailableProduct()?.size ?: 0 > 0 &&
                viewModel.miniCartListBottomSheetUiModel.value?.getUnavailableProduct()?.size ?: 0 == 0
        )
    }

    @Test
    fun `WHEN first load mini cart list success with all item available THEN first product should be an available product`() {
        // given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessAllAvailable()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getCartList(isFirstLoad = true)

        // then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.getAvailableProduct()?.firstOrNull()?.isProductDisabled == false)
    }

    @Test
    fun `WHEN first load mini cart list success but get out of service THEN global event should be updated accordingly`() {
        // given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessOutOfService()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getCartList(isFirstLoad = true)

        // then
        assert(viewModel.globalEvent.value?.state == GlobalEvent.STATE_FAILED_LOAD_MINI_CART_LIST_BOTTOM_SHEET)
    }

    @Test
    fun `WHEN first load mini cart list error THEN global event should be updated accordingly`() {
        // given
        val errorMessage = "Error Message"
        val exception = ResponseErrorException(errorMessage)
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(exception)
        }

        // when
        viewModel.getCartList(isFirstLoad = true)

        // then
        assert(viewModel.globalEvent.value?.state == GlobalEvent.STATE_FAILED_LOAD_MINI_CART_LIST_BOTTOM_SHEET)
    }

    @Test
    fun `WHEN first load mini cart list success with multiple available items in one cart THEN total product count should more than one`() {
        // given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessMultipleAvailableAndUnavailableOneCart()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getCartList(isFirstLoad = true)

        // then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.miniCartWidgetUiModel?.totalProductCount ?: 0 > 1)
    }

    @Test
    fun `WHEN first load mini cart list success with multiple unavailable items in one cart THEN total product error should more than one`() {
        // given
        val mockResponse = DataProvider.provideGetMiniCartListSuccessMultipleAvailableAndUnavailableOneCart()
        coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
        coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getCartList(isFirstLoad = true)

        // then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.miniCartWidgetUiModel?.totalProductError ?: 0 > 1)
    }
}
