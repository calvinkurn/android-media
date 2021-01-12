package com.tokopedia.topads.credit.history.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.credit.history.data.model.TopAdsCreditHistory
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsAutoTopUpUSeCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.*
import java.lang.reflect.Type

import java.util.*

class TopAdsCreditHistoryViewModelTest {
    lateinit var viewModel: TopAdsCreditHistoryViewModel

    @Mock
    lateinit var graphqlRepository: GraphqlRepository

    @Mock
    lateinit var userSessionInterface: UserSessionInterface

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private var autoTopUpUSeCase: TopAdsAutoTopUpUSeCase = mockk(relaxed = true)
    private var topAdsGetShopDepositUseCase: TopAdsGetDepositUseCase = mockk(relaxed = true)

    @Mock
    lateinit var resultsView: Observer<com.tokopedia.usecase.coroutines.Result<TopAdsCreditHistory>>

    val gson = Gson()

    @Captor
    lateinit var argCaptor: ArgumentCaptor<com.tokopedia.usecase.coroutines.Result<TopAdsCreditHistory>>

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = TopAdsCreditHistoryViewModel(graphqlRepository, userSessionInterface, autoTopUpUSeCase, topAdsGetShopDepositUseCase, Dispatchers.Unconfined)

        Mockito.`when`(userSessionInterface.userId).thenReturn("12345")
        Mockito.`when`(userSessionInterface.shopId).thenReturn("123456")
    }

    @Test
    fun `get credit history success`() = runBlocking{
        //given
        val creditHistoryResponse = gson.fromJson(CreditHistoryMockResponse.success,
                TopAdsCreditHistory.CreditsResponse::class.java)
        val gqlResponseSucess = GraphqlResponse(
                mapOf(TopAdsCreditHistory.CreditsResponse::class.java to creditHistoryResponse) as MutableMap<Type, Any>,
                HashMap<Type, List<GraphqlError>>(), false)

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
                mapOf(TopAdsCreditHistory.CreditsResponse::class.java to creditHistoryResponse) as MutableMap<Type, Any>,
                HashMap<Type, List<GraphqlError>>(), false)

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
        Assert.assertEquals(1, values.size)
    }

    @Test
    fun `auto topup status pass`() {
        viewModel.getAutoTopUpStatus("")
        verify {
            autoTopUpUSeCase.execute(any(), any())
        }
    }

    @Test
    fun `test result in getTopAdsDeposit`() {
        viewModel.getShopDeposit()
        verify {
            topAdsGetShopDepositUseCase.execute(any(), any())
        }
    }
}