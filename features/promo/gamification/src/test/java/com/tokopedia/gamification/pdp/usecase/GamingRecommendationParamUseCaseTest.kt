package com.tokopedia.gamification.pdp.usecase

import com.tokopedia.gamification.pdp.data.GamingRecommendationParamResponse
import com.tokopedia.gamification.pdp.data.GqlUseCaseWrapper
import com.tokopedia.gamification.pdp.domain.usecase.GamingRecommendationParamUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GamingRecommendationParamUseCaseTest {

    lateinit var usecase: GamingRecommendationParamUseCase
    val queryString = ""
    @MockK
    lateinit var gqlWrapper: GqlUseCaseWrapper

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        usecase = GamingRecommendationParamUseCase(queryString, gqlWrapper)
    }

    @Test
    fun `check function invokation of Gql wrapper`() {
        runBlockingTest {
            val map = HashMap<String, Any>()
            coEvery { gqlWrapper.getResponse(GamingRecommendationParamResponse::class.java, queryString, map) } returns mockk()
            usecase.getResponse(map)
            coVerify(exactly = 1) { gqlWrapper.getResponse(GamingRecommendationParamResponse::class.java, queryString, map) }
        }
    }

    @Test
    fun `check query params`() {
        val pageName = "Hello"
        val map = usecase.getRequestParams(pageName)
        Assert.assertEquals(map[GamingRecommendationParamUseCase.Params.PAGE_NAME], pageName)

    }
}