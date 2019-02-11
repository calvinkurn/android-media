package com.tokopedia.topads.credit.history.view.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.topads.credit.history.data.model.TopAdsCreditHistory
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.ExperimentalCoroutinesApi
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.*

import java.util.*

class TopAdsCreditHistoryViewModelTest {
    lateinit var viewModel: TopAdsCreditHistoryViewModel

    @Mock
    lateinit var graphqlRepository: GraphqlRepository

    @Mock
    lateinit var userSessionInterface: UserSessionInterface

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var resultsView: Observer<com.tokopedia.usecase.coroutines.Result<TopAdsCreditHistory>>

    val gson = Gson()

    @Captor
    lateinit var argCaptor: ArgumentCaptor<com.tokopedia.usecase.coroutines.Result<TopAdsCreditHistory>>

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = TopAdsCreditHistoryViewModel(graphqlRepository, userSessionInterface, Dispatchers.Unconfined)

        Mockito.`when`(userSessionInterface.userId).thenReturn("12345")
        Mockito.`when`(userSessionInterface.shopId).thenReturn("123456")
    }

    @Test
    fun `get credit history success`() = runBlocking{
        //given
        val creditHistoryResponse = gson.fromJson(CreditHistoryMockResponse.success,
                TopAdsCreditHistory.CreditsResponse::class.java)
        val gqlResponseSucess = GraphqlResponse(
                mapOf(TopAdsCreditHistory.CreditsResponse::class.java to creditHistoryResponse),
                mapOf(TopAdsCreditHistory.CreditsResponse::class.java to listOf()), false)

        val graphqlRequest = GraphqlRequest(Mockito.any(String::class.java),
                Mockito.eq(TopAdsCreditHistory.CreditsResponse::class.java), Mockito.anyMapOf(String::class.java,
                Any::class.java))

        Mockito.`when`(graphqlRepository.getReseponse(listOf(graphqlRequest)))
                .thenReturn(gqlResponseSucess)
        viewModel.creditsHistory.observeForever(resultsView)
        //when
        viewModel.getCreditHistory(CreditHistoryMockResponse.gqlQuery, Date(), Date())

        //then
        Mockito.verify(resultsView, Mockito.times(1)).onChanged(argCaptor.capture())
        val values = argCaptor.allValues
        print(values.get(0).javaClass)
        Assert.assertEquals(1, values.size)
    }

    @Test
    fun `get credit history fail`() = runBlocking{
        //given
        val creditHistoryResponse = gson.fromJson(CreditHistoryMockResponse.fail,
                TopAdsCreditHistory.CreditsResponse::class.java)
        val gqlResponseSucess = GraphqlResponse(
                mapOf(TopAdsCreditHistory.CreditsResponse::class.java to creditHistoryResponse),
                mapOf(TopAdsCreditHistory.CreditsResponse::class.java to listOf()), false)

        val graphqlRequest = GraphqlRequest(Mockito.any(String::class.java),
                Mockito.eq(TopAdsCreditHistory.CreditsResponse::class.java), Mockito.anyMapOf(String::class.java,
                Any::class.java))

        Mockito.`when`(graphqlRepository.getReseponse(listOf(graphqlRequest)))
                .thenReturn(gqlResponseSucess)

        //when
        viewModel.getCreditHistory(CreditHistoryMockResponse.gqlQuery, Date(), Date())
        viewModel.creditsHistory.observeForever(resultsView)
        //then
        Mockito.verify(resultsView, Mockito.times(1)).onChanged(argCaptor.capture())
        val values = argCaptor.allValues
        Assert.assertEquals(1, values.size)
    }
}