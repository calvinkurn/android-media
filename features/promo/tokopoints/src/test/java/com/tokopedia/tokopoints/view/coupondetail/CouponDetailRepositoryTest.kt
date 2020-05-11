package com.tokopedia.tokopoints.view.coupondetail

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.tokopoints.view.util.CommonConstant
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class CouponDetailRepositoryTest {


    lateinit var repository: CouponDetailRepository

    val map = mockk<Map<String,String>>()
    val data = mockk<GraphqlResponse>()
    val graphqlRepository = mockk<GraphqlRepository>{
        coEvery{getReseponse(any(),any())} returns data
    }
    @Before
    fun setUp() {
        repository = CouponDetailRepository(graphqlRepository, map)

    }

    @After
    fun tearDown() {
    }

    @Test
    fun redeemCoupon() {
        val promo = "mndflvs"
        coEvery{map[CommonConstant.GQLQuery.TP_GQL_TOKOPOINT_APPLY_COUPON] }  returns "bdkjasv"
        runBlocking {
            assert(repository.redeemCoupon(promo) == data)
        }
    }

    @Test
    fun getCouponDetail() {
        val couponcode = "sdbvlka"
        coEvery{map[CommonConstant.GQLQuery.TP_GQL_COUPON_DETAIL] }  returns "bdkjasv"
        runBlocking {
            assert(repository.getCouponDetail(couponcode) == data)
        }
    }

    @Test
    fun reFetchRealCode() {
        val couponcode = "sdbvlka"
        coEvery{map[CommonConstant.GQLQuery.TP_GQL_REFETCH_REAL_CODE] }  returns "bdkjasv"
        runBlocking {
            assert(repository.reFetchRealCode(couponcode) == data)
        }
    }

    @Test
    fun swipeMyCoupon() {
        val partnercode = "kbsdfl"
        val pin = "vsbdv"
        coEvery{map[CommonConstant.GQLQuery.TP_GQL_SWIPE_COUPON] }  returns "bdkjasv"
        runBlocking {
            assert(repository.swipeMyCoupon(partnercode,pin) == data)
        }
    }
}