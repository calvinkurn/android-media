package com.tokopedia.oneclickcheckout.order.view

import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.oneclickcheckout.order.view.model.OccButtonState
import com.tokopedia.oneclickcheckout.order.view.model.OrderPromo
import com.tokopedia.promousage.data.response.EntryPointInfo
import com.tokopedia.promousage.data.response.GetPromoListRecommendationEntryPointResponse
import com.tokopedia.promousage.data.response.GetPromoListRecommendationResponseData
import com.tokopedia.promousage.data.response.PromoListRecommendation
import com.tokopedia.promousage.data.response.ResultStatus
import com.tokopedia.promousage.domain.entity.PromoEntryPointInfo
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyAdditionalInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUsageSummariesUiModel
import io.mockk.coEvery
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Test
import kotlin.Exception

@ExperimentalCoroutinesApi
class OrderSummaryPageViewModelPromoEntryPointTest : BaseOrderSummaryPageViewModelTest() {

    @Test
    fun getEntryPointInfo_successWithAnimateWording() {
        // given
        val oldLastApply = LastApplyUiModel(
            additionalInfo = LastApplyAdditionalInfoUiModel(
                usageSummaries = listOf(
                    LastApplyUsageSummariesUiModel(
                        amount = 10_000
                    )
                )
            )
        )
        val newLastApply = LastApplyUiModel(
            additionalInfo = LastApplyAdditionalInfoUiModel(
                usageSummaries = listOf(
                    LastApplyUsageSummariesUiModel(
                        amount = 12_000
                    )
                )
            )
        )
        val orderPromo = OrderPromo(
            isPromoRevamp = true
        )
        orderSummaryPageViewModel.orderPromo.value = orderPromo
        val resultPromoEntryPointInfo = PromoEntryPointInfo(
            isSuccess = true,
            messages = listOf("message")
        )
        every {
            chosenAddressRequestHelper.getChosenAddress()
        } returns ChosenAddress()
        coEvery {
            getPromoListRecommendationEntryPointUseCase(any())
        } returns GetPromoListRecommendationEntryPointResponse(
            GetPromoListRecommendationResponseData(
                PromoListRecommendation(
                    resultStatus = ResultStatus(
                        success = true
                    ),
                    entryPointInfo = EntryPointInfo(
                        messages = listOf("message")
                    )
                )
            )
        )

        // when
        orderSummaryPageViewModel.getEntryPointInfo(
            lastApply = newLastApply,
            oldLastApply = oldLastApply
        )

        // then
        val resultOrderPromo = orderPromo.copy(
            isAnimateWording = true,
            entryPointInfo = resultPromoEntryPointInfo,
            lastApply = newLastApply.copy(
                additionalInfo = LastApplyAdditionalInfoUiModel(
                    usageSummaries = listOf(
                        LastApplyUsageSummariesUiModel(
                            amount = 12_000
                        )
                    )
                )
            ),
            state = OccButtonState.NORMAL
        )
        Assert.assertEquals(resultOrderPromo, orderSummaryPageViewModel.orderPromo.value)
    }

    @Test
    fun getEntryPointInfo_successWithNoAnimateWording() {
        // given
        val oldLastApply = LastApplyUiModel(
            additionalInfo = LastApplyAdditionalInfoUiModel(
                usageSummaries = listOf(
                    LastApplyUsageSummariesUiModel(
                        amount = 10_000
                    )
                )
            )
        )
        val newLastApply = LastApplyUiModel(
            additionalInfo = LastApplyAdditionalInfoUiModel(
                usageSummaries = listOf(
                    LastApplyUsageSummariesUiModel(
                        amount = 9_000
                    )
                )
            )
        )
        val orderPromo = OrderPromo(
            isPromoRevamp = true
        )
        orderSummaryPageViewModel.orderPromo.value = orderPromo
        val resultPromoEntryPointInfo = PromoEntryPointInfo(
            isSuccess = true,
            messages = listOf("message")
        )
        every {
            chosenAddressRequestHelper.getChosenAddress()
        } returns ChosenAddress()
        coEvery {
            getPromoListRecommendationEntryPointUseCase(any())
        } returns GetPromoListRecommendationEntryPointResponse(
            GetPromoListRecommendationResponseData(
                PromoListRecommendation(
                    resultStatus = ResultStatus(
                        success = true
                    ),
                    entryPointInfo = EntryPointInfo(
                        messages = listOf("message")
                    )
                )
            )
        )

        // when
        orderSummaryPageViewModel.getEntryPointInfo(
            lastApply = newLastApply,
            oldLastApply = oldLastApply
        )

        // then
        val resultOrderPromo = orderPromo.copy(
            isAnimateWording = false,
            entryPointInfo = resultPromoEntryPointInfo,
            lastApply = newLastApply.copy(
                additionalInfo = LastApplyAdditionalInfoUiModel(
                    usageSummaries = listOf(
                        LastApplyUsageSummariesUiModel(
                            amount = 9_000
                        )
                    )
                )
            ),
            state = OccButtonState.NORMAL
        )
        Assert.assertEquals(resultOrderPromo, orderSummaryPageViewModel.orderPromo.value)
    }

    @Test
    fun getEntryPointInfo_successWithNoOldLastApply() {
        // given
        val newLastApply = LastApplyUiModel(
            additionalInfo = LastApplyAdditionalInfoUiModel(
                usageSummaries = listOf(
                    LastApplyUsageSummariesUiModel(
                        amount = 9_000
                    )
                )
            )
        )
        val orderPromo = OrderPromo(
            isPromoRevamp = true
        )
        orderSummaryPageViewModel.orderPromo.value = orderPromo
        val resultPromoEntryPointInfo = PromoEntryPointInfo(
            isSuccess = true,
            messages = listOf("message")
        )
        every {
            chosenAddressRequestHelper.getChosenAddress()
        } returns ChosenAddress()
        coEvery {
            getPromoListRecommendationEntryPointUseCase(any())
        } returns GetPromoListRecommendationEntryPointResponse(
            GetPromoListRecommendationResponseData(
                PromoListRecommendation(
                    resultStatus = ResultStatus(
                        success = true
                    ),
                    entryPointInfo = EntryPointInfo(
                        messages = listOf("message")
                    )
                )
            )
        )

        // when
        orderSummaryPageViewModel.getEntryPointInfo(
            lastApply = newLastApply,
            oldLastApply = null
        )

        // then
        val resultOrderPromo = orderPromo.copy(
            isAnimateWording = false,
            entryPointInfo = resultPromoEntryPointInfo,
            lastApply = newLastApply.copy(
                additionalInfo = LastApplyAdditionalInfoUiModel(
                    usageSummaries = listOf(
                        LastApplyUsageSummariesUiModel(
                            amount = 9_000
                        )
                    )
                )
            ),
            state = OccButtonState.NORMAL
        )
        Assert.assertEquals(resultOrderPromo, orderSummaryPageViewModel.orderPromo.value)
    }

    @Test
    fun getEntryPointInfo_successWithEntryPointDisabled() {
        // given
        val oldLastApply = LastApplyUiModel(
            additionalInfo = LastApplyAdditionalInfoUiModel(
                usageSummaries = listOf(
                    LastApplyUsageSummariesUiModel(
                        amount = 10_000
                    )
                )
            )
        )
        val newLastApply = LastApplyUiModel(
            additionalInfo = LastApplyAdditionalInfoUiModel(
                usageSummaries = listOf(
                    LastApplyUsageSummariesUiModel(
                        amount = 9_000
                    )
                )
            )
        )
        val orderPromo = OrderPromo(
            isPromoRevamp = true
        )
        orderSummaryPageViewModel.orderPromo.value = orderPromo
        val resultPromoEntryPointInfo = PromoEntryPointInfo(
            isSuccess = false,
            messages = listOf("message")
        )
        every {
            chosenAddressRequestHelper.getChosenAddress()
        } returns ChosenAddress()
        coEvery {
            getPromoListRecommendationEntryPointUseCase(any())
        } returns GetPromoListRecommendationEntryPointResponse(
            GetPromoListRecommendationResponseData(
                PromoListRecommendation(
                    resultStatus = ResultStatus(
                        success = false
                    ),
                    entryPointInfo = EntryPointInfo(
                        messages = listOf("message")
                    )
                )
            )
        )

        // when
        orderSummaryPageViewModel.getEntryPointInfo(
            lastApply = newLastApply,
            oldLastApply = oldLastApply
        )

        // then
        val resultOrderPromo = orderPromo.copy(
            isAnimateWording = false,
            entryPointInfo = resultPromoEntryPointInfo,
            lastApply = newLastApply.copy(
                additionalInfo = LastApplyAdditionalInfoUiModel(
                    usageSummaries = listOf(
                        LastApplyUsageSummariesUiModel(
                            amount = 9_000
                        )
                    )
                )
            ),
            state = OccButtonState.DISABLE
        )
        Assert.assertEquals(resultOrderPromo, orderSummaryPageViewModel.orderPromo.value)
    }

    @Test
    fun getEntryPointInfo_error() {
        // given
        val oldLastApply = LastApplyUiModel(
            additionalInfo = LastApplyAdditionalInfoUiModel(
                usageSummaries = listOf(
                    LastApplyUsageSummariesUiModel(
                        amount = 10_000
                    )
                )
            )
        )
        val newLastApply = LastApplyUiModel(
            additionalInfo = LastApplyAdditionalInfoUiModel(
                usageSummaries = listOf(
                    LastApplyUsageSummariesUiModel(
                        amount = 9_000
                    )
                )
            )
        )
        val orderPromo = OrderPromo(
            isPromoRevamp = true
        )
        orderSummaryPageViewModel.orderPromo.value = orderPromo
        every {
            chosenAddressRequestHelper.getChosenAddress()
        } returns ChosenAddress()
        coEvery {
            getPromoListRecommendationEntryPointUseCase(any())
        } throws Exception("error")

        // when
        orderSummaryPageViewModel.getEntryPointInfo(
            lastApply = newLastApply,
            oldLastApply = oldLastApply
        )

        // then
        val resultOrderPromo = orderPromo.copy(
            lastApply = newLastApply.copy(
                additionalInfo = LastApplyAdditionalInfoUiModel(
                    usageSummaries = listOf(
                        LastApplyUsageSummariesUiModel(
                            amount = 9_000
                        )
                    )
                )
            ),
            state = OccButtonState.DISABLE
        )
        Assert.assertEquals(resultOrderPromo, orderSummaryPageViewModel.orderPromo.value)
    }
}
