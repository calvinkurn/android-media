package com.tokopedia.minicart.common.widget.viewmodel.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartBundleUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.cartcommon.domain.usecase.BmGmGetGroupProductTickerUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.minicart.cartlist.MiniCartListUiModelMapper
import com.tokopedia.minicart.chatlist.MiniCartChatListUiModelMapper
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListUseCase
import com.tokopedia.minicart.common.domain.usecase.GetProductBundleRecomUseCase
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

class GetMiniCartListSimplifiedTest {

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
    fun `WHEN fetch last widget state with shop id success THEN shop id should be initialized`() {
        // given
        val shopId = listOf("123")
        val mockResponse = DataProvider.provideGetMiniCartSimplifiedSuccessAllAvailable()
        coEvery { getMiniCartListSimplifiedUseCase.setParams(any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getLatestWidgetState(shopId)

        // then
        assert(viewModel.currentShopIds.value?.equals(shopId) == true)
    }

    @Test
    fun `WHEN fetch last widget state with pre defined shop id success THEN shop id should have ben initialized`() {
        // given
        val shopId = listOf("123")
        val mockResponse = DataProvider.provideGetMiniCartSimplifiedSuccessAllAvailable()
        coEvery { getMiniCartListSimplifiedUseCase.setParams(any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(mockResponse)
        }
        viewModel.initializeShopIds(shopId)

        // when
        viewModel.getLatestWidgetState()

        // then
        assert(viewModel.currentShopIds.value?.equals(shopId) == true)
    }

    @Test
    fun `WHEN fetch last widget state with shop id success with all item available THEN isShowMiniCartWidget should be true`() {
        // given
        val shopId = listOf("123")
        val mockResponse = DataProvider.provideGetMiniCartSimplifiedSuccessAllAvailable()
        coEvery { getMiniCartListSimplifiedUseCase.setParams(any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getLatestWidgetState(shopId)

        // then
        assert(viewModel.miniCartSimplifiedData.value?.isShowMiniCartWidget == true)
    }

    @Test
    fun `WHEN fetch last widget state with shop id success with all item unavailable THEN isShowMiniCartWidget should be true`() {
        // given
        val shopId = listOf("123")
        val mockResponse = DataProvider.provideGetMiniCartSimplifiedSuccessAllUnavailable()
        coEvery { getMiniCartListSimplifiedUseCase.setParams(any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getLatestWidgetState(shopId)

        // then
        assert(viewModel.miniCartSimplifiedData.value?.isShowMiniCartWidget == true)
    }

    @Test
    fun `WHEN fetch last widget state with shop id success with empty data THEN isShowMiniCartWidget should be false`() {
        // given
        val shopId = listOf("123")
        val mockResponse = DataProvider.provideGetMiniCartSimplifiedSuccessEmptyData()
        coEvery { getMiniCartListSimplifiedUseCase.setParams(any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getLatestWidgetState(shopId)

        // then
        assert(viewModel.miniCartSimplifiedData.value?.isShowMiniCartWidget == false)
    }

    @Test
    fun `WHEN fetch last widget state with shop id success with all item available THEN mini cart list should not be empty`() {
        // given
        val shopId = listOf("123")
        val mockResponse = DataProvider.provideGetMiniCartSimplifiedSuccessAllAvailable()
        coEvery { getMiniCartListSimplifiedUseCase.setParams(any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getLatestWidgetState(shopId)

        // then
        assert(viewModel.miniCartSimplifiedData.value?.miniCartItems?.size ?: 0 > 0)
    }

    @Test
    fun `WHEN fetch last widget state with shop id success with all item unavailable THEN mini cart list should not be empty`() {
        // given
        val shopId = listOf("123")
        val mockResponse = DataProvider.provideGetMiniCartSimplifiedSuccessAllUnavailable()
        coEvery { getMiniCartListSimplifiedUseCase.setParams(any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getLatestWidgetState(shopId)

        // then
        assert(viewModel.miniCartSimplifiedData.value?.miniCartItems?.size ?: 0 > 0)
    }

    @Test
    fun `WHEN fetch last widget state with shop id success with empty data THEN mini cart list should be empty`() {
        // given
        val shopId = listOf("123")
        val mockResponse = DataProvider.provideGetMiniCartSimplifiedSuccessEmptyData()
        coEvery { getMiniCartListSimplifiedUseCase.setParams(any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getLatestWidgetState(shopId)

        // then
        assert(viewModel.miniCartSimplifiedData.value?.miniCartItems?.size ?: 0 == 0)
    }

    @Test
    fun `WHEN fetch last widget state with shop id success with all item available THEN total product count should be more than zero`() {
        // given
        val shopId = listOf("123")
        val mockResponse = DataProvider.provideGetMiniCartSimplifiedSuccessAllAvailable()
        coEvery { getMiniCartListSimplifiedUseCase.setParams(any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getLatestWidgetState(shopId)

        // then
        assert(viewModel.miniCartSimplifiedData.value?.miniCartWidgetData?.totalProductCount ?: 0 > 0)
    }

    @Test
    fun `WHEN fetch last widget state with shop id success with all item unavailable THEN total product count should be zero`() {
        // given
        val shopId = listOf("123")
        val mockResponse = DataProvider.provideGetMiniCartSimplifiedSuccessAllUnavailable()
        coEvery { getMiniCartListSimplifiedUseCase.setParams(any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getLatestWidgetState(shopId)

        // then
        assert(viewModel.miniCartSimplifiedData.value?.miniCartWidgetData?.totalProductCount ?: 0 == 0)
    }

    @Test
    fun `WHEN fetch last widget state with shop id success with empty data THEN total product count should be zero`() {
        // given
        val shopId = listOf("123")
        val mockResponse = DataProvider.provideGetMiniCartSimplifiedSuccessEmptyData()
        coEvery { getMiniCartListSimplifiedUseCase.setParams(any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getLatestWidgetState(shopId)

        // then
        assert(viewModel.miniCartSimplifiedData.value?.miniCartWidgetData?.totalProductCount ?: 0 == 0)
    }

    @Test
    fun `WHEN fetch last widget state with shop id success with all item available THEN flag containsOnlyUnavailableItems should be false`() {
        // given
        val shopId = listOf("123")
        val mockResponse = DataProvider.provideGetMiniCartSimplifiedSuccessAllAvailable()
        coEvery { getMiniCartListSimplifiedUseCase.setParams(any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getLatestWidgetState(shopId)

        // then
        assert(viewModel.miniCartSimplifiedData.value?.miniCartWidgetData?.containsOnlyUnavailableItems == false)
    }

    @Test
    fun `WHEN fetch last widget state with shop id success with all item unavailable THEN flag containsOnlyUnavailableItems should be true`() {
        // given
        val shopId = listOf("123")
        val mockResponse = DataProvider.provideGetMiniCartSimplifiedSuccessAllUnavailable()
        coEvery { getMiniCartListSimplifiedUseCase.setParams(any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getLatestWidgetState(shopId)

        // then
        assert(viewModel.miniCartSimplifiedData.value?.miniCartWidgetData?.containsOnlyUnavailableItems == true)
    }

    @Test
    fun `WHEN fetch last widget state with shop id success with empty data THEN flag containsOnlyUnavailableItems should be false`() {
        // given
        val shopId = listOf("123")
        val mockResponse = DataProvider.provideGetMiniCartSimplifiedSuccessEmptyData()
        coEvery { getMiniCartListSimplifiedUseCase.setParams(any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getLatestWidgetState(shopId)

        // then
        assert(viewModel.miniCartSimplifiedData.value?.miniCartWidgetData?.containsOnlyUnavailableItems == false)
    }

    @Test
    fun `WHEN fetch last widget state with shop id success with all item available THEN unavailableItemsCount should be zero`() {
        // given
        val shopId = listOf("123")
        val mockResponse = DataProvider.provideGetMiniCartSimplifiedSuccessAllAvailable()
        coEvery { getMiniCartListSimplifiedUseCase.setParams(any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getLatestWidgetState(shopId)

        // then
        assert(viewModel.miniCartSimplifiedData.value?.miniCartWidgetData?.unavailableItemsCount == 0)
    }

    @Test
    fun `WHEN fetch last widget state with shop id success with all item unavailable THEN unavailableItemsCount should be more than zero`() {
        // given
        val shopId = listOf("123")
        val mockResponse = DataProvider.provideGetMiniCartSimplifiedSuccessAllUnavailable()
        coEvery { getMiniCartListSimplifiedUseCase.setParams(any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getLatestWidgetState(shopId)

        // then
        assert(viewModel.miniCartSimplifiedData.value?.miniCartWidgetData?.unavailableItemsCount ?: 0 > 0)
    }

    @Test
    fun `WHEN fetch last widget state with shop id success with empty data THEN unavailableItemsCount should be zero`() {
        // given
        val shopId = listOf("123")
        val mockResponse = DataProvider.provideGetMiniCartSimplifiedSuccessEmptyData()
        coEvery { getMiniCartListSimplifiedUseCase.setParams(any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getLatestWidgetState(shopId)

        // then
        assert(viewModel.miniCartSimplifiedData.value?.miniCartWidgetData?.unavailableItemsCount == 0)
    }

    @Test
    fun `WHEN fetch last widget state error THEN isShowMiniCartWidget should be false`() {
        // given
        val errorMessage = "Error Message"
        val exception = ResponseErrorException(errorMessage)
        val shopId = listOf("123")
        coEvery { getMiniCartListSimplifiedUseCase.setParams(any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(exception)
        }

        // when
        viewModel.getLatestWidgetState(shopId)

        // then
        assert(viewModel.miniCartSimplifiedData.value?.isShowMiniCartWidget == false)
    }

    @Test
    fun `WHEN fetch last widget state twice success THEN should set latest live data value`() {
        // given
        val shopId = listOf("123")

        val mockResponse = DataProvider.provideGetMiniCartSimplifiedSuccessAllAvailable()
        coEvery { getMiniCartListSimplifiedUseCase.setParams(any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(mockResponse)
        }

        // when
        viewModel.getLatestWidgetState(shopId)

        // then
        assert(viewModel.miniCartSimplifiedData.value?.isShowMiniCartWidget == true)

        // given
        val errorMessage = "Error Message"
        val exception = ResponseErrorException(errorMessage)

        coEvery { getMiniCartListSimplifiedUseCase.setParams(any(), any()) } just Runs
        coEvery { getMiniCartListSimplifiedUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(exception)
        }

        // when
        viewModel.getLatestWidgetState(shopId)

        // then
        assert(viewModel.miniCartSimplifiedData.value?.isShowMiniCartWidget == true)
    }
}
