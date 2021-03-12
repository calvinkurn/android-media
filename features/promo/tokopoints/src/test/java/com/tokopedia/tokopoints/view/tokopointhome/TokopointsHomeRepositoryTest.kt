package com.tokopedia.tokopoints.view.tokopointhome

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class TokopointsHomeRepositoryTest {
    lateinit var repository: TokopointsHomeRepository

    @Before
    fun setUp() {
        repository = TokopointsHomeRepository("tp_section_new",
                "tp_homepage_section",
                "tp_gql_reward",
                "tp_gql_usersaving")
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getTokoPointDetailData() {
        runBlocking {
            val useCase = mockk<MultiRequestGraphqlUseCase>()
            repository.mGetTokoPointDetailUseCase = useCase
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
            repository.mGetRewardIntoUseCase = useCase
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
            repository.mGetUserSavingUsecase = useCase
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