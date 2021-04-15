package com.tokopedia.salam.umrah.travel.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.salam.umrah.common.data.UmrahTravelAgentsEntity
import com.tokopedia.salam.umrah.common.usecase.UmrahTravelAgentsUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class UmrahTravelListViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var umrahTravelAgentsUseCase : UmrahTravelAgentsUseCase
    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var UmrahTravelListViewModel: UmrahTravelListViewModel

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        UmrahTravelListViewModel = UmrahTravelListViewModel(umrahTravelAgentsUseCase, dispatcher)
    }


    @Test
    fun getTravelListResult_shouldReturnData(){
        //given
        coEvery {
            umrahTravelAgentsUseCase.executeUseCase(any(),any(),any(),any(),any())
        } returns Success(UmrahTravelAgentsEntity())

        //when
        UmrahTravelListViewModel.requestTravelAgentsData("",1,true)

        //then
        val actual = UmrahTravelListViewModel.travelAgents.value
        assert(actual is Success)
    }

    @Test
    fun getTravelListResult_shouldReturnThrowable(){
        //given
        coEvery{
            umrahTravelAgentsUseCase.executeUseCase(any(),any(),any(),any(),any())
        } returns Fail(Throwable())

        //when
        UmrahTravelListViewModel.requestTravelAgentsData("",1,true)

        //then
        val actual = UmrahTravelListViewModel.travelAgents.value
        assert(actual is Fail)
    }
}