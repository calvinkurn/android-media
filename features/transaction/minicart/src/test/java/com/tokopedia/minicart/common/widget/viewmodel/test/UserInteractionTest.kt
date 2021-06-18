package com.tokopedia.minicart.common.widget.viewmodel.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.minicart.cartlist.MiniCartListUiModelMapper
import com.tokopedia.minicart.common.domain.usecase.*
import com.tokopedia.minicart.common.widget.MiniCartViewModel
import com.tokopedia.minicart.common.widget.viewmodel.utils.DataProvider
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserInteractionTest {

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
        viewModel.initializeShopIds(listOf("123"))
    }

    @Test
    fun `WHEN user change product quantity THEN quantity should be updated accordingly on bottomsheet ui model`() {
        //given
        val productId = "1920796612"
        val newQty = 5
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartSimplifiedData = DataProvider.provideMiniCartSimplifiedDataAllAvailable()
        viewModel.setMiniCartSimplifiedData(miniCartSimplifiedData)

        //when
        viewModel.updateProductQty(productId, newQty)

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.getMiniCartProductUiModelByProductId(productId)?.productQty ?: 0 == newQty)
    }

    @Test
    fun `WHEN user change product quantity THEN quantity should be updated accordingly on widget ui model`() {
        //given
        val productId = "1920796612"
        val newQty = 5
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartSimplifiedData = DataProvider.provideMiniCartSimplifiedDataAllAvailable()
        viewModel.setMiniCartSimplifiedData(miniCartSimplifiedData)

        //when
        viewModel.updateProductQty(productId, newQty)

        //then
        assert(viewModel.miniCartSimplifiedData.value?.getMiniCartItemByProductId(productId)?.quantity ?: 0 == newQty)
    }

    @Test
    fun `WHEN user change product notes THEN notes should be updated accordingly on bottomsheet ui model`() {
        //given
        val productId = "1920796612"
        val newNotes = "new notes"
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartSimplifiedData = DataProvider.provideMiniCartSimplifiedDataAllAvailable()
        viewModel.setMiniCartSimplifiedData(miniCartSimplifiedData)

        //when
        viewModel.updateProductNotes(productId, newNotes)

        //then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.getMiniCartProductUiModelByProductId(productId)?.productNotes ?: 0 == newNotes)
    }

    @Test
    fun `WHEN user change product notes THEN notes should be updated accordingly on widget ui model`() {
        //given
        val productId = "1920796612"
        val newNotes = "new notes"
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartSimplifiedData = DataProvider.provideMiniCartSimplifiedDataAllAvailable()
        viewModel.setMiniCartSimplifiedData(miniCartSimplifiedData)

        //when
        viewModel.updateProductNotes(productId, newNotes)

        //then
        assert(viewModel.miniCartSimplifiedData.value?.getMiniCartItemByProductId(productId)?.notes ?: 0 == newNotes)
    }

    @Test
    fun `WHEN user toggle accordion from expanded to collapsed THEN temporary collapsed data should not be empty`() {
        //given
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllUnavailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartSimplifiedData = DataProvider.provideMiniCartSimplifiedDataAllUnavailable()
        viewModel.setMiniCartSimplifiedData(miniCartSimplifiedData)

        //when
        viewModel.toggleUnavailableItemsAccordion()

        //then
        assert(viewModel.tmpHiddenUnavailableItems.size > 0)
    }

    @Test
    fun `WHEN user toggle accordion from collapsed to expanded THEN temporary collapsed data should be empty`() {
        //given
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllUnavailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartSimplifiedData = DataProvider.provideMiniCartSimplifiedDataAllUnavailable()
        viewModel.setMiniCartSimplifiedData(miniCartSimplifiedData)

        //when
        viewModel.toggleUnavailableItemsAccordion()
        viewModel.toggleUnavailableItemsAccordion()

        //then
        assert(viewModel.tmpHiddenUnavailableItems.size == 0)
    }


}