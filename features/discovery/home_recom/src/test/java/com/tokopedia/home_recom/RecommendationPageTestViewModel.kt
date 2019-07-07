package com.tokopedia.home_recom

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.content.Context
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_recom.model.datamodel.ProductInfoDataModel
import com.tokopedia.home_recom.testViewModel.FakeRecommendationPageViewModel
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit

/**
 * Created by Lukas on 2019-07-04
 */
@RunWith(JUnit4::class)
class RecommendationPageTestViewModel {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var getRecommendationUseCase: GetRecommendationUseCase

    @Mock
    lateinit var graphqlRepository: GraphqlRepository

    @Mock
    lateinit var userSessionInterface: UserSessionInterface

    @Mock
    lateinit var dispatcher: CoroutineDispatcher

    private lateinit var recommendationPageViewModel : FakeRecommendationPageViewModel

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        recommendationPageViewModel = FakeRecommendationPageViewModel(graphqlRepository, userSessionInterface, getRecommendationUseCase, dispatcher)

    }

    @Test
    fun loadSuccessGetPrimaryProduct(){
        val observer = mock(Observer::class.java) as Observer<ProductInfoDataModel>
        recommendationPageViewModel.productInfoDataModel.observeForever(observer)
        recommendationPageViewModel.setReturnError(false)
        recommendationPageViewModel.getPrimaryProduct(context = context, productId = "")
        assertNotNull(recommendationPageViewModel.productInfoDataModel.value)
    }

    @Test
    fun loadErrorGetPrimaryProduct(){
        val observer = mock(Observer::class.java) as Observer<ProductInfoDataModel>
        recommendationPageViewModel.productInfoDataModel.observeForever(observer)
        recommendationPageViewModel.setReturnError(true)
        recommendationPageViewModel.getPrimaryProduct(context = context, productId = "")
        assertNull(recommendationPageViewModel.productInfoDataModel.value)
    }

    @Test
    fun loadSuccessGetRecommendationWidget(){
        val observer = mock(Observer::class.java) as Observer<List<RecommendationWidget>>
        recommendationPageViewModel.recommendationListModel.observeForever(observer)
        recommendationPageViewModel.setReturnError(false)
        recommendationPageViewModel.getRecommendationList(ArrayList()){
            fail()
        }
        assertNotNull(recommendationPageViewModel.recommendationListModel.value)
    }

    @Test
    fun loadErrorGetRecommendationWidget(){
        val observer = mock(Observer::class.java) as Observer<List<RecommendationWidget>>
        recommendationPageViewModel.recommendationListModel.observeForever(observer)
        recommendationPageViewModel.setReturnError(true)
        recommendationPageViewModel.getRecommendationList(ArrayList()){
            assertEquals(it, recommendationPageViewModel.errorMessage)
        }
        assertNull(recommendationPageViewModel.recommendationListModel.value)
    }
}