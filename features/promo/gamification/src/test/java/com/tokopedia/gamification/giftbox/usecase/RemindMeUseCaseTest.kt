package com.tokopedia.gamification.giftbox.usecase

import com.tokopedia.gamification.giftbox.data.entities.RemindMeCheckEntity
import com.tokopedia.gamification.giftbox.data.entities.RemindMeEntity
import com.tokopedia.gamification.giftbox.domain.RemindMeUseCase
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

class RemindMeUseCaseTest {
    lateinit var remindMeUseCase: RemindMeUseCase
    val queryStringRemindMe = "A"
    val queryStringRemindMeCheck = "B"
    val clazzRemindMeEntity = RemindMeEntity::class.java
    val clazzRemindMeCheckEntity = RemindMeCheckEntity::class.java

    @MockK
    lateinit var gqlWrapper: GqlUseCaseWrapper

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        remindMeUseCase = RemindMeUseCase(queryStringRemindMe, queryStringRemindMeCheck, gqlWrapper)
    }

    @Test
    fun `check function invokation of Gql wrapper in remindMe`() {
        runBlockingTest {
            val map = HashMap<String, Any>()
            coEvery { gqlWrapper.getResponse(clazzRemindMeEntity, queryStringRemindMe, map) } returns mockk()

            remindMeUseCase.getRemindMeResponse(map)
            coVerify(exactly = 1) { gqlWrapper.getResponse(clazzRemindMeEntity, queryStringRemindMe, map) }
        }
    }

    @Test
    fun `check function invokation of Gql wrapper in remindMeCheck`() {
        runBlockingTest {

            val map = HashMap<String, Any>()
            coEvery { gqlWrapper.getResponse(clazzRemindMeCheckEntity, queryStringRemindMeCheck, map) } returns mockk()

            remindMeUseCase.getRemindMeCheckResponse(map)
            coVerify(exactly = 1) { gqlWrapper.getResponse(clazzRemindMeCheckEntity, queryStringRemindMeCheck, map) }
        }
    }

    @Test
    fun `check query params`() {
        val param1 = "124"
        val map = remindMeUseCase.getRequestParams(param1)
        Assert.assertEquals(map[RemindMeUseCase.Params.ABOUT], param1)

    }

}