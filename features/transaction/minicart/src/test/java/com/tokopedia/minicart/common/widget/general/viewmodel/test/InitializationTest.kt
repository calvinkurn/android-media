package com.tokopedia.minicart.common.widget.general.viewmodel.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.minicart.chatlist.MiniCartChatListUiModelMapper
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.common.widget.general.MiniCartGeneralViewModel
import com.tokopedia.minicart.common.widget.viewmodel.utils.DataProvider
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InitializationTest {

    private lateinit var viewModel: MiniCartGeneralViewModel
    private var dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider

    @get: Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase = mockk()
    private val getMiniCartListUseCase: GetMiniCartListUseCase = mockk()
    private val miniCartChatListUiModelMapper: MiniCartChatListUiModelMapper = mockk()

    @Before
    fun setUp() {
        viewModel = MiniCartGeneralViewModel(
            dispatchers,
            getMiniCartListSimplifiedUseCase,
            getMiniCartListUseCase,
            miniCartChatListUiModelMapper
        )
    }

    @Test
    fun `WHEN initialize shop id THEN current shop id value should be updated accordingly`() {
        // given
        val shopId = listOf("123")

        // when
        viewModel.initializeShopIds(shopId)

        // then
        assert(viewModel.currentShopIds.value?.equals(shopId) == true)
    }

    @Test
    fun `WHEN initialize global state THEN global state should be on default state`() {
        // given

        // when
        viewModel.initializeGlobalState()

        // then
        assert(viewModel.globalEvent.value?.state == 0)
    }

    @Test
    fun `WHEN initialize currentSource THEN currentSource should be updated accordingly`() {
        // given
        val source = MiniCartSource.MiniCartBottomSheet

        // when
        viewModel.currentSource = source

        // then
        assert(viewModel.currentSource == source)
    }

    @Test
    fun `WHEN initialize currentPage THEN currentPage should be updated accordingly`() {
        // given
        val page = MiniCartAnalytics.Page.HOME_PAGE

        // when
        viewModel.currentPage = MiniCartAnalytics.Page.HOME_PAGE

        // then
        assert(viewModel.currentPage == page)
    }

    @Test
    fun `WHEN initialize isShopDirectPurchase THEN isShopDirectPurchase should be updated accordingly`() {
        // given
        val isShopDirectPurchase = true

        // when
        viewModel.isShopDirectPurchase = isShopDirectPurchase

        // then
        assert(viewModel.isShopDirectPurchase == isShopDirectPurchase)
    }

    @Test
    fun `WHEN update mini cart simplified data THEN mini cart simplified data should not be null`() {
        // given
        val miniCartListUiModels = DataProvider.provideMiniCartSimplifiedDataAllAvailable()

        // when
        viewModel.updateMiniCartSimplifiedData(miniCartListUiModels)

        // then
        assert(viewModel.miniCartSimplifiedData.value != null)
    }
}
