package com.tokopedia.minicart.common.widget.general.viewmodel.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.minicart.chatlist.MiniCartChatListUiModelMapper
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListUseCase
import com.tokopedia.minicart.common.widget.GlobalEvent
import com.tokopedia.minicart.common.widget.general.MiniCartGeneralViewModel
import com.tokopedia.minicart.common.widget.general.viewmodel.utils.DataProvider
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetMiniCartListTest {

    private lateinit var viewModel: MiniCartGeneralViewModel
    private var dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider

    private val getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase = mockk()
    private val getMiniCartListUseCase: GetMiniCartListUseCase = mockk()
    private val miniCartChatListUiModelMapper: MiniCartChatListUiModelMapper = spyk()

    @get: Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel =
            MiniCartGeneralViewModel(
                dispatchers,
                getMiniCartListSimplifiedUseCase,
                getMiniCartListUseCase,
                miniCartChatListUiModelMapper
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
        assert(viewModel.getMiniCartChatListBottomSheetUiModel().value?.isFirstLoad == true)
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
        assert(viewModel.getMiniCartChatListBottomSheetUiModel().value?.isFirstLoad == false)
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
}
