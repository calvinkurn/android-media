package com.tokopedia.promotionstarget

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.promotionstarget.data.AutoApplyParams
import com.tokopedia.promotionstarget.data.autoApply.AutoApplyResponse
import com.tokopedia.promotionstarget.data.gql.GqlUseCaseWrapper
import com.tokopedia.promotionstarget.domain.usecase.AutoApplyUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.rules.TestRule

class AutoApplyUseCaseTest {

    lateinit var autoApplyUseCase: AutoApplyUseCase
    val queryString = ""
    @MockK
    lateinit var gqlWrapper: GqlUseCaseWrapper
    val dispatcher = TestCoroutineDispatcher()
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
        Assert.assertEquals(map[AutoApplyParams.CODE], code)

    }

    @After
    fun cleanup() {
        dispatcher.cleanupTestCoroutines()
    }
}