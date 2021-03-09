package com.tokopedia.tokopoints.view.sendgift

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.tokopoints.view.model.RedeemCouponBaseEntity
import com.tokopedia.tokopoints.view.model.ValidateCouponBaseEntity
import com.tokopedia.tokopoints.view.util.CommonConstant
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class SendGiftRespositoryTest {
    lateinit var repository: SendGiftRespository
    val map = mockk<Map<String, String>>()

    @Before
    fun setUp() {
        repository = SendGiftRespository(map)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun sendGift() {
        runBlocking {
            val useCase = mockk<MultiRequestGraphqlUseCase>()
            repository.mRedeemCouponUseCase = useCase
            every { map[CommonConstant.GQLQuery.TP_GQL_TOKOPOINT_REDEEM_COUPON] } returns "jhnvjdsnv"
            every { useCase.clearRequest() } just Runs
            every { useCase.addRequest(any()) } just Runs
            coEvery { useCase.executeOnBackground() } returns mockk{
                every { getData<RedeemCouponBaseEntity>(RedeemCouponBaseEntity::class.java) } returns mockk()
                every { getError(RedeemCouponBaseEntity::class.java) } returns null
            }
            repository.sendGift(1, "abcd@tokopedia.com","jdcnj")

            coVerify(ordering = Ordering.ORDERED) {
                useCase.clearRequest()
                useCase.addRequest(any())
                useCase.executeOnBackground()
            }
        }
    }

    @Test
    fun preValidateGift() {
        runBlocking {
            val useCase = mockk<MultiRequestGraphqlUseCase>()
            repository.mStartSendGift = useCase
            every { map[CommonConstant.GQLQuery.TP_GQL_TOKOPOINT_VALIDATE_REDEEM] } returns "jhnvjdsnv"
            every { useCase.clearRequest() } just Runs
            every { useCase.addRequest(any()) } just Runs
            coEvery { useCase.executeOnBackground() } returns mockk{
                every { getData<ValidateCouponBaseEntity>(ValidateCouponBaseEntity::class.java) } returns mockk()
                every { getError(ValidateCouponBaseEntity::class.java) } returns null
            }
            repository.preValidateGift(1,"abcd@tokopedia.com")

            coVerify(ordering = Ordering.ORDERED) {
                useCase.clearRequest()
                useCase.addRequest(any())
                useCase.executeOnBackground()
            }
        }
    }
}