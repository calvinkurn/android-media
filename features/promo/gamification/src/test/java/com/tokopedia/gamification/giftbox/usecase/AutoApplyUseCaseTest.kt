package com.tokopedia.gamification.giftbox.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gamification.giftbox.data.entities.AutoApplyResponse
import com.tokopedia.gamification.giftbox.domain.AutoApplyUseCase
import com.tokopedia.gamification.pdp.data.GqlUseCaseWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class AutoApplyUseCaseTest {
    lateinit var autoApplyUseCase: AutoApplyUseCase
    val queryString = ""

    @MockK
    lateinit var gqlWrapper: GqlUseCaseWrapper

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        autoApplyUseCase = AutoApplyUseCase(queryString, gqlWrapper)
    }

    @Test
    fun `check function invokation of Gql wrapper`() {
        runBlockingTest {
            val map = HashMap<String, Any>()
            coEvery { gqlWrapper.getResponse(AutoApplyResponse::class.java, queryString, map) } returns mockk()

            autoApplyUseCase.getResponse(map)
            coVerify(exactly = 1) { gqlWrapper.getResponse(AutoApplyResponse::class.java, queryString, map) }
        }
    }

    @Test
    fun `check query params`() {
        val code = "124"
        val map = autoApplyUseCase.getQueryParams(code)
        Assert.assertEquals(map[AutoApplyUseCase.AutoApplyParams.CODE], code)

    }
}