package com.tokopedia.minicart.common.widget.viewmodel.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartBundleUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.minicart.cartlist.MiniCartListUiModelMapper
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductUiModel
import com.tokopedia.minicart.chatlist.MiniCartChatListUiModelMapper
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.getMiniCartItemBundleGroup
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListUseCase
import com.tokopedia.minicart.common.domain.usecase.GetProductBundleRecomUseCase
import com.tokopedia.minicart.common.widget.MiniCartViewModel
import com.tokopedia.minicart.common.widget.viewmodel.utils.DataProvider
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserInteractionTest {

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
            userSession
        )
    }

    @Test
    fun `WHEN user change product quantity THEN quantity should be updated accordingly on bottomsheet ui model`() {
        // given
        val productId = "1920796612"
        val newQty = 5
        val productUiModel = MiniCartProductUiModel(productId = productId)
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartSimplifiedData = DataProvider.provideMiniCartSimplifiedDataAllAvailable()
        viewModel.setMiniCartSimplifiedData(miniCartSimplifiedData)

        // when
        viewModel.updateProductQty(productUiModel, newQty)

        // then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.getMiniCartProductUiModelByProductId(productId)?.productQty ?: 0 == newQty)
    }

    @Test
    fun `WHEN user change product quantity THEN quantity should be updated accordingly on widget ui model`() {
        // given
        val productId = "1920796612"
        val newQty = 5
        val productUiModel = MiniCartProductUiModel(productId = productId)
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartSimplifiedData = DataProvider.provideMiniCartSimplifiedDataAllAvailable()
        viewModel.setMiniCartSimplifiedData(miniCartSimplifiedData)

        // when
        viewModel.updateProductQty(productUiModel, newQty)

        // then
        assert(viewModel.miniCartSimplifiedData.value?.miniCartItems?.getMiniCartItemProduct(productId)?.quantity ?: 0 == newQty)
    }

    @Test
    fun `WHEN user change product quantity with invalid id THEN quantity should not be updated accordingly on widget ui model`() {
        // given
        val productId = "19207966120"
        val newQty = 5
        val productUiModel = MiniCartProductUiModel(productId = productId)
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartSimplifiedData = DataProvider.provideMiniCartSimplifiedDataAllAvailable()
        viewModel.setMiniCartSimplifiedData(miniCartSimplifiedData)

        // when
        viewModel.updateProductQty(productUiModel, newQty)

        // then
        assert(viewModel.miniCartSimplifiedData.value?.miniCartItems == miniCartSimplifiedData.miniCartItems)
    }

    @Test
    fun `WHEN user change product notes THEN notes should be updated accordingly on bottomsheet ui model`() {
        // given
        val productId = "1920796612"
        val isBundling = false
        val bundleId = ""
        val bundleGroupId = ""
        val newNotes = "new notes"
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartSimplifiedData = DataProvider.provideMiniCartSimplifiedDataAllAvailable()
        viewModel.setMiniCartSimplifiedData(miniCartSimplifiedData)

        // when
        viewModel.updateProductNotes(productId, isBundling, bundleId, bundleGroupId, newNotes)

        // then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.getMiniCartProductUiModelByProductId(productId)?.productNotes ?: 0 == newNotes)
    }

    @Test
    fun `WHEN user change product notes THEN notes should be updated accordingly on widget ui model`() {
        // given
        val productId = "1920796612"
        val isBundling = false
        val bundleId = ""
        val bundleGroupId = ""
        val newNotes = "new notes"
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartSimplifiedData = DataProvider.provideMiniCartSimplifiedDataAllAvailable()
        viewModel.setMiniCartSimplifiedData(miniCartSimplifiedData)

        // when
        viewModel.updateProductNotes(productId, isBundling, bundleId, bundleGroupId, newNotes)

        // then
        assert(viewModel.miniCartSimplifiedData.value?.miniCartItems?.getMiniCartItemProduct(productId)?.notes ?: 0 == newNotes)
    }

    @Test
    fun `WHEN user change product bundle notes THEN notes should be updated accordingly on bottomsheet ui model`() {
        // given
        val productId = "2148476278"
        val isBundling = true
        val bundleId = "36012"
        val bundleGroupId = "bid:36012-pid:2148476278-pid1:2148476278"
        val newNotes = "new notes"
        val miniCartListUiModel = DataProvider.provideMiniCartBundleListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartSimplifiedData = DataProvider.provideGetMiniCartBundleSimplifiedSuccessAllAvailable()
        viewModel.setMiniCartSimplifiedData(miniCartSimplifiedData)

        // when
        viewModel.updateProductNotes(productId, isBundling, bundleId, bundleGroupId, newNotes)

        // then
        assert(viewModel.miniCartListBottomSheetUiModel.value?.getMiniCartProductUiModelByProductId(productId)?.productNotes ?: 0 == newNotes)
    }

    @Test
    fun `WHEN user change product bundle notes THEN notes should be updated accordingly on widget ui model`() {
        // given
        val productId = "2148476278"
        val isBundling = true
        val bundleId = "36012"
        val bundleGroupId = "bid:36012-pid:2148476278-pid1:2148476278"
        val newNotes = "new notes"
        val miniCartListUiModel = DataProvider.provideMiniCartBundleListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartSimplifiedData = DataProvider.provideGetMiniCartBundleSimplifiedSuccessAllAvailable()
        viewModel.setMiniCartSimplifiedData(miniCartSimplifiedData)

        // when
        viewModel.updateProductNotes(productId, isBundling, bundleId, bundleGroupId, newNotes)

        // then
        val miniCartItemBundle = viewModel.miniCartSimplifiedData.value?.miniCartItems?.getMiniCartItemBundleGroup(bundleGroupId)
        val productBundle = miniCartItemBundle?.products?.get(MiniCartItemKey(productId))

        assert(productBundle?.notes ?: 0 == newNotes)
    }

    @Test
    fun `WHEN user change product bundle notes with invalid id THEN notes should not be updated accordingly on widget ui model`() {
        // given
        val productId = "21484762780"
        val isBundling = true
        val bundleId = "360120"
        val bundleGroupId = "bid:360120-pid:2148476278-pid1:2148476278"
        val newNotes = "new notes"
        val miniCartListUiModel = DataProvider.provideMiniCartBundleListUiModelAllAvailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartSimplifiedData = DataProvider.provideGetMiniCartBundleSimplifiedSuccessAllAvailable()
        viewModel.setMiniCartSimplifiedData(miniCartSimplifiedData)

        // when
        viewModel.updateProductNotes(productId, isBundling, bundleId, bundleGroupId, newNotes)

        // then
        val miniCartItemBundle = viewModel.miniCartSimplifiedData.value?.miniCartItems

        assert(miniCartItemBundle == miniCartSimplifiedData.miniCartItems)
    }

    @Test
    fun `WHEN user toggle accordion from expanded to collapsed THEN temporary collapsed data should not be empty`() {
        // given
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAllUnavailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartSimplifiedData = DataProvider.provideMiniCartSimplifiedDataAllUnavailable()
        viewModel.setMiniCartSimplifiedData(miniCartSimplifiedData)

        // when
        viewModel.toggleUnavailableItemsAccordion()

        // then
        assert(viewModel.tmpHiddenUnavailableItems.size > 0)
    }

    @Test
    fun `WHEN user toggle accordion from collapsed to expanded THEN temporary collapsed data should be empty`() {
        // given
        val miniCartListUiModel = DataProvider.provideMiniCartListUiModelAvailableAndUnavailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModel)
        val miniCartSimplifiedData = DataProvider.provideMiniCartSimplifiedDataAllUnavailable()
        viewModel.setMiniCartSimplifiedData(miniCartSimplifiedData)

        // when
        viewModel.toggleUnavailableItemsAccordion()
        viewModel.toggleUnavailableItemsAccordion()

        // then
        assert(viewModel.tmpHiddenUnavailableItems.size == 0)
    }

    @Test
    fun `WHEN close bottom sheet and get latest mini cart data with collapsed unavailable items THEN cart item count should be correct`() {
        // given
        val miniCartListUiModels = DataProvider.provideMiniCartListUiModelAvailableAndUnavailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModels)
        val unavailableItem = MiniCartProductUiModel()
        viewModel.tmpHiddenUnavailableItems.clear()
        viewModel.tmpHiddenUnavailableItems.add(unavailableItem.copy(productId = "1"))
        viewModel.tmpHiddenUnavailableItems.add(unavailableItem.copy(productId = "2"))

        val expectedSize = 7

        // when
        val value = viewModel.getLatestMiniCartData()

        // then
        assert(value.miniCartItems.size == expectedSize)
    }

    @Test
    fun `WHEN close bottom sheet and get latest mini cart data without collapsed unavailable items THEN cart item count should be correct`() {
        // given
        val miniCartListUiModels = DataProvider.provideMiniCartListUiModelAvailableAndUnavailable()
        viewModel.setMiniCartListUiModel(miniCartListUiModels)

        val expectedSize = 5

        // when
        val value = viewModel.getLatestMiniCartData()

        // then
        assert(value.miniCartItems.size == expectedSize)
    }
}
