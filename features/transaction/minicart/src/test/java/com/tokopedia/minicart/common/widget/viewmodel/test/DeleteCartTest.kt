package com.tokopedia.minicart.common.widget.viewmodel.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartBundleUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.domain.data.RemoveFromCartDomainModel
import com.tokopedia.cartcommon.domain.usecase.BmGmGetGroupProductTickerUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.minicart.cartlist.MiniCartListUiModelMapper
import com.tokopedia.minicart.cartlist.uimodel.MiniCartListUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductUiModel
import com.tokopedia.minicart.chatlist.MiniCartChatListUiModelMapper
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListUseCase
import com.tokopedia.minicart.common.domain.usecase.GetProductBundleRecomUseCase
import com.tokopedia.minicart.common.widget.GlobalEvent
import com.tokopedia.minicart.common.widget.MiniCartViewModel
import com.tokopedia.minicart.common.widget.viewmodel.utils.DataProvider
import com.tokopedia.minicart.common.widget.viewmodel.utils.ProductUtils.getBundleProductList
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DeleteCartTest {

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
    fun `WHEN delete single cart item success THEN global event state should be updated accordingly`() {
        // given
        val productId = "1920796612"
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartProductUiModel = miniCartListUiModel.getMiniCartProductUiModelByProductId(productId) ?: MiniCartProductUiModel()

        val mockResponse = DataProvider.provideDeleteFromCartSuccess()
        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.deleteSingleCartItem(miniCartProductUiModel)

        // then
        assert(viewModel.globalEvent.value?.state == GlobalEvent.STATE_SUCCESS_DELETE_CART_ITEM)
    }

    @Test
    fun `WHEN delete single cart item success THEN temporary variable to store last deleted item should not be empty`() {
        // given
        val productId = "1920796612"
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartProductUiModel = miniCartListUiModel.getMiniCartProductUiModelByProductId(productId) ?: MiniCartProductUiModel()

        val mockResponse = DataProvider.provideDeleteFromCartSuccess()
        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.deleteSingleCartItem(miniCartProductUiModel)

        // then
        assert(viewModel.lastDeletedProductItems != null)
    }

    @Test
    fun `WHEN delete single cart item success THEN temporary variable to store last deleted item should contains deleted product`() {
        // given
        val productId = "1920796612"
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartProductUiModel = miniCartListUiModel.getMiniCartProductUiModelByProductId(productId) ?: MiniCartProductUiModel()

        val mockResponse = DataProvider.provideDeleteFromCartSuccess()
        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.deleteSingleCartItem(miniCartProductUiModel)

        // then
        viewModel.lastDeletedProductItems?.forEach {
            assert(it.productId == productId)
        }
    }

    @Test
    fun `WHEN delete single cart item success and has remaining cart item THEN flag isLastItem should be false`() {
        // given
        val productId = "1868874657"
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartProductUiModel = miniCartListUiModel.getMiniCartProductUiModelByProductId(productId) ?: MiniCartProductUiModel()

        val mockResponse = DataProvider.provideDeleteFromCartSuccess()
        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.deleteSingleCartItem(miniCartProductUiModel)

        // then
        val data = viewModel.globalEvent.value?.data as? RemoveFromCartDomainModel ?: RemoveFromCartDomainModel()
        assert(!data.isLastItem)
    }

    @Test
    fun `WHEN delete single cart item success and has no remaining cart item THEN flag isLastItem should be true`() {
        // given
        val productId = "1868874657"
        val miniCartListUiModel = DataProvider.provideGetMiniCartListSuccessWithSingleAvailableItem()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartProductUiModel = miniCartListUiModel.getMiniCartProductUiModelByProductId(productId) ?: MiniCartProductUiModel()

        val mockResponse = DataProvider.provideDeleteFromCartSuccess()
        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.deleteSingleCartItem(miniCartProductUiModel)

        // then
        val data = viewModel.globalEvent.value?.data as? RemoveFromCartDomainModel ?: RemoveFromCartDomainModel()
        assert(data.isLastItem)
    }

    @Test
    fun `WHEN delete single cart item error THEN global event state should be updated accordingly`() {
        // given
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

        // when
        viewModel.deleteSingleCartItem(miniCartProductUiModel)

        // then
        assert(viewModel.globalEvent.value?.state == GlobalEvent.STATE_FAILED_DELETE_CART_ITEM)
    }

    @Test
    fun `WHEN delete single cart item error THEN global event should have throwable with correct error message`() {
        // given
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

        // when
        viewModel.deleteSingleCartItem(miniCartProductUiModel)

        // then
        assert(viewModel.globalEvent.value?.throwable?.message?.equals(errorMessage) == true)
    }

    @Test
    fun `WHEN bulk delete unavailable items success THEN global event state should be updated accordingly`() {
        // given
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllUnavailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)

        val mockResponse = DataProvider.provideDeleteFromCartSuccess()
        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.bulkDeleteUnavailableCartItems()

        // then
        assert(viewModel.globalEvent.value?.state == GlobalEvent.STATE_SUCCESS_DELETE_CART_ITEM)
    }

    @Test
    fun `WHEN bulk delete unavailable items success and has remaining available item THEN flag isLastItem should be false`() {
        // given
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAvailableAndUnavailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)

        val mockResponse = DataProvider.provideDeleteFromCartSuccess()
        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.bulkDeleteUnavailableCartItems()

        // then
        val data = viewModel.globalEvent.value?.data as? RemoveFromCartDomainModel ?: RemoveFromCartDomainModel()
        assert(!data.isLastItem)
    }

    @Test
    fun `WHEN bulk delete unavailable items success and has no remaining available item THEN flag isLastItem should be true`() {
        // given
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllUnavailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)

        val mockResponse = DataProvider.provideDeleteFromCartSuccess()
        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.bulkDeleteUnavailableCartItems()

        // then
        val data = viewModel.globalEvent.value?.data as? RemoveFromCartDomainModel ?: RemoveFromCartDomainModel()
        assert(data.isLastItem)
    }

    @Test
    fun `WHEN bulk delete unavailable items error THEN global event state should be updated accordingly`() {
        // given
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)

        val errorMessage = "Error Message"
        val throwable = ResponseErrorException(errorMessage)
        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        // when
        viewModel.bulkDeleteUnavailableCartItems()

        // then
        assert(viewModel.globalEvent.value?.state == GlobalEvent.STATE_FAILED_DELETE_CART_ITEM)
    }

    @Test
    fun `WHEN bulk delete unavailable items error THEN global event should have throwable with correct error messag`() {
        // given
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)

        val errorMessage = "Error Message"
        val throwable = ResponseErrorException(errorMessage)
        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        // when
        viewModel.bulkDeleteUnavailableCartItems()

        // then
        assert(viewModel.globalEvent.value?.throwable?.message?.equals(errorMessage) == true)
    }

    @Test
    fun `WHEN delete single cart item success but somehow data is empty THEN global event state should not be updated`() {
        // given
        viewModel.initializeGlobalState()

        val productId = "1920796612"
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartProductUiModel = miniCartListUiModel.getMiniCartProductUiModelByProductId(productId) ?: MiniCartProductUiModel()

        val mockResponse = DataProvider.provideDeleteFromCartSuccess()
        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(mockResponse)
        }

        viewModel.setMiniCartListUiModel(MiniCartListUiModel())

        // when
        viewModel.deleteSingleCartItem(miniCartProductUiModel)

        // then
        assert(viewModel.globalEvent.value?.state == 0)
    }

    @Test
    fun `WHEN bulk delete unavailable items with hidden items THEN should remove all items including hidden items`() {
        // given
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllUnavailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        viewModel.toggleUnavailableItemsAccordion()

        val mockResponse = DataProvider.provideDeleteFromCartSuccess()
        val slotUnavailableCartIdList = slot<List<String>>()
        coEvery { deleteCartUseCase.setParams(capture(slotUnavailableCartIdList)) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.bulkDeleteUnavailableCartItems()

        // then
        assert(slotUnavailableCartIdList.captured.size == 2)
    }

    @Test
    fun `WHEN delete multiple cart items success THEN temporary variable to store last deleted item should not be empty`() {
        // given
        val bundleId = "36012"
        val miniCartListUiModel = DataProvider.provideMiniCartBundleListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartProductUiModel = miniCartListUiModel.getBundleProductList(bundleId)

        val mockResponse = DataProvider.provideDeleteFromCartSuccess()
        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.deleteMultipleCartItems(miniCartProductUiModel)

        // then
        assert(viewModel.lastDeletedProductItems != null)
    }

    @Test
    fun `WHEN delete multiple cart items error THEN global event should have throwable with correct error message`() {
        // given
        val bundleId = "36012"
        val miniCartListUiModel = DataProvider.provideMiniCartBundleListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartProductUiModel = miniCartListUiModel.getBundleProductList(bundleId)

        val errorMessage = "Error Message"
        val throwable = ResponseErrorException(errorMessage)
        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        // when
        viewModel.deleteMultipleCartItems(miniCartProductUiModel)

        // then
        assert(viewModel.globalEvent.value?.throwable?.message?.equals(errorMessage) == true)
    }
}
