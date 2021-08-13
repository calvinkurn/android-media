package com.tokopedia.promotionstarget.usecase

import com.tokopedia.promotionstarget.data.AutoApplyParams
import com.tokopedia.promotionstarget.data.autoApply.AutoApplyResponse
import com.tokopedia.promotionstarget.data.gql.GqlUseCaseWrapper
import com.tokopedia.promotionstarget.domain.usecase.AutoApplyUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.spekframework.spek2.Spek

@ExperimentalCoroutinesApi
class AutoApplyUseCaseSpekTest : Spek({

    lateinit var autoApplyUseCase: AutoApplyUseCase
    val queryString = ""
    val gqlWrapper: GqlUseCaseWrapper = mockk()
    val dispatcher = TestCoroutineDispatcher()

    beforeEachTest {
        autoApplyUseCase = AutoApplyUseCase(queryString, gqlWrapper)
    }

    group("function invocation"){
        test("check function invokation of Gql wrapper") {
            runBlockingTest {
                val map = HashMap<String, Any>()
                coEvery { gqlWrapper.getResponse(AutoApplyResponse::class.java, queryString, map) } returns mockk()

                autoApplyUseCase.getResponse(map)
                coVerify(exactly = 1) { gqlWrapper.getResponse(AutoApplyResponse::class.java, queryString, map) }
            }
        }

        test("check query params") {
            val code = "124"
            val map = autoApplyUseCase.getQueryParams(code)
            Assert.assertEquals(map[AutoApplyParams.CODE], code)

        }
    }

    afterGroup {
        dispatcher.cleanupTestCoroutines()
    }
})