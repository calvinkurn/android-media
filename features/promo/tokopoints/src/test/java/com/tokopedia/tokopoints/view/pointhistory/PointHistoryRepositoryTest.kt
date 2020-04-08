package com.tokopedia.tokopoints.view.pointhistory

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class PointHistoryRepositoryTest {


    lateinit var repository: PointHistoryRepository

    var repo = mockk<GraphqlRepository>()
    var map = mockk<Map<String,String>>()

    @Before
    fun setUp() {
        repository = PointHistoryRepository(repo, map , "tp_curent_point")
    }

    @Test
    fun getPointsDetail() {
        var data = mockk<GraphqlResponse>()
        coEvery { repo.getReseponse(any(),any()) } returns  data
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