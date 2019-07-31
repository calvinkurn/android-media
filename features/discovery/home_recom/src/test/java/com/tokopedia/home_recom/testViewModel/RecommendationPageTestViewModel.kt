package com.tokopedia.home_recom.testViewModel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.verify
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
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
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
    lateinit var mockViewModel: RecommendationPageViewModel

    @Mock
    lateinit var mockLiveDataListRecommendationWidget: MutableLiveData<List<RecommendationWidget>>

    @Mock
    lateinit var mockLiveDataProductInfo: MutableLiveData<ProductInfoDataModel>

    private val productsId = "316960043"

    @Test
    fun loadSuccessGetPrimaryProduct(){
        `when`(mockLiveDataProductInfo.value).thenReturn(mock(ProductInfoDataModel::class.java))
        `when`(mockViewModel.productInfoDataModel).thenReturn(mockLiveDataProductInfo)

        Mockito.doNothing().`when`(mockViewModel).getPrimaryProduct(productsId)
        mockViewModel.getPrimaryProduct(productsId)

        verify(mockViewModel, Mockito.times(1)).getPrimaryProduct(productsId)
        assertNotNull(mockViewModel.productInfoDataModel.value)
    }

    @Test
    fun loadErrorGetPrimaryProduct(){
        `when`(mockLiveDataProductInfo.value).thenReturn(null)
        `when`(mockViewModel.productInfoDataModel).thenReturn(mockLiveDataProductInfo)

        Mockito.doNothing().`when`(mockViewModel).getPrimaryProduct(productsId)
        mockViewModel.getPrimaryProduct(productsId)

        verify(mockViewModel, Mockito.times(1)).getPrimaryProduct(productsId)
        assertNull(mockViewModel.productInfoDataModel.value)
    }

    @Test
    fun loadSuccessGetRecommendationWidget(){
        val mockList = mock(List::class.java) as List<String>
        `when`(mockLiveDataListRecommendationWidget.value).thenReturn(listOf())
        `when`(mockViewModel.recommendationListModel).thenReturn(mockLiveDataListRecommendationWidget)
        Mockito.doNothing().`when`(mockViewModel).getRecommendationList(mockList, null)
        mockViewModel.getRecommendationList(mockList, null)
        verify(mockViewModel, Mockito.times(1)).getRecommendationList(mockList, null)
        assertNotNull(mockViewModel.recommendationListModel.value)
    }

    @Test
    fun loadErrorGetRecommendationWidget(){
        val mockList = mock(List::class.java) as List<String>
        `when`(mockLiveDataListRecommendationWidget.value).thenReturn(null)
        `when`(mockViewModel.recommendationListModel).thenReturn(mockLiveDataListRecommendationWidget)
        Mockito.doNothing().`when`(mockViewModel).getRecommendationList(mockList, null)
        mockViewModel.getRecommendationList(mockList, null)
        verify(mockViewModel, Mockito.times(1)).getRecommendationList(mockList, null)
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