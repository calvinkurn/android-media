package com.tokopedia.home_recom

import android.content.Context
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_recom.testViewModel.FakeRecommendationPageViewModel
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

/**
 * Created by Lukas on 2019-07-04
 */
@RunWith(MockitoJUnitRunner::class)
class RecommendationPageTestViewModel {

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

    lateinit var recommendationPageViewModel : FakeRecommendationPageViewModel

    @Before
    fun setup(){
        recommendationPageViewModel = FakeRecommendationPageViewModel(graphqlRepository, userSessionInterface, getRecommendationUseCase, dispatcher)
    }

    @Test
    fun loadSuccessGetPrimaryProduct(){
        recommendationPageViewModel.setReturnError(false)
        recommendationPageViewModel.getPrimaryProduct(context = context, productId = "")
        assertThat(LiveDataTestUtil.getValue(recommendationPageViewModel.productInfoDataModel)).isNotNull
    }

    @Test
    fun loadErrorGetPrimaryProduct(){
        recommendationPageViewModel.setReturnError(true)
        recommendationPageViewModel.getPrimaryProduct(context = context, productId = "")
        assertThat(LiveDataTestUtil.getValue(recommendationPageViewModel.productInfoDataModel)).isNull()
    }

    @Test
    fun loadSuccessGetRecommendationWidget(){
        recommendationPageViewModel.setReturnError(false)
        recommendationPageViewModel.getRecommendationList(ArrayList()){
            fail(it)
        }
        assertThat(recommendationPageViewModel.recommendationListModel).isNotNull
    }

    @Test
    fun loadErrorGetRecommendationWidget(){
        recommendationPageViewModel.setReturnError(false)
        recommendationPageViewModel.getRecommendationList(ArrayList()){
            assertThat(it).isEqualToIgnoringCase(recommendationPageViewModel.errorMessage)
        }
        assertThat(recommendationPageViewModel.recommendationListModel).isNull()
    }
}