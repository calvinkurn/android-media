package com.tokopedia.minicart.v2

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.model.response.AddToCartOccMultiData
import com.tokopedia.atc_common.domain.model.response.AddToCartOccMultiDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.cartcommon.data.response.common.OutOfService
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartABTestData
import com.tokopedia.minicart.common.domain.data.MiniCartCheckoutData
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.common.widget.viewmodel.utils.DataProvider
import com.tokopedia.minicart.domain.GetMiniCartParam
import com.tokopedia.minicart.domain.GetMiniCartWidgetUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class MiniCartV2ViewModelTest {

    private lateinit var viewModel: MiniCartV2ViewModel
    private var dispatcher: CoroutineDispatchers = CoroutineTestDispatchersProvider

    private var getMiniCartWidgetUseCase: GetMiniCartWidgetUseCase = mockk()
    private val updateCartUseCase: UpdateCartUseCase = mockk()
    private val addToCartOccMultiUseCase: AddToCartOccMultiUseCase = mockk()

    @get: Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = MiniCartV2ViewModel(
            dispatcher,
            getMiniCartWidgetUseCase,
            updateCartUseCase,
            addToCartOccMultiUseCase
        )
    }

    @Test
    fun `WHEN fetch last widget state success THEN data should be initialized`() {
        // given
        val mockResponse = DataProvider.provideGetMiniCartBundleSimplifiedSuccessAllAvailable()
        coEvery { getMiniCartWidgetUseCase.invoke(any()) } returns mockResponse

        // when
        viewModel.getLatestWidgetState(
            GetMiniCartParam(
                emptyList(),
                MiniCartSource.TokonowHome.value
            )
        )

        // then
        assert(viewModel.miniCartSimplifiedData.value == mockResponse)
    }

    @Test
    fun `WHEN fetch last widget state success THEN ab test should be initialized`() {
        // given
        val mockResponse = DataProvider.provideGetMiniCartBundleSimplifiedSuccessAllAvailable()
        coEvery { getMiniCartWidgetUseCase.invoke(any()) } returns mockResponse

        // when
        viewModel.getLatestWidgetState(
            GetMiniCartParam(
                emptyList(),
                MiniCartSource.TokonowHome.value
            )
        )

        // then
        assert(viewModel.miniCartABTestData.value == MiniCartABTestData(false, "Beli"))
    }

    @Test
    fun `WHEN fetch last widget state error THEN global event should be updated accordingly`() {
        runTest {
            // given
            val error = IOException()
            coEvery { getMiniCartWidgetUseCase.invoke(any()) } throws error

            var lastGlobalEvent: MiniCartV2GlobalEvent? = null
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.globalEvent.collect {
                    lastGlobalEvent = it
                }
            }

            // when
            viewModel.getLatestWidgetState(
                GetMiniCartParam(
                    emptyList(),
                    MiniCartSource.TokonowHome.value
                )
            )

            // then
            assert(lastGlobalEvent == MiniCartV2GlobalEvent.FailToLoadMiniCart(error))
        }
    }

    @Test
    fun `WHEN go to checkout by atc from mini cart widget success THEN global event state should be updated accordingly`() {
        runTest {
            // given
            val miniCartSimplifiedData =
                DataProvider.provideMiniCartSimplifiedDataBundleAvailableAndUnavailable()
            viewModel.updateMiniCartSimplifiedData(miniCartSimplifiedData)
            viewModel.setMiniCartABTestData(
                isOCCFlow = true,
                buttonBuyWording = "Beli Langsung"
            )

            coEvery { addToCartOccMultiUseCase.setParams(any()) } answers { addToCartOccMultiUseCase }
            coEvery { addToCartOccMultiUseCase.execute(any(), any()) } answers {
                firstArg<(AddToCartOccMultiDataModel) -> Unit>().invoke(
                    AddToCartOccMultiDataModel(
                        status = "OK",
                        data = AddToCartOccMultiData(success = 1)
                    )
                )
            }

            var lastGlobalEvent: MiniCartV2GlobalEvent? = null
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.globalEvent.collect {
                    lastGlobalEvent = it
                }
            }

            // when
            viewModel.goToCheckout()

            // then
            assert(lastGlobalEvent == MiniCartV2GlobalEvent.SuccessGoToCheckout)
        }
    }

    @Test
    fun `WHEN go to checkout by atc from mini cart widget failed THEN global event state should be updated accordingly`() {
        runTest {
            // given
            val miniCartSimplifiedData =
                DataProvider.provideMiniCartSimplifiedDataBundleAvailableAndUnavailable()
            viewModel.updateMiniCartSimplifiedData(miniCartSimplifiedData)
            viewModel.setMiniCartABTestData(
                isOCCFlow = true,
                buttonBuyWording = "Beli Langsung"
            )

            val error = "something error"

            coEvery { addToCartOccMultiUseCase.setParams(any()) } answers { addToCartOccMultiUseCase }
            coEvery { addToCartOccMultiUseCase.execute(any(), any()) } answers {
                firstArg<(AddToCartOccMultiDataModel) -> Unit>().invoke(
                    AddToCartOccMultiDataModel(
                        status = "OK",
                        data = AddToCartOccMultiData(
                            success = 0,
                            message = listOf(error)
                        )
                    )
                )
            }

            var lastGlobalEvent: MiniCartV2GlobalEvent? = null
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.globalEvent.collect {
                    lastGlobalEvent = it
                }
            }

            // when
            viewModel.goToCheckout()

            // then
            assert(lastGlobalEvent == MiniCartV2GlobalEvent.FailGoToCheckout(data = MiniCartCheckoutData(error)))
        }
    }

    @Test
    fun `WHEN go to checkout by atc from mini cart widget error THEN global event state should be updated accordingly`() {
        runTest {
            // given
            val miniCartSimplifiedData =
                DataProvider.provideMiniCartSimplifiedDataBundleAvailableAndUnavailable()
            viewModel.updateMiniCartSimplifiedData(miniCartSimplifiedData)
            viewModel.setMiniCartABTestData(
                isOCCFlow = true,
                buttonBuyWording = "Beli Langsung"
            )

            val error = IOException()

            coEvery { addToCartOccMultiUseCase.setParams(any()) } answers { addToCartOccMultiUseCase }
            coEvery { addToCartOccMultiUseCase.execute(any(), any()) } answers {
                secondArg<(Throwable) -> Unit>().invoke(
                    error
                )
            }

            var lastGlobalEvent: MiniCartV2GlobalEvent? = null
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.globalEvent.collect {
                    lastGlobalEvent = it
                }
            }

            // when
            viewModel.goToCheckout()

            // then
            assert(lastGlobalEvent == MiniCartV2GlobalEvent.FailGoToCheckout(throwable = error))
        }
    }

    @Test
    fun `WHEN go to checkout by update cart from mini cart widget success THEN global event state should be updated accordingly`() {
        runTest {
            // given
            val miniCartSimplifiedData =
                DataProvider.provideMiniCartSimplifiedDataBundleAvailableAndUnavailable()
            viewModel.updateMiniCartSimplifiedData(miniCartSimplifiedData)
            viewModel.setMiniCartABTestData(
                isOCCFlow = false,
                buttonBuyWording = "Beli"
            )

            val mockResponse = DataProvider.provideUpdateCartSuccess()
            coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
            coEvery { updateCartUseCase.execute(any(), any()) } answers {
                firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
            }

            var lastGlobalEvent: MiniCartV2GlobalEvent? = null
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.globalEvent.collect {
                    lastGlobalEvent = it
                }
            }

            // when
            viewModel.goToCheckout()

            // then
            assert(lastGlobalEvent == MiniCartV2GlobalEvent.SuccessGoToCheckout)
        }
    }

    @Test
    fun `WHEN go to checkout by update cart from mini cart widget failed THEN global event state should be updated accordingly`() {
        runTest {
            // given
            val miniCartSimplifiedData =
                DataProvider.provideMiniCartSimplifiedDataBundleAvailableAndUnavailable()
            viewModel.updateMiniCartSimplifiedData(miniCartSimplifiedData)
            viewModel.setMiniCartABTestData(
                isOCCFlow = false,
                buttonBuyWording = "Beli"
            )

            val mockResponse = DataProvider.provideUpdateCartFailed()
            coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
            coEvery { updateCartUseCase.execute(any(), any()) } answers {
                firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
            }

            var lastGlobalEvent: MiniCartV2GlobalEvent? = null
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.globalEvent.collect {
                    lastGlobalEvent = it
                }
            }

            // when
            viewModel.goToCheckout()

            // then
            assert(lastGlobalEvent == MiniCartV2GlobalEvent.FailGoToCheckout(data = MiniCartCheckoutData(outOfService = OutOfService(id = "0"))))
        }
    }

    @Test
    fun `WHEN go to checkout by update cart from mini cart widget error THEN global event state should be updated accordingly`() {
        runTest {
            // given
            val miniCartSimplifiedData =
                DataProvider.provideMiniCartSimplifiedDataBundleAvailableAndUnavailable()
            viewModel.updateMiniCartSimplifiedData(miniCartSimplifiedData)
            viewModel.setMiniCartABTestData(
                isOCCFlow = false,
                buttonBuyWording = "Beli"
            )

            val error = IOException()
            coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
            coEvery { updateCartUseCase.execute(any(), any()) } answers {
                secondArg<(Throwable) -> Unit>().invoke(error)
            }

            var lastGlobalEvent: MiniCartV2GlobalEvent? = null
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.globalEvent.collect {
                    lastGlobalEvent = it
                }
            }

            // when
            viewModel.goToCheckout()

            // then
            assert(lastGlobalEvent == MiniCartV2GlobalEvent.FailGoToCheckout(throwable = error))
        }
    }
}
