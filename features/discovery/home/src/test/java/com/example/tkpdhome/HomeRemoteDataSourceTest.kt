package com.example.tkpdhome

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home.beranda.data.datasource.remote.HomeRemoteDataSource
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.user.session.UserSessionInterface
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
    private lateinit var homeRemoteDataSource: HomeRemoteDataSource

    @Before
    fun init(){
        homeRemoteDataSource = HomeRemoteDataSource(graphqlRepository)
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
            Assert.assertEquals("16206", result.dynamicHomeChannel.channels?.first()?.id)
        }
    }

    private fun getJson(path : String) : String {
        val uri = this.javaClass.classLoader.getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }
}