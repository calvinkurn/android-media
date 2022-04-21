package com.tokopedia.tokopoints.view.tokopointhome

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class TokopointsHomeRepositoryTest {
    lateinit var repository: TokopointsHomeUsecase

    @Before
    fun setUp() {
        repository = TokopointsHomeUsecase("tp_section_new", "tp_gql_usersaving")
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getTokoPointDetailData() {
        runBlocking {
            val useCase = mockk<MultiRequestGraphqlUseCase>()
            repository.tpHomeUsecase = useCase
            every { useCase.clearRequest() } just Runs
            every { useCase.addRequest(any()) } just Runs
            coEvery { useCase.executeOnBackground() } returns mockk()
            repository.getTokoPointDetailData()

            coVerify (ordering = Ordering.ORDERED){
                useCase.clearRequest()
                useCase.addRequest(any())
                useCase.addRequest(any())
                useCase.executeOnBackground()
            }
        }
    }

    @Test
    fun getRewardIntroData() {
        runBlocking {
            val useCase = mockk<MultiRequestGraphqlUseCase>()
            repository.tpHomeUsecase = useCase
            every { useCase.clearRequest() } just Runs
            every { useCase.addRequest(any()) } just Runs
            coEvery { useCase.executeOnBackground() } returns mockk()
            repository.getRewardIntroData()

            coVerify (ordering = Ordering.ORDERED){
                useCase.clearRequest()
                useCase.addRequest(any())
                useCase.executeOnBackground()
            }
        }
    }

    @Test
    fun getUserSavingData() {
        runBlocking {
            val useCase = mockk<MultiRequestGraphqlUseCase>()
            repository.tpHomeUsecase = useCase
            every { useCase.clearRequest() } just Runs
            every { useCase.addRequest(any()) } just Runs
            coEvery { useCase.executeOnBackground() } returns mockk()
            repository.getUserSavingData()

            coVerify (ordering = Ordering.ORDERED){
                useCase.clearRequest()
                useCase.addRequest(any())
                useCase.executeOnBackground()
            }
        }
    }
}