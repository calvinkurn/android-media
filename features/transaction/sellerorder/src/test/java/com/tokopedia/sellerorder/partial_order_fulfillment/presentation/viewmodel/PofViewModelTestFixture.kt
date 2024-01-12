package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.viewmodel

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.orderextension.presentation.model.StringRes
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.GetPofEstimateRequestParams
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.GetPofInfoRequestParams
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.GetPofRequestEstimateResponse
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.GetPofRequestInfoResponse
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.RequestState
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.SendPofRequestParams
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.SendPofResponse
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.usecase.GetPofEstimateUseCase
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.usecase.GetPofInfoUseCase
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.usecase.SendPofUseCase
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofDescriptionUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofErrorStateUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofProductEditableUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofProductListHeaderUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofThinDividerUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofVisitable
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.mapper.PofToasterMapper
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.mapper.PofUiStateMapper
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.PofFooterUiState
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.ToasterQueue
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.UiEffect
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.UiEvent
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.UiState
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.tracker.PofTracker
import com.tokopedia.sellerorder.util.TestHelper
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.MockKAnnotations
import io.mockk.MockKMatcherScope
import io.mockk.coEvery
import io.mockk.coExcludeRecords
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
abstract class PofViewModelTestFixture {

    companion object {
        const val SUCCESS_RESPONSE_GET_POF_ESTIMATE_RESULT = "json/get_pof_estimate_result.json"
        const val SUCCESS_RESPONSE_GET_POF_ESTIMATE_RESULT_WITH_FIRST_PRODUCT_ZERO = "json/get_pof_estimate_result.json"
        const val SUCCESS_RESPONSE_GET_POF_INFO_RESULT_WITH_STATUS_0 = "json/get_pof_info_result_with_status_0.json"
        const val SUCCESS_RESPONSE_SEND_POF = "json/send_pof_success.json"
    }

    @RelaxedMockK
    lateinit var getPofInfoUseCase: GetPofInfoUseCase

    @RelaxedMockK
    lateinit var getPofEstimateUseCase: GetPofEstimateUseCase

    @RelaxedMockK
    lateinit var sendPofUseCase: SendPofUseCase

    @RelaxedMockK
    lateinit var bundle: Bundle

    @RelaxedMockK
    lateinit var tracker: PofTracker

    @get:Rule
    val rule = UnconfinedTestRule()

    lateinit var viewModel: PofViewModel

    private val throwable: Throwable = Throwable()

    protected val errorVisitableListWithStatus0: List<PofVisitable> = createErrorVisitableListWithStatus0()
    protected val initialVisitableListWithStatus0: List<PofVisitable> = createInitialVisitableListWithStatus0()
    protected val initialLoadErrorFooterUiStateWithStatus0: PofFooterUiState = createInitialLoadErrorFooterUiStateWithStatus0()
    protected val reFetchErrorFooterUiStateWithStatus0: PofFooterUiState = createReFetchErrorFooterUiStateWithStatus0()

    protected val toasterQueueCannotExceedCheckoutQuantity: ToasterQueue = createToasterQueueCannotExceedCheckoutQuantity()
    protected val toasterQueueCannotEmptyAllQuantity: ToasterQueue = createToasterQueueCannotEmptyAllQuantity()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = PofViewModel(
            getPofInfoUseCase,
            getPofEstimateUseCase,
            sendPofUseCase,
            PofUiStateMapper(),
            PofToasterMapper(),
            tracker
        )
    }

    @After
    fun cleanup() {
        unmockkAll()
        viewModel.viewModelScope.coroutineContext.cancelChildren()
        unmockkAll()
    }

    private fun createErrorVisitableListWithStatus0(): List<PofVisitable> {
        return listOf(
            PofDescriptionUiModel(StringRes(R.string.som_pof_description_initial), UiEvent.OnClickDescriptionLearnMore),
            PofErrorStateUiModel(throwable)
        ).toList()
    }

    private fun createInitialVisitableListWithStatus0(): List<PofVisitable> {
        return listOf(
            PofDescriptionUiModel(StringRes(R.string.som_pof_description_initial), UiEvent.OnClickDescriptionLearnMore),
            PofProductListHeaderUiModel(
                textLeft = StringRes(R.string.som_pof_product_list_header_left_label),
                textRight = StringRes(R.string.som_pof_product_list_header_right_label)
            ),
            PofThinDividerUiModel(
                R.dimen.som_pof_divider_margin_edge_to_edge,
                R.dimen.som_pof_divider_margin_edge_to_edge,
                R.dimen.som_pof_divider_margin_edge_to_edge,
                R.dimen.som_pof_divider_margin_edge_to_edge
            )
        ).plus(
            /**
             * This should be the mimic the [PofUiStateMapper.includeEditableProductList]
             */
            mutableListOf<PofVisitable>().apply {
                val detailsOriginal = TestHelper
                    .createSuccessResponse<GetPofRequestInfoResponse.Data>(
                        SUCCESS_RESPONSE_GET_POF_INFO_RESULT_WITH_STATUS_0
                    )
                    .infoRequestPartialOrderFulfillment
                    ?.detailsOriginal
                detailsOriginal
                    ?.forEachIndexed { index, details ->
                        val checkoutQuantity = details?.quantityCheckout.orZero()
                        val availableQuantity = details?.quantityCheckout.orZero()
                        val minQuantity = if (detailsOriginal.count() == 1) Int.ONE else Int.ZERO
                        add(
                            PofProductEditableUiModel(
                                orderDetailId = details?.orderDetailId.orZero(),
                                productImageUrl = details?.productPicture.orEmpty(),
                                productName = details?.productName.orEmpty(),
                                productPriceQuantity = "$checkoutQuantity x ${details?.productPrice.orEmpty()}",
                                quantityEditorData = PofProductEditableUiModel.QuantityEditorData(
                                    orderDetailId = details?.orderDetailId.orZero(),
                                    productId = details?.productId.orZero(),
                                    quantity = availableQuantity,
                                    maxQuantity = checkoutQuantity,
                                    minQuantity = minQuantity,
                                    updateTimestamp = 0,
                                    enabled = true
                                )
                            )
                        )
                        if (index < detailsOriginal.count().dec()) {
                            add(
                                PofThinDividerUiModel(
                                    R.dimen.som_pof_divider_margin_edge_to_edge,
                                    R.dimen.som_pof_divider_margin_edge_to_edge,
                                    R.dimen.som_pof_divider_margin_edge_to_edge,
                                    R.dimen.som_pof_divider_margin_edge_to_edge
                                )
                            )
                        }
                    }
            }
        ).toList()
    }

    private fun createInitialLoadErrorFooterUiStateWithStatus0(): PofFooterUiState {
        return PofFooterUiState.InitialLoadError(
            priceEstimationData = PofFooterUiState.PriceEstimationData(
                title = StringRes(R.string.som_pof_footer_title_no_counter),
                price = StringRes(R.string.som_pof_footer_price_no_value)
            ),
            buttonData = PofFooterUiState.SendPofButtonData(
                buttonText = StringRes(R.string.som_pof_footer_button_text_confirm_no_counter),
                show = true,
                isButtonLoading = false,
                isButtonEnabled = false
            )
        )
    }

    private fun createReFetchErrorFooterUiStateWithStatus0(): PofFooterUiState {
        val pofInfo = TestHelper.createSuccessResponse<GetPofRequestInfoResponse.Data>(
            SUCCESS_RESPONSE_GET_POF_INFO_RESULT_WITH_STATUS_0
        )
        val quantity = pofInfo
            .infoRequestPartialOrderFulfillment
            ?.detailsOriginal
            ?.sumOf { it?.quantityCheckout.orZero() }
            .orZero()
        return PofFooterUiState.ErrorReFetch(
            priceEstimationData = PofFooterUiState.PriceEstimationData(
                title = StringRes(
                    R.string.som_pof_footer_title_with_counter,
                    listOf(quantity, quantity)
                ),
                price = StringRes(R.string.som_pof_footer_price_reload),
                showIcon = true,
                icon = IconUnify.RELOAD,
                iconEventData = UiEvent.OnClickRetryFetchPofEstimate,
                iconSize = R.dimen.som_pof_footer_icon_size_reload,
                iconWrapped = true,
            ),
            buttonData = PofFooterUiState.SendPofButtonData(
                buttonText = StringRes(
                    R.string.som_pof_footer_button_text_confirm_with_counter,
                    listOf(quantity)
                ),
                show = true,
                isButtonLoading = false,
                isButtonEnabled = false
            )
        )
    }

    protected fun createFooterUiState(
        pofInfoResponseFilePath: String = SUCCESS_RESPONSE_GET_POF_INFO_RESULT_WITH_STATUS_0,
        pofEstimateResponseFilePath: String = SUCCESS_RESPONSE_GET_POF_ESTIMATE_RESULT,
        quantityList: List<Pair<Long, Int>> = listOf()
    ): PofFooterUiState {
        val pofInfo = TestHelper.createSuccessResponse<GetPofRequestInfoResponse.Data>(
            pofInfoResponseFilePath
        )
        val estimationData = TestHelper.createSuccessResponse<GetPofRequestEstimateResponse.Data>(
            pofEstimateResponseFilePath
        )
        val quantityRequest = pofInfo
            .infoRequestPartialOrderFulfillment
            ?.detailsOriginal
            ?.sumOf { details ->
                quantityList.find {
                    it.first == details?.orderDetailId
                }?.second ?: details?.quantityCheckout.orZero()
            }
            .orZero()
        val quantityCheckout = pofInfo
            .infoRequestPartialOrderFulfillment
            ?.detailsOriginal
            ?.sumOf { it?.quantityCheckout.orZero() }
            .orZero()
        return PofFooterUiState.ShowingPofRequestData(
            priceEstimationData = PofFooterUiState.PriceEstimationData(
                title = StringRes(
                    R.string.som_pof_footer_title_with_counter,
                    listOf(quantityRequest, quantityCheckout)
                ),
                price = StringRes(
                    R.string.som_pof_footer_price_with_value,
                    listOf(estimationData.partialOrderFulfillmentRequestEstimate?.pofFinalEstimation?.valueString.orEmpty())
                ),
                showIcon = true,
                icon = IconUnify.CHEVRON_UP,
                iconEventData = UiEvent.OnClickOpenPofInfoSummary
            ),
            buttonData = PofFooterUiState.SendPofButtonData(
                buttonText = StringRes(
                    R.string.som_pof_footer_button_text_confirm_with_counter,
                    listOf(quantityRequest)
                ),
                show = true,
                isButtonLoading = false,
                isButtonEnabled = quantityRequest != quantityCheckout,
                eventData = UiEvent.OnClickSendPof(
                    pofDetailList = pofInfo
                        .infoRequestPartialOrderFulfillment
                        ?.detailsOriginal
                        ?.map { details ->
                            SendPofRequestParams.PofDetail(
                                orderDetailId = details?.orderDetailId.orZero(),
                                quantityRequest = quantityList.find {
                                    it.first == details?.orderDetailId
                                }?.second ?: details?.quantityCheckout.orZero()
                            )
                        }
                        .orEmpty()
                )
            )
        )
    }

    private fun createToasterQueueCannotExceedCheckoutQuantity(): ToasterQueue {
        return ToasterQueue(text = StringRes(R.string.som_pof_toaster_error_cannot_exceed_checkout_quantity))
    }

    private fun createToasterQueueCannotEmptyAllQuantity(): ToasterQueue {
        return ToasterQueue(text = StringRes(R.string.som_pof_toaster_error_cannot_empty_all_products))
    }

    protected fun runCollecting(
        block: suspend TestScope.(List<UiState>, List<ToasterQueue>, List<UiEffect>) -> Unit
    ) = runTest {
        val uiStates = mutableListOf<UiState>()
        val toasterQueue = mutableListOf<ToasterQueue>()
        val uiEffect = mutableListOf<UiEffect>()
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        val uiStateCollectorJob = backgroundScope.launch(dispatcher) {
            viewModel.uiState.toList(uiStates)
        }
        val toasterQueueCollectorJob = backgroundScope.launch(dispatcher) {
            viewModel.toasterQueue.toList(toasterQueue)
        }
        val uiEffectCollectorJob = backgroundScope.launch(dispatcher) {
            viewModel.uiEffect.toList(uiEffect)
        }
        block(uiStates, toasterQueue, uiEffect)
        uiStateCollectorJob.cancel()
        toasterQueueCollectorJob.cancel()
        uiEffectCollectorJob.cancel()
    }

    protected fun createGetPofInfoParams(
        orderId: Long = 167756654,
        delay: Long = 500
    ): GetPofInfoRequestParams {
        return GetPofInfoRequestParams(
            orderId = orderId,
            delay = delay
        )
    }

    protected fun createGetPofEstimateParams(
        orderId: Long = 167756654,
        delay: Long = 0,
        detailInfo: List<GetPofEstimateRequestParams.DetailInfo?> = TestHelper
            .createSuccessResponse<GetPofRequestInfoResponse.Data>(
                SUCCESS_RESPONSE_GET_POF_INFO_RESULT_WITH_STATUS_0
            )
            .infoRequestPartialOrderFulfillment
            ?.detailsOriginal
            ?.map { details ->
                GetPofEstimateRequestParams.DetailInfo(
                    orderDetailId = details?.orderDetailId.orZero(),
                    productId = details?.productId.orZero(),
                    quantityRequest = details?.quantityRequest.orZero()
                )
            }.orEmpty()
    ): GetPofEstimateRequestParams {
        return GetPofEstimateRequestParams(
            detailInfo = detailInfo,
            orderId = orderId,
            delay = delay
        )
    }

    protected fun createGetPofEstimateParamsWithDefinedQuantity(
        orderId: Long = 167756654,
        delay: Long = 0,
        quantityList: List<Pair<Long, Int>> = listOf()
    ): GetPofEstimateRequestParams {
        return GetPofEstimateRequestParams(
            detailInfo = TestHelper
                .createSuccessResponse<GetPofRequestInfoResponse.Data>(
                    SUCCESS_RESPONSE_GET_POF_INFO_RESULT_WITH_STATUS_0
                )
                .infoRequestPartialOrderFulfillment
                ?.detailsOriginal
                ?.map { details ->
                    val orderDetailId = details?.orderDetailId.orZero()
                    GetPofEstimateRequestParams.DetailInfo(
                        orderDetailId = orderDetailId,
                        productId = details?.productId.orZero(),
                        quantityRequest = quantityList.find {
                            it.first == orderDetailId
                        }?.second ?: details?.quantityRequest.orZero()
                    )
                }.orEmpty(),
            orderId = orderId,
            delay = delay
        )
    }

    protected fun sendOpenScreenEvent() {
        viewModel.onEvent(UiEvent.OpenScreen(167756654, initialPofStatus = 0))
    }

    protected fun sendSaveStateEvent() {
        viewModel.onEvent(UiEvent.SaveState(bundle))
    }

    protected fun sendRestoreStateEvent(onFailedRestoreState: () -> Unit) {
        viewModel.onEvent(UiEvent.RestoreState(bundle, onFailedRestoreState))
    }

    protected fun sendOnClickResetPofFormEvent() {
        viewModel.onEvent(UiEvent.OnClickResetPofForm)
    }

    protected fun sendClickRetryOnErrorStateEvent() {
        viewModel.onEvent(UiEvent.ClickRetryOnErrorState)
    }

    protected fun sendOnClickRetryFetchPofEstimateEvent() {
        viewModel.onEvent(UiEvent.OnClickRetryFetchPofEstimate)
    }

    protected fun sendOnClickOpenPofInfoSummaryEvent() {
        viewModel.onEvent(UiEvent.OnClickOpenPofInfoSummary)
    }

    protected fun sendOnClickDismissSummaryBottomSheetEvent() {
        viewModel.onEvent(UiEvent.OnClickDismissSummaryBottomSheet)
    }

    protected fun sendProductAvailableQuantityChangedEvent(
        orderDetailId: Long,
        quantity: Int,
        previousQuantity: Int
    ) {
        viewModel.onEvent(UiEvent.ProductQuantityChanged(orderDetailId, quantity, previousQuantity))
    }

    protected fun sendOnClickSendPofEvent() {
        val pofInfo = TestHelper.createSuccessResponse<GetPofRequestInfoResponse.Data>(
            SUCCESS_RESPONSE_GET_POF_INFO_RESULT_WITH_STATUS_0
        )
        viewModel.onEvent(
            UiEvent.OnClickSendPof(
                pofInfo
                    .infoRequestPartialOrderFulfillment
                    ?.detailsOriginal
                    ?.map { details ->
                        SendPofRequestParams.PofDetail(
                            orderDetailId = details?.orderDetailId.orZero(),
                            quantityRequest = details?.quantityCheckout.orZero()
                        )
                    }
                    .orEmpty()
            )
        )
    }

    protected fun sendOnClickDescriptionLearnMore() {
        viewModel.onEvent(UiEvent.OnClickDescriptionLearnMore)
    }

    protected fun sendOnClickDismissPofBottomSheet() {
        viewModel.onEvent(UiEvent.OnClickDismissPofBottomSheet)
    }

    protected fun sendOnTryChangeProductQuantityAboveMaxQuantity() {
        viewModel.onEvent(UiEvent.OnTryChangeProductQuantityAboveMaxQuantity)
    }

    protected fun sendOnTryChangeProductQuantityBelowMinQuantity() {
        viewModel.onEvent(UiEvent.OnTryChangeProductQuantityBelowMinQuantity)
    }

    protected fun excludeCalls(calls: suspend MockKMatcherScope.() -> Unit) {
        coExcludeRecords(excludeBlock = calls)
    }

    protected suspend fun excludeOpenScreenGetPofInfoCalls() {
        // exclude open screen calls
        getPofInfoUseCase(createGetPofInfoParams())
    }

    protected suspend fun excludeOpenScreenGetPofEstimateCalls() {
        // exclude open screen calls
        getPofEstimateUseCase(createGetPofEstimateParams())
    }

    protected fun createSuccessGetPofInfoWithStatus0() {
        val response = TestHelper
            .createSuccessResponse<GetPofRequestInfoResponse.Data>(
                SUCCESS_RESPONSE_GET_POF_INFO_RESULT_WITH_STATUS_0
            )
        coEvery {
            getPofInfoUseCase(any())
        } returns flow {
            emit(RequestState.Requesting)
            emit(RequestState.Success(response))
        }
    }

    protected fun createSuccessGetPofEstimate() {
        val originalResponse = TestHelper.createSuccessResponse<GetPofRequestEstimateResponse.Data>(
            SUCCESS_RESPONSE_GET_POF_ESTIMATE_RESULT
        )
        coEvery {
            getPofEstimateUseCase(any())
        } returns flow {
            emit(RequestState.Requesting)
            emit(RequestState.Success(originalResponse))
        }
    }

    protected fun createSuccessSendPof() {
        val originalResponse = TestHelper.createSuccessResponse<SendPofResponse.Data>(
            SUCCESS_RESPONSE_SEND_POF
        )
        coEvery {
            sendPofUseCase(any())
        } returns flow {
            emit(RequestState.Requesting)
            emit(RequestState.Success(originalResponse))
        }
    }

    protected fun createBEErrorGetPofInfo() {
        coEvery {
            getPofInfoUseCase(any())
        } returns flow {
            emit(RequestState.Requesting)
            emit(RequestState.Error(throwable))
        }
    }

    protected fun createFEErrorGetPofInfo() {
        coEvery {
            getPofInfoUseCase(any())
        } throws throwable
    }

    protected fun createBEErrorGetPofEstimate() {
        coEvery {
            getPofEstimateUseCase(any())
        } returns flow {
            emit(RequestState.Requesting)
            emit(RequestState.Error(throwable))
        }
    }

    protected fun createFEErrorGetPofEstimate() {
        coEvery {
            getPofEstimateUseCase(any())
        } throws throwable
    }

    protected fun createBEErrorSendPof() {
        coEvery {
            sendPofUseCase(any())
        } returns flow {
            emit(RequestState.Requesting)
            emit(RequestState.Error(throwable))
        }
    }

    protected fun createFEErrorSendPof() {
        coEvery {
            sendPofUseCase(any())
        } throws throwable
    }

    protected fun getFirstOriginalOrderDetailId(): Long {
        return TestHelper
            .createSuccessResponse<GetPofRequestInfoResponse.Data>(
                SUCCESS_RESPONSE_GET_POF_INFO_RESULT_WITH_STATUS_0
            )
            .infoRequestPartialOrderFulfillment
            ?.detailsOriginal
            ?.firstOrNull()
            ?.orderDetailId!!
    }

    protected fun getLastOriginalOrderDetailId(): Long {
        return TestHelper
            .createSuccessResponse<GetPofRequestInfoResponse.Data>(
                SUCCESS_RESPONSE_GET_POF_INFO_RESULT_WITH_STATUS_0
            )
            .infoRequestPartialOrderFulfillment
            ?.detailsOriginal
            ?.lastOrNull()
            ?.orderDetailId!!
    }

    protected fun getFirstOriginalCheckoutQuantity(): Int {
        return TestHelper
            .createSuccessResponse<GetPofRequestInfoResponse.Data>(
                SUCCESS_RESPONSE_GET_POF_INFO_RESULT_WITH_STATUS_0
            )
            .infoRequestPartialOrderFulfillment
            ?.detailsOriginal
            ?.firstOrNull()
            ?.quantityCheckout!!
    }

    protected fun getLastOriginalCheckoutQuantity(): Int {
        return TestHelper
            .createSuccessResponse<GetPofRequestInfoResponse.Data>(
                SUCCESS_RESPONSE_GET_POF_INFO_RESULT_WITH_STATUS_0
            )
            .infoRequestPartialOrderFulfillment
            ?.detailsOriginal
            ?.lastOrNull()
            ?.quantityCheckout!!
    }
}
