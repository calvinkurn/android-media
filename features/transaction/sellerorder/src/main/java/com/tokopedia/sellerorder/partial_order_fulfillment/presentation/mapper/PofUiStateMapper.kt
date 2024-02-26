package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.mapper

import androidx.annotation.DimenRes
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.order_management_common.presentation.uimodel.StringRes
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.GetPofRequestEstimateResponse
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.GetPofRequestInfoResponse
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.GetPofRequestInfoResponse.Data.InfoRequestPartialOrderFulfillment.Companion.STATUS_INITIAL
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.RequestState
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.SendPofRequestParams
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.SendPofResponse
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofDescriptionUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofErrorStateUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofFullyFulfilledProductListHeaderUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofLoadingUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofPriceBreakdownTotalUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofPriceBreakdownUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofProductEditableUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofProductListHeaderUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofProductStaticUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofSummaryDescriptionUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofThickDividerUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofThinDividerUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofTickerUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofVisitable
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.PofBottomSheetSummaryUiState
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.PofFooterUiState
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.UiEvent
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.UiState
import javax.inject.Inject
import com.tokopedia.sellerorder.R as sellerorderR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class PofUiStateMapper @Inject constructor() {
    fun mapTicker(
        pofInfoData: GetPofRequestEstimateResponse.Data
    ): PofTickerUiModel? {
        val pofInfo = pofInfoData.partialOrderFulfillmentRequestEstimate?.pofInfo
        return if (pofInfo != null && pofInfo.hasInfo == true) {
            PofTickerUiModel(text = pofInfo.text.orEmpty())
        } else null
    }

    fun mapInitialQuantityEditorData(
        pofInfoData: GetPofRequestInfoResponse.Data
    ): List<PofProductEditableUiModel.QuantityEditorData> {
        val pofStatus = pofInfoData.infoRequestPartialOrderFulfillment?.pofStatus.orZero()
        val details = if (pofStatus == STATUS_INITIAL) {
            pofInfoData.infoRequestPartialOrderFulfillment?.detailsOriginal
        } else {
            pofInfoData.infoRequestPartialOrderFulfillment?.detailsFulfilled
        }
        val minQuantity = if (details?.filterNotNull()?.size.orZero() > 1) {
            Int.ZERO
        } else {
            Int.ONE
        }
        return details?.map { detail ->
            PofProductEditableUiModel.QuantityEditorData(
                orderDetailId = detail?.orderDetailId.orZero(),
                productId = detail?.productId.orZero(),
                quantity = detail?.quantityRequest.orZero(),
                minQuantity = minQuantity,
                maxQuantity = detail?.quantityCheckout.orZero(),
                updateTimestamp = Long.ZERO,
                enabled = true
            )
        }.orEmpty()
    }

    fun mapLoadingState(initialPofStatus: Int): UiState {
        return UiState(
            title = mapTitle(initialPofStatus),
            items = mutableListOf<PofVisitable>().apply {
                includeDescription(initialPofStatus)
                includeProductListHeader()
                includeThinDivider()
                includeLoadingUiModel()
            }.toList(),
            footerUiState = mapFooterOnLoading()
        )
    }

    fun map(
        pofInfoRequestState: RequestState<GetPofRequestInfoResponse.Data>,
        pofEstimateRequestState: RequestState<GetPofRequestEstimateResponse.Data>,
        sendPofRequestState: RequestState<SendPofResponse.Data>,
        tickerUiModel: PofTickerUiModel?,
        quantityEditorDataList: List<PofProductEditableUiModel.QuantityEditorData>,
        showBottomSheetSummary: Boolean,
        initialPofStatus: Int
    ): UiState {
        return when (pofInfoRequestState) {
            is RequestState.Error -> mapOnGetPofInfoError(pofInfoRequestState, initialPofStatus)
            is RequestState.None -> mapLoadingState(initialPofStatus)
            is RequestState.Requesting -> mapLoadingState(initialPofStatus)
            is RequestState.Success -> mapOnGetPofInfoSuccess(
                pofInfoRequestState,
                pofEstimateRequestState,
                sendPofRequestState,
                tickerUiModel,
                quantityEditorDataList,
                showBottomSheetSummary
            )
        }
    }

    private fun mapTitle(pofStatus: Int): StringRes {
        return when(pofStatus) {
            STATUS_INITIAL -> StringRes(sellerorderR.string.som_pof_title_request)
            else -> StringRes(sellerorderR.string.som_pof_title_result)
        }
    }

    private fun mapOnGetPofInfoError(
        pofInfoRequestState: RequestState.Error,
        initialPofStatus: Int
    ): UiState {
        return UiState(
            title = mapTitle(initialPofStatus),
            items = mutableListOf<PofVisitable>().apply {
                includeDescription()
                includeErrorUiModel(pofInfoRequestState.throwable)
            }.toList(),
            footerUiState = mapFooterOnInitialLoadError()
        )
    }

    private fun mapFooterOnInitialLoadError(): PofFooterUiState {
        return PofFooterUiState.InitialLoadError(
            priceEstimationData = PofFooterUiState.PriceEstimationData(
                title = StringRes(sellerorderR.string.som_pof_footer_title_no_counter),
                price = StringRes(sellerorderR.string.som_pof_footer_price_no_value)
            ),
            buttonData = PofFooterUiState.SendPofButtonData(
                buttonText = StringRes(sellerorderR.string.som_pof_footer_button_text_confirm_no_counter),
                show = true,
                isButtonLoading = false,
                isButtonEnabled = false
            )
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun mapOnGetPofInfoSuccess(
        pofInfoRequestState: RequestState.Success<GetPofRequestInfoResponse.Data>,
        pofEstimateRequestState: RequestState<GetPofRequestEstimateResponse.Data>,
        sendPofRequestState: RequestState<SendPofResponse.Data>,
        tickerUiModel: PofTickerUiModel?,
        quantityEditorDataList: List<PofProductEditableUiModel.QuantityEditorData>,
        showBottomSheetSummary: Boolean
    ): UiState {
        val detailsOriginal = pofInfoRequestState
            .data
            .infoRequestPartialOrderFulfillment
            ?.detailsOriginal
            .orEmpty()
            .filterNotNull()
        val detailsFulfillment = pofInfoRequestState
            .data
            .infoRequestPartialOrderFulfillment
            ?.detailsFulfilled
            .orEmpty()
            .filterNotNull()
        val pofStatus = pofInfoRequestState.data.infoRequestPartialOrderFulfillment?.pofStatus.orZero()
        val quantityChanged = quantityEditorDataList.any { it.quantity != it.maxQuantity }
        return UiState(
            title = mapTitle(pofStatus),
            showResetButton = pofStatus == STATUS_INITIAL && quantityChanged,
            items = mapItemsOnGetPofInfoSuccess(
                sendPofRequestState,
                detailsOriginal,
                detailsFulfillment,
                pofStatus,
                quantityEditorDataList,
                tickerUiModel
            ),
            footerUiState = mapFooterUiState(
                pofEstimateRequestState,
                pofInfoRequestState,
                sendPofRequestState,
                quantityEditorDataList
            ),
            bottomSheetSummaryUiState = mapBottomSheetSummaryUiState(
                showBottomSheetSummary,
                pofEstimateRequestState,
                tickerUiModel
            )
        )
    }

    private fun mapItemsOnGetPofInfoSuccess(
        sendPofRequestState: RequestState<SendPofResponse.Data>,
        detailsOriginal: List<GetPofRequestInfoResponse.Data.Details>,
        detailsFulfillment: List<GetPofRequestInfoResponse.Data.Details>,
        pofStatus: Int,
        quantityEditorDataList: List<PofProductEditableUiModel.QuantityEditorData>,
        tickerUiModel: PofTickerUiModel?
    ): List<PofVisitable> {
        return mutableListOf<PofVisitable>().apply {
            includeDescription(pofStatus)
            if (pofStatus == STATUS_INITIAL) {
                includeProductListHeader()
                includeThinDivider()
                includeEditableProductList(
                    detailsOriginal,
                    quantityEditorDataList,
                    sendPofRequestState
                )
                includeTicker(tickerUiModel)
            } else {
                val fullyFulfilledProductCount = detailsFulfillment.count {
                    it.quantityRequest.orZero() == it.quantityCheckout.orZero()
                }
                includeProductListHeader()
                includeThinDivider()
                includePartiallyFulfilledProductList(detailsFulfillment)
                if (fullyFulfilledProductCount.isMoreThanZero()) {
                    includeThickDivider()
                    includeFullyFulfilledProductListHeader(fullyFulfilledProductCount)
                    includeFullyFulfilledProductList(detailsFulfillment)
                }
                includeTicker(tickerUiModel)
            }
        }.toList()
    }

    private fun mapFooterUiState(
        pofEstimateRequestState: RequestState<GetPofRequestEstimateResponse.Data>,
        pofInfoRequestState: RequestState.Success<GetPofRequestInfoResponse.Data>,
        sendPofRequestState: RequestState<SendPofResponse.Data>,
        quantityEditorDataList: List<PofProductEditableUiModel.QuantityEditorData>
    ): PofFooterUiState {
        return when (pofEstimateRequestState) {
            is RequestState.Error -> mapFooterOnError(quantityEditorDataList)
            is RequestState.None -> mapFooterOnLoading()
            is RequestState.Requesting -> mapFooterOnReFetch(quantityEditorDataList)
            is RequestState.Success -> mapShowingDataFooterUiState(
                pofInfoRequestState,
                pofEstimateRequestState.data.partialOrderFulfillmentRequestEstimate,
                sendPofRequestState,
                quantityEditorDataList
            )
        }
    }

    private fun mapBottomSheetSummaryUiState(
        showBottomSheetSummary: Boolean,
        pofEstimateRequestState: RequestState<GetPofRequestEstimateResponse.Data>,
        tickerUiModel: PofTickerUiModel?
    ): PofBottomSheetSummaryUiState {
        return if (showBottomSheetSummary && pofEstimateRequestState is RequestState.Success) {
            PofBottomSheetSummaryUiState.Showing(
                items = mutableListOf<PofVisitable>().apply {
                    includePriceBreakdowns(pofEstimateRequestState.data.partialOrderFulfillmentRequestEstimate?.pofSummary)
                    includeThinDivider(
                        marginStart = sellerorderR.dimen.som_pof_divider_margin_start_bottom_sheet_summary,
                        marginEnd = sellerorderR.dimen.som_pof_divider_margin_end_bottom_sheet_summary,
                        marginTop = sellerorderR.dimen.som_pof_divider_margin_top_bottom_sheet_summary,
                        marginBottom = sellerorderR.dimen.som_pof_divider_margin_bottom_bottom_sheet_summary
                    )
                    includePriceBreakdownTotal(pofEstimateRequestState.data.partialOrderFulfillmentRequestEstimate?.pofFinalEstimation)
                    includeSummaryDescription(pofEstimateRequestState.data.partialOrderFulfillmentRequestEstimate?.pofFinalEstimation?.tooltip.orEmpty())
                    includeTicker(tickerUiModel)
                }.toList()
            )
        } else {
            PofBottomSheetSummaryUiState.Hidden
        }
    }

    private fun mapFooterOnError(
        quantityEditorDataList: List<PofProductEditableUiModel.QuantityEditorData>
    ): PofFooterUiState {
        return mapFooterOnErrorReFetch(quantityEditorDataList)
    }

    private fun mapFooterOnLoading(): PofFooterUiState.Loading {
        return PofFooterUiState.Loading(
            buttonData = PofFooterUiState.SendPofButtonData(
                buttonText = StringRes(sellerorderR.string.som_pof_footer_button_text_confirm_no_counter),
                show = true,
                isButtonLoading = false,
                isButtonEnabled = false
            )
        )
    }

    private fun mapFooterOnErrorReFetch(
        quantityEditorDataList: List<PofProductEditableUiModel.QuantityEditorData>
    ): PofFooterUiState {
        val requestedQuantity = quantityEditorDataList.sumOf { it.quantity }
        val originalQuantity = quantityEditorDataList.sumOf { it.maxQuantity }
        return PofFooterUiState.ErrorReFetch(
            priceEstimationData = PofFooterUiState.PriceEstimationData(
                title = StringRes(
                    sellerorderR.string.som_pof_footer_title_with_counter,
                    listOf(requestedQuantity, originalQuantity)
                ),
                price = StringRes(sellerorderR.string.som_pof_footer_price_reload),
                showIcon = true,
                icon = IconUnify.RELOAD,
                iconEventData = UiEvent.OnClickRetryFetchPofEstimate,
                iconSize = sellerorderR.dimen.som_pof_footer_icon_size_reload,
                iconWrapped = true,
            ),
            buttonData = PofFooterUiState.SendPofButtonData(
                buttonText = StringRes(
                    sellerorderR.string.som_pof_footer_button_text_confirm_with_counter,
                    listOf(requestedQuantity)
                ),
                show = true,
                isButtonLoading = false,
                isButtonEnabled = false
            )
        )
    }

    private fun mapFooterOnReFetch(
        quantityEditorDataList: List<PofProductEditableUiModel.QuantityEditorData>
    ): PofFooterUiState {
        val requestedQuantity = quantityEditorDataList.sumOf { it.quantity }
        return PofFooterUiState.ReFetch(
            buttonData = PofFooterUiState.SendPofButtonData(
                buttonText = StringRes(
                    sellerorderR.string.som_pof_footer_button_text_confirm_with_counter,
                    listOf(requestedQuantity)
                ),
                show = true,
                isButtonLoading = false,
                isButtonEnabled = false
            )
        )
    }

    private fun mapShowingDataFooterUiState(
        pofInfoRequestState: RequestState.Success<GetPofRequestInfoResponse.Data>,
        data: GetPofRequestEstimateResponse.Data.PartialOrderFulfillmentRequestEstimate?,
        sendPofRequestState: RequestState<SendPofResponse.Data>,
        quantityEditorDataList: List<PofProductEditableUiModel.QuantityEditorData>
    ): PofFooterUiState {
        val requestedQuantity = quantityEditorDataList.sumOf { it.quantity }
        val originalQuantity = quantityEditorDataList.sumOf { it.maxQuantity }
        val quantityChanged = quantityEditorDataList.any { it.quantity != it.maxQuantity }
        val pofStatus = pofInfoRequestState.data.infoRequestPartialOrderFulfillment?.pofStatus.orZero()
        return if (pofStatus == STATUS_INITIAL) {
            PofFooterUiState.ShowingPofRequestData(
                priceEstimationData = PofFooterUiState.PriceEstimationData(
                    title = StringRes(
                        sellerorderR.string.som_pof_footer_title_with_counter,
                        listOf(requestedQuantity, originalQuantity)
                    ),
                    price = StringRes(
                        sellerorderR.string.som_pof_footer_price_with_value,
                        listOf(data?.pofFinalEstimation?.valueString.orEmpty())
                    ),
                    showIcon = true,
                    icon = IconUnify.CHEVRON_UP,
                    iconEventData = UiEvent.OnClickOpenPofInfoSummary
                ),
                buttonData = PofFooterUiState.SendPofButtonData(
                    buttonText = StringRes(
                        sellerorderR.string.som_pof_footer_button_text_confirm_with_counter,
                        listOf(requestedQuantity)
                    ),
                    show = true,
                    isButtonLoading = sendPofRequestState is RequestState.Requesting,
                    isButtonEnabled = quantityChanged,
                    eventData = UiEvent.OnClickSendPof(
                        pofDetailList = quantityEditorDataList.map { quantityEditorData ->
                            SendPofRequestParams.PofDetail(
                                orderDetailId = quantityEditorData.orderDetailId,
                                quantityRequest = quantityEditorData.quantity
                            )
                        }
                    )
                )
            )
        } else {
            PofFooterUiState.ShowingPofResultData(
                priceEstimationData = PofFooterUiState.PriceEstimationData(
                    title = StringRes(
                        sellerorderR.string.som_pof_footer_title_with_counter,
                        listOf(requestedQuantity, originalQuantity)
                    ),
                    price = StringRes(
                        sellerorderR.string.som_pof_footer_price_with_value,
                        listOf(data?.pofFinalEstimation?.valueString.orEmpty())
                    ),
                    showIcon = true,
                    icon = IconUnify.CHEVRON_UP,
                    iconEventData = UiEvent.OnClickOpenPofInfoSummary
                )
            )
        }
    }

    private fun MutableList<PofVisitable>.includeLoadingUiModel() {
        add(PofLoadingUiModel)
    }

    private fun MutableList<PofVisitable>.includeErrorUiModel(throwable: Throwable) {
        add(PofErrorStateUiModel(throwable))
    }

    private fun MutableList<PofVisitable>.includeDescription(
        pofStatus: Int = STATUS_INITIAL
    ) {
        add(
            PofDescriptionUiModel(
                text = when (pofStatus) {
                    STATUS_INITIAL -> StringRes(sellerorderR.string.som_pof_description_initial)
                    else -> StringRes(sellerorderR.string.som_pof_description_waiting_response)
                },
                onClickEventData = UiEvent.OnClickDescriptionLearnMore
            )
        )
    }

    private fun MutableList<PofVisitable>.includeProductListHeader() {
        add(
            PofProductListHeaderUiModel(
                textLeft = StringRes(sellerorderR.string.som_pof_product_list_header_left_label),
                textRight = StringRes(sellerorderR.string.som_pof_product_list_header_right_label)
            )
        )
    }

    private fun MutableList<PofVisitable>.includeFullyFulfilledProductListHeader(
        fullyFulfilledProductCount: Int
    ) {
        add(
            PofFullyFulfilledProductListHeaderUiModel(
                text = StringRes(
                    sellerorderR.string.som_pof_product_list_header_fully_fulfilled_label,
                    listOf(fullyFulfilledProductCount)
                )
            )
        )
    }

    private fun MutableList<PofVisitable>.includeThinDivider(
        @DimenRes marginStart: Int = sellerorderR.dimen.som_pof_divider_margin_edge_to_edge,
        @DimenRes marginEnd: Int = sellerorderR.dimen.som_pof_divider_margin_edge_to_edge,
        @DimenRes marginTop: Int = sellerorderR.dimen.som_pof_divider_margin_edge_to_edge,
        @DimenRes marginBottom: Int = sellerorderR.dimen.som_pof_divider_margin_edge_to_edge,
    ) {
        add(PofThinDividerUiModel(marginStart, marginEnd, marginTop, marginBottom))
    }

    private fun MutableList<PofVisitable>.includeThickDivider() {
        add(PofThickDividerUiModel)
    }

    private fun MutableList<PofVisitable>.includeEditableProductList(
        details: List<GetPofRequestInfoResponse.Data.Details>,
        quantityEditorDataList: List<PofProductEditableUiModel.QuantityEditorData>,
        sendPofRequestState: RequestState<SendPofResponse.Data>
    ) {
        details.forEachIndexed { index, detail ->
            val checkoutQuantity = detail.quantityCheckout.orZero()
            val quantityEditorData = quantityEditorDataList.find { it.orderDetailId == detail.orderDetailId }
            val availableQuantity = quantityEditorData?.quantity ?: detail.productQuantity.orZero()
            val minQuantity = quantityEditorData?.minQuantity ?: if (details.count() == 1) Int.ONE else Int.ZERO
            val updateTimestamp = quantityEditorData?.updateTimestamp ?: System.currentTimeMillis()
            add(
                PofProductEditableUiModel(
                    orderDetailId = detail.orderDetailId.orZero(),
                    productImageUrl = detail.productPicture.orEmpty(),
                    productName = detail.productName.orEmpty(),
                    productPriceQuantity = "$checkoutQuantity x ${detail.productPrice}",
                    quantityEditorData = PofProductEditableUiModel.QuantityEditorData(
                        orderDetailId = detail.orderDetailId.orZero(),
                        productId = detail.productId.orZero(),
                        quantity = availableQuantity,
                        minQuantity = minQuantity,
                        maxQuantity = checkoutQuantity,
                        updateTimestamp = updateTimestamp,
                        enabled = sendPofRequestState !is RequestState.Requesting
                    )
                )
            )
            if (index < details.count().dec()) includeThinDivider()
        }
    }

    private fun MutableList<PofVisitable>.includePartiallyFulfilledProductList(
        detailsFulfillment: List<GetPofRequestInfoResponse.Data.Details>
    ) {
        val partiallyFulfilledDetails = detailsFulfillment.filter {
            val hasValidQuantity = it.quantityRequest != null && it.quantityCheckout != null
            val hasDifferentQuantity = it.quantityRequest != it.quantityCheckout
            hasValidQuantity && hasDifferentQuantity
        }
        partiallyFulfilledDetails.forEachIndexed { index, details ->
            val orderDetailId = details.orderDetailId ?: return@forEachIndexed
            val productImageUrl = details.productPicture ?: return@forEachIndexed
            val productName = details.productName ?: return@forEachIndexed
            val productPrice = details.productPrice ?: return@forEachIndexed
            val quantityRequest = details.quantityRequest ?: return@forEachIndexed
            val quantityCheckout = details.quantityCheckout ?: return@forEachIndexed
            val productPriceQuantity = "$quantityCheckout x $productPrice"
            add(
                PofProductStaticUiModel(
                    orderDetailId = orderDetailId,
                    productImageUrl = productImageUrl,
                    productName = productName,
                    productPriceQuantity = productPriceQuantity,
                    productQuantity = quantityRequest
                )
            )
            if (index < partiallyFulfilledDetails.count().dec()) includeThinDivider()
        }
    }

    private fun MutableList<PofVisitable>.includeFullyFulfilledProductList(
        detailsFulfillment: List<GetPofRequestInfoResponse.Data.Details>
    ) {
        val fullyFulfilledDetails = detailsFulfillment.filter {
            val hasValidQuantity = it.quantityRequest != null && it.quantityCheckout != null
            val hasSameQuantity = it.quantityRequest == it.quantityCheckout
            hasValidQuantity && hasSameQuantity
        }
        fullyFulfilledDetails.forEachIndexed { index, details ->
            val orderDetailId = details.orderDetailId ?: return@forEachIndexed
            val productImageUrl = details.productPicture ?: return@forEachIndexed
            val productName = details.productName ?: return@forEachIndexed
            val productPrice = details.productPrice ?: return@forEachIndexed
            val quantityRequest = details.quantityRequest ?: return@forEachIndexed
            val quantityCheckout = details.quantityCheckout ?: return@forEachIndexed
            val productPriceQuantity = "$quantityCheckout x $productPrice"
            add(
                PofProductStaticUiModel(
                    orderDetailId = orderDetailId,
                    productImageUrl = productImageUrl,
                    productName = productName,
                    productPriceQuantity = productPriceQuantity,
                    productQuantity = quantityRequest
                )
            )
            if (index < fullyFulfilledDetails.count().dec()) includeThinDivider()
        }
    }

    private fun MutableList<PofVisitable>.includeTicker(tickerUiModel: PofTickerUiModel?) {
        if (tickerUiModel != null) add(tickerUiModel)
    }

    private fun MutableList<PofVisitable>.includePriceBreakdowns(
        pofSummary: List<GetPofRequestEstimateResponse.Data.PartialOrderFulfillmentRequestEstimate.PofSummary?>?
    ) {
        val validPofSummary = pofSummary?.filterNotNull().orEmpty()
        if (validPofSummary.isNotEmpty()) {
            validPofSummary.forEach { priceBreakdown ->
                val value = priceBreakdown.valueString.orEmpty()
                add(
                    PofPriceBreakdownUiModel(
                        label = priceBreakdown.label.orEmpty(),
                        value = value,
                        color = if (value.startsWith("-")) {
                            unifyprinciplesR.color.Unify_RN500
                        } else {
                            sellerorderR.color._dms_som_pof_price_breakdown_normal
                        }
                    )
                )
            }
        }
    }

    private fun MutableList<PofVisitable>.includePriceBreakdownTotal(
        pofFinalEstimation: GetPofRequestEstimateResponse.Data.PartialOrderFulfillmentRequestEstimate.PofFinalEstimation?
    ) {
        add(
            PofPriceBreakdownTotalUiModel(
                label = pofFinalEstimation?.label.orEmpty(),
                value = pofFinalEstimation?.valueString.orEmpty()
            )
        )
    }

    private fun MutableList<PofVisitable>.includeSummaryDescription(tooltip: String) {
        add(PofSummaryDescriptionUiModel(tooltip))
    }
}
