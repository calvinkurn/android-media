package com.tokopedia.minicart.common.widget.viewmodel.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.minicart.cartlist.MiniCartListUiModelMapper
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductUiModel
import com.tokopedia.minicart.common.data.response.deletecart.RemoveFromCartData
import com.tokopedia.minicart.common.domain.data.RemoveFromCartDomainModel
import com.tokopedia.minicart.common.domain.usecase.*
import com.tokopedia.minicart.common.widget.GlobalEvent
import com.tokopedia.minicart.common.widget.MiniCartViewModel
import com.tokopedia.minicart.common.widget.viewmodel.utils.DataProvider
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DeleteCartTest {

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
    fun `WHEN delete single cart item success THEN global event state should be updated accordingly`() {
        //given
        val productId = "1920796612"
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartProductUiModel = miniCartListUiModel.getMiniCartProductUiModelByProductId(productId) ?: MiniCartProductUiModel()

        val mockResponse = DataProvider.provideDeleteFromCartSuccess()
        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(mockResponse)
        }

        //when
        viewModel.deleteSingleCartItem(miniCartProductUiModel)

        //then
        assert(viewModel.globalEvent.value?.state == GlobalEvent.STATE_SUCCESS_DELETE_CART_ITEM)
    }

    @Test
    fun `WHEN delete single cart item success THEN temporary variable to store last deleted item should not be empty`() {
        //given
        val productId = "1920796612"
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartProductUiModel = miniCartListUiModel.getMiniCartProductUiModelByProductId(productId) ?: MiniCartProductUiModel()

        val mockResponse = DataProvider.provideDeleteFromCartSuccess()
        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(mockResponse)
        }

        //when
        viewModel.deleteSingleCartItem(miniCartProductUiModel)

        //then
        assert(viewModel.lastDeletedProductItem != null)
    }

    @Test
    fun `WHEN delete single cart item success THEN temporary variable to store last deleted item should contains deleted product`() {
        //given
        val productId = "1920796612"
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartProductUiModel = miniCartListUiModel.getMiniCartProductUiModelByProductId(productId) ?: MiniCartProductUiModel()

        val mockResponse = DataProvider.provideDeleteFromCartSuccess()
        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(mockResponse)
        }

        //when
        viewModel.deleteSingleCartItem(miniCartProductUiModel)

        //then
        assert(viewModel.lastDeletedProductItem?.productId == productId)
    }

    @Test
    fun `WHEN delete single cart item success and has remaining cart item THEN flag isLastItem should be false`() {
        //given
        val productId = "1868874657"
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartProductUiModel = miniCartListUiModel.getMiniCartProductUiModelByProductId(productId) ?: MiniCartProductUiModel()

        val mockResponse = DataProvider.provideDeleteFromCartSuccess()
        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(mockResponse)
        }

        //when
        viewModel.deleteSingleCartItem(miniCartProductUiModel)

        //then
        val data = viewModel.globalEvent.value?.data as? RemoveFromCartDomainModel ?: RemoveFromCartDomainModel()
        assert(!data.isLastItem)
    }

    @Test
    fun `WHEN delete single cart item success and has no remaining cart item THEN flag isLastItem should be true`() {
        //given
        val productId = "1868874657"
        val miniCartListUiModel = DataProvider.provideGetMiniCartListSuccessWithSingleAvailableItem()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartProductUiModel = miniCartListUiModel.getMiniCartProductUiModelByProductId(productId) ?: MiniCartProductUiModel()

        val mockResponse = DataProvider.provideDeleteFromCartSuccess()
        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(mockResponse)
        }

        //when
        viewModel.deleteSingleCartItem(miniCartProductUiModel)

        //then
        val data = viewModel.globalEvent.value?.data as? RemoveFromCartDomainModel ?: RemoveFromCartDomainModel()
        assert(data.isLastItem)
    }

    @Test
    fun `WHEN delete single cart item error THEN global event state should be updated accordingly`() {
        //given
        val productId = "1920796612"
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartProductUiModel = miniCartListUiModel.getMiniCartProductUiModelByProductId(productId) ?: MiniCartProductUiModel()

        val errorMessage = "Error Message"
        val throwable = ResponseErrorException(errorMessage)
        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        //when
        viewModel.deleteSingleCartItem(miniCartProductUiModel)

        //then
        assert(viewModel.globalEvent.value?.state == GlobalEvent.STATE_FAILED_DELETE_CART_ITEM)
    }

    @Test
    fun `WHEN delete single cart item error THEN global event should have throwable with correct error message`() {
        //given
        val productId = "1920796612"
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartProductUiModel = miniCartListUiModel.getMiniCartProductUiModelByProductId(productId) ?: MiniCartProductUiModel()

        val errorMessage = "Error Message"
        val throwable = ResponseErrorException(errorMessage)
        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        //when
        viewModel.deleteSingleCartItem(miniCartProductUiModel)

        //then
        assert(viewModel.globalEvent.value?.throwable?.message?.equals(errorMessage) == true)
    }

    @Test
    fun `WHEN bulk delete unavailable items success THEN global event state should be updated accordingly`() {
        //given
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllUnavailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)

        val mockResponse = DataProvider.provideDeleteFromCartSuccess()
        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(mockResponse)
        }

        //when
        viewModel.bulkDeleteUnavailableCartItems()

        //then
        assert(viewModel.globalEvent.value?.state == GlobalEvent.STATE_SUCCESS_DELETE_CART_ITEM)
    }

    @Test
    fun `WHEN bulk delete unavailable items success and has remaining available item THEN flag isLastItem should be false`() {
        //given
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAvailableAndUnavailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)

        val mockResponse = DataProvider.provideDeleteFromCartSuccess()
        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(mockResponse)
        }

        //when
        viewModel.bulkDeleteUnavailableCartItems()

        //then
        val data = viewModel.globalEvent.value?.data as? RemoveFromCartDomainModel ?: RemoveFromCartDomainModel()
        assert(!data.isLastItem)
    }

    @Test
    fun `WHEN bulk delete unavailable items success and has no remaining available item THEN flag isLastItem should be true`() {
        //given
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllUnavailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)

        val mockResponse = DataProvider.provideDeleteFromCartSuccess()
        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(mockResponse)
        }

        //when
        viewModel.bulkDeleteUnavailableCartItems()

        //then
        val data = viewModel.globalEvent.value?.data as? RemoveFromCartDomainModel ?: RemoveFromCartDomainModel()
        assert(data.isLastItem)
    }

    @Test
    fun `WHEN bulk delete unavailable items error THEN global event state should be updated accordingly`() {
        //given
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)

        val errorMessage = "Error Message"
        val throwable = ResponseErrorException(errorMessage)
        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        //when
        viewModel.bulkDeleteUnavailableCartItems()

        //then
        assert(viewModel.globalEvent.value?.state == GlobalEvent.STATE_FAILED_DELETE_CART_ITEM)
    }

    @Test
    fun `WHEN bulk delete unavailable items error THEN global event should have throwable with correct error messag`() {
        //given
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)

        val errorMessage = "Error Message"
        val throwable = ResponseErrorException(errorMessage)
        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        //when
        viewModel.bulkDeleteUnavailableCartItems()

        //then
        assert(viewModel.globalEvent.value?.throwable?.message?.equals(errorMessage) == true)
    }

}