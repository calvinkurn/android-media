package com.tokopedia.gamification.giftbox.usecase

import com.tokopedia.gamification.GamificationConstants
import com.tokopedia.gamification.data.entity.ResponseCrackResultEntity
import com.tokopedia.gamification.giftbox.domain.GiftBoxDailyUseCase
import com.tokopedia.gamification.giftbox.domain.GiftBoxTapTapCrackUseCase
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

class GiftBoxTapTapCrackUseCaseTest {
    lateinit var giftBoxTapTapCrackUseCase: GiftBoxTapTapCrackUseCase
    val queryString = ""

    @MockK
    lateinit var gqlWrapper: GqlUseCaseWrapper

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        giftBoxTapTapCrackUseCase = GiftBoxTapTapCrackUseCase(queryString, gqlWrapper)
    }

    @Test
    fun `check function invokation of Gql wrapper`() {
        runBlockingTest {
            val clazz = ResponseCrackResultEntity::class.java
            val map = HashMap<String, Any>()
            coEvery { gqlWrapper.getResponse(clazz, queryString, map) } returns mockk()

            giftBoxTapTapCrackUseCase.getResponse(map)
            coVerify(exactly = 1) { gqlWrapper.getResponse(clazz, queryString, map) }
        }
    }

    @Test
    fun `check query params`() {
        val param1 = "124"
        val param2 = 234L
        val map = giftBoxTapTapCrackUseCase.getQueryParams(param1, param2)
        Assert.assertEquals(map[GamificationConstants.GraphQlVariableKeys.TOKEN_ID], param1)
        Assert.assertEquals(map[GamificationConstants.GraphQlVariableKeys.CAMPAIGN_ID], param2)

    }
}