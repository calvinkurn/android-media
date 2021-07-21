package com.tokopedia.minicart.common.widget.viewmodel.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.minicart.cartlist.MiniCartListUiModelMapper
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.usecase.*
import com.tokopedia.minicart.common.widget.MiniCartViewModel
import com.tokopedia.minicart.common.widget.viewmodel.utils.DataProvider
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InitializationTest {

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
    fun `WHEN initialize current page THEN current page value should be updated accordingly`() {
        //given
        val page = MiniCartAnalytics.Page.HOME_PAGE

        //when
        viewModel.initializeCurrentPage(page)

        //then
        assert(viewModel.currentPage.value?.equals(page) == true)
    }

    @Test
    fun `WHEN initialize shop id THEN current shop id value should be updated accordingly`() {
        //given
        val shopId = listOf("123")

        //when
        viewModel.initializeShopIds(shopId)

        //then
        assert(viewModel.currentShopIds.value?.equals(shopId) == true)
    }

    @Test
    fun `WHEN initialize global state THEN global state should be on default state`() {
        //given

        //when
        viewModel.initializeGlobalState()

        //then
        assert(viewModel.globalEvent.value?.state == 0)
    }

    @Test
    fun `WHEN update mini cart simplified data THEN mini cart simplified data should not be null`() {
        //given
        val miniCartListUiModels = DataProvider.provideMiniCartSimplifiedDataAllAvailable()

        //when
        viewModel.updateMiniCartSimplifiedData(miniCartListUiModels)

        //then
        assert(viewModel.miniCartSimplifiedData.value != null)
    }

    @Test
    fun `WHEN reset temporary hidden unavailable items THEN temporary hidden unavailable items should be emty`() {
        //given
        val miniCartListUiModels = DataProvider.provideMiniCartListUiModelAllUnavailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModels)

        //when
        viewModel.toggleUnavailableItemsAccordion()
        viewModel.resetTemporaryHiddenUnavailableItems()

        //then
        assert(viewModel.tmpHiddenUnavailableItems.size == 0)
    }

}