package com.tokopedia.shop.score.detail_old.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gm.common.constant.COMMUNICATION_PERIOD
import com.tokopedia.gm.common.domain.interactor.GetShopInfoPeriodUseCase
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.score.detail_old.domain.model.ShopScoreResponse
import com.tokopedia.shop.score.detail_old.domain.usecase.GetShopScoreUseCase
import com.tokopedia.shop.score.detail_old.view.mapper.ShopScoreDetailMapper
import com.tokopedia.shop.score.detail_old.view.model.ShopScoreDetailData
import com.tokopedia.shop.score.detail_old.view.model.ShopType
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShopScoreDetailViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getShopScoreUseCase: GetShopScoreUseCase

    @RelaxedMockK
    lateinit var getShopInfoPeriodUseCase: GetShopInfoPeriodUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    private lateinit var mapper: ShopScoreDetailMapper
    private lateinit var viewModel: ShopScoreDetailViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mapper = ShopScoreDetailMapper(userSession)

        viewModel = ShopScoreDetailViewModel(
                getShopScoreUseCase,
                getShopInfoPeriodUseCase,
                userSession,
                mapper,
                CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `when get shop score detail success should set live data success`() {
        val shopId = "1"
        val shopType = ShopType.OFFICIAL_STORE
        val response = ShopScoreResponse()
        val data = ShopScoreDetailData(shopType)
        val shopInfoPeriodUiModel = ShopInfoPeriodUiModel(periodType = COMMUNICATION_PERIOD)

        every { userSession.shopId } returns shopId
        every { userSession.isShopOfficialStore } returns true
        every { userSession.isGoldMerchant } returns false

        coEvery { getShopScoreUseCase.execute(shopId) } returns response.data
        every { getShopInfoPeriodUseCase.requestParams } returns GetShopInfoPeriodUseCase.createParams(shopId.toLongOrZero())
        coEvery { getShopInfoPeriodUseCase.executeOnBackground() } returns shopInfoPeriodUiModel

        viewModel.getShopScoreDetail()

        val expectedResult = Success(data)
        val actualResult = (viewModel.shopScoreData.value as Success).data

        coVerify { getShopScoreUseCase.execute(shopId) }
        coVerify { getShopInfoPeriodUseCase.executeOnBackground() }

        assertEquals(expectedResult.data, actualResult.first)
        assertEquals(shopInfoPeriodUiModel.periodType, actualResult.second.periodType)
    }
}