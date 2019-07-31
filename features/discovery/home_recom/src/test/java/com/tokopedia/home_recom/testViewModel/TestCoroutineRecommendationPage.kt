package com.tokopedia.home_recom.testViewModel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.any
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home_recom.model.entity.PrimaryProductEntity
import com.tokopedia.home_recom.util.PrimaryProductResponse
import com.tokopedia.home_recom.viewmodel.RecommendationPageViewModel
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import rx.Observable
import rx.Subscriber

/**
 * Created by Lukas on 2019-07-11
 */
@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class TestCoroutineRecommendationPage{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    @MockK
    lateinit var userSessionInterface: UserSessionInterface

    @MockK
    lateinit var getRecommendationUseCase: GetRecommendationUseCase

    lateinit var viewModel: RecommendationPageViewModel

    val dispatcher = Dispatchers.Unconfined
    val gson = Gson()

    @Before
    fun setup(){
        MockKAnnotations.init(this)
        viewModel = RecommendationPageViewModel(graphqlRepository, userSessionInterface, getRecommendationUseCase, dispatcher, "")
    }

    @Test
    fun testSuccessSuspendFunction() = runBlocking{
        val response = gson.fromJson(PrimaryProductResponse.success, PrimaryProductEntity::class.java)
        val gqlResponseSuccess = GraphqlResponse(
                mapOf(PrimaryProductEntity::class.java to response),
                mapOf(PrimaryProductEntity::class.java to listOf()), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns  gqlResponseSuccess
        viewModel.getPrimaryProduct("133")
        coVerify { graphqlRepository.getReseponse(any(), any()) }
    }

    @Test
    fun testSuccessGetRecommendationList(){
        every{ getRecommendationUseCase.createObservable(any()) } returns Observable.just(mockk())
        every{ getRecommendationUseCase.getRecomParams(any(),any(),any(),any()) } returns mockk()
        every { getRecommendationUseCase.execute(any(), any()) }.answers {
            val subscriber = secondArg<Subscriber<List<RecommendationWidget>>>()
            subscriber.onNext(listOf())
        }
        val productIds = listOf("111")
        viewModel.getRecommendationList(productIds, any())
        assertNotNull(viewModel.recommendationListModel.value)
    }

    @Test
    fun testErrorGetRecommendationList(){
        val errorMessage = "ERROR"
        every{ getRecommendationUseCase.createObservable(any()) } returns Observable.just(mockk())
        every{ getRecommendationUseCase.getRecomParams(any(),any(),any(),any()) } returns mockk()
        every { getRecommendationUseCase.execute(any(), any()) }.answers {
            val subscriber = secondArg<Subscriber<List<RecommendationWidget>>>()
            subscriber.onError(Throwable(errorMessage))
        }
        val productIds = listOf("111")
        viewModel.getRecommendationList(productIds){
            assertEquals(it, errorMessage)
        }
    }
}