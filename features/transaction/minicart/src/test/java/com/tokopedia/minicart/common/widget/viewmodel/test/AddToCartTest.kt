package com.tokopedia.minicart.common.widget.viewmodel.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.atc_common.domain.model.response.AddToCartOccMultiData
import com.tokopedia.atc_common.domain.model.response.AddToCartOccMultiDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.minicart.cartlist.MiniCartListUiModelMapper
import com.tokopedia.minicart.common.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.minicart.common.domain.usecase.*
import com.tokopedia.minicart.common.widget.GlobalEvent
import com.tokopedia.minicart.common.widget.MiniCartViewModel
import com.tokopedia.minicart.common.widget.viewmodel.utils.DataProvider
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddToCartTest {

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
    fun `WHEN update cart for checkout from mini cart widget success THEN global event state should be updated accordingly`() {
        //given
        val miniCartSimplifiedData = DataProvider.provideMiniCartSimplifiedDataAvailableAndUnavailable()
        viewModel.setMiniCartSimplifiedData(miniCartSimplifiedData)

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { addToCartOccMultiUseCase.setParams(any()) } answers { addToCartOccMultiUseCase }
        coEvery { addToCartOccMultiUseCase.execute(any(), any()) } answers {
            firstArg<(AddToCartOccMultiDataModel) -> Unit>().invoke(AddToCartOccMultiDataModel(status = "OK", data = AddToCartOccMultiData(success = 1)))
        }

        val observer = GlobalEvent.OBSERVER_MINI_CART_WIDGET

        //when
        viewModel.addToCart(observer)

        //then
        assert(viewModel.globalEvent.value?.state == GlobalEvent.STATE_SUCCESS_ADD_TO_CART_FOR_CHECKOUT)
    }

    @Test
    fun `WHEN update cart for checkout from mini cart bottom sheet success THEN global event state should be updated accordingly`() {
        //given
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParamsFromUiModels(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }

        val isForCheckout = true
        val observer = GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET

        //when
        viewModel.addToCart(observer)

        //then
        assert(viewModel.globalEvent.value?.state == GlobalEvent.STATE_SUCCESS_ADD_TO_CART_FOR_CHECKOUT)
    }

    @Test
    fun `WHEN update cart for checkout from mini cart bottom sheet error THEN global event state should be updated accordingly`() {
        //given
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)

        val errorMessage = "Error Message"
        val throwable = ResponseErrorException(errorMessage)
        coEvery { updateCartUseCase.setParamsFromUiModels(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        val isForCheckout = true
        val observer = GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET

        //when
        viewModel.addToCart(observer)

        //then
        assert(viewModel.globalEvent.value?.state == GlobalEvent.STATE_FAILED_ADD_TO_CART_FOR_CHECKOUT)
    }

    @Test
    fun `WHEN update cart for checkout from mini cart bottom sheet failed with data THEN update cart data should not be empty`() {
        //given
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)

        val mockResponse = DataProvider.provideUpdateCartFailed()
        coEvery { updateCartUseCase.setParamsFromUiModels(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }

        val isForCheckout = true
        val observer = GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET

        //when
        viewModel.addToCart(observer)

        //then
        val data = viewModel.globalEvent.value?.data as? AddToCartOccMultiDataModel
        assert(data != null)
    }

}