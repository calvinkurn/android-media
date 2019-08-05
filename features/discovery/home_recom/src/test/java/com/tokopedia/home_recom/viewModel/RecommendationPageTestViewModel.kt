package com.tokopedia.home_recom.viewModel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.any
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home_recom.model.datamodel.ProductInfoDataModel
import com.tokopedia.home_recom.model.entity.PrimaryProductEntity
import com.tokopedia.home_recom.viewmodel.RecommendationPageViewModel
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import rx.Observable
import rx.Subscriber

/**
 * Created by Lukas on 2019-07-04
 */
@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class RecommendationPageTestViewModel {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    @MockK
    lateinit var userSessionInterface: UserSessionInterface

    @MockK
    lateinit var getRecommendationUseCase: GetRecommendationUseCase

    @MockK
    lateinit var mockLiveDataListRecommendationWidget: MutableLiveData<List<RecommendationWidget>>

    @MockK
    lateinit var mockLiveDataProductInfo: MutableLiveData<ProductInfoDataModel>

    private lateinit var viewModel: RecommendationPageViewModel

    private val productsId = "316960043"
    private val dispatcher = Dispatchers.Unconfined
    private val gson = Gson()
    private val successJson = "primary_product_success_response.json"

    @Before
    fun setup(){
        MockKAnnotations.init(this)
        viewModel = RecommendationPageViewModel(graphqlRepository, userSessionInterface, getRecommendationUseCase, mockk(), mockk(), mockk(), dispatcher, "")
    }

    @Test
    fun loadSuccessGetPrimaryProduct(){
        val spy = spyk(viewModel)
        //given
        every { mockLiveDataProductInfo.value } returns mockk()
        every { spy.productInfoDataModel } returns mockLiveDataProductInfo
        every { spy.getPrimaryProduct(productsId) } answers {}

        //when
        spy.getPrimaryProduct(productsId)

        //then
        verify { spy.getPrimaryProduct(productsId) }
        assertNotNull(spy.productInfoDataModel.value)
    }

    @Test
    fun loadErrorGetPrimaryProduct(){
        val spy = spyk(viewModel)
        //given
        every { mockLiveDataProductInfo.value } returns null
        every { spy.productInfoDataModel } returns mockLiveDataProductInfo
        every { spy.getPrimaryProduct(productsId) } answers {}

        //when
        spy.getPrimaryProduct(productsId)

        //then
        verify { spy.getPrimaryProduct(productsId) }
        assertNull(spy.productInfoDataModel.value)
    }

    @Test
    fun loadSuccessGetRecommendationWidget(){
        val spy = spyk(viewModel)
        val mockList = mockk<List<String>>()

        //given
        every { mockLiveDataListRecommendationWidget.value } returns listOf()
        every { spy.recommendationListModel } returns mockLiveDataListRecommendationWidget
        every { spy.getRecommendationList(mockList, null) } answers {}
        spy.getRecommendationList(mockList, null)

        //then
        verify { spy.getRecommendationList(mockList, null) }
        assertNotNull(spy.recommendationListModel.value)
    }

    @Test
    fun loadErrorGetRecommendationWidget(){
        val spy = spyk(viewModel)
        val mockList = mockk<List<String>>()

        //given
        every { mockLiveDataListRecommendationWidget.value } returns null
        every { spy.recommendationListModel } returns mockLiveDataListRecommendationWidget
        every { spy.getRecommendationList(mockList, null) } answers {}
        spy.getRecommendationList(mockList, null)

        //then
        verify { spy.getRecommendationList(mockList, null) }
        assertNull(spy.recommendationListModel.value)
    }

    @Test
    fun checkIsNotLogin(){
        val spy = spyk(viewModel)
        every{ spy.isLoggedIn() } returns false
        Assert.assertFalse(spy.isLoggedIn())
    }

    @Test
    fun checkIsLogin(){
        val spy = spyk(viewModel)
        every{ spy.isLoggedIn() } returns true
        Assert.assertTrue(spy.isLoggedIn())
    }

    @Test
    fun testSuccessSuspendFunction() = runBlocking{
        val spy = spyk(viewModel)
        val json = this.javaClass.classLoader?.getResourceAsStream(successJson)?.readBytes()?.toString(Charsets.UTF_8)
        val response = gson.fromJson(json, PrimaryProductEntity::class.java)
        val gqlResponseSuccess = GraphqlResponse(
                mapOf(PrimaryProductEntity::class.java to response),
                mapOf(PrimaryProductEntity::class.java to listOf()), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns  gqlResponseSuccess
        spy.getPrimaryProduct(productsId)
        coVerify{ spy.getPrimaryProduct(productsId) }
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
            Assert.assertEquals(it, errorMessage)
        }
    }
}