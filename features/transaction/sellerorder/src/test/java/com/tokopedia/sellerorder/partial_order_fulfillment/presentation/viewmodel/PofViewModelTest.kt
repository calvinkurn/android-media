package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.viewmodel

import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofProductEditableUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.PofBottomSheetSummaryUiState
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.ToasterQueue
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.UiEffect
import io.mockk.Ordering
import io.mockk.clearMocks
import io.mockk.coVerify
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
            clearMocks(getPofInfoUseCase)
            clearMocks(getPofEstimateUseCase)
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()
            sendProductAvailableQuantityChangedEvent(
                orderDetailId = getFirstOriginalOrderDetailId(),
                quantity = 0,
                exceedCheckoutQuantity = false
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
                    if (
                        visitable is PofProductEditableUiModel &&
                        visitable.orderDetailId == getFirstOriginalOrderDetailId()
                    ) {
                        visitable.copy(
                            quantityEditorData = visitable
                                .quantityEditorData
                                .copy(quantity = 0)
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
                quantityList = listOf(getFirstOriginalOrderDetailId() to 0)
            )
            assertEquals(expectedFooterUiState, uiState.footerUiState)
            //endregion verify footer
            //region verify toaster queue
            assertEquals(emptyList<ToasterQueue>(), toasterQueues)
            //endregion verify toaster queue
            //endregion perform change quantity

            //region perform change quantity
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
            //endregion perform change quantity
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
            val bottomSheetUiState = uiStates.last().bottomSheetSummaryUiState
            assertTrue(bottomSheetUiState is PofBottomSheetSummaryUiState.Showing)
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
    fun `validate when receive ProductAvailableQuantityChanged and quantity is above checkout quantity`() {
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
                quantity = getFirstOriginalCheckoutQuantity(),
                exceedCheckoutQuantity = true
            )
            uiState = uiStates.last()
            //region verify use-cases
            coVerify(inverse = true) {
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
            //region verify toaster queue
            val toasterQueue = toasterQueues.last()
            assertEquals(toasterQueueCannotExceedCheckoutQuantity, toasterQueue)
            //endregion verify toaster queue
            //endregion perform change quantity
        }
    }

    @Test
    fun `validate when receive ProductAvailableQuantityChanged and quantity is zero but others quantity is above zero`() {
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
                exceedCheckoutQuantity = false
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
                    if (
                        visitable is PofProductEditableUiModel &&
                        visitable.orderDetailId == getFirstOriginalOrderDetailId()
                    ) {
                        visitable.copy(
                            quantityEditorData = visitable
                                .quantityEditorData
                                .copy(quantity = 0)
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
            val expectedFooterUiState = createFooterUiState(
                pofEstimateResponseFilePath = SUCCESS_RESPONSE_GET_POF_ESTIMATE_RESULT_WITH_FIRST_PRODUCT_ZERO,
                quantityList = listOf(getFirstOriginalOrderDetailId() to 0)
            )
            assertEquals(expectedFooterUiState, uiState.footerUiState)
            //endregion verify footer
            //region verify toaster queue
            assertEquals(emptyList<ToasterQueue>(), toasterQueues)
            //endregion verify toaster queue
            //endregion perform change quantity
        }
    }

    @Test
    fun `validate when receive ProductAvailableQuantityChanged and all quantity is zero`() {
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

            //region perform change quantity on first product
            clearMocks(getPofInfoUseCase)
            clearMocks(getPofEstimateUseCase)
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()
            sendProductAvailableQuantityChangedEvent(
                orderDetailId = getFirstOriginalOrderDetailId(),
                quantity = 0,
                exceedCheckoutQuantity = false
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
                    if (
                        visitable is PofProductEditableUiModel &&
                        visitable.orderDetailId == getFirstOriginalOrderDetailId()
                    ) {
                        visitable.copy(
                            quantityEditorData = visitable
                                .quantityEditorData
                                .copy(quantity = 0)
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
            var expectedFooterUiState = createFooterUiState(
                pofEstimateResponseFilePath = SUCCESS_RESPONSE_GET_POF_ESTIMATE_RESULT_WITH_FIRST_PRODUCT_ZERO,
                quantityList = listOf(getFirstOriginalOrderDetailId() to 0)
            )
            assertEquals(expectedFooterUiState, uiState.footerUiState)
            //endregion verify footer
            //region verify toaster queue
            assertEquals(emptyList<ToasterQueue>(), toasterQueues)
            //endregion verify toaster queue
            //endregion perform change quantity on first product

            //region perform change quantity on last product
            clearMocks(getPofInfoUseCase)
            clearMocks(getPofEstimateUseCase)
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()
            sendProductAvailableQuantityChangedEvent(
                orderDetailId = getLastOriginalOrderDetailId(),
                quantity = 0,
                exceedCheckoutQuantity = false
            )
            uiState = uiStates.last()
            //region verify use-cases
            coVerify(inverse = true) {
                getPofEstimateUseCase(any())
                getPofInfoUseCase(any())
            }
            //endregion verify use-cases
            //region verify items
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
            expectedFooterUiState = createFooterUiState(
                pofEstimateResponseFilePath = SUCCESS_RESPONSE_GET_POF_ESTIMATE_RESULT_WITH_FIRST_PRODUCT_ZERO,
                quantityList = listOf(getFirstOriginalOrderDetailId() to 0)
            )
            assertEquals(expectedFooterUiState, uiState.footerUiState)
            //endregion verify footer
            //region verify toaster queue
            assertEquals(toasterQueueCannotEmptyAllQuantity, toasterQueues.last())
            //endregion verify toaster queue
            //endregion perform change quantity on last product
        }
    }

    @Test
    fun `validate when receive OnClickSendPof and success`() {
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
            clearMocks(getPofInfoUseCase)
            clearMocks(getPofEstimateUseCase)
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()
            sendProductAvailableQuantityChangedEvent(
                orderDetailId = getFirstOriginalOrderDetailId(),
                quantity = 0,
                exceedCheckoutQuantity = false
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
                    if (
                        visitable is PofProductEditableUiModel &&
                        visitable.orderDetailId == getFirstOriginalOrderDetailId()
                    ) {
                        visitable.copy(
                            quantityEditorData = visitable
                                .quantityEditorData
                                .copy(quantity = 0)
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
                quantityList = listOf(getFirstOriginalOrderDetailId() to 0)
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
            assertEquals(UiEffect.FinishActivity, uiEffects.last())
            //endregion validate ui effect
            //endregion perform send pof request
        }
    }

    @Test
    fun `validate when receive OnClickSendPof and error from BE`() {
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
            clearMocks(getPofInfoUseCase)
            clearMocks(getPofEstimateUseCase)
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()
            sendProductAvailableQuantityChangedEvent(
                orderDetailId = getFirstOriginalOrderDetailId(),
                quantity = 0,
                exceedCheckoutQuantity = false
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
                    if (
                        visitable is PofProductEditableUiModel &&
                        visitable.orderDetailId == getFirstOriginalOrderDetailId()
                    ) {
                        visitable.copy(
                            quantityEditorData = visitable
                                .quantityEditorData
                                .copy(quantity = 0)
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
                quantityList = listOf(getFirstOriginalOrderDetailId() to 0)
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
            //endregion perform send pof request
        }
    }

    @Test
    fun `validate when receive OnClickSendPof and error from FE`() {
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
            clearMocks(getPofInfoUseCase)
            clearMocks(getPofEstimateUseCase)
            createSuccessGetPofInfoWithStatus0()
            createSuccessGetPofEstimate()
            sendProductAvailableQuantityChangedEvent(
                orderDetailId = getFirstOriginalOrderDetailId(),
                quantity = 0,
                exceedCheckoutQuantity = false
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
                    if (
                        visitable is PofProductEditableUiModel &&
                        visitable.orderDetailId == getFirstOriginalOrderDetailId()
                    ) {
                        visitable.copy(
                            quantityEditorData = visitable
                                .quantityEditorData
                                .copy(quantity = 0)
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
                quantityList = listOf(getFirstOriginalOrderDetailId() to 0)
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
            //endregion perform send pof request
        }
    }
}
