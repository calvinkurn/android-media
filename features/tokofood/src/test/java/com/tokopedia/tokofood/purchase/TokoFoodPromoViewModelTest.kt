package com.tokopedia.tokofood.purchase

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.PromoListTokoFood
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.PromoListTokoFoodCoupon
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.PromoListTokoFoodData
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
                promoListTokoFoodUseCase.get().execute(SOURCE)
            } returns response

            viewModel.loadData()

            val expectedUiEventState = UiEvent.EVENT_SUCCESS_LOAD_PROMO_PAGE
            val expectedFragmentUiModel = TokoFoodPromoUiModelMapper.mapResponseDataToFragmentUiModel(response.data)
            val expectedVisitables = TokoFoodPromoUiModelMapper.mapResponseDataToVisitables(response.data)
            assertEquals(expectedUiEventState, viewModel.uiEvent.value?.state)
            assertEquals(expectedFragmentUiModel, viewModel.fragmentUiModel.value)
            assertEquals(expectedVisitables, viewModel.visitables.value)
        }
    }
    @Test
    fun `when loadData failed, should set ui event state to failed load`() {
        runBlocking {
            coEvery {
                promoListTokoFoodUseCase.get().execute(SOURCE)
            } throws MessageErrorException("")

            viewModel.loadData()

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