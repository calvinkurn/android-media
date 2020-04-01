package com.tokopedia.salam.umrah.travel.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.*
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.salam.umrah.UmrahDispatchersProviderTest
import com.tokopedia.salam.umrah.travel.data.UmrahTravelAgentBySlugNameEntity
import com.tokopedia.salam.umrah.travel.presentation.usecase.UmrahTravelUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UmrahTravelInfoViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var umrahTravelUseCase : UmrahTravelUseCase
    private val dispatcher = UmrahDispatchersProviderTest()
    private lateinit var umrahTravelInfoViewModel: UmrahTravelInfoViewModel

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        umrahTravelInfoViewModel = UmrahTravelInfoViewModel(umrahTravelUseCase, dispatcher)
    }


    @Test
    fun getTravelSlugResult_shouldReturnData(){
        //given
        coEvery {
            umrahTravelUseCase.executeUsecase(any(),any())
        } returns Success(UmrahTravelAgentBySlugNameEntity())

        //when
        umrahTravelInfoViewModel.requestTravelData("","")

        //then
        val actual = umrahTravelInfoViewModel.travelAgentData.value
        assert(actual is Success)
    }

    @Test
    fun getTravelSlugResult_shouldReturnThrowable(){
        //given
        coEvery{
            umrahTravelUseCase.executeUsecase(any(),any())
        } returns Fail(Throwable())

        //when
        umrahTravelInfoViewModel.requestTravelData("","")

        //then
        val actual = umrahTravelInfoViewModel.travelAgentData.value
        assert(actual is Fail)
    }

}