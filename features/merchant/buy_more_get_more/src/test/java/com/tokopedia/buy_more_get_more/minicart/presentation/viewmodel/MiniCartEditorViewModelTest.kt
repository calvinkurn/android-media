package com.tokopedia.buy_more_get_more.minicart.presentation.viewmodel

import com.tokopedia.buy_more_get_more.minicart.domain.model.MiniCartParam
import com.tokopedia.buy_more_get_more.minicart.domain.usecase.GetGroupProductTickerUseCase
import com.tokopedia.buy_more_get_more.minicart.domain.usecase.GetMiniCartEditorDataUseCase
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmState
import com.tokopedia.buy_more_get_more.minicart.presentation.model.effect.MiniCartEditorEffect
import com.tokopedia.buy_more_get_more.minicart.presentation.model.event.MiniCartEditorEvent
import com.tokopedia.buy_more_get_more.minicart.presentation.model.state.MiniCartEditorState
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.ArgumentMatchers

/**
 * Created by @ilhamsuaib on 05/01/24.
 */

@OptIn(ExperimentalCoroutinesApi::class)
class MiniCartEditorViewModelTest : BaseCartCheckboxViewModelTest<MiniCartEditorViewModel>() {

    @RelaxedMockK
    lateinit var updateCartUseCase: UpdateCartUseCase

    @RelaxedMockK
    lateinit var deleteCartUseCase: DeleteCartUseCase

    @RelaxedMockK
    lateinit var getGroupProductTickerUseCase: GetGroupProductTickerUseCase

    @RelaxedMockK
    lateinit var getMiniCartEditorDataUseCase: GetMiniCartEditorDataUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    private val defaultState = MiniCartEditorState()

    override fun createViewModel(): MiniCartEditorViewModel {
        return MiniCartEditorViewModel(
            { getMiniCartEditorDataUseCase },
            { updateCartUseCase },
            { deleteCartUseCase },
            { getGroupProductTickerUseCase },
            { coroutineTestRule.dispatchers },
            { userSession },
            { setCartListCheckboxStateUseCase },
        )
    }

    @Test
    fun `when get user id from user session should return the user id`() {
        val dummyUserId = "1"
        every {
            userSession.userId
        } returns dummyUserId

        val actualUserId = viewModel.getUserId()

        assertEquals(dummyUserId, actualUserId)
    }

    @Test
    fun `when fetch data should fetch initial data then return successfully`() {
        runStateAndUiEffectTest { states, effects ->
            val shopIds = ArgumentMatchers.anyList<Long>()
            val param = MiniCartParam(shopIds = shopIds)
            val dummy = dummyMiniCartEditorData()

            coEvery {
                getMiniCartEditorDataUseCase.invoke(param)
            } returns dummy

            viewModel.setEvent(MiniCartEditorEvent.FetchData(param))

            assertEquals(defaultState, states[0])
            assertEquals(defaultState.updateSuccess(dummy), states[1])
        }
    }

    @Test
    fun `when fetch data should fetch initial data then return failed`() {
        runStateAndUiEffectTest { states, effects ->
            val shopIds = ArgumentMatchers.anyList<String>()
            val param = MiniCartParam(shopIds = shopIds.map { it.toLong() })
            val throwable = RuntimeException()

            coEvery {
                getMiniCartEditorDataUseCase.invoke(param)
            } throws throwable

            viewModel.setEvent(MiniCartEditorEvent.FetchData(param))

            assertEquals(defaultState, states[0])
            assertEquals(defaultState.updateError(throwable).toString(), states[1].toString())
        }
    }

    @Test
    fun `when set cart list checkbox state should invoke mutation setCartListCheckboxStateUseCase`() {
        runTest {
            `when fetch data should fetch initial data then return successfully`()

            val results = mutableListOf<BmgmState<Boolean>>()
            coEvery {
                setCartListCheckboxStateUseCase.invoke(any())
            } returns true

            val job = launch(UnconfinedTestDispatcher()) {
                viewModel.setCheckListState.toList(results)
            }

            viewModel.setEvent(MiniCartEditorEvent.SetCartListCheckboxState)

            // expected loading state first
            assertEquals(BmgmState.Loading, results[0])

            // then set to success state
            assertEquals(BmgmState.Success(true), results[1])

            job.cancel()
        }
    }

    @Test
    fun `when adjust quantity successfully, then update the state data based on group product ticker`() {
        runStateAndUiEffectTest { states, effects ->
            //fetch initial data first
            `when fetch data should fetch initial data then return successfully`()

            val product = BmgmMiniCartVisitable.ProductUiModel(cartId = "1", productId = "1")
            val newQty = 2
            val params = listOf(
                UpdateCartRequest(
                    quantity = newQty, cartId = product.cartId, productId = product.productId
                )
            )
            val source = UpdateCartUseCase.VALUE_SOURCE_UPDATE_QTY_NOTES

            val dummy = UpdateCartV2Data()
            coEvery {
                updateCartUseCase.executeOnBackground()
            } returns dummy

            val groupProductTicker = defaultState.data
            coEvery {
                getGroupProductTickerUseCase.invoke(any(), any(), any(), any())
            } returns groupProductTicker

            viewModel.setEvent(MiniCartEditorEvent.AdjustQuantity(product, newQty))

            coVerify {
                updateCartUseCase.setParams(params, source)
                updateCartUseCase.executeOnBackground()
            }

            coVerify {
                getGroupProductTickerUseCase.invoke(any(), any(), any(), any())
            }

            //assert the partial loading state
            assertEquals(states[1].updatePartiallyLoading(), states[2])

            //assert after fetching the group product ticker data
            assertEquals(states[2].updateSuccess(groupProductTicker), states[3])

        }
    }

    @Test
    fun `when adjust quantity failed, then failed to update the state data`() {
        runStateAndUiEffectTest { states, effects ->
            //fetch initial data first
            `when fetch data should fetch initial data then return successfully`()

            val product = BmgmMiniCartVisitable.ProductUiModel(cartId = "1", productId = "1")
            val newQty = 2
            val params = listOf(
                UpdateCartRequest(
                    quantity = newQty, cartId = product.cartId, productId = product.productId
                )
            )
            val source = UpdateCartUseCase.VALUE_SOURCE_UPDATE_QTY_NOTES

            //when update quantity then failed
            val exception = RuntimeException()
            coEvery {
                updateCartUseCase.executeOnBackground()
            } throws exception

            //when fetch group product ticker then failed
            val exceptionProductTicker = RuntimeException()
            coEvery {
                getGroupProductTickerUseCase.invoke(any(), any(), any(), any())
            } throws exceptionProductTicker

            viewModel.setEvent(MiniCartEditorEvent.AdjustQuantity(product, newQty))

            coVerify {
                updateCartUseCase.setParams(params, source)
                updateCartUseCase.executeOnBackground()
            }

            coVerify {
                getGroupProductTickerUseCase.invoke(any(), any(), any(), any())
            }

            //assert the partial loading state
            assertEquals(states[1].updatePartiallyLoading(), states[2])

            //assert the partial loading state dismissed
            assertEquals(states[2].dismissPartiallyLoading(), states[3])

            //assert after fetching the group product ticker data
            assertEquals(
                states[3].updateError(exceptionProductTicker).toString(),
                states[4].toString()
            )
        }
    }

    @Test
    fun `when delete cart item successfully, then update the state data based on group product ticker`() {
        runStateAndUiEffectTest { states, effects ->
            //fetch initial data first
            `when fetch data should fetch initial data then return successfully`()

            val product = BmgmMiniCartVisitable.ProductUiModel(cartId = "1", productId = "1")
            val responseDelete = RemoveFromCartData()
            coEvery {
                deleteCartUseCase.executeOnBackground()
            } returns responseDelete

            val groupProductTicker = defaultState.data
            coEvery {
                getGroupProductTickerUseCase.invoke(any(), any(), any(), any())
            } returns groupProductTicker

            viewModel.setEvent(MiniCartEditorEvent.DeleteCart(product))

            coVerify {
                deleteCartUseCase.setParams(listOf(product.cartId))
                deleteCartUseCase.executeOnBackground()
            }

            coVerify {
                getGroupProductTickerUseCase.invoke(any(), any(), any(), any())
            }

            //assert the partial loading state
            assertEquals(states[1].updatePartiallyLoading(), states[2])

            //assert after fetching the group product ticker data
            assertEquals(states[2].updateSuccess(groupProductTicker), states[3])
        }
    }

    @Test
    fun `when there is only 1 product in cart then delete cart item successfully, should not fetch the group product ticker`() {
        runStateAndUiEffectTest { states, effects ->
            //fetch initial data first with only 1 product
            val shopIds = ArgumentMatchers.anyList<Long>()
            val param = MiniCartParam(shopIds = shopIds)
            val dummy = BmgmMiniCartDataUiModel(
                tiers = listOf(
                    BmgmMiniCartVisitable.ProductUiModel(cartId = "1", productId = "1")
                )
            )
            coEvery {
                getMiniCartEditorDataUseCase.invoke(param)
            } returns dummy

            viewModel.setEvent(MiniCartEditorEvent.FetchData(param))

            //assert loading state
            assertEquals(defaultState, states[0])

            //assert success state
            assertEquals(states[0].updateSuccess(dummy), states[1])

            //delete cart item
            val product = BmgmMiniCartVisitable.ProductUiModel(cartId = "1", productId = "1")
            val responseDelete = RemoveFromCartData()
            coEvery {
                deleteCartUseCase.executeOnBackground()
            } returns responseDelete

            viewModel.setEvent(MiniCartEditorEvent.DeleteCart(product))

            coVerify {
                deleteCartUseCase.setParams(listOf(product.cartId))
                deleteCartUseCase.executeOnBackground()
            }

            //should not fetch the group product ticker
            coVerify(inverse = true) {
                getGroupProductTickerUseCase.invoke(any(), any(), any(), any())
            }

            //assert the ui effect to close the page
            assertEquals(MiniCartEditorEffect.DismissBottomSheet, effects[0])
        }
    }

    @Test
    fun `when delete cart item failed, then show the error message toaster`() {
        runStateAndUiEffectTest { states, effects ->
            //fetch initial data first
            `when fetch data should fetch initial data then return successfully`()

            val product = BmgmMiniCartVisitable.ProductUiModel(cartId = "1", productId = "1")
            val throwable = RuntimeException("delete failed")
            coEvery {
                deleteCartUseCase.executeOnBackground()
            } throws throwable

            viewModel.setEvent(MiniCartEditorEvent.DeleteCart(product))

            coVerify {
                deleteCartUseCase.setParams(listOf(product.cartId))
                deleteCartUseCase.executeOnBackground()
            }

            //assert the partial loading state
            assertEquals(states[1].updatePartiallyLoading(), states[2])

            //assert the partial loading state dismissed
            assertEquals(states[2].dismissPartiallyLoading(), states[3])

            //assert the ui effect to show the error toaster
            assertEquals(MiniCartEditorEffect.OnRemoveFailed("delete failed"), effects[0])
        }
    }

    private fun dummyMiniCartEditorData(): BmgmMiniCartDataUiModel {
        return BmgmMiniCartDataUiModel(
            tiers = listOf(
                BmgmMiniCartVisitable.ProductUiModel(cartId = "1", productId = "1"),
                BmgmMiniCartVisitable.ProductUiModel(cartId = "2", productId = "2")
            )
        )
    }

    private fun runStateAndUiEffectTest(body: TestScope.(states: List<MiniCartEditorState>, effects: List<MiniCartEditorEffect>) -> Unit) {
        runTest {
            val states = mutableListOf<MiniCartEditorState>()
            val effects = mutableListOf<MiniCartEditorEffect>()

            val collectState = launch(UnconfinedTestDispatcher()) {
                viewModel.cartData.toList(states)
            }
            val collectEffect = launch(UnconfinedTestDispatcher()) {
                viewModel.effect.toList(effects)
            }

            body(states, effects)

            collectState.cancel()
            collectEffect.cancel()
        }
    }
}