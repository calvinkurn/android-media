package com.tokopedia.home_recom.testViewModel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.tokopedia.home_recom.model.datamodel.ProductInfoDataModel
import com.tokopedia.home_recom.viewmodel.RecommendationPageViewModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
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
    lateinit var mockViewModel: RecommendationPageViewModel

    @Mock
    lateinit var mockLiveDataListRecommendationWidget: MutableLiveData<List<RecommendationWidget>>

    @Mock
    lateinit var mockLiveDataProductInfo: MutableLiveData<ProductInfoDataModel>

    @Test
    fun loadSuccessGetPrimaryProduct(){
        val productsId = "[]"
        `when`(mockLiveDataProductInfo.value).thenReturn(mock(ProductInfoDataModel::class.java))
        `when`(mockViewModel.productInfoDataModel).thenReturn(mockLiveDataProductInfo)

        doNothing().`when`(mockViewModel).getPrimaryProduct(productsId, context)
        mockViewModel.getPrimaryProduct(productsId, context)

        verify(mockViewModel, times(1)).getPrimaryProduct(productsId, context)
        assertNotNull(mockViewModel.productInfoDataModel.value)
    }

    @Test
    fun loadErrorGetPrimaryProduct(){
        val productsId = "[]"
        `when`(mockLiveDataProductInfo.value).thenReturn(null)
        `when`(mockViewModel.productInfoDataModel).thenReturn(mockLiveDataProductInfo)

        doNothing().`when`(mockViewModel).getPrimaryProduct(productsId, context)
        mockViewModel.getPrimaryProduct(productsId, context)

        verify(mockViewModel, times(1)).getPrimaryProduct(productsId, context)
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