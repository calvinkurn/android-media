package com.tokopedia.tokofood.purchase

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.MerchantPromoListTokoFoodCoupon
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.MerchantPromoListTokoFoodErrorPage
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.MerchantPromoListTokoFoodSection
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.MerchantPromoListTokoFoodSubSection
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.PromoListTokoFood
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.PromoListTokoFoodData
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.UiEvent
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.mapper.MerchantTokoFoodPromoUiModelMapper
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

@FlowPreview
@ExperimentalCoroutinesApi
class MerchantTokoFoodPromoViewModelTest : MerchantTokoFoodPromoViewModelTestFixture() {

    @Test
    fun `when loadData success, should set ui event value`() {
        runBlocking {
            val merchantId = "123"
            val response = PromoListTokoFood(
                status = TokoFoodCartUtil.SUCCESS_STATUS,
                data = PromoListTokoFoodData(
                    availableSection = MerchantPromoListTokoFoodSection(
                        subSection = MerchantPromoListTokoFoodSubSection(
                            coupons = listOf(
                                MerchantPromoListTokoFoodCoupon()
                            )
                        )
                    )
                )
            )

            coEvery {
                promoListTokoFoodUseCase.get().execute(SOURCE, merchantId)
            } returns response

            viewModel.loadData(SOURCE, merchantId)

            val expectedUiEventState = UiEvent.EVENT_SUCCESS_LOAD_PROMO_PAGE

            val expectedFragmentUiModel = MerchantTokoFoodPromoUiModelMapper.mapResponseDataToFragmentUiModel(response.data)
            val expectedVisitables = MerchantTokoFoodPromoUiModelMapper.mapResponseDataToVisitables(response.data)

            assertEquals(expectedUiEventState, viewModel.uiEvent.value?.state)
            assertEquals(expectedFragmentUiModel, viewModel.fragmentUiModel.value)
            assertEquals(expectedVisitables, viewModel.visitables.value)
        }
    }

    @Test
    fun `when loadData success but only unavailable ones, should still set success ui event value`() {
        runBlocking {
            val merchantId = "123"
            val response = PromoListTokoFood(
                status = TokoFoodCartUtil.SUCCESS_STATUS,
                data = PromoListTokoFoodData(
                    unavailableSection = MerchantPromoListTokoFoodSection(
                        subSection = MerchantPromoListTokoFoodSubSection(
                            coupons = listOf(
                                MerchantPromoListTokoFoodCoupon()
                            )
                        )
                    ),
                    changeRestrictionMessage = "This coupon cannot be used"
                )
            )

            coEvery {
                promoListTokoFoodUseCase.get().execute(SOURCE, merchantId)
            } returns response

            viewModel.loadData(SOURCE, merchantId)

            val expectedUiEventState = UiEvent.EVENT_SUCCESS_LOAD_PROMO_PAGE
            val expectedFragmentUiModel = MerchantTokoFoodPromoUiModelMapper.mapResponseDataToFragmentUiModel(response.data)
            val expectedVisitables = MerchantTokoFoodPromoUiModelMapper.mapResponseDataToVisitables(response.data)
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
            val merchantId = "123"
            val response = PromoListTokoFood(
                status = TokoFoodCartUtil.SUCCESS_STATUS,
                data = PromoListTokoFoodData(
                    errorPage = MerchantPromoListTokoFoodErrorPage(
                        isShowErrorPage = true
                    )
                )
            )

            coEvery {
                promoListTokoFoodUseCase.get().execute(SOURCE, merchantId)
            } returns response

            viewModel.loadData(SOURCE, merchantId)

            val expectedUiEventState = UiEvent.EVENT_ERROR_PAGE_PROMO_PAGE
            assertEquals(expectedUiEventState, viewModel.uiEvent.value?.state)
        }
    }

    @Test
    fun `when loadData resulted in no coupons, should set ui event value to no coupon`() {
        runBlocking {
            val merchantId = "123"
            val response = PromoListTokoFood(
                status = TokoFoodCartUtil.SUCCESS_STATUS,
                data = PromoListTokoFoodData()
            )

            coEvery {
                promoListTokoFoodUseCase.get().execute(SOURCE, merchantId)
            } returns response

            viewModel.loadData(SOURCE, merchantId)

            val expectedUiEventState = UiEvent.EVENT_NO_COUPON
            assertEquals(expectedUiEventState, viewModel.uiEvent.value?.state)
        }
    }

    @Test
    fun `when loadData failed, should set ui event state to failed load`() {
        runBlocking {
            val merchantId = "123"
            coEvery {
                promoListTokoFoodUseCase.get().execute(SOURCE, merchantId)
            } throws MessageErrorException("")

            viewModel.loadData(SOURCE, merchantId)

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
