package com.tokopedia.minicart.common.widget.viewmodel.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.minicart.cartlist.MiniCartListUiModelMapper
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductUiModel
import com.tokopedia.minicart.common.data.response.undodeletecart.UndoDeleteCartDataResponse
import com.tokopedia.minicart.common.domain.data.UndoDeleteCartDomainModel
import com.tokopedia.minicart.common.domain.usecase.*
import com.tokopedia.minicart.common.widget.GlobalEvent
import com.tokopedia.minicart.common.widget.MiniCartViewModel
import com.tokopedia.minicart.common.widget.viewmodel.utils.DataProvider
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UndoDeleteCartTest {

    private lateinit var viewModel: MiniCartViewModel
    private var dispatcher: CoroutineDispatchers = CoroutineTestDispatchersProvider

    private var uiModelMapper: MiniCartListUiModelMapper = spyk()
    private var getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase = mockk()
    private val getMiniCartListUseCase: GetMiniCartListUseCase = mockk()
    private val deleteCartUseCase: DeleteCartUseCase = mockk()
    private val undoDeleteCartUseCase: UndoDeleteCartUseCase = mockk()
    private val updateCartUseCase: UpdateCartUseCase = mockk()

    @get: Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = MiniCartViewModel(dispatcher, getMiniCartListSimplifiedUseCase, getMiniCartListUseCase, deleteCartUseCase, undoDeleteCartUseCase, updateCartUseCase, uiModelMapper)
    }

    @Test
    fun `WHEN undo delete cart item success THEN global event state should be updated accordingly`() {
        //given
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val productId = "1920796612"
        val miniCartProductUiModel = miniCartListUiModel.getMiniCartProductUiModelByProductId(productId)
                ?: MiniCartProductUiModel()
        viewModel.setLastDeleteProductItem(miniCartProductUiModel)

        val mockResponse = DataProvider.provideUndoDeleteFromCartSuccess()
        coEvery { undoDeleteCartUseCase.setParams(any()) } just Runs
        coEvery { undoDeleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(UndoDeleteCartDataResponse) -> Unit>().invoke(mockResponse)
        }

        //when
        viewModel.undoDeleteCartItem(false)

        //then
        assert(viewModel.globalEvent.value?.state == GlobalEvent.STATE_SUCCESS_UNDO_DELETE_CART_ITEM)
    }

    @Test
    fun `WHEN undo delete cart item success THEN temporary stored last deleted item should be null`() {
        //given
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val productId = "1920796612"
        val miniCartProductUiModel = miniCartListUiModel.getMiniCartProductUiModelByProductId(productId)
                ?: MiniCartProductUiModel()
        viewModel.setLastDeleteProductItem(miniCartProductUiModel)

        val mockResponse = DataProvider.provideUndoDeleteFromCartSuccess()
        coEvery { undoDeleteCartUseCase.setParams(any()) } just Runs
        coEvery { undoDeleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(UndoDeleteCartDataResponse) -> Unit>().invoke(mockResponse)
        }

        //when
        viewModel.undoDeleteCartItem(false)

        //then
        assert(viewModel.lastDeletedProductItem == null)
    }

    @Test
    fun `WHEN undo delete last cart item success THEN global event data flag should be last item`() {
        //given
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val productId = "1920796612"
        val miniCartProductUiModel = miniCartListUiModel.getMiniCartProductUiModelByProductId(productId)
                ?: MiniCartProductUiModel()
        viewModel.setLastDeleteProductItem(miniCartProductUiModel)

        val mockResponse = DataProvider.provideUndoDeleteFromCartSuccess()
        coEvery { undoDeleteCartUseCase.setParams(any()) } just Runs
        coEvery { undoDeleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(UndoDeleteCartDataResponse) -> Unit>().invoke(mockResponse)
        }

        //when
        viewModel.undoDeleteCartItem(true)

        //then
        val data = viewModel.globalEvent.value?.data as? UndoDeleteCartDomainModel
        assert(data?.isLastItem == true)
    }

    @Test
    fun `WHEN undo delete cart item error THEN global event state should be updated accordingly`() {
        //given
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val productId = "1920796612"
        val miniCartProductUiModel = miniCartListUiModel.getMiniCartProductUiModelByProductId(productId)
                ?: MiniCartProductUiModel()
        viewModel.setLastDeleteProductItem(miniCartProductUiModel)

        val errorMessage = "Error Message"
        val throwable = ResponseErrorException(errorMessage)
        coEvery { undoDeleteCartUseCase.setParams(any()) } just Runs
        coEvery { undoDeleteCartUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        //when
        viewModel.undoDeleteCartItem(false)

        //then
        assert(viewModel.globalEvent.value?.state == GlobalEvent.STATE_FAILED_UNDO_DELETE_CART_ITEM)
    }

}