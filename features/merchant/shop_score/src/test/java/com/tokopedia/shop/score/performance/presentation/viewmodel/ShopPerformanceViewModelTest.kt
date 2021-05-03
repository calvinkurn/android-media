package com.tokopedia.shop.score.performance.presentation.viewmodel

import com.tokopedia.gm.common.constant.TRANSITION_PERIOD
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.performance.domain.model.ShopScoreLevelResponse
import com.tokopedia.shop.score.performance.domain.model.ShopScoreWrapperResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import junit.framework.Assert.assertEquals
import org.junit.Test

class ShopPerformanceViewModelTest : ShopPerformanceViewModelTestFixture() {

    @Test
    fun `when getShopInfo should set live data success`() {
        coroutineTestRule.runBlockingTest {
            val shopInfoPeriodUiModel = ShopInfoPeriodUiModel(periodType = TRANSITION_PERIOD, shopAge = 80, isNewSeller = true)
            onGetShopInfoPeriodUseCase_thenReturn(shopInfoPeriodUiModel)
            shopPerformanceViewModel.getShopInfoPeriod()
            verifyGetShopInfoPeriodUseCaseCalled()
            val actualResult = (shopPerformanceViewModel.shopInfoPeriod.value as Success).data
            assertEquals(shopInfoPeriodUiModel, actualResult)
            assertEquals(shopInfoPeriodUiModel.periodType, actualResult.periodType)
            assertEquals(shopInfoPeriodUiModel.isNewSeller, actualResult.isNewSeller)
        }
    }

    @Test
    fun `when getShopInfo should set live data error`() {
        coroutineTestRule.runBlockingTest {
            val errorException = MessageErrorException()
            onGetShopInfoPeriodUseCaseError_thenReturn(errorException)
            shopPerformanceViewModel.getShopInfoPeriod()
            verifyGetShopInfoPeriodUseCaseCalled()
            val actualResult = (shopPerformanceViewModel.shopInfoPeriod.value as Fail).throwable::class.java
            val expectedResult = errorException::class.java
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when getShopScoreLevel should set live data success`() {
        coroutineTestRule.runBlockingTest {
            val shopInfoPeriodUiModel = ShopInfoPeriodUiModel(periodType = TRANSITION_PERIOD, shopAge = 80, isNewSeller = true)
            val shopScoreWrapperResponse = ShopScoreWrapperResponse(
                    shopScoreLevelResponse = ShopScoreLevelResponse.ShopScoreLevel(
                            result = ShopScoreLevelResponse.ShopScoreLevel.Result(shopLevel = 4, shopScore = 80)
                    ),
            )
            onGetShopPerformanceUseCase_thenReturn(shopScoreWrapperResponse)
            shopPerformanceViewModel.getShopScoreLevel(shopInfoPeriodUiModel)
            verifyGetShopPerformanceUseCaseCalled()
            val actualResult = (shopPerformanceViewModel.shopPerformancePage.value as Success).data.second
            val expectedResult = shopScoreWrapperResponse.shopScoreLevelResponse?.result
            assertEquals(shopScoreWrapperResponse, actualResult)
            assertEquals(expectedResult?.shopLevel, actualResult.shopScoreLevelResponse?.result?.shopLevel)
            assertEquals(expectedResult?.shopScore, actualResult.shopScoreLevelResponse?.result?.shopScore)
        }
    }

    @Test
    fun `when getShopScoreLevel should set live data error`() {
        coroutineTestRule.runBlockingTest {
            val errorException = MessageErrorException()
            val shopInfoPeriodUiModel = ShopInfoPeriodUiModel(periodType = TRANSITION_PERIOD, shopAge = 80, isNewSeller = true)
            onGetShopPerformanceUseCaseError_thenReturn(errorException)
            shopPerformanceViewModel.getShopScoreLevel(shopInfoPeriodUiModel)
            verifyGetShopPerformanceUseCaseCalled()
            val actualResult = (shopPerformanceViewModel.shopPerformancePage.value as Fail).throwable::class.java
            val expectedResult = errorException::class.java
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when getShopInfoLevel should set live data success`() {
        val level = 4
        val shopInfoLevelMapper = shopScoreMapper.mapToShopInfoLevelUiModel(level)
        shopPerformanceViewModel.getShopInfoLevel(level)
        val actualResult = shopPerformanceViewModel.shopInfoLevel.value
        assertEquals(shopInfoLevelMapper.nextUpdate, actualResult?.nextUpdate)
        assertEquals(shopInfoLevelMapper.periodDate, actualResult?.periodDate)
        assertEquals(shopInfoLevelMapper.productSold, actualResult?.productSold)
        assertEquals(shopInfoLevelMapper.shopIncome, actualResult?.shopIncome)
        assertEquals(shopInfoLevelMapper.cardTooltipLevelList, actualResult?.cardTooltipLevelList)
    }

    @Test
    fun `when getShopPerformanceDetail should set data success`() {
        val identifierPerformance = ShopScoreConstant.ORDER_SUCCESS_RATE_KEY
        val shopPerformanceDetailMapper = shopScoreMapper.mapToShopPerformanceDetail(identifierPerformance)
        shopPerformanceViewModel.getShopPerformanceDetail(identifierPerformance)
        val actualResult = shopPerformanceViewModel.shopPerformanceDetail.value
        assertEquals(shopPerformanceDetailMapper.urlLink, actualResult?.urlLink)
        assertEquals(shopPerformanceDetailMapper.descCalculation, actualResult?.descCalculation)
        assertEquals(shopPerformanceDetailMapper.moreInformation, actualResult?.moreInformation)
        assertEquals(shopPerformanceDetailMapper.descTips, actualResult?.descTips)
    }
}