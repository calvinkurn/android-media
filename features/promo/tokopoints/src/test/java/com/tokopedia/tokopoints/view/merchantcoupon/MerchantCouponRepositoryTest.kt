package com.tokopedia.tokopoints.view.merchantcoupon

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlResponse
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class MerchantCouponRepositoryTest {


    lateinit var repository: MerchantCouponRepository
    val data = mockk<GraphqlResponse>()

    @Before
    fun setUp() {
        repository = MerchantCouponRepository("graphqlQuery")

    }

    @After
    fun tearDown() {
    }

    @Test
    fun getCouponData() {

        runBlocking {
            val useCase = mockk<MultiRequestGraphqlUseCase>()
            repository.mGetMerchantCouponUsecase = useCase

            every { useCase.clearRequest() } just Runs
            every { useCase.addRequest(any()) } just Runs
            coEvery { useCase.executeOnBackground() } returns mockk()
            repository.getProductData(1, "0")
            coVerify(ordering = Ordering.ORDERED) {
                useCase.clearRequest()
                useCase.addRequest(any())
                useCase.executeOnBackground()
            }
        }
    }
}