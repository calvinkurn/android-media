package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.viewmodel

import android.app.Activity
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofProductEditableUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.PofBottomSheetSummaryUiState
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.ToasterQueue
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.UiEffect
import io.mockk.Ordering
import io.mockk.clearMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PofViewModelTest : PofViewModelTestFixture() {
    @Test
    fun `validate when receive OpenScreen event and fetch data is success`() {
        runCollecting { uiStates, _, _ ->
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()

            sendOpenScreenEvent()

            val uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            coVerify(ordering = Ordering.SEQUENCE) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(initialVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(createFooterUiState(), uiState.footerUiState)
            //endregion verify footer
        }
    }

    @Test
    fun `validate when receive OpenScreen event and GetPofInfoUseCase is error from BE`() {
        runCollecting { uiStates, _, _ ->
            createBEErrorGetPofInfo()
            createSuccessGetPofEstimate()

            sendOpenScreenEvent()

            val uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofInfoUseCase(createGetPofInfoParams())
            }
            coVerify(inverse = true) {
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(errorVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(initialLoadErrorFooterUiStateWithStatus0, uiState.footerUiState)
            //endregion verify footer
        }
    }

    @Test
    fun `validate when receive OpenScreen event and GetPofInfoUseCase is error from FE`() {
        runCollecting { uiStates, _, _ ->
            createFEErrorGetPofInfo()
            createSuccessGetPofEstimate()

            sendOpenScreenEvent()

            val uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofInfoUseCase(createGetPofInfoParams())
            }
            coVerify(inverse = true) {
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(errorVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(initialLoadErrorFooterUiStateWithStatus0, uiState.footerUiState)
            //endregion verify footer
        }
    }

    @Test
    fun `validate when receive OpenScreen event and GetPofEstimateUseCase is error from BE`() {
        runCollecting { uiStates, _, _ ->
            createSuccessGetPofInfoWithStatus0()
            createBEErrorGetPofEstimate()

            sendOpenScreenEvent()

            val uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            coVerify(ordering = Ordering.SEQUENCE) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(initialVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(reFetchErrorFooterUiStateWithStatus0, uiState.footerUiState)
            //endregion verify footer
        }
    }

    @Test
    fun `validate when receive OpenScreen event and GetPofEstimateUseCase is error from FE`() {
        runCollecting { uiStates, _, _ ->
            createSuccessGetPofInfoWithStatus0()
            createFEErrorGetPofEstimate()

            sendOpenScreenEvent()

            val uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            coVerify(ordering = Ordering.SEQUENCE) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(initialVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(reFetchErrorFooterUiStateWithStatus0, uiState.footerUiState)
            //endregion verify footer
        }
    }

    @Test
    fun `validate when receive OnClickResetPofForm`() {
        runCollecting { uiStates, toasterQueues, _ ->
            //region perform open screen
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()

            sendOpenScreenEvent()

            var uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            coVerify(ordering = Ordering.SEQUENCE) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(initialVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(createFooterUiState(), uiState.footerUiState)
            //endregion verify footer
            //endregion perform open screen

            //region perform change quantity
            val targetQuantity = getFirstOriginalCheckoutQuantity().dec()
            clearMocks(getPofInfoUseCase)
            clearMocks(getPofEstimateUseCase)
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()
            sendProductAvailableQuantityChangedEvent(
                orderDetailId = getFirstOriginalOrderDetailId(),
                quantity = targetQuantity,
                previousQuantity = getFirstOriginalCheckoutQuantity()
            )
            uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofEstimateUseCase(
                    createGetPofEstimateParamsWithDefinedQuantity(
                        delay = 1000,
                        quantityList = listOf(getFirstOriginalOrderDetailId() to targetQuantity)
                    )
                )
            }
            coVerify(inverse = true) {
                getPofInfoUseCase(createGetPofInfoParams())
            }
            //endregion verify use-cases
            //region verify items
            var expectedVisitableList = initialVisitableListWithStatus0
                .map { visitable ->
                    if (
                        visitable is PofProductEditableUiModel &&
                        visitable.orderDetailId == getFirstOriginalOrderDetailId()
                    ) {
                        visitable.copy(
                            quantityEditorData = visitable
                                .quantityEditorData
                                .copy(quantity = targetQuantity)
                        )
                    } else visitable
                }
            uiState.items.forEachIndexed { index, visitable ->
                if (
                    visitable is PofProductEditableUiModel &&
                    visitable.orderDetailId == getFirstOriginalOrderDetailId()
                ) {
                    Assertions
                        .assertThat(visitable)
                        .isEqualToIgnoringGivenFields(
                            expectedVisitableList[index] as PofProductEditableUiModel,
                            PofProductEditableUiModel::quantityEditorData.name
                        )
                    Assertions
                        .assertThat(visitable.quantityEditorData)
                        .isEqualToIgnoringGivenFields(
                            (expectedVisitableList[index] as PofProductEditableUiModel).quantityEditorData,
                            PofProductEditableUiModel.QuantityEditorData::updateTimestamp.name
                        )
                } else {
                    assertEquals(expectedVisitableList[index], visitable)
                }
            }
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            var expectedFooterUiState = createFooterUiState(
                pofEstimateResponseFilePath = SUCCESS_RESPONSE_GET_POF_ESTIMATE_RESULT_WITH_FIRST_PRODUCT_ZERO,
                quantityList = listOf(getFirstOriginalOrderDetailId() to targetQuantity)
            )
            assertEquals(expectedFooterUiState, uiState.footerUiState)
            //endregion verify footer
            //region verify toaster queue
            assertEquals(emptyList<ToasterQueue>(), toasterQueues)
            //endregion verify toaster queue
            //endregion perform change quantity

            //region perform reset quantity
            clearMocks(getPofInfoUseCase)
            clearMocks(getPofEstimateUseCase)
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()
            sendOnClickResetPofFormEvent()
            uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            coVerify(inverse = true) {
                getPofInfoUseCase(createGetPofInfoParams())
            }
            //endregion verify use-cases
            //region verify items
            expectedVisitableList = initialVisitableListWithStatus0
            assertEquals(expectedVisitableList, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            expectedFooterUiState = createFooterUiState()
            assertEquals(expectedFooterUiState, uiState.footerUiState)
            //endregion verify footer
            //region verify toaster queue
            assertEquals(emptyList<ToasterQueue>(), toasterQueues)
            //endregion verify toaster queue
            //region verify tracker
            verify(exactly = 1) { tracker.trackClickReset() }
            //endregion verify tracker
            //endregion perform reset quantity
        }
    }

    @Test
    fun `validate when receive ClickRetryOnErrorState event and fetch data is success`() {
        runCollecting { uiStates, _, _ ->
            //region perform open screen
            createBEErrorGetPofInfo()
            createBEErrorGetPofEstimate()
            sendOpenScreenEvent()
            var uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofInfoUseCase(createGetPofInfoParams())
            }
            coVerify(inverse = true) {
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(errorVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(initialLoadErrorFooterUiStateWithStatus0, uiState.footerUiState)
            //endregion verify footer
            //endregion perform open screen

            //region perform click retry on error state
            clearMocks(getPofInfoUseCase)
            clearMocks(getPofEstimateUseCase)
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()
            sendClickRetryOnErrorStateEvent()
            uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofInfoUseCase(createGetPofInfoParams(delay = 1000))
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            coVerify(ordering = Ordering.SEQUENCE) {
                getPofInfoUseCase(createGetPofInfoParams(delay = 1000))
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(initialVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(createFooterUiState(), uiState.footerUiState)
            //endregion verify footer
            //endregion perform click retry on error state
        }
    }

    @Test
    fun `validate when receive ClickRetryOnErrorState event and GetPofInfoUseCase is error`() {
        runCollecting { uiStates, _, _ ->
            //region perform open screen
            createBEErrorGetPofInfo()
            createBEErrorGetPofEstimate()
            sendOpenScreenEvent()
            var uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofInfoUseCase(createGetPofInfoParams())
            }
            coVerify(inverse = true) {
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(errorVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(initialLoadErrorFooterUiStateWithStatus0, uiState.footerUiState)
            //endregion verify footer
            //endregion perform open screen

            //region perform click retry on error state
            clearMocks(getPofInfoUseCase)
            clearMocks(getPofEstimateUseCase)
            createBEErrorGetPofInfo()
            createSuccessGetPofEstimate()
            sendClickRetryOnErrorStateEvent()
            uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofInfoUseCase(createGetPofInfoParams(delay = 1000))
            }
            coVerify(inverse = true) {
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(errorVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(initialLoadErrorFooterUiStateWithStatus0, uiState.footerUiState)
            //endregion verify footer
            //endregion perform click retry on error state
        }
    }

    @Test
    fun `validate when receive ClickRetryOnErrorState event and GetPofEstimateUseCase is error`() {
        runCollecting { uiStates, _, _ ->
            //region perform open screen
            createBEErrorGetPofInfo()
            createBEErrorGetPofEstimate()
            sendOpenScreenEvent()
            var uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofInfoUseCase(createGetPofInfoParams())
            }
            coVerify(inverse = true) {
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(errorVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(initialLoadErrorFooterUiStateWithStatus0, uiState.footerUiState)
            //endregion verify footer
            //endregion perform open screen

            //region perform click retry on error state
            clearMocks(getPofInfoUseCase)
            clearMocks(getPofEstimateUseCase)
            createSuccessGetPofInfoWithStatus0()
            createBEErrorGetPofEstimate()
            sendClickRetryOnErrorStateEvent()
            uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofInfoUseCase(createGetPofInfoParams(delay = 1000))
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            coVerify(ordering = Ordering.SEQUENCE) {
                getPofInfoUseCase(createGetPofInfoParams(delay = 1000))
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(initialVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(reFetchErrorFooterUiStateWithStatus0, uiState.footerUiState)
            //endregion verify footer
            //endregion perform click retry on error state
        }
    }

    @Test
    fun `validate when receive OnClickDescriptionLearnMore event`() {
        runCollecting { uiStates, _, _ ->
            //region perform open screen
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()

            sendOpenScreenEvent()

            val uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            coVerify(ordering = Ordering.SEQUENCE) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(initialVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(createFooterUiState(), uiState.footerUiState)
            //endregion verify footer
            //endregion perform open screen

            //region perform click description learn more
            sendOnClickDescriptionLearnMore()
            //region validate tracker
            verify(exactly = 1) {
                tracker.trackClickDescriptionLearnMore()
            }
            //endregion validate tracker
            //endregion perform click description learn more
        }
    }

    @Test
    fun `validate when receive OnClickDismissPofBottomSheet event before perform send POF`() {
        runCollecting { uiStates, _, uiEffects ->
            //region perform open screen
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()

            sendOpenScreenEvent()

            val uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            coVerify(ordering = Ordering.SEQUENCE) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(initialVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(createFooterUiState(), uiState.footerUiState)
            //endregion verify footer
            //endregion perform open screen

            //region perform click description learn more
            sendOnClickDismissPofBottomSheet()
            //region validate ui effects
            assertEquals(1, uiEffects.count())
            assertEquals(UiEffect.FinishActivity(Activity.RESULT_CANCELED), uiEffects.last())
            //endregion validate ui effects
            //endregion perform click description learn more
        }
    }

    @Test
    fun `validate when receive OnClickDismissPofBottomSheet event after perform send POF`() {
        runCollecting { uiStates, toasterQueues, uiEffects ->
            //region perform open screen
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()

            sendOpenScreenEvent()

            var uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            coVerify(ordering = Ordering.SEQUENCE) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(initialVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(createFooterUiState(), uiState.footerUiState)
            //endregion verify footer
            //endregion perform open screen

            //region perform change quantity on first product
            val targetQuantity = getFirstOriginalCheckoutQuantity().dec()
            clearMocks(getPofInfoUseCase)
            clearMocks(getPofEstimateUseCase)
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()
            sendProductAvailableQuantityChangedEvent(
                orderDetailId = getFirstOriginalOrderDetailId(),
                quantity = targetQuantity,
                previousQuantity = getFirstOriginalCheckoutQuantity()
            )
            uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofEstimateUseCase(
                    createGetPofEstimateParamsWithDefinedQuantity(
                        delay = 1000,
                        quantityList = listOf(getFirstOriginalOrderDetailId() to targetQuantity)
                    )
                )
            }
            coVerify(inverse = true) {
                getPofInfoUseCase(createGetPofInfoParams())
            }
            //endregion verify use-cases
            //region verify items
            val expectedVisitableList = initialVisitableListWithStatus0
                .map { visitable ->
                    if (
                        visitable is PofProductEditableUiModel &&
                        visitable.orderDetailId == getFirstOriginalOrderDetailId()
                    ) {
                        visitable.copy(
                            quantityEditorData = visitable
                                .quantityEditorData
                                .copy(quantity = targetQuantity)
                        )
                    } else visitable
                }
            uiState.items.forEachIndexed { index, visitable ->
                if (
                    visitable is PofProductEditableUiModel
                ) {
                    Assertions
                        .assertThat(visitable)
                        .isEqualToIgnoringGivenFields(
                            expectedVisitableList[index] as PofProductEditableUiModel,
                            PofProductEditableUiModel::quantityEditorData.name
                        )
                    Assertions
                        .assertThat(visitable.quantityEditorData)
                        .isEqualToIgnoringGivenFields(
                            (expectedVisitableList[index] as PofProductEditableUiModel).quantityEditorData,
                            PofProductEditableUiModel.QuantityEditorData::updateTimestamp.name
                        )
                } else {
                    assertEquals(expectedVisitableList[index], visitable)
                }
            }
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            val expectedFooterUiState = createFooterUiState(
                pofEstimateResponseFilePath = SUCCESS_RESPONSE_GET_POF_ESTIMATE_RESULT_WITH_FIRST_PRODUCT_ZERO,
                quantityList = listOf(getFirstOriginalOrderDetailId() to targetQuantity)
            )
            assertEquals(expectedFooterUiState, uiState.footerUiState)
            //endregion verify footer
            //region verify toaster queue
            assertEquals(emptyList<ToasterQueue>(), toasterQueues)
            //endregion verify toaster queue
            //endregion perform change quantity on first product

            //region perform send pof request
            clearMocks(getPofInfoUseCase)
            clearMocks(getPofEstimateUseCase)
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()
            createBEErrorSendPof()
            sendOnClickSendPofEvent()
            //region validate use case
            coVerify(exactly = 1) {
                sendPofUseCase(any())
            }
            coVerify(inverse = true) {
                getPofInfoUseCase(any())
                getPofEstimateUseCase(any())
            }
            //endregion validate use case
            //region validate ui effect
            assertEquals(emptyList<UiEffect>(), uiEffects)
            //endregion validate ui effect
            //region validate tracker
            verify(exactly = 1) { tracker.trackClickSend() }
            //endregion validate tracker
            //endregion perform send pof request

            //region perform click description learn more
            sendOnClickDismissPofBottomSheet()
            //region validate ui effects
            assertEquals(1, uiEffects.count())
            assertEquals(UiEffect.FinishActivity(Activity.RESULT_OK), uiEffects.last())
            //endregion validate ui effects
            //endregion perform click description learn more
        }
    }

    @Test
    fun `validate when receive OnClickRetryFetchPofEstimate event and GetPofEstimateUseCase is success`() {
        runCollecting { uiStates, _, _ ->
            //region perform open screen
            createSuccessGetPofInfoWithStatus0()
            createBEErrorGetPofEstimate()
            sendOpenScreenEvent()
            var uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            coVerify(ordering = Ordering.SEQUENCE) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(initialVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(reFetchErrorFooterUiStateWithStatus0, uiState.footerUiState)
            //endregion verify footer
            //endregion perform open screen

            //region perform retry fetch pof estimate
            clearMocks(getPofInfoUseCase)
            clearMocks(getPofEstimateUseCase)
            createSuccessGetPofEstimate()
            sendOnClickRetryFetchPofEstimateEvent()
            uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            coVerify(inverse = true) {
                getPofInfoUseCase(createGetPofInfoParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(initialVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(createFooterUiState(), uiState.footerUiState)
            //endregion verify footer
            //endregion perform retry fetch pof estimate
        }
    }

    @Test
    fun `validate when receive OnClickRetryFetchPofEstimate event and GetPofEstimateUseCase is error`() {
        runCollecting { uiStates, _, _ ->
            //region perform open screen
            createSuccessGetPofInfoWithStatus0()
            createBEErrorGetPofEstimate()
            sendOpenScreenEvent()
            var uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            coVerify(ordering = Ordering.SEQUENCE) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(initialVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(reFetchErrorFooterUiStateWithStatus0, uiState.footerUiState)
            //endregion verify footer
            //endregion perform open screen

            //region perform retry fetch pof estimate
            clearMocks(getPofInfoUseCase)
            clearMocks(getPofEstimateUseCase)
            createSuccessGetPofInfoWithStatus0()
            createBEErrorGetPofEstimate()
            sendOnClickRetryFetchPofEstimateEvent()
            uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(initialVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(reFetchErrorFooterUiStateWithStatus0, uiState.footerUiState)
            //endregion verify footer
            //endregion perform retry fetch pof estimate
        }
    }

    @Test
    fun `validate when receive OnClickOpenPofInfoSummary event`() {
        runCollecting { uiStates, _, _ ->
            //region perform open screen
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()

            sendOpenScreenEvent()

            val uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            coVerify(ordering = Ordering.SEQUENCE) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(initialVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(createFooterUiState(), uiState.footerUiState)
            //endregion verify footer
            //endregion perform open screen

            //region perform open info summary bottom sheet
            sendOnClickOpenPofInfoSummaryEvent()
            //region validate bottom sheet state
            val bottomSheetUiState = uiStates.last().bottomSheetSummaryUiState
            assertTrue(bottomSheetUiState is PofBottomSheetSummaryUiState.Showing)
            //endregion validate bottom sheet state
            //region validate tracker
            verify(exactly = 1) { tracker.trackClickOpenPofSummaryBottomSheet() }
            //endregion validate tracker
            //endregion perform open info summary bottom sheet
        }
    }

    @Test
    fun `validate when receive OnClickDismissSummaryBottomSheet event`() {
        runCollecting { uiStates, _, _ ->
            //region perform open screen
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()

            sendOpenScreenEvent()

            val uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            coVerify(ordering = Ordering.SEQUENCE) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(initialVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(createFooterUiState(), uiState.footerUiState)
            //endregion verify footer
            //endregion perform open screen

            //region perform open summary bottom sheet
            sendOnClickOpenPofInfoSummaryEvent()
            var bottomSheetUiState = uiStates.last().bottomSheetSummaryUiState
            assertTrue(bottomSheetUiState is PofBottomSheetSummaryUiState.Showing)
            //endregion perform open summary bottom sheet

            //region perform dismiss summary bottom sheet
            sendOnClickDismissSummaryBottomSheetEvent()
            bottomSheetUiState = uiStates.last().bottomSheetSummaryUiState
            assertTrue(bottomSheetUiState is PofBottomSheetSummaryUiState.Hidden)
            //endregion perform dismiss summary bottom sheet
        }
    }

    @Test
    fun `validate when receive ProductAvailableQuantityChanged event and quantity is increasing`() {
        runCollecting { uiStates, toasterQueues, _ ->
            //region perform open screen
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()

            sendOpenScreenEvent()

            var uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            coVerify(ordering = Ordering.SEQUENCE) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(initialVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(createFooterUiState(), uiState.footerUiState)
            //endregion verify footer
            //endregion perform open screen

            //region perform reduce quantity
            clearMocks(getPofInfoUseCase)
            clearMocks(getPofEstimateUseCase)
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()
            sendProductAvailableQuantityChangedEvent(
                orderDetailId = getFirstOriginalOrderDetailId(),
                quantity = 0,
                previousQuantity = getFirstOriginalCheckoutQuantity()
            )
            uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofEstimateUseCase(
                    createGetPofEstimateParamsWithDefinedQuantity(
                        delay = 1000,
                        quantityList = listOf(getFirstOriginalOrderDetailId() to 0)
                    )
                )
            }
            coVerify(inverse = true) {
                getPofInfoUseCase(createGetPofInfoParams())
            }
            //endregion verify use-cases
            //region verify items
            var expectedVisitableList = initialVisitableListWithStatus0
                .map { visitable ->
                    if (visitable is PofProductEditableUiModel) {
                        if (visitable.orderDetailId == getFirstOriginalOrderDetailId()) {
                            visitable.copy(
                                quantityEditorData = visitable
                                    .quantityEditorData
                                    .copy(quantity = 0)
                            )
                        } else {
                            visitable.copy(
                                quantityEditorData = visitable
                                    .quantityEditorData
                                    .copy(minQuantity = 1)
                            )
                        }
                    } else visitable
                }
            uiState.items.forEachIndexed { index, visitable ->
                if (
                    visitable is PofProductEditableUiModel &&
                    visitable.orderDetailId == getFirstOriginalOrderDetailId()
                ) {
                    Assertions
                        .assertThat(visitable)
                        .isEqualToIgnoringGivenFields(
                            expectedVisitableList[index] as PofProductEditableUiModel,
                            PofProductEditableUiModel::quantityEditorData.name
                        )
                    Assertions
                        .assertThat(visitable.quantityEditorData)
                        .isEqualToIgnoringGivenFields(
                            (expectedVisitableList[index] as PofProductEditableUiModel).quantityEditorData,
                            PofProductEditableUiModel.QuantityEditorData::updateTimestamp.name
                        )
                } else {
                    assertEquals(expectedVisitableList[index], visitable)
                }
            }
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            var expectedFooterUiState = createFooterUiState(
                pofEstimateResponseFilePath = SUCCESS_RESPONSE_GET_POF_ESTIMATE_RESULT_WITH_FIRST_PRODUCT_ZERO,
                quantityList = listOf(getFirstOriginalOrderDetailId() to 0)
            )
            assertEquals(expectedFooterUiState, uiState.footerUiState)
            //endregion verify footer
            //region verify toaster queue
            assertEquals(emptyList<ToasterQueue>(), toasterQueues)
            //endregion verify toaster queue
            //region verify tracker
            verify(exactly = 1) { tracker.trackClickReduceQuantity() }
            //endregion verify tracker
            //endregion perform reduce quantity

            //region perform increase quantity
            clearMocks(getPofInfoUseCase)
            clearMocks(getPofEstimateUseCase)
            clearMocks(tracker)
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()
            sendProductAvailableQuantityChangedEvent(
                orderDetailId = getFirstOriginalOrderDetailId(),
                quantity = getFirstOriginalCheckoutQuantity(),
                previousQuantity = 0
            )
            uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofEstimateUseCase(
                    createGetPofEstimateParamsWithDefinedQuantity(
                        delay = 1000,
                        quantityList = listOf(getFirstOriginalOrderDetailId() to getFirstOriginalCheckoutQuantity())
                    )
                )
            }
            coVerify(inverse = true) {
                getPofInfoUseCase(createGetPofInfoParams())
            }
            //endregion verify use-cases
            //region verify items
            expectedVisitableList = initialVisitableListWithStatus0
            uiState.items.forEachIndexed { index, visitable ->
                if (
                    visitable is PofProductEditableUiModel &&
                    visitable.orderDetailId == getFirstOriginalOrderDetailId()
                ) {
                    Assertions
                        .assertThat(visitable)
                        .isEqualToIgnoringGivenFields(
                            expectedVisitableList[index] as PofProductEditableUiModel,
                            PofProductEditableUiModel::quantityEditorData.name
                        )
                    Assertions
                        .assertThat(visitable.quantityEditorData)
                        .isEqualToIgnoringGivenFields(
                            (expectedVisitableList[index] as PofProductEditableUiModel).quantityEditorData,
                            PofProductEditableUiModel.QuantityEditorData::updateTimestamp.name
                        )
                } else {
                    assertEquals(expectedVisitableList[index], visitable)
                }
            }
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            expectedFooterUiState = createFooterUiState(
                pofEstimateResponseFilePath = SUCCESS_RESPONSE_GET_POF_ESTIMATE_RESULT_WITH_FIRST_PRODUCT_ZERO,
                quantityList = listOf(getFirstOriginalOrderDetailId() to getFirstOriginalCheckoutQuantity())
            )
            assertEquals(expectedFooterUiState, uiState.footerUiState)
            //endregion verify footer
            //region verify toaster queue
            assertEquals(emptyList<ToasterQueue>(), toasterQueues)
            //endregion verify toaster queue
            //region verify tracker
            verify(inverse = true) { tracker.trackClickReduceQuantity() }
            verify(exactly = 1) { tracker.trackClickIncreaseQuantity() }
            //endregion verify tracker
            //endregion perform increase quantity
        }
    }

    @Test
    fun `validate when receive ProductAvailableQuantityChanged event and quantity is zero but others quantity is above zero`() {
        runCollecting { uiStates, toasterQueues, _ ->
            //region perform open screen
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()

            sendOpenScreenEvent()

            var uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            coVerify(ordering = Ordering.SEQUENCE) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(initialVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(createFooterUiState(), uiState.footerUiState)
            //endregion verify footer
            //endregion perform open screen

            //region perform change quantity
            clearMocks(getPofInfoUseCase)
            clearMocks(getPofEstimateUseCase)
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()
            sendProductAvailableQuantityChangedEvent(
                orderDetailId = getFirstOriginalOrderDetailId(),
                quantity = 0,
                previousQuantity = getFirstOriginalCheckoutQuantity()
            )
            uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofEstimateUseCase(
                    createGetPofEstimateParamsWithDefinedQuantity(
                        delay = 1000,
                        quantityList = listOf(getFirstOriginalOrderDetailId() to 0)
                    )
                )
            }
            coVerify(inverse = true) {
                getPofInfoUseCase(createGetPofInfoParams())
            }
            //endregion verify use-cases
            //region verify items
            val expectedVisitableList = initialVisitableListWithStatus0
                .map { visitable ->
                    if (visitable is PofProductEditableUiModel) {
                        if (visitable.orderDetailId == getFirstOriginalOrderDetailId()) {
                            visitable.copy(
                                quantityEditorData = visitable
                                    .quantityEditorData
                                    .copy(quantity = 0)
                            )
                        } else {
                            visitable.copy(
                                quantityEditorData = visitable
                                    .quantityEditorData
                                    .copy(minQuantity = 1)
                            )
                        }
                    } else visitable
                }
            uiState.items.forEachIndexed { index, visitable ->
                if (
                    visitable is PofProductEditableUiModel &&
                    visitable.orderDetailId == getFirstOriginalOrderDetailId()
                ) {
                    Assertions
                        .assertThat(visitable)
                        .isEqualToIgnoringGivenFields(
                            expectedVisitableList[index] as PofProductEditableUiModel,
                            PofProductEditableUiModel::quantityEditorData.name
                        )
                    Assertions
                        .assertThat(visitable.quantityEditorData)
                        .isEqualToIgnoringGivenFields(
                            (expectedVisitableList[index] as PofProductEditableUiModel).quantityEditorData,
                            PofProductEditableUiModel.QuantityEditorData::updateTimestamp.name
                        )
                } else {
                    assertEquals(expectedVisitableList[index], visitable)
                }
            }
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            val expectedFooterUiState = createFooterUiState(
                pofEstimateResponseFilePath = SUCCESS_RESPONSE_GET_POF_ESTIMATE_RESULT_WITH_FIRST_PRODUCT_ZERO,
                quantityList = listOf(getFirstOriginalOrderDetailId() to 0)
            )
            assertEquals(expectedFooterUiState, uiState.footerUiState)
            //endregion verify footer
            //region verify toaster queue
            assertEquals(emptyList<ToasterQueue>(), toasterQueues)
            //endregion verify toaster queue
            //region verify tracker
            verify(exactly = 1) { tracker.trackClickReduceQuantity() }
            //endregion verify tracker
            //endregion perform change quantity
        }
    }

    @Test
    fun `validate when receive OnClickSendPof event and success`() {
        runCollecting { uiStates, toasterQueues, uiEffects ->
            //region perform open screen
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()

            sendOpenScreenEvent()

            var uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            coVerify(ordering = Ordering.SEQUENCE) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(initialVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(createFooterUiState(), uiState.footerUiState)
            //endregion verify footer
            //endregion perform open screen

            //region perform change quantity on first product
            val targetQuantity = getFirstOriginalCheckoutQuantity().dec()
            clearMocks(getPofInfoUseCase)
            clearMocks(getPofEstimateUseCase)
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()
            sendProductAvailableQuantityChangedEvent(
                orderDetailId = getFirstOriginalOrderDetailId(),
                quantity = targetQuantity,
                previousQuantity = getFirstOriginalCheckoutQuantity()
            )
            uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofEstimateUseCase(
                    createGetPofEstimateParamsWithDefinedQuantity(
                        delay = 1000,
                        quantityList = listOf(getFirstOriginalOrderDetailId() to targetQuantity)
                    )
                )
            }
            coVerify(inverse = true) {
                getPofInfoUseCase(createGetPofInfoParams())
            }
            //endregion verify use-cases
            //region verify items
            val expectedVisitableList = initialVisitableListWithStatus0
                .map { visitable ->
                    if (
                        visitable is PofProductEditableUiModel &&
                        visitable.orderDetailId == getFirstOriginalOrderDetailId()
                    ) {
                        visitable.copy(
                            quantityEditorData = visitable
                                .quantityEditorData
                                .copy(quantity = targetQuantity)
                        )
                    } else visitable
                }
            uiState.items.forEachIndexed { index, visitable ->
                if (
                    visitable is PofProductEditableUiModel
                ) {
                    Assertions
                        .assertThat(visitable)
                        .isEqualToIgnoringGivenFields(
                            expectedVisitableList[index] as PofProductEditableUiModel,
                            PofProductEditableUiModel::quantityEditorData.name
                        )
                    Assertions
                        .assertThat(visitable.quantityEditorData)
                        .isEqualToIgnoringGivenFields(
                            (expectedVisitableList[index] as PofProductEditableUiModel).quantityEditorData,
                            PofProductEditableUiModel.QuantityEditorData::updateTimestamp.name
                        )
                } else {
                    assertEquals(expectedVisitableList[index], visitable)
                }
            }
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            val expectedFooterUiState = createFooterUiState(
                pofEstimateResponseFilePath = SUCCESS_RESPONSE_GET_POF_ESTIMATE_RESULT_WITH_FIRST_PRODUCT_ZERO,
                quantityList = listOf(getFirstOriginalOrderDetailId() to targetQuantity)
            )
            assertEquals(expectedFooterUiState, uiState.footerUiState)
            //endregion verify footer
            //region verify toaster queue
            assertEquals(emptyList<ToasterQueue>(), toasterQueues)
            //endregion verify toaster queue
            //endregion perform change quantity on first product

            //region perform send pof request
            clearMocks(getPofInfoUseCase)
            clearMocks(getPofEstimateUseCase)
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()
            createSuccessSendPof()
            sendOnClickSendPofEvent()
            //region validate use case
            coVerify(exactly = 1) {
                sendPofUseCase(any())
            }
            coVerify(inverse = true) {
                getPofInfoUseCase(any())
                getPofEstimateUseCase(any())
            }
            //endregion validate use case
            //region validate ui effect
            assertEquals(UiEffect.FinishActivity(Activity.RESULT_OK), uiEffects.last())
            //endregion validate ui effect
            //region validate tracker
            verify(exactly = 1) { tracker.trackClickSend() }
            //endregion validate tracker
            //endregion perform send pof request
        }
    }

    @Test
    fun `validate when receive OnClickSendPof event and error from BE`() {
        runCollecting { uiStates, toasterQueues, uiEffects ->
            //region perform open screen
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()

            sendOpenScreenEvent()

            var uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            coVerify(ordering = Ordering.SEQUENCE) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(initialVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(createFooterUiState(), uiState.footerUiState)
            //endregion verify footer
            //endregion perform open screen

            //region perform change quantity on first product
            val targetQuantity = getFirstOriginalCheckoutQuantity().dec()
            clearMocks(getPofInfoUseCase)
            clearMocks(getPofEstimateUseCase)
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()
            sendProductAvailableQuantityChangedEvent(
                orderDetailId = getFirstOriginalOrderDetailId(),
                quantity = targetQuantity,
                previousQuantity = getFirstOriginalCheckoutQuantity()
            )
            uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofEstimateUseCase(
                    createGetPofEstimateParamsWithDefinedQuantity(
                        delay = 1000,
                        quantityList = listOf(getFirstOriginalOrderDetailId() to targetQuantity)
                    )
                )
            }
            coVerify(inverse = true) {
                getPofInfoUseCase(createGetPofInfoParams())
            }
            //endregion verify use-cases
            //region verify items
            val expectedVisitableList = initialVisitableListWithStatus0
                .map { visitable ->
                    if (
                        visitable is PofProductEditableUiModel &&
                        visitable.orderDetailId == getFirstOriginalOrderDetailId()
                    ) {
                        visitable.copy(
                            quantityEditorData = visitable
                                .quantityEditorData
                                .copy(quantity = targetQuantity)
                        )
                    } else visitable
                }
            uiState.items.forEachIndexed { index, visitable ->
                if (
                    visitable is PofProductEditableUiModel
                ) {
                    Assertions
                        .assertThat(visitable)
                        .isEqualToIgnoringGivenFields(
                            expectedVisitableList[index] as PofProductEditableUiModel,
                            PofProductEditableUiModel::quantityEditorData.name
                        )
                    Assertions
                        .assertThat(visitable.quantityEditorData)
                        .isEqualToIgnoringGivenFields(
                            (expectedVisitableList[index] as PofProductEditableUiModel).quantityEditorData,
                            PofProductEditableUiModel.QuantityEditorData::updateTimestamp.name
                        )
                } else {
                    assertEquals(expectedVisitableList[index], visitable)
                }
            }
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            val expectedFooterUiState = createFooterUiState(
                pofEstimateResponseFilePath = SUCCESS_RESPONSE_GET_POF_ESTIMATE_RESULT_WITH_FIRST_PRODUCT_ZERO,
                quantityList = listOf(getFirstOriginalOrderDetailId() to targetQuantity)
            )
            assertEquals(expectedFooterUiState, uiState.footerUiState)
            //endregion verify footer
            //region verify toaster queue
            assertEquals(emptyList<ToasterQueue>(), toasterQueues)
            //endregion verify toaster queue
            //endregion perform change quantity on first product

            //region perform send pof request
            clearMocks(getPofInfoUseCase)
            clearMocks(getPofEstimateUseCase)
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()
            createBEErrorSendPof()
            sendOnClickSendPofEvent()
            //region validate use case
            coVerify(exactly = 1) {
                sendPofUseCase(any())
            }
            coVerify(inverse = true) {
                getPofInfoUseCase(any())
                getPofEstimateUseCase(any())
            }
            //endregion validate use case
            //region validate ui effect
            assertEquals(emptyList<UiEffect>(), uiEffects)
            //endregion validate ui effect
            //region validate tracker
            verify(exactly = 1) { tracker.trackClickSend() }
            //endregion validate tracker
            //endregion perform send pof request
        }
    }

    @Test
    fun `validate when receive OnClickSendPof event and error from FE`() {
        runCollecting { uiStates, toasterQueues, uiEffects ->
            //region perform open screen
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()

            sendOpenScreenEvent()

            var uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            coVerify(ordering = Ordering.SEQUENCE) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(initialVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(createFooterUiState(), uiState.footerUiState)
            //endregion verify footer
            //endregion perform open screen

            //region perform change quantity on first product
            val targetQuantity = getFirstOriginalCheckoutQuantity().dec()
            clearMocks(getPofInfoUseCase)
            clearMocks(getPofEstimateUseCase)
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()
            sendProductAvailableQuantityChangedEvent(
                orderDetailId = getFirstOriginalOrderDetailId(),
                quantity = targetQuantity,
                previousQuantity = getFirstOriginalCheckoutQuantity()
            )
            uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofEstimateUseCase(
                    createGetPofEstimateParamsWithDefinedQuantity(
                        delay = 1000,
                        quantityList = listOf(getFirstOriginalOrderDetailId() to targetQuantity)
                    )
                )
            }
            coVerify(inverse = true) {
                getPofInfoUseCase(createGetPofInfoParams())
            }
            //endregion verify use-cases
            //region verify items
            val expectedVisitableList = initialVisitableListWithStatus0
                .map { visitable ->
                    if (
                        visitable is PofProductEditableUiModel &&
                        visitable.orderDetailId == getFirstOriginalOrderDetailId()
                    ) {
                        visitable.copy(
                            quantityEditorData = visitable
                                .quantityEditorData
                                .copy(quantity = targetQuantity)
                        )
                    } else visitable
                }
            uiState.items.forEachIndexed { index, visitable ->
                if (
                    visitable is PofProductEditableUiModel
                ) {
                    Assertions
                        .assertThat(visitable)
                        .isEqualToIgnoringGivenFields(
                            expectedVisitableList[index] as PofProductEditableUiModel,
                            PofProductEditableUiModel::quantityEditorData.name
                        )
                    Assertions
                        .assertThat(visitable.quantityEditorData)
                        .isEqualToIgnoringGivenFields(
                            (expectedVisitableList[index] as PofProductEditableUiModel).quantityEditorData,
                            PofProductEditableUiModel.QuantityEditorData::updateTimestamp.name
                        )
                } else {
                    assertEquals(expectedVisitableList[index], visitable)
                }
            }
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            val expectedFooterUiState = createFooterUiState(
                pofEstimateResponseFilePath = SUCCESS_RESPONSE_GET_POF_ESTIMATE_RESULT_WITH_FIRST_PRODUCT_ZERO,
                quantityList = listOf(getFirstOriginalOrderDetailId() to targetQuantity)
            )
            assertEquals(expectedFooterUiState, uiState.footerUiState)
            //endregion verify footer
            //region verify toaster queue
            assertEquals(emptyList<ToasterQueue>(), toasterQueues)
            //endregion verify toaster queue
            //endregion perform change quantity on first product

            //region perform send pof request
            clearMocks(getPofInfoUseCase)
            clearMocks(getPofEstimateUseCase)
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()
            createFEErrorSendPof()
            sendOnClickSendPofEvent()
            //region validate use case
            coVerify(exactly = 1) {
                sendPofUseCase(any())
            }
            coVerify(inverse = true) {
                getPofInfoUseCase(any())
                getPofEstimateUseCase(any())
            }
            //endregion validate use case
            //region validate ui effect
            assertEquals(emptyList<UiEffect>(), uiEffects)
            //endregion validate ui effect
            //region validate tracker
            verify(exactly = 1) { tracker.trackClickSend() }
            //endregion validate tracker
            //endregion perform send pof request
        }
    }

    @Test
    fun `validate when receive OnTryChangeProductQuantityAboveMaxQuantity event`() {
        runCollecting { uiStates, toasterQueues, uiEffects ->
            //region perform open screen
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()

            sendOpenScreenEvent()

            val uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            coVerify(ordering = Ordering.SEQUENCE) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(initialVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(createFooterUiState(), uiState.footerUiState)
            //endregion verify footer
            //endregion perform open screen

            //region perform try change product quantity above max quantity
            clearMocks(getPofInfoUseCase)
            clearMocks(getPofEstimateUseCase)
            sendOnTryChangeProductQuantityAboveMaxQuantity()
            //region verify use-cases
            coVerify(inverse = true) {
                getPofInfoUseCase(any())
                getPofEstimateUseCase(any())
            }
            //endregion perform try change product quantity above max quantity
        }
    }

    @Test
    fun `validate when receive OnTryChangeProductQuantityBelowMinQuantity event`() {
        runCollecting { uiStates, toasterQueues, uiEffects ->
            //region perform open screen
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()

            sendOpenScreenEvent()

            val uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            coVerify(ordering = Ordering.SEQUENCE) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(initialVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(createFooterUiState(), uiState.footerUiState)
            //endregion verify footer
            //endregion perform open screen

            //region perform try change product quantity above max quantity
            clearMocks(getPofInfoUseCase)
            clearMocks(getPofEstimateUseCase)
            sendOnTryChangeProductQuantityBelowMinQuantity()
            //region verify use-cases
            coVerify(inverse = true) {
                getPofInfoUseCase(any())
                getPofEstimateUseCase(any())
            }
            //endregion verify use-cases
            //endregion perform try change product quantity above max quantity
        }
    }

    @Test
    fun `validate when receive SaveState event`() {
        runCollecting { uiStates, _, _ ->
            //region perform open screen
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()

            sendOpenScreenEvent()

            var uiState = uiStates.last()
            //region verify use-cases
            coVerify(exactly = 1) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            coVerify(ordering = Ordering.SEQUENCE) {
                getPofInfoUseCase(createGetPofInfoParams())
                getPofEstimateUseCase(createGetPofEstimateParams())
            }
            //endregion verify use-cases
            //region verify items
            assertEquals(initialVisitableListWithStatus0, uiState.items)
            //endregion verify items
            //region verify summary bottom sheet should hidden
            assertEquals(PofBottomSheetSummaryUiState.Hidden, uiState.bottomSheetSummaryUiState)
            //endregion verify summary bottom sheet should hidden
            //region verify footer
            assertEquals(createFooterUiState(), uiState.footerUiState)
            //endregion verify footer
            //endregion perform open screen

            //region perform save state
            sendSaveStateEvent()
            uiState = uiStates.last()
            //region validate bundle put operations
            val quantityEditorDataList = uiState
                .items
                .mapNotNull {
                    if (it is PofProductEditableUiModel) {
                        it.quantityEditorData
                    } else null
                }
            verify {
                bundle.putLong("orderId", 167756654L)
                bundle.putInt("initialPofStatus", 0)
                bundle.putParcelableArrayList("quantityEditorDataList", ArrayList(quantityEditorDataList))
            }
            verify(exactly = 1) {
                bundle.putLong(any(), any())
            }
            verify(exactly = 1) {
                bundle.putInt(any(), any())
            }
            verify(exactly = 1) {
                bundle.putParcelableArrayList(any(), any())
            }
            //endregion validate bundle put operations
            //endregion perform save state
        }
    }

    @Test
    fun `validate when receive RestoreState event and success`() {
        runCollecting { _, _, _ ->
            //region perform restore state
            val onFailedRestoreState = mockk<() -> Unit>(relaxed = true)
            every {
                bundle.getLong("orderId", -1)
            } returns 167756654L
            every {
                bundle.getInt("initialPofStatus", -1)
            } returns 0
            every {
                bundle.getParcelableArrayList<PofProductEditableUiModel.QuantityEditorData>("quantityEditorDataList")
            } returns arrayListOf(mockk(relaxed = true), mockk(relaxed = true))
            sendRestoreStateEvent(onFailedRestoreState)
            //region validate success restore
            verify(inverse = true) {
                onFailedRestoreState()
            }
            //endregion validate success restore
            //region validate bundle put operations
            verify {
                bundle.getLong("orderId", -1)
                bundle.getInt("initialPofStatus", -1)
                bundle.getParcelableArrayList<PofProductEditableUiModel.QuantityEditorData>("quantityEditorDataList")
            }
            verify(exactly = 1) {
                bundle.getLong(any(), any())
            }
            verify(exactly = 1) {
                bundle.getInt(any(), any())
            }
            verify(exactly = 1) {
                bundle.getParcelableArrayList<PofProductEditableUiModel.QuantityEditorData>("quantityEditorDataList")
            }
            //endregion validate bundle put operations
            //endregion perform save state
        }
    }

    @Test
    fun `validate when receive RestoreState event and error restore order ID`() {
        runCollecting { _, _, _ ->
            //region perform restore state
            val onFailedRestoreState = mockk<() -> Unit>(relaxed = true)
            every {
                bundle.getLong("orderId", -1)
            } returns -1
            every {
                bundle.getInt("initialPofStatus", -1)
            } returns 0
            every {
                bundle.getParcelableArrayList<PofProductEditableUiModel.QuantityEditorData>("quantityEditorDataList")
            } returns arrayListOf(mockk(relaxed = true), mockk(relaxed = true))
            sendRestoreStateEvent(onFailedRestoreState)
            //region validate success restore
            verify(exactly = 1) {
                onFailedRestoreState()
            }
            //endregion validate success restore
            //region validate bundle put operations
            verify {
                bundle.getLong("orderId", -1)
                bundle.getInt("initialPofStatus", -1)
                bundle.getParcelableArrayList<PofProductEditableUiModel.QuantityEditorData>("quantityEditorDataList")
            }
            verify(exactly = 1) {
                bundle.getLong(any(), any())
            }
            verify(exactly = 1) {
                bundle.getInt(any(), any())
            }
            verify(exactly = 1) {
                bundle.getParcelableArrayList<PofProductEditableUiModel.QuantityEditorData>("quantityEditorDataList")
            }
            //endregion validate bundle put operations
            //endregion perform save state
        }
    }

    @Test
    fun `validate when receive RestoreState event and error restore initial pof status`() {
        runCollecting { _, _, _ ->
            //region perform restore state
            val onFailedRestoreState = mockk<() -> Unit>(relaxed = true)
            every {
                bundle.getLong("orderId", -1)
            } returns 167756654L
            every {
                bundle.getInt("initialPofStatus", -1)
            } returns -1
            every {
                bundle.getParcelableArrayList<PofProductEditableUiModel.QuantityEditorData>("quantityEditorDataList")
            } returns arrayListOf(mockk(relaxed = true), mockk(relaxed = true))
            sendRestoreStateEvent(onFailedRestoreState)
            //region validate success restore
            verify(exactly = 1) {
                onFailedRestoreState()
            }
            //endregion validate success restore
            //region validate bundle put operations
            verify {
                bundle.getLong("orderId", -1)
                bundle.getInt("initialPofStatus", -1)
                bundle.getParcelableArrayList<PofProductEditableUiModel.QuantityEditorData>("quantityEditorDataList")
            }
            verify(exactly = 1) {
                bundle.getLong(any(), any())
            }
            verify(exactly = 1) {
                bundle.getInt(any(), any())
            }
            verify(exactly = 1) {
                bundle.getParcelableArrayList<PofProductEditableUiModel.QuantityEditorData>("quantityEditorDataList")
            }
            //endregion validate bundle put operations
            //endregion perform save state
        }
    }

    @Test
    fun `validate when receive RestoreState event and error restore quantity edit data list`() {
        runCollecting { _, _, _ ->
            //region perform restore state
            val onFailedRestoreState = mockk<() -> Unit>(relaxed = true)
            every {
                bundle.getLong("orderId", -1)
            } returns 167756654L
            every {
                bundle.getInt("initialPofStatus", -1)
            } returns 0
            every {
                bundle.getParcelableArrayList<PofProductEditableUiModel.QuantityEditorData>("quantityEditorDataList")
            } returns null
            sendRestoreStateEvent(onFailedRestoreState)
            //region validate success restore
            verify(exactly = 1) {
                onFailedRestoreState()
            }
            //endregion validate success restore
            //region validate bundle put operations
            verify {
                bundle.getLong("orderId", -1)
                bundle.getInt("initialPofStatus", -1)
                bundle.getParcelableArrayList<PofProductEditableUiModel.QuantityEditorData>("quantityEditorDataList")
            }
            verify(exactly = 1) {
                bundle.getLong(any(), any())
            }
            verify(exactly = 1) {
                bundle.getInt(any(), any())
            }
            verify(exactly = 1) {
                bundle.getParcelableArrayList<PofProductEditableUiModel.QuantityEditorData>("quantityEditorDataList")
            }
            //endregion validate bundle put operations
            //endregion perform save state
        }
    }
}
