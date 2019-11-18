package com.example.home

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.v2.home.data.datasource.remote.HomeRemoteDataSource
import com.tokopedia.v2.home.model.pojo.home.HomeData
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.File

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class HomeRemoteDataSourceTest {
    val graphqlRepository = mockk<GraphqlRepository>()
    val userSessionInterface = mockk<UserSessionInterface>()
    val homeQuery = ""
    private lateinit var homeRemoteDataSource: HomeRemoteDataSource

    @Before
    fun init(){
        homeRemoteDataSource = HomeRemoteDataSource(graphqlRepository, userSessionInterface, homeQuery)
        every { userSessionInterface.isLoggedIn } returns true
        every { userSessionInterface.userId } returns "-1"
    }

    @Test
    fun `get home data`() {
        val homeData = Gson().fromJson<HomeData>(getJson("home.json"), HomeData::class.java)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(mapOf(
                HomeData::class.java to homeData
        ), mapOf(), false)
        runBlocking {
            val graphqlResponse = homeRemoteDataSource.getHomeData()
            val result = graphqlResponse.getData<HomeData>(HomeData::class.java)
            Assert.assertEquals(null, graphqlResponse.getError(HomeData::class.java))
            Assert.assertEquals(0, result.id)
            Assert.assertEquals("16206", result.dynamicHomeChannel?.channels?.first()?.id)
        }
    }

    private fun getJson(path : String) : String {
        val uri = this.javaClass.classLoader.getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }
}