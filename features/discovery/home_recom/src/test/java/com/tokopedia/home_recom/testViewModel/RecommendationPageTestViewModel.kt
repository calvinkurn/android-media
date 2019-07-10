package com.tokopedia.home_recom.testViewModel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home_recom.model.datamodel.ProductInfoDataModel
import com.tokopedia.home_recom.model.entity.PrimaryProductEntity
import com.tokopedia.home_recom.util.PrimaryProductResponse
import com.tokopedia.home_recom.viewmodel.RecommendationPageViewModel
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

/**
 * Created by Lukas on 2019-07-04
 */
@RunWith(PowerMockRunner::class)
@PrepareForTest(RecommendationPageViewModel::class)
class RecommendationPageTestViewModel {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var graphqlRepository: GraphqlRepository

    @Mock
    lateinit var mockViewModel: RecommendationPageViewModel

    @Mock
    lateinit var userSessionInterface: UserSessionInterface

    @Mock
    lateinit var getRecommendationUseCase: GetRecommendationUseCase

    @Mock
    lateinit var mockLiveDataListRecommendationWidget: MutableLiveData<List<RecommendationWidget>>

    @Mock
    lateinit var mockLiveDataProductInfo: MutableLiveData<ProductInfoDataModel>

    private val productsId = "316960043"

    private val gson = Gson()

    @ExperimentalCoroutinesApi
    @Before
    fun setup(){
        mockViewModel = RecommendationPageViewModel(graphqlRepository, userSessionInterface, getRecommendationUseCase, Dispatchers.Unconfined, "")
    }

    @Test
    fun loadSuccessGetPrimaryProduct() {

        val response = gson.fromJson(PrimaryProductResponse.success, PrimaryProductEntity::class.java)

        val gqlResponseSuccess = GraphqlResponse(
                mapOf(PrimaryProductEntity::class.java to response),
                mapOf(PrimaryProductEntity::class.java to listOf()), false)

        val graphqlRequest = GraphqlRequest(Mockito.any(String::class.java),
                    Mockito.eq(PrimaryProductEntity::class.java), Mockito.anyMapOf(String::class.java, Any::class.java)
                )
        runBlocking {
            `when`(graphqlRepository.getReseponse(listOf(graphqlRequest))).thenReturn(
                    gqlResponseSuccess
            )
            mockViewModel.getPrimaryProduct(productsId)
        }


        assertNotNull(mockViewModel.productInfoDataModel.value)
    }

    @Test
    fun loadErrorGetPrimaryProduct(){

        `when`(mockLiveDataProductInfo.value).thenReturn(null)
        `when`(mockViewModel.productInfoDataModel).thenReturn(mockLiveDataProductInfo)

        doNothing().`when`(mockViewModel).getPrimaryProduct(productsId)
        mockViewModel.getPrimaryProduct(productsId)

        verify(mockViewModel, times(1)).getPrimaryProduct(productsId)
        assertNull(mockViewModel.productInfoDataModel.value)
    }

    @Test
    fun loadSuccessGetRecommendationWidget(){
        val mockList = mock(List::class.java) as List<String>
        `when`(mockLiveDataListRecommendationWidget.value).thenReturn(listOf())
        `when`(mockViewModel.recommendationListModel).thenReturn(mockLiveDataListRecommendationWidget)
        doNothing().`when`(mockViewModel).getRecommendationList(mockList, null)
        mockViewModel.getRecommendationList(mockList, null)
        verify(mockViewModel, times(1)).getRecommendationList(mockList, null)
        assertNotNull(mockViewModel.recommendationListModel.value)
    }

    @Test
    fun loadErrorGetRecommendationWidget(){
        val mockList = mock(List::class.java) as List<String>
        `when`(mockLiveDataListRecommendationWidget.value).thenReturn(null)
        `when`(mockViewModel.recommendationListModel).thenReturn(mockLiveDataListRecommendationWidget)
        doNothing().`when`(mockViewModel).getRecommendationList(mockList, null)
        mockViewModel.getRecommendationList(mockList, null)
        verify(mockViewModel, times(1)).getRecommendationList(mockList, null)
        assertNull(mockViewModel.recommendationListModel.value)
    }

    @Test
    fun checkIsNotLogin(){
        `when`(mockViewModel.isLoggedIn()).thenReturn(false)
        Assert.assertFalse(mockViewModel.isLoggedIn())
    }

    @Test
    fun checkIsLogin(){
        `when`(mockViewModel.isLoggedIn()).thenReturn(true)
        Assert.assertTrue(mockViewModel.isLoggedIn())
    }
}