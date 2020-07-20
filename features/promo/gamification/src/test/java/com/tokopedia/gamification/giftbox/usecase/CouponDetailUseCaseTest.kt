package com.tokopedia.gamification.giftbox.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gamification.giftbox.data.entities.GetCouponDetail
import com.tokopedia.gamification.giftbox.domain.CouponDetailUseCase
import com.tokopedia.gamification.pdp.data.GqlUseCaseWrapper
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class CouponDetailUseCaseTest {

    lateinit var couponDetailUseCase: CouponDetailUseCase
    val queryString = ""

    @MockK
    lateinit var gqlWrapper: GqlUseCaseWrapper

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        couponDetailUseCase = spyk(CouponDetailUseCase(queryString, gqlWrapper))
    }

    @Test
    fun `check function invokation of Gql wrapper`() {
        runBlockingTest {
            val couponIdList = emptyList<String>()
            val map: Map<*, *> = HashMap<Any,Any>()
            coEvery { gqlWrapper.getResponse(Map::class.java, any(), emptyMap()) } returns map
            couponDetailUseCase.getResponse(couponIdList)
            coVerify { gqlWrapper.getResponse(Map::class.java, any(), emptyMap()) }
        }
    }
}