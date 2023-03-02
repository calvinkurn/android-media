package com.tokopedia.tokofood.purchase

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.CartGeneralBusinessDataCustomResponse
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.CartGeneralPromoListBusinessData
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.CartGeneralPromoListDataData
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.PromoListTokoFoodCoupon
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.PromoListTokoFoodErrorPage
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.PromoListTokoFoodSection
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.PromoListTokoFoodSubSection
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.UiEvent
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.mapper.TokoFoodPromoUiModelMapper
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

@FlowPreview
@ExperimentalCoroutinesApi
class TokoFoodPromoViewModelTest: TokoFoodPromoViewModelTestFixture() {

    @Test
    fun `when loadData success, should set ui event value`() {
        runBlocking {
            val merchantId = "123"
            val cartList: List<String> = listOf()
            val response = CartGeneralPromoListDataData(
                businessData = listOf(
                    CartGeneralPromoListBusinessData(
                        businessId = TokoFoodCartUtil.getBusinessId(),
                        success = TokoFoodCartUtil.SUCCESS_STATUS_INT,
                        customResponse = CartGeneralBusinessDataCustomResponse(
                            availableSection = PromoListTokoFoodSection(
                                subSection = PromoListTokoFoodSubSection(
                                    coupons = listOf(
                                        PromoListTokoFoodCoupon()
                                    )
                                )
                            )
                        )
                    )
                )
            )

            coEvery {
                promoListTokoFoodUseCase.get().execute(SOURCE, merchantId, cartList)
            } returns response

            viewModel.loadData(SOURCE, merchantId, cartList)

            val expectedUiEventState = UiEvent.EVENT_SUCCESS_LOAD_PROMO_PAGE
            val expectedFragmentUiModel = TokoFoodPromoUiModelMapper.mapResponseDataToFragmentUiModel(response.getTokofoodBusinessData().customResponse)
            val expectedVisitables = TokoFoodPromoUiModelMapper.mapResponseDataToVisitables(response.getTokofoodBusinessData().customResponse)
            assertEquals(expectedUiEventState, viewModel.uiEvent.value?.state)
            assertEquals(expectedFragmentUiModel, viewModel.fragmentUiModel.value)
            assertEquals(expectedVisitables, viewModel.visitables.value)
        }
    }

    @Test
    fun `when loadData success but only unavailable ones, should still set success ui event value`() {
        runBlocking {
            val merchantId = "123"
            val cartList: List<String> = listOf()
            val response = CartGeneralPromoListDataData(
                businessData = listOf(
                    CartGeneralPromoListBusinessData(
                        businessId = TokoFoodCartUtil.getBusinessId(),
                        success = TokoFoodCartUtil.SUCCESS_STATUS_INT,
                        customResponse = CartGeneralBusinessDataCustomResponse(
                            changeRestrictionMessage = "This coupon cannot be used",
                            unavailableSection = PromoListTokoFoodSection(
                                subSection = PromoListTokoFoodSubSection(
                                    coupons = listOf(
                                        PromoListTokoFoodCoupon()
                                    )
                                )
                            )
                        )
                    )
                )
            )

            coEvery {
                promoListTokoFoodUseCase.get().execute(SOURCE, merchantId, cartList)
            } returns response

            viewModel.loadData(SOURCE, merchantId, cartList)

            val expectedUiEventState = UiEvent.EVENT_SUCCESS_LOAD_PROMO_PAGE
            val expectedFragmentUiModel = TokoFoodPromoUiModelMapper.mapResponseDataToFragmentUiModel(response.getTokofoodBusinessData().customResponse)
            val expectedVisitables = TokoFoodPromoUiModelMapper.mapResponseDataToVisitables(response.getTokofoodBusinessData().customResponse)
            assertEquals(expectedUiEventState, viewModel.uiEvent.value?.state)
            assertEquals(expectedFragmentUiModel, viewModel.fragmentUiModel.value)
            assertEquals(expectedVisitables, viewModel.visitables.value)
        }
    }

    @Test
    fun `when loadData response is not success, should set ui event value to failure`() {
        runBlocking {
            val merchantId = "123"
            val cartList: List<String> = listOf()
            val response = CartGeneralPromoListDataData(
                businessData = listOf(
                    CartGeneralPromoListBusinessData(
                        businessId = TokoFoodCartUtil.getBusinessId(),
                        success = 0,
                    )
                )
            )

            coEvery {
                promoListTokoFoodUseCase.get().execute(SOURCE, merchantId, cartList)
            } returns response

            viewModel.loadData(SOURCE, merchantId, cartList)

            val expectedUiEventState = UiEvent.EVENT_FAILED_LOAD_PROMO_PAGE
            assertEquals(expectedUiEventState, viewModel.uiEvent.value?.state)
        }
    }

    @Test
    fun `when loadData should show error page, should set ui event value to error`() {
        runBlocking {
            val merchantId = "123"
            val cartList: List<String> = listOf()
            val response = CartGeneralPromoListDataData(
                businessData = listOf(
                    CartGeneralPromoListBusinessData(
                        businessId = TokoFoodCartUtil.getBusinessId(),
                        success = TokoFoodCartUtil.SUCCESS_STATUS_INT,
                        customResponse = CartGeneralBusinessDataCustomResponse(
                            errorPage = PromoListTokoFoodErrorPage(
                                isShowErrorPage = true
                            )
                        )
                    )
                )
            )

            coEvery {
                promoListTokoFoodUseCase.get().execute(SOURCE, merchantId, cartList)
            } returns response

            viewModel.loadData(SOURCE, merchantId, cartList)

            val expectedUiEventState = UiEvent.EVENT_ERROR_PAGE_PROMO_PAGE
            assertEquals(expectedUiEventState, viewModel.uiEvent.value?.state)
        }
    }

    @Test
    fun `when loadData resulted in no coupons, should set ui event value to no coupon`() {
        runBlocking {
            val merchantId = "123"
            val cartList: List<String> = listOf()
            val response = CartGeneralPromoListDataData(
                businessData = listOf(
                    CartGeneralPromoListBusinessData(
                        businessId = TokoFoodCartUtil.getBusinessId(),
                        success = TokoFoodCartUtil.SUCCESS_STATUS_INT,
                    )
                )
            )

            coEvery {
                promoListTokoFoodUseCase.get().execute(SOURCE, merchantId, cartList)
            } returns response

            viewModel.loadData(SOURCE, merchantId, cartList)

            val expectedUiEventState = UiEvent.EVENT_NO_COUPON
            assertEquals(expectedUiEventState, viewModel.uiEvent.value?.state)
        }
    }

    @Test
    fun `when loadData failed, should set ui event state to failed load`() {
        runBlocking {
            val merchantId = "123"
            val cartList: List<String> = listOf()
            coEvery {
                promoListTokoFoodUseCase.get().execute(SOURCE, merchantId, cartList)
            } throws MessageErrorException("")

            viewModel.loadData(SOURCE, merchantId, cartList)

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
