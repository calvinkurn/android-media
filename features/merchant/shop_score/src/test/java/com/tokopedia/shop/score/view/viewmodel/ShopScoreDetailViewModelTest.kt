package com.tokopedia.shop.score.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.score.domain.model.ShopScoreResponse
import com.tokopedia.shop.score.domain.usecase.GetShopScoreUseCase
import com.tokopedia.shop.score.view.mapper.ShopScoreDetailMapper
import com.tokopedia.shop.score.view.model.ShopScoreDetailData
import com.tokopedia.shop.score.view.model.ShopType
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShopScoreDetailViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var getShopScoreUseCase: GetShopScoreUseCase
    private lateinit var userSession: UserSessionInterface
    private lateinit var mapper: ShopScoreDetailMapper
    private lateinit var viewModel: ShopScoreDetailViewModel

    @Before
    fun setUp() {
        getShopScoreUseCase = mockk()
        userSession = mockk()

        mapper = ShopScoreDetailMapper(userSession)

        viewModel = ShopScoreDetailViewModel(
            getShopScoreUseCase,
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
}