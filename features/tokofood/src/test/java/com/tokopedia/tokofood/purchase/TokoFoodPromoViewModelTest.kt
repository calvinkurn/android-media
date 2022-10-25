package com.tokopedia.tokofood.purchase

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.PromoListTokoFood
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.PromoListTokoFoodCoupon
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.PromoListTokoFoodData
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.PromoListTokoFoodErrorPage
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.PromoListTokoFoodSection
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.PromoListTokoFoodSubSection
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.UiEvent
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.mapper.TokoFoodPromoUiModelMapper
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

@FlowPreview
@ExperimentalCoroutinesApi
class TokoFoodPromoViewModelTest: TokoFoodPromoViewModelTestFixture() {

    @Test
    fun `when loadData success, should set ui event value`() {
        runBlocking {
            val response = PromoListTokoFood(
                status = TokoFoodCartUtil.SUCCESS_STATUS,
                data = PromoListTokoFoodData(
                    availableSection = PromoListTokoFoodSection(
                        subSection = PromoListTokoFoodSubSection(
                            coupons = listOf(
                                PromoListTokoFoodCoupon()
                            )
                        )
                    )
                )
            )

            coEvery {
                promoListTokoFoodUseCase.get().execute(SOURCE, "")
            } returns response

            viewModel.loadData(SOURCE, "")

            val expectedUiEventState = UiEvent.EVENT_SUCCESS_LOAD_PROMO_PAGE
            val expectedFragmentUiModel = TokoFoodPromoUiModelMapper.mapResponseDataToFragmentUiModel(response.data)
            val expectedVisitables = TokoFoodPromoUiModelMapper.mapResponseDataToVisitables(response.data)
            assertEquals(expectedUiEventState, viewModel.uiEvent.value?.state)
            assertEquals(expectedFragmentUiModel, viewModel.fragmentUiModel.value)
            assertEquals(expectedVisitables, viewModel.visitables.value)
        }
    }

    @Test
    fun `when loadData success but only unavailable ones, should still set success ui event value`() {
        runBlocking {
            val response = PromoListTokoFood(
                status = TokoFoodCartUtil.SUCCESS_STATUS,
                data = PromoListTokoFoodData(
                    unavailableSection = PromoListTokoFoodSection(
                        subSection = PromoListTokoFoodSubSection(
                            coupons = listOf(
                                PromoListTokoFoodCoupon()
                            )
                        )
                    ),
                    changeRestrictionMessage = "This coupon cannot be used"
                )
            )

            coEvery {
                promoListTokoFoodUseCase.get().execute(SOURCE, "")
            } returns response

            viewModel.loadData(SOURCE, "")

            val expectedUiEventState = UiEvent.EVENT_SUCCESS_LOAD_PROMO_PAGE
            val expectedFragmentUiModel = TokoFoodPromoUiModelMapper.mapResponseDataToFragmentUiModel(response.data)
            val expectedVisitables = TokoFoodPromoUiModelMapper.mapResponseDataToVisitables(response.data)
            assertEquals(expectedUiEventState, viewModel.uiEvent.value?.state)
            assertEquals(expectedFragmentUiModel, viewModel.fragmentUiModel.value)
            assertEquals(expectedVisitables, viewModel.visitables.value)
        }
    }

    @Test
    fun `when loadData response is not success, should set ui event value to failure`() {
        runBlocking {
            val response = PromoListTokoFood(
                status = "ERROR"
            )

            coEvery {
                promoListTokoFoodUseCase.get().execute(SOURCE, "")
            } returns response

            viewModel.loadData(SOURCE, "")

            val expectedUiEventState = UiEvent.EVENT_FAILED_LOAD_PROMO_PAGE
            assertEquals(expectedUiEventState, viewModel.uiEvent.value?.state)
        }
    }

    @Test
    fun `when loadData should show error page, should set ui event value to error`() {
        runBlocking {
            val response = PromoListTokoFood(
                status = TokoFoodCartUtil.SUCCESS_STATUS,
                data = PromoListTokoFoodData(
                    errorPage = PromoListTokoFoodErrorPage(
                        isShowErrorPage = true
                    )
                )
            )

            coEvery {
                promoListTokoFoodUseCase.get().execute(SOURCE, "")
            } returns response

            viewModel.loadData(SOURCE, "")

            val expectedUiEventState = UiEvent.EVENT_ERROR_PAGE_PROMO_PAGE
            assertEquals(expectedUiEventState, viewModel.uiEvent.value?.state)
        }
    }

    @Test
    fun `when loadData resulted in no coupons, should set ui event value to no coupon`() {
        runBlocking {
            val response = PromoListTokoFood(
                status = TokoFoodCartUtil.SUCCESS_STATUS,
                data = PromoListTokoFoodData()
            )

            coEvery {
                promoListTokoFoodUseCase.get().execute(SOURCE, "")
            } returns response

            viewModel.loadData(SOURCE, "")

            val expectedUiEventState = UiEvent.EVENT_NO_COUPON
            assertEquals(expectedUiEventState, viewModel.uiEvent.value?.state)
        }
    }

    @Test
    fun `when loadData failed, should set ui event state to failed load`() {
        runBlocking {
            coEvery {
                promoListTokoFoodUseCase.get().execute(SOURCE, "")
            } throws MessageErrorException("")

            viewModel.loadData(SOURCE, "")

            val expectedUiEventState = UiEvent.EVENT_FAILED_LOAD_PROMO_PAGE
            assertEquals(expectedUiEventState, viewModel.uiEvent.value?.state)
        }
    }

    @Test
    fun `when showChangeRestrictionMessage should set ui event to show toaster`() {
        viewModel.showChangeRestrictionMessage()

        val expectedUiEventState = UiEvent.EVENT_SHOW_TOASTER
        assertEquals(expectedUiEventState, viewModel.uiEvent.value?.state)
    }

}
