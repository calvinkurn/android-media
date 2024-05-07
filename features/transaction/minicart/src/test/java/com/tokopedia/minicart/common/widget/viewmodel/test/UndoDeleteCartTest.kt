package com.tokopedia.minicart.common.widget.viewmodel.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartBundleUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.cartcommon.data.response.undodeletecart.UndoDeleteCartDataResponse
import com.tokopedia.cartcommon.domain.data.UndoDeleteCartDomainModel
import com.tokopedia.cartcommon.domain.usecase.BmGmGetGroupProductTickerUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.minicart.cartlist.MiniCartListUiModelMapper
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductUiModel
import com.tokopedia.minicart.chatlist.MiniCartChatListUiModelMapper
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

class UndoDeleteCartTest {

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
    private val updateGwpUseCase: BmGmGetGroupProductTickerUseCase = mockk()
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
            updateGwpUseCase,
            userSession
        )
    }

    @Test
    fun `WHEN undo delete cart item success THEN global event state should be updated accordingly`() {
        // given
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val productId = "1920796612"
        val miniCartProductUiModel = miniCartListUiModel.getMiniCartProductUiModelByProductId(productId)
            ?: MiniCartProductUiModel()
        viewModel.setLastDeleteProductItems(listOf(miniCartProductUiModel))

        val mockResponse = DataProvider.provideUndoDeleteFromCartSuccess()
        coEvery { undoDeleteCartUseCase.setParams(any()) } just Runs
        coEvery { undoDeleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(UndoDeleteCartDataResponse) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.undoDeleteCartItem(false)

        // then
        assert(viewModel.globalEvent.value?.state == GlobalEvent.STATE_SUCCESS_UNDO_DELETE_CART_ITEM)
    }

    @Test
    fun `WHEN undo delete cart item success THEN temporary stored last deleted item should be null`() {
        // given
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val productId = "1920796612"
        val miniCartProductUiModel = miniCartListUiModel.getMiniCartProductUiModelByProductId(productId)
            ?: MiniCartProductUiModel()
        viewModel.setLastDeleteProductItems(listOf(miniCartProductUiModel))

        val mockResponse = DataProvider.provideUndoDeleteFromCartSuccess()
        coEvery { undoDeleteCartUseCase.setParams(any()) } just Runs
        coEvery { undoDeleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(UndoDeleteCartDataResponse) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.undoDeleteCartItem(false)

        // then
        assert(viewModel.lastDeletedProductItems == null)
    }

    @Test
    fun `WHEN undo delete last cart item success THEN global event data flag should be last item`() {
        // given
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val productId = "1920796612"
        val miniCartProductUiModel = miniCartListUiModel.getMiniCartProductUiModelByProductId(productId)
            ?: MiniCartProductUiModel()
        viewModel.setLastDeleteProductItems(listOf(miniCartProductUiModel))

        val mockResponse = DataProvider.provideUndoDeleteFromCartSuccess()
        coEvery { undoDeleteCartUseCase.setParams(any()) } just Runs
        coEvery { undoDeleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(UndoDeleteCartDataResponse) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.undoDeleteCartItem(true)

        // then
        val data = viewModel.globalEvent.value?.data as? UndoDeleteCartDomainModel
        assert(data?.isLastItem == true)
    }

    @Test
    fun `WHEN undo delete cart item error THEN global event state should be updated accordingly`() {
        // given
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val productId = "1920796612"
        val miniCartProductUiModel = miniCartListUiModel.getMiniCartProductUiModelByProductId(productId)
            ?: MiniCartProductUiModel()
        viewModel.setLastDeleteProductItems(listOf(miniCartProductUiModel))

        val errorMessage = "Error Message"
        val throwable = ResponseErrorException(errorMessage)
        coEvery { undoDeleteCartUseCase.setParams(any()) } just Runs
        coEvery { undoDeleteCartUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        // when
        viewModel.undoDeleteCartItem(false)

        // then
        assert(viewModel.globalEvent.value?.state == GlobalEvent.STATE_FAILED_UNDO_DELETE_CART_ITEM)
    }

    @Test
    fun `WHEN undo delete but temporary deleted item is null THEN global event state should not be updated`() {
        // given
        viewModel.initializeGlobalState()

        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)

        val mockResponse = DataProvider.provideUndoDeleteFromCartSuccess()
        coEvery { undoDeleteCartUseCase.setParams(any()) } just Runs
        coEvery { undoDeleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(UndoDeleteCartDataResponse) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.undoDeleteCartItem(false)

        // then
        assert(viewModel.globalEvent.value?.state == 0)
    }
}
