package com.tokopedia.shop.score.penalty.presentation

import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ShopPenaltyDetailViewModelTest : ShopPenaltyDetailViewModelTestFixture() {

    @Test
    fun `when getPenaltyDetailData should set live data success`() {
        val itemPenaltyUiModel = ItemPenaltyUiModel(
                statusPenalty = ShopScoreConstant.ON_GOING,
                endDateDetail = "09 Mei 2021", startDate = "12 Apr 2021",
                typePenalty = "Penolakan Pengiriman", deductionPoint = "-1",
                reasonPenalty = "Seller melakukan cash advance pada transaksi INV/20210126/XX/V/553738330"
        )

        val expectedResult = penaltyMapper.mapToPenaltyDetail(itemPenaltyUiModel)

        shopPenaltyDetailViewModel.getPenaltyDetailData(itemPenaltyUiModel)

        val actualResult = shopPenaltyDetailViewModel.penaltyDetailData.value

        assertEquals(expectedResult.titleDetail, actualResult?.titleDetail)
        assertEquals(expectedResult.startDateDetail, actualResult?.startDateDetail)
        assertEquals(expectedResult.endDateDetail, actualResult?.endDateDetail)
        assertEquals(expectedResult.deductionPointPenalty, actualResult?.deductionPointPenalty)
        assertNotNull(actualResult?.stepperPenaltyDetailList)
    }
}