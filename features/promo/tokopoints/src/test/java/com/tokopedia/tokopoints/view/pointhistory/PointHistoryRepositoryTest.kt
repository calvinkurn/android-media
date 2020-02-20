package com.tokopedia.tokopoints.view.pointhistory

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
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

    var repo = mockk<GraphqlRepository>()
    var map = mockk<Map<String,String>>()

    @Before
    fun setUp() {
        repository = PointHistoryRepository(repo, map)
    }

    @Test
    fun getPointsDetail() {
        var data = mockk<GraphqlResponse>()
        coEvery { repo.getReseponse(any(),any()) } returns  data
        coEvery{map.get(any())} returns "hiosdbvoiav"
        runBlocking{
           assert(repository.getPointsDetail() == data)
        }


    }

    @Test
    fun getPointList() {
        var data = mockk<GraphqlResponse>()
        coEvery { repo.getReseponse(any(),any()) } returns  data
        coEvery{map.get(any())} returns "hiosdbvoiav"
        runBlocking {
             assert(repository.getPointList(1) == data)
        }

    }


}