package com.tokopedia.tokopoints.view.pointhistory

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import rx.Subscriber

class PointHistoryRepositoryTest {


    lateinit var repository: PointHistoryRepository

    var homeUseCase = mockk<GraphqlUseCase>()
    var pointHistory = mockk<GraphqlUseCase>()
    var map = mockk<Map<String,String>>()
    var suberscriber = mockk<Subscriber<GraphqlResponse>>()

    @Before
    fun setUp() {
        repository = PointHistoryRepository(pointHistory, homeUseCase,map)
    }

    @Test
    fun getPointsDetail() {
        coEvery{pointHistory.clearRequest()} just Runs
        coEvery{pointHistory.addRequest(any())} just Runs
        coEvery{pointHistory.execute(suberscriber)} just Runs
        coEvery{map.get(any())} returns "hiosdbvoiav"


        runBlocking{
            repository.getPointsDetail(suberscriber)
        }
        coVerify {
            pointHistory.clearRequest()
            pointHistory.addRequest(any())
            pointHistory.execute(suberscriber)
        }

    }

    @Test
    fun getPointList() {
        coEvery{homeUseCase.clearRequest()} just Runs
        coEvery{homeUseCase.addRequest(any())} just Runs
        coEvery{homeUseCase.execute(suberscriber)} just Runs
        coEvery{map.get(any())} returns "hiosdbvoiav"

        runBlocking {
            repository.getPointList(1,suberscriber)
        }

        coVerify {
            homeUseCase.clearRequest()
            homeUseCase.addRequest(any())
            homeUseCase.execute(suberscriber)
        }
    }

    @Test
    fun onCleared() {
        every { pointHistory.unsubscribe() } just Runs
        every { homeUseCase.unsubscribe() } just Runs
        repository.onCleared()

        coVerify {
            homeUseCase.unsubscribe()
            pointHistory.unsubscribe()
        }
    }
}