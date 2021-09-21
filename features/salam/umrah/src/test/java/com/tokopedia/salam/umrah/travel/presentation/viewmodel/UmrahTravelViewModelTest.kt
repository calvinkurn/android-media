package com.tokopedia.salam.umrah.travel.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.*
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
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
class UmrahTravelViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var umrahTravelUseCase : UmrahTravelUseCase
    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var umrahTravelViewModel: UmrahTravelViewModel

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        umrahTravelViewModel = UmrahTravelViewModel(umrahTravelUseCase, dispatcher)
    }

    @Test
    fun getTravelSlugResult_shouldReturnData(){
        //given
        coEvery {
            umrahTravelUseCase.executeUsecase(any(),any())
        } returns Success(UmrahTravelAgentBySlugNameEntity())

        //when
        umrahTravelViewModel.requestTravelData("","")

        //then
        val actual = umrahTravelViewModel.travelAgentData.value
        assert(actual is Success)
    }

    @Test
    fun getTravelSlugResult_shouldReturnThrowable(){
        //given
        coEvery{
            umrahTravelUseCase.executeUsecase(any(),any())
        } returns Fail(Throwable())

        //when
        umrahTravelViewModel.requestTravelData("","")

        //then
        val actual = umrahTravelViewModel.travelAgentData.value
        assert(actual is Fail)
    }
}