package com.tokopedia.gamification.giftbox.usecase

import com.tokopedia.gamification.giftbox.data.entities.GiftBoxRewardEntity
import com.tokopedia.gamification.giftbox.domain.GiftBoxDailyRewardUseCase
import com.tokopedia.gamification.pdp.data.GqlUseCaseWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GiftBoxDailyRewardUseCaseTest {
    lateinit var giftBoxDailyRewardUseCase: GiftBoxDailyRewardUseCase
    val queryString = ""

    @MockK
    lateinit var gqlWrapper: GqlUseCaseWrapper

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        giftBoxDailyRewardUseCase = GiftBoxDailyRewardUseCase(queryString, gqlWrapper)
    }

    @Test
    fun `check function invokation of Gql wrapper`() {
        runBlockingTest {
            val map = HashMap<String, Any>()
            coEvery { gqlWrapper.getResponse(GiftBoxRewardEntity::class.java, queryString, map) } returns mockk()

            giftBoxDailyRewardUseCase.getResponse(map)
            coVerify(exactly = 1) { gqlWrapper.getResponse(GiftBoxRewardEntity::class.java, queryString, map) }
        }
    }

    @Test
    fun `check query params`() {
        val param1 = "124"
        val param2 = "234"
        val map = giftBoxDailyRewardUseCase.getRequestParams(param1, param2)
        Assert.assertEquals(map[GiftBoxDailyRewardUseCase.Params.CAMPAIGN_SLUG], param1)
        Assert.assertEquals(map[GiftBoxDailyRewardUseCase.Params.UNIQUE_CODE], param2)

    }

}