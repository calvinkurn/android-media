package com.tokopedia.gamification.giftbox.usecase

import com.tokopedia.gamification.giftbox.data.entities.GiftBoxEntity
import com.tokopedia.gamification.giftbox.domain.GiftBoxDailyUseCase
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

class GiftBoxDailyUseCaseTest {
    lateinit var giftBoxDailyUseCase: GiftBoxDailyUseCase
    val queryString = ""

    @MockK
    lateinit var gqlWrapper: GqlUseCaseWrapper

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        giftBoxDailyUseCase = GiftBoxDailyUseCase(queryString, gqlWrapper)
    }

    @Test
    fun `check function invokation of Gql wrapper`() {
        runBlockingTest {
            val clazz = GiftBoxEntity::class.java
            val map = HashMap<String, Any>()
            coEvery { gqlWrapper.getResponse(clazz, queryString, map) } returns mockk()

            giftBoxDailyUseCase.getResponse(map)
            coVerify(exactly = 1) { gqlWrapper.getResponse(clazz, queryString, map) }
        }
    }

    @Test
    fun `check query params`() {
        val param1 = "124"
        val map = giftBoxDailyUseCase.getRequestParams(param1)
        Assert.assertEquals(map[GiftBoxDailyUseCase.Params.PAGE], param1)

    }

}