package com.tokopedia.minicart.common.widget.viewmodel.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.atc_common.domain.model.response.AddToCartOccMultiData
import com.tokopedia.atc_common.domain.model.response.AddToCartOccMultiDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartBundleUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.cartcommon.data.response.common.OutOfService
import com.tokopedia.cartcommon.data.response.updatecart.ToasterAction
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.minicart.cartlist.MiniCartListUiModelMapper
import com.tokopedia.minicart.chatlist.MiniCartChatListUiModelMapper
import com.tokopedia.minicart.common.domain.data.MiniCartCheckoutData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListUseCase
import com.tokopedia.minicart.common.domain.usecase.GetProductBundleRecomUseCase
import com.tokopedia.minicart.common.widget.GlobalEvent
import com.tokopedia.minicart.common.widget.MiniCartViewModel
import com.tokopedia.minicart.common.widget.viewmodel.utils.DataProvider
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GoToCheckoutTest {

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
    fun `WHEN go to checkout by atc from mini cart widget success THEN global event state should be updated accordingly`() {
        // given
        val miniCartSimplifiedData = DataProvider.provideMiniCartSimplifiedDataBundleAvailableAndUnavailable()
        viewModel.setMiniCartSimplifiedData(miniCartSimplifiedData)
        viewModel.setMiniCartABTestData(
            isOCCFlow = true,
            buttonBuyWording = "Beli Langsung"
        )

        coEvery { addToCartOccMultiUseCase.setParams(any()) } answers { addToCartOccMultiUseCase }
        coEvery { addToCartOccMultiUseCase.execute(any(), any()) } answers {
            firstArg<(AddToCartOccMultiDataModel) -> Unit>().invoke(AddToCartOccMultiDataModel(status = "OK", data = AddToCartOccMultiData(success = 1)))
        }

        val observer = GlobalEvent.OBSERVER_MINI_CART_WIDGET

        // when
        viewModel.goToCheckout(observer)

        // then
        assert(viewModel.globalEvent.value?.state == GlobalEvent.STATE_SUCCESS_TO_CHECKOUT)
    }

    @Test
    fun `WHEN go to checkout by atc from mini cart bottom sheet success THEN global event state should be updated accordingly`() {
        // given
        val miniCartListUiModel = DataProvider.provideMiniCartBundleListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        viewModel.setMiniCartABTestData(
            isOCCFlow = true,
            buttonBuyWording = "Beli Langsung"
        )

        coEvery { addToCartOccMultiUseCase.setParams(any()) } answers { addToCartOccMultiUseCase }
        coEvery { addToCartOccMultiUseCase.execute(any(), any()) } answers {
            firstArg<(AddToCartOccMultiDataModel) -> Unit>().invoke(AddToCartOccMultiDataModel(status = "OK", data = AddToCartOccMultiData(success = 1)))
        }

        val observer = GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET

        // when
        viewModel.goToCheckout(observer)

        // then
        assert(viewModel.globalEvent.value?.state == GlobalEvent.STATE_SUCCESS_TO_CHECKOUT)
    }

    @Test
    fun `WHEN go to checkout by atc from mini cart bottom sheet error THEN global event state should be updated accordingly`() {
        // given
        val miniCartListUiModel = DataProvider.provideMiniCartBundleListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        viewModel.setMiniCartABTestData(
            isOCCFlow = true,
            buttonBuyWording = "Beli Langsung"
        )

        val errorMessage = "Error Message"
        val throwable = ResponseErrorException(errorMessage)
        coEvery { addToCartOccMultiUseCase.setParams(any()) } answers { addToCartOccMultiUseCase }
        coEvery { addToCartOccMultiUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        val observer = GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET

        // when
        viewModel.goToCheckout(observer)

        // then
        assert(viewModel.globalEvent.value?.state == GlobalEvent.STATE_FAILED_TO_CHECKOUT)
    }

    @Test
    fun `WHEN go to checkout by atc from mini cart bottom sheet failed with data THEN atc data should not be empty`() {
        // given
        val miniCartListUiModel = DataProvider.provideMiniCartBundleListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        viewModel.setMiniCartABTestData(
            isOCCFlow = true,
            buttonBuyWording = "Beli Langsung"
        )

        coEvery { addToCartOccMultiUseCase.setParams(any()) } answers { addToCartOccMultiUseCase }
        coEvery { addToCartOccMultiUseCase.execute(any(), any()) } answers {
            firstArg<(AddToCartOccMultiDataModel) -> Unit>().invoke(AddToCartOccMultiDataModel(status = "OK", data = AddToCartOccMultiData(success = 0, outOfService = OutOfService(), toasterAction = ToasterAction())))
        }

        val observer = GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET

        // when
        viewModel.goToCheckout(observer)

        // then
        val data = viewModel.globalEvent.value?.data as? MiniCartCheckoutData
        assert(data != null)
    }

    @Test
    fun `WHEN go to checkout by update cart from mini cart widget success THEN global event state should be updated accordingly`() {
        // given
        val miniCartSimplifiedData = DataProvider.provideMiniCartSimplifiedDataBundleAvailableAndUnavailable()
        viewModel.setMiniCartSimplifiedData(miniCartSimplifiedData)
        viewModel.setMiniCartABTestData(
            isOCCFlow = false,
            buttonBuyWording = "Beli"
        )

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }

        val observer = GlobalEvent.OBSERVER_MINI_CART_WIDGET

        // when
        viewModel.goToCheckout(observer)

        // then
        assert(viewModel.globalEvent.value?.state == GlobalEvent.STATE_SUCCESS_TO_CHECKOUT)
    }

    @Test
    fun `WHEN go to checkout by update cart from mini cart bottom sheet success THEN global event state should be updated accordingly`() {
        // given
        val miniCartListUiModel = DataProvider.provideMiniCartBundleListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        viewModel.setMiniCartABTestData(
            isOCCFlow = false,
            buttonBuyWording = "Beli"
        )

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }

        val observer = GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET

        // when
        viewModel.goToCheckout(observer)

        // then
        assert(viewModel.globalEvent.value?.state == GlobalEvent.STATE_SUCCESS_TO_CHECKOUT)
    }

    @Test
    fun `WHEN go to checkout by update cart from mini cart bottom sheet error THEN global event state should be updated accordingly`() {
        // given
        val miniCartListUiModel = DataProvider.provideMiniCartBundleListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        viewModel.setMiniCartABTestData(
            isOCCFlow = false,
            buttonBuyWording = "Beli"
        )

        val errorMessage = "Error Message"
        val throwable = ResponseErrorException(errorMessage)
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        val observer = GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET

        // when
        viewModel.goToCheckout(observer)

        // then
        assert(viewModel.globalEvent.value?.state == GlobalEvent.STATE_FAILED_TO_CHECKOUT)
    }

    @Test
    fun `WHEN go to checkout by update cart from mini cart bottom sheet failed with data THEN update cart data should not be empty`() {
        // given
        val miniCartListUiModel = DataProvider.provideMiniCartBundleListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        viewModel.setMiniCartABTestData(
            isOCCFlow = false,
            buttonBuyWording = "Beli"
        )

        val mockResponse = DataProvider.provideUpdateCartFailed()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }

        val observer = GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET

        // when
        viewModel.goToCheckout(observer)

        // then
        val data = viewModel.globalEvent.value?.data as? MiniCartCheckoutData
        assert(data != null)
    }
}
