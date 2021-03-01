package com.tokopedia.shop.score.detail.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gm.common.domain.interactor.GetShopInfoUseCase
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.score.detail.domain.model.ShopScoreResponse
import com.tokopedia.shop.score.detail.domain.usecase.GetShopScoreUseCase
import com.tokopedia.shop.score.detail.view.mapper.ShopScoreDetailMapper
import com.tokopedia.shop.score.detail.view.model.ShopScoreDetailData
import com.tokopedia.shop.score.detail.view.model.ShopType
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
    lateinit var getShopInfoUseCase: GetShopInfoUseCase

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
                getShopInfoUseCase,
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

        every { userSession.shopId } returns shopId
        every { userSession.isShopOfficialStore } returns true
        every { userSession.isGoldMerchant } returns false

        coEvery { getShopScoreUseCase.execute(shopId) } returns response.data

        viewModel.getShopScoreDetail()

        val expectedResult = Success(data)
        val actualResult = viewModel.shopScoreData.value

        coVerify { getShopScoreUseCase.execute(shopId) }

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when get shop info period success should set live data success`() {
        val shopId = 1

        getShopInfoUseCase.requestParams = GetShopInfoUseCase.createParams(shopId)
        coEvery { getShopInfoUseCase.executeOnBackground() } returns ShopInfoPeriodUiModel()

        viewModel.getShopInfoPeriod(shopId)

        val expectedResult = Success(mapper.mapToIsShowTickerShopInfo(ShopInfoPeriodUiModel()))
        val actualResult = viewModel.tickerShopInfoPeriod.value

        coVerify { getShopInfoUseCase.executeOnBackground() }

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when get shop score detail error should set live data fail`() {
        val shopId = "1"
        val error = MessageErrorException()

        every { userSession.shopId } returns shopId
        coEvery { getShopScoreUseCase.execute(shopId) } throws error

        viewModel.getShopScoreDetail()

        val expectedError = MessageErrorException::class.java
        val actualError = (viewModel.shopScoreData.value as? Fail)?.let {
            it.throwable::class.java
        }

        coVerify { getShopScoreUseCase.execute(shopId) }

        assertEquals(expectedError, actualError)
    }

    @Test
    fun `when get shop info period error should set live data fail`() {
        val shopId = 1
        val error = MessageErrorException()

        getShopInfoUseCase.requestParams = GetShopInfoUseCase.createParams(shopId)
        coEvery { getShopInfoUseCase.executeOnBackground() } throws error

        viewModel.getShopInfoPeriod(shopId)

        val expectedError = MessageErrorException::class.java
        val actualError = (viewModel.tickerShopInfoPeriod.value as? Fail)?.let {
            it.throwable::class.java
        }

        coVerify { getShopInfoUseCase.executeOnBackground() }

        assertEquals(expectedError, actualError)
    }
}