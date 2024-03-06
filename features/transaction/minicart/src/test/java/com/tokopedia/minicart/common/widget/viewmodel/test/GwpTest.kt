package com.tokopedia.minicart.common.widget.viewmodel.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartBundleUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.model.bmgm.response.BmGmGetGroupProductTickerResponse
import com.tokopedia.cartcommon.domain.usecase.BmGmGetGroupProductTickerUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.minicart.cartlist.MiniCartListGwpUiModelMapper.getGwpErrorState
import com.tokopedia.minicart.cartlist.MiniCartListGwpUiModelMapper.getGwpSuccessState
import com.tokopedia.minicart.cartlist.MiniCartListUiModelMapper
import com.tokopedia.minicart.chatlist.MiniCartChatListUiModelMapper
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GwpTest {
    private lateinit var viewModel: MiniCartViewModel

    private var dispatcher: CoroutineDispatchers = CoroutineTestDispatchersProvider

    private var getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase = mockk()
    private val getMiniCartListUseCase: GetMiniCartListUseCase = mockk()
    private val deleteCartUseCase: DeleteCartUseCase = mockk()
    private val undoDeleteCartUseCase: UndoDeleteCartUseCase = mockk()
    private val updateCartUseCase: UpdateCartUseCase = mockk()
    private val addToCartOccMultiUseCase: AddToCartOccMultiUseCase = mockk()
    private val getProductBundleRecomUseCase: GetProductBundleRecomUseCase = mockk()
    private val addToCartBundleUseCase: AddToCartBundleUseCase = mockk()
    private var miniCartListUiModelMapper: MiniCartListUiModelMapper = spyk()
    private var miniCartChatListUiModelMapper: MiniCartChatListUiModelMapper = spyk()
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

    private fun stubGetMiniCartListParams() = coEvery {
        getMiniCartListUseCase.setParams((any()))
    } just Runs

    private fun stubUpdateCartParams() = coEvery {
        updateCartUseCase.setParams(any(), any())
    } just Runs

    private fun stubGetMiniCartResponse(response: MiniCartData) =  coEvery {
        getMiniCartListUseCase.execute(any(), any())
    }  answers {
        firstArg<(MiniCartData) -> Unit>().invoke(response)
    }

    private fun stubUpdateCartResponse(response: UpdateCartV2Data) = coEvery {
        updateCartUseCase.execute(any(), any())
    } answers {
        firstArg<(UpdateCartV2Data) -> Unit>().invoke(response)
    }

    private fun stubUpdateGwpResponse(response: BmGmGetGroupProductTickerResponse) = coEvery {
        updateGwpUseCase.invoke(any())
    } returns response

    private fun stubUpdateGwpResponse(throwable: Throwable) = coEvery {
        updateGwpUseCase.invoke(any())
    } throws throwable

    @Test
    fun `WHEN update gwp success THEN mini cart list bottom sheet should be updated with success result`() {
        // provide data response
        val getMiniCartResponse = DataProvider.provideGetMiniCartGwpSuccess()
        val updateCartResponse = DataProvider.provideUpdateCartSuccess()
        val updateGwpResponse = DataProvider.provideUpdateGwpSuccess()

        // stub data getMiniCart, updateCart and updateGwp
        stubGetMiniCartListParams()
        stubGetMiniCartResponse(getMiniCartResponse)

        stubUpdateCartParams()
        stubUpdateCartResponse(updateCartResponse)

        stubUpdateGwpResponse(updateGwpResponse)

        // get mini cart list ui model and set gwp params
        viewModel.getCartList()

        val baseUiModel = viewModel.miniCartListBottomSheetUiModel.value?.copy()

        // update cart and automatically update gwp when the process success updated
        viewModel.updateCart(getMiniCartResponse.data.availableSection.availableGroup.first().cartDetails.first().cartDetailInfo.bmgmData.offerId)

        val resultUiModel = getGwpSuccessState(
            uiModel = baseUiModel,
            response = updateGwpResponse
        )

        assertNotEquals(baseUiModel, viewModel.miniCartListBottomSheetUiModel.value)
        assertEquals(resultUiModel, viewModel.miniCartListBottomSheetUiModel.value)
    }

    @Test
    fun `WHEN update gwp error THEN mini cart list bottom sheet should be updated with error result`() {
        // provide data response
        val getMiniCartResponse = DataProvider.provideGetMiniCartGwpSuccess()
        val updateCartResponse = DataProvider.provideUpdateCartSuccess()

        // stub data getMiniCart, updateCart and updateGwp
        stubGetMiniCartListParams()
        stubGetMiniCartResponse(getMiniCartResponse)

        stubUpdateCartParams()
        stubUpdateCartResponse(updateCartResponse)

        stubUpdateGwpResponse(Throwable())

        // get mini cart list ui model and set gwp params
        viewModel.getCartList()

        val baseUiModel = viewModel.miniCartListBottomSheetUiModel.value?.copy()
        val offerId = getMiniCartResponse.data.availableSection.availableGroup.first().cartDetails.first().cartDetailInfo.bmgmData.offerId

        // update cart and automatically update gwp when the process success updated
        viewModel.updateCart(offerId)

        val resultUiModel = getGwpErrorState(
            uiModel = baseUiModel,
            offerId = offerId
        )

        assertNotEquals(baseUiModel, viewModel.miniCartListBottomSheetUiModel.value)
        assertEquals(resultUiModel, viewModel.miniCartListBottomSheetUiModel.value)
    }

    @Test
    fun `WHEN update gwp error THEN refresh the update process, mini cart list bottom sheet should be updated with success result`() {
        // provide data response
        val getMiniCartResponse = DataProvider.provideGetMiniCartGwpSuccess()
        val updateCartResponse = DataProvider.provideUpdateCartSuccess()
        val updateGwpResponse = DataProvider.provideUpdateGwpSuccess()

        // stub data getMiniCart and updateCart with data response and updateGwp with throwable
        stubGetMiniCartListParams()
        stubGetMiniCartResponse(getMiniCartResponse)

        stubUpdateCartParams()
        stubUpdateCartResponse(updateCartResponse)

        stubUpdateGwpResponse(Throwable())

        // get mini cart list ui model and set gwp params
        viewModel.getCartList()

        val baseUiModel = viewModel.miniCartListBottomSheetUiModel.value?.copy()
        val offerId = getMiniCartResponse.data.availableSection.availableGroup.first().cartDetails.first().cartDetailInfo.bmgmData.offerId

        // update cart and automatically update gwp when the process success updated
        viewModel.updateCart(offerId)

        val resultErrorUiModel = getGwpErrorState(
            uiModel = baseUiModel,
            offerId = offerId
        )

        assertNotEquals(baseUiModel, viewModel.miniCartListBottomSheetUiModel.value)
        assertEquals(resultErrorUiModel, viewModel.miniCartListBottomSheetUiModel.value)

        // stub updateGwp with data response
        stubUpdateGwpResponse(updateGwpResponse)

        // get mini cart list ui model and set gwp params
        viewModel.refreshGwpWidget(offerId)

        val resultSuccessUiModel = getGwpSuccessState(
            uiModel = baseUiModel,
            response = updateGwpResponse
        )

        assertNotEquals(baseUiModel, viewModel.miniCartListBottomSheetUiModel.value)
        assertEquals(resultSuccessUiModel, viewModel.miniCartListBottomSheetUiModel.value)
    }
}
