package com.tokopedia.tokopoints.view.couponlisting

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.tokopoints.view.util.CommonConstant
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test


class StackedCouponRepositoryTest {


    private val mgraphRepository = mockk<GraphqlRepository>()
    private val map  = mockk<Map<String,String>>()
    private val data = mockk<GraphqlResponse>()

    lateinit var repository: StackedCouponRepository

    @Before
    fun setUp() {
        repository = StackedCouponRepository(mgraphRepository,map)
        coEvery{ mgraphRepository.response(any(), any()) } returns data
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `getFilter`() {
        runBlocking {
            every { map[CommonConstant.GQLQuery.TP_GQL_COUPON_FILTER] } returns "dasdv"
            val response = repository.getFilter("slug")

            coVerify(exactly = 1) { mgraphRepository.response(any(),any()) }
            assert(data == response)
        }
    }

    @Test
    fun `getCouponList`() {
        runBlocking {
            every { map[CommonConstant.GQLQuery.TP_GQL_COUPON_LISTING_STACK] } returns "dasdv"
            val response = repository.getCouponList(1,1)

            coVerify(exactly = 1) { mgraphRepository.response(any(),any()) }
            assert(data == response)
        }
    }

    @Test
    fun `getInStackedCouponList`() {
        runBlocking {
            every { map[CommonConstant.GQLQuery.TP_GQL_COUPON_IN_STACK] } returns "dasdv"
            val response = repository.getInStackedCouponList("slug")

            coVerify(exactly = 1) { mgraphRepository.response(any(),any()) }
            assert(data == response)
        }

    }
}
